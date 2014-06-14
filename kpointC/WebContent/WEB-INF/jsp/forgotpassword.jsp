<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">
		<div id="contentWrapper" class="clearfix">
		<div id="content" class="clearfix">
		<s:form beanclass="org.webdev.kpoint.action.ForgotPasswordActionBean" class="small">
			<fieldset>
				<h1>Forgot My Password</h1>
				<p>Please enter your email address. An email will be sent to
				you with a link to the password reset page.  If you continue
				to encounter any problems please contact Kinek support at
				<a href="mailto:kineksupport@kinek.com">kineksupport@kinek.com</a></p>
				<s:messages />
				<s:errors />
				
				<table class="superplain form">
					<tr class="label">
						<td>
							<s:label for="username">Email</s:label><br />
						</td>
					</tr>
					<tr class="val">
						<td>
							<s:text name="username" id="username" />
						</td>
					</tr>
					<tr>
						<td>
							<s:submit name="send" value="Send" class="button" />
						</td>
					</tr>
				</table>

			</fieldset>
		</s:form>
		</div>
		</div>

	</s:layout-component>
</s:layout-render>