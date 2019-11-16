package webapp;

import database.Database;
import database.dto.User;
import database.dto.UserFileInfo;
import database.dto.Util.DtoUtils;
import webapp.utils.AsyncCrypto;
import webapp.utils.CryptoUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "DecryptServlet")
public class DecryptServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        User loggedUser = DtoUtils.getLoggedUser(request);

        if(loggedUser == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (request.getParameter("decrypt") != null) {
            String fileName = request.getParameter("fileToDecrypt");
            if("OfflineDec.jar".equals(fileName)) {
                List<UserFileInfo> userFiles = Database.findUserFilesByUserId(loggedUser.getId());
                request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
                request.setAttribute("keymsg", "You can not decrypt this file.");
                request.getRequestDispatcher("/files.jsp").forward(request, response);
            } else {
                String fileNameSep = fileName.substring(0, 4);
                boolean badKey = true;
                boolean wrongFile = true;

                if (fileName != null && !fileName.isEmpty()) {
                    wrongFile = false;
                    UserFileInfo fileInfo = DtoUtils.getUserFileByName(Database.findUserFilesByUserId(loggedUser.getId()), fileName);
                    //String privateKey = loggedUser.getPrivateKey();
                    HttpSession session = request.getSession(false);
                    String privateKey = (String)session.getAttribute("privateKey");


                    File decFile = null;
                    if (privateKey != null) {
                        AsyncCrypto ac = null;
                        try {
                            ac = new AsyncCrypto();
                            String s = fileInfo.getHashKey();
                            byte[] data = Base64.getDecoder().decode(s);

                            PrivateKey pk = ac.getPrivateKey(privateKey);

                            if (pk != null) {
                                badKey = false;
                                String keySalt = ac.decrypt(data, pk);
                                if (keySalt != null) {
                                    //  fileName = fileName.substring(4, fileName.length());
                                    decFile = new File(loggedUser.getDirectory() + File.separator + "dec_" + fileName);
                                    if (CryptoUtils.decrypt(keySalt.substring(0, 16), keySalt.substring(16, 34), fileInfo.getFileInfo().getFile(), decFile)) {
                                    //if (CryptoUtils.decrypt(pk, fileInfo.getFileInfo().getFile(), decFile)) {
                                        request.setAttribute("keymsg", "Done. Integrity check OK.");
                                    } else {
                                        request.setAttribute("keymsg", "Your file was modified.");
                                    }
                                }
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
                    OutputStream outFile = response.getOutputStream();
                    FileInputStream in = new FileInputStream(decFile);
                    byte[] buffer = new byte[4096];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        outFile.write(buffer, 0, length);
                    }
                    in.close();
                    outFile.flush();
                    decFile.delete();
                }
            }
        } else if (request.getParameter("download") != null) {
            String fileName = request.getParameter("fileToDecrypt");
            if (fileName != null && !fileName.isEmpty()) {
                File file = new File(loggedUser.getDirectory() + File.separator + fileName);

                /* hash to header */
                /*
                boolean test = true;
                if (test)
                {
                	SuborUtils.addStuff(file, file);
                }*/

                /* hash to header - end */


                FileInputStream in = new FileInputStream(file);
                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
                OutputStream outFile = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    outFile.write(buffer, 0, length);
                }
                in.close();
                outFile.flush();
            } else {
                List<UserFileInfo> userFiles = Database.findUserFilesByUserId(loggedUser.getId());
                request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
                request.getRequestDispatcher("/files.jsp").forward(request, response);
            }
        } else if (request.getParameter("delete") != null) {
            String fileName = request.getParameter("fileToDecrypt");
            if("OfflineDec.jar".equals(fileName)) {
                List<UserFileInfo> userFiles = Database.findUserFilesByUserId(loggedUser.getId());
                request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
                request.getRequestDispatcher("/files.jsp").forward(request, response);
                return;
            } else {
                if (fileName != null && !fileName.isEmpty()) {
                    UserFileInfo userFileInfo = DtoUtils.getUserFileByName(Database.findUserFilesByUserId(loggedUser.getId()), fileName);
                    if (userFileInfo.getOwnerFlag()){
                        Database.deleteFileInfoIDFromUserFile(userFileInfo.getFileInfoId());
                       // Database.DeleteRowFromUserFile(userFileInfo.getId());
                        Database.DeleteRowFromFileInfo(userFileInfo.getFileInfoId());
                        userFileInfo.getFileInfo().getFile().delete();
                    }
                   else {
                        Database.DeleteRowFromUserFile(userFileInfo.getId());
                    }

                }
                List<UserFileInfo> userFiles = Database.findUserFilesByUserId(loggedUser.getId());
                request.setAttribute("files", DtoUtils.getUserFiles(userFiles));
                request.getRequestDispatcher("/files.jsp").forward(request, response);
            }
        } else if (request.getParameter("shareRequest") != null) {
            Long fileId = Long.valueOf(request.getParameter("fileId"));
            Database.insertRequest(fileId, loggedUser.getId(), Database.findFileOwner(fileId).getId());
            /*request.setAttribute("fileId", fileId);
            request.setAttribute("fileName", request.getParameter("fileToDecrypt"));
            request.getRequestDispatcher("/fileInfo.jsp").forward(request, response);*/
            request.setAttribute("files", Database.getAllFileInfo());
            request.getRequestDispatcher("/files.jsp").forward(request, response);
        } else {
            request.setAttribute("files", Database.getAllFileInfo());
            request.getRequestDispatcher("/files.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Long UserID = (Long)session.getAttribute("loggedUser");

        if(UserID == null)
        {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
    }
}
