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
<title>Player</title>
<style type="text/css">
/* http://www.scriptsmashup.com/Video_Skin_Generator/Videojs/videojs-skin-generator.html  */
.video-js .vjs-menu-button-inline.vjs-slider-active,.video-js .vjs-menu-button-inline:focus,.video-js .vjs-menu-button-inline:hover,.video-js.vjs-no-flex .vjs-menu-button-inline {
    width: 10em
}

.video-js .vjs-controls-disabled .vjs-big-play-button {
    display: none!important
}

.video-js .vjs-control {
    width: 3em
}

.video-js .vjs-menu-button-inline:before {
    width: 1.5em
}

.vjs-menu-button-inline .vjs-menu {
    left: 3em
}

.vjs-paused.vjs-has-started.video-js .vjs-big-play-button,.video-js.vjs-ended .vjs-big-play-button,.video-js.vjs-paused .vjs-big-play-button {
    display: block
}

.video-js .vjs-load-progress div,.vjs-seeking .vjs-big-play-button,.vjs-waiting .vjs-big-play-button {
    display: none!important
}

.video-js .vjs-mouse-display:after,.video-js .vjs-play-progress:after {
    padding: 0 .4em .3em
}

.video-js.vjs-ended .vjs-loading-spinner {
    display: none;
}

.video-js.vjs-ended .vjs-big-play-button {
    display: block !important;
}

.video-js *,.video-js:after,.video-js:before {
    box-sizing: inherit;
    font-size: inherit;
    color: inherit;
    line-height: inherit
}

.video-js.vjs-fullscreen,.video-js.vjs-fullscreen .vjs-tech {
    width: 100%!important;
    height: 100%!important
}

.video-js {
    font-size: 14px;
    overflow: hidden
}

.video-js .vjs-control {
    color: inherit
}

.video-js .vjs-menu-button-inline:hover,.video-js.vjs-no-flex .vjs-menu-button-inline {
    width: 8.35em
}

.video-js .vjs-volume-menu-button.vjs-volume-menu-button-horizontal:hover .vjs-menu .vjs-menu-content {
    height: 3em;
    width: 6.35em
}

.video-js .vjs-control:focus:before,.video-js .vjs-control:hover:before {
    text-shadow: 0 0 1em #fff,0 0 1em #fff,0 0 1em #fff
}

.video-js .vjs-spacer,.video-js .vjs-time-control {
    display: -webkit-box;
    display: -moz-box;
    display: -ms-flexbox;
    display: -webkit-flex;
    display: flex;
    -webkit-box-flex: 1 1 auto;
    -moz-box-flex: 1 1 auto;
    -webkit-flex: 1 1 auto;
    -ms-flex: 1 1 auto;
    flex: 1 1 auto
}

.video-js .vjs-time-control {
    -webkit-box-flex: 0 1 auto;
    -moz-box-flex: 0 1 auto;
    -webkit-flex: 0 1 auto;
    -ms-flex: 0 1 auto;
    flex: 0 1 auto;
    width: auto
}

.video-js .vjs-time-control.vjs-time-divider {
    width: 14px
}

.video-js .vjs-time-control.vjs-time-divider div {
    width: 100%;
    text-align: center
}

.video-js .vjs-time-control.vjs-current-time {
    margin-left: 1em
}

.video-js .vjs-time-control .vjs-current-time-display,.video-js .vjs-time-control .vjs-duration-display {
    width: 100%
}

.video-js .vjs-time-control .vjs-current-time-display {
    text-align: right
}

.video-js .vjs-time-control .vjs-duration-display {
    text-align: left
}

.video-js .vjs-play-progress:before,.video-js .vjs-progress-control .vjs-play-progress:before,.video-js .vjs-remaining-time,.video-js .vjs-volume-level:after,.video-js .vjs-volume-level:before,.video-js.vjs-live .vjs-time-control.vjs-current-time,.video-js.vjs-live .vjs-time-control.vjs-duration,.video-js.vjs-live .vjs-time-control.vjs-time-divider,.video-js.vjs-no-flex .vjs-time-control.vjs-remaining-time {
    display: none
}

.video-js.vjs-no-flex .vjs-time-control {
    display: table-cell;
    width: 4em
}

.video-js .vjs-progress-control {
    position: absolute;
    left: 0;
    right: 0;
    width: 100%;
    height: .5em;
    top: -.5em
}

.video-js .vjs-progress-control .vjs-load-progress,.video-js .vjs-progress-control .vjs-play-progress,.video-js .vjs-progress-control .vjs-progress-holder {
    height: 100%
}

.video-js .vjs-progress-control .vjs-progress-holder {
    margin: 0
}

.video-js .vjs-progress-control:hover {
    height: 1.5em;
    top: -1.5em
}

.video-js .vjs-control-bar {
    -webkit-transition: -webkit-transform .1s ease 0s;
    -moz-transition: -moz-transform .1s ease 0s;
    -ms-transition: -ms-transform .1s ease 0s;
    -o-transition: -o-transform .1s ease 0s;
    transition: transform .1s ease 0s
}

