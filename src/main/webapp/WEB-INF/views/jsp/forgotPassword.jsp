<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>forgot password</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include><br>
	<form action="<c:url value="/forgotPassword" />" method = "post">
      <input type="hidden" name="username" value="${username}" required><br>
      <input type="text" placeholder="Enter email for new password" name="email" required><br>
      <button type="submit">Send new password</button>
	</form>
	<c:if test="${emailError != null }">
		<div class="err">
			<c:out value="${emailError }"></c:out><br>
		</div>
    </c:if>
	

</body>
</html>