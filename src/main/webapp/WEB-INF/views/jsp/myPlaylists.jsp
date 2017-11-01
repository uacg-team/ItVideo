<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
		function addToPlaylist1(playlistId,userId,videoId) {
			if (typeof userId === 'undefined') {
			    alert("First login!");
			    return;
			}
			var request = new XMLHttpRequest();
			request.onreadystatechange =  function() {
				if (this.readyState == 4 && this.status == 200) {
					var add = document.getElementById("playlist".concat(playlistId));
					if(add.className === "glyphicon glyphicon-minus"){
						add.className="glyphicon glyphicon-ok";
					}else{
						add.className="lyphicon glyphicon-minus";
					}
				}
			};
			var url = "addToPlaylist";
			var param = "playlistId="+playlistId+"&videoId="+videoId;
			request.open("POST", url, true);
			request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			request.send(param);
		}
		function addToPlaylist(playlistId,userId,videoId) {
			if (typeof userId === 'undefined') {
			    alert("First login!");
			    return;
			}
			var request = new XMLHttpRequest();
			request.onreadystatechange =  function() {
				if (this.readyState == 4 && this.status == 200) {
					var add = document.getElementById("playlist".concat(playlistId));
					if(add.alt==="notInPlaylist"){
						add.alt="inPlaylist";
						add.src="<c:url value="/pics/ok.png"/>";
					}else{
						add.alt="notInPlaylist";
						add.src="<c:url value="/pics/not_ok.png"/>";
					}
				}
			};
			var url = "addToPlaylist";
			var param = "playlistId="+playlistId+"&videoId="+videoId;
			request.open("POST", url, true);
			request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			request.send(param);
		}
		/* When the user clicks on the button, 
		toggle between hiding and showing the dropdown content */
		function showMyPlaylists() {
		    document.getElementById("dropPlaylists").classList.toggle("show");
		}

		// Close the dropdown if the user clicks outside of it
		window.onclick = function(event) {
		  if (!event.target.matches('.dropbtnplaylists')) {
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
.dropbtnplaylists {
    background-color: #999966;
    color: black;
    padding: 8px;
    font-size: 16px;
    border: none;
    cursor: pointer;
}

.dropbtn:hover, .dropbtn:focus {
    background-color: #4d4d33;
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
<!-- show my playlist while watching  -->
<div class="btn-group">
  <a href="#" class="btn btn-primary">add to playlist</a>
  <a href="#" class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></a>
		<ul class="dropdown-menu" >
  		<c:forEach items="${requestScope.myPlaylists}" var="playlist">
	  			<li onclick="addToPlaylist(${playlist.playlistId},${user.userId},${requestScope.mainVideo.videoId})">
	  				<c:if test="${playlist.videoStatus == 1}">
				      	<img alt="inPlaylist" id="playlist${playlist.playlistId}" src="<c:url value="/pics/ok.png"/>" style="width: 22px; height: auto" >
    				</c:if>
    				<c:if test="${playlist.videoStatus != 1}">
						<img alt="notInPlaylist" id="playlist${playlist.playlistId}" src="<c:url value="/pics/not_ok.png"/>" style="width: 22px; height: auto" >	
    				</c:if>
    				<span>${playlist.playlistName}</span>
    			</li>
	    </c:forEach>
	    </ul>
 </div>


<%-- 

<div class="dropdown">
<button onclick="showMyPlaylists()" class="dropbtnplaylists">add to playlist</button>
  <div id="dropPlaylists" class="dropdown-content">
	  	<c:forEach items="${requestScope.myPlaylists}" var="playlist">
  			<ul onclick="addToPlaylist(${playlist.playlistId},${user.userId},${requestScope.mainVideo.videoId})">
	  			<li>
	  				<c:if test="${playlist.videoStatus == 1}">
	    				<img alt="inPlaylist" id="playlist${playlist.playlistId}" src="<c:url value="/pics/ok.png"/>" style="width: 22px; height: auto" >
    				</c:if>
    				<c:if test="${playlist.videoStatus != 1}">
	    				<img alt="notInPlaylist" id="playlist${playlist.playlistId}" src="<c:url value="/pics/not_ok.png"/>" style="width: 22px; height: auto" >
    				</c:if>
    			</li>
    			<li>
	    			<p>${playlist.playlistName}</p>
	    		</li>
   			 </ul>
	    </c:forEach>
  </div>
</div>

 --%>
</body>
</html>