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
    <title>$Title$</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<section class="center" style="width: 50%; text-align: center">
    <h2>Folder list:</h2>
    <form action="/files" method="post" enctype="multipart/form-data">
        <input style="width: 60%;" type="submit" name="b1" value="showfiles"><br>
    </form>
</section>
<section class="center" style="width: 50%; text-align: center">
    <h2>Folder list:</h2>
    <a style="width: 60%;" class="link" href="/keygen">Generate me random private/public key pair</a>
</section>
<section class="center" style="width: 50%;text-align: center">
    <h2>File upload:</h2>
    <form style="text-align: center" action="upload" method="post" enctype="multipart/form-data">
        <div><span>File:</span> <input type="file" required name="file"/></div>
<%--        <div><span>Your public key:</span> <input style="max-width: 40%;" type="text" required name="publicKey"/></div>--%>
        <div>Your public key:</div>
        <textarea placeholder="Enter your pulbic key to encrypy selected file..." name="publicKey" required></textarea>
        <div><input style="width: 60%;" type="submit" value="upload"/></div>
    </form>
</section>


</body>

</html>
