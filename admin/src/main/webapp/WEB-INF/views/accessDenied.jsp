<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>AccessDenied page</title>
</head>
<body>
<div class="generic-container">
    <div class="authbar">
        <span>Уважаемый <strong>${loggedinuser}</strong>, Вы не авторизованы для доступа к данной странице.</span> <span
            class="floatRight"><a href="<c:url value="/logout" />">Logout</a></span>
    </div>
</div>
</body>
</html>