<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
 <meta name="viewport" content="width=device-width, initial-scale=1">
 <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 <script src="http://vjs.zencdn.net/6.2.8/video.js"></script>
<link href="http://vjs.zencdn.net/6.2.8/video-js.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<c:url value="/css/inline.css" />" />
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/video-js.css"/>"/>
  <style>
    /* Remove the navbar's default margin-bottom and rounded borders */ 
    .navbar {
      margin-bottom: 0;
      border-radius: 0;
    }
    
    /* Set height of the grid so .sidenav can be 100% (adjust as needed) */
    .row.content {height: 450px}
    
    /* Set gray background color and 100% height */
    .sidenav {
      padding-top: 20px;
      background-color: #f1f1f1;
      height: 100%;
    }
    
    /* Set black background color, white text and some padding */
    footer {
      background-color: #555;
      color: white;
      padding: 15px;
    }
    
    /* On small screens, set height to 'auto' for sidenav and grid */
    @media screen and (max-width: 767px) {
      .sidenav {
        height: auto;
        padding: 15px;
      }
      .row.content {height:auto;} 
    }
  </style>
</head>
<body>	
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <a class="navbar-brand" href="#">Logo</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
        <li class="active"><a href="#">Home</a></li>
        <li><a href="#">About</a></li>
        <li><a href="#">Projects</a></li>
        <li><a href="#">Contact</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
	      <c:if test="${ sessionScope.user != null}"> 
			<li>
				<a href="<c:url value="/viewProfile/${sessionScope.user.userId}"/>">
					<img src="<c:url value="/img/${sessionScope.user.userId}"/>" width="50px" height="auto"/>
					<c:out value="${sessionScope.user.username}"></c:out>
				</a>
			</li>
			<li><a href="<c:url value="/uploadVideo"/>">Upload</a></li>
			<li><a href="<c:url value="/logout"/>">Logout</a></li>
		</c:if>
       	<li><a href="<c:url value="/login"/>">Login</a></li>
       	<li><a href="<c:url value="/register"/>">Register</a></li>
      </ul>
    </div>
  </div>
</nav>
  
<div class="container-fluid text-center">    
  <div class="row content">
    <div class="col-sm-1 sidenav">
      <p><a href="#">Link</a></p>
      <p><a href="#">Link</a></p>
      <p><a href="#">Link</a></p>
    </div>
    <div class="col-sm-8 text-left"> 
      
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
		  		<source src="<c:url value="/video/${requestScope.mainVideo.videoId}"/>" type="video/mp4">
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
    </div>
    <div class="col-sm-3 sidenav">
      <div class="well">
        <p>ADS</p>
      </div>
      <div class="well">
        <p>ADS</p>
      </div>
    </div>
  </div>
</div>

<footer class="container-fluid text-center">
  <p>Footer Text</p>
</footer>
</body>
</html>