<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/depot.jsp">
	<s:layout-component name="contents">
		<script type="text/javascript">
	// <![CDATA[
		jQuery(document).ready(function(){

			//Set input mask for phone field
			jQuery('#depot\\.phone').mask('(999) 999-9999');
			jQuery('select#stateId').append("<option selected='selected'>Choose a State from the supplied list</option>");

			//Country toggle
			function countryOnChange() {
				jQuery('select#stateId').html("");
				jQuery('select#stateId').append("<option value='' selected='selected'>Please choose a country before selecting a state</option>");
				//Choose canada
				if (jQuery("select#countryId").val() == '2') {
					jQuery('select#stateId').html("");
					//List all provinces
					jQuery('select#stateId').append("<option value='' selected='selected'>Choose a Province/Region from the supplied list</option>");
					jQuery('select#stateId').append("<option value='57'>Alberta</option>");
					jQuery('select#stateId').append("<option value='56'>British Columbia</option>");
					jQuery('select#stateId').append("<option value='59'>Manitoba</option>");
					jQuery('select#stateId').append("<option value='61'>Newfoundland and Labrador</option>");
					jQuery('select#stateId').append("<option value='62'>New Brunswick</option>");
					jQuery('select#stateId').append("<option value='54'>Northwest Territories</option>");
					jQuery('select#stateId').append("<option value='63'>Nova Scotia</option>");
					jQuery('select#stateId').append("<option value='52'>Nunavut</option>");
					jQuery('select#stateId').append("<option value='55'>Ontario</option>");
					jQuery('select#stateId').append("<option value='64'>Prince Edward Island</option>");
					jQuery('select#stateId').append("<option value='53'>Quebec</option>");
					jQuery('select#stateId').append("<option value='58'>Saskatchewan</option>");
					jQuery('select#stateId').append("<option value='60'>Yukon</option>");
				
				//Choose USA
				} else if (jQuery("select#countryId").val() == '1') {
					jQuery('select#stateId').html("");
					//List all states
					jQuery('select#stateId').append("<option value='' selected='selected'>Choose a State from the supplied list</option>");
					jQuery('select#stateId').append("<option value='1'>Alabama</option>");
					jQuery('select#stateId').append("<option value='2'>Alaska</option>");
					jQuery('select#stateId').append("<option value='3'>Arizona</option>");
					jQuery('select#stateId').append("<option value='4'>Arkansas</option>");
					jQuery('select#stateId').append("<option value='5'>California</option>");
					jQuery('select#stateId').append("<option value='6'>Colorado</option>");
					jQuery('select#stateId').append("<option value='7'>Connecticut</option>");
					jQuery('select#stateId').append("<option value='8'>Delaware</option>");
					jQuery('select#stateId').append("<option value='9'>District of Columbia</option>");
					jQuery('select#stateId').append("<option value='10'>Florida</option>");
					jQuery('select#stateId').append("<option value='11'>Georgia</option>");
					jQuery('select#stateId').append("<option value='12'>Hawaii</option>");
					jQuery('select#stateId').append("<option value='13'>Idaho</option>");
					jQuery('select#stateId').append("<option value='14'>Illinois</option>");
					jQuery('select#stateId').append("<option value='15'>Indiana</option>");
					jQuery('select#stateId').append("<option value='16'>Iowa</option>");
					jQuery('select#stateId').append("<option value='17'>Kansas</option>");
					jQuery('select#stateId').append("<option value='18'>Kentucky</option>");
					jQuery('select#stateId').append("<option value='19'>Louisiana</option>");
					jQuery('select#stateId').append("<option value='20'>Maine</option>");
					jQuery('select#stateId').append("<option value='21'>Maryland</option>");
					jQuery('select#stateId').append("<option value='22'>Massachusetts</option>");
					jQuery('select#stateId').append("<option value='23'>Michigan</option>");
					jQuery('select#stateId').append("<option value='24'>Minnesota</option>");
					jQuery('select#stateId').append("<option value='25'>Mississippi</option>");
					jQuery('select#stateId').append("<option value='26'>Missouri</option>");
					jQuery('select#stateId').append("<option value='27'>Montana</option>");
					jQuery('select#stateId').append("<option value='28'>Nebraska</option>");
					jQuery('select#stateId').append("<option value='29'>Nevada</option>");
					jQuery('select#stateId').append("<option value='30'>New Hampshire</option>");
					jQuery('select#stateId').append("<option value='31'>New Jersey</option>");
					jQuery('select#stateId').append("<option value='32'>New Mexico</option>");
					jQuery('select#stateId').append("<option value='33'>New York</option>");
					jQuery('select#stateId').append("<option value='34'>North Carolina</option>");
					jQuery('select#stateId').append("<option value='35'>North Dakota</option>");
					jQuery('select#stateId').append("<option value='36'>Ohio</option>");
					jQuery('select#stateId').append("<option value='37'>Oklahoma</option>");
					jQuery('select#stateId').append("<option value='38'>Oregon</option>");
					jQuery('select#stateId').append("<option value='39'>Pennsylvania</option>");
					jQuery('select#stateId').append("<option value='40'>Rhode Island</option>");
					jQuery('select#stateId').append("<option value='41'>South Carolina</option>");
					jQuery('select#stateId').append("<option value='42'>South Dakota</option>");
					jQuery('select#stateId').append("<option value='43'>Tennessee</option>");
					jQuery('select#stateId').append("<option value='44'>Texas</option>");
					jQuery('select#stateId').append("<option value='45'>Utah</option>");
					jQuery('select#stateId').append("<option value='46'>Vermont</option>");
					jQuery('select#stateId').append("<option value='47'>Virginia</option>");
					jQuery('select#stateId').append("<option value='48'>Washington</option>");
					jQuery('select#stateId').append("<option value='49'>West Virginia</option>");
					jQuery('select#stateId').append("<option value='50'>Wisconsin</option>");
					jQuery('select#stateId').append("<option value='51'>Wyoming</option>");
				}
			}
			jQuery('select#countryId').change(countryOnChange);
			jQuery('select#countryId').val("${actionBean.countryId}");
			countryOnChange();
			jQuery('select#stateId').val("${actionBean.stateId}");
		});
	// ]]>
	</script>
	
	
		<!-- STEPS -->
		<ul id="steps">
			<c:choose>
			<c:when test="${actionBean.createDepot}">
				<li class="active"><a href="#step1" title="Edit KinekPoint Information"><span>Step 1:</span> KinekPoint Information</a></li>
				<li><a href="#step2" title="Edit Services &amp; Features"><span>Step 2:</span> Services &amp; Features</a></li>
				<li><a href="#step3" title="Edit Pricing"><span>Step 3:</span> Pricing</a></li>
				<li><a href="#step4" title="Edit Hours of Operation"><span>Step 4:</span> Hours of Operation</a></li>
			</c:when>
			<c:otherwise>
				<li class="active">
					<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean">
						<s:param name="depotId" value="${actionBean.depotId}" />
						KinekPoint Information
					</s:link>
				</li>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean" event="viewFeatures">
						<s:param name="depotId" value="${actionBean.depotId}" />
						Services &amp; Features
					</s:link>
				</li>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean" event="viewPrices">
						<s:param name="depotId" value="${actionBean.depotId}" />
						Pricing
					</s:link>
				</li>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean" event="viewHours">
						<s:param name="depotId" value="${actionBean.depotId}" />
						Hours of Operation
					</s:link>
				</li>
			</c:otherwise>
			</c:choose>
		</ul>
		<!-- /STEPS -->
		
		<!-- STEPCONTAINER -->
		<div id="stepContainer">
			
			<!-- STEP TITLE -->
			<h1>
				<c:choose>
					<c:when test="${actionBean.createDepot}">
						<span>1</span> 
					</c:when>
				</c:choose>
				Edit KinekPoint Information
			</h1>
			<!-- /STEP TITLE -->
			
			<stripes:errors/>
			
			<stripes:messages/>
			
			<!-- EDITDEPOTPROFILE STEP1 -->
			<s:form beanclass="org.webdev.kpoint.action.ManageDepotActionBean">
				<fieldset>
					<s:hidden name="depotId"/>
					<s:hidden name="depot.depotId"/>
					<s:hidden name="action" />	
					<s:hidden name="createDepot" />					
					<c:choose>
						<c:when test="${actionBean.activeUser.adminAccessCheck}">
							<!-- BLOCK -->
							<ol class="clearfix">
								<li class="half">
									<s:label for="statusId">Status <span class="required">*</span></s:label><br />
									<s:select name="statusId" id="statusId">
										<s:options-collection collection="${actionBean.statuses}" label="name" value="id" />
									</s:select>
								</li>		
							</ol>
							<!-- /BLOCK -->
						</c:when>
					</c:choose>
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<s:label for="depot.name">Business Name</s:label><span class="required">*</span><br />
							<s:text name="depot.name" id="depot.name" />
							<small class="validation">* What is the name of your business?</small>
						</li>									
					</ol>
					<!-- /BLOCK -->
		
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<s:label for="depot.address1">Address Line 1</s:label><span class="required">*</span><br />
							<s:text name="depot.address1" id="depot.address1"/>
							<small class="validation">* Where are you located?</small>
						</li>
						<li class="half">
							<s:label for="depot.address2">Address Line 2</s:label> <br />
							<s:text name="depot.address2" id="depot.address2" />
							<small class="validation">* Where are you located?</small>
						</li>									
					</ol>
					<!-- /BLOCK -->
		
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<s:label for="depot.city">City</s:label><span class="required">*</span><br />
							<s:text name="depot.city" id="depot.city" />
							<small class="validation">* Which city are you located in?</small>
						</li>									
					</ol>
					<!-- /BLOCK -->
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<s:label for="countryId">Country</s:label><span class="required">*</span><br />
							<s:select name="countryId" id="countryId">
								<s:option value="" label="Please select a country" />
								<s:options-collection collection="${actionBean.countries}" label="name" value="countryId" />
							</s:select>
							<small class="validation">* Which country are you located in?</small>
						</li>
						<li class="half">
							<s:label for="stateId">State/Province/Region</s:label><span class="required">*</span><br />
							<s:select name="stateId" id="stateId">
								<s:options-collection collection="${actionBean.states}" label="name" value="stateId" id="stateId" />
							</s:select>
							<small class="validation">* Which state/province/region are you location in?</small>
						</li>
						<li class="half">
							<s:label for="depot.zip">Postal/Zip Code</s:label><span class="required">*</span><br />
							<s:text name="depot.zip" id="depot.zip" />
							<small class="validation">* Enter your zip/postal code.</small>
						</li>									
					</ol>
					<!-- /BLOCK -->
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<s:label for="regionId">Region</s:label><br />
							<s:select name="regionId" id="regionId">
								<s:option value="" label="Please select a region" />
								<s:options-collection collection="${actionBean.regions}" label="name" value="regionId" /> 
							</s:select>
							<small class="validation">* Which region are your KinekPoint located in?</small>
						</li>
						<li class="half">
							<s:label for="organizationId">Organization</s:label><br />
							<s:select name="organizationId" id="organizationId">
								<s:option value="" label="Please select a organization" />
								<s:options-collection collection="${actionBean.organizations}" label="name" value="organizationId" id="organizationId" /> 
							</s:select>
							<small class="validation">* Which organization are you associated?</small>
						</li>								
					</ol>
					<!-- /BLOCK -->					
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<s:label for="depot.phone">Phone Number</s:label><span class="required">*</span><br />
							<s:text name="depot.phone" id="depot.phone" />
							<small class="validation">* Enter your phone number.</small>
						</li>
						<li class="half">
							<s:label for="depot.email">Email Address</s:label><span class="required">*</span><br />
							<s:text name="depot.email" id="depot.email" />
							<small class="validation">* Enter your email address.</small>
						</li>															
					</ol>
					<!-- /BLOCK -->																
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="rightalign half">
							<s:link beanclass="org.webdev.kpoint.action.ViewKinekPointActionBean">Cancel</s:link>
							<span class="separator">|</span>
							<s:submit name="saveContact" value="Save Changes" class="button" />
						</li>									
					</ol>
					<!-- /BLOCK -->
		
					<!-- FORM CONTENT -->
					<div class="formContent">
						<p><small>Fields marked with <span class="required">*</span> are required to proceed.</small></p>
					</div>
					<!-- /FORM CONTENT -->
				</fieldset>
			</s:form>  
			<!-- /EDITDEPOTPROFILE STEP1 -->
		</div>
		<!-- /STEPCONTAINER -->
	</s:layout-component>
</s:layout-render>