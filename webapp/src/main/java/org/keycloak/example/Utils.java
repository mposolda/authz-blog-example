package org.keycloak.example;

import javax.servlet.ServletContext;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class Utils {

    public static Db getDb(ServletContext context) {
        return (Db) context.getAttribute("db");
    }
}
