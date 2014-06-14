<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page import="org.webdev.kpoint.bl.manager.ExternalSettingsManager" %>
<%@ taglib prefix="janrain" uri="http://janrain4j.googlecode.com/tags" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Sign up with Kinek</s:layout-component>

	<s:layout-component name="body">
  	<link rel="stylesheet" href="resource/css/pages/signup.css" type="text/css" media="screen,projection" />
  		
 	<script type="text/javascript">
		var RecaptchaOptions = {
			theme : 'clean'
	 	};
	</script>

	<!-- Google Code for Register Conversion Page -->
	<script type="text/javascript">
		/* <![CDATA[ */
		var google_conversion_id = 1023631268;
		var google_conversion_language = "en";
		var google_conversion_format = "3";
		var google_conversion_color = "ff0000";
		var google_conversion_label = "BUn3CPavzwEQpL-N6AM";
		var google_conversion_value = 0;
		if (2) {
		  google_conversion_value = 2;
		}

		jQuery(document).ready(function(){
			jQuery('#user\\.email').keydown(function() {
				checkCompletion();
			});

			jQuery('#user\\.password').keydown(function() {
				checkCompletion();
			});

			jQuery('#confirmPassword').keydown(function() {
				checkCompletion();
			});

			jQuery('#recaptcha_response_field').keydown(function() {
				checkCompletion();
			});

			checkCompletion();
		});

		function checkCompletion() {
			var formComplete = false;
			
			if (jQuery('#user\\.email').val() != "" && 
					jQuery('#user\\.password').val() != "" && 
					jQuery('#confirmPassword').val() != "" && 
					jQuery('#recaptcha_response_field').val()) {
				formComplete = true;
			}

			if (formComplete) {
				jQuery('input#createUser').removeAttr('disabled');
				jQuery('input#createUser').removeClass('buttonDisabled');
			} else {
				jQuery('input#createUser').attr('disabled', 'disabled');
				jQuery('input#createUser').addClass('buttonDisabled');
			}
		}
		/* ]]> */
	</script>
	<script type="text/javascript" src="https://www.googleadservices.com/pagead/conversion.js"></script>
	
	<noscript>
		<div style="display:inline;">
			<img height="1" width="1" style="border-style:none;" alt=""
			src="https://www.googleadservices.com/pagead/conversion/1023631268/?value=2&amp;label=BUn3CPavzwEQpL-N6AM&amp;guid=ON&amp;script=0"/>
		</div>
	</noscript>


	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<div id="content">

			<div id="findDeliveryLocation">
				<h1>Registration. It's free to Sign up!</h1>
				<br/><br/>
			</div>
			
			<s:errors />
			<s:messages />
			
			<s:form name="signup" id="signup" action="/Register.action">
				<div id="leftForm">
					<div class="signupTitle">
						Create a new Kinek Account
					</div>
					<table class="superplain form">
						<tr>
							<td class="labelcol">
								<s:label for="user.email">Email </s:label>
							</td>
							<td class="valcol">
								<s:text name="user.email" id="user.email" />
							</td>	
						</tr>
						<tr>
							<td class="labelcol">
								<s:label for="user.password">Password </s:label>
							</td>
							<td class="valcol">
								<s:password name="user.password" id="user.password"/>
							</td>	
						</tr>
						<tr>
							<td class="labelcol">
								<s:label for="confirmPassword">Confirm Password </s:label>
							</td>
							<td class="valcol">
								<s:password name="confirmPassword" id="confirmPassword"/>
							</td>	
						</tr>
						<tr>
							<td colspan="2" class="captcha">
								<script type="text/javascript"
								   src="https://www.google.com/recaptcha/api/challenge?k=6LfkL78SAAAAAMjAtD27luqsGvK2GH1C48GyMm0k">
								</script>
							</td>
						</tr>
						<tr>
							<td class="buttonwrapper center" colspan="2">
								<s:submit name="createUser" id="createUser" value="Create My Account" class="button" />
							</td>
						</tr>
						<tr>
							<td colspan="2" class="center">
								<s:label for="signup">Already have an account? </s:label>
								<s:link beanclass="org.webdev.kpoint.action.LoginActionBean" title="SignIn">Sign in here</s:link>
							</td>	
						</tr>
					</table>
				</div>
				
				<div id="orWrapper">
					<span>OR</span>
				</div>
				
				<div id="rightForm">
					<div class="signupTitle">
						Sign in using one of the following
					</div>
					<table class="superplain form">
						<tr>
							<td><janrain:signInEmbedded width="350" flags="hide_sign_in_with" tokenUrl="${actionBean.janrainTokenUrl}"/></td>
						</tr>
						<tr>
							<td class="footerRow">
								There is no need to create a new user name and password if you have an account with any of these social sites.  Just select an icon to connect your profile.
							</td>
						</tr>
					</table>
				</div>
			</s:form>
		</div>
	</div>
	<!-- /CONTENTWRAPPER -->
	</s:layout-component>
</s:layout-render>