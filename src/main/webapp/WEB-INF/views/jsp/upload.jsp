<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include><br>
	
	<c:if test="${ sessionScope.user == null }">
		<c:redirect url="/login"></c:redirect>
	</c:if>
	
	<c:if test="${ sessionScope.user != null }">
	<div class="row">
		<div class="col-lg-3"></div>
		<div class="col-lg-6">
		<div class="well bs-component">
		<form class="form-horizontal" action="<c:url value="/uploadVideo"/>" method="post"  enctype="multipart/form-data">
		  <fieldset>
		    <legend>Upload video</legend>
		    <div class="form-group">
		      <label for="text" class="col-lg-2 control-label">Name</label>
		      <div class="col-lg-10">
		        <input type="text" name="name" class="form-control" placeholder="Video Name">
				<span class="help-block">Required</span>
		      </div>
		    </div>
		    <div class="form-group">
		      <label for="text" class="col-lg-2 control-label">Description</label>
		      <div class="col-lg-10">
		        <input type="text"  name="description" class="form-control" placeholder="Video Description">
		      </div>
		    </div>
		    <div class="form-group">
		      <label for="text" class="col-lg-2 control-label">Tags</label>
		      <div class="col-lg-10">
		      	<input type="text"  name="tags"  class="form-control" placeholder="Tags">
		        <span class="help-block">Please add tags separated by space</span>
		      </div>
		    </div>
		    <div class="form-group">
		      <label for="textArea" class="col-lg-2 control-label">Browse</label>
		      <div class="col-lg-10">
			 	<input type="file" name="newVideo" accept="video/mp4">
		        <br><span class="help-block">Required</span><br><span class="help-block">Please upload video format mp4</span>
		      </div>
		    </div>
		    <div class="form-group">
		      <label class="col-lg-2 control-label">Privacy</label>
		      <div class="col-lg-10">
		        <div class="radio">
		          <label>
		            <input name="privacy" type="radio" name="optionsRadios" value="1" checked="checked">
		            Public
		          </label>
		        </div>
		        <div class="radio">
		          <label>
		            <input name="privacy"  type="radio" name="optionsRadios" value="2">
		            Private
		          </label>
		        </div>
		      </div>
		    </div>
		    <div class="form-group">
		      <div class="col-lg-10 col-lg-offset-2">
		     <!--    <button type="reset" class="btn btn-default">Cancel</button> -->
		        <input type="submit" class="btn btn-primary" value="Upload">
		        <!-- <button type="submit" class="btn btn-primary">Upload</button> -->
		      </div>
		    </div>
		    
			<c:if test="${ error != null }">
				<div class="err">
					<h1><c:out value="${error}"></c:out></h1>
				</div>
			</c:if>
			
		  </fieldset>
		</form>
		</div>
		</div>
		<div class="col-lg-3"></div>
	</div>
	</c:if>
</body>
</html>