package webapp;

import database.Database;
import database.dto.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            if (testUser.equals(username) && testPass.equals(password)) {
                Cookie loginCookie = new Cookie("username", username);
                loginCookie.setMaxAge(45 * 60); //Zivotnost - 45 minut
                response.addCookie(loginCookie);
                request.setAttribute("loggedIn", true);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                request.setAttribute("badCredentials", true);
                User user = Database.findUserByName(username);
                user.getId();
            }
        } else {
//            response.sendRedirect("/index");

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
