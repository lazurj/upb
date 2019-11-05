<%@ page import="webapp.utils.AsyncCrypto" %>
<%@ page import="java.security.NoSuchAlgorithmException" %><%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 26. 10. 2019
  Time: 15:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Keygen</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>

<%
    String privateKey = null;
    String publicKey = null;
    try {
        AsyncCrypto asyncCrypto = new AsyncCrypto();
        privateKey =asyncCrypto.PrivateKeyString();
        publicKey = asyncCrypto.PublicKeyString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
%>

<section style="margin: 2em auto; width: 50%; min-width: 500px;">
    <div class="section">
        <h1>
            Key generator
        </h1>
        <hr>
        <h2>
            Private key:
        </h2>
        <pre><%=privateKey%></pre>
        <h2>
            Public key:
        </h2>
        <pre><%=publicKey%></pre>
    </div>
</section>

<div style="background: #b9c9fe; vertical-align: middle; padding: 1em; border-bottom: 4px solid #aabcfe;">
    <a style="background: rgb(221, 221, 221); width: 180px;" class="link" href="/">Return</a>
</div>
</body>
</html>
