<%@ page language="java" contentType="text/html; charset=UTF8"
    pageEncoding="UTF8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF8">
<title></title>
<script src="http://210.241.200.113:10080/recharge/js/jquery-1.10.2.js"></script>
</head>
<body>
<script type="text/javascript">
$( document ).ready(function() { 	
	$('#form').submit(function(e) {
		   var $this = $(this);
		   var $input =  $this.find('input').val();
		 if($input == '') {
		   alert ("you must choose a image");
		   return false; 
		   e.preventDefault(); 
		  }    
	});  
});

</script>
<form id="form" enctype="multipart/form-data" method="post" action="<%= request.getContextPath() + "/servlet/test/UploadFile2" %>">

	<input type="file" name="file1" size="30">
	
	&nbsp;&nbsp;&nbsp;<input type="submit" value="submit">
	
</form>
</body>
</html>