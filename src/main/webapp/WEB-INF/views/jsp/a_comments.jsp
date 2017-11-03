<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>
<script type="text/javascript" src="<c:url value="/js/commentBuilder-v1.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/commentLikes.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/dateParser.js"/>"></script>
<script type="text/javascript">
function getSelected(){
	var e=document.getElementById("choiseCompareComments");	
	return e.options[e.selectedIndex].value;
}
</script>
<style type="text/css">
.addNewComment img{
width: 60px;
height: 60px;
margin-left: 0px;
}
.comment-box-user img{
width: 60px;
height: 60px;
margin-left: 0px;
}
.comment-box comment-box-inner{
float: right;
}
.comment-box comment-box-user{
float: left;
}
.comment-buttons-left{
padding-left: 0;
}
.comment-buttons-left button{
float: left;
}
.comment-buttons-right button{
float: right;
margin-left: 5px;
margin-right: 5px;
}
.like-buttons ul{
float: right;
}
.reply-box-user img{
width: 50px;
height: 50px;
margin-left: 0px;
}
textarea { resize: none; }
.comment-box-inner p{
	word-wrap: break-word;
}
.comment-sort-by{

}
</style>
</head>
<body>
	<strong>Leave comment...</strong><br>
	<p style=" font-weight: bold;" class="comment-header" id="myUsername">${sessionScope.user.username}</p>
	<c:if test="${sessionScope.user==null}">
		<a href="<c:url value="/login"/>">First login</a>
	</c:if>
	
	<div id="addNewComment">
		<div class="comment-box-user col-lg-1 container-fluid" style="padding-left: 0px;">
			<c:if test="${sessionScope.user!=null}">
				<img src="<c:url value="/img/${sessionScope.user.userId}"/>"/>
			</c:if>
			<c:if test="${sessionScope.user==null}">
				<img src="<c:url value="/pics/avatar.png"/>"/>
			</c:if>
		</div>
		<div class="col-lg-11 container-fluid">
			<c:if test="${sessionScope.user!=null}">
				<textarea class="form-control" rows="3" cols="80" id="novComentar"></textarea>
				<button style="float: right; margin: 5px;" class="btn btn-primary btn-xs" onclick="postComment(${sessionScope.user.userId},${requestScope.mainVideo.videoId},0,'${sessionScope.user.username}')">addComment</button>
			</c:if>
			<c:if test="${sessionScope.user==null}">
				<textarea class="form-control" rows="3" cols="80" id="novComentar" ></textarea>
				<button class="btn btn-primary btn-xs" style="float: right; margin: 5px;">addComment</button>
			</c:if>
		</div>
	</div>
<div class="row">
	<hr>
</div>
<div class="row">
	<div class="col-lg-8">
		<strong><span>Comments:</span><span id="countComments">${requestScope.countComments}</span></strong>
	</div>
	<div id="initial-settings" class="col-lg-4">
		<div class="col-lg-6">
			<select class="form-control comment-sort-by" id="choiseCompareComments" name="compare">
			  <option value="newest" selected="selected">Newest</option>
			  <option value="oldest">Oldest</option>
			  <option value="mostLiked">Most liked</option>
			  <option value="mostDisliked">Most disliked</option>
			</select>
		</div>
		<div class="col-lg-6">
			<c:if test="${sessionScope.user!=null}">
			<button class="btn btn-primary" onclick="comments(${sessionScope.user.userId},${requestScope.mainVideo.videoId},getSelected(),1,2,50)">Show comments</button>
			</c:if>
			<c:if test="${sessionScope.user==null}">
			<button class="btn btn-primary" onclick="comments(0,${requestScope.mainVideo.videoId},getSelected(),1,2,50)">Show comments</button>
			</c:if>
		</div>
	</div>
</div>
<div class="row">
<hr>
</div>
<div id="newComments" title="0" >
<!-- insert new Comments -->
</div>
<div id="initialComments">
</div>
<div id="comments">
<!-- place for comments -->
</div>
</body>
</html>