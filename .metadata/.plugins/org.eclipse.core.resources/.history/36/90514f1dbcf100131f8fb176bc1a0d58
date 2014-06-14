<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-definition>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>${pageTitle}</title>

<!-- METATAGS -->
<meta name="ROBOTS" content="index,follow" />
<meta name="author" content="KinekPoint" />
<meta name="copyright" content="2009 KinekPoint" />
<meta name="keywords" content="" />
<meta name="description" content="" />

<!-- STYLESHEETS -->
<link rel="stylesheet" href="resource/css/depot/reset.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/depot/style.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/depot/forms.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/jquery.ptTimeSelect.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/jquery-themes/kinek-blue/jquery-ui-1.8.10.custom.css" type="text/css" media="screen,projection" />

<!--[if IE 7]><link rel="stylesheet" href="resource/css/depot/ie7.css" type="text/css" media="screen,projection" /><![endif]-->
<!--[if IE 6]><link rel="stylesheet" href="resource/css/depot/ie6.css" type="text/css" media="screen,projection" /><![endif]-->

<!-- PRINT STYLSHEET -->
<link rel="stylesheet" href="resource/depot/css/print.css" type="text/css" media="print" />

<!-- JAVASCRIPT -->
<script type="text/javascript" src="resource/js/swfobject2.js"></script>
<script type="text/javascript" src="resource/js/jquery-1.5.1.min.js"></script> 
<script type="text/javascript" src="resource/js/jquery.validate.js"></script>
<script type="text/javascript" src="resource/js/jquery.metadata.js"></script>
<script type="text/javascript" src="resource/js/jquery.maskedinput-1.2.2.min.js"></script>
<script type="text/javascript" src="resource/js/jquery.ptTimeSelect.js"></script>


<!-- CUSTOM FUNCTIONS -->
<script type="text/javascript" src="resource/js/custom_depot.js"></script>
	
</head>
  <body>
  
  	<!-- WRAPPER -->
	<div id="wrapper">
  
	  	<!-- HEADER -->
		<div id="header" class="clearfix">
		
			<!-- TOPNAV -->
			<div id="topnav" class="clearfix">
				<s:link beanclass="org.webdev.kpoint.action.LoginActionBean" title="Sign In">Sign In</s:link>
				<span class="separator">|</span> 		
				<s:link beanclass="org.webdev.kpoint.action.depot.ContactActionBean" title="Contact Us">Contact Us</s:link>
				<span class="separator">|</span> 		
				<s:link beanclass="org.webdev.kpoint.action.depot.AboutActionBean" title="Contact Us">About Us</s:link>			
			</div>
			<!-- /TOPNAV -->
			
			<!-- LOGO -->
			<s:link beanclass="org.webdev.kpoint.action.depot.HomeActionBean" title="Kinek Home" id="logo"><img src="resource/images/logo_top.png" width="326" height="50" alt="Kinek Home" /></s:link>
			<!-- /LOGO -->	
		
			<!-- NAV -->
			<ul id="nav" class="clearfix">
				<li><s:link beanclass="org.webdev.kpoint.action.depot.HomeActionBean" title="Home">Home</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.depot.HowItWorksActionBean" title="How It Works">How It Works</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="FAQ">FAQ<s:param name="action">FAQ</s:param></s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.depot.BecomeAKinekPointActionBean" title="Become A KinekPoint">Become A KinekPoint</s:link></li>
			</ul>
			<!-- /NAV -->
		
		</div>
		<!-- /HEADER -->
	  
	  	<s:layout-component name="body"></s:layout-component>
		
	</div>
	<!-- /WRAPPER -->
	
	<!-- FOOTER -->
	<div id="footer" class="clearfix">
		
		<!-- COPYRIGHT -->
		<p class="copyright">&copy; Copyright 2009, Kinek Technologies Inc.</p>
		<!-- /COPYRIGHT -->
		
		<!-- BOTTOMNAV -->
		<p class="bottomnav">
			<jsp:useBean id="externalSettingsManager" scope="application" class="org.webdev.kpoint.util.ExternalSettingsManagerHelper"/>
			<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Privacy Policy">
				Privacy Policy
				<s:param name="action">Privacy</s:param>
			</s:link>
			<span class="separator">|</span>
			<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Terms of Service">
				Terms of Service
				<s:param name="action">Terms</s:param>
			</s:link>
		</p>
		<!-- /BOTTOMNAV -->
		
	</div>
	<!-- /FOOTER -->
	
	<!-- begin olark code --><script type='text/javascript'>/*{literal}<![CDATA[*/window.olark||(function(i){var e=window,h=document,a=e.location.protocol=="https:"?"https:":"http:",g=i.name,b="load";(function(){e[g]=function(){(c.s=c.s||[]).push(arguments)};var c=e[g]._={},f=i.methods.length; while(f--){(function(j){e[g][j]=function(){e[g]("call",j,arguments)}})(i.methods[f])} c.l=i.loader;c.i=arguments.callee;c.f=setTimeout(function(){if(c.f){(new Image).src=a+"//"+c.l.replace(".js",".png")+"&"+escape(e.location.href)}c.f=null},20000);c.p={0:+new Date};c.P=function(j){c.p[j]=new Date-c.p[0]};function d(){c.P(b);e[g](b)}e.addEventListener?e.addEventListener(b,d,false):e.attachEvent("on"+b,d); (function(){function l(j){j="head";return["<",j,"></",j,"><",z,' onl'+'oad="var d=',B,";d.getElementsByTagName('head')[0].",y,"(d.",A,"('script')).",u,"='",a,"//",c.l,"'",'"',"></",z,">"].join("")}var z="body",s=h[z];if(!s){return setTimeout(arguments.callee,100)}c.P(1);var y="appendChild",A="createElement",u="src",r=h[A]("div"),G=r[y](h[A](g)),D=h[A]("iframe"),B="document",C="domain",q;r.style.display="none";s.insertBefore(r,s.firstChild).id=g;D.frameBorder="0";D.id=g+"-loader";if(/MSIE[ ]+6/.test(navigator.userAgent)){D.src="javascript:false"} D.allowTransparency="true";G[y](D);try{D.contentWindow[B].open()}catch(F){i[C]=h[C];q="javascript:var d="+B+".open();d.domain='"+h.domain+"';";D[u]=q+"void(0);"}try{var H=D.contentWindow[B];H.write(l());H.close()}catch(E){D[u]=q+'d.write("'+l().replace(/"/g,String.fromCharCode(92)+'"')+'");d.close();'}c.P(2)})()})()})({loader:(function(a){return "static.olark.com/jsclient/loader0.js?ts="+(a?a[1]:(+new Date))})(document.cookie.match(/olarkld=([0-9]+)/)),name:"olark",methods:["configure","extend","declare","identify"]});
	/* custom configuration goes here (www.olark.com/documentation) */
	olark.identify('2281-625-10-3955');/*]]>{/literal}*/</script>
	<!-- end olark code -->
  </body>
</html>

</s:layout-definition>