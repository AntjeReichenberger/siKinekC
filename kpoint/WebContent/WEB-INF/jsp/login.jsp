<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
  
	<div id="contentWrapper" class="clearfix">


		<!-- CONTENT -->
		<div id="content" class="wide clearfix">  
  
<s:form beanclass="org.webdev.kpoint.action.LoginActionBean" id="kpLogin" focus="">

	<h1>Welcome!</h1>
	<p>Please enter your username &amp; password to begin.</p>
	
	<s:errors/>	
	<s:messages/>
	
	<ol>
		<li>
			<s:label for="username">Username</s:label><br />
			<s:text name="username" id="username" />
		</li>
		<li>
			<s:label for="passwd">Password</s:label><br />
			<s:password name="passwd" id="passwd" />
		</li>
		<li>
			<s:submit name="login" value="Login" class="button" /><span class="separator">|</span> <s:link beanclass="org.webdev.kpoint.action.ForgotPasswordActionBean">Forgot your password</s:link>
		</li>
	</ol>
</s:form>

		</div>
		<!-- /CONTENT -->
		
	</div>
	<!-- /CONTENTWRAPPER -->
	
	<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>	

  </s:layout-component>
</s:layout-render>