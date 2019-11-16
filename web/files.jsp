<%@ page import="database.dto.FileInfo" %>
<%@ page import="java.io.File" %><%--
  Created by IntelliJ IDEA.
  User: Rastik
  Date: 10/22/2019
  Time: 11:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Files</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

</head>
<body>
<%@include file="navbar.jsp" %>
<section class="section">
    <div class="container">


        <section class="section">
            <div class="box">

                <h2 class="title">List of uploaded files</h2>

                <%--TODO--%>
                <%--Treba nastavit form action na Filter sluzbu--%>
                <form>
                    <div class="control">
                        <label class="radio">
                            <input type="radio" name="filter" checked>
                            My files
                        </label>
                        <label class="radio">
                            <input type="radio" name="filter">
                            Shared files
                        </label>
                        <label class="radio">
                            <input type="radio" name="filter">
                            All files
                        </label>
                    </div>
                    <div class="control">
                        <input class="input" type="text" placeholder="Search">
                    </div>
                    <button class="button is-dark is-fullwidth" type="submit" name="filterSubmit">
                        <span class="icon is-small"><i class="fas fa-search"></i></span>
                        <span>Search</span>
                    </button>
                </form>
                <hr>
                <table class="table is-striped is-hoverable is-fullwidth" style="border: 2px solid #dbdbdb">
                    <thead>
                    <th>Name</th>
                    <th>Length</th>
                    <th></th>
                    </thead>
                    <%
                        String msg = (String) request.getAttribute("keymsg");
                        List<FileInfo> files =
                                (List<FileInfo>) request.getAttribute("files");

                        if (files != null && files.size() > 0) {
                            for (FileInfo file : files) {%>
                    <tbody>
                    <td><i class="fas fa-file-archive"></i><span style="padding-left: 0.5em;"><%=file.getFileName()%></span>
                    </td>
                    <td><%=file.getFile().length()%>
                    </td>
                    <td>
                        <form action="fileinfo" method="post">
                            <input type="hidden" name="fileName" value=<%="\"" + file.getFileName() + "\""%>>
                            <input type="hidden" name="fileId" value=<%="\"" + file.getId() + "\""%>>
                            <button type="submit" class="button">Info</button>
                        </form>
                    </td>

                    <%
                        }
                    } else if (files != null) {%>
                    <td colspan="3" style="text-align: center"><strong>Folder is empty </strong></td>
                    <%
                        }
                    %>
                    </tbody>
                </table>

                <% if (msg != null && !msg.isEmpty()) {%>
                <strong><%=msg%>
                </strong><br>
                <%}%>
            </div>
            <a class="button is-link is-fullwidth" class="link" href="./upload">Return</a>
        </section>

    </div>
</section>
</body>
</html>