package org.keycloak.example;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class ViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<html><head><title>View blogs</title></head><body>");

        writer.println("<table border><tr><th>Message</th><th>Shared with user</th></tr>");

        Db db = Utils.getDb(getServletContext());

        for (Db.Message message : db.getMessages()) {
            writer.println("<tr><td>" + message.getText() + "</td><td>" + message.getUsername() + "</td></tr>");
        }

        writer.println("</body></html>");
        writer.flush();
    }

}
