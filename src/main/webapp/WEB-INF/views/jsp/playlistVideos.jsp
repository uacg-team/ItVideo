<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
div.inline { 
	float:left; 
	margin:5px;
	padding: 5px;
	border-style: solid; 
	border-color: black; 
	border-width: 1px;
}
</style>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	<c:forEach items="${requestScope.videos}" var="video">
		<c:out value="${video.name}"></c:out>
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
		<a href="player?url=${video.locationUrl}" id="preview">	
			<video width="320" height="240" preload="none">
		  		<source src="video?url=${video.locationUrl}&userId=${video.userId}" type="video/mp4">
			</video>
		</a><br>
		
		
		<form action="videoLike?like=1&videoId=${video.videoId}&userId=${sessionScope.user.userId}" method="post">
			<c:out value="${video.likes}"></c:out>
			<input type="submit" value="Like">
		</form>
		<form action="videoLike?like=-1&videoId=${video.videoId}&userId=${sessionScope.user.userId}" method="post">
			<c:out value="${video.disLikes}"></c:out>
			<input type="submit" value="Dislike">
		</form>
	</div>
	</c:forEach>
</body>
</html>