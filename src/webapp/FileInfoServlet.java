package webapp;

import database.Database;
import database.dto.FileInfo;
import database.dto.Request;
import database.dto.User;
import database.dto.UserFileInfo;
import database.dto.Util.DtoUtils;
import webapp.utils.AsyncCrypto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileInfoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedUser = DtoUtils.getLoggedUser(request);
        if(loggedUser  == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        Long fileId = Long.valueOf((String)request.getParameter("fileId"));
        FileInfo file = Database.findFileInfoById(fileId);
        User fileOwner = Database.findFileOwner(fileId);

        if(request.getParameter("comment") != null) {
            String text = request.getParameter("commentText");
            Database.insertComment(fileId, loggedUser.getId(),text);
        }

        if(request.getParameter("acceptShareRequest") != null) {
            String[] names = request.getParameterValues("applicants");
            List<String> requestIds = Arrays.asList(names);
            for(String reqId : requestIds){
                Request req = Database.findRequestsById(Long.valueOf(reqId));
                UserFileInfo userFileInfo = Database.findUserFilesByUserIdAndFileId(req.getOwnerId(), req.getFileId());
                if(loggedUser.getId().equals(req.getOwnerId())) {

                    HttpSession session = request.getSession(false);
                    String privateKey = (String)session.getAttribute("privateKey");
                    AsyncCrypto.shareFile(loggedUser, req.getRequestUser(), userFileInfo,privateKey);
                    Database.deactivateRequest(req.getId());
                }
            }
        }
        if(request.getParameter("declineShareRequest") != null) {
            String[] names = request.getParameterValues("applicants");
            List<String> requestIds = Arrays.asList(names);
            for(String reqId : requestIds){
                Request req = Database.findRequestsById(Long.valueOf(reqId));
                if(loggedUser.getId().equals(req.getOwnerId())) {
                    Database.deactivateRequest(req.getId());
                }
            }
        }

        request.setAttribute("userFileInfo", Database.findUserFilesByUserIdAndFileId(loggedUser.getId(), fileId));
        request.setAttribute("fileOwner", fileOwner);
        if(fileOwner.getId().equals(loggedUser.getId())) {
            request.setAttribute("requests", Database.findRequestsByFileId(fileId));
        } else {
            request.setAttribute("userRequest", Database.findRequestsByFileIdAndUserId(fileId, loggedUser.getId()));
        }
        request.setAttribute("file", file);
        request.getRequestDispatcher("/fileinfo.jsp").forward(request, response);
    }
}
