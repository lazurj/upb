package webapp;

import database.Database;
import database.dto.FileInfo;
import database.dto.User;
import database.dto.Util.DtoUtils;
import webapp.utils.SearchUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FilesShowServlet")
public class FilesShowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedUser = DtoUtils.getLoggedUser(request);
        List<FileInfo> result = new ArrayList<FileInfo>();
        if(loggedUser  == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if(request.getParameter("filterSubmit") != null) {
            //String[] names = request.getParameterValues("filter");
            //List<String> filterVal = Arrays.asList(names);
            String filter = request.getParameter("filter");
            String searchText = request.getParameter("searchText");
            List<FileInfo> files = null;
            switch (filter) {
                case "all" :
                    files = Database.getAllFileInfo();
                    break;
                case  "my" :
                    files = Database.getFilesByOwnerFlag(loggedUser.getId(), true);
                    break;
                case "shared" :
                    files = Database.getFilesByOwnerFlag(loggedUser.getId(), false);
                    break;
            }

            if(searchText != null && !searchText.isEmpty() && files != null) {
                searchText = SearchUtil.normalizeText(searchText.toLowerCase());
                for (FileInfo fi : files) {
                    if(SearchUtil.findTextInFileContent(fi, searchText)) {
                        result.add(fi);
                    }
                }
            } else if (files != null) {
                result.addAll(files);
            }
        } else {
            result = Database.getFilesByOwnerFlag(loggedUser.getId(), true);
        }

        request.setAttribute("files", result);
        request.setAttribute("filter", request.getParameter("filter"));
        request.setAttribute("searchText", request.getParameter("searchText"));
        request.getRequestDispatcher("/files.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedUser = DtoUtils.getLoggedUser(request);
        if(loggedUser  == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        request.setAttribute("files", Database.getFilesByOwnerFlag(loggedUser.getId(), true));
        request.getRequestDispatcher("/files.jsp").forward(request, response);
    }
}
