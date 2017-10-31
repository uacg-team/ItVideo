<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>header</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://bootswatch.com/cerulean/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<style type="text/css">
.avatar-header{
margin: 0px;
padding: 0px;
height: 25px; 
width: 25px;
border-radius: 50%;
}
.center-search-itvideo{
   margin-bottom: 0;
   margin-top: 15px;
}
.nav{
    text-align: center;
}
.navbar-nav li a{
	display: block;
    width: auto;
	margin-bottom: 5px;
    margin-top: 0px;
}
</style>
</head>
<body>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-2">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
  	<a class="navbar-brand" href="<c:url value="/main" />"><img src="<c:url value="/pics/logo.png"/>" style="width: auto; height: 40px"></a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
      <form class="center-search-itvideo" action="<c:url value="/search" />" method="get">
      		<div class="col-lg-6">
				<input class="form-control" type="search" name="search" placeholder="Search...">
			</div>
			 <!--  onchange="location = this.value;"  -->
			 <div class="col-lg-2">
				<select class="form-control" name="searchParam">
					 <option value="users" <c:if test="${sessionScope.searchParam eq \"users\" }"> selected </c:if>>users</option>
					 <option value="videos" <c:if test="${sessionScope.searchParam eq \"videos\" }"> selected </c:if>>videos</option>
					 <option value="playlists" <c:if test="${sessionScope.searchParam eq \"playlists\" }"> selected </c:if>>playlists</option>
				</select> 
			</div>
	  </form>
	      <ul class="nav navbar-nav navbar-right">
	     	 <c:if test="${ sessionScope.user != null}">
	     		 <li>
	     		 		<a href="<c:url value="/uploadVideo"/>">Upload</a>
	     		 </li>
	     		 <li>
	     		 	<a href="<c:url value="/viewProfile/${sessionScope.user.userId}"/>">
						<img class="avatar-header" src="<c:url value="/img/${sessionScope.user.userId}"/>"/>
					</a>
				 </li>
	      		 <li><a href="<c:url value="/logout"/>">Logout</a></li>
			 </c:if>
			 <c:if test="${sessionScope.user == null}"> 
				 <li><a href="<c:url value="/register"/>">Register</a></li>
				 <li><a href="<c:url value="/login"/>"><span class="glyphicon glyphicon-user"></span>Login</a></li>
			 </c:if>
	      </ul>
    </div>
  </div>
</nav>
</body>
</html>