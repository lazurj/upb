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
    <title>Main</title>
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
                <table class="table is-striped is-hoverable is-fullwidth" style="border: 2px solid #dbdbdb">
                    <thead>
                    <th>Name</th>
                    <th>Length</th>
                    <th></th>
                    <th></th>
                    </thead>
                    <%-- Fetching the attributes of the request object
                         which was previously set by the servlet
                          "StudentServlet.java"
                    --%>
                    <%

                        String msg = (String) request.getAttribute("keymsg");
                        File[] files =
                                (File[]) request.getAttribute("files");

                        if (files != null && files.length > 0) {
                            for (File file : files) {%>
                    <tbody>
                    <td><%=file.getName()%>
                    </td>
                    <td><%=file.length()%>
                    </td>
                    <td><%="<button class=\"button\" onclick=\"document.getElementById('fileToDecrypt').value='" + file.getName() + "'\">Select<//button>"%>
                    </td>
                    <td><%="<form style=\"margin-block-end: 0;\" action=\"decrypt\" method=\"post\"><input type=\"hidden\" name=\"fileName\" value=\'" + file.getName() + "'><button class=\"button\" name= \"getHash\" type=\"submit\">Download hashkey<//button></form>"%>
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
                <form action="decrypt" method="post">
                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label">Selected folder:</label>
                        </div>
                        <div class="field-body">
                            <div class="field">
                                <input class="input" class="read-only" id="fileToDecrypt" type="text" required
                                       name="fileToDecrypt"/>
                            </div>
                        </div>
                    </div>

                    <div class="field is-horizontal">
                        <div class="field-label is-normal">
                            <label class="label is-normal">Decryption:</label>
                        </div>
                        <div class="field-body">
                            <div class="field">
                                <div class="control">
                                    <%--            <input type="text" name="key"/>--%>
                                    <textarea class="textarea clear-bottom-radius" style="height: 80px;"
                                              placeholder="Enter your private key to decrypy selected file..."
                                              name="key"></textarea>
                                </div>
                                <button class="button is-dark is-fullwidth clear-top-radius" type="submit" name="decrypt"
                                        value="Decrypt">
                                    <span class="icon is-small"><i class="fas fa-unlock-alt"></i></span>
                                    <span>Decrypt</span>
                                    file
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="field has-addons">
                        <p class="control" style="width: 100%;">
                            <button class="button is-dark is-fullwidth" type="submit" name="download"
                                    value="download">
                                <span class="icon is-small"><i class="fas fa-file-download"></i></span>
                                <span>Download</span>
                            </button>
                        </p>
                        <p class="control" style="width: 100%;">
                            <button class="button is-dark is-fullwidth" type="submit" name="delete"
                                    value="delete">
                                <span class="icon is-small"><i class="fas fa-trash-alt"></i></span>
                                <span>Delete</span>
                            </button>
                        </p>
                    </div>
                </form>
            </div>
            <a class="button is-link is-fullwidth" class="link" href="./">Return</a>
        </section>

    </div>
</section>
</body>
</html>