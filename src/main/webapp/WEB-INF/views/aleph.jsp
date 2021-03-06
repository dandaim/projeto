<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Home - DAHELE - UFRJ</title>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script>
		<script>
			
			$(document).ready(function(){				
				$(".files").hide();
				$(".files-opt").hide();			
			});		
		</script>
		<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet" media="screen">
		<link href="<c:url value="/resources/css/include.css" />" rel="stylesheet" media="screen" >
	</head> 
	<body>
		<div class="content" id="wrap">
			<div class="container">
				<script src="http://code.jquery.com/jquery.js" ></script>
				<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>			
				<c:url value="/showMessage.html" var="messageUrl" />
				<div class="page-header">
					<h3>Enter data for processing</h3>
				</div>
				<div class="row">
					<c:if test="${not empty msg}">
						<div class="alert alert-${msgType}" role="alert">${msg}</div>	
					</c:if>	
					<form:form method="post" action="${context}/aleph/request" class="form-horizontal" modelAttribute="beaconForm" enctype="multipart/form-data" >
						<div class="form-group" >
							<label for="email" class="col-sm-2 control-label">Email</label>
							<div class="col-sm-5">
								<form:input type="email" id="email" class="form-control" placeholder="Enter email" name="email" path="email" />
								<form:errors path="email" cssClass="error" />
							</div>							
						</div>
						<div class="form-group" >
							<label for="name" class="col-sm-2 control-label">Name</label>
							<div class="col-sm-5">
								<form:input type="text" id="name" class="form-control" placeholder="Enter name" name="name" path="name" />
								<form:errors path="name" cssClass="error" />
							</div>							
						</div>						
						<div class="form-group">
							<label for="arqb" class="col-sm-2 control-label">Base file<span class="glyphicon glyphicon-info-sign" title="Load your base file here."></span></label>
							<div class="col-sm-5">
								<form:input id="arqb" type="file" value="Choose Value" name="arqb" path="arqb" />
								<form:errors path="arqb" cssClass="error" />
							</div>							
						</div>
						<div class="form-group" id="rowpos">							
							<label class="col-sm-2 control-label">Positive files <span class="glyphicon glyphicon-info-sign" title="Load your positive files here. At least 1 file and maximum of 10 files." ></span></label>
							<div class="col-sm-10">
								<form:input type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:input class="files" type="file" value="Choose Value" name="arqpos[]" path="arqpos" />
								<form:errors path="arqpos" cssClass="error" />
							</div>							
						</div>
						<div class="form-group" id="rowneg">							
							<label class="col-sm-2 control-label">Negative files<span class="glyphicon glyphicon-info-sign" title="Load your negative files here. At least 1 file and maximum of 10 files." ></span></label>
							<div class="col-sm-10">
								<form:input type="file" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:input class="files" type="file" value="Choose Value" name="arqneg[]" path="arqneg" />
								<form:errors path="arqneg" cssClass="error" />
							</div>							
						</div>
						  
						<div class="form-group" id="optional">
							<label class="col-sm-2 control-label">Optional files<span class="glyphicon glyphicon-info-sign" title="Load optional files here. Maximum of 10 files." ></span>
							</label>
							<div class="col-sm-10">
								<form:input type="file" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
								<form:input type="file" class="files-opt" name="arqopt[]" value="Choose Value" path="arqopt" />
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-10 col-sm-offset-2">
								<input type="button" id="btnadd" class="btn btn-primary" value="Add files" />
								<input type="button" id="btnremove" class="btn btn-primary" value="Remove Files" />	
							</div>
							
						</div>
						<div class="form-group">
							<div class="col-sm-10 col-sm-offset-2">
								<input type="button" id="btnaddopt" class="btn btn-primary" value="Add Optional files" />
								<input type="button" id="btnremoveopt" class="btn btn-primary" value="Remove Optional Files" />		
							</div>
						</div>	
						<div class="form-group">
							<div class="col-sm-10 col-sm-offset-2">
								<input type="submit" class="btn btn-danger" value="Send Data" />
							</div>					
						</div>					
					</form:form>
				</div>
			</div>
		</div>
		<%@include file="footer.jsp" %>
		<script>
			$("#btnadd").click(function(){
				$("#rowpos .files").each( function(){					
					if( !$(this).is(":visible") ) {
						$(this).show();
						return false;
					}					
				});	
				$("#rowneg .files").each( function(){					
					if( !$(this).is(":visible") ) {
						$(this).show();
						return false;
					}					
				});
			});	

			$("#btnremove").click(function(){
				$("#rowpos .files").each( function(){					
					if( $(this).is(":visible") ) {
						$(this).hide();
						return false;
					}					
				});	
				$("#rowneg .files").each( function(){					
					if( $(this).is(":visible") ) {
						$(this).hide();
						return false;
					}					
				});
			});	
			$("#btnaddopt").click(function(){
				$("#optional .files-opt").each( function(){					
					if( !$(this).is(":visible") ) {
						$(this).show();
						return false;
					}					
				});					
			});
			$("#btnremoveopt").click(function(){
				$("#optional .files-opt").each( function(){					
					if( $(this).is(":visible") ) {
						$(this).hide();
						return false;
					}					
				});					
			});	
		</script>
	</body>
</html>
