<html>
	<head>
		<style type="text/css">
			body
			{
				font-family: Arial;
				font-size: 16px;
				font-weight: normal;
			}
		</style>
	</head>
	<body>
		<script type="text/javascript" src="resource/js/jquery-1.5.1.min.js"></script>
		<script type="text/javascript">
			jQuery(document).ready(function(){
				document.forms[0].submit();
			});					
		</script>
		
		Redirecting you to PayPal...

		<form action="https://www.sandbox.paypal.com/cgi-bin/webscr" method="post">
			<input type="hidden" name="num" value="${actionBean.num}"/>
			<input type="hidden" name="cmd" value="${actionBean.cmd}"/>
			<input type="hidden" name="encrypted" value="${actionBean.encrypt}"/>
		</form>
	</body>
</html>