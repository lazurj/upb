package webapp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class FileInfoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("file", request.getParameter("file"));
        request.setAttribute("fileName", request.getParameter("fileName"));
        request.getRequestDispatcher("/fileinfo.jsp").forward(request, response);
    }
}
