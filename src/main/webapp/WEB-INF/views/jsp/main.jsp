<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>main</title>
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
	
	<div>
		<select onchange="location = this.value;">
			 <option value="main?sort=date" <c:if test="${requestScope.sort == \"date\" }"> selected </c:if> >SortByDate</option>
			 <option value="main?sort=like" <c:if test="${requestScope.sort == \"like\" }"> selected </c:if>>SortByLikes</option>
			 <option value="main?sort=view" <c:if test="${requestScope.sort == \"view\" }"> selected </c:if>>SortByViews</option>
		</select>
	</div>
	
	<c:if test="${requestScope.search == null }">
		<div class="inline">
			<jsp:include page="showVideos.jsp"></jsp:include><br>
		</div>
	</c:if>
	
	<c:if test="${requestScope.search != null }">
		<div>
			<c:if test="${requestScope.videos != null }">
				<div class="inline">
				<h1>Videos found</h1>
					<jsp:include page="showVideos.jsp"></jsp:include><br>
				</div>
			</c:if>
			
			<c:if test="${requestScope.videos == null }">
				<div class="inline">
					<h1>videos not found</h1>
				</div>
			</c:if>
				
			<c:if test="${requestScope.users != null }">
				<div class="inline">
				<h1>Users found</h1>
					<c:forEach items="${requestScope.users}" var="userFound">
						<div class="inline">
							<a href="viewProfile?username=${userFound.username}">
								<img src="img?path=${userFound.avatarUrl}" width="50px" height="auto"/>
								<c:out value="${userFound.username}"></c:out>
							</a>
						</div>
					</c:forEach>
				</div>
			</c:if>
			
			<br>
			
			<c:if test="${requestScope.users == null }">
				<div class="inline">
					<h1>users not found</h1>
				</div>
			</c:if>
				
			<br>
				
			<c:if test="${requestScope.playlists != null }">
				<div class="inline">
					<h1>Playlist found</h1>
					<div class="inline">
						<c:forEach items="${requestScope.playlists}" var="playlistFound">
							<c:out value="${playlistFound.playlistName}"></c:out>
							<%-- 
							<a href="xxxxx?xxxxx=${playlistFound.xxxxxxx}">
								<c:out value="${playlistFound.name}"></c:out>
							</a>
							 --%>
						</c:forEach>
					</div>
				</div>
			</c:if>
			
			<br>	
			
			<c:if test="${requestScope.playlists == null }">
				<div class="inline">
					<h1>playlists not found</h1>
				</div>
			</c:if>
		</div>
	</c:if>
</body>
</html>