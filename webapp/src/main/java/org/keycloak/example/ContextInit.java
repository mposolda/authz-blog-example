package org.keycloak.example;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.util.JsonSerialization;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class ContextInit implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("db", new Db());

        try {
            InputStream is = servletContextEvent.getServletContext().getResourceAsStream("/WEB-INF/keycloak-authz-client.json");
            Configuration cfg = JsonSerialization.readValue(is, Configuration.class);
            AuthzClient authzClient = AuthzClient.create(cfg);
            servletContextEvent.getServletContext().setAttribute("authz-client", authzClient);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
