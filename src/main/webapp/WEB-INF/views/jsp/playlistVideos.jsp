<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function showForm(){
	var div = document.getElementById("edit-form");
	if(div.title==="none"){
		div.style.display="block";
		div.title="block";
	}else{
		div.style.display="none";
		div.title="none"
	}
}
</script>
<style type="text/css">
div.inline { 
	float:left; 
	margin:5px;
	padding: 5px;
	border-style: solid; 
	border-color: black; 
	border-width: 1px;
}
.edit-form {
display: none;
}
.center {
text-align:center;
}
.left {
text-align:left;
}
</style>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	<br>
	<div class="col-lg-1"></div>
	<div class="col-lg-10">
		<div class="well bs-component">
			<h3 class="center">Playlist: ${requestScope.playlistName}</h3><hr><br>
			<!-- delete -->
			<c:if test="${sessionScope.user.userId == requestScope.userId}">
			<div class="col-lg-4">
			<form action="deletePlaylist" method="post">
				<input type="hidden" value="${requestScope.playlistId}" name="playlistId">
				<input class="btn btn-danger" type="submit" value="delete"/>
			</form>
			</div>
			<!-- change name -->
			<div class="col-lg-8 right">
				<div class="col-lg-3">
				<button class="btn btn-warning" id="edit-button" onclick="showForm()">Edit playlist name</button>
				</div>
				<div id="edit-form" title="none" class="edit-form col-lg-9" >
					<form action=<c:url value="/editPlaylist"/> method="post">
						<input type="hidden" value="${requestScope.playlistId}" name="playlistId">
						<div class="col-lg-8">
							<input class="form-control" type="text" placeholder="${requestScope.playlistName}" name="newPlaylistName"/>
						</div>
						<div class="col-lg-4">
							<input class="btn btn-primary" type="submit" value="edit"/>
						</div>
					</form>
				</div>
			</div>
			<div class="row"></div><br><br>
			</c:if>
			<c:if test="${fn:length(videos)==0}"><h3>Empty</h3></c:if>
			<jsp:include page="showVideosRequest.jsp"></jsp:include>
		</div>
	</div>
	<div class="col-lg-1"></div>		
</body>
</html>