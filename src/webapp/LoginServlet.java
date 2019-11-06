package webapp;

import database.Database;
import database.dto.User;
import webapp.utils.AsyncCrypto;
import webapp.utils.CryptoUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import static webapp.utils.AsyncCrypto.HMAC_SHA256;

public class LoginServlet extends HttpServlet {
    private final String testUser = "admin";
    private final String testPass = "pass";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String isLogout = request.getParameter("logout");

        Database.createNewDatabase("upb.db");
        if (isLogout != null) {
            Cookie[] cookies = request.getCookies();
            Cookie loginCookie = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("username")) {
                        loginCookie = cookie;
                        break;
                    }
                }
            }
            if (loginCookie != null) {
                loginCookie.setMaxAge(0);
                response.addCookie(loginCookie);
                request.setAttribute("loggedOut", true);
            }
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String isLogin = request.getParameter("login");
        String isLogout = request.getParameter("logout");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (isLogin != null) {
//            if (testUser.equals(username) && testPass.equals(password)) {
//                Cookie loginCookie = new Cookie("username", username);
//                loginCookie.setMaxAge(45 * 60); //Zivotnost - 45 minut
//                response.addCookie(loginCookie);
//                request.setAttribute("loggedIn", true);
//                request.getRequestDispatcher("/login.jsp").forward(request, response);
//            } else {
//                request.setAttribute("badCredentials", true);
//                User user = Database.findUserByName(username);
//                user.getId();
//            }

            User user = Database.findUserByName(username);
            if (user != null){
               String salt = user.getSalt();

                byte[] hashPassword = AsyncCrypto.hmacDigestBytes(password,salt,HMAC_SHA256);
                String hashPassordString = Base64.getEncoder().encodeToString(hashPassword);

                if (hashPassordString.equals(user.getPassword())){
                    System.out.println("yes");
                }
            }






        } else {
            response.sendRedirect("/login");

        }

        if (isLogout != null) {

        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    private boolean isLogedIn(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    return true;
                }
            }
        }
        return false;
    }

}
