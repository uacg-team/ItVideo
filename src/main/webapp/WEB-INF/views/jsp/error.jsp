<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>error</title>
</head>
<body>

<h1>Ooops! Something went wrong!</h1><br>

<c:if test="${exception eq \"SQLException\" }" >
	<img src="<c:url value="/pics/SQLException.png"/>" /><br>
	<h1><c:out value="Reason: ${getMessage}"></c:out></h1><br>
</c:if>

<c:if test="${exception eq \"VideoException\" }" >
	<img src="<c:url value="/pics/VideoException.png"/>" /><br>
	<h1><c:out value="Reason: ${getMessage}"></c:out></h1><br>
</c:if>

<c:if test="${exception eq \"UserException\" }" >
	<img src="<c:url value="/pics/UserException.png"/>" /><br>
	<h1><c:out value="Reason: ${getMessage}"></c:out></h1><br>
</c:if>


<c:if test="${exception eq \"VideoNotFoundException\" }" >
	<img src="<c:url value="/pics/VideoNotFoundException.png"/>" /><br>
	<h1><c:out value="Reason: ${getMessage}"></c:out></h1><br>
</c:if>

<c:if test="${exception eq \"UserNotFoundException\" }" >
	<img src="<c:url value="/pics/UserNotFoundException.png"/>" /><br>
	<h1><c:out value="Reason: ${getMessage}"></c:out></h1><br>
</c:if>

<c:if test="${exception eq \"IOException\" }" >
	<h1><c:out value="Reason: ${getMessage}"></c:out></h1><br>
</c:if>

<c:if test="${exception eq \"MessagingException\" }" >
	<h1><c:out value="Reason: ${getMessage}"></c:out></h1><br>
</c:if>



<a href="<c:url value="/main" />"><button type="button" formmethod="get">go to main page</button></a>

</body>
</html>