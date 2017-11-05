<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- show my playlist in my profile -->
<div class="btn-group">
  <a href="#" class="btn btn-primary">${user.username}'s playlists</a>
  <a href="#" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></a>
  <ul class="dropdown-menu">
   <c:forEach items="${requestScope.myPlaylists}" var="playlist">
		<li>
		<c:url value="/showPlaylist" var="myURL">
			<c:param name="playlistName" value="${playlist.playlistName}"/>
			<c:param name="userId" value="${user.userId}"/>
		</c:url>
		<a href="${myURL}">
			${playlist.playlistName}
		</a>
		</li>
   </c:forEach>
  </ul>
</div>
<%-- 
	<c:forEach items="${requestScope.myPlaylists}" var="playlist">
		<ul>
			<li>
				<c:url value="/showPlaylist" var="myURL">
					  <c:param name="playlistName" value="${playlist.playlistName}"/>
 				 	  <c:param name="userId" value="${user.userId}"/>
				</c:url>
				<a href="${myURL}">
					<button class="btn btn-primary btn-xs" >view</button>
				</a>
			</li>
			<li>
				<p><b>${playlist.playlistName}</b>
			</li>
		</ul>
	</c:forEach>
	
	 --%>
	 <br>
	 <br>
	<c:if test="${sessionScope.user.userId == user.userId}">
		<form action=<c:url value="/createPlaylist"/> method="post">
			<input type="hidden" value="${sessionScope.user.userId}" name="userId">
			<input class="form-control" type="text" placeholder="New playlist name" name="newPlaylist"/>
			<input class="btn btn-primary btn-xs" type="submit" value="create"/>
		</form>
	</c:if>
</body>
</html>