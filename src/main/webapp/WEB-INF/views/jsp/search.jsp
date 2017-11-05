<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search</title>
<style type="text/css">
.row-eq-height {
  display: flex;
  flex-wrap: wrap;
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
	<div>
		<c:if test="${sessionScope.searchParam == \"videos\" }">
			<c:if test="${fn:length(requestScope.list) eq 0}">
				<h1>Sorry, no videos found</h1>
			</c:if>
			<c:if test="${fn:length(requestScope.list) gt 0}">
				<h1>Videos found</h1>
				<jsp:include page="showVideosRequestP.jsp"></jsp:include><br>
			</c:if>
		</c:if>

		<c:if test="${sessionScope.searchParam == \"users\" }">
			<c:if test="${fn:length(list) eq 0}">
				<h1>Sorry, no users found</h1>
			</c:if>
			<c:if test="${fn:length(list) gt 0}">
				<h1>Users found</h1>
				<c:forEach items="${list}" var="userFound">
					<div class="background">
					<a href="<c:url value="/viewProfile/${userFound.userId}" />">
						<img src="<c:url value="/img/${userFound.userId}" />" width="50px" height="auto"/>
						<c:out value="${userFound.username}"></c:out>
					</a>
					</div>
				</c:forEach>
			</c:if>
		</c:if>
		
		<br>
			
		<c:if test="${sessionScope.searchParam == \"playlists\" }">
			<c:if test="${fn:length(list) eq 0}">
				<h1>Sorry, no playlists found</h1>
			</c:if>
			<c:if test="${fn:length(list) gt 0}">
				<h1>Playlist found</h1>
				<c:forEach items="${list}" var="playlist">
					<div class="background">
					<c:out value="${playlist.playlistName}"></c:out>
					<a href=<c:url value="/showPlaylist?playlistName=${playlist.playlistName}&userId=${playlist.userId}"/>>
						<button class="btn btn-primary btn-xs">View</button>
					</a>
					</div>
				</c:forEach>
			</c:if>
		</c:if>
	</div>
</body>
</html>