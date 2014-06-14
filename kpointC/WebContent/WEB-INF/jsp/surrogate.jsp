<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
		<div class="clearfix"></div>
		<div id="adbModule">	
			<script type="text/javascript" src="resource/js/surrogate.js"></script>
			<!-- Add Surrogate -->
			<s:errors />
			<s:messages />
			
			<div>
				Instructional text
			</div>
			
			<s:link href="${actionBean.pdf1583Form}">Download 1583 Form</s:link>			
			<div>
			<s:form name="viewSurrogateFrm" action="/Surrogate.action">
				<table class="superplain form">
					<tr>
						<td>Upload 1583 Form</td><td><s:file name="upload1583Pdf"/></td>
					</tr>				
					<tr>
						<td>Kinek User ID #1</td><td> <s:file name="uploadKPUserId1"/></td>
					</tr>
					<tr>
						<td>Kinek User ID #2</td><td> <s:file name="uploadKPUserId2"/></td>
					</tr>
					<tr>
						<td>Surrogate User ID #1</td><td> <s:file name="uploadSurrogateUserId1"/></td>
					</tr>
					<tr>
						<td>Surrogate User ID #2</td><td> <s:file name="uploadSurrogateUserId2"/></td>
					</tr>
					<tr>
						<td>First Name</td><td><s:text name="firstName"></s:text> </td>
					</tr>
					<tr>
						<td>Last Name</td><td><s:text name="lastName"></s:text> </td>
					</tr>
					<tr>
						<td>Street Address</td><td><s:text name="streetName"></s:text> </td>
					</tr>
					<tr>
						<td>Country</td>
						<td>
							<s:select name="countryId" id="countryId" onchange="getAjaxStates()">
								<s:option value="" label="Please select a country" />
								<s:options-collection collection="${actionBean.countries}" label="name" value="countryId" /> 
							</s:select>
						</td>
					</tr>					
					<tr>
						<td>Province/State</td>
						<td>
							<s:select name="stateId" id="stateId">
								<s:option value="" label="Please select a state/province" />
								<s:options-collection collection="${actionBean.states}" label="name" value="stateId" /> 
							</s:select>
						</td>
					</tr>															
				</table>
				<s:submit name="createSurrogate" value="Save Surrogate" class="button"></s:submit>				
				<s:submit name="cancelSurrogate" value="Cancel" class="button"></s:submit>
				<s:hidden name="action"></s:hidden>
			</s:form>
			</div>				
			<!-- //Add Surrogate -->
			<script type="text/javascript">
				function getAjaxStates(){
					var countryId = jQuery("#countryId").val();
					getStates(countryId);
				}
			</script>	
		</div>	
	</s:layout-component>
</s:layout-render>