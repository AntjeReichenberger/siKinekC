<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Shopping Online?</title>

<!-- METATAGS -->
<meta name="ROBOTS" content="index,follow" />
<meta name="author" content="KinekPoint" />
<meta name="copyright" content="2009 KinekPoint" />
<meta name="keywords" content="Kinek, safe shopping, Shipping, alternative, not home, safe deliveries" />
<meta name="description" content="safe shopping - embrace safe deliveries by shipping to a local KinekPoint" />

<!-- STYLESHEETS -->
<link rel="stylesheet" href="resource/css/common/messages.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/pages/landing/shoppingOnline/safeshopping.css" type="text/css" media="screen,projection" />

<!--[if IE 8]>
	<link rel="stylesheet" media="screen" type="text/css" href="resource/css/pages/landing/shoppingOnline/safeshopping-ie8.css"  />
<![endif]-->

<!--[if lte IE 7]>
	<link rel="stylesheet" media="screen" type="text/css" href="resource/css/pages/landing/shoppingOnline/safeshopping-ie7.css"  />
<![endif]-->

 <script type="text/javascript" src="resource/js/jquery-1.5.1.min.js"></script>
 <script type="text/javascript">
    function validateSearch(){
		var message = "<div id=\"message\" class=\"error\">";
		var isValid = true;

		if (document.getElementById('postalCode').value == "") {
			isValid = false;
			message += "<p>Please enter valid Zip or Postal code</p>";
		}

		var zipRegex = /^([0-9]{5}$)/;
		var postalRegex = /^[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}\s?[0-9]{1}[a-zA-Z]{1}[0-9]{1}$/;
		var postalNoSpaceRegex = /^[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}[0-9]{1}$/;
		if (jQuery('#postalCode').val() != '' && !(zipRegex.test(jQuery('#postalCode').val()) || postalRegex.test(jQuery('#postalCode').val()))) {
			isValid = false;
			message += "<p>Please enter a valid Zip or Postal Code</p>";
		}
		//If no space in postal code, add it to avoid bad search
		else if(jQuery('#postalCode').val() != '' && postalNoSpaceRegex.test(jQuery('#postalCode').val()) && isValid) {
			var spaced = jQuery('#postalCode').val().substring(0,3) + " " + jQuery('#postalCode').val().substring(3,6);
			jQuery('#postalCode').val(spaced);
		}
		
		message += "</div>";

		if(!isValid)
		{
			jQuery('#errors').html(message);
		}
		else
		{
			jQuery('#errors').html("");
		}

		return isValid;
	} 
 </script>

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
<body>

  <div id="container">
	<!-- start header -->
	<div id="header">
		<h1>Shopping Online?</h1>
		<h3>8 out of 10 people are not home during delivery hours.<br/>
			 Are you one of them?</h3>				
	</div>
	<!-- end header -->
	
	<!-- start maincontent -->
	<div id="maincontent">
		<div id="bubble">
			<div id="bubblemaintext">
				You work hard to buy what you want and then spend<br/>
				hours researching online before making the purchase.<br/>
				Why risk losing it if you're not going to be home?
			</div>		
			<div id="bubblefootertext">
				Kinek's nationwide network of secure KinekPoints will receive your online<br/>
				or catalogue orders, keep them safe, and notify you when they arrive.
			</div>
		 </div>					
		 <div id="maincontentsearchboxtitle">
			Enter your zip code to find a secure KinekPoint location near you.
		</div>
		<div id="maincontentsearchbox">
		  <s:form name="findDepots" id="findDepots" action="/DepotSearch.action">
			<div id="errors"></div>
			<s:errors />
			<s:messages />		  			
			<s:text id="postalCode" name="postalCode" maxlength="7"></s:text>&nbsp;
			<small class="validation"></small>
			<s:hidden id="cityRadius" name="cityRadius" value="10" ></s:hidden>
			<s:submit name="search" id="searchbtn" onclick="return validateSearch();" value=""/>						
		  </s:form>	
		</div>
		<div id="dropshadow">
			<img  src="resource/images/landing/shoppingOnline/safeshopping_dropshadow.jpg" alt="drop shadow" />
		</div>
		<div id="kplogo">
			<a href="https://www.kinek.com" >
				<img src="resource/images/landing/shoppingOnline/safeshopping_kp_logo.jpg" alt="Kinek"  />						
			</a>
			<div><a href="https://www.kinek.com" >Kinek</a></div>
		</div>
	</div>
	<!-- end maincontent --> 
	
	<!-- start footer -->
	<div id="footer">
		&#169; Copyright 2010&nbsp; 
			<span class="bullet"><img src="resource/images/landing/shoppingOnline/safeshopping_bullet.jpg" /></span>&nbsp;
			<a href="https://www.kinek.com" >Kinek</a>&nbsp;
		    <span class="bullet"><img src="resource/images/landing/shoppingOnline/safeshopping_bullet.jpg" /></span>&nbsp;
		   	<a href="/terms-of-use">Terms of Use</a>&nbsp;
			<span class="bullet"><img src="resource/images/landing/shoppingOnline/safeshopping_bullet.jpg" /></span>&nbsp;
			<a href="/privacy">Privacy</a>&nbsp;
    </div>			
    <!-- end footer -->
    
