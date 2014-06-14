<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>Kinek - Administration Portal</title>

<!-- METATAGS -->
<meta name="author" content="KinekPoint" />
<meta name="copyright" content="2009 KinekPoint" />
<meta name="keywords" content="KinekPoint" />
<meta name="description" content="KinekPoint" />
<meta name="robots" content="index,follow" />

<!-- STYLESHEETS -->
<link rel="stylesheet" href="resource/css/reset.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/forms.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/style.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/superfish.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/datePicker.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/jquery.ptTimeSelect.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/jquery-themes/kinek-blue/jquery-ui-1.8.10.custom.css" type="text/css" media="screen,projection" />

<!--[if IE 8]><link rel="stylesheet" href="resource/css/ie8.css" type="text/css" media="screen,projection" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" href="resource/css/ie7.css" type="text/css" media="screen,projection" /><![endif]-->
<!--[if IE 6]><link rel="stylesheet" href="resource/css/ie6.css" type="text/css" media="screen,projection" /><![endif]-->

<!-- PRINT STYLSHEET -->
<link rel="stylesheet" href="resource/css/print.css" type="text/css" media="print" />

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/prototype/1/prototype.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/scriptaculous/1/scriptaculous.js"></script>
              
<!-- JAVASCRIPT -->
<script type="text/javascript" src="resource/js/md5.js"></script>
<script type="text/javascript" src="resource/js/swfobject2.js"></script>
<script type="text/javascript" src="resource/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="resource/js/jquery-ui-1.8.9.custom.min.js"></script>
<script type="text/javascript" src="resource/js/jquery.hoverIntent.js"></script>
<script type="text/javascript" src="resource/js/jquery.superfish.js"></script>
<script type="text/javascript" src="resource/js/jquery.supersubs.js"></script>
<script type="text/javascript" src="resource/js/jquery.maskedinput-1.2.2.min.js"></script>
<script type="text/javascript" src="resource/js/jquery.maxlength.js"></script>
<script type="text/javascript" src="resource/js/date.js"></script>
<script type="text/javascript" src="resource/js/jquery.datePicker.js"></script>
<script type="text/javascript" src="resource/js/jquery.ptTimeSelect.js"></script>

<script type="text/javascript" src="resource/js/custom.js"></script>
<script type="text/javascript" xml:space="preserve">
	/*
	 * Function that uses Prototype to invoke an action of a form. Slurps the values
	 * from the form using prototype's 'Form.serialize()' method, and then submits
	 * them to the server using prototype's 'Ajax.Updater' which transmits the request
	 * and then renders the resposne text into the named container.
	 *
	 * @param form reference to the form object being submitted
	 * @param event the name of the event to be triggered, or null
	 * @param container the name of the HTML container to insert the result into
	 */
	function invoke(form, event, container) {
	    var params = Form.serialize(form);
	    if (event != null) params = event + '&' + params;
	    new Ajax.Updater(container, form.action, {method:'post', postBody:params});
	}
</script>
<script type="text/javascript">
/* Pause function. This will allow animation to pause temporarily before occuring */
jQuery.fn.pause = function(duration) {
    jQuery(this).animate({ dummy: 1 }, duration);
    return this;
};

/* Fade out success message after a set length of time
 * Also fade out both success and error messages on click */
jQuery(document).ready(function() {
	if (jQuery('#message.success') != null) {
		jQuery('#message.success').pause(15000).fadeOut('slow');
	}
	if (jQuery('#message') != null) {
		jQuery('#message').click(function() {
			jQuery(this).fadeOut('slow', function() {
				jQuery(this).hide();
			});
	 	});
	}
});
</script>
      
</head>

