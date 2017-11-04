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
	var insertedCommentsBeforeShow=document.getElementById("newComments");
	if(insertedCommentsBeforeShow.title != "0"){
		insertedCommentsBeforeShow.innerHTML="";
	}
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
			document.getElementById('viewRepliesButton'+commentId).innerHTML="";
		}
	}
	if (typeof myUserId === 'undefined') {
	    myUserId=0;
	}
	if (typeof comparator === 'undefined') {
		comparator=0;
	}
	var url="getRepliesWithVotes/"+commentId+"/"+myUserId+"/"+comparator;
	request.open("GET", url, true);
	request.send();
}
/**
 * @returns html for one comment
 */
function buildComment(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,date,numberReplies,myUserId,comparator){
	var htmlOneComment="";
	htmlOneComment=htmlOneComment.concat('<div id="' + commentId + '">');
	htmlOneComment=htmlOneComment.concat('<p hidden id="numberRepliesForComment'+commentId+'">'+numberReplies+'</p>');
	htmlOneComment=htmlOneComment.concat('<div class="comment-box">');
	htmlOneComment=htmlOneComment.concat('<a href=/ItVideo/viewProfile/' + userId + '>');
	htmlOneComment=htmlOneComment.concat('<p class="comment-header"><span>' + username + '</span></p>');
	htmlOneComment=htmlOneComment.concat('</a>');
	htmlOneComment=htmlOneComment.concat('<div class="comment-box-user col-lg-1 container-fluid" style="padding-left: 0px;">');
	htmlOneComment=htmlOneComment.concat('<a href=/ItVideo/viewProfile/' + userId + '>');
	htmlOneComment=htmlOneComment.concat('<img src="/ItVideo/img/' + userId + '"/>');
	htmlOneComment=htmlOneComment.concat('</a>');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="comment-box-inner col-lg-11 container-fluid">');
	htmlOneComment=htmlOneComment.concat('<p >' + text + '</p> <br>');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<p class="comment-date">' + dateParse(date) + '</p>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2 comment-buttons-left">');
	//if hasReplies -add button show replies
	if(numberReplies>0){
		htmlOneComment=htmlOneComment.concat('<div  id="viewRepliesButton' + commentId + '">');
			htmlOneComment=htmlOneComment.concat('<button class="btn btn-primary btn-xs" onclick="showReplies(' + commentId + ',' + myUserId + ',' + comparator + ')">show replies('+numberReplies+')</button>');
		htmlOneComment=htmlOneComment.concat('</div>');
	}
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	//add button reply
	if(myUserId != 'undefined'||myUserId!=0){
		htmlOneComment=htmlOneComment.concat('<div class="comment-buttons-right">');
			htmlOneComment=htmlOneComment.concat('<button class="btn btn-primary btn-xs" onclick="addReplyPopUpHtml('+myUserId+','+videoId+','+commentId+',\''+username+'\')">add reply</button>');
		htmlOneComment=htmlOneComment.concat('</div>');
	}
	//add button delete
	if(myUserId == userId){
		htmlOneComment=htmlOneComment.concat('<div class="comment-buttons-right" id="deleteComment' + commentId + '">');
			htmlOneComment=htmlOneComment.concat('<button class="btn btn-danger btn-xs" onclick="deleteComment('+commentId+')">delete</button>');
		htmlOneComment=htmlOneComment.concat('</div>');
	}
	htmlOneComment=htmlOneComment.concat('</div>');
	//likes dislikes
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2 like-buttons">');
	htmlOneComment=htmlOneComment.concat('<ul>');
	htmlOneComment=htmlOneComment.concat('<li>');
	htmlOneComment=htmlOneComment.concat('<span class="label label-primary" id="likes'+ commentId + '">' + likes + '</span>');
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('<li>');
	if(vote === 1){
		htmlOneComment=htmlOneComment.concat('<img alt="liked" id="like' + commentId + '" src="/ItVideo/pics/liked.png" style="width: 25px; height: auto" onclick="likeComment(' + commentId + ',' + myUserId + ')">');
	}else{
		htmlOneComment=htmlOneComment.concat('<img alt="like" id="like' + commentId + '" src="/ItVideo/pics/like.png" style="width: 25px; height: auto" onclick="likeComment(' + commentId + ',' + myUserId + ')">');
	}
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('<li>');
	htmlOneComment=htmlOneComment.concat('<span class="label label-primary" id="dislikes' + commentId + '">' + dislikes + '</span>');
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
	
	//insertion point for new comment:
	htmlOneComment=htmlOneComment.concat('<div id="addReply' + commentId + '">');
	htmlOneComment=htmlOneComment.concat('</div>');
	//insertion point for replies:
	htmlOneComment=htmlOneComment.concat('<div id="viewReplies' + commentId + '">');
	htmlOneComment=htmlOneComment.concat('</div>');
	
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('</div>');
	return htmlOneComment;
}