<!-- begin olark code --><script type='text/javascript'>/*{literal}<![CDATA[*/window.olark||(function(i){var e=window,h=document,a=e.location.protocol=="https:"?"https:":"http:",g=i.name,b="load";(function(){e[g]=function(){(c.s=c.s||[]).push(arguments)};var c=e[g]._={},f=i.methods.length; while(f--){(function(j){e[g][j]=function(){e[g]("call",j,arguments)}})(i.methods[f])} c.l=i.loader;c.i=arguments.callee;c.f=setTimeout(function(){if(c.f){(new Image).src=a+"//"+c.l.replace(".js",".png")+"&"+escape(e.location.href)}c.f=null},20000);c.p={0:+new Date};c.P=function(j){c.p[j]=new Date-c.p[0]};function d(){c.P(b);e[g](b)}e.addEventListener?e.addEventListener(b,d,false):e.attachEvent("on"+b,d); (function(){function l(j){j="head";return["<",j,"></",j,"><",z,' onl'+'oad="var d=',B,";d.getElementsByTagName('head')[0].",y,"(d.",A,"('script')).",u,"='",a,"//",c.l,"'",'"',"></",z,">"].join("")}var z="body",s=h[z];if(!s){return setTimeout(arguments.callee,100)}c.P(1);var y="appendChild",A="createElement",u="src",r=h[A]("div"),G=r[y](h[A](g)),D=h[A]("iframe"),B="document",C="domain",q;r.style.display="none";s.insertBefore(r,s.firstChild).id=g;D.frameBorder="0";D.id=g+"-loader";if(/MSIE[ ]+6/.test(navigator.userAgent)){D.src="javascript:false"} D.allowTransparency="true";G[y](D);try{D.contentWindow[B].open()}catch(F){i[C]=h[C];q="javascript:var d="+B+".open();d.domain='"+h.domain+"';";D[u]=q+"void(0);"}try{var H=D.contentWindow[B];H.write(l());H.close()}catch(E){D[u]=q+'d.write("'+l().replace(/"/g,String.fromCharCode(92)+'"')+'");d.close();'}c.P(2)})()})()})({loader:(function(a){return "static.olark.com/jsclient/loader0.js?ts="+(a?a[1]:(+new Date))})(document.cookie.match(/olarkld=([0-9]+)/)),name:"olark",methods:["configure","extend","declare","identify"]});

/* custom configuration goes here (www.olark.com/documentation) */

olark.identify('2281-625-10-3955');/*]]>{/literal}*/</script>

<!-- end olark code -->


  </div>		
</body>
</html>


