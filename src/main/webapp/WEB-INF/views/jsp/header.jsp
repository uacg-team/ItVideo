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





/* Full-width input fields */
input[type=text], input[type=password] {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
}


/* Center the image and position the close button */
.imgcontainer {
    text-align: center;
    margin: 24px 0 12px 0;
    position: relative;
}

img.avatar {
    width: 40%;
    border-radius: 50%;
}

span.psw {
    float: right;
    padding-top: 16px;
}

/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    padding-top: 60px;
}

/* Modal Content/Box */
.modal-content {
    background-color: #fefefe;
    margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
    border: 1px solid #888;
    width: 80%; /* Could be more or less, depending on screen size */
}

/* The Close Button (x) */
.close {
    position: absolute;
    right: 25px;
    top: 0;
    color: #000;
    font-size: 35px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: red;
    cursor: pointer;
}

/* Add Zoom Animation */
.animate {
    -webkit-animation: animatezoom 0.6s;
    animation: animatezoom 0.6s
}

@-webkit-keyframes animatezoom {
    from {-webkit-transform: scale(0)} 
    to {-webkit-transform: scale(1)}
}
    
@keyframes animatezoom {
    from {transform: scale(0)} 
    to {transform: scale(1)}
}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
    span.psw {
       display: block;
       float: none;
    }
    .cancelbtn {
       width: 100%;
    }
}
</style>
<script type="text/javascript">
//Get the modal
var modal = document.getElementById('id01');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
</script>
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
				 <li><button type="button" class="btn btn-info" onclick="document.getElementById('id01').style.display='block'" style="width:auto;"><span class="glyphicon glyphicon-user"></span>Login</button></li>
			 </c:if>
	      </ul>
    </div>
  </div>
</nav>


<div id="id01" class="modal">
  
  <form class="modal-content animate" action="/go to login servlet">
    <div class="imgcontainer">
      <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>
      <img src="<c:url value="/pics/logo.png"/>" alt="Avatar" class="avatar">
    </div>

    <div class="container-fluid">
	  <div class="col-lg-8">
      	<label><b>Username</b></label>
        <input type="text" class="form-control" id="inputEmail" name="username" required placeholder="Enter Username">
      </div><br>
      <div class="col-lg-8">
    	<label><b>Password</b></label>
      	<input type="password" class="form-control" placeholder="Enter Password" name="password" required>
      </div><br>
      <button class="btn btn-info" type="submit">Login</button>
    </div>

    <div class="container" style="background-color:#f1f1f1">
      <button type="button" onclick="document.getElementById('id01').style.display='none'" class="btn btn-danger">Cancel</button>
      <span class="psw">Forgot <a href="#">password?</a></span>
      <!-- add new window with username -->
    </div>
  </form>
</div>



<%-- 

<fieldset>
	<legend>Login</legend>
  		<form action="<c:url value="/login" />" method = "post">
	      <input type="text" placeholder="Enter Username" name="username" required><br>
	      <c:if test="${requestScope.usernameError != null }">
			<div class="err">
				<c:out value="${requestScope.usernameError }"></c:out>
				<br>
				<a href="<c:url value="/register" />"><button type="button" formmethod="get">register</button></a>
			</div>
	      </c:if>
	      <input type="password" placeholder="Enter Password" name="password" required><br>
	      <c:if test="${requestScope.passwordError != null }">
			<div class="err">
				<c:out value="${requestScope.passwordError }"></c:out><br>
			</div>
	      </c:if>
	      <button type="submit">Login</button><br>
	      <input type="checkbox" checked="checked"> Remember me
  		</form>
  		
		<c:if test="${requestScope.passwordError != null }">
			<form action="<c:url value="/forgotPassword" />" method="get">
				<input type="hidden" value="${requestScope.username}" name="username">
				<input type="submit" value="Forgot password?">
			</form>
		</c:if>
  		
</fieldset> --%>



</body>
</html>