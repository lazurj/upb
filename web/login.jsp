<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 31. 10. 2019
  Time: 18:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

    <script>
        function showLogin() {
            let loginForm = document.getElementById("loginForm");
            loginForm.setAttribute("style", "display: block;");

            let registerForm = document.getElementById("registerForm");
            registerForm.setAttribute("style", "display: none;");

            let loginTab = document.getElementById("loginTab");
            loginTab.classList.add("is-active");

            let registerTab = document.getElementById("registerTab");
            registerTab.classList.remove("is-active");
        }

        function showRegister() {
            let loginForm = document.getElementById("loginForm");
            loginForm.setAttribute("style", "display: none;");

            let registerForm = document.getElementById("registerForm");
            registerForm.setAttribute("style", "display: block;");

            let loginTab = document.getElementById("loginTab");
            loginTab.classList.remove("is-active");

            let registerTab = document.getElementById("registerTab");
            registerTab.classList.add("is-active");
        }
    </script>
</head>
<body>
<%@include file="navbar.jsp" %>

<%
    Boolean loggedIn = (Boolean) request.getAttribute("loggedIn");
    Boolean loggedOut = (Boolean) request.getAttribute("loggedOut");

    Boolean badCredentials = (Boolean) request.getAttribute("badCredentials");
%>
<section class="section">
    <div class="container">
        <%
            if (loggedIn == null) {
        %>
        <section class="section">
            <div class="box">

                <div class="tabs is-centered">
                    <ul>
                        <li id="loginTab" class="is-active" style="width: 30%;"><a onclick="showLogin()">Login</a></li>
                        <li id="registerTab" style="width: 30%;"><a onclick="showRegister()">Register</a></li>
                    </ul>
                </div>
                <form id="loginForm" action="login" method="post" name="loginForm">

                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label required">Username:</label>
                        </div>
                        <div class="field-body">
                            <div class="field">
                                <input class="input" type="text" required
                                       name="username"/>
                            </div>
                        </div>
                    </div>

                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label required">Password:</label>
                        </div>
                        <div class="field-body">
                            <div class="field">
                                <input class="input" type="password" required
                                       name="password"/>
                            </div>
                        </div>
                    </div>

                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label"></label>
                        </div>
                        <div class="field-body">
                            <button class="button is-link is-fullwidth" type="submit" name="login"
                                    value="delete">
                                <span class="icon is-small"><i class="fas fa-sign-in-alt"></i></span>
                                <span>Sign-in</span>
                            </button>
                        </div>
                    </div>

                    <%
                        if (badCredentials != null && badCredentials == true) {
                    %>
                    <article class="message is-danger">
                        <div class="message-body">
                            You have entered an invalid username or password.
                        </div>
                    </article>
                    <%
                        }
                    %>
                </form>

                <form id="registerForm" style="display: none;" action="login" method="post" name="registerForm">
                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label required">Username:</label>
                        </div>
                        <div class="field-body">
                            <div class="field">
                                <input class="input" type="text" required
                                       name="username"/>
                            </div>
                        </div>
                    </div>
                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label required">Email:</label>
                        </div>
                        <div class="field-body">
                            <div class="field">
                                <input class="input" type="text" required
                                       name="email"/>
                            </div>
                        </div>
                    </div>
                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label required">Password:</label>
                        </div>
                        <div class="field-body">
                            <div class="field">
                                <input class="input" type="password" required
                                       name="password"/>
                            </div>
                        </div>
                    </div>

                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label required">Repeat password:</label>
                        </div>
                        <div class="field-body">
                            <div class="field">
                                <input class="input" type="password" required
                                       name="password2"/>
                            </div>
                        </div>
                    </div>

                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label"></label>
                        </div>
                        <div class="field-body">
                            <button class="button is-link is-fullwidth" type="submit" name="register"
                                    value="register">
                                <span class="icon is-small"><i class="fas fa-user-check"></i></span>
                                <span>Register</span>
                            </button>
                        </div>
                    </div>
                </form>

            </div>
        </section>
        <%
        } else {
        %>
        <div class="message is-success">
            <div class="message-header">
                <p>Logged in</p>
            </div>
            <div class="message-body">
                You were successfully logged in.
            </div>
        </div>

        <a class="button is-link is-fullwidth" href="/index.jsp">Continue</a>
        <%
            }
        %>
    </div>
</section>

<%
    if (loggedOut != null && loggedOut == true) {
%>
<div class="message is-success toast">
    <div class="message-header">
        <p>Logged out</p>
        <button class="delete" aria-label="delete"></button>
    </div>
    <div class="message-body">
        You were successfully logged out.
    </div>
</div>
<%
    }
%>

</body>

<script>
    let setToastDisappear = (toast) => {
        let t = toast;
        setTimeout(_ => {
            t.classList.add('toast-fade');
            removeToast();
        }, 3000);
    }

    let removeToast = (toast) => {
        let t = toast;
        setTimeout(_ => {
            t.remove();
        }, 1000);
    }

    let toasts = document.getElementsByClassName("toast");
    for (let i = 0; i < toasts.length; i++) {
        console.log(toasts[i]);
        setToastDisappear(toasts[i]);
    }
</script>

</html>
