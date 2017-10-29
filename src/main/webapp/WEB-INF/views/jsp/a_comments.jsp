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
//obtain selected option from menu
function getComparator(){
	
}
</script>
</head>
<body>
<div id="addNewComment">
	<strong>Leave comment</strong><br>
	<strong>${sessionScope.user.username}</strong>
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
	<form action="<c:url value="/addComment"/>" method="post">
		<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId"/>
		<!-- New Comment<input type="text" placeholder="add comment" name="newComment"/>
		<br> -->
		<textarea rows="3" cols="80" name="newComment"></textarea>
		<input type="submit" value="comment"/>
	</form>
	</li>
	</ul>
</div>
<br>
<hr>
<div id="comments">
<ul>
	<li>
		<select id="choiseCompareComments" name="compare">
		  <option value="latest" selected="selected">Latest</option>
		  <option value="oldest">Oldest</option>
		</select>
	</li>
	<li>
	<c:if test="${sessionScope.user!=null}">
	<button onclick="comments(${sessionScope.user.userId},${requestScope.mainVideo.videoId},'comp',1,2,50)">Show comments</button>
	</c:if>
	<c:if test="${sessionScope.user==null}">
	<button onclick="comments(0,${requestScope.mainVideo.videoId},'comp',1,2,50)">Show comments</button>
	</c:if>
	</li>
</ul>
</div>
</body>
</html>