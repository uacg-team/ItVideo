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
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootswatch/3.3.7/cerulean/bootstrap.min.css">
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script> -->
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
   margin-bottom: 15px;
   margin-top: 15px;
}
.nav{
    text-align: center;
}
.navbar-nav li button{
	display: block;
    width: auto;
	margin-bottom: 15px;
    margin-top: 0px;
    margin-left: 5px;
    margin-right: 5px;
}


/* login form additions */
.imgcontainer {
    text-align: center;
    margin: 24px 0 12px 0;
    position: relative;
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


/* Set a style for all buttons */
.btn-login-itvideo {
    width: 100%;
    margin: 10px 0;
}
.field-addition{
	display: inline-block;
	margin: 8px 0;
	width: 100%;
	box-sizing: border-box;
}

/* Extra styles for the cancel button */
.cancelbtn {
    width: auto;
    padding: 10px 18px;
    background-color: #f44336;
}

img.avatar {
    width: 25%;
    border-radius: 50%;
}

.container-login {
    padding: 16px;
}

span.psw {
    float: right;
    padding-top: 11px;
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
    margin: 1% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
    border: 1px solid #888;
    width: 50%;/*   Could be more or less, depending on screen size */
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
    -webkit-animation: animatezoom 0.9s;
    animation: animatezoom 0.9s
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
function loginPost(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			/* false - Default. Reloads the current page from the cache. */
			/* true - Reloads the current page from the server */
			window.location.reload(false); 
		}else{
			var resp = JSON.parse(this.responseText);
			var typeError= resp.typeError;
			var msg=resp.msg;
			var action = resp.action;
			if (this.readyState == 4 && this.status == 401) {
				if(typeError === "not-activated" || typeError === "usernameError"){
					var insertion=document.getElementById("user-err-login");
				}else if(typeError === "passwordError"){
					var insertion=document.getElementById("pass-err-login");
				}
				//delete other problems if have
				document.getElementById("user-err-login").innerHTML="";
				document.getElementById("pass-err-login").innerHTML="";
				insertion.innerHTML="<p class=\"text-danger\">"+msg+"</p>";
			}
			else if (this.readyState == 4 && this.status == 500) {
				var insertion=document.getElementById("other-err-login");
				insertion.innerHTML="<p class=\"text-danger\">"+msg+"</p>";
			}
		}
	}
	var username = document.getElementById("username-login").value;
	var password = document.getElementById("password-login").value;
	var url = "/ItVideo/main/login";
	var param = "username=" + username + "&password=" + password;
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
}

