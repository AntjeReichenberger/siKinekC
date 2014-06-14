<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard-changepassword.css" type="text/css" />
	
	<script type="text/javascript">
		jQuery(document).ready(function(){
			if ("${actionBean.displayMessage}" == "true")
				jQuery('#headerMsg').show();
			else
				jQuery('#headerMsg').hide();
		});
	</script>
	<br class="clear" />
	
	<div id="adbModule">
		<div id="pageTitle">
			<h1>Change Password</h1>
			<br />
		</div>
		
		<s:form beanclass="org.webdev.kpoint.action.PasswordActionBean">
			<input class="hidden" type="password" name="autofillblock"/>
			<s:errors/>
			<s:messages/>
			<fieldset>
				<p id="headerMsg">While your account was set up to sign in with Facebook/Google/Yahoo, you can also create a password for your account to log in normally using an email and password combination. This is entirely optional and can be created by filling in the following fields:</p>
				<table class="superplain form">
					<tr>
						<td class="label">
							<label for="newPassword">New Password</label>
						</td>
						<td>
							<s:password name="newPassword" id="newPassword" />
							<span class="required">*</span>
						</td>
					</tr>
					<tr>
						<td class="label">
							<label for="confirmPassword">Confirm Password</label>
						</td>
						<td>
							<s:password name="confirmPassword" id="confirmPassword" />
							<span class="required">*</span>
						</td>
					</tr>
					<tr>
						<td class="label"></td>
						<td class="submit">
							<s:submit name="savePassword" value="Save Password" class="button" />
						</td>
					</tr>
				</table>
			
				<!-- FORM CONTENT -->
				<div class="formContent">
					<p><small>Fields marked with <span class="required">*</span> are required.</small></p>
				</div>
			</fieldset>
		</s:form> 
	</div>

	</s:layout-component>
</s:layout-render>