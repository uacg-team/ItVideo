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
			<input type="text" value="${username}" placeholder="Username" name="username" required><br>
      		<!-- Username Error  -->
			<c:if test="${usernameError != null }">
				<div class="err">
					<c:out value="${ usernameError }"></c:out><br>
				</div>
			</c:if>
	      <input type="password" placeholder="Password" name="password" required><br>
	      <input type="password" placeholder="Confirm Password" name="confirmPassword" required><br>
			<!-- Password Error  -->
			<c:if test="${passError != null }">
				<div class="err">
					<c:out value="${passError}"></c:out><br>
				</div>
			</c:if>
	      <input type="text" placeholder="Email" value="${email}" name="email" required><br>
	      	<!-- email Error  -->
			<c:if test="${userError != null }">
				<div class="err">
					<c:out value="${userError}"></c:out><br>
				</div>
			</c:if>
	      <button onclick="activeteMessage()" type="submit">Register</button><br>
		</form>
	</fieldset>
	
	<script>
		function activeteMessage() {
		    alert("Thank You! Please check your email to activate your account");
		}
	</script>

</body>
</html>