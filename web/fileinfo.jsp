<%--
  Created by IntelliJ IDEA.
  User: kalan
  Date: 14. 11. 2019
  Time: 19:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="database.Database" %>
<%@ page import="database.dto.Comment" %>
<%@ page import="database.dto.FileInfo" %>
<%@ page import="database.dto.Request" %>
<%@ page import="database.dto.UserFileInfo" %>
<%@ page import="webapp.utils.TimeUtil" %>
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
    User owner = (User) request.getAttribute("fileOwner");
    Request userRequest = request.getAttribute("userRequest") != null ? (Request) request.getAttribute("userRequest") : null;
    List<Request> requests = request.getAttribute("requests") != null ? (List<Request>) request.getAttribute("requests") : null;
    UserFileInfo userFileInfo = request.getAttribute("userFileInfo") != null ? (UserFileInfo) request.getAttribute("userFileInfo") : null;
    FileInfo file = (FileInfo) request.getAttribute("file");
%>
<div class="cover"></div>
<section class="section">
    <div class="container">
        <section class="section">
            <div class="columns">
                <div class="column">
                    <div class="box">
                        <h2 class="title"><i class="fas fa-file-archive"></i><span
                                style="padding-left: 0.5em;"><%= file.getFileName()%></span>
                            <span
                                    style="padding-left: 0.8em;">(<%= owner.getUserName()%>)</span>
                        </h2>
                        <form action="decrypt" method="post">
                            <input type="hidden" id="fileToDecrypt"
                                    <%="value=\"" + file.getFileName() + "\""%>
                                   name="fileToDecrypt"/>
                            <input type="hidden" id="fileId"
                                    <%="value=\"" + file.getId().longValue() + "\""%>
                                   name="fileId"/>
                            <%--Zobraz vtedy ak vlastnim subor alebo mi ho niekto sharol--%>
                            <%
                                if(userFileInfo != null) {
                            %>
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
                            <%
                                }
                                if(userFileInfo == null && userRequest == null) {
                            %>
                            <div class="field">
                                <button class="button is-dark is-fullwidth" type="submit"
                                        name="shareRequest"
                                        value="shareRequest">
                                    <span class="icon is-small"><i class="fas fa-share-square"></i></span>
                                    <span>Request for sharing</span>
                                </button>
                            </div>
                            <%
                                }
                                if(userFileInfo == null && userRequest != null) {
                            %>
                            <div class="field">
                                <button class="button is-dark is-fullwidth" type="submit"
                                        name="shareRequest"
                                        disabled
                                        value="shareRequest">
                                    <span class="icon is-small"><i class="fas fa-share-square"></i></span>
                                    <span>Request for sharing</span>
                                </button>
                                <p class="help">You are waiting for <%= owner.getUserName()%> to accept your share request. Request sended: <%=TimeUtil.formatDate(userRequest.getCreateDate())%></p>
                            </div>
                            <%
                                }
                            %>
                            <%--END--%>
                        </form>
                    </div>
                </div>
                <%--Zobraz len ak vlastnim subor--%>
                <%
                    if(userFileInfo != null && Boolean.TRUE.equals(userFileInfo.getOwnerFlag())) {
                %>
                <div class="column is-one-quarter">
                    <div class="box">
                        <h2 class="subtitle">
                            Share requests
                        </h2>
                        <hr>
                        <%
                            if(requests != null && !requests.isEmpty()) {
                        %>
                        <form action="fileinfo" method="post">
                            <input type="hidden" id="fileId"
                                    <%="value=\"" + file.getId().longValue() + "\""%>
                                   name="fileId"/>
                            <%--FOR pre vsetky share requesty--%>
                            <%
                            for(Request req : requests) {
                            %>
                            <div class="field">
                                <div class="control">
                                    <label class="checkbox">
                                        <input type="checkbox" name="applicants" value="<%=req.getId().longValue()%>">
                                        <%=req.getRequestUser().getUserName()%>
                                    </label>
                                </div>
                            </div>
                            <%
                            }
                            %>
                            <%--END--%>
                            <button class="button is-dark is-halfwidth" type="submit" name="acceptShareRequest"
                                    value="acceptShareRequest">Accept
                            </button>
                            <button class="button is-dark is-halfwidth" type="submit" name="declineShareRequest"
                                    value="declineShareRequest">Decline
                            </button>
                        </form>
                    <%
                        }
                    %>
                    </div>

                </div>
                <%
                }
            %>
                <%--END--%>
            </div>

            <div class="box">
                <h2 class="subtitle">
                    Comment section
                </h2>
                <hr>

                <%--TOTO TU JE VZOR KOMENTARA--%>
                <%--FOR pre vsetky komentare pre dany subor--%>
                <%
                    List<Comment> commets = Database.findCommentByFileId(Long.valueOf(file.getId()));
                    for(Comment comment : commets) {
                %>
                <article class="message is-dark comment">
                    <div class="message-header">
                        <%--Meno autora komentara--%>
                        <p><%=comment.getUser().getUserName()%></p>
                        <%--END--%>

                        <%--datum napisania komentara--%>
                        <p>
                            <%=TimeUtil.formatDate(comment.getCreateDate())%>
                        </p>
                        <%--END--%>
                    </div>

                    <%--TELO spravy komentara (text, teda samotny komentar)--%>
                    <div class="message-body">
                        <%=comment.getText()%>
                    </div>
                    <%--END--%>
                </article>
                <%
                    }
                %>
                <%--END--%>

                <%--TODO--%>
                <%--Treba nastavit action pre formular--%>
                <form action="fileinfo" method="post">
                    <input type="hidden" id="fileId"
                            <%="value=\"" + file.getId().longValue() + "\""%>
                           name="fileId"/>
                    <textarea class="textarea" name="commentText" placeholder="Write your comment here..."></textarea>
                    <button class="button is-dark is-fullwidth clear-top-radius" name="comment" value="comment">
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
