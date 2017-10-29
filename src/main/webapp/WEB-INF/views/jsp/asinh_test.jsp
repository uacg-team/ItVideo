<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link type="text/css" rel="stylesheet" href="<c:url value="/css/comments.css"/>"/>

<script type="text/javascript" src="<c:url value="/js/commentBuilder.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/commentLikes.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/dateParser.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/test.js"/>"></script>
</head>
<body>

<div id="videoComments">
<button onclick="testHtml()">info</button>
</div>
<div id="newComment">
always on top for new comment! will be added in this section after comment
</div>
show all comments
<div id="comments">
<button onclick="comments(6)">info</button>
</div>
<div id="test">
oppp
	<div id="showmore">
	<button onclick="test()">testing</button>
	</div>
</div>
</body>
</html>