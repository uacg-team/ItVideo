<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>	
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>

<script type="text/javascript">
	
		function likeVideo() {
			var request = new XMLHttpRequest();
			request.onreadystatechange = function() {
				//when response is received
				if (this.readyState == 4 && this.status == 200) {
					var button = document.getElementById("likebutton");
					button.innerHTML = "Unlike";
					button.style.background='red';
				}
				else
				if (this.readyState == 4 && this.status == 401) {
					alert("Sorry, you must log in to like this video!");
				}
					
			}
			request.open("post", "commentLove/1", true);
			request.send();
		}
		
		function unlikeVideo() {
			var request = new XMLHttpRequest();
			request.onreadystatechange = function() {
				//when response is received
				if (this.readyState == 4 && this.status == 200) {
					var button = document.getElementById("likebutton");
					button.innerHTML = "Like";
					button.style.background='green';
				}
				else
					if (this.readyState == 4 && this.status == 401) {
						alert("Sorry, you must log in to like this video!");
					}
			}
			request.open("post", "unlike", true);
			request.send();
		}
	</script>
</head>
<body>
	<c:if test="${sessionScope.user!=null}">
	<img src="<c:url value="/img/${sessionScope.user.userId}"/>" width="50px" height="auto"/>
	</c:if>
		<form action="<c:url value="/addComment"/>" method="post">
			<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId"/>
			New Comment<input type="text" placeholder="add comment" name="newComment"/>
			<input type="submit" value="comment"/>
		</form>
	<br>
	<br>
	<br>
	
	<b><c:out value="Comments: ${requestScope.countComments}"></c:out></b>
	<br>
	<br>
	<br>
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
  				<form action="<c:url value="/commentLike"/>" method="post">
					<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId">
					<input type="hidden" value="${comment.commentId}" name="commentId">
					<input type="hidden" value="1" name="like">
					<input type="submit" value="like"/>
				</form>
			</li>
			<li>
				<p>${comment.dislikes} dislikes</p>
			</li>
			<li>
				<form action="<c:url value="/commentLike"/>" method="post">
					<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId">
					<input type="hidden" value="${comment.commentId}" name="commentId">
					<input type="hidden" value="-1" name="like">
					<input type="submit" value="dislike"/>
				</form>
			</li>
		</ul>
		<button style="background-color: green" id="likebutton" onclick="likeVideo()">Like</button>
		</div>
		<c:if test="${sessionScope.user.userId==comment.userId}">
			<form action="comment?deleteCommentId=${comment.commentId}&url=${requestScope.mainVideo.locationUrl}" method="post">
			<input type="submit" value="delete"/>
			</form>
		</c:if>
	</div>
	<br>
	
	<form action="<c:url value="/addComment"/>" method="post">
		<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId"/>
		<input type="hidden" value="${comment.commentId}" name="reply"/>
		New reply<input type="text" placeholder="add reply" name="newComment"/>
		<input type="submit" value="reply"/>
	</form>
	
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
	  				<div class="like-buttons">
		  				<ul>
			  				<li>
			  					<p>${reply.likes} likes</p>
			  				</li>
			  				<li>
								<form action="<c:url value="/commentLike"/>" method="post">
									<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId">
									<input type="hidden" value="${reply.commentId}" name="commentId">
									<input type="hidden" value="1" name="like">
									<input type="submit" value="like"/>
								</form>
							<li>
							<li>
								<p>${reply.dislikes} dislikes</p>
							</li>
							<li>
								<form action="<c:url value="/commentLike"/>" method="post">
									<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId">
									<input type="hidden" value="${reply.commentId}" name="commentId">
									<input type="hidden" value="-1" name="like">
									<input type="submit" value="dislike"/>
								</form>
							</li>
						</ul>
						</div>
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