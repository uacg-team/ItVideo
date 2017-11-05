/**
 * @param commentId -comment which will be liked
 * @param userId-depends from viewer id
 * @returns
 */
function likeComment(commentId,myUserId) {
	if (typeof myUserId === 'undefined' || myUserId === 0) {
	    alert("First login!");
	    return;
	}
	var request = new XMLHttpRequest();
	request.onreadystatechange =  function() {
		if (this.readyState == 4) {
			var like = document.getElementById("like".concat(commentId));
		    var dislike = document.getElementById("dislike".concat(commentId));
		    var likes = document.getElementById("likes".concat(commentId));
		    var countLikes = likes.innerHTML;
		    var dislikes = document.getElementById("dislikes".concat(commentId));
		    var countDislikes = dislikes.innerHTML;
			if(this.status == 200){
			    if(like.alt==="like" && dislike.alt==="dislike"){
			    	countLikes++;
			    	likes.innerHTML=countLikes;
			        like.alt="liked";
			        like.src="/ItVideo/pics/liked.png";
			    }else if(like.alt==="like" && dislike.alt==="disliked"){
			    	countLikes++;
			    	likes.innerHTML=countLikes;
			    	countDislikes--;
			    	dislikes.innerHTML=countDislikes;
			    	like.alt="liked";
			    	like.src="/ItVideo/pics/liked.png";
			    	dislike.alt="dislike";
			    	dislike.src="/ItVideo/pics/dislike.png";
			    }else{
			        //like.alt=="liked"
			        countLikes--;
			        likes.innerHTML=countLikes;
			        like.alt="like";
			        like.src="/ItVideo/pics/like.png";
			    }
			}else if(this.status == 401){
				alert("Please login!");
			}else if(this.status == 500){
				alert("Our team has been alerted of the issue, we are looking into it immediately");
			}
		}
	};
	var url = "/ItVideo/player/commentLike";
	var param1 = "commentId=";
	var param =param1.concat(commentId,"&like=1","&userId=",myUserId);
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}
/**
 * @param commentId-comment which will be dislike
 * @param userId-depends from viewer id
 * @returns
 */
function dislikeComment(commentId,myUserId) {
	if (typeof myUserId === 'undefined') {
	    alert("First login!");
	    return;
	}
	if (myUserId === 0) {
	    alert("First login!");
	    return;
	}
	var request = new XMLHttpRequest();
	request.onreadystatechange =  function() {
		if (this.readyState == 4) {
			var like = document.getElementById("like".concat(commentId));
		    var dislike = document.getElementById("dislike".concat(commentId));
		    var likes = document.getElementById("likes".concat(commentId));
		    var countLikes = likes.innerHTML;
		    var dislikes = document.getElementById("dislikes".concat(commentId));
		    var countDislikes = dislikes.innerHTML;
			if(this.status == 200){
			    if(like.alt==="like" && dislike.alt==="dislike"){
			    	countDislikes++;
			    	dislikes.innerHTML=countDislikes;
			    	dislike.alt="disliked";
					dislike.src="/ItVideo/pics/disliked.png";
				}else if(dislike.alt==="dislike" && like.alt==="liked"){
					countDislikes++;
					dislikes.innerHTML=countDislikes;
					countLikes--;
					likes.innerHTML=countLikes;
					dislike.alt="disliked";
					dislike.src="/ItVideo/pics/disliked.png";
					like.alt="like";
					like.src="/ItVideo/pics/like.png";
				}else{
					countDislikes--;
					dislikes.innerHTML=countDislikes;
					//dislike.alt=="disliked"
					dislike.alt="dislike";
					dislike.src="/ItVideo/pics/dislike.png";
				}
			}else if(this.status == 401){
				alert("Please login!");
			}else if(this.status == 500){
				alert("Our team has been alerted of the issue, we are looking into it immediately");
			}
		}
	};
	var url = "/ItVideo/player/commentLike";
	var param1 = "commentId=";
	var param =param1.concat(commentId,"&like=-1","&userId=",myUserId);
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}