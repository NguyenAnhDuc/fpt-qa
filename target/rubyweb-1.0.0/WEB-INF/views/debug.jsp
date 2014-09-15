<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Debug</title>
<link rel="stylesheet" type="text/css" href="resources/css/debug.css" />
<link rel="stylesheet" type="text/css"
	href="resources/css/bootstrap/bootstrap.min.css" />
<script src="resources/js/jquery-1.11.1.min.js"></script>
<script src="resources/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-7  col-md-offset-5" id="panel-debug">
				<div class="timeline-centered">
					<article class="timeline-entry">
					<div class="timeline-entry-inner">
						<div class="timeline-icon bg-info">
							<i class="entypo-feather"></i>
						</div>
						<div class="timeline-label">
							<h2>Search in cache</h2>
							<p id="result-cache">The question is in cache.</p>
						</div>
					</div>
					</article>


					<article class="timeline-entry">
					<div id="panel-process-question" class="timeline-entry-inner">
						<div class="timeline-icon bg-secondary">
							<i class="entypo-suitcase"></i>
						</div>

						<div class="timeline-label">
							<h2>Process question to find intend and modifiers</h2>
							<p id="result-process-question">Result process question</p>
						</div>
					</div>
					</article>

					<article class="timeline-entry">
					<div id="panel-process-question" class="timeline-entry-inner">
						<div class="timeline-icon bg-secondary">
							<i class="entypo-suitcase"></i>
						</div>
						<div class="timeline-label">
							<h2>Process question to find intend and modifiers</h2>
							<p id="result-process-question">Result process question</p>
						</div>
					</div>
					</article>


					<article class="timeline-entry begin">
					<div class="timeline-entry-inner">
						<div class="timeline-icon"
							style="-webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg);">
							<i class="entypo-flight"></i> +
						</div>
					</div>
					</article>

				</div>

			</div>
		</div>
	</div>
</body>
</html>