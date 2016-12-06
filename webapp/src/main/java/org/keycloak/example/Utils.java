package org.keycloak.example;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.AuthorizationContext;
import org.keycloak.KeycloakPrincipal;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class Utils {

    public static Db getDb(ServletContext context) {
        return (Db) context.getAttribute("db");
    }

    public static AuthorizationContext getAuthzContext(HttpServletRequest req) {
        KeycloakPrincipal kcPrincipal = (KeycloakPrincipal) req.getUserPrincipal();
        return kcPrincipal.getKeycloakSecurityContext().getAuthorizationContext();
    }
}
