<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
<jsp:include page="header.jsp"></jsp:include><br>

	<div class="container-fluid text-center">    
	  <div class="row content">
	    <div class="col-sm-1 sidenav">
	    </div>
	    <div class="col-sm-10 text-left"> 
	    
<div class="container-fluid">
    <div class="row row-eq-height">
	<c:forEach items="${list}" var="video">	
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
</div>
	    <div class="col-sm-1 sidenav">
	    </div>
	    </div>
	    </div>
	    
	    
<div class="row content">
<div class="col-sm-1">
</div>
<div class="col-sm-10">
	<form action="/ItVideo/main1" method="post">
 	<button class="btn btn-primary" type="submit" value="1" name="pageid">&laquo;</button>
	<c:forEach begin="1" end="${totalPages/total}" varStatus="loop">	
		<button class="btn btn-primary" type="submit" value="${loop.index}" name="pageid">${loop.index}</button>
	</c:forEach>
	<button class="btn btn-primary" type="submit" value="<fmt:formatNumber type = "number" maxFractionDigits = "0" value = "${totalPages/total}" />" name="pageid">&raquo;</button>
	</form>
</div>
<div class="col-sm-1">
</div>
</div>
	   
</body>
</html>