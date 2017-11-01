<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
</style>
</head>
<body>
<c:forEach items="${requestScope.videos}" var="video">	
	<a href="<c:url value="/player/${video.videoId}" />">	
		<video width="320" height="240" preload="none" poster="<c:url value="/thumbnail/${video.videoId}" />"></video>
	</a>
	<p class="text-primary">
	<c:forEach items="${video.tags}" var="tag">	
			<a href="<c:url value="/search/tag/${tag.tag}"/>" class="btn btn-primary btn-xs"><c:out value="#${tag.tag} "></c:out></a>
	</c:forEach>
	</p>
</c:forEach>
</body>
</html>