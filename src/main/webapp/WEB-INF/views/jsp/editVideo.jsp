<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>editVideo</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include><br>
	<div class="inline">
		<!-- Video Edit -->
		<form action="editVideo" method="post">
			<input type="hidden" value="${requestScope.video.videoId }" name="videoId"><br>
		 	name: <input type="text" placeholder="${requestScope.video.name }" name="name"><br>
		 	description: <input type="text" placeholder="${requestScope.video.description }" name="description"><br>
		 	privacy
		 	<select name="privacy">
			  <option <c:if test="${requestScope.video.privacyId == \"1\" }"> selected </c:if> value="1">Public</option>
			  <option <c:if test="${requestScope.video.privacyId == \"2\" }"> selected </c:if> value="2">Private</option>
			</select><br>
	 		<input type="submit" value="Update">
		</form>
		
		<!-- Delete video -->
		<c:if test="${ not empty sessionScope.user  }">
			<c:if test="${sessionScope.user.userId == requestScope.video.userId}">
				<form action="deleteVideo" method="post">
					<input type="hidden" value="${requestScope.video.videoId}" name="videoId">
					<input type="submit" value="Delete">
				</form>
			</c:if>
		</c:if>
		
		<br>
		TAGS:
		<c:forEach items="${requestScope.video.tags}" var="currentTag">	
			<c:out value="#${currentTag.tag} "></c:out>
		</c:forEach>
		<!-- video info -->
		<h3>Views: <c:out value="${requestScope.video.views }"></c:out></h3>
		<!-- video owner -->
		<h3>Owner: 
			<a href="viewProfile?username=${sessionScope.user.username}">
				<c:out value="${sessionScope.user.username }"></c:out>
				<img src="img?path=${sessionScope.user.avatarUrl }&userId=${sessionScope.user.userId }" width="50px" height="auto"/>
			</a>
		</h3>
		<br>
		<!-- video player -->
		<video width="800" height="600" controls preload="auto">
		  		<source src="video?url=${requestScope.video.locationUrl}&userId=${requestScope.video.userId}" type="video/mp4">
		</video>
	</div>
</body>
</html>