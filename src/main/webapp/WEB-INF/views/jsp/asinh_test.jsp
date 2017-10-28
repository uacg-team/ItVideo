<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>
<script type="text/javascript">
function show() {
	var obj = JSON.parse('{ "name":"John", "age":30, "city":"New York"}');
	document.getElementById("demo").innerHTML = obj.name + ", " + obj.age;
}
function test(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var listComments = JSON.parse(this.responseText);
			var comment = listComments[0];
			var oniq = document.getElementById("oniq");
			oniq.innerHTML =comment.text;
		}
	}
	request.open("GET", "player/getCommentsWithVotes", true);
	request.send();
}
function showBigInfo(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var listComments = JSON.parse(this.responseText);
			var oniq = document.getElementById("showBigInfo");
			var content = '<img alt="like" id="like" src="<c:url value="/pics/like.png"/>" style="width: 50px; height: auto" onclick="likeButton()">';
			content=content.concat('<img src="<c:url value="/img/6"/>" width="50px" height="auto"/>');
			//content=content.concat('vypreki che e nechetimo</strong>');
			//oniq.innerHTML ='<p>raboti</p><strong>vypreki che e nechetimo</strong>';
			oniq.innerHTML =content;
		}
	}
	request.open("GET", "player/getCommentsWithVotes", true);
	request.send();
}
function comments(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var listComments = JSON.parse(this.responseText);
			var comment = listComments[0];
			var videoComments = document.getElementById("videoComments");
			var commentsCount = videoComments.length;
			//for (var i = 0; i < arrayLength; i++) {
				//parsing one comment
				var commentId="";
				var text = "";
				var uderId="";
				var videoId="";
				var replyId="";
				//replies
				var replies="";
				var hasReplies;
				// likes/dislikes
				var likes;
				var dislikes;
				// userInfo
				var username;
				var url;
				// myVoteinfo
				var vote;
			//}
				var htmlOneComment="";
			

		}
	}
	request.open("GET", "player/getCommentsWithVotes", true);
	request.send();
}
function buildComment(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,myUserId){
	var htmlOneComment='<img src="<c:url value="/img/' + userId + '"/>" width="50px" height="auto"/>';
	htmlOneComment=htmlOneComment.concat('<div class="comment-box">');
	htmlOneComment=htmlOneComment.concat('<p class="comment-header"><span>' + username + '</span></p>');
	htmlOneComment=htmlOneComment.concat('<div class="comment-box-inner">');
	htmlOneComment=htmlOneComment.concat('<p class="comment-box-inner">' + text + '</p> <br>');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="triangle-comment"></div>');
	htmlOneComment=htmlOneComment.concat('<p class="comment-date">still no date</p>');
	htmlOneComment=htmlOneComment.concat('<div class="like-buttons">');
	htmlOneComment=htmlOneComment.concat('<ul>');
	
	htmlOneComment=htmlOneComment.concat('<li>');
	htmlOneComment=htmlOneComment.concat('<p id="likes'+ commentId + '">' + likes + '</p>');
	htmlOneComment=htmlOneComment.concat('</li>');
	
	htmlOneComment=htmlOneComment.concat('<li>');
	if(vote === 1){
		htmlOneComment=htmlOneComment.concat('<img alt="liked" id="like' + commentId + '" src="<c:url value="/pics/liked.png"/>" style="width: 25px; height: auto" onclick="likeComment(' + commentId + ',' + myUserId + ')">');
	}else{
		htmlOneComment=htmlOneComment.concat('<img alt="like" id="like' + commentId + '" src="<c:url value="/pics/like.png"/>" style="width: 25px; height: auto" onclick="likeComment(' + commentId + ',' + myUserId + ')">');
	}
	htmlOneComment=htmlOneComment.concat('</li>');
	
	htmlOneComment=htmlOneComment.concat('<li>');
	htmlOneComment=htmlOneComment.concat('<p id="dislikes' + commentId + '">' + dislikes + '</p>');
	htmlOneComment=htmlOneComment.concat('</li>');
	
	htmlOneComment=htmlOneComment.concat('<li>');
	if(vote === -1){
		htmlOneComment=htmlOneComment.concat('<img alt="dislike" id="dislike' + commentId + '" src="<c:url value="/pics/dislike.png"/>" style="width: 25px; height: auto" onclick="dislikeComment(' + commentId + ',' + myUserId + ')">');
	}else{
		htmlOneComment=htmlOneComment.concat('<img alt="disliked" id="dislike' + commentId + '" src="<c:url value="/pics/disliked.png"/>" style="width: 25px; height: auto" onclick="dislikeComment(' + commentId + ',' + myUserId + ')">');
	}
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('</ul>');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('</div>');
	return htmlOneComment;
}
function testHtml(){
	var videoComments = document.getElementById("videoComments");
	videoComments.innerHTML =buildComment(1,"ala bala",6,23,0,null,false,12,3,"vilio","alabala",1,6);
}
</script>
</head>
<body>
<div class="show">
<button onclick="show()" class="dropbtnplaylists">info</button>
<p id="demo"></p>
</div>
<div class="showfirstComment">
<button onclick="test()">info</button>
<p id="allComments"></p>
</div>
<div id="showBigInfo">
<button onclick="showBigInfo()">info</button>
</div>
<div id="videoComments">
<button onclick="testHtml()">info</button>
</div>
</body>
</html>