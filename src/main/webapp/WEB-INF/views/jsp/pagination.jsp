<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
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
</body>
</html>