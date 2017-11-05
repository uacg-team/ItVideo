<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>	
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>main</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	
	<div class="container-fluid text-center">    
	  <div class="row content">
	    <div class="col-sm-1 sidenav">
	    </div>
	    <div class="col-sm-10 text-left"> 
		<ul class="nav nav-pills">
			<li class="dropdown">
			    <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
			      Sort by <span class="caret"></span>
			    </a>
			    <ul class="dropdown-menu">
			      <li <c:if test="${sessionScope.sort eq \"date\" }">  class="active" </c:if>><a href="<c:url value="/main/sort/date" />">Date</a></li>
			      <li <c:if test="${sessionScope.sort eq \"like\" }">  class="active" </c:if>><a href="<c:url value="/main/sort/like" />">Likes</a></li>
			      <li <c:if test="${sessionScope.sort eq \"view\" }">  class="active" </c:if>><a href="<c:url value="/main/sort/view" />">Views</a></li>
			    </ul>
 			</li>
 		</ul>
 		
		<jsp:include page="showVideosRequestP.jsp"></jsp:include>
		
		<!-- pagination -->
		<br/>	    
		<br/>	    
		<div class="container">
			<div class="row content">
				<div class="col-sm-1">
				</div>
				<div class="col-sm-10 text-center">
					<form action="/ItVideo/main/page" method="get">
					    <button class="btn btn-primary" type="submit" value="1" name="pageid">&laquo;</button>
						<c:forEach begin="1" end="${totalPages}" varStatus="loop">
							<c:if test="${pageid == loop.index}">
								<button class="btn disabled" type="submit" value="${loop.index}" name="pageid">${loop.index}</button>
							</c:if>
							<c:if test="${pageid != loop.index}">
								<button class="btn btn-primary" type="submit" value="${loop.index}" name="pageid">${loop.index}</button>
							</c:if>
						</c:forEach>
						<button class="btn btn-primary" type="submit" name="pageid" value="${totalPages}" >&raquo;</button>
					</form>
				</div>
				<div class="col-sm-1">
				</div>
			</div>
		</div>
		<br/>	    
		<br/>
		<!-- pagination -->
			
	    </div>
	    <div class="col-sm-1 sidenav">
	    </div>
	  </div>
	</div>
</body>
</html>