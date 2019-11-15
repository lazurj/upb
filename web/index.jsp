<%@ page import="database.Database" %><%--
  Created by IntelliJ IDEA.
  User: Rastik
  Date: 10/23/2019
  Time: 12:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>

    <script>
        function fileUploadHandler(fileUpload) {
            if (fileUpload && fileUpload.files && fileUpload.files[0]) {
                let fileName = document.getElementById("file-name");
                if (fileName) {
                    fileName.innerText = fileUpload.files[0].name;
                } else {
                    let fileLabel = document.getElementById("file-label");
                    fileLabel.innerHTML = fileLabel.innerHTML
                        + '<span id="file-name" class="file-name">' + fileUpload.files[0].name + '</span>';
                }
            }
        }
    </script>
</head>
<body>
<%@include file="navbar.jsp" %>
<section class="section">
    <div class="container">
        <section class="section">
            <div class="box">
                <h3 class="title">File upload:</h3>
                <form style="text-align: center" action="upload" method="post" enctype="multipart/form-data">
                    <div class="field">
                        <div class="file has-name is-fullwidth">
                            <label id="file-label clear-bottom-radius" class="file-label">
                                <input class="file-input" required type="file" name="file"
                                       onchange="fileUploadHandler(this);">
                                <span class="file-cta clear-bottom-radius">
                                <span class="file-icon">
                                <i class="fas fa-upload"></i>
                                </span>
                                <span class="file-label">
                                Choose a file…
                                </span>
                            </span>
                                <span id="file-name" class="file-name clear-bottom-radius"></span>
                            </label>
                        </div>
                    </div>
                    <%--                    <textarea class="textarea clear-radius" placeholder="Enter your pulbic key to encrypy selected file..."--%>
                    <%--                              name="publicKey"--%>
                    <%--                              required></textarea>--%>
                    <input type="hidden" name="user" value=""/>
                    <div id="userSelect" class="field">
                        <label class="label">Sharing file with others:</label>
                        <div class="control is-expanded">
                            <div class="select is-multiple is-fullwidth">
                                <select name="sharedUsers" multiple size="4">
                                    <%--Tu treba dat select zamestnancov a cez FOR ich ako option vypisovat--%>
                                    <%--Value - nastav ako ID pouzivatela--%>
                                    <%--Option - vypis meno (napr. nick/email)--%>

                                    <%  User loggedUser = DtoUtils.getLoggedUser(request);
                                        List<User> users = Database.findOtherUsers(loggedUser.getId());
                                        if(users != null){
                                        for (User u: users) {
                                            String u1name = u.getUserName();%>
                                        <option value = "<%=u.getId()%>"><%=u.getUserName()%></option><% }}%>
                                </select>
                            </div>
                        </div>
                        <p class="help"><i>Hold Ctrl to select mutliple</i></p>
                    </div>
                    <%--Nastav hodnotu VALUE na id pouzivatela--%>

                    <button class="button is-dark is-fullwidth clear-top-radius" value="upload">
                        <span class="icon is-small"><i class="fas fa-lock"></i></span>
                        <span>Upload file for encryption</span>
                    </button>
                </form>
            </div>
        </section>
    </div>
    <div class="container">
        <section class="section">
            <form action="files" method="post" enctype="multipart/form-data">
                <%--                <input class="button is-link is-fullwidth" type="submit" name="b1" value="showfiles"><br>--%>
                <button class="button is-link is-fullwidth" type="submit" name="b1" value="showfiles">Show files
                </button>
            </form>
        </section>
    </div>
</section>

</body>

</html>