function buildReply(commentId, text, userId, videoId, replyId, likes, dislikes, username, url, vote, date, myUserId){
	//build html for comment
	var htmlOneComment="";
	htmlOneComment=htmlOneComment.concat('<div id='+commentId+'>');
	htmlOneComment=htmlOneComment.concat('<div class="reply-box">');
	htmlOneComment=htmlOneComment.concat('<a href=/ItVideo/viewProfile/' + userId + '>');
	htmlOneComment=htmlOneComment.concat('<p class="reply-header"><span>' + username + '</span></p>');
	htmlOneComment=htmlOneComment.concat('</a>');
	htmlOneComment=htmlOneComment.concat('<div class="reply-box-user col-lg-1 container-fluid" style="padding-left: 0px;">');
	htmlOneComment=htmlOneComment.concat('<a href=/ItVideo/viewProfile/' + userId + '>');
	htmlOneComment=htmlOneComment.concat('<img src="/ItVideo/img/' + userId + '"/>');
	htmlOneComment=htmlOneComment.concat('</a>');
	htmlOneComment=htmlOneComment.concat('</div>');
	
	htmlOneComment=htmlOneComment.concat('<div class="reply-box-inner col-lg-11 container-fluid">');
	htmlOneComment=htmlOneComment.concat('<p>' + text + '</p><br>');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<p class="comment-date">' + dateParse(date) + '</p>');
	htmlOneComment=htmlOneComment.concat('</div>');
	
	
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2">');
	if(myUserId == userId){
		htmlOneComment=htmlOneComment.concat('<div id="deleteComment' + commentId + '">');
			htmlOneComment=htmlOneComment.concat('<button class="btn btn-danger btn-xs" onclick="deleteComment('+commentId+')">delete</button>');
		htmlOneComment=htmlOneComment.concat('</div>');
	}
	htmlOneComment=htmlOneComment.concat('</div>');
	htmlOneComment=htmlOneComment.concat('<div class="col-lg-2 like-buttons">');
	htmlOneComment=htmlOneComment.concat('<ul>');
	htmlOneComment=htmlOneComment.concat('<li>');
	htmlOneComment=htmlOneComment.concat('<p class="label label-primary" id="likes'+ commentId + '">' + likes + '</p>');
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('<li>');
	if(vote === 1){
		htmlOneComment=htmlOneComment.concat('<img alt="liked" id="like' + commentId + '" src="/ItVideo/pics/liked.png" style="width: 25px; height: auto" onclick="likeComment(' + commentId + ',' + myUserId + ')">');
	}else{
		htmlOneComment=htmlOneComment.concat('<img alt="like" id="like' + commentId + '" src="/ItVideo/pics/like.png" style="width: 25px; height: auto" onclick="likeComment(' + commentId + ',' + myUserId + ')">');
	}
	htmlOneComment=htmlOneComment.concat('</li>');
	htmlOneComment=htmlOneComment.concat('<li>');
	htmlOneComment=htmlOneComment.concat('<p class="label label-primary" id="dislikes' + commentId + '">' + dislikes + '</p>');
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
	return htmlOneComment;
}

