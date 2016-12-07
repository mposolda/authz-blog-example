package org.keycloak.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.keycloak.AuthorizationContext;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.EntitlementRequest;
import org.keycloak.authorization.client.representation.EntitlementResponse;
import org.keycloak.authorization.client.representation.PermissionRequest;
import org.keycloak.authorization.client.representation.ResourceRepresentation;
import org.keycloak.authorization.client.representation.ScopeRepresentation;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.idm.authorization.Permission;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class Db {

    private List<Message> messages = new CopyOnWriteArrayList<>();

    public List<Message> getMessages(AuthzClient authzClient, String token) {
        List<Message> allMessages = new ArrayList<>(messages);

        List<String> permittedMessageResourceNames = getPermittedMessages(allMessages, authzClient, token);

        List<Message> allowedMessages = allMessages.stream().filter((Message m) -> {

            String resourceName = "blogpost:" + m.getText().toLowerCase() + ":" + m.getUsername();
            boolean hasPermission = permittedMessageResourceNames.contains(resourceName);

            System.err.println("Resource name: " + resourceName + ", has permission: " + hasPermission);
            return hasPermission;

        }).collect(Collectors.toList());

        return allowedMessages;
    }


    private List<String> getPermittedMessages(List<Message> allMessages, AuthzClient authzClient, String token) {
        List<String> permittedMessages = new ArrayList<>();

        List<PermissionRequest> permissionRequests = new ArrayList<>();
        for (Message m : allMessages) {
            PermissionRequest pr = new PermissionRequest();
            String resourceName = "blogpost:" + m.getText().toLowerCase() + ":" + m.getUsername();
            pr.setResourceSetName(resourceName);
            pr.setScopes(Collections.singleton("view-blogpost"));
            permissionRequests.add(pr);
        }

        EntitlementRequest req = new EntitlementRequest();
        req.setRpt(null);
        req.setPermissions(permissionRequests);
        try {
            EntitlementResponse response = authzClient.entitlement(token).get("authz-blog", req);
            String rptToken = response.getRpt();
            System.err.println("RPT Token: " + rptToken);

            TokenIntrospectionResponse requestingPartyToken = authzClient.protection().introspectRequestingPartyToken(rptToken);
            for (Permission permission : requestingPartyToken.getPermissions()) {
                if (permission.getScopes().contains("view-blogpost")) {
                    permittedMessages.add(permission.getResourceSetName());
                }
            }

            return permittedMessages;
        } catch (Exception ex) {
            // TODO: Better handling...
            System.err.println("No permissions to anything");
            return Collections.emptyList();
        }
    }

    public void add(String message, String username, AuthzClient authzClient) {
        Message record = new Message();
        record.setText(message);
        record.setUsername(username);
        messages.add(record);

        String resourceName = "blogpost:" + record.getText().toLowerCase() + ":" + record.getUsername();
        ResourceRepresentation rep = new ResourceRepresentation();

        rep.setName(resourceName);
        rep.setUri(resourceName);
        rep.setType("blogpost");
        rep.setOwner("john");
        rep.addScope(new ScopeRepresentation("view-blogpost"));
        authzClient.protection().resource().create(rep);
    }


    public static class Message {

        private String text;
        private String username;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
