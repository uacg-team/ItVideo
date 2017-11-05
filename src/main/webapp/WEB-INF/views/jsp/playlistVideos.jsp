<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<!-- delete, rename -->
	<jsp:include page="header.jsp"></jsp:include><br>
	<h3>Playlist: ${requestScope.playlistName}</h3><br>
	<c:if test="${fn:length(videos)==0}"><h3>Empty</h3></c:if>
	<jsp:include page="showVideosRequest.jsp"></jsp:include>
</body>
</html>