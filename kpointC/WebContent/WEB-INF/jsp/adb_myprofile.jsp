<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">	
	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard-myprofile.css" type="text/css" />

	<script type="text/javascript">
	// <![CDATA[
		jQuery(document).ready(function(){
			jQuery('#adpnav li:eq(2)').addClass('active');
		});
	// ]]>
	</script>
	
	<br class="clear"/>
	
	<div id="adbModule" style="width:875px !important">
		<div id="pageTitle">
			<h1>Edit Profile Information</h1>
			<br />
		</div>
		
		<s:form beanclass="org.webdev.kpoint.action.MyProfileActionBean" id="myProfile">			
			<fieldset class="userform">
				<s:errors />
				<s:messages />
				
				<table class="superplain form">

					<tr>
						<td class="label">
							<label for="user.firstName">First Name:</label>
						</td>
						<td class="val">
							<s:text name="user.firstName" id="user.firstName" readonly="true" />
						</td>
						<td class="labelright">
							<label for="user.phone">Primary Contact Number:</label>
						</td>
						<td class="valright" style="width: 275px;">
							<s:text name="user.phone" id="phone"/>
							<img src="resource/images/help_icon.png" onmouseover="openInfoDialog('primaryPhoneinfoPopUpDiv', this)" onmouseout="closeInfoDialog('primaryPhoneinfoPopUpDiv')"></img>
						</td>
					</tr>
					<tr>
						<td class="label">
							<label for="user.lastName">Last Name:</label>
						</td>
						<td class="val">
							<s:text name="user.lastName" id="user.lastName" readonly="true" />
						</td>
						<td class="labelright">
							<label for="user.email">Email:</label>
						</td>
						<td class="valright">	
							<s:text name="user.email" id="user.email"/>
							<span class="required">*</span>
						</td>
					</tr>
					<tr>
						<td class="label">
							<label for="user.address1">Address Line 1:</label>
						</td>
						<td class="val">	
							<s:text name="user.address1" id="user.address1" readonly="false" />
						</td>
						<td class="labelright"></td>
						<td class="valright" rowspan="2">
							<s:submit name="storeUser" id="changeSubmit" value="Save Changes" class="button" />
						</td>
					</tr>
					<tr>
						<td class="label">
							<label for="user.address2" class="wide">Address Line 2:</label>
						</td>
						<td class="val">
							<s:text name="user.address2" id="user.address2" readonly="false" />
						</td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td class="label">
							<label for="user.city">City:</label>
						</td>
						<td class="val">	
							<s:text name="user.city" id="user.city" readonly="false" />
						</td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td class="label">
							<label for="country">Country:</label>
						</td>
						<td class="val">
							<s:select name="user.state.country" id="country" class="select" disabled="false" >
								<s:options-collection collection="${actionBean.countries}" label="name" value="countryId" />
						  	</s:select>
						  	<s:hidden name="user.state.country.countryId"></s:hidden>
						</td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td class="label">
							<label for="user.state">State/Province/Region:</label>
						</td>
						<td class="val">
							<s:select name="user.state" id="user.state" class="select" disabled="false" ></s:select>
						  	
						  	<s:hidden name="user.state.stateId"></s:hidden>
						</td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td class="label">
							<label for="user.zip">Zip/Postal Code:</label>
						</td>
						<td class="val">	
							<s:text name="user.zip" id="user.zip" readonly="false" />
						</td>
						<td colspan="2"></td>
					</tr>
					
				</table>

			<!-- FORM CONTENT -->
			<div class="formContent">			    
				<p><small>Fields marked with <span class="required">*</span> are required.</small></p>
			</div>
		
			<div id="cellPhoneInfoPopUpDiv" title="Cell/Mobile Number" class="hidden infoPopUpDiv">
				Enter to receive text message delivery notifications.
			</div>
		
			<div id="primaryPhoneinfoPopUpDiv" title="Primary Contact Number" class="hidden infoPopUpDiv">
				Enter your phone number so KinekPoints can contact you if any problems arise.
			</div>
		
			<div id="profileInfoPopUpDiv" title="Change Your Address" class="hidden infoPopUpDiv">
				When you pick up your packages from a KinekPoint, we ask to see 2 pieces of ID to compare against your Kinek profile. This is part of the process we use to ensure the security of your packages. However, as part of this process, we require that your address information cannot be edited after it has been entered.
			</div>
			</fieldset>
		</s:form>
	</div>
	
	<script type="text/javascript">
	// <![CDATA[
		   jQuery(document).ready(function(){
			 	//Set input mask for phone field
				jQuery('#phone').mask('(999) 999-9999');
	
				jQuery('select#user\\.state').append("<option selected='selected'>Choose a State from the supplied list</option>");
	
				//Country toggle
				function countryOnChange() {
					jQuery('select#user\\.state').html("");
					
					//Choose canada
					if (jQuery("select#country").val() == '2') {
						jQuery('select#user\\.state').html("");
						//List all provinces
						jQuery('select#user\\.state').append("<option selected='selected'>Choose a Province/Region from the supplied list</option>");
						jQuery('select#user\\.state').append("<option value='57'>Alberta</option>");
						jQuery('select#user\\.state').append("<option value='56'>British Columbia</option>");
						jQuery('select#user\\.state').append("<option value='59'>Manitoba</option>");
						jQuery('select#user\\.state').append("<option value='61'>Newfoundland and Labrador</option>");
						jQuery('select#user\\.state').append("<option value='62'>New Brunswick</option>");
						jQuery('select#user\\.state').append("<option value='54'>Northwest Territories</option>");
						jQuery('select#user\\.state').append("<option value='63'>Nova Scotia</option>");
						jQuery('select#user\\.state').append("<option value='52'>Nunavut</option>");
						jQuery('select#user\\.state').append("<option value='55'>Ontario</option>");
						jQuery('select#user\\.state').append("<option value='64'>Prince Edward Island</option>");
						jQuery('select#user\\.state').append("<option value='53'>Quebec</option>");
						jQuery('select#user\\.state').append("<option value='58'>Saskatchewan</option>");
						jQuery('select#user\\.state').append("<option value='60'>Yukon</option>");
						//Change the mask of the ZIP/Postal Code input field
						jQuery('#user\\.zip').unmask();
						jQuery('#user\\.zip').mask('a9a 9a9');
					
					//Choose USA
					} else if (jQuery("select#country").val() == '1') {
						jQuery('select#user\\.state').html("");
						//List all states
						jQuery('select#user\\.state').append("<option selected='selected'>Choose a State from the supplied list</option>");
						jQuery('select#user\\.state').append("<option value='1'>Alabama</option>");
						jQuery('select#user\\.state').append("<option value='2'>Alaska</option>");
						jQuery('select#user\\.state').append("<option value='3'>Arizona</option>");
						jQuery('select#user\\.state').append("<option value='4'>Arkansas</option>");
						jQuery('select#user\\.state').append("<option value='5'>California</option>");
						jQuery('select#user\\.state').append("<option value='6'>Colorado</option>");
						jQuery('select#user\\.state').append("<option value='7'>Connecticut</option>");
						jQuery('select#user\\.state').append("<option value='8'>Delaware</option>");
						jQuery('select#user\\.state').append("<option value='9'>District of Columbia</option>");
						jQuery('select#user\\.state').append("<option value='10'>Florida</option>");
						jQuery('select#user\\.state').append("<option value='11'>Georgia</option>");
						jQuery('select#user\\.state').append("<option value='12'>Hawaii</option>");
						jQuery('select#user\\.state').append("<option value='13'>Idaho</option>");
						jQuery('select#user\\.state').append("<option value='14'>Illinois</option>");
						jQuery('select#user\\.state').append("<option value='15'>Indiana</option>");
						jQuery('select#user\\.state').append("<option value='16'>Iowa</option>");
						jQuery('select#user\\.state').append("<option value='17'>Kansas</option>");
						jQuery('select#user\\.state').append("<option value='18'>Kentucky</option>");
						jQuery('select#user\\.state').append("<option value='19'>Louisiana</option>");
						jQuery('select#user\\.state').append("<option value='20'>Maine</option>");
						jQuery('select#user\\.state').append("<option value='21'>Maryland</option>");
						jQuery('select#user\\.state').append("<option value='22'>Massachusetts</option>");
						jQuery('select#user\\.state').append("<option value='23'>Michigan</option>");
						jQuery('select#user\\.state').append("<option value='24'>Minnesota</option>");
						jQuery('select#user\\.state').append("<option value='25'>Mississippi</option>");
						jQuery('select#user\\.state').append("<option value='26'>Missouri</option>");
						jQuery('select#user\\.state').append("<option value='27'>Montana</option>");
						jQuery('select#user\\.state').append("<option value='28'>Nebraska</option>");
						jQuery('select#user\\.state').append("<option value='29'>Nevada</option>");
						jQuery('select#user\\.state').append("<option value='30'>New Hampshire</option>");
						jQuery('select#user\\.state').append("<option value='31'>New Jersey</option>");
						jQuery('select#user\\.state').append("<option value='32'>New Mexico</option>");
						jQuery('select#user\\.state').append("<option value='33'>New York</option>");
						jQuery('select#user\\.state').append("<option value='34'>North Carolina</option>");
						jQuery('select#user\\.state').append("<option value='35'>North Dakota</option>");
						jQuery('select#user\\.state').append("<option value='36'>Ohio</option>");
						jQuery('select#user\\.state').append("<option value='37'>Oklahoma</option>");
						jQuery('select#user\\.state').append("<option value='38'>Oregon</option>");
						jQuery('select#user\\.state').append("<option value='39'>Pennsylvania</option>");
						jQuery('select#user\\.state').append("<option value='40'>Rhode Island</option>");
						jQuery('select#user\\.state').append("<option value='41'>South Carolina</option>");
						jQuery('select#user\\.state').append("<option value='42'>South Dakota</option>");
						jQuery('select#user\\.state').append("<option value='43'>Tennessee</option>");
						jQuery('select#user\\.state').append("<option value='44'>Texas</option>");
						jQuery('select#user\\.state').append("<option value='45'>Utah</option>");
						jQuery('select#user\\.state').append("<option value='46'>Vermont</option>");
						jQuery('select#user\\.state').append("<option value='47'>Virginia</option>");
						jQuery('select#user\\.state').append("<option value='48'>Washington</option>");
						jQuery('select#user\\.state').append("<option value='49'>West Virginia</option>");
						jQuery('select#user\\.state').append("<option value='50'>Wisconsin</option>");
						jQuery('select#user\\.state').append("<option value='51'>Wyoming</option>");
						//Change the mask of the ZIP/Postal Code input field
						jQuery('#user\\.zip').unmask();
						jQuery('#user\\.zip').mask('99999');
					}
				}
	
				//Gets the index of a value in a select box
				function getIndex(selectbox, searchValue) {
					for (var i=0; i<selectbox.options.length; i++) {
						if (selectbox.options[i].value == searchValue)
							return i;
					}
					return 0;
				}
	
				jQuery('select#country').change(countryOnChange);
				jQuery('select#country').val("${actionBean.user.state.country.countryId}");
				countryOnChange();
				
				jQuery('select#user\\.state').val("${actionBean.user.state.stateId}");
		   });
	// ]]>
	</script>

	</s:layout-component>
</s:layout-render>