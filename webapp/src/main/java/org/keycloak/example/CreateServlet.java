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
public class CreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<html><head><title>Create blog</title></head><body>");

        if (req.getAttribute("success") != null) {
            writer.println("<b>Message saved successfully!</b>");
        }

        writer.println("<form action='" + req.getRequestURI() + "' method='POST'>");
        writer.println("Message text: <input name='text' /><br>");
        writer.println("Shared with user: <input name='shared-user' /><br>");
        writer.println("<input type='submit' value='Submit' />");
        writer.println("</form>");

        writer.println("</body></html>");
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String text = req.getParameter("text");
        String sharedUser = req.getParameter("shared-user");

        if (sharedUser == null || sharedUser.isEmpty()) {
            sharedUser = "";
        }

        Db db= Utils.getDb(getServletContext());
        db.add(text, sharedUser);

        req.setAttribute("success", true);

        doGet(req, resp);
    }

}
