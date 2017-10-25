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
	<c:forEach items="${requestScope.myPlaylists}" var="playlist">
		<p><b>${playlist.playlistName}</b>
			<a href="playlist?v=loadVideos&playlistName=${playlist.playlistName}">
			<button>view</button>
			</a>
	</c:forEach>
	
	<form action="playlist?m=createPlaylist" method="post">
	New playlist<input type="text" placeholder="add name" name="newPlaylist"/>
	<input type="submit" value="create"/>
	</form>
</body>
</html>