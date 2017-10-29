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

function comments(myUserId){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var listComments = JSON.parse(this.responseText);
			var commentsCount = listComments.length;
			var htmlOneComment="";
			for (var i = 0; i < commentsCount; i++) {
				var comment = listComments[i];;
				//parsing one comment
				var commentId=comment.commentId;
				console.log(commentId)
				var text = comment.text;
				var userId=comment.userId;
				var videoId=comment.videoId;
				var replyId=comment.replyId;
				//replies
				var replies=comment.replies;
				var hasReplies=comment.hasReplies;
				// likes/dislikes
				var likes=comment.likes;
				var dislikes=comment.dislikes;
				// userInfo
				var username=comment.username;
				var url=comment.url;
				// myVoteinfo
				var vote=comment.vote;

				htmlOneComment=htmlOneComment.concat(buildComment(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,myUserId));
			}
			var videoComments = document.getElementById("comments");
			videoComments.innerHTML =htmlOneComment;
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
		htmlOneComment=htmlOneComment.concat('<img alt="disliked" id="dislike' + commentId + '" src="<c:url value="/pics/disliked.png"/>" style="width: 25px; height: auto" onclick="dislikeComment(' + commentId + ',' + myUserId + ')">');
	}else{
		htmlOneComment=htmlOneComment.concat('<img alt="dislike" id="dislike' + commentId + '" src="<c:url value="/pics/dislike.png"/>" style="width: 25px; height: auto" onclick="dislikeComment(' + commentId + ',' + myUserId + ')">');
	}
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('</ul>');
	htmlOneComment=htmlOneComment.concat('</div>');
	
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<br>');
	return htmlOneComment;
}

function buildReply(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,myUserId){
	
}

function testHtml(){
	var videoComments = document.getElementById("videoComments");
	videoComments.innerHTML =buildComment(1,"ala bala",6,23,0,null,false,12,3,"vilio","alabala",1,6);
}
</script>
</head>
<body>

<div id="videoComments">
<button onclick="testHtml()">info</button>
</div>
show all comments
<div id="comments">
<button onclick="comments(6)">info</button>
</div>

</body>
</html>