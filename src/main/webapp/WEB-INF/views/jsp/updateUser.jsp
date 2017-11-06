<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>update user</title>
<style type="text/css">
.center {
text-align:center;
}
.right {
text-align:right;
}
.vertical-center {
vertical-align: middle;
}
.margin-5 {
margin: 5px;
}
.img-avatar{
border:1.5px solid black;
margin: 10px;
}
</style>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
		
	<br>
		<div class="col-lg-3"></div>
		<div class="col-lg-6">
			<div class="well bs-component">
				 <fieldset>
				    <legend >Update user</legend>
				    <div class="center">
						<img class="img-avatar" src="<c:url value="/img/${sessionScope.user.userId}"/>" width="150px" height="auto"/><br>
					</div>
						<!-- upload avatar -->
						<form action="<c:url value="/uploadAvatar"/>" method="post" enctype="multipart/form-data">
							<label class="col-lg-3 btn btn-default btn-file">
								Choose file<input style="display: none;" type="file" name="avatar"  accept="image/png"><br>
							</label>
							<div class="col-lg-3 right"></div>
							<div class="col-lg-6 right">
								<input class="btn btn-primary" type="submit" value="Upload" />
							</div>
						</form>
						<c:if test="${ error != null }">
							<div class="err">
								<h1><c:out value="${error}"></c:out></h1>
							</div>
						</c:if>
				</fieldset>
			</div>
			<div class="well bs-component">
			<fieldset>
				<!-- update user -->
				<form action="updateUser" method="post">
					<label for="text" class="col-lg-2 control-label">Username:</label>
					<div class="col-lg-10">
				 		<input class="form-control" type="text" placeholder="${sessionScope.user.username }" name="username"><br>
					 	<c:if test="${errorUsername != null }">
							<div class="err">
								<span class="text-danger">${errorUsername}"></span><br>
							</div>
					    </c:if>
				 	</div>
				 	<label for="text" class="col-lg-2 control-label vertical-center">Email:</label>
			 		<div class="col-lg-10">
			 			<input class="form-control" type="text" placeholder="${sessionScope.user.email }" name="email"><br>
			 		</div>
				 	<c:if test="${errorEmail != null }">
						<div class="err">
							<span class="text-danger">${errorEmail}</span><br>
						</div>
				    </c:if>
				    <label for="text" class="col-lg-2 control-label">New password:</label>
			 		<div class="col-lg-10">
				 		<input class="form-control" type="password" name="newPassword"><br>
				 	</div>
				 	<label for="text" class="col-lg-2 control-label">Confirm password:</label>
				 	<div class="col-lg-10">
				 		<input class="form-control" type="password" name="newPasswordConfirm"><br>
				 	</div>
				 	<c:if test="${errorNewPassword != null }">
						<div class="err">
							<span class="text-danger">${errorNewPassword}"></span><br>
						</div>
				    </c:if>
				 	<label for="text" class="col-lg-2 control-label">Facebook:</label>
				 	<div class="col-lg-10">
				 		<input class="form-control" type="text" placeholder="${sessionScope.user.facebook }" name="facebook"><br>
				 	</div>
				 	<label for="text" class="col-lg-2 control-label">First name:</label>
				 	<div class="col-lg-10">
				 		<input class="form-control" type="text" placeholder="${sessionScope.user.firstName }" name="firstName"><br>
				 	</div>
				 	<label for="text" class="col-lg-2 control-label">Last name:</label>
				 	<div class="col-lg-10">
				 		<input class="form-control" type="text" placeholder="${sessionScope.user.lastName }" name="lastName"><br>
				 	</div>
				 	<label for="text" class="col-lg-2 control-label">Gender:</label>
				 	<div class="col-lg-10">
					 	<select class="form-control" name="gender">
						  <option <c:if test="${sessionScope.user.gender == \"\" }"> selected </c:if> value="">Rather not say</option>
						  <option <c:if test="${sessionScope.user.gender == \"Male\" }"> selected </c:if> value="Male">Male</option>
						  <option <c:if test="${sessionScope.user.gender == \"Female\" }"> selected </c:if> value="Female">Female</option>
						  <option <c:if test="${sessionScope.user.gender == \"Custom\" }"> selected </c:if> value="Custom">Custom</option>
						</select><br>
					</div>
					<br>
					<label for="text" class="col-lg-2 control-label">password:</label>
					<div class="col-lg-10">
				 		<input class="form-control" type="password" name="oldPassword" required="required">
				 		<span class="help-block">Required</span>
				 	</div>
				 	<c:if test="${errorPassword != null }">
						<div class="err">
							<span class="text-danger">"${errorPassword}"></span><br>
						</div>
				    </c:if>
				    <div class="col-lg-12 center margin-5">
			 			<input class="btn btn-primary" type="submit" value="Update">
			 		</div>
				</form>
				
			<%-- 	<a href="<c:url value="/viewProfile/${user.userId}"/>">
					<button>Go to profile</button>
				</a> --%>
				<br>
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
 			</fieldset>
			</div>
 		</div>
		<div class="col-lg-3"></div>
</body>
</html>