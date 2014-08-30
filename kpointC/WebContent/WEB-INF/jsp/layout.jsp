<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-definition>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">
<head profile="http://gmpg.org/xfn/11">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache">
<title>${pageTitle}</title>

<!-- METATAGS -->
<meta name="ROBOTS" content="index,follow" />
<meta name="author" content="KinekPoint" />
<meta name="copyright" content="2009 KinekPoint" />
<meta name="keywords" content="" />
<meta name="description" content="" />

<!-- STYLESHEETS -->
<c:choose>
	<c:when test="${externalSettingsManager.concatenateCss}">
		<link rel="stylesheet" href="resource/css/structure/structure-cat.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/common/common-cat.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/plugins/plugins-cat.css" type="text/css" media="screen,projection" />
	</c:when>
	<c:otherwise>
		<link rel="stylesheet" href="resource/css/structure/header.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/structure/toolbox.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/structure/content.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/structure/footer.css" type="text/css" media="screen,projection" />
		
		<link rel="stylesheet" href="resource/css/common/reset.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/common/forms.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/common/general.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/common/messages.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/common/search.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/common/tables.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/common/infowindowpopup.css" type="text/css" media="screen,projection" />
		
		<link rel="stylesheet" href="resource/css/plugins/datePicker.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/plugins/jScrollPane.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/plugins/prettyPhoto.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/plugins/superfish.css" type="text/css" media="screen,projection" />
		<link rel="stylesheet" href="resource/css/plugins/jquery-ui-1.8.10.custom.css" type="text/css" media="screen,projection" />
	</c:otherwise>	
</c:choose>

<!-- JAVASCRIPT -->
<script type='text/javascript' src='https://ajax.googleapis.com/ajax/libs/prototype/1/prototype.js'></script>
<script type="text/javascript" src="resource/js/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="resource/js/jquery-ui-1.8.9.custom.min.js"></script>
<script type="text/javascript" src="resource/js/jScrollPane.js"></script>
<script type="text/javascript" src="resource/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="resource/js/jquery.cornerz.js"></script>
<script type="text/javascript" src="resource/js/jquery.maskedinput-1.2.2.min.js"></script>
<script type="text/javascript" src="resource/js/jFav_v1.0.js"></script>
<script type="text/javascript" src="resource/js/swfobject2.js"></script>
<script type="text/javascript" src="resource/js/jquery.innerfade.js"></script>
<script type="text/javascript" src="resource/js/jquery.superfish.js"></script>
<script type="text/javascript" src="resource/js/jquery.supersubs.js"></script>

<!-- CUSTOM FUNCTIONS -->
<script type="text/javascript" src="resource/js/infowindowpopup.js"></script>
<script type="text/javascript" src="resource/js/custom.js"></script>
<script type="text/javascript" src="resource/js/jquery.prettyPhoto.js"></script>
<script type="text/javascript" src="resource/js/jquery.watermark.min.js"></script>

<!--  Device detection - if Iphone or Ipod display prompt for our free app -->
<script type="text/javascript">
   function setDetectionCookie ()
   {
       var expires = new Date();
       expires.setTime(expires.getTime() + (90 * 1000 * 60 * 60 * 24));
       document.cookie = 'skip_device_detect=false;expires=' + expires.toGMTString() + ';path=/';
   }
   
   if (document.cookie.indexOf('skip_device_detect=false') == -1) {
       var agent = navigator.userAgent;
	   if((agent.match(/iPhone/i)) || agent.match(/iPod/i)) {
           var device = "iPhone";
           if(agent.match(/iPod/i))
              device = "iPod";
           
               var result=confirm("Use Kinek on your " + device + " with our free App!  Tap OK to download it from the Apple App Store..");
           if (result==true)
               {
               window.location = 'http://itunes.apple.com/app/kinek/id452364465?mt=8';
           }
           else {
               setDetectionCookie();
           }
       }
   }
</SCRIPT>

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

