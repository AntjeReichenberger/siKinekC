<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=iso-8859-1">
	<meta name="description" content="${actionBean.landingPage.kpName} KinekPoint provides a solution that can help Millions of ${actionBean.landingPage.nationality} solve their shipping problems.">
	<meta name="keywords" content="${actionBean.landingPage.metaTagKeyword}">
	<title>Send your deliveries to the ${actionBean.landingPage.kpName} KinekPoint</title>
	<link rel="stylesheet" href="resource/css/pages/landing/landing-urban.css" type="text/css" media="screen,projection" />
	<script type="text/javascript" src="resource/js/jquery-1.5.1.min.js"></script>
	<script type="text/javascript" src="resource/js/swfobject2.js"></script>
	
	<!-- Yahoo Ad Campaign -->
	<SCRIPT language="JavaScript" type="text/javascript">
	<!-- Yahoo! Inc.
	window.ysm_customData = new Object();
	window.ysm_customData.conversion = "transId=,currency=,amount=";
	var ysm_accountid  = "1FAPANITILI0EPG9P4CLAEP2UGG";
	document.write("<SCR" + "IPT language='JavaScript' type='text/javascript' "
	+ "SRC=https://" + "srv1.wa.marketingsolutions.yahoo.com" + "/script/ScriptServlet" + "?aid=" + ysm_accountid
	+ "></SCR" + "IPT>");
	// -->
	</SCRIPT>
	
	<!--  Google Analytics Tracking -->
	<script type="text/javascript">
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-9692338-1']);
	  _gaq.push(['_trackPageview']);
	
	  (function() {
	    var ga = document.createElement('script'); ga.type = 'text/javascript';
		ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(ga, s);
	  })();
	</script>

</head>
<body class="depot">

<div id="wrapper">
	<h1>Why ship to ${actionBean.landingPage.kpName}?</h1>
	<h2>Millions of ${actionBean.landingPage.nationality} are not home during delivery hours and are faced with:</h2>
	<br class="clear" />
	<div id="trio">
			<div id="trioAlternative"></div>
	</div>
	<script type="text/javascript">
	// <![CDATA[
		var flashvars = {};
		var params = {allowfullscreen: "true", allowscriptaccess: "always", wmode: "transparent"};
		swfobject.embedSWF("resource/flash/landingUrban.swf", "trio", "505", "175", "9.0.0", "resource/flash/expressInstall.swf", flashvars, params);
	// ]]>
	</script>
	<br class="clear" />

	<ul class="flashcaption">
	<li class="missed">Missed Deliveries </li>
	<li class="stolen">Stolen Packages </li>
	<li class="damaged">Damaged Packages</li>	
	</ul>
	<br class="clear" />
	
	<div id="bubble">
		<h2>Online shopping is easy, getting your package is not...</h2>
		<h3>${actionBean.landingPage.kpName} is your alternate shipping address ${actionBean.landingPage.prefix} ${actionBean.landingPage.location}.</h3>
		<span class="listintro">How to ship to ${actionBean.landingPage.kpName}:</span>
		<ul>
			<li>Sign-up for a Kinek account</li>
			<li>Select ${actionBean.landingPage.kpName} as your KinekPoint</li>
			<li>Use your KinekPoint shipping address when ordering online or from a catalogue</li>
		</ul>
		<span class="listoutro">Kinek will notify you the moment your package arrives!</span>
	</div>
	<div id="signup"><a href="/sign-up" title="Sign-Up Now!"></a></div>
	
	<br class="clear" />
	
	<div id="footer">
		<div id="logo"><a href="/" title="Kinek"></a></div>
		
		<br class="clear" />
		
		<div id="footlinks">
			<div id="copyright">© Copyright 2011</div>
			<span class="linkdivider">&bull;</span>
			Kinek
			<span class="linkdivider">&bull;</span>
			<a href="/terms-of-use">Terms of Use</a>
			<span class="linkdivider">&bull;</span>
			<a href="/privacy">Privacy</a>
		</div>
	</div>
</div>

<!-- begin olark code --><script type='text/javascript'>/*{literal}<![CDATA[*/window.olark||(function(i){var e=window,h=document,a=e.location.protocol=="https:"?"https:":"http:",g=i.name,b="load";(function(){e[g]=function(){(c.s=c.s||[]).push(arguments)};var c=e[g]._={},f=i.methods.length; while(f--){(function(j){e[g][j]=function(){e[g]("call",j,arguments)}})(i.methods[f])} c.l=i.loader;c.i=arguments.callee;c.f=setTimeout(function(){if(c.f){(new Image).src=a+"//"+c.l.replace(".js",".png")+"&"+escape(e.location.href)}c.f=null},20000);c.p={0:+new Date};c.P=function(j){c.p[j]=new Date-c.p[0]};function d(){c.P(b);e[g](b)}e.addEventListener?e.addEventListener(b,d,false):e.attachEvent("on"+b,d); (function(){function l(j){j="head";return["<",j,"></",j,"><",z,' onl'+'oad="var d=',B,";d.getElementsByTagName('head')[0].",y,"(d.",A,"('script')).",u,"='",a,"//",c.l,"'",'"',"></",z,">"].join("")}var z="body",s=h[z];if(!s){return setTimeout(arguments.callee,100)}c.P(1);var y="appendChild",A="createElement",u="src",r=h[A]("div"),G=r[y](h[A](g)),D=h[A]("iframe"),B="document",C="domain",q;r.style.display="none";s.insertBefore(r,s.firstChild).id=g;D.frameBorder="0";D.id=g+"-loader";if(/MSIE[ ]+6/.test(navigator.userAgent)){D.src="javascript:false"} D.allowTransparency="true";G[y](D);try{D.contentWindow[B].open()}catch(F){i[C]=h[C];q="javascript:var d="+B+".open();d.domain='"+h.domain+"';";D[u]=q+"void(0);"}try{var H=D.contentWindow[B];H.write(l());H.close()}catch(E){D[u]=q+'d.write("'+l().replace(/"/g,String.fromCharCode(92)+'"')+'");d.close();'}c.P(2)})()})()})({loader:(function(a){return "static.olark.com/jsclient/loader0.js?ts="+(a?a[1]:(+new Date))})(document.cookie.match(/olarkld=([0-9]+)/)),name:"olark",methods:["configure","extend","declare","identify"]});

/* custom configuration goes here (www.olark.com/documentation) */

olark.identify('2281-625-10-3955');/*]]>{/literal}*/</script>

<!-- end olark code -->

</body>
</html>
