<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">


<link rel="stylesheet" type="text/css"
	href="resources/css/chat-widget.css" />
<link rel="stylesheet" type="text/css" href="resources/css/debug.css" />
<link rel="stylesheet" type="text/css" href="resources/css/main.css" />

<link rel="stylesheet" type="text/css"
	href="resources/css/combo_css/style.css" />

<script src="resources/js/jquery.scrollablecombo.js"></script>
<script>
	$(function() {
		$('#ui_element').scrollablecombo();

	});
	function onChat() {
		//alert("searchtext");
		var question = $('#btn-input').val();
		$('#result-intent').html("");
		$('#result-question-type').html("");
		$('#result-paramaters').html("");
		$('#result-time').html("");
		$('#result-final').html("");
		$
				.ajax({
					type : "POST",
					url : "/rubyweb/getAnswer",
					contentType : "application/x-www-form-urlencoded;charset=UTF-8",
					data : "question=" + encodeURIComponent(question),
					success : function(result) {
						var htmlResult = "";
						//question
						htmlResult = htmlResult
								.concat("<li class=\"left clearfix\"><span class=\"chat-img pull-left\">");
						htmlResult = htmlResult
								.concat("<img src=\"http://placehold.it/50/55C1E7/fff&text=U\" alt=\"User Avatar\" class=\"img-circle\" /></span>");
						htmlResult = htmlResult
								.concat("<div class=\"chat-body clearfix\">");
						htmlResult = htmlResult.concat("<p>");
						htmlResult = htmlResult.concat(result.question);
						htmlResult = htmlResult.concat("</p></div></li>");
						//answer
						htmlResult = htmlResult
								.concat("<li class=\"right clearfix\"><span class=\"chat-img pull-right\">");
						htmlResult = htmlResult
								.concat("<img src=\"http://placehold.it/50/FA6F57/fff&text=ME\" alt=\"User Avatar\" class=\"img-circle\" /></span>");
						htmlResult = htmlResult
								.concat("<div class=\"chat-body clearfix\">");
						htmlResult = htmlResult.concat("<p>");
						htmlResult = htmlResult.concat(result.answer);
						htmlResult = htmlResult.concat("</p></div></li>");
						$('#btn-input').val('');
						$('.chat').append(htmlResult);
						$('.panel-body').scrollTop(1E10);
						//show debug 
						$('#panel-debug').removeClass('hidden');
						/* if (result.inCache){
							$('#result-cache').html('The question is in cache');
							$('#panel-process-question').addClass('hidden');
							$('#panel-get-cache').removeClass('hidden');
						}
						else {
							$('#result-cache').html('The question is NOT in cache');
							$('#result-process-question').html('Process question to find intent and modifiers. Then, cached the question.');
							$('#panel-get-cache').addClass('hidden');
							$('#panel-process-question').removeClass('hidden');
						} 
						// head and modifiers
						var structureQuestion = "";
						structureQuestion += 'intent:	' + result.questionStructure.head + '</br> Modifiers:	'  ;
						for (var i=0;i<result.questionStructure.modifiers.length - 1;i++){
							structureQuestion += result.questionStructure.modifiers[i] + ", ";
						}
						if (result.questionStructure.modifiers.length > 0)
							structureQuestion += result.questionStructure.modifiers[result.questionStructure.modifiers.length-1];
						 */
						$('#result-intent').html(result.intent);
						$('#result-question-type').html(result.questionType);
						if (result.questionType == 'static') {
							$('#result-paramaters').html(
									'Mov_title: ' + result.movieTitle);
						} else {
							var htmlParamater = '', dateExtract = '';
							if (result.movieTicket.cinema != null)
								htmlParamater += 'Cin_name: '
										+ result.movieTicket.cinema + "</br>";
							if (result.movieTicket.movie != null)
								htmlParamater += 'Mov_Title: '
										+ result.movieTicket.movie + "</br>";
							if (result.beginTime != null)
								dateExtract += 'Begin Time: '
										+ new Date(result.beginTime) + "</br>";
							if (result.endTime != null)
								dateExtract += 'End Time: ' + new Date( result.endTime);
										+ "</br>";
							$('#result-time').html(dateExtract);
							$('#result-paramaters').html(htmlParamater);
						}
						$('#result-final').html(result.answer);
					},
					error : function(result) {
						alert("Error");
					}
				});
	};
