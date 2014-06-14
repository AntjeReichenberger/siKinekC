<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
  	
  	<script type="text/javascript" src="resource/js/time.js"></script>
  	
  	<script type="text/javascript">
		// <![CDATA[
		       jQuery(document).ready(function(){
					jQuery('#adpnav li:eq(3)').addClass('active');
					
					formatTime('.startTime', '&nbsp;&nbsp;&nbsp;');
					formatTime('.endTime', '&nbsp;&nbsp;&nbsp;');
		       });
		// ]]>
	</script>
  	
  	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">

		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<!-- INTERFACEWRAPPER -->
			<div id="interfaceWrapper">
				
				<!-- INTERFACE -->				
				<div id="interface">
              	 
              	 <!-- STEPS -->
					<ul id="steps">
						<li class="active"><a href="#" title="View KinekPoint Profile">View KinekPoint Profile</a></li>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
						
						<!-- STEP TITLE -->
						<h1>KinekPoint Profile</h1>
						<!-- /STEP TITLE -->
						
						<!-- EDITDEPOTPROFILE STEP1 -->
						<s:form beanclass="org.webdev.kpoint.action.ViewKinekPointActionBean">
							<s:messages/>
							<fieldset>
								<s:hidden name="depotId"></s:hidden>
								<!-- BLOCK -->
								<c:choose>
									<c:when test="${actionBean.activeUser.depotAdminAccessCheck}">
										<label for="selectedDepotId" style="margin-left:27px; margin-bottom:-15px;">Depot</label>										
										<ol class="clearfix">
											<li class="half">
												<s:select name="selectedDepotId" id="selectedDepotId">
													<s:option value="">Please select a KinekPoint</s:option>
													<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
												</s:select>												
											</li>			
											<li class="half">
												<s:submit name="getSelectedDepotDetails" value="Display Depot Details" class="button"></s:submit>
											</li>					
										</ol>
									</c:when>
								</c:choose>
								<ol class="clearfix">
									<li class="half">
										<table>
											<thead>
												<tr>
													<th colspan="2">KinekPoint Information</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td><strong>Depot ID</strong></td>
													<td>${actionBean.depot.depotId} </td>
												</tr>
												<tr>
													<td><strong>Business Name</strong></td>
													<td>${actionBean.depot.name}</td>
												</tr>											
												<tr>
													<td><strong>Phone #</strong></td>
													<td>${actionBean.depot.phone}</td>
												</tr>
												<tr>
													<td><strong>Email Address</strong></td>
													<td>${actionBean.depot.email}</td>
												</tr>												
											</tbody>
										</table>
										
									</li>
									<li class="half">

										<table>
											<thead>
												<tr>
													<th colspan="2">Postal Information</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td><strong>Address Line 1</strong></td>
													<td>${actionBean.depot.address1}</td>
												</tr>
												<tr>
													<td><strong>Address Line 2</strong></td>
													<td>${actionBean.depot.address2}</td>
												</tr>
												<tr>
													<td><strong>City</strong></td>
													<td>${actionBean.depot.city}</td>
												</tr>
												<tr>
													<td><strong>State/Province/Region</strong></td>
													<td>${actionBean.depot.state.name}</td>
												</tr>
												<tr>
													<td><strong>Postal/Zip Code</strong></td>
													<td>${actionBean.depot.zip}</td>
												</tr>																													
											</tbody>
										</table>

									</li>
									<li class="half">
									
										<table>
											<thead>
												<tr>
													<th colspan="2">Services &amp; Features</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td><strong>Service Available In</strong></td>
													<td>|
													<c:forEach items="${actionBean.depot.languages}" var="item" varStatus="loop">
													${item.name}&nbsp;|&nbsp;
													</c:forEach>
													</td>
												</tr>
												<tr>
													<td><strong>Methods of Payment</strong></td>
													<td>|
													<c:forEach items="${actionBean.depot.payMethod}" var="item" varStatus="loop">
													${item.name}&nbsp;|&nbsp;
													</c:forEach>
													</td>
												</tr>
												<tr>
													<td><strong>Accepted Credit Cards</strong></td>
													<td>|
													<c:forEach items="${actionBean.depot.cards}" var="item" varStatus="loop">
													${item.name}&nbsp;|&nbsp;
													</c:forEach>
													</td>
												</tr>
												<tr>
													<td><strong>Other Features</strong></td>
													<td>|
													<c:forEach items="${actionBean.depot.features}" var="item" varStatus="loop">
													${item.name}&nbsp;|&nbsp;
													</c:forEach>
													</td>
												</tr>
												<tr>
													<td><strong>Parcels with Duty &amp; Taxes</strong></td>
													<c:choose>
														<c:when test="${actionBean.depot.acceptsDutyAndTax}">
															<td>Accepts</td>
														</c:when>
														<c:otherwise>
															<td>Does not Accept</td>
														</c:otherwise>
													</c:choose>
												</tr>
												<tr>
													<td  width="150px"><strong>Additional Info</strong></td>
													<td>${actionBean.depot.extraInfo}</td>
												</tr>
											</tbody>
										</table>									
									
									</li>
									<li class="half">
									
										<table>
											<thead>
												<tr>
													<th colspan="2">Hours of Operation</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td><strong>Monday</strong></td>
														<td>
															<c:choose>
																<c:when test="${actionBean.depot.operatingHours.closedMonday}">
																	Closed
																</c:when>
																<c:otherwise>
																<span class="startTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.mondayStart}" type="both" pattern="h:mm a" />
																</span>to<span class="endTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.mondayEnd}" type="both" pattern="h:mm a" />
																</span>
																</c:otherwise>
															</c:choose>	
														</td>
													</tr>
													<tr>
														<td><strong>Tuesday</strong></td>
														<td>
															<c:choose>
																<c:when test="${actionBean.depot.operatingHours.closedTuesday}">
																	Closed
																</c:when>
																<c:otherwise>
																<span class="startTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.tuesdayStart}" type="both" pattern="h:mm a" />
																</span>to<span class="endTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.tuesdayEnd}" type="both" pattern="h:mm a" />
																</span>
																</c:otherwise>
															</c:choose>	
														</td>
													</tr>
													<tr>
														<td><strong>Wednesday</strong></td>
														<td>
															<c:choose>
																<c:when test="${actionBean.depot.operatingHours.closedWednesday}">
																	Closed
																</c:when>
																<c:otherwise>
																<span class="startTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.wednesdayStart}" type="both" pattern="h:mm a" />
																</span>to<span class="endTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.wednesdayEnd}" type="both" pattern="h:mm a" />
																</span>
																</c:otherwise>
															</c:choose>	
														</td>
													</tr>
													<tr>
														<td><strong>Thursday</strong></td>
														<td>
															<c:choose>
																<c:when test="${actionBean.depot.operatingHours.closedThursday}">
																	Closed
																</c:when>
																<c:otherwise>
																	<span class="startTime">
																		<fmt:formatDate value="${actionBean.depot.operatingHours.thursdayStart}" type="both" pattern="h:mm a" />
																	</span>to<span class="endTime">
																		<fmt:formatDate value="${actionBean.depot.operatingHours.thursdayEnd}" type="both" pattern="h:mm a" />
																	</span>
																</c:otherwise>
															</c:choose>						
														</td>
													</tr>
													<tr>
														<td><strong>Friday</strong></td>
														<td>
															<c:choose>
																<c:when test="${actionBean.depot.operatingHours.closedFriday}">
																	Closed
																</c:when>
																<c:otherwise>
																	<span class="startTime">
																		<fmt:formatDate value="${actionBean.depot.operatingHours.fridayStart}" type="both" pattern="h:mm a" />
																	</span>to<span class="endTime">
																		<fmt:formatDate value="${actionBean.depot.operatingHours.fridayEnd}" type="both" pattern="h:mm a" />
																	</span>
																</c:otherwise>
															</c:choose>	
														</td>
													</tr>
													<tr>
														<td><strong>Saturday</strong></td>
														<td>
															<c:choose>
																<c:when test="${actionBean.depot.operatingHours.closedSaturday}">
																	Closed
																</c:when>
																<c:otherwise>
																<span class="startTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.saturdayStart}" type="both" pattern="h:mm a" />
																</span>to<span class="endTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.saturdayEnd}" type="both" pattern="h:mm a" />
																</span>
																</c:otherwise>
															</c:choose>						
														</td>
													</tr>
													<tr>
														<td><strong>Sunday</strong></td>
														<td>
															<c:choose>
																<c:when test="${actionBean.depot.operatingHours.closedSunday}">
																	Closed
																</c:when>
																<c:otherwise>
																<span class="startTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.sundayStart}" type="both" pattern="h:mm a" />
																</span>to<span class="endTime">
																	<fmt:formatDate value="${actionBean.depot.operatingHours.sundayEnd}" type="both" pattern="h:mm a" />
																</span>
																</c:otherwise>
															</c:choose>
														</td>
													</tr>
												<tr>
													<td width="120px"><strong>Additional Info (Hours of operation)</strong></td>
													<td>${actionBean.depot.operatingHours.hoursInfo}</td>
												</tr>
											</tbody>
										</table>
									</li>
									<li class="half">
										<table>
											<thead>
												<tr>	
													<th colspan="2">Receiving Fees</th>
												</tr>
											</thead>
											<tbody>
												<c:choose>
													<c:when test="${fn:length(actionBean.unifiedRates)>0}">
														<c:forEach items="${actionBean.unifiedRates}" var="unified" varStatus="loop">
															<tr>
																<c:choose>
																	<c:when test="${(unified.packageWeightGroup.maxWeight > 0)}">
																		<td style="width: 150px"><fmt:formatNumber value="${unified.packageWeightGroup.minWeight}" maxFractionDigits="0"/> lbs - <fmt:formatNumber value="${unified.packageWeightGroup.maxWeight}" maxFractionDigits="0"/> lbs</td>
																	</c:when>
																	<c:otherwise>
																		<td style="width: 100px"><fmt:formatNumber value="${unified.packageWeightGroup.minWeight}" maxFractionDigits="0"/> lbs or greater</td>
																	</c:otherwise>
																</c:choose>
																
																<c:choose>
																	<c:when test="${(actionBean.kpPackageRates[unified.id-1] != null)}">
																		<td><fmt:formatNumber value="${actionBean.kpPackageRates[unified.id-1].actualFee}" type="currency"></fmt:formatNumber></td>
																	</c:when>
																	<c:otherwise>
																		<td>Not supported</td>
																	</c:otherwise>
																</c:choose>
															</tr>
														</c:forEach>
														<tr><td>Add. Skid Fee</td>
															<c:choose>
																<c:when test="${(actionBean.kpSkidRate != null)}">
																	<td><fmt:formatNumber value="${actionBean.kpSkidRate.actualFee}" type="currency"></fmt:formatNumber></td>
																</c:when>
																<c:otherwise>
																	<td>Not supported</td>
																</c:otherwise>
															</c:choose>
														</tr>
													</c:when>
												</c:choose>
											</tbody>
										</table>	
									</li>
									<c:if test="${actionBean.hasExtendedRates}">
									<li class="half">
										<table>
											<thead>
												<tr>	
													<th colspan="3">Extended Storage Fees</th>
												</tr>
											</thead>
											<tbody>
												<c:choose>
													<c:when test="${fn:length(actionBean.unifiedExtendedStorageRates)>0}">
														<c:forEach items="${actionBean.unifiedExtendedStorageRates}" var="unified" varStatus="loop">
															<c:if test="${(unified.fee > 0)}">
															<tr>
																<c:choose>
																	<c:when test="${(unified.storageWeightGroup.maxWeight > 0)}">
																		<td style="width: 150px"><fmt:formatNumber value="${unified.storageWeightGroup.minWeight}" maxFractionDigits="0"/> lbs - <fmt:formatNumber value="${unified.storageWeightGroup.maxWeight}" maxFractionDigits="0"/> lbs</td>
																	</c:when>
																	<c:otherwise>
																		<td style="width: 100px"><fmt:formatNumber value="${unified.storageWeightGroup.minWeight}" maxFractionDigits="0"/> lbs or greater</td>
																	</c:otherwise>
																</c:choose>
																
																<c:choose>
																	<c:when test="${(unified.storageDuration.maxDays > 0)}">
																		<td style="width: 150px"><fmt:formatNumber value="${unified.storageDuration.minDays}" maxFractionDigits="0"/> days - <fmt:formatNumber value="${unified.storageDuration.maxDays}" maxFractionDigits="0"/> days</td>
																	</c:when>
																	<c:otherwise>
																		<td style="width: 100px"><fmt:formatNumber value="${unified.storageDuration.minDays}" maxFractionDigits="0"/> days or more</td>
																	</c:otherwise>
																</c:choose>
															
																<c:choose>
																	<c:when test="${(actionBean.kpExtendedStorageRates[unified.id-1] != null)}">
																		<td><fmt:formatNumber value="${actionBean.kpExtendedStorageRates[unified.id-1].actualFee}" type="currency"></fmt:formatNumber></td>
																	</c:when>
																	<c:otherwise>
																		<td>\$${(unified.fee)}</td>
																	</c:otherwise>
																</c:choose>
																</c:if>
															</tr>
														</c:forEach>
													</c:when>
												</c:choose>
											</tbody>
										</table>	
									</li>
									</c:if>
									
							
										
								</ol>
								<!-- /BLOCK -->
								
															
								
								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="rightalign half">
										<s:submit name="editDepot" class="button" value="Edit KinekPoint" />
									</li>									
								</ol>
								<!-- /BLOCK -->
								
							</fieldset>
						</s:form>
						<!-- /EDITDEPOTPROFILE STEP1 -->
						
					</div>
					<!-- /STEPCONTAINER --> 
              	 
            	</div>
				<!-- /INTERFACE -->
				
			</div>
			<!-- /INTERFACEWRAPPER -->
						
		</div>
		<!-- /CONTENT -->
		
	</div>
	<!-- /CONTENTWRAPPER -->
	
	<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>

  </s:layout-component>
</s:layout-render>