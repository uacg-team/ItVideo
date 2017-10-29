<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
			<c:if test="${videos != null }">
				<div class="inline">
				<h1>Videos found</h1>
					<c:forEach items="${videos}" var="video">	
					<div class="inline">
						<c:out value="Name: ${video.name}"></c:out><br>
						<c:out value="Description: ${video.description}"></c:out><br>
						<c:out value="Owner: ${video.userName}"></c:out><br>
						<c:out value="Published: ${video.date}"></c:out><br>
						<c:out value="Views: ${video.views}"></c:out><br>
						<c:out value="Privacy: ${video.privacy}"></c:out><br>
						<c:out value="Tags: "></c:out><br>
						<c:forEach items="${video.tags}" var="tag">	
							<c:out value="#${tag.tag} "></c:out>
						</c:forEach>
						<br>
						<a href="<c:url value="/player/${video.videoId}" />">	
							<video width="320" height="240" preload="none">
						  		<source src="<c:url value="/video/${video.videoId}" />" type="video/mp4">
							</video>
						</a><br>
					</div>
					</c:forEach>
				</div>
			</c:if>
			
			<c:if test="${videos == null }">
				<div class="inline">
					<h1>videos not found</h1>
				</div>
			</c:if>
				
			<c:if test="${users != null }">
				<div class="inline">
				<h1>Users found</h1>
					<c:forEach items="${users}" var="userFound">
						<div class="inline">
						
							<a href="<c:url value="/viewProfile/${userFound.userId}" />">
								<img src="<c:url value="/img/${userFound.userId}" />" width="50px" height="auto"/>
								<c:out value="${userFound.username}"></c:out>
							</a>
						</div>
					</c:forEach>
				</div>
			</c:if>
			
			<br>
			
			<c:if test="${users == null }">
				<div class="inline">
					<h1>users not found</h1>
				</div>
			</c:if>
				
			<br>
				
			<c:if test="${playlists != null }">
				<div class="inline">
					<h1>Playlist found</h1>
					<div class="inline">
						<c:forEach items="${playlists}" var="playlist">
							<c:out value="${playlist.playlistName}"></c:out>
							<a href=<c:url value="/showPlaylist?playlistName=${playlist.playlistName}&userId=${playlist.userId}"/>>
								<button>View</button>
							</a>
						</c:forEach>
					</div>
				</div>
			</c:if>
			
			<br>	
			
			<c:if test="${playlists == null }">
				<div class="inline">
					<h1>playlists not found</h1>
				</div>
			</c:if>
		</div>
</body>
</html>