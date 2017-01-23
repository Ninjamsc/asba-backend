<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Registration Confirmation Page</title>
	<link href="<c:url value='/static/css/bootstrap.css' />" rel="stylesheet"></link>
	<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
</head>
<body>
	<div class="generic-container">
		<div class="authbar">
			<span> Зарегистрирован в панели управления как  <strong>${loggedinuser}</strong> </span> <span class="floatRight"><a href="<c:url value="/logout" />">Выход</a></span>
		</div>
		<div class="alert alert-success lead">
	    	${success}
		</div>
		
		<span class="well floatRight">
			Перейти к  <a href="<c:url value='/list' />">списку пользователей</a>
		</span>
	</div>
</body>

</html>