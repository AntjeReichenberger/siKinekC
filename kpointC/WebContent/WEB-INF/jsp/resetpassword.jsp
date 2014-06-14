<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">

		<div id="contentWrapper" class="clearfix">
		<div id="content" class="clearfix">
		<s:form beanclass="org.webdev.kpoint.action.ResetPasswordActionBean" class="small">
			<input class="hidden" type="password" name="autofillblock"/>
			<h1>Reset your Password</h1>
			<c:choose>
				<c:when test="${actionBean.key != null}">
					<p>Please provide a new password.</p>
					<s:hidden name="key"/>
					<stripes:errors />
					
					<table class="superplain form">
						<tr class="label">
							<td>
								<s:label for="passwd">Password</s:label>
							</td>
						</tr>
						<tr class="val">
							<td>
								<s:password name="passwd" id="passwd" />
							</td>
						</tr>
						<tr class="label">
							<td>
								<s:label for="confPassword">Confirm Password</s:label>
							</td>
						</tr>
						<tr class="val">
							<td>
								<s:password name="confPasswd" id="confPasswd" />
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="resetPassword" value="Reset" class="button" />
							</td>
						</tr>
					</table>

				</c:when>
				<c:otherwise>
					<p>Sorry, your password cannot be reset. The URL provided contains an invalid key. 
					Please try again or <s:link beanclass="org.webdev.kpoint.action.ForgotPasswordActionBean">have another key sent to you.</s:link></p>
				</c:otherwise>
			</c:choose>
		</s:form>
		</div>
		</div>

	</s:layout-component>
</s:layout-render>