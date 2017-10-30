/**
 * AJAX call for videoId,myUserId comments
 * @param myUserId - for likes dislikes
 * @param allCommentsNumber - for show more button 
 * @param part - commentsPerClick*part starting point in search
 * @param commentsPerClick -number of received comments
 * @param video_id -search comments for this video
 * @param startFrom -append html for div with this id
 * @returns 
 */
function comments(myUserId, video_id, comparator, part, commentsPerClick, allCommentsNumber){
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
				var numberReplies=comment.numberReplies;
				htmlComments=htmlComments.concat(buildComment(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,date,numberReplies,myUserId));
				//if more comments show button show more
			}
			var videoComments = document.getElementById("comments");
			videoComments.innerHTML =htmlComments;
		}
	}
	if (typeof myUserId === 'undefined') {
	    myUserId = 0;
	}
	if (typeof comparator === 'undefined') {
		comparator = 0;
	}
	//TODO delete player
	//player/getCommentsWithVotes/{videoId}/{myUserId}/{compare}/{part}/{countReplies}
	var url="getCommentsWithVotes/" + video_id + "/" + myUserId + "/"+comparator+"/" + part + "/" + commentsPerClick;
	request.open("GET", url, true);
	request.send();
}
/**
 * AJAX call for commentId, myUserId, comparator replies
 * @param commentId
 * @param myUserId
 * @param comparator
 * @returns
 */
function showReplies(commentId, myUserId, comparator){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var listComments = JSON.parse(this.responseText);
			var commentsCount = listComments.length;
			var htmlComments="";
			for (var i = 0; i < commentsCount; i++) {
				var comment = listComments[i];;
				//parsing one comment
				var rcommentId=comment.commentId;
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
				var numberReplies=comment.numberReplies;
				htmlComments=htmlComments.concat(buildReply(rcommentId, text, userId, videoId, replyId, likes, dislikes, username, url, vote, date, myUserId));
				
			}
		var div = document.getElementById('viewReplies'+commentId);
	    div.innerHTML=htmlComments;
		}
	}
	if (typeof myUserId === 'undefined') {
	    myUserId=0;
	}
	if (typeof comparator === 'undefined') {
		comparator=0;
	}
	//TODO delete reply
	var url="getRepliesWithVotes/"+commentId+"/"+myUserId+"/"+comparator;
	request.open("GET", url, true);
	request.send();
	//show all replies for comment
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
 * @param numberReplies
 * @param myUserId-depends from viewer
 * @returns html for one comment
 */
function buildComment(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,date,numberReplies,myUserId,comparator){
	var htmlOneComment="";
	htmlOneComment=htmlOneComment.concat('<img src="/ItVideo/img/' + userId + '" width="50px" height="auto"/>');
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
	htmlOneComment=htmlOneComment.concat('<br>');
	//if hasReplies -add button show replies
	if(numberReplies>0){
		htmlOneComment=htmlOneComment.concat('<div id="viewReplies' + commentId + '">');
			htmlOneComment=htmlOneComment.concat('<button onclick="showReplies(' + commentId + ',' + myUserId + ',' + comparator + ')">show replies('+numberReplies+')</button>');
		htmlOneComment=htmlOneComment.concat('</div>');
	}
	//add button reply
	return htmlOneComment;
}

function buildReply(commentId, text, userId, videoId, replyId, likes, dislikes, username, url, vote, date, myUserId){
	//build html for comment
	var htmlOneComment='<img src="/ItVideo/img/' + userId + '" width="50px" height="auto"/>';
	htmlOneComment=htmlOneComment.concat('<div class="reply-box">');
	htmlOneComment=htmlOneComment.concat('<p class="reply-header"><span>' + username + '</span></p>');
	htmlOneComment=htmlOneComment.concat('<div class="reply-box-inner">');
	htmlOneComment=htmlOneComment.concat('<p>' + text + '</p><br>');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="triangle-comment"></div>');
	htmlOneComment=htmlOneComment.concat('<p class="comment-date">' + dateParse(date) + '</p>');
	htmlOneComment=htmlOneComment.concat('</div>');
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
	htmlOneComment=htmlOneComment.concat('<br>');
	
	return htmlOneComment;
}

function postComment(myUserId,videoId,replyId,username) {
	//String text, LocalDateTime date, long userId, long videoId, long replyId
	if (typeof myUserId === 'undefined') {
	    alert("First login!");
	    return;
	}
	var text = document.getElementById("novComentar").value
	var url = "/ItVideo/player/addComment";
	var param = "videoId=" + videoId + "&myUserId=" + myUserId + "&text=" + text+ "&replyId=" + replyId;
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			//clear text field
			document.getElementById("novComentar").value="";
			var lastComment = JSON.parse(this.responseText);
			
			//parsing one comment
			var commentId=lastComment.commentId;
			var text = lastComment.text;
			var userId=lastComment.userId;
			var videoId=lastComment.videoId;
			var date = lastComment.date;
			
			var htmlComment=buildComment(commentId,text,userId,videoId,0,null,false,0,0,username,' ',0,date,0,myUserId);
			var insertion=document.getElementById("newComments");
			if(insertion.title == 0){
				insertion.innerHTML=htmlComment;
				insertion.title="1";
			}else{
				insertion.insertAdjacentHTML('beforeend', htmlComment);
			}
		}
	}
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}
function deleteComment(commentId){
	//post delete comment from db
	//delete comment div
}
function postReply(){
	
}


function htmlShowMoreComments(commentsShow,allCommentsNumber){
	//show button if there is more comments
}

function showButton(insertionDivName,deleteDivElement,html){
	//remove button with name deleteDivElement
	var elem = document.getElementById(deleteDivElement);
    elem.parentNode.removeChild(elem);
	//add html in div with name insertionDivName
    var testDiv = document.getElementById(insertionDivName);
    testDiv.insertAdjacentHTML('beforeend', html);
}
