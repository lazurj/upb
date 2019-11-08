package webapp;

import database.Database;
import database.dto.FileInfo;
import database.dto.User;
import database.dto.UserFileInfo;
import database.dto.UserKey;
import database.dto.Util.DtoUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import webapp.utils.AsyncCrypto;
import webapp.utils.CryptoUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import static webapp.utils.AsyncCrypto.HMAC_SHA256;

public class FileUploadHandler extends HttpServlet {
    public static String UPLOAD_DIRECTORY = "files";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = DtoUtils.getLoggedUser(request);
        if(loggedUser  == null) {
            response.sendRedirect("/login");
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
                for(FileItem item : multiparts){
                  //  if(!item.isFormField()){

                        String fieldName = item.getFieldName();

                        if ("file".equals(fieldName)){

                            name = item.getName();
                            file = new File(loggedUser.getDirectory() +File.separator + name);
                            item.write(file);
                            fileName = file.getName();

                        }
                       // item.write( new File(UPLOAD_DIRECTORY + File.separator + name));
                    //}
                }
                File encFile = new File (loggedUser.getDirectory() + File.separator +"enc_"+ fileName);
                //generovanie symetrickeho kluca
                String key = CryptoUtils.generateRandomKey(16);
                String salt = CryptoUtils.generateRandomKey(18);
                String fullKey = key + salt;
                //zasifrovanie sym kluca verejnym klucom
                AsyncCrypto asyncCrypto = new AsyncCrypto();
                UserKey userKey = Database.findMaxUserKeyByUserId(loggedUser.getId());
                byte[] encKey = asyncCrypto.encrypt(fullKey,asyncCrypto.getPublicKey(userKey.getPublicKey()));
                String encKeyValue = Base64.getEncoder().encodeToString(encKey);
//                String encKeyValue = new String(encKey, "UTF-8");
//                String encKeyValue = Base64.getDecoder().decode(encKey);
               // String encKeyValue = new String(encKey);
             //   encKeyValue =new String(encKey, StandardCharsets.UTF_8);

                Database.insertFile(encFile.getName(), "mac");
                FileInfo fileInfo = Database.findFileInfoByName(encFile.getName());
                Database.insertUserFile(loggedUser.getId(), fileInfo.getId(),userKey.getId(),encKeyValue );
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));

               // String content1 = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(file.getPath())));
                byte[] hashofFile = AsyncCrypto.hmacDigestBytes(content,"password",HMAC_SHA256);
                CryptoUtils.encrypt(key, salt, file, encFile, hashofFile);

                //String test1 = AsyncCrypto.hmacDigest("fooo","password",HMAC_SHA256);
                //byte[] test2 = AsyncCrypto.getSHA("foo");
                request.setAttribute("keymsg", encKeyValue);
                //File uploaded successfully
                request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
                request.setAttribute("message", "File Upload Failed due to " + ex);
            } finally {
                if(file != null) {
                    file.delete();
                }
            }
        }else{
            request.setAttribute("message",
                    "Sorry this Servlet only handles file upload request");
        }
        List<UserFileInfo> userFiles = Database.findUserFilesByUserId(loggedUser .getId());
        request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
        request.getRequestDispatcher("/files.jsp").forward(request, response);
    }
}