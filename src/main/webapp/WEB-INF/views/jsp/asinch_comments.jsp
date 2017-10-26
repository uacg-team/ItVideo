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

		function likeComment(commentId,userId) {
			var request = new XMLHttpRequest();
			request.onreadystatechange =  function() {
				var like = document.getElementById("like");
			    var dislike = document.getElementById("dislike");
			    if(like.alt==="like" && dislike.alt==="dislike"){
			        like.alt="liked";
			        like.src="<c:url value="/pics/liked.png"/>";
			    }else if(like.alt==="like" && dislike.alt==="disliked"){
			    	like.alt="liked";
			    	like.src="<c:url value="/pics/liked.png"/>";
			    	dislike.alt="dislike";
			    	dislike.src="<c:url value="/pics/dislike.png"/>";
			    }else{
			        //like.alt=="liked"
			        like.alt="like";
			        like.src="<c:url value="/pics/like.png"/>";
			    }
			};
			var url = "commentLikeTest";
			var param1 = "commentId=";
			var param =param1.concat(commentId,"&like=1","&userId=",userId);
			request.open("POST", url, true);
			request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			request.send(param);
		}
		
		function dislikeComment() {
			
		}
		
		function likeButton(){
		    var like = document.getElementById("like");
		    var dislike = document.getElementById("dislike");
		    if(like.alt==="like" && dislike.alt==="dislike"){
		        like.alt="liked";
		        like.src="<c:url value="/pics/liked.png"/>";
		    }else if(like.alt==="like" && dislike.alt==="disliked"){
		    	like.alt="liked";
		    	like.src="<c:url value="/pics/liked.png"/>";
		    	dislike.alt="dislike";
		    	dislike.src="<c:url value="/pics/dislike.png"/>";
		    }else{
		        //like.alt=="liked"
		        like.alt="like";
		        like.src="<c:url value="/pics/like.png"/>";
		    }
		}
		
		function dislikeButton(){
			var like = document.getElementById("like");
			var dislike = document.getElementById("dislike");
			if(like.alt==="like" && dislike.alt==="dislike"){
				dislike.alt="disliked";
				dislike.src="<c:url value="/pics/disliked.png"/>";
			}else if(dislike.alt==="dislike" && like.alt==="liked"){
				dislike.alt="disliked";
				dislike.src="<c:url value="/pics/disliked.png"/>";
				like.alt="like";
				like.src="<c:url value="/pics/like.png"/>";
			}else{
				//dislike.alt=="disliked"
				dislike.alt="dislike";
				dislike.src="<c:url value="/pics/dislike.png"/>";
			}
		}

	</script>
</head>
<body>
<!-- display: inline-block -->
hello

<div>
	<img alt="like" id="like" src="<c:url value="/pics/like.png"/>" style="width: 50px; height: auto" onclick="likeButton()">
	<img alt="dislike" id="dislike" src="<c:url value="/pics/dislike.png"/>" style="width: 50px; height: auto" onclick="dislikeButton()">
</div>
<c:forEach items="${requestScope.comments}" var="comment">

	
	<img src="<c:url value="/img/${comment.userId}"/>" width="50px" height="auto"/>

	<div class="comment-box">
			<p class="comment-header"><span>${comment.username}</span></p>
		<div class="comment-box-inner"> 
					 	<p class="comment-box-inner">${comment.text}</p> <br>
			</div>
			<div class="triangle-comment">
				</div>
				<p class="comment-date">${comment.date}</p>
				<div class="like-buttons">
				<ul>
 				<li>
  				<p>${comment.likes} likes</p>
  			</li>
  			<li>
  				<form action="<c:url value="/commentLikeTest"/>" method="post">
  					<input type="hidden" value="${comment.commentId}" name="commentId">
					<input type="hidden" value="1" name="like">
					<input type="submit" value="like"/>
				</form>
			</li>
			<li>
				<p>${comment.dislikes} dislikes</p>
			</li>
			<li>
				<form action="<c:url value="/commentLikeTest"/>" method="post">
					<input type="hidden" value="${comment.commentId}" name="commentId">
					<input type="hidden" value="-1" name="like">
					<input type="submit" value="dislike"/>
				</form>
			</li>
		</ul>
		${comment.commentId}
		${user.userId }
  		<img alt="like" id="like" src="<c:url value="/pics/like.png"/>" style="width: 50px; height: auto" onclick="likeComment(${comment.commentId},${user.userId })">
		</div>
	</div>
</c:forEach>
</body>
</html>