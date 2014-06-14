<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Suggest a Kinek Point</s:layout-component>

	<s:layout-component name="body">
	<link rel="stylesheet" href="resource/css/pages/suggestakp.css" type="text/css" media="screen,projection" />
	
	<script type="text/javascript">
		jQuery(document).ready(function(){
			jQuery('#nav li:eq(5)').addClass('active');

			jQuery('select#countryId').change(countryOnChange);
			countryOnChange();
			populateState();
		});
		
		function populateState()
		{
			if(document.getElementById("stateHidden").value != null)
			{
				SetCheckedValue(document.getElementById("prospect.state"), document.getElementById("stateHidden").value);
			}
		}

		function SetCheckedValue(radioObj, newValue) 
		{
			if(!radioObj)
				return;
			var radioLength = radioObj.length;
			if(radioLength == undefined) {
				radioObj.checked = (radioObj.value == newValue.toString());
				return;
			}
			for(var i = 0; i < radioLength; i++) {
				radioObj[i].selected = false;
				if(radioObj[i].value == newValue.toString()) {
					radioObj[i].selected = true;
				}
			}
		}

		//Country toggle
		function countryOnChange() {
			//Choose canada
			if (jQuery("select#countryId").val() == '2') {
				//List all provinces
				jQuery('select#prospect\\.state').html("");
				jQuery('select#prospect\\.state').append("<option selected='selected'>Choose a Province/Region</option>");
				jQuery('select#prospect\\.state').append("<option value='57'>Alberta</option>");
				jQuery('select#prospect\\.state').append("<option value='56'>British Columbia</option>");
				jQuery('select#prospect\\.state').append("<option value='59'>Manitoba</option>");
				jQuery('select#prospect\\.state').append("<option value='61'>Newfoundland and Labrador</option>");
				jQuery('select#prospect\\.state').append("<option value='62'>New Brunswick</option>");
				jQuery('select#prospect\\.state').append("<option value='54'>Northwest Territories</option>");
				jQuery('select#prospect\\.state').append("<option value='63'>Nova Scotia</option>");
				jQuery('select#prospect\\.state').append("<option value='52'>Nunavut</option>");
				jQuery('select#prospect\\.state').append("<option value='55'>Ontario</option>");
				jQuery('select#prospect\\.state').append("<option value='64'>Prince Edward Island</option>");
				jQuery('select#prospect\\.state').append("<option value='53'>Quebec</option>");
				jQuery('select#prospect\\.state').append("<option value='58'>Saskatchewan</option>");
				jQuery('select#prospect\\.state').append("<option value='60'>Yukon</option>");
				//Change the mask of the ZIP/Postal Code input field
				jQuery('#prospect\\.zip').unmask();
				jQuery('#prospect\\.zip').mask('a9a 9a9');
				
			//Choose USA
			} else if (jQuery("select#countryId").val() == '1') {
				//List all states
				jQuery('select#prospect\\.state').html("");
				jQuery('select#prospect\\.state').append("<option selected='selected'>Choose a State</option>");
				jQuery('select#prospect\\.state').append("<option value='1'>Alabama</option>");
				jQuery('select#prospect\\.state').append("<option value='2'>Alaska</option>");
				jQuery('select#prospect\\.state').append("<option value='3'>Arizona</option>");
				jQuery('select#prospect\\.state').append("<option value='4'>Arkansas</option>");
				jQuery('select#prospect\\.state').append("<option value='5'>California</option>");
				jQuery('select#prospect\\.state').append("<option value='6'>Colorado</option>");
				jQuery('select#prospect\\.state').append("<option value='7'>Connecticut</option>");
				jQuery('select#prospect\\.state').append("<option value='8'>Delaware</option>");
				jQuery('select#prospect\\.state').append("<option value='9'>District of Columbia</option>");
				jQuery('select#prospect\\.state').append("<option value='10'>Florida</option>");
				jQuery('select#prospect\\.state').append("<option value='11'>Georgia</option>");
				jQuery('select#prospect\\.state').append("<option value='12'>Hawaii</option>");
				jQuery('select#prospect\\.state').append("<option value='13'>Idaho</option>");
				jQuery('select#prospect\\.state').append("<option value='14'>Illinois</option>");
				jQuery('select#prospect\\.state').append("<option value='15'>Indiana</option>");
				jQuery('select#prospect\\.state').append("<option value='16'>Iowa</option>");
				jQuery('select#prospect\\.state').append("<option value='17'>Kansas</option>");
				jQuery('select#prospect\\.state').append("<option value='18'>Kentucky</option>");
				jQuery('select#prospect\\.state').append("<option value='19'>Louisiana</option>");
				jQuery('select#prospect\\.state').append("<option value='20'>Maine</option>");
				jQuery('select#prospect\\.state').append("<option value='21'>Maryland</option>");
				jQuery('select#prospect\\.state').append("<option value='22'>Massachusetts</option>");
				jQuery('select#prospect\\.state').append("<option value='23'>Michigan</option>");
				jQuery('select#prospect\\.state').append("<option value='24'>Minnesota</option>");
				jQuery('select#prospect\\.state').append("<option value='25'>Mississippi</option>");
				jQuery('select#prospect\\.state').append("<option value='26'>Missouri</option>");
				jQuery('select#prospect\\.state').append("<option value='27'>Montana</option>");
				jQuery('select#prospect\\.state').append("<option value='28'>Nebraska</option>");
				jQuery('select#prospect\\.state').append("<option value='29'>Nevada</option>");
				jQuery('select#prospect\\.state').append("<option value='30'>New Hampshire</option>");
				jQuery('select#prospect\\.state').append("<option value='31'>New Jersey</option>");
				jQuery('select#prospect\\.state').append("<option value='32'>New Mexico</option>");
				jQuery('select#prospect\\.state').append("<option value='33'>New York</option>");
				jQuery('select#prospect\\.state').append("<option value='34'>North Carolina</option>");
				jQuery('select#prospect\\.state').append("<option value='35'>North Dakota</option>");
				jQuery('select#prospect\\.state').append("<option value='36'>Ohio</option>");
				jQuery('select#prospect\\.state').append("<option value='37'>Oklahoma</option>");
				jQuery('select#prospect\\.state').append("<option value='38'>Oregon</option>");
				jQuery('select#prospect\\.state').append("<option value='39'>Pennsylvania</option>");
				jQuery('select#prospect\\.state').append("<option value='40'>Rhode Island</option>");
				jQuery('select#prospect\\.state').append("<option value='41'>South Carolina</option>");
				jQuery('select#prospect\\.state').append("<option value='42'>South Dakota</option>");
				jQuery('select#prospect\\.state').append("<option value='43'>Tennessee</option>");
				jQuery('select#prospect\\.state').append("<option value='44'>Texas</option>");
				jQuery('select#prospect\\.state').append("<option value='45'>Utah</option>");
				jQuery('select#prospect\\.state').append("<option value='46'>Vermont</option>");
				jQuery('select#prospect\\.state').append("<option value='47'>Virginia</option>");
				jQuery('select#prospect\\.state').append("<option value='48'>Washington</option>");
				jQuery('select#prospect\\.state').append("<option value='49'>West Virginia</option>");
				jQuery('select#prospect\\.state').append("<option value='50'>Wisconsin</option>");
				jQuery('select#prospect\\.state').append("<option value='51'>Wyoming</option>");
				//Change the mask of the ZIP/Postal Code input field
				jQuery('#prospect\\.zip').unmask();
				jQuery('#prospect\\.zip').mask('99999');
			}
		}
	
		function updateStateHidden()
		{
			document.getElementById('stateHidden').value = document.getElementById('prospect.state').value;
		}

		function validateProspect()
		{
			jQuery('#message').hide();
			
			var message = "<div id=\"message\" class=\"error\">";
			var isValid = true;
			
			if (document.getElementById('prospect.city').value == "")
			{
				isValid = false;
				message += "<p>City is a required field</p>";
			}

			if (document.getElementById('prospect.state').selectedIndex ==0)
			{
				isValid = false;
				message += "<p>State is a required field</p>";
			}
			
			if (document.getElementById('prospect.zip').value == "")
			{
				isValid = false;
				message += "<p>Zip is a required field</p>";
			}
			message += "</div>";

			if(!isValid)
			{
				jQuery('#errors').html(message);
			}
			else
			{
				jQuery('#errors').html("");
			}

			return isValid;
		}
	</script>
	
		<!-- CONTENTWRAPPER -->
		<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
		
			<h1>Suggest a KinekPoint</h1>
			
			<s:link name="returnToSearch" beanclass="org.webdev.kpoint.action.FindDepotActionBean" class="buttonLink" id="returnLink" >Return to Search</s:link>
		
			<div id="errors"></div>
			<stripes:errors/>
			<stripes:messages/>
		
			<s:form name="suggestAKP" id="suggestAKP" action="/SuggestAKinekPoint.action" focus="">
				<div id="prospects">						
					<div id="prospectContents">
						<table class="superplain form">
							<tr>
								<td>
									<label for="countryId">Country:</label>
									<br />
									<s:select name="countryId" id="countryId">
										<s:options-collection collection="${actionBean.countries}" label="name" value="countryId" />
								  	</s:select>
								</td>
								<td>
									<s:label for="prospect.state">State:</s:label>
									<br />
									<s:select name="prospect.state" id="prospect.state" onchange="updateStateHidden();">
									</s:select>	
									<s:hidden name="stateHidden" id="stateHidden" class="hidden"></s:hidden>
								</td>
							</tr>
							<tr>
								<td>
									<s:label for="prospect.city">City:</s:label>
									<br />
									<s:text id="prospect.city" name="prospect.city"></s:text>
								</td>
								<td>
									<s:label for="prospect.zip">Zip / Postal Code:</s:label>
									<br />
									<s:text id="prospect.zip" name="prospect.zip"></s:text>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<s:label for="userEmailAddress">Email address:</s:label>
									<br />
									<s:text id="prospect.notifyEmailAddress" name="prospect.notifyEmailAddress"></s:text>
									<br />
									(Enter your email to be notified when KP is available)
								</td>
							</tr>
							<tr>
								<td>
									<div align="left">
										<s:submit name="createProspect" id="createProspect" value="Submit" class="depotSearchbutton" onclick="return validateProspect();" />
									</div>
								</td>
								<td></td>
							</tr>
						</table>
					</div>								
				</div>
			</s:form>
		
		</div>
		</div>
	
	</s:layout-component>
</s:layout-render>