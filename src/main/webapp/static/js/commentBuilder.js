/**
 * @param myUserId - for likes dislikes
 * @param allCommentsNumber - for show more button 
 * @param part - commentsPerClick*part starting point in search
 * @param commentsPerClick -number of received comments
 * @param video_id -search comments for this video
 * @param startFrom -append html for div with this id
 * @returns 
 */
function comments(myUserId, allCommentsNumber, part, commentsPerClick,video_id,startFrom){
	//TODO add button show more if comments showed<all comments
	//if startFrom!=undefined append to last comment
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var listComments = JSON.parse(this.responseText);
			var commentsCount = listComments.length;
			var htmlComments="";
			for (var i = 0; i < commentsCount; i++) {
				var comment = listComments[i];;
				//parsing one comment
				var commentId=comment.commentId;
				var text = comment.text;
				var userId=comment.userId;
				var videoId=comment.videoId;
				var replyId=comment.replyId;
				var date = comment.date;
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

				htmlComments=htmlComments.concat(buildComment(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,date,myUserId));
				//if more comments show button show more
			}
			var videoComments = document.getElementById("comments");
			videoComments.innerHTML =htmlComments;
		}
	}
	var url="player/getCommentsWithVotes/"+video_id+"/"+part+"/"+commentsPerClick;
	request.open("GET", "player/getCommentsWithVotes", true);
	request.send();
}
/**
 * Build one comment
 * @param commentId
 * @param text
 * @param userId
 * @param videoId
 * @param replyId
 * @param replies
 * @param hasReplies
 * @param likes
 * @param dislikes
 * @param username
 * @param url
 * @param vote
 * @param date
 * @param myUserId-depends from viewer
 * @returns html for one comment
 */
function buildComment(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,date,myUserId){
	var htmlOneComment='<img src="/ItVideo/img/' + userId + '" width="50px" height="auto"/>';
	htmlOneComment=htmlOneComment.concat('<div id="' + commentId + '" class="comment-box">');
	htmlOneComment=htmlOneComment.concat('<p class="comment-header"><span>' + username + '</span></p>');
	htmlOneComment=htmlOneComment.concat('<div class="comment-box-inner">');
	htmlOneComment=htmlOneComment.concat('<p class="comment-box-inner">' + text + '</p> <br>');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="triangle-comment"></div>');
	htmlOneComment=htmlOneComment.concat('<p class="comment-date">' + dateParse(date) + '</p>');
	
	htmlOneComment=htmlOneComment.concat('<div class="like-buttons">');
	htmlOneComment=htmlOneComment.concat('<ul>');
	htmlOneComment=htmlOneComment.concat('<li>');
	htmlOneComment=htmlOneComment.concat('<p id="likes'+ commentId + '">' + likes + '</p>');
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('<li>');
	if(vote === 1){
		htmlOneComment=htmlOneComment.concat('<img alt="liked" id="like' + commentId + '" src="/ItVideo/pics/liked.png" style="width: 25px; height: auto" onclick="likeComment(' + commentId + ',' + myUserId + ')">');
	}else{
		htmlOneComment=htmlOneComment.concat('<img alt="like" id="like' + commentId + '" src="/ItVideo/pics/like.png" style="width: 25px; height: auto" onclick="likeComment(' + commentId + ',' + myUserId + ')">');
	}
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('<li>');
	htmlOneComment=htmlOneComment.concat('<p id="dislikes' + commentId + '">' + dislikes + '</p>');
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('<li>');
	if(vote === -1){
		htmlOneComment=htmlOneComment.concat('<img alt="disliked" id="dislike' + commentId + '" src="/ItVideo/pics/disliked.png" style="width: 25px; height: auto" onclick="dislikeComment(' + commentId + ',' + myUserId + ')">');
	}else{
		htmlOneComment=htmlOneComment.concat('<img alt="dislike" id="dislike' + commentId + '" src="/ItVideo/pics/dislike.png" style="width: 25px; height: auto" onclick="dislikeComment(' + commentId + ',' + myUserId + ')">');
	}
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('</ul>');
	htmlOneComment=htmlOneComment.concat('</div>');
	
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<br>');
	//if hasReplies -add button show replies
	//add button reply
	return htmlOneComment;
}

function buildReply(commentId, text, userId, videoId, replyId, likes, dislikes, username, url, vote, date, myUserId){
	//build html for comment
}
function showReplies(commentId,myUserId){
	//show all replies for comment
}
function htmlShowMoreComments(commentsShow,allCommentsNumber){
	//show button if there is more comments
}
function htmlShowReplies(hasReplies,commentId){
	//apend button show replies if has replies
	var html = "";
	if(hasReplies){
		html="";
		/*
		 <div id="replies'+commentId+'">
		<button onclick="comments(6)">show replies</button>
		</div>
		*/

	}
	return html;
}