package webapp;

import database.Database;
import database.dto.User;
import database.dto.UserFileInfo;
import database.dto.Util.DtoUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import webapp.utils.AsyncCrypto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUploadHandler extends HttpServlet {
    public static String UPLOAD_DIRECTORY = "files";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = DtoUtils.getLoggedUser(request);
        if(loggedUser  == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        //process only if its multipart content

        if(ServletFileUpload.isMultipartContent(request)){
            File file = null;
            try {
                File fileDir = new File(UPLOAD_DIRECTORY);
                fileDir.mkdir();
                List<FileItem> multiparts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);
                String name;
                String fileName = "";
                String publicKey = "";
                String originalName = "";

                List<User> usersShare = new ArrayList<>();

                for(FileItem item : multiparts){
                  //  if(!item.isFormField()){
                        String fieldName = item.getFieldName();
                        if ("file".equals(fieldName)){
                            UserFileInfo userFileInfo = DtoUtils.getUserFileByName(Database.findUserFilesByUserId(loggedUser.getId()), item.getName());
                            name = userFileInfo != null ? new Date().getTime() + "_" +item.getName() : item.getName();
                            file = new File(UPLOAD_DIRECTORY +File.separator + name);
                            item.write(file);
                            fileName = file.getName();
                        }
                       if ("sharedUsers".equals(fieldName)){
                            item.getString();
                           usersShare.add(Database.findUserById(Long.parseLong(item.getString())));
                       }
                }
                //sifrovanie log usera
                AsyncCrypto.encUserFile(loggedUser,file,fileName, true);
                //sifrovanie zdielanych
                if (!usersShare.isEmpty()){
                    for (User u: usersShare) {
                        UserFileInfo userFileInfo = DtoUtils.getUserFileByName(Database.findUserFilesByUserId(loggedUser.getId()), file.getName());
                        AsyncCrypto.shareFile(loggedUser, u, userFileInfo);
                    }
                }

                request.setAttribute("keymsg", "test");
                //File uploaded successfully
                request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
                request.setAttribute("message", "File Upload Failed due to " + ex);
            } finally {

            }
        }else{
            request.setAttribute("message",
                    "Sorry this Servlet only handles file upload request");
        }
        List<UserFileInfo> userFiles = Database.findUserFilesByUserId(loggedUser .getId());
        request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
        request.getRequestDispatcher("/files.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedUser = DtoUtils.getLoggedUser(request);
        if(loggedUser  == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }
}



