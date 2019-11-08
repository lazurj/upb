package webapp;

import database.Database;
import database.dto.User;
import database.dto.UserFileInfo;
import database.dto.Util.DtoUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FilesShowServlet")
public class FilesShowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedUser = DtoUtils.getLoggedUser(request);
        if(loggedUser  == null) {
            response.sendRedirect("/login");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        List<UserFileInfo> userFiles = Database.findUserFilesByUserId(loggedUser.getId());
        request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
        request.getRequestDispatcher("/files.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedUser = DtoUtils.getLoggedUser(request);
        if(loggedUser  == null) {
            response.sendRedirect("/login");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        List<UserFileInfo> userFiles = Database.findUserFilesByUserId(loggedUser .getId());
        request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
        request.getRequestDispatcher("/files.jsp").forward(request, response);

    }
}
