<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %><%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 31. 10. 2019
  Time: 11:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String url = request.getRequestURL().toString();
    String[] parsedUrl = url.split("/");
    List<String> listUrl = Arrays.asList(parsedUrl);
//    if (listUrl.contains("") || listUrl.contains("index.jsp")) {


%>
<form id="showFileForm" style="display: none;" action="files" method="post" enctype="multipart/form-data">
    <%--                <input class="button is-link is-fullwidth" type="submit" name="b1" value="showfiles"><br>--%>

</form>

<form id="logoutForm" style="display: none;" action="login" method="post"></form>

<nav class="navbar is-link">
    <div class="navbar-brand">
        <a role="button" class="navbar-burger burger" aria-label="menu" aria-expanded="false" data-target="navbarMenu">
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
        </a>
    </div>
    <div id="navbarMenu" class="navbar-menu">
        <div class="navbar-start">
            <a href="/" class="navbar-item">
                Home
            </a>

            <%--            <a class="navbar-item"><button form="showFileForm" class="button is-text" type="submit" name="b1" value="showfiles">Show files</button></a>--%>
            <a href="files" class="navbar-item">
                File
            </a>
            <%--            <button form="showFileForm" class="navbar-item" type="submit" name="b1" value="showfiles">Show files</button>--%>
            <a href="keygen" class="navbar-item">
                Key generator
            </a>
        </div>
        <div class="navbar-end">
            <div class="buttons">
                <%--                <span>Logged as: <b>John Snow</b></span>--%>
                <%
                    String username = null;
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals("username"))
                                username = cookie.getValue();
                        }
                    }
//                    if(username == null) response.sendRedirect("login");
                %>
                <% if (username != null) {
                %>
                    <span>Logged as: <b><%=username%></b></span>
<%--                <div class="navbar-item">--%>
<%--                    <span><%=username%></span>--%>
<%--                </div>--%>
                <div class="navbar-item">
                    <a href="login?logout" class="button is-light">
                        Logout
                    </a>
                </div>
                <% } else { %>
                <div class="navbar-item">
                    <a href="login" class="button is-light">
                        Login
                    </a>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</nav>