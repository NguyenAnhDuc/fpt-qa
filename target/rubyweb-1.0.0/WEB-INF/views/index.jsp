<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html lang="en" class="no-js">
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
		<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
		<title>Natural Language Form</title>
		<meta name="description" content="Natural Language Form with custom text input and drop-down lists" />
		<meta name="keywords" content="Natural Language UI, sentence form, text input, contenteditable, html5, css3, jquery" />
		<meta name="author" content="Codrops" />
		<link rel="shortcut icon" href="../favicon.ico"> 
		<link rel="stylesheet" type="text/css" href="resources/css/default.css" />
		<link rel="stylesheet" type="text/css" href="resources/css/component.css" />
		<script src="resources/js/modernizr.custom.js"></script>
	</head>
	<body class="nl-blurred">
		<div class="container demo-1">
			<!-- Top Navigation -->
			<div class="codrops-top clearfix">
				<a class="codrops-icon codrops-icon-prev" href="http://tympanus.net/Development/MorphingDevices/"><span>Previous Demo</span></a>
				<span class="right"><a class="codrops-icon codrops-icon-drop" href="http://tympanus.net/codrops/?p=15139"><span>Back to the Codrops Article</span></a></span>
			</div>
			<header>
				<h1>Hôm này xem gì? <span>FPT-Ruby Project</span></h1>	
			</header>
			<div class="main clearfix">
				<form id="nl-form" class="nl-form">
					Tôi muốn xem
					<select>
						<option value="0" selected> phim</option>
						<c:forEach var="movie" items="${movies}">
							<option value="${movie}">${movie}</option>
						</c:forEach>
						
					</select>
					<br />thể loại
					<select>
						<option value="1" selected>anytype</option>
					 	<option value="1">2D</option>
					 	<option value="2">3D</option>
					</select>
					<br />ở 
					<select>
						<option value="1" selected>rạp</option>
						<c:forEach var="cinema" items="${cinemas}">
							<option value="${cinema}">${cinema}</option>
						</c:forEach>
					</select>
					<br />vào lúc 
					<select>
						<option value="1" selected>anytime</option>
					 	<option value="1">7 p.m.</option>
					 	<option value="2">8 p.m.</option>
					 	<option value="3">9 p.m.</option>
					</select>
					
					<%-- in <input type="text" value="" placeholder="any city" data-subline="For example: <em>Los Angeles</em> or <em>New York</em>"/> --%>
					<div class="nl-submit-wrap">
						<button class="nl-submit" type="submit">Tìm kiếm</button>
					</div>
					<div class="nl-overlay"></div>
				</form>
			</div>
		</div><!-- /container -->
		<script src="resources/js/nlform.js"></script>
		<script>
			var nlform = new NLForm( document.getElementById( 'nl-form' ) );
		</script>
	</body>
</html>