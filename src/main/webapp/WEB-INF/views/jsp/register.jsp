<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>

	<fieldset>
	<legend>Registration</legend>
  		<form action="register" method = "post">
	      <input type="text" placeholder="Username" name="username" required><br>
	      <input type="password" placeholder="Password" name="password" required><br>
	      <input type="text" placeholder="Email" name="email" required><br>
	      <c:if test="${requestScope.userError != null }">
			<div style="color: red">
				<c:out value="${requestScope.userError }"></c:out><br>
				<a href="#"> Forgot password?</a>
			</div>
	      </c:if>
	      <button type="submit">Register</button><br>
	      <input type="checkbox" checked="checked"> Remember me
		</form>
	</fieldset>

</body>
</html>