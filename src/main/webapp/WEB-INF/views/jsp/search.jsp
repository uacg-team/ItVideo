<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/inline.css" />" />
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	
	<div>
		<c:if test="${sessionScope.searchParam == \"videos\" }">
			<c:if test="${fn:length(requestScope.videos) eq 0}">
				<h1>Sorry, no videos found</h1>
			</c:if>
			<c:if test="${fn:length(requestScope.videos) gt 0}">
				<div class="inline">
				<h1>Videos found</h1>
				<jsp:include page="showVideosRequest.jsp"></jsp:include><br>
				</div>
			</c:if>
		</c:if>

		<c:if test="${sessionScope.searchParam == \"users\" }">
			<c:if test="${fn:length(requestScope.searchResult) eq 0}">
				<h1>Sorry, no users found</h1>
			</c:if>
			<c:if test="${fn:length(requestScope.searchResult) gt 0}">
				<div class="inline">
				<h1>Users found</h1>
					<c:forEach items="${requestScope.searchResult}" var="userFound">
						<div class="inline">
						
							<a href="<c:url value="/viewProfile/${userFound.userId}" />">
								<img src="<c:url value="/img/${userFound.userId}" />" width="50px" height="auto"/>
								<c:out value="${userFound.username}"></c:out>
							</a>
						</div>
					</c:forEach>
				</div>
			</c:if>
		</c:if>
		
		<br>
			
		<c:if test="${sessionScope.searchParam == \"playlists\" }">
			<c:if test="${fn:length(requestScope.searchResult) eq 0}">
				<h1>Sorry, no playlists found</h1>
			</c:if>
			<c:if test="${fn:length(requestScope.searchResult) gt 0}">
				<div class="inline">
					<h1>Playlist found</h1>
					<div class="inline">
						<c:forEach items="${requestScope.searchResult}" var="playlist">
							<c:out value="${playlist.playlistName}"></c:out>
							<a href=<c:url value="/showPlaylist?playlistName=${playlist.playlistName}&userId=${playlist.userId}"/>>
								<button>View</button>
							</a>
						</c:forEach>
					</div>
				</div>
			</c:if>
		</c:if>
	</div>
</body>
</html>