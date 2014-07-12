<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<c:set var="context" value="${pageContext.request.contextPath}" />
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Welcome - DAHELE - UFRJ</title>
		<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet" media="screen">
		<link href="<c:url value="/resources/css/include.css" />" rel="stylesheet" media="screen" >
	</head>
	<body>
		<div class="content" id="wrap">
			<div class="container">
				<script src="http://code.jquery.com/jquery.js" ></script>
				<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>	
				<div class="page-header">
					<h3>Welcome to DAHELE</h3>
				</div>
				<div class="row">
					<div class="col-sm-8 col-sm-offset-2">
						<p>Welcome to DAHELE, an online tool that aims to assist in machine learning by processing assertions in ILP (inductive logic programming) by using the Aleph (A Learning Engine for Proposing Hypotheses), derived from Prolog.</p>
					</div>
					<div class="col-sm-8 col-sm-offset-2">
						<p>Aleph uses the information contained in the following three files to build a theory:</p>
					</div>
					<div class="col-sm-8 col-sm-offset-2">
						<ul>
							<li>file.b: it contains information about the domain knowledge (intensional and extensional), restrictions on search and languages ​​(mode declarations, determinations), type constraints and parameters;</li>
							<li>file.f: it contains the positive examples (only ground facts);</li>
							<li>file.n: it contains the negative examples (only ground facts). This file may not exist, since Aleph can perform learning by using only positive examples.</li>
						</ul>
					</div>
					<div class="col-sm-8 col-sm-offset-2">
						<p>Upon processing completion, a response is generated. This, being a success or a failure, will be forwarded to the email entered in the application form.</p>
					</div>
					<br/><br/>
					<div class="col-sm-3 col-sm-offset-2">
						<label>To build a theory from aleph files click button below</label>
						<a href="${context}/aleph"><button type="button" class="btn btn-primary btn-lg btn-block">Use aleph here</button></a>						
					</div>
					<div class="col-sm-3 col-sm-offset-1">
						<label>To generate a triplets file click button below</label>
						<a href="${context}/rdf"><button type="button" class="btn btn-primary btn-lg btn-block">Use rdf here</button></a>						
					</div>

				</div>
			</div>
		</div>
		<%@include file="footer.jsp" %>
	</body>
</html>