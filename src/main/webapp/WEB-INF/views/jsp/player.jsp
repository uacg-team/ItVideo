<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="http://vjs.zencdn.net/6.2.8/video.js"></script>
<link href="http://vjs.zencdn.net/6.2.8/video-js.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/Video-js-Sublime-Skin.css"/>"/>
<%-- <link type="text/css" rel="stylesheet" href="<c:url value="/css/inline.css" />" /> --%>
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
<style type="text/css">
video {
    max-width: 100%;
    height: auto;
    align-content: center;
}
.tilt {
  -webkit-transition: all 0.1s ease;
     -moz-transition: all 0.1s ease;
       -o-transition: all 0.1s ease;
      -ms-transition: all 0.1s ease;
          transition: all 0.1s ease;
}
 
.tilt:hover {
  -webkit-transform: rotate(-10deg);
     -moz-transform: rotate(-10deg);
       -o-transform: rotate(-10deg);
      -ms-transform: rotate(-10deg);
          transform: rotate(-10deg);
}

</style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include><br>
   <div class="container-fluid">
    <div class="col-sm-9">
		<div class="row">
			<!-- GRANDE video player -->
	        <video id="my_video_1" class="video-js vjs-sublime-skin" controls preload="auto" width="640px" height="264px" 
	        poster="<c:url value="/thumbnail/${mainVideo.videoId} " />" data-setup=' {"aspectRatio":"640:267", "playbackRates": [1, 1.5, 2] }'>
	            <source src="<c:url value="/videoStream/${requestScope.mainVideo.videoId}" />" type="video/mp4">
	        </video>
	        <script type="text/javascript">
	            $(function () {
	                var $refreshButton = $('#refresh');
	                var $results = $('#css_result');
	
	                function refresh() {
	                    var css = $('style.cp-pen-styles').text();
	                    $results.html(css);
	                }
	
	                refresh();
	                $refreshButton.click(refresh);
	
	                // Select all the contents when clicked
	                $results.click(function () {
	                    $(this).select();
	                });
	            });
	        </script>
		</div>
	    <div class="row">
	    <br>
			<!-- myPlaylists -->
             <c:if test="${not empty sessionScope.user}">
                 <span style="float:left;">
                     <jsp:include page="myPlaylists.jsp"></jsp:include>
                 </span>
             </c:if>
             <c:if test="${ not empty sessionScope.user  }">
                 <c:if test="${sessionScope.user.userId == requestScope.mainVideo.userId}">
                     <span style="float:left;">
                         <!-- Edit video -->
                         <form action="<c:url value="/editVideo/${mainVideo.videoId}"/>" method="get">
                             <input class="btn btn-warning" type="submit" value="edit">
                         </form>
                     </span>
                     <span style="float:left;">
                         <!-- Delete video -->
                         <form action="<c:url value="/deleteVideo "/>" method="post">
                             <input type="hidden" value="${requestScope.mainVideo.videoId}" name="videoId">
                             <input class="btn btn-danger" type="submit" value="delete">
                         </form>
                     </span>
                 </c:if>
             </c:if>
             <br>
	    </div>
	    <hr>
   		<div class="row">
            <!-- start video info -->
            <div class="row">
                <div class="col-xs-9 container">
                    <div class="media">
                        <div class="media-left media-top">
                            <a href="<c:url value="/viewProfile/${mainVideo.userId} " />">
                                <img src="<c:url value="/img/${mainVideo.userId} "/>" class="media-object" width="50px" height="auto" />
                            </a>
                        </div>
                        <div class="media-body" style="text-align: left;">
                            <h4 class="media-heading">
                                <a href="<c:url value="/viewProfile/${mainVideo.userId} " />">
                                    <strong>
                                        <c:out value="${mainVideo.userName }"></c:out>
                                    </strong>
                                </a>
                                <!-- ajax follow/unfollow start -->
                                <c:if test="${sessionScope.user.userId != null}">
                                    <c:if test="${sessionScope.user.userId != mainVideo.userId}">
                                        <c:if test="${follow == \"false\"}">
                                            <input class="btn btn-primary btn-sm" type="button" value="follow" id="follow-button" onclick="follow(${sessionScope.user.userId},${mainVideo.userId})"></input>
                                        </c:if>
                                        <c:if test="${follow == \"true\"}">
                                            <input class="btn btn-primary btn-sm" type="button" value="unfollow" id="follow-button" onclick="follow(${sessionScope.user.userId},${mainVideo.userId})"></input>
                                        </c:if>
                                    </c:if>
                                </c:if>
                                <!-- ajax follow/unfollow end -->
                            </h4>
                            <p class="text-primary" style="font-size: 150%;">
                                <c:out value="${mainVideo.name}"></c:out>
                                <p>
                                    <p class="text-primary">Description:
                                        <c:out value="${mainVideo.description }"></c:out>
                                    </p>
                                    <c:forEach items="${mainVideo.tags}" var="tag">
                                        <a href="<c:url value="/search/tag/${tag.tag} "/>" class="btn btn-primary btn-xs">
                                            <c:out value="#${tag.tag} "></c:out>
                                        </a>
                                    </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="col-xs-3 container">
                    <div class="row content">
                        <!-- ajax like dislike -->
                        <div class="like-buttons">
                            <ul>
                                <li>
                                    <h3>
                                        <span class="label label-primary" id="likes${mainVideo.videoId}">${mainVideo.likes}</span>
                                    </h3>
                                </li>
                                <li>
                                    <div class="tilt">
                                        <h6>
                                            <c:if test="${mainVideo.vote == 1}">
                                                <img alt="liked" id="like${mainVideo.videoId}" src="<c:url value="/pics/liked.png "/>" style="width: 50px; height: auto"
                                                    onclick="likeVideo(${mainVideo.videoId},${user.userId})">
                                            </c:if>
                                            <c:if test="${mainVideo.vote < 1}">
                                                <img alt="like" id="like${mainVideo.videoId}" src="<c:url value="/pics/like.png "/>" style="width: 50px; height: auto" onclick="likeVideo(${mainVideo.videoId},${user.userId})">
                                            </c:if>
                                        </h6>
                                    </div>
                                </li>
                                <li>
                                    <h3>
                                        <span class="label label-primary" id="dislikes${mainVideo.videoId}">${mainVideo.dislikes}</span>
                                    </h3>
                                </li>
                                <li>
                                    <div class="tilt">
                                        <h6>
                                            <c:if test="${mainVideo.vote > -1}">
                                                <img alt="dislike" id="dislike${mainVideo.videoId}" src="<c:url value="/pics/dislike.png "/>" style="width: 50px; height: auto"
                                                    onclick="dislikeVideo(${mainVideo.videoId},${user.userId})">
                                            </c:if>
                                            <c:if test="${mainVideo.vote == -1}">
                                                <img alt="disliked" id="dislike${mainVideo.videoId}" src="<c:url value="/pics/disliked.png "/>" style="width: 50px; height: auto"
                                                    onclick="dislikeVideo(${mainVideo.videoId},${user.userId})">
                                            </c:if>
                                        </h6>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <!-- ajax like dislike end -->
                    </div>
                    <div class="row pagination-centered">
                        <h4 style="text-align: center" class="text-primary text-right">
                            <c:out value="${mainVideo.views }"></c:out> views</h4>
                    </div>
                </div>
            </div>
            <!-- end video info  -->			
	    </div>
 	    <hr>
   		<div class="row">
			<!-- asynchrn comments -->		
			<div>
				<%-- <jsp:include page="asinch_comments.jsp"></jsp:include> --%>
				<jsp:include page="a_comments.jsp"></jsp:include>
			</div> 	
   		</div>
    </div>
    <div class="col-sm-3">
   		<jsp:include page="related.jsp"></jsp:include>
    </div>
</div>
    
    
      
<%-- 	
<!-- related videos -->
<h1>RELATED</h1>
<jsp:include page="showVideosRequest.jsp"></jsp:include>

<c:forEach items="${requestScope.related}" var="relVideo">	
	Name: <c:out value="${relVideo.name }"></c:out><br>
	<a href="<c:url value= "/player/${relVideo.videoId}" />">	
		<video width="320" height="240">
	  		<source src="/video/${relVideo.userId}" type="video/mp4">
		</video>
	</a><br>	
	Tags: <c:forEach items="${relVideo.tags}" var="tag">	
		<c:out value="#${tag.tag } "></c:out>
	</c:forEach>
</c:forEach>
--%>
	
</body>
</html>