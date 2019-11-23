package webapp;

import database.dto.User;
import database.dto.Util.DtoUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

public class KeygenServlet extends HttpServlet {



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User loggedUser = DtoUtils.getLoggedUser(request);


        if(loggedUser == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession(false);
        String privateKey = (String)session.getAttribute("privateKey");

        if ("download".equals(request.getParameter("b"))) {

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
        User user = DtoUtils.getLoggedUser(request);
        if(user == null) {
            response.sendRedirect("./");
            //request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
//            AsyncCrypto asyncCrypto = new AsyncCrypto();
//            privateKey =asyncCrypto.PrivateKeyString();
//            publicKey = asyncCrypto.PublicKeyString();

            //CryptoUtils.setpublicKey(publicKey,privateKey);
            HttpSession session = request.getSession(false);
            String privateKey = (String)session.getAttribute("privateKey");
            request.setAttribute("privateKey", privateKey);
            request.setAttribute("publicKey", user.getPublicKey());
            request.getRequestDispatcher("/keygen.jsp").forward(request, response);


        } catch (Exception e) {
            e.printStackTrace();
        }





        request.getRequestDispatcher("/keygen.jsp").forward(request, response);
    }
}
