<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Complete Your Profile</s:layout-component>
  	
  	<s:layout-component name="body">
	<script type="text/javascript">
		// <![CDATA[
	       jQuery(document).ready(function(){
		       if ("${actionBean.showAllTabs}" == "true") {
              		jQuery('#nav li:eq(4)').addClass('active');
		       }
              else {
             		jQuery('#nav li:eq(0)').addClass('active');
              }
	       });
		// ]]>
	</script>
	<script type="text/javascript">
		// <![CDATA[
		    jQuery(document).ready(function(){
				//Gets the index of a value in a select box
				function getIndex(selectbox, searchValue) {
					for (var i=0; i<selectbox.options.length; i++) {
						if (selectbox.options[i].value == searchValue)
							return i;
					}
					return 0;
				}
				jQuery(".streetaddress").watermark('House # and Street Name Only');
				checkForm();
			});


			function validateForm() {
				var valid = true;

				jQuery('input').each(function(index) {
					var elemId = this.id.replace('.', '\\.');
					var elemValid = true;
					if (elemId == 'user\\.zip') {
						var regex = '(^\\d{5}(-\\d{4})?$)|(^[ABCEGHJKLMNPRSTVXYabceghjklmnprstvxy]{1}\\d{1}[A-Za-z]{1} *\\d{1}[A-Za-z]{1}\\d{1}$)';
						if (this.value.match(regex) == null) {
							elemValid = false;
						}
					}

					if (!elemValid) {
						jQuery('#' + elemId + 'ErrorImg').show();
						jQuery('#' + elemId + 'ErrorTxt').show();
						valid = false;
					}
				});

				jQuery('select').each(function(index) {
					var elemId = this.id.replace('.', '\\.');
					var elemValid = true;
					
					if (this.value == "") {
							elemValid = false;
					}

					if (!elemValid) {
						jQuery('#' + elemId + 'ErrorImg').show();
						jQuery('#' + elemId + 'ErrorTxt').show();
						valid = false;
					}
				});

				if (!jQuery('#agreedToTOS').is(":checked")) {
					jQuery('#agreedToTOSErrorImg').show();
					jQuery('#agreedToTOSErrorTxt').show();
					valid = false;
				}

				if (!valid) {
					jQuery('#message').show();
					window.scrollTo(0, 0);
				}

				return valid;
			}
		// ]]>
	</script>
	<script type="text/javascript">
		function checkForm() {
			jQuery('input').each(function(index) {
				toggleCheck(this);
			});

			jQuery('select').each(function(index) {
				toggleCheck(this);
			});
		}

		function toggleCheck(element) {
			var elemId = element.id.replace(".", "\\.");
			
			if (elemId == 'user\\.zip') {
				var regex = '(^\\d{5}(-\\d{4})?$)|(^[ABCEGHJKLMNPRSTVXYabceghjklmnprstvxy]{1}\\d{1}[A-Za-z]{1} *\\d{1}[A-Za-z]{1}\\d{1}$)';
				if (element.value.match(regex) != null) {
					jQuery('#' + elemId + 'Check').show();
					jQuery('#' + elemId + 'ErrorImg').hide();
					jQuery('#' + elemId + 'ErrorTxt').hide();
				}
				else {
					jQuery('#' + elemId + 'Check').hide();
				}
			}
			else if (elemId == 'agreedToTOS') {
				if (element.checked) {
					jQuery('#agreedToTOSErrorImg').hide();
					jQuery('#agreedToTOSErrorTxt').hide();
				}
			}
			else if (elemId == 'user\\.firstName') {
				if (element.value.length > 1) {
					jQuery('#' + elemId + 'Check').show();
					jQuery('#' + elemId + 'ErrorImg').hide();
					jQuery('#' + elemId + 'ErrorTxt').hide();
					jQuery('#' + elemId + 'LengthErrorTxt').hide();
				}
				else if(element.value.length == 1) {
					jQuery('#' + elemId + 'Check').hide();
					jQuery('#' + elemId + 'ErrorImg').show();
					jQuery('#' + elemId + 'ErrorTxt').hide();
					jQuery('#' + elemId + 'LengthErrorTxt').show();
				}
				else {
					jQuery('#' + elemId + 'Check').hide();
				}
			}
			else if (elemId == 'user\\.lastName') {
				if (element.value.length > 1) {
					jQuery('#' + elemId + 'Check').show();
					jQuery('#' + elemId + 'ErrorImg').hide();
					jQuery('#' + elemId + 'ErrorTxt').hide();
					jQuery('#' + elemId + 'LengthErrorTxt').hide();
				}
				else if(element.value.length == 1) {
					jQuery('#' + elemId + 'Check').hide();
					jQuery('#' + elemId + 'ErrorImg').show();
					jQuery('#' + elemId + 'ErrorTxt').hide();
					jQuery('#' + elemId + 'LengthErrorTxt').show();
				}
				else {
					jQuery('#' + elemId + 'Check').hide();
				}
			}
			else {
				if (element.value == "") {
					jQuery('#' + elemId + 'Check').hide();
				}
				else {
					jQuery('#' + elemId + 'Check').show();
					jQuery('#' + elemId + 'ErrorImg').hide();
					jQuery('#' + elemId + 'ErrorTxt').hide();
				}
			}
		}
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
	/* ]]> */
	</script>
	<script type="text/javascript" src="https://www.googleadservices.com/pagead/conversion.js">
	</script>
	<noscript>
	<div style="display:inline;">
	<img height="1" width="1" style="border-style:none;" alt="" src="https://www.googleadservices.com/pagead/conversion/1023631268/?value=2&amp;label=BUn3CPavzwEQpL-N6AM&amp;guid=ON&amp;script=0"/>
	</div>
	</noscript> 

	<link rel="stylesheet" href="resource/css/pages/completeyourprofile.css" type="text/css" />
		
  	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
		
			<img src="resource/images/RegistrationProgressBarProfile.gif" id="profileprogress"></img>
			
			<s:form beanclass="org.webdev.kpoint.action.CompleteYourProfileActionBean" id="completeYourProfile">
				<fieldset>
					<s:errors />
					<s:messages />
					<div id="message" class="completeProfileError">
						<p>Please correct the errors below</p>
					</div>

					<table class="superplain form">
						<tr>
							<td class="label" colspan="2">
								<label class="title">Please fill in your home address</label>
								(
								<a href="#" id="why" onmouseover="openInfoDialog('popUpDiv', this)" onmouseout="closeInfoDialog('popUpDiv')">why do we need this?</a>
								)
							</td>
						</tr>
						<tr>
							<td class="label">
								<label for="user.firstName">First Name:</label>
							</td>
							<td class="val">
								<s:text name="user.firstName" id="user.firstName" onblur="checkForm();"/>
								<span class="required">*</span>
								<img src="resource/images/checkmark_green.png" class="greencheck" id="user.firstNameCheck"></img>
								<img src="resource/icons/icon_cancel.png" class="error" id="user.firstNameErrorImg"></img>
								<span class="error" id="user.firstNameErrorTxt">This is a required field, please complete to continue</span>
								<span class="error" id="user.firstNameLengthErrorTxt">First name must be two or more characters</span>
							</td>
						</tr>
						<tr>
							<td class="label">
								<label for="user.lastName">Last Name:</label>
							</td>
							<td class="val">
								<s:text name="user.lastName" id="user.lastName" onblur="checkForm();"/>
								<span class="required">*</span>
								<img src="resource/images/checkmark_green.png" class="greencheck" id="user.lastNameCheck"></img>
								<img src="resource/icons/icon_cancel.png" class="error" id="user.lastNameErrorImg"></img>
								<span class="error" id="user.lastNameErrorTxt">This is a required field, please complete to continue</span>
								<span class="error" id="user.lastNameLengthErrorTxt">Last name must be two or more characters</span>
							</td>
						</tr>
						<tr>
							<td class="label">
								<label for="user.address1">Street Address:</label>
							</td>
							<td class="val">	
								<s:text name="user.address1" id="user.address1" onblur="checkForm();" class="streetaddress"/>
								<span class="required">*</span>
								<img src="resource/images/checkmark_green.png" class="greencheck" id="user.address1Check"></img>
								<img src="resource/icons/icon_cancel.png" class="error" id="user.address1ErrorImg"></img>
								<span class="error" id="user.address1ErrorTxt">This is a required field, please complete to continue</span>
							</td>
						</tr>
						<tr>
							<td class="label">
								<label for="user.zip">Zip/Postal Code:</label>
							</td>
							<td class="val">	
								<s:text name="user.zip" id="user.zip" onblur="checkForm();"/>
								<span class="required">*</span>
								<img src="resource/images/checkmark_green.png" class="greencheck" id="user.zipCheck"></img>
								<img src="resource/icons/icon_cancel.png" class="error" id="user.zipErrorImg"></img>
								<span class="error" id="user.zipErrorTxt">This is a required field, please complete to continue</span>
							</td>
						</tr>
						<tr>
							<td colspan="2" id="agreedToTOSTD" class="label">
								<s:checkbox name="user.agreedToTOS" id="agreedToTOS" class="checkbox" onclick="checkForm();"/>
								<div class="regformterms">
									<p>I have read and agree to the
									<a href="${externalSettingsManager.termsAndConditionsUrl}" target="_blank">Terms</a> 
									</p>
									
									<p class="line2">Kinek does not sell, rent, trade, or give out any personal data.</p>
								</div>
								<img src="resource/icons/icon_cancel.png" class="error" id="agreedToTOSErrorImg"></img>
								<span class="error" id="agreedToTOSErrorTxt">This is a required field, please complete to continue</span>
							</td>
						</tr>
						<tr>
							<td class="label"></td>
							<td class="val submit">
								<s:submit name="storeUser" id="changeSubmit" value="Continue" class="button" onclick="return validateForm();"/>
							</td>
						</tr>
					</table>
					
					<div id="popUpDiv" title="Why do we need this?" class="hidden infoPopUpDiv">
						The following info is only used to verify ID when picking up a package and is never shared with any 3rd parties.
					</div>

				</fieldset>
			</s:form>
						
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->
   </s:layout-component>
</s:layout-render>
	