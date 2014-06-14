<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-definition>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>KinekPoint</title>

<!-- METATAGS -->
<meta name="ROBOTS" content="index,follow" />
<meta name="author" content="KinekPoint" />
<meta name="copyright" content="2009 KinekPoint" />
<meta name="keywords" content="" />
<meta name="description" content="" />

<!-- STYLESHEETS -->
<link rel="stylesheet" href="resource/css/pages/landing/landing.css" type="text/css" media="screen,projection" />
<link rel="stylesheet" href="resource/css/plugins/prettyPhoto.css" type="text/css" media="screen,projection" />

<!--[if lt IE 8]><link rel="stylesheet" media="screen" type="text/css" href="resource/css/pages/landing/landing-ie.css"  /><![endif]-->

<!-- JAVASCRIPT -->
<script type="text/javascript" src="resource/js/swfobject2.js"></script>
<script type="text/javascript" src="resource/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="resource/js/jquery.prettyPhoto.js"></script>

<!-- CUSTOM FUNCTIONS -->
<script type="text/javascript" charset="utf-8">
	// Search field focus
    $(document).ready(function () {
  		$('#criteria').one("focus", function() {
  		$(this).val("");
 	 	});
	});
	//Initialize prettyPhoto lightbox
	$(document).ready(function(){
			$("a[rel^='prettyPhoto']").prettyPhoto({
			theme: 'dark_rounded'
		});
	});
</script>

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
<body>

	<div id="container">
		<!--Header-->
		<div id="header" class="clearfix">
			<div id="headerJoin">
				<h2>
					Guarantee that you'll always receive your delivery. 
					<a href="Register.action">
						<img src="resource/images/landing/btn-sign-up.gif" alt="Sign Up Now" width="159" height="39"/>
					</a>
				</h2>
			</div>
			<a href="Home.action" ><img class="logo" src="resource/images/landing/logo.gif" alt="Kinek"/></a>
		</div>
		<!--@end Header-->
		
		<!--Content-->
		<div id="pcontent"  class="clearfix">
			<!--Main Content Area-->
			<div id="main">
				<!--Intro Content-->
				<div class="cBlock clearfix">
					<div id="video">
						<p><a target="new" href="http://www.youtube.com/watch?v=WAz-2I9q-k4"><img src="resource/images/landing/btn-playvideo-ie.png" alt="Play Video" width="104" height="102"/></a></p>
						<p><a target="new" href="http://www.youtube.com/watch?v=WAz-2I9q-k4" title="See how Kinek works">Watch a demo video</a></p>
					</div>
					
					${text}
					
				</div>
				<!--@end Intro Content-->
				
				<!--Steps-->
				<div class="cBlock">
					<h1 class="howh1">Here's How It Works</h1>
					<div class="clearfix">
						<div class="step">
							<h2>Step 1 <span>Get Kinek</span></h2>
							<img src="resource/images/landing/icon-search-ie.png" alt="icon-search" class="fltLeft" width="41" height="33"/>
							<p><a href="DepotSearch.action">Search</a> for your nearest KinekPoint, then sign up for a <strong>free</strong> Kinek account.</p>
						</div>
						<div class="step">
							<h2>Step 2 <span>Shop Anywhere</span></h2>
							<img src="resource/images/landing/icon-shop-ie.png" alt="icon-shop" class="fltLeft" width="55" height="47"/>
							<p>When checking out, fill in the address of your KinekPoint, and your Kinek Number, which you received while signing up.</p>
						</div>
						<div class="step last">
							<h2>Step 3 <span>Pick Up</span></h2>
							<img src="resource/images/landing/icon-pickup-ie.png" alt="icon-pickup" class="fltLeft" width="46" height="59"/>
							<p><strong>You'll be notified when your package has arrived.</strong><br />For a small fee, you can pick it up from your KinekPoint.</p>
						</div>
					</div>
				</div>
				<!--@end Steps-->
				
				<!--CTA-->
				<div id="cta" class="clearfix">
					<h1 class="howh1">Get Started</h1>
					<div class="start"><h2>or start using<br /> Kinek today:</h2> <a href="Register.action"><img src="resource/images/landing/btn-sign-up.gif" alt="Sign Up Now" width="159" height="39"/></a></div>
					<div class="search">
						<h2>Find a KinekPoint near you</h2>
						<form name="search" id="search" action="/DepotSearch.action">
							<fieldset>
								<input type="text" name="criteria" id="criteria" value="Enter an Address or Zip Code"/>
								<input type="submit" name="submit" id="submit" class="submit" value="Search"/>
							</fieldset>
						</form>
					</div>
				</div>
				<!--@end CTA-->
			</div>
			<!--@end Main Content Area-->
			
			<!--images and Footer-->
			<div id="bigImg">
				${image}				
				<div id="footer">
					<p>&copy; 2009 Kinek Technologies, Inc.</p>
					<ul>
						<li><a href="/terms-of-use"">Terms of Service</a></li>
						<li><a href="/privacy">Privacy</a></li>
						<li><a href="/faq">FAQ</a></li>
					</ul>
				</div>			
			</div>
			<!--@end images and Footer-->
		</div>
	</div>
	