<script type="text/javascript">
	function hoverLink(li) {
		if (!jQuery(li).hasClass('active')) {
			jQuery(li).addClass('hover');
		}
	}
	
	function unhoverLink(li) {
		if (!jQuery(li).hasClass('active')) {
			jQuery(li).removeClass('hover');
		}
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


${org.webdev.kpoint.action.HomeActionBean.getClass()}

<%
String thisPage = new String (request.getServletPath().toString());
String homePage = new String ("/WEB-INF/jsp/home.jsp");
String loginPage = new String ("/WEB-INF/jsp/login.jsp");
String signupPage = new String ("/WEB-INF/jsp/signup.jsp");
String homeClass = "";
String signedInClass = "";
boolean home = false;
boolean signedIn = false;
boolean inADB = false;
boolean showSignUpBT = false;
boolean showAllTabs = false;

if (thisPage.contentEquals(homePage)) 
{ 
	homeClass ="home";
	home = true;
  	out.println("<meta name=\"google-site-verification\" content=\"8T_29dwaaRwzKGgK0HJsaG2RCvwAqvZ-K5dxsDRhxEc\" />");
}
%>

<c:choose>
	<c:when test="${actionBean.signedIn}">
		<% 
			signedInClass = "signedIn"; 
			signedIn = true;
		%>
	</c:when>
</c:choose>

<c:choose>
	<c:when test="${actionBean.showAllTabs}">
		<% 
			showAllTabs = true;
		%>
	</c:when>
</c:choose>

<%
	if (!signedIn && !home && !thisPage.contentEquals(loginPage) && !thisPage.contentEquals(signupPage)) {
		showSignUpBT = true;
	}
%>

<% if(signedIn){ %>	
	<!--  Social Sharing via Janrain -->
	
	<script type="text/javascript">
		function displaySocialSharing(){
			RPXNOW.loadAndRun(['Social'], sharingcallback);
	}
	
	function sharingcallback() {
		var activity = new RPXNOW.Social.Activity(
		   "Share your comment",
		   "",
		   "http://www.kinek.com");
		activity.setTitle("Discovered Kinek");
		activity.setDescription("Just got my very own US address with Kinek! Online shopping just got a whole lot easier :)");	   
		RPXNOW.Social.publishActivity(activity);
	}
	</script>
<% } %>

</head>

<body class="<%out.print(homeClass);%> <%out.print(signedInClass);%>">

<div id="wrapper">
	<div id="header" class="clearfix">
		<span id="logo">
			<img src="resource/images/logo_top.png" width="326" height="50" alt="Kinek Home" />
			
		</span>
		
		<div id="topnav" class="clearfix">
			<% if (signedIn) { %>
				<span class="welcome">Welcome ${actionBean.activeUser.firstName}!</span>
				<!-- <s:link beanclass="org.webdev.kpoint.action.LoginActionBean" event="logout" title="Logout">Logout</s:link> -->
				<s:link beanclass="org.webdev.kpoint.action.wordpress.WPLoginActionBean" event="logout" title="Logout">Logout</s:link>
				<span class="separator">|</span>
				<s:link beanclass="org.webdev.kpoint.action.FAQLoggedInActionBean" title="Help">Help</s:link>
			<%} else { %>
				<s:link beanclass="org.webdev.kpoint.action.LoginActionBean" title="Consumer Login">Consumer Login</s:link>
				<span class="separator">|</span>
				<a href="${externalSettingsManager.depotPortalBaseUrl}/DepotHome.action" class="last" title="KinekPoint Partner Login">KP Partner Login</a>
			<%}%>
		</div>
		
		<% if (signedIn) { %>
			<div id="tellyourfriends">
				<a href="javascript:displaySocialSharing()"><img src="resource/images/tellyourfriends.jpg"/></a>
			</div>
		<%}%>
		
		<% if (signedIn && !showAllTabs) { %>
			<ul id="nav" class="clearfix loggedin">
				<li onmouseover="hoverLink(this)" onmouseout="unhoverLink(this)"><s:link beanclass="org.webdev.kpoint.action.DashboardActionBean" title="My Kinek">My Account</s:link></li>
			</ul>
		<%} else if (signedIn && showAllTabs) { %>
			<ul id="nav" class="clearfix loggedin">
				<li onmouseover="hoverLink(this)" onmouseout="unhoverLink(this)">
					<s:link beanclass="org.webdev.kpoint.action.DashboardActionBean" title="My Kinek">My Account
						<s:param name="showAllTabs" value="false"></s:param>
					</s:link>
				</li>

			</ul>
		<%} else { %>
			<ul id="nav" class="clearfix">
				<li onmouseover="hoverLink(this)" onmouseout="unhoverLink(this)"><s:link beanclass="org.webdev.kpoint.action.LoginActionBean" title="My Kinek">My Account</s:link></li>
			</ul>
		<%}%>
		

		<div id="toolBox">
		</div>
		<div id="bottomBar" class="<%out.print(signedInClass);%>"></div>
	</div>

	<s:layout-component name="body"></s:layout-component>  

	<!--[if IE 7]><link rel="stylesheet" href="resource/css/common/ie8.css" type="text/css" media="screen,projection" /><![endif]-->    
</div>


<div id="footer" class="clearfix <%if(home && signedIn) out.print("signedin");%>">

	<jsp:useBean id="externalSettingsManager" scope="application" class="org.webdev.kpoint.util.ExternalSettingsManagerHelper"/>

	<p class="copyright">${externalSettingsManager.externalKinekCopyright} </p>
	
	
	<p class="bottomnav">	
		<s:link beanclass="org.webdev.kpoint.action.ContactUsActionBean" title="Contact Us">Contact Us</s:link>		
		<span class="dotseparator"></span>
		<s:link beanclass="org.webdev.kpoint.action.AboutUsActionBean" title="About Us">About Us</s:link>
		<span class="dotseparator"></span>		
		<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Privacy Policy">Privacy Policy<s:param name="action">Privacy</s:param></s:link>				
		<span class="dotseparator"></span>
		<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Terms of Service">Terms of Service<s:param name="action">Terms</s:param></s:link>	   
   </p>	   
   
   <!-- begin olark code --><script type='text/javascript'>/*{literal}<![CDATA[*/window.olark||(function(i){var e=window,h=document,a=e.location.protocol=="https:"?"https:":"http:",g=i.name,b="load";(function(){e[g]=function(){(c.s=c.s||[]).push(arguments)};var c=e[g]._={},f=i.methods.length; while(f--){(function(j){e[g][j]=function(){e[g]("call",j,arguments)}})(i.methods[f])} c.l=i.loader;c.i=arguments.callee;c.f=setTimeout(function(){if(c.f){(new Image).src=a+"//"+c.l.replace(".js",".png")+"&"+escape(e.location.href)}c.f=null},20000);c.p={0:+new Date};c.P=function(j){c.p[j]=new Date-c.p[0]};function d(){c.P(b);e[g](b)}e.addEventListener?e.addEventListener(b,d,false):e.attachEvent("on"+b,d); (function(){function l(j){j="head";return["<",j,"></",j,"><",z,' onl'+'oad="var d=',B,";d.getElementsByTagName('head')[0].",y,"(d.",A,"('script')).",u,"='",a,"//",c.l,"'",'"',"></",z,">"].join("")}var z="body",s=h[z];if(!s){return setTimeout(arguments.callee,100)}c.P(1);var y="appendChild",A="createElement",u="src",r=h[A]("div"),G=r[y](h[A](g)),D=h[A]("iframe"),B="document",C="domain",q;r.style.display="none";s.insertBefore(r,s.firstChild).id=g;D.frameBorder="0";D.id=g+"-loader";if(/MSIE[ ]+6/.test(navigator.userAgent)){D.src="javascript:false"} D.allowTransparency="true";G[y](D);try{D.contentWindow[B].open()}catch(F){i[C]=h[C];q="javascript:var d="+B+".open();d.domain='"+h.domain+"';";D[u]=q+"void(0);"}try{var H=D.contentWindow[B];H.write(l());H.close()}catch(E){D[u]=q+'d.write("'+l().replace(/"/g,String.fromCharCode(92)+'"')+'");d.close();'}c.P(2)})()})()})({loader:(function(a){return "static.olark.com/jsclient/loader0.js?ts="+(a?a[1]:(+new Date))})(document.cookie.match(/olarkld=([0-9]+)/)),name:"olark",methods:["configure","extend","declare","identify"]});
	/* custom configuration goes here (www.olark.com/documentation) */
	olark.identify('2281-625-10-3955');/*]]>{/literal}*/</script>
	<!-- end olark code -->
		
</div>


<% if(signedIn){ %>	
	<!--  Social Sharing via Janrain -->
	<script type="text/javascript">
		var rpxJsHost = (("https:" == document.location.protocol) ? "https://" : "http://static.");
		document.write(unescape("%3Cscript src='" + rpxJsHost + "rpxnow.com/js/lib/rpx.js' type='text/javascript'%3E%3C/script%3E"));
	</script>
	<script type="text/javascript">
		RPXNOW.init({appId: '${externalSettingsManager.janrainApplicationId}', xdReceiver: '${externalSettingsManager.consumerPortalBaseUrl}/SocialSharing.action'});
	</script>
<% } %>
</body>
</html>
</s:layout-definition>