<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
		function addToPlaylist(playlistId,userId,videoId) {
			if (typeof userId === 'undefined') {
			    alert("First login!");
			    return;
			}
			var request = new XMLHttpRequest();
			request.onreadystatechange =  function() {
				//add mark alredy in list
			};
			var url = "addToPlaylist";
			var param1 = "playlistId=";
			var param =param1.concat(playlistId,"&userId=",userId);
			request.open("POST", url, true);
			request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			request.send(param);
		}
		/* When the user clicks on the button, 
		toggle between hiding and showing the dropdown content */
		function myFunction() {
		    document.getElementById("myDropdown").classList.toggle("show");
		}

		// Close the dropdown if the user clicks outside of it
		window.onclick = function(event) {
		  if (!event.target.matches('.dropbtn')) {
		    var dropdowns = document.getElementsByClassName("dropdown-content");
		    var i;
		    for (i = 0; i < dropdowns.length; i++) {
		      var openDropdown = dropdowns[i];
		      if (openDropdown.classList.contains('show')) {
		        openDropdown.classList.remove('show');
		      }
		    }
		  }
		}
</script>
<style>
.dropbtn {
    background-color: #00bfff;
    color: black;
    padding: 8px;
    font-size: 16px;
    border: none;
    cursor: pointer;
}

.dropbtn:hover, .dropbtn:focus {
    background-color: #0086b3;
}

.dropdown {
    position: relative;
    display: inline-block;
}

.dropdown-content {
    display: none;
    position: absolute;
    background-color: #f9f9f9;
    min-width: 160px;
    overflow: auto;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    z-index: 1;
}

.dropdown-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
}

.dropdown a:hover {background-color: #f1f1f1}

.show {display:block;}
</style>
</head>
<body>
<b>add to playlist</b>
<select name="addToPlaylist" onchange="location = this.value;">
	<c:forEach items="${requestScope.myPlaylists}" var="playlist">
		 <option value="with ajax send post to playlist?playlistName=${playlist.playlistName}">${playlist.playlistName}</option>
	</c:forEach>
</select>
<div class="dropdown">
<button onclick="myFunction()" class="dropbtn">myPlaylists</button>
  <div id="myDropdown" class="dropdown-content">
  	<ul>
	  	<c:forEach items="${requestScope.myPlaylists}" var="playlist">
	    		<img alt="notIn" id="like${playlist.playlistId}" src="<c:url value="/pics/not_ok.png"/>" style="width: 25px; height: auto" onclick="addToPlaylist(${playlist.playlistId},${user.userId},${requestScope.mainVideo.videoId})">
	    		<p>${playlist.playlistName}</p>
	    </c:forEach>
    </ul>
  </div>
</div>
</body>
</html>