</script>
<style type="text/css">
body {
	margin-top: 20px;
	background-color: #f0f0f0;
	font-family: "Helvetica Neue", Arial, Helvetica, Geneva, sans-serif;
}

.box {
	border: 15px solid #fff;
	height: 300px;
	width: 500px;
	position: relative;
	padding: 10px 10px 10px 10px;
	-moz-box-shadow: 0px 0px 2px #ccc inset;
	-webkit-box-shadow: 0px 0px 2px #ccc inset;
	box-shadow: 0px 0px 2px #ccc inset;
}

.box h3 {
	text-transform: uppercase;
	color: #ccc;
	text-shadow: 0 1px 0 #fff;
}
</style>

			<div class="row">
				<div class="col-md-5 col-lg-5">
					<div class="panel panel-primary">
						<div class="panel-heading"></div>
						<div class="panel-body">
							<ul class="chat">
								<li class="left clearfix"><span class="chat-img pull-left">
										<img src="http://placehold.it/50/55C1E7/fff&text=U"
										alt="User Avatar" class="img-circle" />
								</span>
									<div class="chat-body clearfix">
										<!-- <div class="header"> -->
										<!--  <strong class="primary-font"></strong> <small class="pull-right text-muted">
                                     <span class="glyphicon glyphicon-time"></span>12 mins ago</small>  -->
										<!-- </div> -->
										<p>Tối nay ngoài rạp có phim gì hay không?</p>
									</div></li>
								<li class="right clearfix"><span
									class="chat-img pull-right"> <img
										src="http://placehold.it/50/FA6F57/fff&text=ME"
										alt="User Avatar" class="img-circle" />
								</span>
									<div class="chat-body clearfix">
										<div class="header">
											<!-- <small class=" text-muted"><span class="glyphicon glyphicon-time"></span>13 mins ago</small>
                                    <strong class="pull-right primary-font">Bhaumik Patel</strong> -->
										</div>
										<p>Tối nay có X-men, Trái tim máu, và Ninja Rùa.</p>
									</div></li>
								<li class="left clearfix"><span class="chat-img pull-left">
										<img src="http://placehold.it/50/55C1E7/fff&text=U"
										alt="User Avatar" class="img-circle" />
								</span>
									<div class="chat-body clearfix">
										<div class="header">
											<!-- <strong class="primary-font">Jack Sparrow</strong> <small class="pull-right text-muted">
                                        <span class="glyphicon glyphicon-time"></span>14 mins ago</small> -->
										</div>
										<p>Gần đây có rạp nào tốt không?</p>
									</div></li>
								<li class="right clearfix"><span
									class="chat-img pull-right"> <img
										src="http://placehold.it/50/FA6F57/fff&text=ME"
										alt="User Avatar" class="img-circle" />
								</span>
									<div class="chat-body clearfix">
										<div class="header">
											<!-- <small class=" text-muted"><span class="glyphicon glyphicon-time"></span>15 mins ago</small>
                                    <strong class="pull-right primary-font">Bhaumik Patel</strong> -->
										</div>
										<p>Gần đây có rạp Lotte Lanmard và rạp Plantium The
											Garden.</p>
									</div></li>
							</ul>
						</div>
						<div class="panel-footer">
							<div class="input-group">
								<input id="btn-input" type="text" class="form-control input-sm"
									placeholder="Hỏi tôi..."
									onkeyup="if (event.keyCode == 13) document.getElementById('btn-chat').click();" />
								<span class="input-group-btn">
									<button class="btn btn-warning btn-sm" id="btn-chat"
										onclick="onChat()">Gửi</button>
								</span>
							</div>
						</div>

					</div>
					<div class="text-center">
						<%@ include file="questionType.jsp"%>
					</div>
					<%-- <div class="box">
                <h3 class="text-center">Cached Questions</h3>
                <select class="questions_combo" id="ui_element">
                    <option value="A" selected>Choose a question</option>
                    <c:forEach var="question" items="${questions}">
							<option value="${question}">${question}</option>
						</c:forEach>
                </select>
            </div>  --%>
				</div>
				<div class="col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1 "
					id="panel-debug">
					<div class="timeline-centered">
						<!--
					<article class="timeline-entry">
					 <div class="timeline-entry-inner">
						<div class="timeline-icon bg-violet">
							<i class="entypo-feather"></i>
						</div>
						<div class="timeline-label">
							<h2>Search in cache Process</h2>
							<p id="result-cache">The question is in cache.</p>
						</div>
					</div>
					</article>
					
					<article class="timeline-entry">
					<div id="panel-get-cache" class="timeline-entry-inner">
						<div class="timeline-icon bg-coral">
							<i class="entypo-suitcase"></i>
						</div>
						<div class="timeline-label">
							<h2>Get intent and modifiers from cache</h2>
							<p id="result-get-cache">If the question was in cache, get question from cache.</p>
						</div>
					</div>
					</article> 
					
					<article class="timeline-entry">
					<div id="panel-process-question" class="timeline-entry-inner">
						<div class="timeline-icon bg-info">
							<i class="entypo-suitcase"></i>
						</div>

						<div class="timeline-label">
							<h2>Process question to find intent and modifiers</h2>
							<p id="result-process-question">If the question was in cache, process question to find intent and modifiers. Then, cached the question.</p>
						</div>
					</div>
					</article>

					<article class="timeline-entry">
					<div id="panel-result-question-structure" class="timeline-entry-inner">
						<div class="timeline-icon bg-secondary">
							<i class="entypo-suitcase"></i>
						</div>
						<div class="timeline-label">
							<h2>Intent and modifiers</h2>
							<p id="result-question-structure">Question structure result</p>
						</div>
					</div>
					</article>
					
					<article class="timeline-entry">
					<div id="panel-process-getanswer" class="timeline-entry-inner">
						<div class="timeline-icon bg-warning">
							<i class="entypo-suitcase"></i>
						</div>
						<div class="timeline-label">
							<h2>Get Answer Process</h2>
							<p id="result-getanswer">Query DB or get answer from Google</p>
						</div>
					</div>
					</article>
					
					<article class="timeline-entry">
					<div id="panel-result-final" class="timeline-entry-inner">
						<div class="timeline-icon bg-success">
							<i class="entypo-suitcase"></i>
						</div>
						<div class="timeline-label">
							<h2>Result</h2>
							<p id="result-final">Result</p>
						</div>
					</div>
					</article>
					-->

						<article class="timeline-entry">
							<div class="timeline-entry-inner">
								<div class="timeline-icon bg-violet">
									<i class="entypo-feather"></i>
								</div>
								<div class="timeline-label">
									<h2>Get Intent</h2>
									<p id="result-intent">Intent of the question</p>
								</div>
							</div>
						</article>

						<article class="timeline-entry">
							<div class="timeline-entry-inner">
								<div class="timeline-icon bg-coral">
									<i class="entypo-suitcase"></i>
								</div>
								<div class="timeline-label">
									<h2>Depend on the intent, get question type!</h2>
									<p id="result-question-type">The question is dynamic or
										static?</p>
								</div>
							</div>
						</article>

						<article class="timeline-entry">
							<div id="panel-process-question" class="timeline-entry-inner">
								<div class="timeline-icon bg-info">
									<i class="entypo-suitcase"></i>
								</div>

								<div class="timeline-label">
									<h2>Query Paramater</h2>
									<p id="result-paramaters">Paramaters to query DB</p>
								</div>
							</div>
						</article>

						<article class="timeline-entry">
							<div id="panel-result-final" class="timeline-entry-inner">
								<div class="timeline-icon  bg-secondary">
									<i class="entypo-suitcase"></i>
								</div>
								<div class="timeline-label">
									<h2>Time Extractor</h2>
									<p id="result-time">Time Conditions</p>
								</div>
							</div>
						</article>

						<article class="timeline-entry">
							<div id="panel-result-final" class="timeline-entry-inner">
								<div class="timeline-icon bg-success">
									<i class="entypo-suitcase"></i>
								</div>
								<div class="timeline-label">
									<h2>Result</h2>
									<p id="result-final">Result</p>
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
			<div class="row">
				    <div id="disqus_thread"></div>
    <script type="text/javascript">
        /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
        var disqus_shortname = 'ftiqa'; // required: replace example with your forum shortname

        /* * * DON'T EDIT BELOW THIS LINE * * */
        (function() {
            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
        })();
    </script>
    <noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
    <a href="http://disqus.com" class="dsq-brlink">comments powered by <span class="logo-disqus">Disqus</span></a>
    
			</div>
	</tiles:putAttribute>
</tiles:insertDefinition>