<!-- begin olark code --><script type='text/javascript'>/*{literal}<![CDATA[*/window.olark||(function(i){var e=window,h=document,a=e.location.protocol=="https:"?"https:":"http:",g=i.name,b="load";(function(){e[g]=function(){(c.s=c.s||[]).push(arguments)};var c=e[g]._={},f=i.methods.length; while(f--){(function(j){e[g][j]=function(){e[g]("call",j,arguments)}})(i.methods[f])} c.l=i.loader;c.i=arguments.callee;c.f=setTimeout(function(){if(c.f){(new Image).src=a+"//"+c.l.replace(".js",".png")+"&"+escape(e.location.href)}c.f=null},20000);c.p={0:+new Date};c.P=function(j){c.p[j]=new Date-c.p[0]};function d(){c.P(b);e[g](b)}e.addEventListener?e.addEventListener(b,d,false):e.attachEvent("on"+b,d); (function(){function l(j){j="head";return["<",j,"></",j,"><",z,' onl'+'oad="var d=',B,";d.getElementsByTagName('head')[0].",y,"(d.",A,"('script')).",u,"='",a,"//",c.l,"'",'"',"></",z,">"].join("")}var z="body",s=h[z];if(!s){return setTimeout(arguments.callee,100)}c.P(1);var y="appendChild",A="createElement",u="src",r=h[A]("div"),G=r[y](h[A](g)),D=h[A]("iframe"),B="document",C="domain",q;r.style.display="none";s.insertBefore(r,s.firstChild).id=g;D.frameBorder="0";D.id=g+"-loader";if(/MSIE[ ]+6/.test(navigator.userAgent)){D.src="javascript:false"} D.allowTransparency="true";G[y](D);try{D.contentWindow[B].open()}catch(F){i[C]=h[C];q="javascript:var d="+B+".open();d.domain='"+h.domain+"';";D[u]=q+"void(0);"}try{var H=D.contentWindow[B];H.write(l());H.close()}catch(E){D[u]=q+'d.write("'+l().replace(/"/g,String.fromCharCode(92)+'"')+'");d.close();'}c.P(2)})()})()})({loader:(function(a){return "static.olark.com/jsclient/loader0.js?ts="+(a?a[1]:(+new Date))})(document.cookie.match(/olarkld=([0-9]+)/)),name:"olark",methods:["configure","extend","declare","identify"]});

/* custom configuration goes here (www.olark.com/documentation) */

olark.identify('2281-625-10-3955');/*]]>{/literal}*/</script>

<!-- end olark code -->
	
</body>
</html>
</s:layout-definition>