.video-js.not-hover.vjs-has-started.vjs-paused.vjs-user-active .vjs-control-bar,.video-js.not-hover.vjs-has-started.vjs-paused.vjs-user-inactive .vjs-control-bar,.video-js.not-hover.vjs-has-started.vjs-playing.vjs-user-active .vjs-control-bar,.video-js.not-hover.vjs-has-started.vjs-playing.vjs-user-inactive .vjs-control-bar,.video-js.vjs-has-started.vjs-playing.vjs-user-inactive .vjs-control-bar {
    visibility: visible;
    opacity: 1;
    -webkit-backface-visibility: hidden;
    -webkit-transform: translateY(3em);
    -moz-transform: translateY(3em);
    -ms-transform: translateY(3em);
    -o-transform: translateY(3em);
    transform: translateY(3em);
    -webkit-transition: -webkit-transform 1s ease 0s;
    -moz-transition: -moz-transform 1s ease 0s;
    -ms-transition: -ms-transform 1s ease 0s;
    -o-transition: -o-transform 1s ease 0s;
    transition: transform 1s ease 0s
}

.video-js.not-hover.vjs-has-started.vjs-paused.vjs-user-active .vjs-progress-control,.video-js.not-hover.vjs-has-started.vjs-paused.vjs-user-inactive .vjs-progress-control,.video-js.not-hover.vjs-has-started.vjs-playing.vjs-user-active .vjs-progress-control,.video-js.not-hover.vjs-has-started.vjs-playing.vjs-user-inactive .vjs-progress-control,.video-js.vjs-has-started.vjs-playing.vjs-user-inactive .vjs-progress-control {
    height: .25em;
    top: -.25em;
    pointer-events: none;
    -webkit-transition: height 1s,top 1s;
    -moz-transition: height 1s,top 1s;
    -ms-transition: height 1s,top 1s;
    -o-transition: height 1s,top 1s;
    transition: height 1s,top 1s
}

.video-js.not-hover.vjs-has-started.vjs-paused.vjs-user-active.vjs-fullscreen .vjs-progress-control,.video-js.not-hover.vjs-has-started.vjs-paused.vjs-user-inactive.vjs-fullscreen .vjs-progress-control,.video-js.not-hover.vjs-has-started.vjs-playing.vjs-user-active.vjs-fullscreen .vjs-progress-control,.video-js.not-hover.vjs-has-started.vjs-playing.vjs-user-inactive.vjs-fullscreen .vjs-progress-control,.video-js.vjs-has-started.vjs-playing.vjs-user-inactive.vjs-fullscreen .vjs-progress-control {
    opacity: 0;
    -webkit-transition: opacity 1s ease 1s;
    -moz-transition: opacity 1s ease 1s;
    -ms-transition: opacity 1s ease 1s;
    -o-transition: opacity 1s ease 1s;
    transition: opacity 1s ease 1s
}

.video-js.vjs-live .vjs-live-control {
    margin-left: 1em
}

.video-js .vjs-big-play-button {
    top: 50%;
    left: 50%;
    margin-left: -1em;
    margin-top: -1em;
    width: 2em;
    height: 2em;
    line-height: 2em;
    border: none;
    border-radius: 50%;
    font-size: 3.5em;
    background-color: rgba(0,0,0,.45);
    color: #fff;
    -webkit-transition: border-color .4s,outline .4s,background-color .4s;
    -moz-transition: border-color .4s,outline .4s,background-color .4s;
    -ms-transition: border-color .4s,outline .4s,background-color .4s;
    -o-transition: border-color .4s,outline .4s,background-color .4s;
    transition: border-color .4s,outline .4s,background-color .4s
}

.video-js .vjs-menu-button-popup .vjs-menu {
    left: -3em
}

.video-js .vjs-menu-button-popup .vjs-menu .vjs-menu-content {
    background-color: transparent;
    width: 12em;
    left: -1.5em;
    padding-bottom: .5em
}

.video-js .vjs-menu-button-popup .vjs-menu .vjs-menu-item,.video-js .vjs-menu-button-popup .vjs-menu .vjs-menu-title {
    background-color: #151b17;
    margin: .3em 0;
    padding: .5em;
    border-radius: .3em
}

.video-js .vjs-menu-button-popup .vjs-menu .vjs-menu-item.vjs-selected {
    background-color: #2483d5
}

.video-js .vjs-big-play-button {
    background-color: rgba(255,255,255,0.1);
    font-size: 6em;
    border-radius: 50%;
    height: 2em !important;
    line-height: 2em !important;
    margin-top: -1em !important
}

.video-js:hover .vjs-big-play-button,.video-js .vjs-big-play-button:focus,.video-js .vjs-big-play-button:active {
    background-color: rgba(255,255,255,0.23)
}

.video-js .vjs-loading-spinner {
    border-color: rgba(255,255,255,0.7)
}

.video-js .vjs-control-bar2 {
    background-color: #fcfcfc
}

.video-js .vjs-control-bar {
    background-color: rgba(252,252,252,0.19) !important;
    color: #ffffff;
    font-size: 16px
}

.video-js .vjs-play-progress,.video-js  .vjs-volume-level {
    background-color: #cccccc
}
</style>
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
		

		<!-- GRANDE video player -->
		<video id="my-video" class="video-js" controls preload="auto" width="640px" height="264px"
  				poster="<c:url value="/thumbnail/${mainVideo.videoId}" />" data-setup="{}">
		  		<source src="<c:url value="/videoStream/${requestScope.mainVideo.videoId}"/>" type="video/mp4">
		</video>
			
		<br>
		
		
		<%-- 	
		<!-- normal video player -->
		<video poster="<c:url value="/thumbnail/${mainVideo.videoId}" />" width="800" height="600" controls="controls" preload="auto" >
		  		<source src="<c:url value="/videoStream/${requestScope.mainVideo.videoId}"/>" type="video/mp4">
		</video>
		 --%>
		
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

</body>
</html>