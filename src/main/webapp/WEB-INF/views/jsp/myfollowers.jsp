<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>followers</title>
</head>
<body>
	<div>
		<h1>followers</h1>
		<c:forEach items="${ requestScope.followers }" var="user">
		<div>
			<a href="viewProfile?username=${user.username}">
				<img src="img?path=${user.avatarUrl}&userId=${user.userId}" width="50px" height="auto"/>
				<c:out value="${user.username}"></c:out>
			</a>
		</div>
		</c:forEach>
	</div>
	<br>
</body>
</html>