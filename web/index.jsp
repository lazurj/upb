<%--
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
                    <div class="file has-name is-fullwidth">
                        <label id="file-label clear-bottom-radius" class="file-label">
                            <input class="file-input" required type="file" name="publicKey"
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
                    <textarea class="textarea clear-radius" placeholder="Enter your pulbic key to encrypy selected file..."
                              name="publicKey"
                              required></textarea>
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
            <a class="button is-link is-fullwidth" href="keygen">Generate me random private/public key pair</a>
        </section>
    </div>
</section>

</body>

</html>