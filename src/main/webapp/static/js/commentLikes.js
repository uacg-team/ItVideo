/**
 * @param commentId -comment which will be liked
 * @param userId-depends from viewer id
 * @returns
 */
function likeComment(commentId,userId) {
	if (typeof userId === 'undefined') {
	    alert("First login!");
	    return;
	}
	var request = new XMLHttpRequest();
	request.onreadystatechange =  function() {
		var like = document.getElementById("like".concat(commentId));
	    var dislike = document.getElementById("dislike".concat(commentId));
	   
	    var likes = document.getElementById("likes".concat(commentId));
	    var countLikes = likes.innerHTML;
	    var dislikes = document.getElementById("dislikes".concat(commentId));
	    var countDislikes = dislikes.innerHTML;
	    
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
	};
	var url = "/ItVideo/player/commentLikeTest";
	var param1 = "commentId=";
	var param =param1.concat(commentId,"&like=1","&userId=",userId);
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}
/**
 * @param commentId-comment which will be dislike
 * @param userId-depends from viewer id
 * @returns
 */
function dislikeComment(commentId,userId) {
	if (typeof userId === 'undefined') {
	    alert("First login!");
	    return;
	}
	var request = new XMLHttpRequest();
	request.onreadystatechange =  function() {
		var like = document.getElementById("like".concat(commentId));
	    var dislike = document.getElementById("dislike".concat(commentId));
	    
	    var likes = document.getElementById("likes".concat(commentId));
	    var countLikes = likes.innerHTML;
	    var dislikes = document.getElementById("dislikes".concat(commentId));
	    var countDislikes = dislikes.innerHTML;
	    
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
	};
	var url = "/ItVideo/player/commentLikeTest";
	var param1 = "commentId=";
	var param =param1.concat(commentId,"&like=-1","&userId=",userId);
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}