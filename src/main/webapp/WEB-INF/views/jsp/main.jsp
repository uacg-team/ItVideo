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
			<div>
				<select onchange="location = this.value;">
					 <option value="<c:url value="/main/sort/date" />" <c:if test="${sessionScope.sort eq \"date\" }"> selected </c:if>>SortByDate</option>
					 <option value="<c:url value="/main/sort/like" />" <c:if test="${sessionScope.sort eq \"like\" }"> selected </c:if>>SortByLikes</option>
					 <option value="<c:url value="/main/sort/view" />" <c:if test="${sessionScope.sort eq \"view\" }"> selected </c:if>>SortByViews</option>
				</select>
			</div>
			<jsp:include page="showVideosRequest.jsp"></jsp:include>
	    </div>
	    <div class="col-sm-1 sidenav">
	    </div>
	  </div>
	</div>




</body>
</html>