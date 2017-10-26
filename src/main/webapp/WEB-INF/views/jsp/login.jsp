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
  		<form action="login" method = "post">
	      <input type="text" placeholder="Enter Username" name="username" required><br>
	      <c:if test="${requestScope.usernameError != null }">
			<div class="err">
				<c:out value="${requestScope.usernameError }"></c:out>
			</div>
	      </c:if>
	      <input type="password" placeholder="Enter Password" name="password" required><br>
	      <c:if test="${requestScope.passwordError != null }">
			<div class="err">
				<c:out value="${requestScope.passwordError }"></c:out><br>
				<a href="#">Forgot password?</a>
			</div>
	      </c:if>
	      <button type="submit">Login</button><br>
	      <input type="checkbox" checked="checked"> Remember me
  		</form>
</fieldset>
<br>
</body>
</html>