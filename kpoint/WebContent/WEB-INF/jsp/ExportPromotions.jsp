<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">
	<script type="text/javascript">
	// <![CDATA[
		jQuery(document).ready(function(){
			
			//Enable date pickers
			jQuery('.date-pick').datePicker({
				startDate: '01/01/2009'
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
		});
	
	// ]]>
	</script>
	<!-- STEPS -->

		<!-- CONTENTWRAPPER -->
		<div id="contentWrapper" class="clearfix">
		
			<!-- CONTENT -->
			<div id="content" class="wide clearfix">
		
				<!-- INTERFACEWRAPPER -->
				<div id="interfaceWrapper">
		
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
					
						<!-- STEP TITLE -->
						<h1>Export Promotional Campaigns</h1>
						<!-- /STEP TITLE -->
						<stripes:errors/>
						<stripes:messages/>
					
						<!-- KinekPoint Change Report -->
						<s:form beanclass="org.webdev.kpoint.action.ExportPromotionsActionBean">
							<table class="superplain" style="width: 750px">
								<tbody>
									<tr>
										<td>
											<s:label for="promotionCode">Promotion Code:</s:label><br/>
											<s:text name="promotionCode" id="promotionCode" style="width: 300px;"/>
											<small class="validation">* What is the Promotion Code?</small>
										</td>
										<td>
											<s:label for="constraint">Region Constraints:</s:label><br />
											<s:select name="stateId" id="stateId" style="width: 300px;">
												<s:options-collection collection="${actionBean.states}" label="name" value="stateId" id="stateId" />
											</s:select>
											<small class="validation">* Which region is the search constrained to?</small>
										</td>
									</tr>
									<tr>
										<td>
											<s:label for="redemption">Redemption Status:</s:label><br/>
											<s:select name="redemptionId" id="redemptionId" style="width: 300px;">
												<s:option value="" label="Select a Status" />
												<s:options-collection collection="${actionBean.redemptions}" label="name" value="redemptionId" />
											</s:select>
											<small class="validation">* Which redemption status is the search constrained to?</small>
										</td>
										<td>
											<s:label for="association">Association Constraints:</s:label><br/>
											<s:select name="associationId" id="associationId" style="width: 300px;">
												<s:option value="" label="Select an Association" />
												<s:options-collection collection="${actionBean.associations}" label="name" value="associationId" />
											</s:select>
											<small class="validation">* Which association is the search constrained to?</small>
										</td>
									</tr>
									<tr>
										<td>
											<label for="date_start">Availibility Date Range: </label><br />
											<s:text name="startDate" id="date_start" class="date-pick" />
											<s:text name="endDate" id="date_end" class="date-pick" /><br /><br />
											<span style="margin-left:40px;">(Start Date)</span>
											<span style="margin-left:130px;">(End Date)</span>
										</td>
										<td>
											<s:label for="depot">KinekPoints:</s:label><br/>
											<s:select name="depotId" id="depotId" style="width: 300px;">
												<s:option value="" label="Select a KinekPoint" />
												<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
											</s:select>
											<small class="validation">* Which KinekPoint is the search constrained to?</small>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<s:link beanclass="org.webdev.kpoint.action.ExportPromotionsActionBean" style="vertical-align:middle;">Reset</s:link>
											<span class="separator">|</span>
											<s:submit name="search" value="Search" class="button" />
											<s:submit name="export" value="Export" class="button" />
										</td>
									</tr>
								</tbody>
							</table>

							<h1>Matching Promotional Campaigns</h1>
						
							<!-- FORM CONTENT -->
							<div class="formContent">
									<table id="PromosForExport" class="sortable">
										<thead>
											<tr>
												<th>Code</th>
												<th>Title</th>
												<th># Available</th>
												<th>Start Date</th>
												<th>End Date</th>
												<th>Consumer Credit</th>
												<th>Depot Credit</th>
												<th>Region</th>
												<th>Association</th>
												<th>KinekPoint</th>
											</tr>
										</thead>
										<tbody>
										<c:choose>
											<c:when test="${actionBean.goodResults}">
												<c:forEach items="${actionBean.promosForExport}" var="promos" varStatus="loop">
													<tr>
														<td>${promos.code}</td>
														<td>${promos.title}</td>
														<td><fmt:formatNumber value="${promos.availabilityCount}" type="number"></fmt:formatNumber></td>
														<td><fmt:formatDate value="${promos.startDate}" type="date" pattern="MMMMM dd, yyyy"/></td> 
														<td><fmt:formatDate value="${promos.endDate}" type="date" pattern="MMMMM dd, yyyy"/></td>
														<c:choose>
															<c:when test="${promos.consumerCreditCalcType.id==2}">
																<td align="right"><fmt:formatNumber value="${promos.consumerCreditAmount/100.0}" type="percent"></fmt:formatNumber></td>
															</c:when>
															<c:otherwise>
																<td align="right"><fmt:formatNumber value="${promos.consumerCreditAmount}" type="currency"></fmt:formatNumber></td>
															</c:otherwise>
														</c:choose>
														
														<c:choose>
															<c:when test="${promos.depotCreditCalcType.id==2}">
																<td align="right"><fmt:formatNumber value="${promos.depotCreditAmount/100.0}" type="percent"></fmt:formatNumber></td>
															</c:when>
															<c:otherwise>
																<td align="right"><fmt:formatNumber value="${promos.depotCreditAmount}" type="currency"></fmt:formatNumber></td>
															</c:otherwise>
														</c:choose>
														
														<td>${promos.state.name}</td>
														<td>${promos.association.name}</td>
														<td>${promos.depot.name}</td>
														
													</tr>
													</c:forEach>
												</c:when>
												<c:when test="${actionBean.noResults}">
													<tr>
														<td colspan="10">
															We are unable to find any Promotions that match the criteria you have provided.
														</td>
													</tr>
												</c:when>
												<c:when test="${actionBean.tooManyResults}">
													<tr>
														<td colspan="10">
															The criteria you have provided matches too many Promotions (&gt; 100,000).<br/>
															Please refine your criteria information to narrow the search
														</td>
													</tr>
												</c:when>
											</c:choose>
										</tbody>
									</table>
								
							</div>
							<!-- /FORM CONTENT -->
						
						</s:form>
						<!-- /KinekPoint Change Report -->
					
					</div>
					<!-- /STEPCONTAINER -->
					
				</div>
				<!-- /INTERFACE -->
				
			</div>
			<!-- /CONTENT -->
			
		</div>
		<!-- /CONTENTWRAPPER -->
		
		
		<script type="text/javascript" src="resource/js/jquery.tablesorter.min.js"></script>
		<script type="text/javascript">
			jQuery(document).ready(function() { jQuery("#PromosForExport").tablesorter(); });
		</script>
		
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
	</s:layout-component>
</s:layout-render>