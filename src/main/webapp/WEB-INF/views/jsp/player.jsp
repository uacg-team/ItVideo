<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://vjs.zencdn.net/6.2.8/video.js"></script>
<link href="http://vjs.zencdn.net/6.2.8/video-js.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<c:url value="/css/inline.css" />" />
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/video-js.css"/>"/>
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
		
		function changeFollowButton(){
		    var elem = document.getElementById("follow-button");
		    if (elem.value==="follow") {
		    	elem.value = "unfollow"
		    } else if (elem.value==="unfollow"){
		    	elem.value = "follow"
		    }
		}
		
		function follow(followingId,myId) {
			if (typeof myId === 'undefined') {
			    alert("First login!");
			    return;
			}
			var request = new XMLHttpRequest();

			request.onreadystatechange = function() {
				changeFollowButton();
			};
			var elem = document.getElementById("follow-button");
			var url = "asyncFollow";
			var param1 = "following=";
			var param =param1.concat(followingId,"&action=",elem.value,"&follower=",myId);
			request.open("POST", url, true);
			request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			request.send(param);
		}
		
	</script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>

	<div class="inline">
		<!-- video info -->
		Name: <c:out value="${mainVideo.name }"></c:out><br>
		Description: <c:out value="${mainVideo.description }"></c:out><br>
		Views: <c:out value="${mainVideo.views }"></c:out><br>
		Tags:
		<c:forEach items="${mainVideo.tags}" var="currentTag">	
			<c:out value="#${currentTag.tag} "></c:out>
		</c:forEach>
		<br>
		
		Owner: 
		<a href="<c:url value="/viewProfile/${mainVideo.userId}" />">
			<c:out value="${mainVideo.userName }"></c:out>
			<img src="<c:url value="/img/${mainVideo.userId}"/>" width="50px" height="auto"/>
		</a>
		
		<!-- ajax follow/unfollow -->
		<c:if test="${sessionScope.user.userId != null}">	
			<c:if test="${sessionScope.user.userId != mainVideo.userId}">	
				<c:if test="${follow == \"false\"}">
					<input type="button" value="follow"   id="follow-button" onclick="follow(${sessionScope.user.userId},${mainVideo.userId})"></input>
				</c:if>
				<c:if test="${follow == \"true\"}">
					<input type="button" value="unfollow" id="follow-button" onclick="follow(${sessionScope.user.userId},${mainVideo.userId})"></input>
				</c:if>
			</c:if>
		</c:if>
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

		<!-- GRANDE video player -->
		<video id="my-video" class="video-js" controls preload="auto" width="640px" height="264px"
  				poster="<c:url value="/thumbnail/${mainVideo.videoId}" />" data-setup="{}">
		  		<source src="<c:url value="/videoStream/${requestScope.mainVideo.videoId}"/>" type="video/mp4">
		</video>
		
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
	</div>
		
	<div class="inline">		
		<br>
		<!-- myPlaylists -->
		<c:if test="${not empty sessionScope.user}">
			<jsp:include page="myPlaylists.jsp"></jsp:include>
		</c:if>
		<br>
		<!-- asynchrn comments -->
		<%-- <jsp:include page="asinch_comments.jsp"></jsp:include> --%>
		<jsp:include page="a_comments.jsp"></jsp:include>
		
	</div> 	
	
	<div class="inline">	
		<!-- related videos -->
		<h1>RELATED</h1>
		<jsp:include page="showVideosRequest.jsp"></jsp:include>
	<%-- 	
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
		
	</div> 	
</body>
</html>