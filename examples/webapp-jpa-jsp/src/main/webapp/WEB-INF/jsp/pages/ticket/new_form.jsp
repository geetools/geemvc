<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.geetools.org/jsp/geemvc/form" prefix="f" %>
<%@ taglib uri="http://www.geetools.org/jsp/geemvc/html" prefix="h" %>


<h:use layout="/WEB-INF/jsp/layout/layout.jsp" pageTitle="Bug Tracker">

	<h:section name="nav-bar">
		<div class="row center-block">
			<div class="col-sm-4 col-xs-12 col-centered">
				<h:url value="/tickets"><h:message key="ticket.nav.backToTickets">Back to tickets</h:message></h:url>
			</div>
		</div>
	</h:section>

	<h:section name="content">

		<div class="bug-tracker-container">
			<div class="row center-block">
				<div class="col-xs-12">
					<div class="row">
						<h1 class="h1"><h:message key="ticket.new.header">Create new ticket</h:message></h1>
						<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">

							<f:form action="/tickets/create-ticket" method="post" class="form-horizontal">

								<div class="form-group">
									<div class="row">
										<div class="col-xs-12">
											<label for="ticket.title" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">
												<h:message key="ticket.title">Title</h:message>
											</label>

											<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
												<f:text name="ticket.title" class="form-control"/>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="row">
										<div class="col-xs-12">
											<label for="ticket.description" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">
												<h:message key="ticket.description">Description</h:message>
											</label>

											<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
												<f:textarea name="ticket.description" class="form-control"/>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="row">
										<div class="col-xs-12">
											<label for="ticket.type" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">
												<h:message key="ticket.type">Type</h:message>
											</label>

											<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
												<div id="ticket-type">
													<f:select name="ticket.type" class="form-control">
														<f:option value="BUG"><h:message key="ticket.type.bug">Bug</h:message></f:option>
														<f:option value="FEATURE"><h:message key="ticket.type.feature">Feature</h:message></f:option>
														<f:option value="TASK"><h:message key="ticket.type.task">Task</h:message></f:option>
													</f:select>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="row">
										<div class="col-xs-12">
											<label for="ticket.status" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">
												<h:message key="ticket.status">Status</h:message>
											</label>

											<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
												<div id="ticket-status">
													<f:select name="ticket.status" class="form-control">
														<f:option value="NEW"><h:message key="ticket.status.new">New</h:message></f:option>
														<f:option value="IN_PROGRESS"><h:message key="ticket.status.in_progress">In Progress</h:message></f:option>
														<f:option value="CLOSED"><h:message key="ticket.status.closed">Closed</h:message></f:option>
													</f:select>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="row">
										<div class="col-xs-12">
											<label for="ticket.status" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">
												<h:message key="ticket.type.priority">Priority</h:message>
											</label>

											<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
												<div id="ticket-priority">
													<f:select name="ticket.priority" class="form-control">
														<f:option value="HIGHEST"><h:message key="ticket.priority.highest">Highest</h:message></f:option>
														<f:option value="HIGH"><h:message key="ticket.priority.high">High</h:message></f:option>
														<f:option value="MEDIUM"><h:message key="ticket.priority.medium">Medium</h:message></f:option>
														<f:option value="LOW"><h:message key="ticket.priority.low">Low</h:message></f:option>
														<f:option value="LOWEST"><h:message key="ticket.priority.lowest">Lowest</h:message></f:option>
													</f:select>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="row">
										<div class="col-xs-12">
											<label for="ticket-assignee" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">
												<h:message key="ticket.assignee">Assignee</h:message>
											</label>

											<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
												<div id="ticket-assignee">
													<f:select name="ticket.assignee.id" class="form-control">
														<f:options values="users" value="id" label="name"/>
													</f:select>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="row">
										<div class="col-xs-12">
											<label for="ticket-reporter" class="col-xs-12 col-sm-6 col-md-6 col-lg-6 control-label">
												<h:message key="ticket.reporter">Reporter</h:message>
											</label>

											<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
												<div id="ticket-reporter">
													<f:select name="ticket.reporter.id" class="form-control">
														<f:options values="users" value="id" label="name"/>
													</f:select>
												</div>
											</div>
										</div>
									</div>
								</div>

								<div class="form-group">
									<div class="row">
										<div class="col-xs-12">
											<div class="col-xs-6"></div>
											<div class="col-xs-12 col-sm-6 col-md-6 col-lg-6">
												<f:button name="update.save" class="btn btn-primary" id="ticket-update-save">
													<h:message key="ticket.update.save">Save</h:message>
												</f:button>
												<h:url class="btn btn-default" value="/tickets"><h:message key="ticket.update.cancel">Cancel</h:message></h:url>
											</div>
										</div>
									</div>
								</div>
							</f:form>
						</div>
					</div>
				</div>
			</div>
		</div>

	</h:section>

</h:use>






