<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>
<script type="text/javascript" src="<c:url value="/js/commentBuilder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/commentLikes.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/dateParser.js"/>"></script>
<script type="text/javascript">
function getSelected(){
	var e=document.getElementById("choiseCompareComments");	
	return e.options[e.selectedIndex].value;
}
</script>
</head>
<body>
<div id="addNewComment">
	<strong>Leave comment</strong><br>
	<strong>${sessionScope.user.username}</strong>
	<c:if test="${sessionScope.user==null}">
			<a href="<c:url value="/login"/>">First login</a>
			
	</c:if>
	<ul>
	<li>
		<c:if test="${sessionScope.user!=null}">
		<img src="<c:url value="/img/${sessionScope.user.userId}"/>" height="50px" width="auto"/>
		</c:if>
		<c:if test="${sessionScope.user==null}">
		<img src="<c:url value="/pics/avatar.png"/>" height="50px" width="auto" />
		</c:if>
	</li>
	<li>
		<c:if test="${sessionScope.user!=null}">
			<textarea rows="3" cols="80" id="novComentar"></textarea>
			<button onclick="postComment(${sessionScope.user.userId},${requestScope.mainVideo.videoId},0,'${sessionScope.user.username}')">addComment</button>
		</c:if>
		<c:if test="${sessionScope.user==null}">
			<textarea rows="3" cols="80" id="novComentar"></textarea>
			<button>addComment</button>
		</c:if>
	</li>
	</ul>
</div>
<hr>
<div id="newComments" title="0" >
<!-- insert new Comments -->
</div>
<div id="initialComments">
<b><c:out value="Comments: ${requestScope.countComments}"></c:out></b>
<!-- insert new Comments -->
<!-- show first 5 comments if more -show button show more-->
</div>
<div id="comments">
<ul>
	<li>
		<select id="choiseCompareComments" name="compare">
		  <option value="newest" selected="selected">Newest</option>
		  <option value="oldest">Oldest</option>
		  <option value="mostLiked">Most liked</option>
		  <option value="mostDisliked">Most disliked</option>
		</select>
	</li>
	<li>
	<c:if test="${sessionScope.user!=null}">
	<button onclick="comments(${sessionScope.user.userId},${requestScope.mainVideo.videoId},getSelected(),1,2,50)">Show comments</button>
	</c:if>
	<c:if test="${sessionScope.user==null}">
	<button onclick="comments(0,${requestScope.mainVideo.videoId},getSelected(),1,2,50)">Show comments</button>
	</c:if>
	</li>
</ul>
</div>
</body>
</html>