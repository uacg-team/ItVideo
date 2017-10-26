<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>main</title>
<link type="text/css" rel="stylesheet" href="/css/inline.css" />
</head>
<body>

	<jsp:include page="header.jsp"></jsp:include><br>
	
	<div>
		<select onchange="location = this.value;">
			 <option value="<c:url value="/main/sort/date" />" <c:if test="${sessionScope.sort eq \"date\" }"> selected </c:if>>SortByDate</option>
			 <option value="<c:url value="/main/sort/like" />" <c:if test="${sessionScope.sort eq \"like\" }"> selected </c:if>>SortByLikes</option>
			 <option value="<c:url value="/main/sort/view" />" <c:if test="${sessionScope.sort eq \"view\" }"> selected </c:if>>SortByViews</option>
		</select>
	</div>
	
	<div class="inline">
		<c:forEach items="${sessionScope.videos}" var="video">	
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
	
</body>
</html>