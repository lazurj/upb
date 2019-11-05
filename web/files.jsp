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
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="styles.css">

</head>
<body>

<div class="center" style="width: 50%; text-align: center">
    <h2>List of uploaded files</h2>
    <table>
        <thead>
        <th><b>Name</b></th>
        <th><b>Lenght</b></th>
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
        <td><%="<button onclick=\"document.getElementById('fileToDecrypt').value='" + file.getName() + "'\">Select<//button>"%>
        </td>

        <%
                }
            } else if(files != null) {%>
        <td colspan="3" style="text-align: center"><strong>Folder is empty </strong></td>
        <%
                }
        %>
        </tbody>

        <% if (msg != null && !msg.isEmpty()) {%>
         <strong><%=msg%>
    </strong><br>
        <%}%>
    </table>
</div>
<form style="width: 50%;" class="center" action="/decrypt" method="post">
    <hr>

    <div class="center" style="text-align: center; margin: 1em;">
        <span><strong>Selected folder:</strong></span>
        <input style="width: 50%;" class="read-only" id="fileToDecrypt" type="text" required name="fileToDecrypt"/><br>
    </div>
    <div style="display: flex">
        <div style="display: inline-block; width: 50%; text-align: center">
            <h3>Decryption:</h3>
            <div>
                <%--            <input type="text" name="key"/>--%>
                <textarea style="height: 80px;" placeholder="Enter your private key to decrypy selected file..."
                          name="key"></textarea></div>
            <div><input style="width: 60%;" type="submit" name="decrypt" value="Decrypt"></div>
        </div>
        <div style="display: inline-block; width: 50%;">
            <h3>Options:</h3>
            <div style="display: flex">
                <input style="width: 100%" type="submit" name="download" value="download">
                <input style="width: 100%" type="submit" name="delete" value="delete">
            </div>
        </div>
    </div>

</form>

<div style="background: #b9c9fe; vertical-align: middle; padding: 1em; border-bottom: 4px solid #aabcfe;">
    <a style="background: rgb(221, 221, 221); width: 180px;" class="link" href="/">Return</a>
</div>

<br>
</body>
</html>
