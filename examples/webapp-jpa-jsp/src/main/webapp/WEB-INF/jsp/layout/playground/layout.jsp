<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.geetools.org/jsp/geemvc/form" prefix="f"%>
<%@ taglib uri="http://www.geetools.org/jsp/geemvc/html" prefix="h"%>

<h:layout>
	<html>
		<head>
			<title>Layout Playground - ${pageTitle}</title>
			
			<link href="<c:url value="/css/vendor/bootstrap.min.css" />" rel="stylesheet">
			<link href="<c:url value="/css/vendor/bootstrap-theme.min.css" />" rel="stylesheet">
			<link href="<c:url value="/css/playground/styles.css" />" rel="stylesheet">
			
			<script src="<c:url value="/js/vendor/jquery-1.12.4.min.js" />"></script>
			<script src="<c:url value="/js/vendor/bootstrap.min.js" />"></script>
			<script src="<c:url value="/js/playground/main.js" />"></script>
		</head>
		
		<body>
	
			<h:section name="header">
				<jsp:include page="../../includes/playground/header.jsp" />
			</h:section>

			<div class="container">
				<h:section name="content" />
			</div>
			
			<h:section name="footer">
				<jsp:include page="../../includes/playground/footer.jsp" />
			</h:section>

		</body>
	</html>
</h:layout>

