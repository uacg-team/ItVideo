<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>videos</title>
<style>

img {
    max-width: 100%;
    height: auto;
}
video {
    max-width: 100%;
    height: auto;
}

.row-eq-height {
  display: flex;
  flex-wrap: wrap;
}
</style>
</head>
<body>
<div class="container-fluid">
    <div class="row row-eq-height">
	<c:forEach items="${requestScope.videos}" var="video">	
    	<div class="col-xs-6 col-sm-4 col-md-4 col-lg-3" >
		<div>

			<a href="<c:url value="/player/${video.videoId}" />">	
				<video width="320" height="240" preload="none" poster="<c:url value="/thumbnail/${video.videoId}" />"></video>
			</a>
			
				<p class="text-primary"><strong><c:out value="${video.name}"></c:out></strong></p>
				<p class="text-primary">
				<c:forEach items="${video.tags}" var="tag">	
						<a href="#" class="btn btn-primary btn-xs"><c:out value="#${tag.tag} "></c:out></a>
				</c:forEach>
				</p>
			
			</div>
		</div>
	</c:forEach>
</div>
</div>
</body>
</html>
				
				<%-- 
				<p class="text-primary"><c:out value="Description: ${video.description}"></c:out></p>
				<p class="text-primary">		
				<a href="<c:url value="/viewProfile/${video.userId}" />">
					<c:out value="Owner: ${video.userName}"></c:out>
					<img class="img-rounded" src="<c:url value="/img/${video.userId}"/>" width="50px" height="auto"/>
				</a>
				</p>
				<p class="text-primary">
				<fmt:parseDate value="${ video.date }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
				Published: <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" /><br>
				</p>
				<p class="text-primary">
				<c:out value="Views: ${video.views}"></c:out>
				</p>
				<p class="text-primary">	
				<c:out value="Privacy: ${video.privacy}"></c:out>
				</p>
				 --%>