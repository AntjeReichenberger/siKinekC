<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<!-- WRAPPER -->
<div id="wrapper">

	<!-- HEADER -->
	<div id="header">
		
		<!-- TOPNAV -->
		<div id="topnav" class="clearfix">
			<c:choose>
				<c:when test="${activeUser==null}">
					<s:link beanclass="org.webdev.kpoint.action.LoginActionBean" title="Login">Login</s:link>
				</c:when>
				<c:otherwise>
					<s:link beanclass="org.webdev.kpoint.action.LoginActionBean" event="logout" title="Logout">Logout</s:link>
				</c:otherwise>
			</c:choose>
			<span class="separator">|</span> 
			
			<c:if test="${actionBean.showHelpButton}">
				<a href="ShowPage.action?action=help" title="Help">Help</a>
				<span class="separator">|</span>
			</c:if> 
			
			<s:link beanclass="org.webdev.kpoint.action.ContactActionBean" title="Contact Us">Contact Us</s:link>
			<span class="separator">|</span> 
			
			<s:link beanclass="org.webdev.kpoint.action.AboutActionBean" title="Contact Us">About Us</s:link>			
			
			
		</div>
		<!-- /TOPNAV -->
		
		<!-- LOGO -->
		<img src="resource/images/logo_top.png" width="326" height="50" alt="Kinek Home" id="logo" />
		<!-- /LOGO -->
	
	</div>
	<!-- /HEADER -->