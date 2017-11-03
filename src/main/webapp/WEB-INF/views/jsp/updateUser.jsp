<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>update user</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<br>
	
	<img src="<c:url value="/img/${sessionScope.user.userId}"/>" width="100px" height="auto" /><br>
	
	
	<!-- upload avatar -->
	<form action="<c:url value="/uploadAvatar"/>" method="post" enctype="multipart/form-data">
		<input type="file" name="avatar"  accept="image/png"><br>
		<input type="submit" value="Upload" />
	</form>
	
	<c:if test="${ error != null }">
		<div class="err">
			<h1><c:out value="${error}"></c:out></h1>
		</div>
	</c:if>
	
	
	<!-- update user -->
	<form action="updateUser" method="post">
	 	username: <input type="text" placeholder="${sessionScope.user.username }" name="username"><br>
	 	
	 	  <c:if test="${errorUsername != null }">
			<div class="err">
				<c:out value="${errorUsername}"></c:out><br>
			</div>
	      </c:if>
	 	
	 	
	 	email: <input type="text" placeholder="${sessionScope.user.email }" name="email"><br>
	 	
	 	  <c:if test="${errorEmail != null }">
			<div class="err">
				<c:out value="${errorEmail}"></c:out><br>
			</div>
	      </c:if>
	 	newPassword: <input type="text" name="newPassword"><br>
	 	newPasswordConfirm: <input type="text" name="newPasswordConfirm"><br>
	 	  <c:if test="${errorNewPassword != null }">
			<div class="err">
				<c:out value="${errorNewPassword}"></c:out><br>
			</div>
	      </c:if>
	 	
	 	
	 	facebook<input type="text" placeholder="${sessionScope.user.facebook }" name="facebook"><br>
	 	First Name<input type="text" placeholder="${sessionScope.user.firstName }" name="firstName"><br>
	 	Last Name<input type="text" placeholder="${sessionScope.user.lastName }" name="lastName"><br>
	 	
	 	gender
	 	<select name="gender">
		  <option <c:if test="${sessionScope.user.gender == \"\" }"> selected </c:if> value="">Rather not say</option>
		  <option <c:if test="${sessionScope.user.gender == \"Male\" }"> selected </c:if> value="Male">Male</option>
		  <option <c:if test="${sessionScope.user.gender == \"Female\" }"> selected </c:if> value="Female">Female</option>
		  <option <c:if test="${sessionScope.user.gender == \"Custom\" }"> selected </c:if> value="Custom">Custom</option>
		</select>
		<br>
	 	currentPassword: <input type="text" name="oldPassword" required="required"><br>
	 	  <c:if test="${errorPassword != null }">
			<div class="err">
				<c:out value="${errorPassword}"></c:out><br>
			</div>
	      </c:if>
 		<input type="submit" value="Update">
	</form>
	
	<a href="<c:url value="/viewProfile/${user.userId}"/>">
		<button>Go to profile</button>
	</a>
		
 	<c:if test="${requestScope.UserException != null }">
		<div style="color: red">
			<c:out value="${requestScope.UserException }"></c:out>
		</div>
 	</c:if>
 	<c:if test="${requestScope.SQLException != null }">
		<div style="color: red">
			<c:out value="${requestScope.SQLException }"></c:out>
		</div>
 	</c:if>
 	<c:if test="${requestScope.UserNotFoundException != null }">
		<div style="color: red">
			<c:out value="${requestScope.UserNotFoundException }"></c:out>
		</div>
 	</c:if>
</body>
</html>