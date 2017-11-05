<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>videos</title>
<style>
#textLimit {
   overflow: hidden;
   text-overflow: ellipsis;
   display: -webkit-box;
   line-height: 16px;     /* fallback */
   max-height: 32px;      /* fallback */
   -webkit-line-clamp: 2; /* number of lines to show */
   -webkit-box-orient: vertical;
}

video {
    max-width: 100%;
    height: auto;
}

.row-eq-height {
  display: flex;
  flex-wrap: wrap;
}

#videosList {
 max-width: 600px; 
  overflow: hidden;
}
</style>
</head>
<body>
<div class="container-fluid">
    <div class="row row-eq-height">
	<c:forEach items="${requestScope.videos}" var="video">	
    	<div class="col-xs-6 col-sm-4 col-md-4 col-lg-3" >
		<div id="videosList">   
		<div class="video">
			<a href="<c:url value="/player/${video.videoId}" />">	
				<%-- 	<c:url value="thumbnail/${video.videoId}" /> --%>
				 <video width="320" height="240" preload="none" poster="${pageContext.request.contextPath}/thumbnail/${video.videoId}">
				      <source src="<c:url value="/video/${video.videoId}" />" type="video/mp4">
				</video>
			</a>
			<a href="<c:url value="/player/${video.videoId}" />">
				<p class="text-primary" id="textLimit" >
				<strong><c:out value="${video.name}"></c:out></strong>
				</p>
			</a>
			<p class="text-primary">
			<c:if test="${not empty video.tags }">
				<c:forEach items="${video.tags}" var="tag">	
						<a href="<c:url value="/search/tag/${tag.tag}"/>" class="btn btn-primary btn-xs"><c:out value="#${tag.tag} "></c:out></a>
				</c:forEach>
			</c:if>
			</p>
		</div>
		</div>
		</div>
	</c:forEach>
</div>
</div>


<script type="text/javascript">
window.onload = function() { //executes code after DOM loads
	 //--- select all <video> on the page
	 const vids = document.getElementsByTagName(`video`)
	 // Loop over the selected elements and add event listeners
	 for (let i = 0; i < vids.length; i++) {
	   vids[i].addEventListener(`mouseover`, function(e) { 
	     vids[i].play()
	   })
	   vids[i].addEventListener( `mouseout`, function(e) {
	     vids[i].pause()
	   })
	 }
	}
</script>
</body>
</html>