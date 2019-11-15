<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 14. 11. 2019
  Time: 19:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.io.File" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File Info</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.8.0/css/bulma.min.css">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <script defer src="https://use.fontawesome.com/releases/v5.3.1/js/all.js"></script>
</head>
<body>
<%@include file="navbar.jsp" %>
<%
    String fileName = (String) request.getAttribute("fileName");
    File file = new File(fileName);
%>
<div class="cover"></div>
<section class="section">
    <div class="container">
        <section class="section">
            <div class="columns">
                <div class="column">
                    <div class="box">
                        <h2 class="title"><i class="fas fa-file-archive"></i><span
                                style="padding-left: 0.5em;"><%= file.getName()%></span>
                        </h2>
                        <form action="decrypt" method="post">
                            <input type="hidden" id="fileToDecrypt"
                                    <%="value=\"" + file.getName() + "\""%>
                                   name="fileToDecrypt"/>
                            <%--Zobraz vtedy ak vlastnim subor alebo mi ho niekto sharol--%>
                            <div class="field">
                                <button class="button is-dark is-fullwidth" type="submit"
                                        name="decrypt"
                                        value="Decrypt">
                                    <span class="icon is-small"><i class="fas fa-unlock-alt"></i></span>
                                    <span>Decrypt file</span>
                                </button>
                            </div>

                            <div class="field">
                                <button class="button is-dark is-fullwidth" type="submit"
                                        name="download"
                                        value="download">
                                    <span class="icon is-small"><i class="fas fa-file-download"></i></span>
                                    <span>Download</span>
                                </button>
                            </div>

                            <div class="field">
                                <button class="button is-dark is-fullwidth" type="submit"
                                        name="delete"
                                        value="delete">
                            <span class="icon is-small"><i
                                    class="fas fa-trash-alt"></i></span>
                                    <span>Delete</span>
                                </button>
                            </div>
                            <%--END--%>
                            <%--Zobraz len vtedy ak nevlastnim subor a este som si nepoziadal o share request--%>
                            <div class="field">
                                <button class="button is-dark is-fullwidth" type="submit"
                                        name="shareRequest"
                                        value="shareRequest">
                                    <span class="icon is-small"><i class="fas fa-share-square"></i></span>
                                    <span>Request for sharing</span>
                                </button>
                            </div>
                            <%--END--%>
                            <%--Zobraz len vtedy ak nevlastnim subor a este som si nepoziadal o share request--%>
                            <div class="field">
                                <button class="button is-dark is-fullwidth" type="submit"
                                        name="shareRequest"
                                        disabled
                                        value="shareRequest">
                                    <span class="icon is-small"><i class="fas fa-share-square"></i></span>
                                    <span>Request for sharing</span>
                                </button>
                                <p class="help">You are waiting for owner to accept share request...</p>
                            </div>
                            <%--END--%>
                        </form>
                    </div>
                </div>
                <%--Zobraz len ak vlastnim subor--%>
                <div class="column is-one-quarter">
                    <div class="box">
                        <h2 class="subtitle">
                            Share requests
                        </h2>
                        <hr>

                        <%--TODO--%>
                        <%--Dopln action pre form--%>
                        <form>
                            <%--FOR pre vsetky share requesty--%>
                            <div class="field">
                                <div class="control">
                                    <label class="checkbox">
                                        <input type="checkbox">
                                        Rastikm
                                    </label>
                                </div>
                            </div>
                            <div class="field">
                                <div class="control">
                                    <label class="checkbox">
                                        <input type="checkbox">
                                        Jakub
                                    </label>
                                </div>
                            </div>
                            <%--END--%>

                            <button class="button is-dark is-fullwidth" type="submit" name="acceptShareRequest"
                                    value="acceptShareRequest">Share this file
                            </button>
                        </form>
                    </div>

                </div>
                <%--END--%>
            </div>

            <div class="box">
                <h2 class="subtitle">
                    Comment section
                </h2>
                <hr>

                <%--TOTO TU JE VZOR KOMENTARA--%>
                <%--FOR pre vsetky komentare pre dany subor--%>
                <article class="message is-dark comment">
                    <div class="message-header">
                        <%--Meno autora komentara--%>
                        <p>Admin</p>
                        <%--END--%>

                        <%--datum napisania komentara--%>
                        <p>
                            (14.11.2019 21:28)
                        </p>
                        <%--END--%>
                    </div>

                    <%--TELO spravy komentara (text, teda samotny komentar)--%>
                    <div class="message-body">
                        Python file... What a looser
                    </div>
                    <%--END--%>
                </article>
                <%--END--%>

                <%--TODO--%>
                <%--Treba nastavit action pre formular--%>
                <form>
                    <textarea class="textarea" placeholder="Write your comment here..."></textarea>
                    <button class="button is-dark is-fullwidth clear-top-radius" value="upload">
                        <span>Send comment</span>
                    </button>
                </form>
                <%--END--%>
            </div>

        </section>
    </div>
</section>

</body>
</html>
