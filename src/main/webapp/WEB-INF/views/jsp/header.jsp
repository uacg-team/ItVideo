<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>header</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/inline.css" />" />
<style type="text/css">
ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    border: 1px solid #e7e7e7;
    background-color: #f3f3f3;
}

li {
	vertical-align: middle;
	position:relative;
    float: left;
    text-align: center;
}

li a {
    display: block;
    color: #666;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
}

/* Change the link color to #111 (black) on hover */
li a:hover {
    background-color: #ffffff;
}
</style>
</head>
<body>
	
<ul>
	<li>
  		<a href="<c:url value="/main" />"><img src="<c:url value="/pics/logo.png"/>" style="width: 200px; height: auto"></a>
	</li>
	<c:if test="${sessionScope.user == null}"> 
		<li><a href="login">Login</a></li>
		<li><a href="register">Register</a></li>
	</c:if>
	<li><a href="upload">Upload</a></li>
	
	
	<li>
		<form action="<c:url value="/search" />" method="get">
			<input type="text" name="search" placeholder="Search..">
		</form>
	</li>
		
	
	<c:if test="${ sessionScope.user != null}"> 
	<li>
		<a href="viewProfile?username=${sessionScope.user.username}">
			<img src="<c:url value="/img/${sessionScope.user.userId}"/>" width="50px" height="auto"/>
			<c:out value="${sessionScope.user.username}"></c:out>
		</a>
	</li>
	<li><a href="updateUser">Update user</a></li>
	<li><a href="<c:url value="/logout"/>">Logout</a></li>
	</c:if>
</ul>
<br>
</body>
</html>