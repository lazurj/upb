package webapp;

import database.Database;
import database.dto.UserFileInfo;
import database.dto.Util.DtoUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FilesShowServlet")
public class FilesShowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<UserFileInfo> userFiles = Database.findUserFilesByUserId(Long.valueOf(2));
        request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
        request.getRequestDispatcher("/files.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Long UserID = (Long)session.getAttribute("loggedUser");

        if(UserID == null)
        {
            response.sendRedirect("/login");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        File[] files = new File(FileUploadHandler.UPLOAD_DIRECTORY).listFiles();

        request.setAttribute("files", files);
        request.getRequestDispatcher("/files.jsp").forward(request, response);
    }
}
