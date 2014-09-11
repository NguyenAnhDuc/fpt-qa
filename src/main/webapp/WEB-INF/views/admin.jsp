<html>
<head>
	<link rel="stylesheet" type="text/css" href="resources/css/bootstrap/bootstrap.min.css" />
	<script src="resources/js/jquery-1.11.1.min.js"></script>  
	<script src="resources/js/bootstrap/bootstrap.min.js"></script>
	<script type="text/javascript">
	function crawl() {
		//alert("searchtext");
		var numday = $('input:radio[name=inlineRadioOptions]:checked').val();
		$.ajax({
			type : "POST",
			url : "/rubyweb/admin/crawlPhimChieuRap",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			success : function(result) {
				if (result.status === "success") alert ("DONE!");
				else alert("Something went wrong");
			},
			error : function(result) {
				alert("Error");
			}
		});
	};
	</script>
</head>
<body>
	<div class="container">
		<header class="text-center">
			<img src="resources/images/fpt_logo.png" class="logo" />
			<h2>Ruby Project - Admin</h2>
			
		</header>
		<!-- <label class="radio-inline"> <input type="radio"
				name="inlineRadioOptions" id="inlineRadio1" value="1" checked="checked">
				1 days
			</label> <label class="radio-inline"> <input type="radio"
				name="inlineRadioOptions" id="inlineRadio2" value="2">
				2 days 
			</label> <label class="radio-inline"> <input type="radio"
				name="inlineRadioOptions" id="inlineRadio3" value="3">
				3 days
			</label> -->
	  <button type="button" class="btn btn-primary btn-lg btn-block" id="btnSubmit" onclick="crawl()">Crawl schedule from phimchieurap.com</button>
	</div>
</body>
</html>