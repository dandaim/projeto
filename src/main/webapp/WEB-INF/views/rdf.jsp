<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
	<head>
		<meta charset="utf-8">
		<title>Home - DAHELE - UFRJ</title>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3/jquery.min.js"></script>
		<script>
			
			$(document).ready(function(){				
				$(".file").hide();				
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
				<div class="page-header">
					<h3>Choose a rdf source</h3>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<form:form method="post" action="${context}/rdf/request" class="form-horizontal" modelAttribute="tripletsForm" enctype="multipart/form-data" >
							<div class="form-group " >
								<label for="email" class="col-sm-1 control-label">Email</label>
								<div class="col-sm-5">
									<form:input type="email" id="email" class="form-control" placeholder="Enter email" name="email" path="email" />
									<form:errors path="email" cssClass="error" />
								</div>							
							</div>
							<div class="form-group" >
								<label for="name" class="col-sm-1 control-label">Name</label>
								<div class="col-sm-5">
									<form:input type="text" id="name" class="form-control" placeholder="Enter name" name="name" path="name" />
									<form:errors path="name" cssClass="error" />
								</div>							
							</div>
							<div class="form-group">
							    <div class="col-sm-offset-1 col-sm-10">
							      	<div class="radio">
									  <label>
									    <form:radiobutton path="option" value="url" checked="checked"/>
									    Use RDF from an url
									  </label>								  
									</div>
									<div class="radio">
									   <label>
									    <form:radiobutton path="option" value="file" />
									    Use RDF from a file
									  </label>							  
									</div>
							    </div>
						  	</div>						  						
							<div class="url form-group" >
								<label for="url" class="col-sm-1 control-label">Url</label>
								<div class="col-sm-5">
									<form:input id="url" class="form-control" placeholder="Enter rdf's url here" name="url" path="url" />
									<form:errors path="url" cssClass="error" />
								</div>							
							</div>											
							<div class="file form-group">
								<label for="file" class="col-sm-1 control-label">Rdf file<span class="glyphicon glyphicon-info-sign" title="Load your rdf file here."></span></label>
								<div class="col-sm-5">
									<form:input id="file" type="file" value="Choose Value" name="file" path="file" />
									<form:errors path="file" cssClass="error" />
								</div>							
							</div>
							<div class="form-group">
								<div class="col-sm-8 col-sm-offset-1">
									<input type="submit" class="btn btn-danger" value="Send Data" />
								</div>
							</div>									
						</form:form>
					</div>					
				</div>
			</div>
		</div>
		<%@include file="footer.jsp" %>

		<script>
			$("input:radio").change(function(){
				
				if( $(this).val() == "url" ) {
					$(".url").show();
					$(".file").hide();
				} else {
					$(".url").hide();
					$(".file").show();
				}				
			});
		</script>
	</body>
</html>