function postComment(myUserId,videoId,replyId,username) {
	if (typeof myUserId === 'undefined') {
	    alert("First login!");
	    return;
	}
	var text = document.getElementById("novComentar").value;
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
			//creating comment html
			var htmlComment=buildComment(commentId,text,userId,videoId,0,null,false,0,0,username,' ',0,date,0,myUserId);
			var insertion=document.getElementById("newComments");
			if(insertion.title == 0){
				insertion.innerHTML=htmlComment;
				insertion.title="1";
			}else{
				insertion.title=Number(insertion.title="1")+1;
				insertion.insertAdjacentHTML('afterbegin', htmlComment);
			}
			//increase number of comments
			var countComments=document.getElementById("countComments").innerText;
			document.getElementById("countComments").innerText=(Number(countComments)+1);
		}
	}
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}

function postReply(myUserId,videoId,replyId,username){
	if (typeof myUserId === 'undefined') {
	    alert("First login!");
	    return;
	}
	var text = document.getElementById("novReplyText"+replyId).value;
	var url = "/ItVideo/player/addComment";
	var param = "videoId=" + videoId + "&myUserId=" + myUserId + "&text=" + text+ "&replyId=" + replyId;
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			//clear text field
			var lastComment = JSON.parse(this.responseText);
			
			//parsing one comment
			var commentId=lastComment.commentId;
			var replyId=lastComment.replyId;
			var text = lastComment.text;
			var userId=lastComment.userId;
			var videoId=lastComment.videoId;
			var date = lastComment.date;
				//(commentId,text,userId,videoId,replyId,replies,hasReplies,likes,dislikes,username,url,vote,date,numberReplies,myUserId,comparator)
				//(commentId, text, userId, videoId, replyId, likes, dislikes, username, url, vote, date, myUserId)
			var htmlComment=buildReply(commentId,text,userId,videoId,replyId,0,0,username,' ',0,date,userId);
			
			var insertion=document.getElementById("addReply"+replyId);
			insertion.innerHTML=htmlComment;
			//increase number of comments
			document.getElementById("numberRepliesForComment"+replyId).innerHTML=(Number(document.getElementById("numberRepliesForComment"+replyId).innerHTML)+1);
			var countComments=document.getElementById("countComments").innerText;
			document.getElementById("countComments").innerText=(Number(countComments)+1);
		}
	}
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}

function addReplyPopUpHtml(myUserId,videoId,replyId,username){
	//insert form to input new Comment
	if(myUserId == 'undefined'||myUserId==0){
		alert("First login!");
		return;
	}
	var myUsername=document.getElementById('myUsername').innerHTML;
	var html="";
	html=html.concat(' <p class="comment-header"><strong>'+myUsername+'</strong></p>');
	html=html.concat('<div class="comment-box-user col-lg-1 container-fluid" style="padding-left: 0px;">');
	html=html.concat('<img  src="/ItVideo/img/' + myUserId + '" height="50px" width="auto" />');
	html=html.concat('</div>');
	html=html.concat('<div class="col-lg-11 container-fluid">');
	html=html.concat('<textarea  class="form-control" rows="3" cols="80"  id="novReplyText'+replyId+'"></textarea>');
	html=html.concat('</div>');
	html=html.concat('<button style="float: right; margin: 5px;" class="btn btn-primary btn-xs" onclick="postReply(' + myUserId + ',' + videoId+',' + replyId+',\''+username+'\')">add reply</button>');
	document.getElementById('addReply' + replyId).innerHTML=html;
}

function deleteComment(commentId){
	var url = "/ItVideo/player/deleteComment";
	var param = "commentId=" + commentId;

	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			//get number of replies
			var numberReplies;
			if(document.getElementById("numberRepliesForComment"+commentId)==null){
				numberReplies=0;
			}else{
				numberReplies=document.getElementById("numberRepliesForComment"+commentId).innerHTML;
			}
			var elem = document.getElementById(commentId);
		    elem.parentNode.removeChild(elem);
		    // elem.parentNode.removeChild(elem);
		    //decrease number of comments
			var countComments=document.getElementById("countComments").innerText;
			document.getElementById("countComments").innerText=(Number(countComments)-1-numberReplies);
		}
	}
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}


function showButton(insertionDivName,deleteDivElement,html){
	//remove button with name deleteDivElement
	var elem = document.getElementById(deleteDivElement);
    elem.parentNode.removeChild(elem);
	//add html in div with name insertionDivName
    var testDiv = document.getElementById(insertionDivName);
    testDiv.insertAdjacentHTML('beforeend', html);
}