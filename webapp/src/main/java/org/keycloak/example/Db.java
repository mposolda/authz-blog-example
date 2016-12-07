package org.keycloak.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.keycloak.AuthorizationContext;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class Db {

    private List<Message> messages = new CopyOnWriteArrayList<>();

    public List<Message> getMessages(AuthorizationContext authzContext) {
        List<Message> allMessages = new ArrayList<>(messages);

        List<Message> allowedMessages = allMessages.stream().filter((Message m) -> {

            String resourceName = "blogpost:" + m.getText().toLowerCase() + ":" + m.getUsername();
            boolean hasPermission = authzContext.hasPermission(resourceName, "view-blogpost");

            System.err.println("Resource name: " + resourceName + ", has permission: " + hasPermission);
            return hasPermission;

        }).collect(Collectors.toList());

        return allowedMessages;
    }

    public void add(String message, String username) {
        Message record = new Message();
        record.setText(message);
        record.setUsername(username);
        messages.add(record);
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
