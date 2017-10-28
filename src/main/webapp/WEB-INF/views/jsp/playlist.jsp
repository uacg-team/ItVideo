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
	<c:forEach items="${requestScope.myPlaylists}" var="playlist">
		<p><b>${playlist.playlistName}</b>
			<a href=<c:url value="/showPlaylist?playlistName=${playlist.playlistName}&userId=${user.userId}"/>>
			<button>view</button>
			</a>
	</c:forEach>
	<c:if test="${sessionScope.user.userId == user.userId}">
		<form action=<c:url value="/createPlaylist"/> method="post">
		<input type="hidden" value="${sessionScope.user.userId}" name="userId">
		New playlist<input type="text" placeholder="add name" name="newPlaylist"/>
		<input type="submit" value="create"/>
		</form>
	</c:if>
</body>
</html>