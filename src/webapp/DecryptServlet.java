package webapp;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

@WebServlet(name = "DecryptServlet")
public class DecryptServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("decrypt") != null) {
            String fileName = request.getParameter("fileToDecrypt");
            String fileNameSep = fileName.substring(0, 4);
            boolean badKey = true;
            boolean wrongFile = true;

            if (fileName != null && !fileName.isEmpty() && !"dec_".equals(fileNameSep) && "enc_".equals(fileNameSep)) {
                wrongFile = false;
                File file = new File(FileUploadHandler.UPLOAD_DIRECTORY + File.separator + fileName);
                String privateKey = request.getParameter("key");
                privateKey = privateKey != null && !privateKey.isEmpty() ? privateKey : CryptoUtils.getKeyFromFile(fileName);
                if (privateKey != null) {
                    AsyncCrypto ac = null;
                    try {
                        ac = new AsyncCrypto();
                        String s = CryptoUtils.getKeyFromFile(fileName);
//                        byte[] data = s.getBytes("UTF-8");
//                        byte[] data = (Base64.getDecoder().decode(s));
                        byte[] data = Base64.getDecoder().decode(s);

                        PrivateKey pk = ac.getPrivateKey(privateKey);

                        if (pk != null){
                            badKey = false;
                            String keySalt = ac.decrypt(data, pk);
                            if (keySalt != null){
                                fileName = fileName.substring(4, fileName.length());
                                File decFile = new File(FileUploadHandler.UPLOAD_DIRECTORY + File.separator + "dec_" + fileName);

                                if(CryptoUtils.decrypt(keySalt.substring(0, 16), keySalt.substring(16, 34), file, decFile)){
                                    file.delete();
                                    request.setAttribute("keymsg","Done. Integrity check OK.");
                                }
                                else {
                                    request.setAttribute("keymsg","Your file was modified.");
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        File[] files = new File(FileUploadHandler.UPLOAD_DIRECTORY).listFiles();

                        request.setAttribute("files", files);
                        request.getRequestDispatcher("/files.jsp").forward(request, response);

                    }
                }
            }
            else{

            }

            File[] files = new File(FileUploadHandler.UPLOAD_DIRECTORY).listFiles();
            if (badKey){
                request.setAttribute("keymsg","Please try another key.");
            }
            if (wrongFile){
                request.setAttribute("keymsg","Wrong file.");
            }

            request.setAttribute("files", files);
            request.getRequestDispatcher("/files.jsp").forward(request, response);
        }

        if (request.getParameter("download") != null) {
            String fileName = request.getParameter("fileToDecrypt");

            if (fileName != null && !fileName.isEmpty()) {
                File file = new File(FileUploadHandler.UPLOAD_DIRECTORY + File.separator + fileName);

                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
                OutputStream outFile = response.getOutputStream();
                FileInputStream in = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    outFile.write(buffer, 0, length);
                }
                in.close();
                outFile.flush();


            } else {
                File[] files = new File(FileUploadHandler.UPLOAD_DIRECTORY).listFiles();

                request.setAttribute("files", files);
                request.getRequestDispatcher("/files.jsp").forward(request, response);
            }

        }

        if (request.getParameter("delete") != null) {
            String fileName = request.getParameter("fileToDecrypt");

            if (fileName != null && !fileName.isEmpty()) {
                File file = new File(FileUploadHandler.UPLOAD_DIRECTORY + File.separator + fileName);

                file.delete();

            }
            File[] files = new File(FileUploadHandler.UPLOAD_DIRECTORY).listFiles();

            request.setAttribute("files", files);
            request.getRequestDispatcher("/files.jsp").forward(request, response);

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
