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
<b>add to playlist</b>
<select name="addToPlaylist" onchange="location = this.value;">
	<c:forEach items="${requestScope.myPlaylists}" var="playlist">
		 <option value="with ajax send post to playlist?playlistName=${playlist.playlistName}">${playlist.playlistName}</option>
	</c:forEach>
	
</select>
		
</body>
</html>