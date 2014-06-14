<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">

		<div id="contentWrapper" class="clearfix"><!-- CONTENT -->
		<div id="content" class="wide clearfix">
		
		<s:form beanclass="org.webdev.kpoint.action.ForgotPasswordActionBean" id="resetPassword">
			<h1>Forgot My Password</h1>
			<p>Please provide enter your user name, an email will be sent to you with a link to the password reset page.</p>
			<s:messages/>
			<s:errors/>
			<ol>
				<li>
					<s:label for="username">Username</s:label><br />
					<s:text name="username" id="username" />
				</li>
				<li>
					<s:submit name="send" value="Send" class="button" />
				</li>
			</ol>
		</s:form></div>
		<!-- /CONTENT --></div>
		<!-- /CONTENTWRAPPER -->

		<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>

	</s:layout-component>
</s:layout-render>