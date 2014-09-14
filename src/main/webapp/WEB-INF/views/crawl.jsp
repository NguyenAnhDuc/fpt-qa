<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<!-- Latest compiled and minified CSS -->
<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
<script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">

<!-- Optional theme -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">

<!-- Latest compiled and minified JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<script type="text/javascript">
	function crawlManual() {
		var cin_name = $("#cin_name").val();
		var mov_title = $("#mov_title").val();
		var time = $("#schedule").val();

		$.ajax({
			type : "POST",
			url : "/rubyweb/admin/crawlManual",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			data : "cin_name=" + encodeURIComponent(cin_name) + "&mov_title="
					+ encodeURIComponent(mov_title) + "&time="
					+ encodeURIComponent(time),
			success : function(result) {
				if (result.status === "success")
					alert("DONE!");
				else
					alert("Something went wrong");
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
			<img src="../resources/images/fpt_logo.png" class="logo" />
			<h2>Ruby Project - Crawl data</h2>
		</header>
	   <c:if test="${status=='failed'}">
	   		<div class="alert alert-danger" role="alert">Oh snap! Something went wrong!</div>
	   </c:if>
	   
	  <c:if test="${status=='success'}">
	   		<div class="alert alert-success" role="alert">Schedule added!</div>
	   </c:if>
	   
	   <form action="/rubyweb/admin/crawlManual" method="post"  role="form" data-toggle="validator" class="form-horizontal">
			<div class="row" style="margin-bottom: 10px">
			<select class="form-control " id = "cin_name" name="cin_name">
				<option>CGV Vincom City Towers</option>
				<option>Lotte Cinema Landmark</option>
				<option>Platinum Vincom Royal City</option>
				<option>Platinum Cineplex Times City</option>
				<option>TT Chiếu Phim Quốc Gia</option>
			</select>
			</div>
			<div class="form-group ">
				<label class="col-sm-2 control-label">Tên phim</label>
				<div class="col-sm-10">
				<input type="text" class="form-control " id="mov_title" name="mov_title"
						placeholder="Tên phim" required>
				</div>
			</div>
			<div class="form-group ">
				<label for="inputPassword" class="col-sm-2 control-label">Giờ chiếu</label>
				<div class="col-sm-10">
					<textarea rows="3" class="form-control" id="schedule" name="time"
						placeholder="Giờ chiếu" required>
					</textarea>
				</div>
			</div>
			<h5>Schedule for: </h5> 
			<label class="radio-inline">
			  <input type="radio" name="numdays" id="inlineRadio1" value="1" > 1 days
			</label>
			<label class="radio-inline">
			  <input type="radio" name="numdays" id="inlineRadio2" value="2"> 2 days
			</label>
			<label class="radio-inline">
			  <input type="radio" name="numdays" id="inlineRadio3" value="3" checked="checked"> 3 days
			</label>
			<button type="submit" class="btn btn-primary btn-lg btn-block " id="btnSubmit" style="margin-top: 10px">Thêm lịch chiếu</button>
			
		</form> 
	</div>
</body>
</html>