<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>change demo</title>
  <style>
  div {
    color: red;
  }
  </style>
  <!-- <script src="//code.jquery.com/jquery-1.4.2.js"></script> -->
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
</head>
<body>
 
<select name="sweets" multiple="multiple">
  <option>Chocolate</option>
  <option selected="selected">Candy</option>
  <option>Taffy</option>
  <option selected="selected">Caramel</option>
  <option>Fudge</option>
  <option>Cookie</option>
</select>
<div></div>
 
<script>
$( "select" ).change(function() {
	  alert( "Handler for .change() called." );
	});  
</script>
 
</body>
</html>