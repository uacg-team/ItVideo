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
				var like = document.getElementById("like".concat(commentId));
			    var dislike = document.getElementById("dislike".concat(commentId));
			    
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
		
		function dislikeComment(commentId,userId) {
			var request = new XMLHttpRequest();
			request.onreadystatechange =  function() {
				var like = document.getElementById("like".concat(commentId));
			    var dislike = document.getElementById("dislike".concat(commentId));
			    
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
			};
			var url = "commentLikeTest";
			var param1 = "commentId=";
			var param =param1.concat(commentId,"&like=1","&userId=",userId);
			request.open("POST", url, true);
			request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			request.send(param);
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
		 				<p id="likes${comment.commentId}">${comment.likes}</p>
		 			</li>
		 			<li>	
		 				<img alt="like" id="like${comment.commentId}" src="<c:url value="/pics/like.png"/>" style="width: 50px; height: auto" onclick="likeComment(${comment.commentId},${user.userId})">
					</li>
					<li>	
						<p id="dislikes${comment.commentId}">${comment.dislikes}</p>
		 			</li>
		 			<li>
		 				<img alt="dislike" id="dislike${comment.commentId}" src="<c:url value="/pics/dislike.png"/>" style="width: 50px; height: auto" onclick="dislikeComment(${comment.commentId},${user.userId})">
					</li>
				</ul>
			</div>
	</div>
</c:forEach>
</body>
</html>