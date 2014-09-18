<html>
<head>
	<link rel="stylesheet" type="text/css" href="resources/css/bootstrap/bootstrap.min.css" />
	
</head>
<body>
	<div class="container">
		<header class="text-center" style="margin-bottom: 20px">
			<img src="resources/images/fpt_logo.png" class="logo" />
			<h2>Ruby Project - Admin</h2>
			
		</header>
	  <form action="admin/crawlPhimChieuRap">
	  	<input type="submit" class="btn btn-primary btn-lg btn-block" id="btnSubmit" value="Crawl schedule from phimchieurap.com" />
	  </form>	
	  <form action="admin/crawl">
	  	<input type="submit" class="btn btn-primary btn-lg btn-block" id="btnSubmit" value="Crawl Manually" />
	  </form>
	  <form action="admin/show">
	  	<input type="submit" class="btn btn-primary btn-lg btn-block" id="btnSubmit" value="Show Tickets Today" />
	  </form>
	</div>
</body>
</html>