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
		<form action="<c:url value="/editVideo/${requestScope.video.videoId}"/>" method="post">
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
		
		<!-- back to player -->
		<form action="<c:url value="/player/${requestScope.video.videoId}"/>" method="get">
	 		<input type="submit" value="back to player">
		</form>
		
		<br>
		TAGS:
		<c:forEach items="${requestScope.video.tags}" var="currentTag">	
			<c:out value="#${currentTag.tag} "></c:out>
		</c:forEach>
		<!-- video info -->
		<h3>Views: <c:out value="${requestScope.video.views }"></c:out></h3>
		<!-- video owner -->
		<h3>Owner: 
			<a href="<c:url value="/viewProfile/${user.userId}" />">
				<c:out value="${sessionScope.user.username }"></c:out>
				<img src="<c:url value="/img/${user.userId}" />" width="50px" height="auto"/>
			</a>
		</h3>
		<br>
		<!-- video player -->
		<video width="800" height="600" controls preload="auto">
		  		<source src="<c:url value="/video/${requestScope.video.videoId}"/>" type="video/mp4">
		</video>
	</div>
</body>
</html>