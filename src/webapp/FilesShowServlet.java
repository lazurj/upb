package webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "FilesShowServlet")
public class FilesShowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File[] files = new File(FileUploadHandler.UPLOAD_DIRECTORY).listFiles();

        request.setAttribute("files", files);
        request.getRequestDispatcher("/files.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File[] files = new File(FileUploadHandler.UPLOAD_DIRECTORY).listFiles();

        request.setAttribute("files", files);
        request.getRequestDispatcher("/files.jsp").forward(request, response);
    }
}
