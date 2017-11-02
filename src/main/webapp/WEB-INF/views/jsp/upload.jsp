<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	
	<c:if test="${ sessionScope.user == null }">
		<c:redirect url="/login"></c:redirect>
	</c:if>
	
	<c:if test="${ requestScope.error != null }">
		<div class="err">
			<c:out value="${requestScope.error}"></c:out>
		</div>
	</c:if>
	
	<c:if test="${ sessionScope.user != null }">
	<form action="<c:url value="/uploadVideo"/>" method="post"  enctype="multipart/form-data">
		<div  class="container">
			<fieldset>
				<legend>Upload</legend>
				<input type="text" placeholder="Enter video name" name="name" required  size="40"><br>
				<input type="text" placeholder="Enter video description" name="description" required size="40"><br>
				<input type="text" placeholder="Enter tags separated by space" name="tags"><br>
				<input type="radio" name="privacy" value="1" checked>Public 
				<input type="radio" name="privacy" value="2">Private<br>
				<input type="file" name="newVideo" accept="video/*"><br>
			 	<input type="submit" value="Upload">
			</fieldset>
		</div>
	</form>
	</c:if>
</body>
</html>