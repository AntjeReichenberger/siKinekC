<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<%@ taglib prefix="janrain" uri="http://janrain4j.googlecode.com/tags" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Login to Kinek</s:layout-component>
	
	<s:layout-component name="body">
  	<link rel="stylesheet" href="resource/css/pages/login.css" type="text/css" media="screen,projection" />
  	
	<script type="text/javascript">
	// <![CDATA[
	       jQuery(document).ready(function(){
	       		jQuery('#nav li:eq(4)').addClass('active');
	       		
	       });
	// ]]>
	
	</script>

	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<div id="content">

			<div id="signInHeader">
				<h1>Sign In</h1>
				<br/><br/>
			</div>

			<s:errors />
			<s:messages />
			
			<s:form name="login" id="login" action="/Login.action">
				
				<div id="leftForm">
					<div class="loginTitle">
						Sign in using your existing Kinek Account
					</div>
					<table class="superplain form">
						<tr>
							<td class="labelcol">
								<s:label for="username">Email </s:label>
							</td>
							<td class="valcol">
								<s:text name="username" />
							</td>	
						</tr>
						<tr>
							<td>
								<s:label for="passwd">Password </s:label>
							</td>
							<td>
								<s:password name="passwd" /><br />
								<s:link beanclass="org.webdev.kpoint.action.ForgotPasswordActionBean" title="Forgot Password">Forgot Password</s:link>
							</td>	
						</tr>
						<tr>
							<td></td>
							<td class="buttonwrapper">
								<s:submit name="login" value="Login" class="button" />
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td class="signup">
								<s:label for="signup">Don't have an account? </s:label>
								<s:link beanclass="org.webdev.kpoint.action.SignupActionBean" title="Sign Up">Sign Up Now</s:link>
							</td>	
						</tr>
					</table>
				</div>
				
				<div id="orWrapper">
					<span>OR</span>
				</div>
				
				<div id="rightForm">
					<div class="loginTitle">
						Sign in using one of the following
					</div>
					<table class="superplain form">
						<tr>
							<td><janrain:signInEmbedded width="360" flags="hide_sign_in_with"/></td>
						</tr>
					</table>
				</div>
				
			</s:form>
		</div>
	</div>
	<!-- /CONTENTWRAPPER -->
	</s:layout-component>
</s:layout-render>