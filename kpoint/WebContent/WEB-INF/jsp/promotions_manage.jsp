<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/promotions.jsp">
	<s:layout-component name="contents">

	<script type="text/javascript">
	// <![CDATA[
		jQuery(document).ready(function(){

			jQuery('#promotionDescription').maxLength(199);
			
			//Enable date pickers
			jQuery('.date-pick').datePicker({
				startDate: 0,
				minDate: 0
			});

			//Country toggle
					jQuery('select#stateId').html("");
					//List all provinces
					jQuery('select#stateId').append("<option value='0' selected='selected'>Select a State / Province</option>");
			
				//Choose USA
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
					//Choose canada
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

			jQuery('select#stateId').val("${actionBean.stateId}");

			function reset() {

			}
		});
	
	// ]]>
	</script>
	<!-- STEPS -->
	
		<h1>Manage Promotional Campaigns</h1>
		
		<stripes:errors/>
		<stripes:messages/>

		<s:form beanclass="org.webdev.kpoint.action.ManagePromotionsActionBean">
				<fieldset>
					<!-- BLOCK -->
					<table class="superplain" style="width:625px;margin-top:0px;margin-bottom:0px;">
					<tr>
						<td><s:label for="promotionCode">Promotion Code:</s:label><span class="required">*</span></td>
						<td colspan="2"><s:text name="promotionCode" id="promotionCode" maxlength="9" style="width: 200px;"/></td>
					</tr>
					<tr>
						<td><s:label for="promotionTitle">Title:</s:label><span class="required">*</span></td>
						<td colspan="2"><s:text name="promotionTitle" id="promotionTitle" maxlength="29" style="width: 200px;"/></td>
					</tr>
					<tr>
						<td><label for="promotionDescription">Description:</label><span class="required">*</span></td>
						<td  colspan="2"><s:textarea name="promotionDescription" id="promotionDescription" rows="3" style="width: 360px;"></s:textarea></td>
					</tr>
					<tr>
						<td><s:label for="quantity"># Available:</s:label><span class="required">*</span></td>
						<td colspan="2"><s:text name="quantity" id="quantity" style="width: 136px;"/></td>
					</tr>
					<tr>
						<td><label for="date_start">Availability:</label><span class="required">*</span></td>
						<td>
							<s:text name="startDate" id="date_start" class="date-pick" /><br /><br />
						  	<span style="margin-left:40px;">(Start Date)</span>
						</td>
						<td>
							<s:text name="endDate" id="date_end" class="date-pick" /><br /><br />
							<span style="margin-left:40px;">(End Date)</span>
						</td>
					</tr>
					<tr>
						<td><label>Consumer Credit Amount:<span class="required">*</span></label></td>
						<td>
							<s:label for="consumerCreditType.dollar" class="radio">
								<s:radio name="consumerCreditType" value="dollar" class="radio" id="consumerCreditType.dollar"  checked="checked"/> Dollar Amount
							</s:label>
							<br />
							<s:label for="consumerCreditType.percentage" class="radio">
								<s:radio name="consumerCreditType" value="percentage" class="radio" id="consumerCreditType.percentage"/> Percentage
							</s:label>
						</td>
						<td><s:text name="consumerCredit" id="consumerCredit" style="width: 100px;"/></td>
					</tr>
					<tr>
						<td><label>KinekPoint Credit Amount:<span class="required">*</span></label></td>
						<td>
							<s:label for="depotCreditType.dollar" class="radio">
								<s:radio name="depotCreditType" value="dollar" class="radio" id="depotCreditType.dollar"  checked="checked"/> Dollar Amount
							</s:label>
							<br />
							<s:label for="depotCreditType.percentage" class="radio">
								<s:radio name="depotCreditType" value="percentage" class="radio" id="depotCreditType.percentage"/> Percentage
							</s:label>
						</td>
						<td><s:text name="depotCredit" id="depotCredit" style="width: 100px;"/></td>
					</tr>
					<tr>
						<td><s:label for="constraint">Region<br />Constraints:</s:label></td>
						<td colspan="2">
							<s:select name="stateId" id="stateId" style="width: 360px;">
								<s:options-collection collection="${actionBean.states}" label="name" value="stateId" id="stateId" />
							</s:select>
						</td>
					</tr>
					<tr>
						<td><s:label for="association">Association<br />Constraints:</s:label></td>
						<td  colspan="2">
							<s:select name="associationId" id="associationId" style="width: 360px;">
								<s:option value="" label="Select an Association" />
								<s:options-collection collection="${actionBean.associations}" label="name" value="associationId" />
							</s:select>
							<small class="validation">* Which association is the promotion constrained to?</small>
						</td>
					</tr>
					<tr>
						<td><s:label for="depot">KinekPoints:</s:label></td>
						<td colspan="2">
							<s:select name="depotId" id="depotId" style="width: 360px;">
								<s:option value="" label="Select a KinekPoint" />
								<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
							</s:select>
						</td>
					</tr>
				</table>
				
				<table class="superplain" style="width:625px;margin-top:0px;">
					<tr>
					<td>
					<ol>
						<li class="rightalign">
							<s:link beanclass="org.webdev.kpoint.action.ViewKinekPointActionBean">Cancel</s:link>
							<span class="separator">|</span>
							<s:button name="resetPromotion" value="Reset" class="button" onclick="reset();"/>
							<s:submit name="submitPromotion" value="Submit" class="button" />
						</li>
					</ol>
					</td>
					</tr>
				</table>
					

					<!-- FORM CONTENT -->
					<div class="formContent">
						<p><small>Fields marked with <span class="required">*</span> are required to proceed.</small></p>
					</div>
					<!-- /FORM CONTENT -->
				</fieldset>
			</s:form>

	</s:layout-component>
 </s:layout-render>
