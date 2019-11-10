package webapp;

import database.Database;
import database.dto.User;
import webapp.utils.AsyncCrypto;
import webapp.utils.Validators;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static webapp.utils.AsyncCrypto.HMAC_SHA256;

public class LoginServlet extends HttpServlet {
    private final String testUser = "admin";
    private final String testPass = "pass";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String isLogout = request.getParameter("logout");

        if (isLogout != null) {
            request.setAttribute("loggedOut", true);
            HttpSession session = request.getSession(false);
            session.invalidate();
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String isLogin = request.getParameter("login");
        String isRegister = request.getParameter("register");
        String isLogout = request.getParameter("logout");

        String username = request.getParameter("username");
        String email = request.getParameter("email");
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
            if (user != null) {
                String salt = user.getSalt();

                byte[] hashPassword = AsyncCrypto.hmacDigestBytes(password, salt, HMAC_SHA256);
                String hashPassordString = Base64.getEncoder().encodeToString(hashPassword);

                if (hashPassordString.equals(user.getPassword())) {
                    request.setAttribute("loggedIn", true);
                    HttpSession session = request.getSession();
                    session.setMaxInactiveInterval(45 * 60); //Zivotnost - 45 minut
                    session.setAttribute("loggedUser", user.getId());
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                } else {
                    request.setAttribute("loginError", "Incorrect username or password");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    return;
                }
            } else {
                request.setAttribute("loginError", "Incorrect username or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
        } else if (isRegister != null) {
            if (username != null && email != null && password != null) {

                // email validation
                String emailError = Validators.email(password);
                if (emailError != null) {
                    request.setAttribute("registerError", emailError);
                } else

                // password validation
                try {
                    String passError = Validators.password(password);
                    if (passError == null) {
                        // username validation
                        if (Database.findUserByName(username) != null) {
                            request.setAttribute("registerError", "Name <strong>" + username + "</strong> is already in use.");
                            request.getRequestDispatcher("/login.jsp").forward(request, response);
                            return;
                        } else {

                        }
                        Long userId = Database.insertUser(username, password, email);
                        User newUser = Database.findUserById(userId);
                        Database.insertUserKey(userId);
                        File userDir = new File(newUser.getDirectory());
                        userDir.mkdir();
                        request.setAttribute("registerComplete", true);
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                        return;
                    } else {
                        request.setAttribute("registerError", passError);
                        request.getRequestDispatcher("/login.jsp").forward(request, response);
                        return;
                    }
                } catch (Exception e) {
                    request.setAttribute("registerError", "General error");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    return;
                    // e.printStackTrace();
                }
            } else {
                request.setAttribute("registerError", "Missing values");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
        } else if (isLogout != null) {
            HttpSession session = request.getSession(false);
            session.invalidate();
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
