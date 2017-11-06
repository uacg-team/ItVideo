<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>view profile</title>
<style type="text/css">

.row-eq-height {
  display: flex;
  flex-wrap: wrap;
}

.btn-margin {
	margin: 5px;
}

.background {
	background-color: rgba(76, 177, 234, 0.15);
	border-radius: 15px;
	border: 2px solid lightblue;
	float: none;
	padding: 15px;
}
</style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include><br>

<div class="container">
<div class="row row-eq-height">
	<div class="col-xs-3 background">
		<div class="">
			<h1>User</h1>
			<a href="<c:url value="/viewProfile/${user.userId}" />">
				<img src="<c:url value="/img/${user.userId}" />" width="50px" height="auto"/>
				<c:out value="${user.username}"></c:out>
			</a>
			<!-- Edit / Delete user -->
			<c:if test="${ not empty sessionScope.user  }">
				<c:if test="${sessionScope.user.userId == user.userId}">
					<form action="<c:url value="/updateUser/${sessionScope.user.userId}"/>" method="get">
						<input class="btn btn-warning btn-xs btn-margin" type="submit" value="edit user">
					</form>
					<form action="<c:url value="/deleteUser/${sessionScope.user.userId}"/>" method="post">
						<input class="btn btn-danger btn-xs btn-margin" type="submit" value="delete user">
					</form>
				</c:if>
			</c:if>
		</div>
	</div>
	<div class="col-xs-3 background">
			<h1>followers</h1>
			<c:forEach items="${ requestScope.followers }" var="user">
			<div>
				<a href="<c:url value="/viewProfile/${user.userId}" />">
					<img src="<c:url value="/img/${user.userId}" />" width="50px" height="auto"/>
					<c:out value="${user.username}"></c:out>
				</a>
			</div>
			</c:forEach>
	</div>
	<div class="col-xs-3 background">
		<h1>following</h1>
		<c:forEach items="${ requestScope.following }" var="user">
		<div>
			<a href="<c:url value="/viewProfile/${user.userId}" />">
				<img src="<c:url value="/img/${user.userId}" />" width="50px" height="auto"/>
				<c:out value="${user.username}"></c:out>
			</a>
		</div>
		</c:forEach>	
	</div>
	<div class="col-xs-3 background">
		<h1>Playlists</h1>
		<jsp:include page="playlist.jsp"></jsp:include>
	</div>
</div>
<br>
<div class="row row-eq-height">
	<div class="container">
		<div class="col-xs-12 background">
			<h1>Videos</h1>
			<jsp:include page="showVideosRequest.jsp"></jsp:include><br>
		</div>
	</div>
</div>
</div>
</body>
</html>