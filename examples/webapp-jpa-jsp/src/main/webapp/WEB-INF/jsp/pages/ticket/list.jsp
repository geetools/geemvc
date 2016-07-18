<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.geetools.org/jsp/geemvc/form" prefix="f" %>
<%@ taglib uri="http://www.geetools.org/jsp/geemvc/html" prefix="h" %>

<h:use layout="/WEB-INF/jsp/layout/layout.jsp" pageTitle="Bug Tracker">

    <h:section name="content">

        <div class="bug-tracker-container">
            <div class="row center-block">
                <h1 class="h1"><h:message key="ticket.tickets">Tickets</h:message></h1>

                <div class="row">
                    <div class="col-xs-12">
                        <div class="col-xs-12">
                            <c:forEach var="ticket" items="${tickets}">
                                <div class="row ticket-info" ticket-id="${ticket.id}">
                                    <div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 link">
                                        <h:url value="/tickets/details/${ticket.id}">${ticket.title}</h:url>
                                    </div>

                                    <div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 type">
                                            ${ticket.type}
                                    </div>

                                    <div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 status">
                                            ${ticket.status}
                                    </div>

                                    <div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 assignee">
                                            ${ticket.assignee.forename} ${ticket.assignee.surname}
                                    </div>

                                    <div class="col-xs-12 col-sm-2 col-md-2 col-lg-2 reporter">
                                            ${ticket.reporter.forename} ${ticket.reporter.surname}
                                    </div>

                                    <div class="col-xs-12 col-sm-1 col-md-1 col-lg-1 created">
                                            ${ticket.createdOn}
                                    </div>

                                    <div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 text-right">
                                        <a name="ticket-delete" ticket-id="${ticket.id}" href="#" title="<h:message key='ticket.remove'>Remove ticket</h:message>">
                                            <span class="glyphicon glyphicon-remove"></span></a>
                                    </div>

                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
                        <h:url class="btn btn-primary" value="/tickets/new-ticket"><h:message key="ticket.update.cancel">Create new ticket</h:message></h:url>
                    </div>
                </div>
            </div>
        </div>
    </h:section>


</h:use>


