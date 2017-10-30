<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="<c:url value="/css/inline.css"/>" />
<title>videos</title>
</head>
<body>
	<c:forEach items="${requestScope.videos}" var="video">	
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
			<video width="320" height="240" preload="none" poster="<c:url value="/thumbnail/${video.videoId}" />">
		  		<source src="<c:url value="/video/${video.videoId}" />" type="video/mp4">
			</video>
		</a><br>
	</div>
	</c:forEach>
</body>
</html>