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
			if (typeof userId === 'undefined') {
			    alert("First login!");
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
			        like.src="<c:url value="/pics/liked.png"/>";
			    }else if(like.alt==="like" && dislike.alt==="disliked"){
			    	countLikes++;
			    	likes.innerHTML=countLikes;
			    	countDislikes--;
			    	dislikes.innerHTML=countDislikes;
			    	like.alt="liked";
			    	like.src="<c:url value="/pics/liked.png"/>";
			    	dislike.alt="dislike";
			    	dislike.src="<c:url value="/pics/dislike.png"/>";
			    }else{
			        //like.alt=="liked"
			        countLikes--;
			        likes.innerHTML=countLikes;
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
			if (typeof userId === 'undefined') {
			    alert("First login!");
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
					dislike.src="<c:url value="/pics/disliked.png"/>";
				}else if(dislike.alt==="dislike" && like.alt==="liked"){
					countDislikes++;
					dislikes.innerHTML=countDislikes;
					countLikes--;
					likes.innerHTML=countLikes;
					dislike.alt="disliked";
					dislike.src="<c:url value="/pics/disliked.png"/>";
					like.alt="like";
					like.src="<c:url value="/pics/like.png"/>";
				}else{
					countDislikes--;
					dislikes.innerHTML=countDislikes;
					//dislike.alt=="disliked"
					dislike.alt="dislike";
					dislike.src="<c:url value="/pics/dislike.png"/>";
				}
			};
			var url = "commentLikeTest";
			var param1 = "commentId=";
			var param =param1.concat(commentId,"&like=-1","&userId=",userId);
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
${user.userId}
<div>
	<img alt="like" id="like" src="<c:url value="/pics/like.png"/>" style="width: 50px; height: auto" onclick="likeButton()">
	<img alt="dislike" id="dislike" src="<c:url value="/pics/dislike.png"/>" style="width: 50px; height: auto" onclick="dislikeButton()">
</div>
<!-- comments -->
<c:forEach items="${requestScope.comments}" var="comment">
	<img src="<c:url value="/img/${comment.userId}"/>" width="50px" height="auto"/>

		<div class="comment-box">
			<p class="comment-header"><span>${comment.username}</span></p>
			<div class="comment-box-inner"> 
				<p class="comment-box-inner">${comment.text}</p> <br>
			</div>
			<div class="triangle-comment"></div>
			<p class="comment-date">${comment.date}</p>
			<div class="like-buttons">
				<ul>
					<li>
		 				<p id="likes${comment.commentId}">${comment.likes}</p>
		 			</li>
		 			<li>
		 				<c:if test="${comment.vote == 1}">	
		 					<img alt="liked" id="like${comment.commentId}" src="<c:url value="/pics/liked.png"/>" style="width: 25px; height: auto" onclick="likeComment(${comment.commentId},${user.userId})">
						</c:if>
						<c:if test="${comment.vote < 1}">
							<img alt="like" id="like${comment.commentId}" src="<c:url value="/pics/like.png"/>" style="width: 25px; height: auto" onclick="likeComment(${comment.commentId},${user.userId})">
						</c:if>
					</li>
					<li>	
						<p id="dislikes${comment.commentId}">${comment.dislikes}</p>
		 			</li>
		 			<li>
		 				<c:if test="${comment.vote > -1}">
		 					<img alt="dislike" id="dislike${comment.commentId}" src="<c:url value="/pics/dislike.png"/>" style="width: 25px; height: auto" onclick="dislikeComment(${comment.commentId},${user.userId})">
						</c:if>
						<c:if test="${comment.vote == -1}">
							<img alt="disliked" id="dislike${comment.commentId}" src="<c:url value="/pics/disliked.png"/>" style="width: 25px; height: auto" onclick="dislikeComment(${comment.commentId},${user.userId})">
						</c:if>
					</li>
				</ul>
			</div>
		</div>
	<c:if test="${comment.hasReplies}">
			<c:forEach items="${comment.replies}" var="reply">
				<img src="<c:url value="/img/${comment.userId}"/>" width="50px" height="auto"/>	
				<div class="reply-box">
 					<p class="reply-header"><span>${reply.username}</span></p>
					<div class="reply-box-inner"> 
   						 <p>${reply.text}</p><br>  
	 				</div>
	  				<div class="triangle-comment"></div>
	  				<!-- Neobhodimo e preminavane prez SimpleDateTime-nqma LocalDateTime v JSTL -->
	  				<%-- <fmt:parseDate value = "${reply.date}" var = "parsedDate" pattern = "dd-mm-yyyy" /> --%>
	  				<p class="comment-date"><c:out value="${reply.date}"/></p>
	  			</div>
			  	<div class="like-buttons">
					<ul>
						<li>
			 				<p id="likes${reply.commentId}">${reply.likes}</p>
			 			</li>
			 			<li>
							<c:if test="${reply.vote == 1}"> 
								<img alt="liked" id="like${reply.commentId}" src="<c:url value="/pics/liked.png"/>" style="width: 25px; height: auto" onclick="likeComment(${reply.commentId},${user.userId})">
							</c:if>
							<c:if test="${reply.vote < 1}"> 
								<img alt="like" id="like${reply.commentId}" src="<c:url value="/pics/like.png"/>" style="width: 25px; height: auto" onclick="likeComment(${reply.commentId},${user.userId})">
							</c:if> 
						</li>
						<li>	
							<p id="dislikes${reply.commentId}">${reply.dislikes}</p>
			 			</li>
			 			<li>
			 				<c:if test="${reply.vote == -1}"> 
							  <img alt="disliked" id="dislike${reply.commentId}" src="<c:url value="/pics/disliked.png"/>" style="width: 25px; height: auto" onclick="dislikeComment(${reply.commentId},${user.userId})">
							</c:if>
							<c:if test="${reply.vote > -1}"> 
							 <img alt="dislike" id="dislike${reply.commentId}" src="<c:url value="/pics/dislike.png"/>" style="width: 25px; height: auto" onclick="dislikeComment(${reply.commentId},${user.userId})">
							</c:if> 
						</li>
					</ul>
				</div>
	  				
				<br>
				<form action="<c:url value="/addComment"/>" method="post">
					<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId"/>
					<input type="hidden" value="${comment.commentId}" name="reply"/>
					New reply<input type="text" placeholder="add reply" name="newComment"/>
					<input type="submit" value="reply"/>
				</form>
				
			</c:forEach>
		</c:if>
</c:forEach>
</body>
</html>