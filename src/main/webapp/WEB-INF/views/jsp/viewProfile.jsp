<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>view profile</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/inline.css" />" />
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	
	<div class="inline">
		<h1>User</h1>
		<a href="<c:url value="/viewProfile/${user.userId}" />">
			<img src="<c:url value="/img/${user.userId}" />" width="50px" height="auto"/>
			<c:out value="${user.username}"></c:out>
		</a>
		
		<!-- Delete user -->
		<c:if test="${ not empty sessionScope.user  }">
			<c:if test="${sessionScope.user.userId == user.userId}">
				<form action="<c:url value="/updateUser/${sessionScope.user.userId}"/>" method="post">
					<input type="submit" value="edit user">
				</form>
				<form action="<c:url value="/deleteUser/${sessionScope.user.userId}"/>" method="post">
					<input type="submit" value="delete user">
				</form>
			</c:if>
		</c:if>
		
		<!-- follow/unfollow logic  -->
		<c:if test="${sessionScope.user.userId != user.userId}">
			<c:set var="contains" value="false" />
			<c:forEach var="follower" items="${requestScope.followers}">
			  <c:if test="${follower.userId eq sessionScope.user.userId}">
			    <c:set var="contains" value="true" />
			  </c:if>
			</c:forEach>
			
			<c:if test="${contains eq true}">
				<form action="follow" method="post">
					<input type="hidden" value="${user.userId}" name="following">
					<input type="hidden" value="${sessionScope.user.userId}" name="follower">
					<input type="hidden" value="unfollow" name="action">
					<input type="submit" value="unfollow">
				</form>
			</c:if>
			
			<c:if test="${contains eq false}">
				<form action="follow" method="post">
					<input type="hidden" value="${user.userId}" name="following">
					<input type="hidden" value="${sessionScope.user.userId}" name="follower">
					<input type="hidden" value="follow" name="action">
					<input type="submit" value="follow">
				</form>
			</c:if>
		</c:if>
	</div>
	
	<!-- followers -->
	<div class="inline">
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
	
	<!-- following -->
	<div class="inline">
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
	<div class="inline">
	<h3>Videos</h3>
		<jsp:include page="showVideos.jsp"></jsp:include><br>
	</div>
	<div class="inline">
	<h3>Playlists</h3>
		<jsp:include page="playlist.jsp"></jsp:include>
	</div>
</body>
</html>