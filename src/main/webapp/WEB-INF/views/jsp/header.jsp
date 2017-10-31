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
      </button>
      <a class="navbar-brand" href="#">Brand</a>
    </div>

    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
      <ul class="nav navbar-nav">
        <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
        <li><a href="#">Link</a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">Dropdown <span class="caret"></span></a>
          <ul class="dropdown-menu" role="menu">
            <li><a href="#">Action</a></li>
            <li><a href="#">Another action</a></li>
            <li><a href="#">Something else here</a></li>
            <li class="divider"></li>
            <li><a href="#">Separated link</a></li>
            <li class="divider"></li>
            <li><a href="#">One more separated link</a></li>
          </ul>
        </li>
      </ul>
      <form class="navbar-form navbar-left" role="search">
        <div class="form-group">
          <input type="text" class="form-control" placeholder="Search">
        </div>
        <button type="submit" class="btn btn-default">Submit</button>
      </form>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#">Link</a></li>
      </ul>
    </div>
  </div>
</nav>

<%-- 
<nav class="navbar navbar-inverse">
	<div class="container-fluid">
    	<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"  data-target="#bs-example-navbar-collapse-1">
			  <span class="sr-only">Toggle navigation</span>
			  <span class="icon-bar"></span>
			  <span class="icon-bar"></span>
			  <span class="icon-bar"></span>
			</button>
			  <a class="navbar-brand" href="#">Brand</a>
    	</div>
    	
    	<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
    		<ul class="nav navbar-nav">
  				<li>
			  		<a href="<c:url value="/main" />"><img src="<c:url value="/pics/logo.png"/>" style="width: 40px; height: auto"></a>
				</li>
				<c:if test="${sessionScope.user == null}"> 
					<li><a href="<c:url value="/login"/>">Login</a></li>
					<li><a href="<c:url value="/register"/>">Register</a></li>
				</c:if>
				<li><a href="<c:url value="/uploadVideo"/>">Upload</a></li>
				
				
				<li>
					<form action="<c:url value="/search" />" method="get">
						 <!--  onchange="location = this.value;"  -->
						<select name="searchParam">
							 <option value="users" <c:if test="${sessionScope.searchParam eq \"users\" }"> selected </c:if>>users</option>
							 <option value="videos" <c:if test="${sessionScope.searchParam eq \"videos\" }"> selected </c:if>>videos</option>
							 <option value="playlists" <c:if test="${sessionScope.searchParam eq \"playlists\" }"> selected </c:if>>playlists</option>
						</select>
						<input type="search" name="search" placeholder="Search..">
					</form>
				</li>
					
				
				<c:if test="${ sessionScope.user != null}"> 
				<li>
					<a href="<c:url value="/viewProfile/${sessionScope.user.userId}"/>">
						<img src="<c:url value="/img/${sessionScope.user.userId}"/>" width="50px" height="auto"/>
						<c:out value="${sessionScope.user.username}"></c:out>
					</a>
				</li>
			
				<li><a href="<c:url value="/logout"/>">Logout</a></li>
			
				</c:if>
    		</ul>
    	</div>
	</div>
</nav>


<ul>
	<li>
  		<a href="<c:url value="/main" />"><img src="<c:url value="/pics/logo.png"/>" style="width: 200px; height: auto"></a>
	</li>
	<c:if test="${sessionScope.user == null}"> 
		<li><a href="<c:url value="/login"/>">Login</a></li>
		<li><a href="<c:url value="/register"/>">Register</a></li>
	</c:if>
	<li><a href="<c:url value="/uploadVideo"/>">Upload</a></li>
	
	
	<li>
		<form action="<c:url value="/search" />" method="get">
			 <!--  onchange="location = this.value;"  -->
			<select name="searchParam">
				 <option value="users" <c:if test="${sessionScope.searchParam eq \"users\" }"> selected </c:if>>users</option>
				 <option value="videos" <c:if test="${sessionScope.searchParam eq \"videos\" }"> selected </c:if>>videos</option>
				 <option value="playlists" <c:if test="${sessionScope.searchParam eq \"playlists\" }"> selected </c:if>>playlists</option>
			</select>
			<input type="search" name="search" placeholder="Search..">
		</form>
	</li>
		
	
	<c:if test="${ sessionScope.user != null}"> 
	<li>
		<a href="<c:url value="/viewProfile/${sessionScope.user.userId}"/>">
			<img src="<c:url value="/img/${sessionScope.user.userId}"/>" width="50px" height="auto"/>
			<c:out value="${sessionScope.user.username}"></c:out>
		</a>
	</li>

	<li><a href="<c:url value="/logout"/>">Logout</a></li>

	</c:if>
</ul>
<br>
 --%>


</body>
</html>