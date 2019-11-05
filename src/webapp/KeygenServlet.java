package webapp;

import webapp.utils.AsyncCrypto;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class KeygenServlet extends HttpServlet {
    String privateKey = null;
    String publicKey = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/keygen.jsp").forward(request, response);
    }
}
