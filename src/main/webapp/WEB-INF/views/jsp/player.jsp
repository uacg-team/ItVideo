<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="<c:url value="/css/inline.css" />" />
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>
<title>Player</title>
<script type="text/javascript">
		function likeVideo(videoId,userId) {
			if (typeof userId === 'undefined') {
			    alert("First login!");
			    return;
			}
			var request = new XMLHttpRequest();
			request.onreadystatechange =  function() {
				var like = document.getElementById("like".concat(videoId));
			    var dislike = document.getElementById("dislike".concat(videoId));
			   
			    var likes = document.getElementById("likes".concat(videoId));
			    var countLikes = likes.innerHTML;
			    var dislikes = document.getElementById("dislikes".concat(videoId));
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
			var url = "videoAsyncLike";
			var param1 = "videoId=";
			var param =param1.concat(videoId,"&like=1","&userId=",userId);
			request.open("POST", url, true);
			request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			request.send(param);
		}
		
		function dislikeVideo(videoId,userId) {
			if (typeof userId === 'undefined') {
			    alert("First login!");
			    return;
			}
			var request = new XMLHttpRequest();
			request.onreadystatechange =  function() {
				var like = document.getElementById("like".concat(videoId));
			    var dislike = document.getElementById("dislike".concat(videoId));
			    
			    var likes = document.getElementById("likes".concat(videoId));
			    var countLikes = likes.innerHTML;
			    var dislikes = document.getElementById("dislikes".concat(videoId));
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
			var url = "videoAsyncLike";
			var param1 = "videoId=";
			var param =param1.concat(videoId,"&like=-1","&userId=",userId);
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
	<jsp:include page="header.jsp"></jsp:include><br>

	<div class="inline">
		<!-- video info -->
		<h3>Name: <c:out value="${mainVideo.name }"></c:out></h3>
		<h3>Description: <c:out value="${mainVideo.description }"></c:out></h3>
		<h3>Views: <c:out value="${mainVideo.views }"></c:out></h3>
		<h3>Tags:
		<c:forEach items="${mainVideo.tags}" var="currentTag">	
			<c:out value="#${currentTag.tag} "></c:out>
		</c:forEach>
		</h3>
		
		<h3>Owner: 
			<a href="<c:url value="/viewProfile/${mainVideo.userId}" />">
				<c:out value="${mainVideo.userName }"></c:out>
				<img src="<c:url value="/img/${mainVideo.userId}"/>" width="50px" height="auto"/>
			</a>
		</h3>
		
		<br>
		
		<!-- Edit video -->
		<c:if test="${sessionScope.user.userId == mainVideo.userId }">
			<form action="<c:url value="/editVideo/${mainVideo.videoId}"/>" method="get">
				<input type="submit" value="edit video">
			</form>
		</c:if>
		
		<br>
		
		
		<!-- Delete video -->
		<c:if test="${ not empty sessionScope.user  }">
			<c:if test="${sessionScope.user.userId == requestScope.mainVideo.userId}">
				<form action="<c:url value="/deleteVideo"/>" method="post">
					<input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId">
					<input type="submit" value="Delete">
				</form>
			</c:if>
		</c:if>
		

		<!-- normal video player -->
		<video poster="<c:url value="/thumbnail/${mainVideo.videoId}" />" width="800" height="600" controls="controls" preload="auto" >
		  		<source src="<c:url value="/videoStream/${requestScope.mainVideo.videoId}"/>" type="video/mp4">
		</video>
		<br>
		<%-- 
		<!-- Like video button -->
		<form action="<c:url value="/videoLike" />" method="post">
			<input type="hidden" name="like" value="1">
			<input type="hidden" name="videoId" value="${requestScope.mainVideo.videoId}">
			<input type="hidden" name="userId" value="${sessionScope.user.userId}">
			<input type="hidden" name="url" value="${requestScope.mainVideo.locationUrl}">
			<c:out value="${requestScope.likes}"></c:out>
			<input type="submit" value="Like">
		</form>
		
		<!-- dislike video button -->
		<form action="<c:url value="/videoLike" />" method="post">
			<input type="hidden" name="like" value="-1">
			<input type="hidden" name="videoId" value="${requestScope.mainVideo.videoId}">
			<input type="hidden" name="userId" value="${sessionScope.user.userId}">
			<input type="hidden" name="url" value="${requestScope.mainVideo.locationUrl}">
			<c:out value="${requestScope.disLikes}"></c:out>
			<input type="submit" value="Dislike">
		</form>
		 --%>
		 
		<!-- ajax like dislike -->
		<div class="like-buttons">
			<ul>
				<li>
	 				<p id="likes${mainVideo.videoId}">${mainVideo.likes}</p>
	 			</li>
	 			<li>
	 				<c:if test="${mainVideo.vote == 1}">	
	 					<img alt="liked" id="like${mainVideo.videoId}" src="<c:url value="/pics/liked.png"/>" style="width: 25px; height: auto" onclick="likeVideo(${mainVideo.videoId},${user.userId})">
					</c:if>
					<c:if test="${mainVideo.vote < 1}">
						<img alt="like" id="like${mainVideo.videoId}" src="<c:url value="/pics/like.png"/>" style="width: 25px; height: auto" onclick="likeVideo(${mainVideo.videoId},${user.userId})">
					</c:if>
				</li>
				<li>	
					<p id="dislikes${mainVideo.videoId}">${mainVideo.dislikes}</p>
	 			</li>
	 			<li>
	 				<c:if test="${mainVideo.vote > -1}">
	 					<img alt="dislike" id="dislike${mainVideo.videoId}" src="<c:url value="/pics/dislike.png"/>" style="width: 25px; height: auto" onclick="dislikeVideo(${mainVideo.videoId},${user.userId})">
					</c:if>
					<c:if test="${mainVideo.vote == -1}">
						<img alt="disliked" id="dislike${mainVideo.videoId}" src="<c:url value="/pics/disliked.png"/>" style="width: 25px; height: auto" onclick="dislikeVideo(${mainVideo.videoId},${user.userId})">
					</c:if>
				</li>
			</ul>
		</div>
		<br>
		
		</div>
<%-- 			
		<br>
		<!-- myPlaylists -->
		<c:if test="${not empty sessionScope.user}">
			<jsp:include page="myPlaylists.jsp"></jsp:include>
		</c:if>
		<br>
		<!-- asynchrn comments -->
		<jsp:include page="asinch_comments.jsp"></jsp:include>
		
	
	

	<!-- related videos -->
	<h1>RELATED</h1>
	<c:forEach items="${requestScope.related}" var="relVideo">	
	<div class="inline">
		Name: <c:out value="${relVideo.name }"></c:out><br>
		<a href="<c:url value= "/player/${relVideo.videoId}" />">	
			<video width="320" height="240">
		  		<source src="/video/${relVideo.userId}" type="video/mp4">
			</video>
		</a><br>	
		Tags: <c:forEach items="${relVideo.tags}" var="tag">	
			<c:out value="#${tag.tag } "></c:out>
		</c:forEach>
	</div>
	</c:forEach>
--%>

</body>
</html>