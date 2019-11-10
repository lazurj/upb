<%@ page import="webapp.utils.AsyncCrypto" %>
<%@ page import="java.security.NoSuchAlgorithmException" %>
<%@ page import="webapp.utils.CryptoUtils" %><%--
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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>
</head>
<body>

<%
    /*
    String privateKey = null;
    String publicKey = null;
    try {
        AsyncCrypto asyncCrypto = new AsyncCrypto();
        privateKey =asyncCrypto.PrivateKeyString();
        publicKey = asyncCrypto.PublicKeyString();


    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }*/

    String privateKey = (String) request.getAttribute("privateKey");
    String publicKey = (String) request.getAttribute("publicKey");
%>
<%@include file="navbar.jsp" %>
<section class="section">
    <div class="container">
        <section class="section">
            <div class="card">
                <header class="card-header">
                    <p class="card-header-title">
                        Key generator
                    </p>
                </header>
                <div class="card-content">
                    <div class="content">
                        <h4 class="subtitle is-4">
                            Private key:
                        </h4>
                        <pre><%=privateKey%></pre>
                        <br>
                        <h4 class="subtitle is-4">
                            Public key:
                        </h4>
                        <pre><%=publicKey%></pre>
                    </div>
                </div>
                <footer class="card-footer">
                    <button form="submitForm" class="card-footer-item button" name="b"
                           type="submit" value="generate">
                    <span class="icon">
                        <i class="fa fa-refresh"></i>
                    </span>
                        <span>Generate new key</span>
                    </button>
                    <button form="submitForm" class="card-footer-item button" name="b"
                            type="submit" value="download">
                    <span class="icon">
                        <i class="fas fa-file-download"></i>
                    </span>
                        <span>Download</span>
                    </button>
                </footer>
            </div>
            <a class="button is-link is-fullwidth" href="./upload">Return</a>
        </section>
    </div>
</section>

<form id="submitForm" class="center" action="keygen" method="post">
    <input type="hidden" name="privateKey" value="<%=privateKey%>">
    <input type="hidden" name="publicKey" value="<%=publicKey%>">
</form>


</body>
</html>
