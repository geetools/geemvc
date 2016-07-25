<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://geetools.com/jsp/geemvc/form" prefix="f" %>
<%@ taglib uri="http://geetools.com/jsp/geemvc/html" prefix="h" %>

<h:use layout="/WEB-INF/jsp/layout/layout.jsp" pageTitle="Bug Tracker">

    <h:section name="nav-bar">

        <div class="row center-block">
            <div class="col-sm-4 col-xs-12 col-centered">
                <h:url value="/tickets"><h:message key="ticket.nav.backToTickets">Back to tickets</h:message></h:url>
            </div>
            <div class="col-sm-4 col-xs-12 col-centered">
                <h:url value="/tickets/edit/${ticket.id}"><h:message key="ticket.nav.editTicket">Edit this ticket</h:message></h:url>
            </div>
            <div class="col-sm-4 col-xs-12 col-centered">
                <h:url value="/tickets/new-ticket"><h:message key="ticket.nav.createNew">Create a new ticket</h:message></h:url>
            </div>
        </div>

    </h:section>

    <h:section name="content">

        <div class="bug-tracker-container">
            <div class="row center-block">
                <div class="col-xs-12">
                    <div class="row">
                        <h1 class="h1">${ticket.title}</h1>
                        <div class="col-xs-12 ticket-info">
                            <div class="row">
                                <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                                    <div class="col-xs-6">
                                        <span class="ticket-info-label"><h:message key="ticket.type">Type</h:message></span>
                                    </div>
                                    <div class="col-xs-6 type">
                                            ${ticket.type}
                                    </div>
                                </div>

                                <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                                    <div class="col-xs-6">
                                        <span class="ticket-info-label"><h:message key="ticket.status">Status</h:message></span>
                                    </div>
                                    <div class="col-xs-6 status">
                                            ${ticket.status}
                                    </div>
                                </div>

                                <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                                    <div class="col-xs-6">
                                        <span class="ticket-info-label"><h:message key="ticket.priority">Priority</h:message></span>
                                    </div>
                                    <div class="col-xs-6 priority">
                                            ${ticket.priority}
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                                    <div class="col-xs-6">
                                        <span class="ticket-info-label"><h:message key="ticket.assignne">Assignne</h:message></span>
                                    </div>
                                    <div class="col-xs-6">
                                            ${ticket.assignee.forename} ${ticket.assignee.surname}
                                    </div>
                                </div>

                                <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                                    <div class="col-xs-6">
                                        <span class="ticket-info-label"><h:message key="ticket.reporter">Reporter</h:message></span>
                                    </div>
                                    <div class="col-xs-6">
                                            ${ticket.reporter.forename} ${ticket.reporter.surname}
                                    </div>
                                </div>

                                <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                                    <div class="col-xs-6">
                                        <span class="ticket-info-label"><h:message key="ticket.createdOn">Created On</h:message></span>
                                    </div>
                                    <div class="col-xs-6">
                                            ${ticket.createdOn}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row comments">
                            <div class="col-xs-12">
                                <div class="ticket-comment-header">
                                    <span><h:message key="ticket.comment">Comments</h:message></span>
                                </div>
                                <div class="row comment">
                                    <div class="col-xs-12">
                                            <%--<a name="new-comment" ticket-id="${ticket.id}" class="btn btn-primary" href="#"><h:message key="ticket.comments.add">Add a comment</h:message></a>--%>
                                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addCommentModal">
                                            <h:message key="ticket.comment.add">Add a comment</h:message>
                                        </button>
                                    </div>
                                </div>
                                <c:forEach var="comment" items="${ticket.comments}">
                                    <div class="row comment">
                                        <div class="col-xs-12 header">
                                                ${comment.user.forename} ${comment.user.surname} ${comment.createdOn}
                                        </div>
                                        <div class="col-xs-12 content">
                                                ${comment.comment}
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <!-- Add Comment Modal -->
        <div class="modal fade" id="addCommentModal" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><h:message key="ticket.comments.add.header">New comment</h:message></h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-xs-12">
                                <label for="comment-content" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">
                                    <h:message key="comment.add.header">Comment</h:message>
                                </label>

                                <div class="col-xs-12">
                                    <textarea id="comment-content" class="form-control vresize" rows="3"></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-12">
                                <label for="comment-author" class="col-xs-12 control-label">
                                    <h:message key="comment.add.author">Author</h:message>
                                </label>
                                <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
                                    <select id="comment-author" class="form-control">
                                        <c:forEach items="${users}" var="user">
                                            <option value="${user.id}">${user.forename} ${user.surname}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal"><h:message key="ticket.comments.add.close">Close</h:message></button>
                        <button id="comment-save-btn" ticket-id="${ticket.id}" type="button" class="btn btn-primary"><h:message key="ticket.comments.add.save">Save changes</h:message></button>
                    </div>
                </div>
            </div>
        </div>

    </h:section>

</h:use>






