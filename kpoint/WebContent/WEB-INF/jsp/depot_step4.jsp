<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/depot.jsp">
	<s:layout-component name="contents">

		<script src="resource/js/nicEdit.js" type="text/javascript"></script>
		
		<style type="text/css">
			textarea {
			  width: 100%;
			  min-width: 100%;
			  max-width: 100%;
			}
			
			p {
				margin: 0px;
				padding: 0px;
			}
		</style>
		
		<script type="text/javascript">
			bkLib.onDomLoaded(function() {
				new nicEditor({buttonList : ['bold', 'italic', 'underline'], iconsPath : 'resource/images/nicEditorIcons.gif'}).panelInstance('depot.operatingHours.hoursInfo');
			});
		</script>

		<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery('#depot\\.operatingHours\\.mondayStart').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.mondayEnd').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.tuesdayStart').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.tuesdayEnd').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.wednesdayStart').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.wednesdayEnd').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.thursdayStart').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.thursdayEnd').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.fridayStart').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.fridayEnd').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.saturdayStart').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.saturdayEnd').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.sundayStart').ptTimeSelect();
			jQuery('#depot\\.operatingHours\\.sundayEnd').ptTimeSelect();

			checkSelected();
		});

		function checkSelected() {
			var table = document.getElementById("operatingHoursTable");
			if (!table) {
				return;
			}
			
			var rows = table.rows;
			var checkStr = "";
			
			for(var i = 1; i < rows.length; i++) {
				if (rows[i].cells[3].childNodes[1].tagName == "INPUT" && (rows[i].cells[3].childNodes[1].checked==true)) {
					rows[i].cells[1].childNodes[1].value = "";
					rows[i].cells[2].childNodes[1].value = "";
				}
			}
		}
		</script>
			
		<!-- STEPS -->
		<ul id="steps">
			<c:choose>
			<c:when test="${actionBean.createDepot}">
				<li><a href="#step1" title="Edit KinekPoint Information"><span>Step 1:</span> KinekPoint Information</a></li>
				<li><a href="#step2" title="Edit Services &amp; Features"><span>Step 2:</span> Services &amp; Features</a></li>
				<li><a href="#step3" title="Edit Pricing"><span>Step 3:</span> Pricing</a></li>
				<li class="active"><a href="#step4" title="Edit Hours of Operation"><span>Step 4:</span> Hours of Operation</a></li>
			</c:when>
			<c:otherwise>
				<li>
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
				<li class="active">
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

			<h1 style="float:right;">KinekPoint: ${actionBean.depotName}</h1>
		
			<!-- STEP TITLE -->
			<h1>
				<c:choose>
					<c:when test="${actionBean.createDepot}">
						<span>4</span> 
					</c:when>
				</c:choose>
				Edit Hours of Operation
			</h1>
			<!-- /STEP TITLE -->
			
			<stripes:errors />
			<stripes:messages/>
			
			<!-- EDITDEPOTPROFILE STEP1 -->
			<s:form beanclass="org.webdev.kpoint.action.ManageDepotActionBean">
				<fieldset>								
					<s:hidden name="depotId"/>
					<s:hidden name="createDepot"/>
					<s:hidden name="depot.depotId"/>

					<div class="formContent">
						<table id="operatingHoursTable">
							<thead>
								<tr>
									<th>Day</th>
									<th>Opening Time</th>
									<th>Closing Time</th>
									<th>Closed</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><strong>Monday</strong></td>
									<td>
										<s:text name="depot.operatingHours.mondayStart" id="depot.operatingHours.mondayStart" class="time monday" formatType="time" formatPattern="h:mm a"></s:text>
									</td>
									<td>
										<s:text name="depot.operatingHours.mondayEnd" id="depot.operatingHours.mondayEnd" class="time monday" formatType="time" formatPattern="h:mm a"></s:text>										
									</td>
									<td>
										<s:checkbox class="checkbox" name="depot.operatingHours.closedMonday" id="depot.operatingHours.closedMonday" onclick="checkSelected()"/>
									</td>
								</tr>
								<tr>
									<td><strong>Tuesday</strong></td>
									<td>
										<s:text name="depot.operatingHours.tuesdayStart" id="depot.operatingHours.tuesdayStart" class="time tuesday"  formatType="time" formatPattern="h:mm a"></s:text>
									</td>
									<td>
										<s:text name="depot.operatingHours.tuesdayEnd" id="depot.operatingHours.tuesdayEnd" class="time tuesday" formatType="time" formatPattern="h:mm a"></s:text>										
									</td>
									<td>
										<s:checkbox class="checkbox" name="depot.operatingHours.closedTuesday" id="depot.operatingHours.closedTuesday" onclick="checkSelected()"/>
									</td>
								</tr>
								<tr>
									<td><strong>Wednesday</strong></td>
									<td>
										<s:text name="depot.operatingHours.wednesdayStart" id="depot.operatingHours.wednesdayStart" class="time wednesday"  formatType="time" formatPattern="h:mm a"></s:text>
									</td>
									<td>
										<s:text name="depot.operatingHours.wednesdayEnd"  id="depot.operatingHours.wednesdayEnd" class="time wednesday" formatType="time" formatPattern="h:mm a"></s:text>										
									</td>
									<td>
										<s:checkbox class="checkbox" name="depot.operatingHours.closedWednesday" id="depot.operatingHours.closedWednesday" onclick="checkSelected()"/>
									</td>
								</tr>
								<tr>
									<td><strong>Thursday</strong></td>
									<td>
										<s:text name="depot.operatingHours.thursdayStart" id="depot.operatingHours.thursdayStart" class="time thursday" formatType="time" formatPattern="h:mm a"></s:text>
									</td>
									<td>
										<s:text name="depot.operatingHours.thursdayEnd" id="depot.operatingHours.thursdayEnd" class="time thursday" formatType="time" formatPattern="h:mm a"></s:text>										
									</td>
									<td>
										<s:checkbox class="checkbox" name="depot.operatingHours.closedThursday" id="depot.operatingHours.closedThursday" onclick="checkSelected()"/>
									</td>
								</tr>
								<tr>
									<td><strong>Friday</strong></td>
									<td>
										<s:text name="depot.operatingHours.fridayStart" id="depot.operatingHours.fridayStart" class="time friday" formatType="time" formatPattern="h:mm a"></s:text>
									</td>
									<td>
										<s:text name="depot.operatingHours.fridayEnd" id="depot.operatingHours.fridayEnd" class="time friday" formatType="time" formatPattern="h:mm a"></s:text>										
									</td>
									<td>
										<s:checkbox class="checkbox" name="depot.operatingHours.closedFriday" id="depot.operatingHours.closedFriday" onclick="checkSelected()"/>
									</td>
								</tr>
								<tr>
									<td><strong>Saturday</strong></td>
									<td>
										<s:text name="depot.operatingHours.saturdayStart" id="depot.operatingHours.saturdayStart" class="time saturday" formatType="time" formatPattern="h:mm a"></s:text>
									</td>
									<td>
										<s:text name="depot.operatingHours.saturdayEnd" id="depot.operatingHours.saturdayEnd" class="time saturday" formatType="time" formatPattern="h:mm a"></s:text>										
									</td>
									<td>
										<s:checkbox class="checkbox" name="depot.operatingHours.closedSaturday" id="depot.operatingHours.closedSaturday" onclick="checkSelected()"/>
									</td>
								</tr>
								<tr>
									<td><strong>Sunday</strong></td>
									<td>
										<s:text name="depot.operatingHours.sundayStart" id="depot.operatingHours.sundayStart" class="time sunday" formatType="time" formatPattern="h:mm a"></s:text>
									</td>
									<td>
										<s:text name="depot.operatingHours.sundayEnd" id="depot.operatingHours.sundayEnd" class="time sunday" formatType="time" formatPattern="h:mm a"></s:text>										
									</td>
									<td>
										<s:checkbox class="checkbox" name="depot.operatingHours.closedSunday" id="depot.operatingHours.closedSunday" onclick="checkSelected()"/>
									</td>
								</tr>							
								
							</tbody>
						</table>
						
						<!-- BLOCK -->
						<ol class="clearfix">
							<li>
								<s:label for="depot.operatingHours.hoursInfo">Additional Info (Hours of operation):</s:label><br />
								<s:textarea name="depot.operatingHours.hoursInfo" id="depot.operatingHours.hoursInfo" rows="13" cols="14"></s:textarea>
								<small class="validation">* Enter additional details about your hours of operation (maximum 1000 characters)</small>
							</li>																	
						</ol>
						<!-- /BLOCK -->						
						
					</div>
					<!-- /FORM CONTENT -->
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="rightalign half">
							<s:link beanclass="org.webdev.kpoint.action.ViewKinekPointActionBean">Cancel</s:link>
							<span class="separator">|</span>
							<s:submit class="button" name="saveHours" value="Save Changes" onclick="checkSelected()"/>
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