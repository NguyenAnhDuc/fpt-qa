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
<div id="hello">This is text!</div>​
 
<script>
jQuery(document).ready(function() {
 
    jQuery('#hello').bind('html-change-pre', function() {
        console.log('html-change-pre triggered', this, arguments);
    });
    
    jQuery('#hello').bind('html-change-post', function() {
        console.log('html-change-post triggered', this, arguments);
    });
    
    console.log('Calling html with 1 arg:');
    jQuery('#hello').html('This is text (that has been changed)!');
    console.log('Calling html with no args:');
    console.log(jQuery('#hello').html());
});​
</script>
</body>
</html>