function registerPost(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var insertion = document.getElementById("success-register");
			document.getElementById("user-err-register").innerHTML="";
			document.getElementById("pass-err-register").innerHTML="";
			document.getElementById("other-err-register").innerHTML="";
			insertion.innerHTML = "<p class=\"text-success\">Thank You! Please check your email to activate your account</p>";
			//window.location.href = "/ItVideo/main";
			//Thank You! Please check your email to activate your account
		}else{
			var resp = JSON.parse(this.responseText);
			var typeError= resp.typeError;
			var msg=resp.msg;
			var action = resp.action;
			if (this.readyState == 4 && this.status == 400) {
				if(typeError === "usernameError"){
					var insertion=document.getElementById("user-err-register");
				}else if(typeError === "passwordError"){
					var insertion=document.getElementById("pass-err-register");
				}else{
					var insertion=document.getElementById("other-err-register");
				}
				//delete other problems if have
				document.getElementById("user-err-register").innerHTML="";
				document.getElementById("pass-err-register").innerHTML="";
				document.getElementById("other-err-register").innerHTML="";
				insertion.innerHTML  = "<p class=\"text-danger\">"+msg+"</p>";
			}
			else if (this.readyState == 4 && this.status == 500) {
				var insertion = document.getElementById("other-err-register");
				document.getElementById("user-err-register").innerHTML="";
				document.getElementById("pass-err-register").innerHTML="";
				document.getElementById("other-err-register").innerHTML="";
				insertion.innerHTML = "<p class=\"text-danger\">"+msg+"</p>";
			}
		}
	}
	var username = document.getElementById("username-register").value;
	var password = document.getElementById("password-register").value;
	var confirmPassword = document.getElementById("confirmPassword-register").value;
	var email = document.getElementById("email-register").value;
	var url = "/ItVideo/main/register";
	var param = "username=" + username + "&password=" + password+"&confirmPassword=" + confirmPassword+"&email=" + email;
	request.open("POST", url, true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	request.send(param);
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
	      		<%--  <li><a href="<c:url value="/logout"/>">Logout</a></li> --%>
				<li><a href="<c:url value="/logout"/>" type="button" class="btn btn-info" style="width:auto;"><span class="glyphicon glyphicon-log-out"></span>Logout</a></li>
			 </c:if>
			 <c:if test="${sessionScope.user == null}"> 
				 <li><button type="button" class="btn btn-info" onclick="document.getElementById('register-form-itvideo').style.display='block'" style="width:auto;"><span class="glyphicon glyphicon-user"></span> Register</button></li>
				 <li><button type="button" class="btn btn-info" onclick="document.getElementById('login-form-itvideo').style.display='block'" style="width:auto;"><span class="glyphicon glyphicon-log-in"></span> Login</button></li>
			 </c:if>
	      </ul>
    </div>
  </div>
</nav>


<!-- login form -->
<div id="login-form-itvideo" class="modal">
						<%-- action="<c:url value="/login"  method="post"/>" --%>
  <div class="modal-content animate" onsubmit="loginPost()">
    <div class="imgcontainer">
      <span onclick="document.getElementById('login-form-itvideo').style.display='none'" class="close" title="Close Modal">&times;</span>
      <img src="<c:url value="/pics/avatar.png"/>" alt="Avatar" class="avatar">
    </div>

    <div class="container-login">
      <label><b>Username</b></label>
      <input type="text" placeholder="Enter Username" name="username" class="form-control field-addition" required id="username-login">
	  	<div id="user-err-login">	
		</div>
      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="password" class="form-control field-addition" required id="password-login">
		<div id="pass-err-login">
		</div>
      <button class="btn btn-info btn-login-itvideo" onclick="loginPost()">Login</button>
      	<div id="other-err-login">
		</div>
    </div>
    <div class="container-login" style="background-color:#f1f1f1">
      <button type="button" onclick="document.getElementById('login-form-itvideo').style.display='none'" class="btn btn-danger">Cancel</button>
      <span class="psw text-muted"><a  href="#">Forgot password?</a></span>
    </div>
  </div>
</div>
<!-- register from -->
<div id="register-form-itvideo" class="modal">
  <div class="modal-content animate" >
    <div class="imgcontainer">
      <span onclick="document.getElementById('register-form-itvideo').style.display='none'" class="close" title="Close Modal">&times;</span>
      <img src="<c:url value="/pics/avatar.png"/>" alt="Avatar" class="avatar">
    </div>

    <div class="container-login">
      <label><b>Username</b></label>
      <input type="text" placeholder="Enter Username" name="username" class="form-control field-addition" required id="username-register">
	  	<div id="user-err-register"></div>
      <label><b>Password</b></label>
      <input type="password" placeholder="Enter Password" name="password" class="form-control field-addition" required id="password-register">
      <label><b>Confirm Password</b></label>
      <input type="password" placeholder="Confirm Password" name="confirmPassword" class="form-control field-addition" required id="confirmPassword-register">
		<div id="pass-err-register"></div>
	  <label><b>Email</b></label>
	  <input type="text" placeholder="Email" class="form-control field-addition" name="email" required id="email-register"><br>
      	<div id="other-err-register"></div>
		<div id="success-register"></div>
      <button class="btn btn-info btn-login-itvideo" onclick="registerPost()">Register</button>
    </div>
    <div class="container-login" style="background-color:#f1f1f1">
      <button type="button" onclick="document.getElementById('register-form-itvideo').style.display='none'" class="btn btn-danger">Cancel</button>
      <button type="button" onclick="document.getElementById('register-form-itvideo').style.display='none'" class="btn btn-info" style="float: right;">Ok</button>
    </div>
  </div>
</div>

<script>
//close modal loigin if click other
var modalRegister = document.getElementById('register-form-itvideo');
var modalLogin = document.getElementById('login-form-itvideo');
// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modalRegister) {
    	modalRegister.style.display = "none";
    }
    if (event.target == modalLogin) {
    	modalLogin.style.display = "none";
    }
}
</script>
</body>
</html>