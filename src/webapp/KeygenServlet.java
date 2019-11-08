package webapp;

import database.dto.User;
import database.dto.Util.DtoUtils;
import webapp.utils.AsyncCrypto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class KeygenServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(DtoUtils.getLoggedUser(request) == null) {
            response.sendRedirect("/login");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if ("download".equals(request.getParameter("b"))) {
            String privateKey = request.getParameter("privateKey");
            String publicKey = request.getParameter("publicKey");

            File keyFile = new File("key_generator.txt");
            FileWriter writer = new FileWriter(keyFile);
            StringBuilder sb = new StringBuilder();
            writer.write("<-Private Key->");
            writer.write(System.lineSeparator());
            writer.write(privateKey);
            writer.write(System.lineSeparator());
            writer.write("<-Public Key->");
            writer.write(System.lineSeparator());
            writer.write(publicKey);
            writer.close();

            response.setHeader("Content-disposition", "attachment; filename=" + keyFile.getName());
            OutputStream outFile = response.getOutputStream();
            FileInputStream in = new FileInputStream(keyFile);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                outFile.write(buffer, 0, length);
            }
            in.close();
            outFile.flush();
        }




    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(DtoUtils.getLoggedUser(request) == null) {
            response.sendRedirect("/login");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
//            AsyncCrypto asyncCrypto = new AsyncCrypto();
//            privateKey =asyncCrypto.PrivateKeyString();
//            publicKey = asyncCrypto.PublicKeyString();

            //CryptoUtils.setpublicKey(publicKey,privateKey);
            User user = DtoUtils.getLoggedUser(request);
            String privateKey = user.getPrivateKey();
            String publicKey = user.getPublicKey();
            request.setAttribute("privateKey", privateKey);
            request.setAttribute("publicKey", publicKey);
            request.getRequestDispatcher("/keygen.jsp").forward(request, response);


        } catch (Exception e) {
            e.printStackTrace();
        }





        request.getRequestDispatcher("/keygen.jsp").forward(request, response);
    }
}
