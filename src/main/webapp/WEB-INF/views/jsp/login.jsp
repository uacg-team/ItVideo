<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include><br>

<fieldset>
	<legend>Login</legend>
  		<form action="<c:url value="/login" />" method = "post">
	      <input type="text" placeholder="Enter Username" name="username" required><br>
	      <c:if test="${requestScope.usernameError != null }">
			<div class="err">
				<c:out value="${requestScope.usernameError }"></c:out>
				<br>
				<a href="<c:url value="/register" />"><button type="button" formmethod="get">register</button></a>
			</div>
	      </c:if>
	      <input type="password" placeholder="Enter Password" name="password" required><br>
	      <c:if test="${requestScope.passwordError != null }">
			<div class="err">
				<c:out value="${requestScope.passwordError }"></c:out><br>
			</div>
	      </c:if>
	      <button type="submit">Login</button><br>
	      <input type="checkbox" checked="checked"> Remember me
  		</form>
  		
		<c:if test="${requestScope.passwordError != null }">
			<form action="<c:url value="/forgotPassword" />" method="get">
				<input type="hidden" value="${requestScope.username}" name="username">
				<input type="submit" value="Forgot password?">
			</form>
		</c:if>
  		
</fieldset>
<br>
</body>
</html>