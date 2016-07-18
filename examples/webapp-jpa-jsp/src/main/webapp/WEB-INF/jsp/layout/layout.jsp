<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.geetools.org/jsp/geemvc/form" prefix="f"%>
<%@ taglib uri="http://www.geetools.org/jsp/geemvc/html" prefix="h"%>

<h:layout>
	<html>
		<head>
			<title>${pageTitle}</title>

			<link href="<c:url value="/css/vendor/bootstrap.min.css" />" rel="stylesheet">
			<link href="<c:url value="/css/vendor/bootstrap-theme.min.css" />" rel="stylesheet">
			<link href="<c:url value="/css/styles.css" />" rel="stylesheet">

			<script type="text/javascript">var baseURI = '<c:url value="${pageContext.request.servletPath}" />';</script>
			<script src="<c:url value="/js/vendor/jquery-1.12.4.min.js" />"></script>
			<script src="<c:url value="/js/vendor/bootstrap.min.js" />"></script>
			<script src="<c:url value="/js/main.js" />"></script>
		</head>

		<body>

			<h:section name="header">
				<jsp:include page="../includes/header.jsp" />
			</h:section>

			<div class="container">
				<h:section name="nav-bar" />
			</div>

			<div class="container">
				<h:section name="content" />
			</div>

			<h:section name="footer">
				<jsp:include page="../includes/footer.jsp" />
			</h:section>

		</body>
	</html>
</h:layout>

