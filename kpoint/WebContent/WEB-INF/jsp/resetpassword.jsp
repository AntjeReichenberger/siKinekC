<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">

		<div id="contentWrapper" class="clearfix"><!-- CONTENT -->
		<div id="content" class="wide clearfix">
		<s:form beanclass="org.webdev.kpoint.action.ResetPasswordActionBean" id="resetPassword">
			<h1>Reset your Password</h1>
			<c:choose>
				<c:when test="${actionBean.key != null}">
					<p>Please provide a new password.</p>
					<s:hidden name="key"/>
					<stripes:errors />
					<ol>
						<li>
							<s:label for="passwd">Password</s:label><br />
							<s:password name="passwd" id="passwd" />
						</li>
						<li>
							<s:label for="confPassword">Confirm Password</s:label><br />
							<s:password name="confPasswd" id="confPasswd" />
						</li>
						<li>
							<s:submit name="resetPassword" value="Reset" class="button" />
						</li>
					</ol>
				</c:when>
				<c:otherwise>
					<p>Sorry, your password cannot be reset. The URL provided contains an invalid key. 
					Please try again or <s:link beanclass="org.webdev.kpoint.action.ForgotPasswordActionBean">have another key sent to you.</s:link></p>
				</c:otherwise>
			</c:choose>
		</s:form>
		</div>
		<!-- /CONTENT --></div>
		<!-- /CONTENTWRAPPER -->

		<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>

	</s:layout-component>
</s:layout-render>