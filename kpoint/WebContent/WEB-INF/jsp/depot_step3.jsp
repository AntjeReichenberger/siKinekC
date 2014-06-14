<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/depot.jsp">
	<s:layout-component name="contents">
		<style type="text/css">
			.tableTextArea {
			  width: 90% !important;
			  min-width: 90% !important;
			  max-width: 90% !important;
			}
			
			table{
				margin-top: 5px !important;
			}
			
			p {
				margin: 0px;
				padding: 0px;
			}
		</style>
			
		<!-- STEPS -->
		<ul id="steps">
			<c:choose>
			<c:when test="${actionBean.createDepot}">
				<li><a href="#step1" title="Edit KinekPoint Information"><span>Step 1:</span> KinekPoint Information</a></li>
				<li><a href="#step2" title="Edit Services &amp; Features"><span>Step 2:</span> Services &amp; Features</a></li>
				<li class="active"><a href="#step3" title="Edit Pricing"><span>Step 3:</span> Pricing</a></li>
				<li><a href="#step4" title="Edit Hours of Operation"><span>Step 4:</span> Hours of Operation</a></li>
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
				<li class="active">
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

			<h1 style="float:right;">KinekPoint: ${actionBean.depotName}</h1>
		
			<!-- STEP TITLE -->
			<h1>
				<c:choose>
					<c:when test="${actionBean.createDepot}">
						<span>3</span> 
					</c:when>
				</c:choose>
				Edit Pricing
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
					
						<ol class="clearfix">
						<li class="half">
					
						<h3>Package Rates</h3>
						<table style="width: 500px">	
								<thead>
									<tr>	
										<th>Weight</th>
										<th>Receiving Fee</th>
										<th>Supported</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${fn:length(actionBean.unifiedRates)>0}">
											<c:forEach items="${actionBean.unifiedRates}" var="unified"
												varStatus="loop">
												<tr>
													<c:choose>
														<c:when test="${(unified.packageWeightGroup.maxWeight > 0)}">
															<td style="width: 150px">${unified.packageWeightGroup.minWeight} lbs - ${unified.packageWeightGroup.maxWeight} lbs</td>
														</c:when>
														<c:otherwise>
															<td style="width: 100px">${unified.packageWeightGroup.minWeight} lbs or greater</td>
														</c:otherwise>
													</c:choose>
													
													<c:choose>
														<c:when test="${(actionBean.kpPackageRates[unified.id-1] != null)}">
															<td><s:text class="tableTextArea" name="kpPackageRates[${unified.id-1}].actualFee" id="kpPackageRates[${unified.id-1}].actualFee" formatType="currency"></s:text>(max \$${unified.fee})</td>
														</c:when>
														<c:otherwise>
															<td><s:text class="tableTextArea" name="kpPackageRates[${unified.id-1}].actualFee" id="kpPackageRates[${unified.id-1}].actualFee" formatType="currency">\$${unified.fee}</s:text>(max \$${unified.fee})</td>
														</c:otherwise>
													</c:choose>
													
													<c:choose>		
														<c:when test="${(actionBean.kpPackageAccepted != null)}">
															<td style="width: 50px"><s:checkbox name="kpPackageAccepted[${unified.id-1}]"/></td>
														</c:when>
														<c:otherwise>	
															<c:choose>
																<c:when test="${(actionBean.kpPackageRates[unified.id-1] != null)}">
																	<td style="width: 50px"><s:checkbox checked="true" name="kpPackageAccepted[${unified.id-1}]"/></td>
																</c:when>
																<c:otherwise>
																	<td style="width: 50px"><s:checkbox name="kpPackageAccepted[${unified.id-1}]"/></td>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												</tr>
											</c:forEach>
											
											<tr><td>Add. Skid Fee</td>
												<c:choose>
													<c:when test="${(actionBean.kpSkidRate != null)}">
														<td><s:text class="tableTextArea" name="kpSkidRate.actualFee" id="kpSkidRate.actualFee" formatType="currency"></s:text>(max \$${actionBean.unifiedSkidRate.fee})</td>
														<td style="width: 50px"><s:checkbox name="kpSkidRateAccepted" checked="true"/></td>
													</c:when>
													<c:otherwise>
														<td><s:text class="tableTextArea" name="kpSkidRate.actualFee" id="kpSkidRate.actualFee" formatType="currency">\$0.00</s:text>(max \$${actionBean.unifiedSkidRate.fee})</td>
														<td style="width: 50px"><s:checkbox name="kpSkidRateAccepted"/></td>
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
							<h3>Extended Storage Rates</h3>
							<table style="width: 500px">
								<thead>
									<tr>	
										<th>Weight</th>
										<th>Days</th>
										<th>Fee/Day</th>
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
															<td style="width: 150px">${unified.storageWeightGroup.minWeight} lbs - ${unified.storageWeightGroup.maxWeight} lbs</td>
														</c:when>
														<c:otherwise>
															<td style="width: 100px">${unified.storageWeightGroup.minWeight} lbs or greater</td>
														</c:otherwise>
													</c:choose>	
													
													<c:choose>
														<c:when test="${(unified.storageDuration.maxDays > 0)}">
															<td style="width: 150px">${unified.storageDuration.minDays} days - ${unified.storageDuration.maxDays} days</td>
														</c:when>
														<c:otherwise>
															<td style="width: 100px">${unified.storageDuration.minDays} days or more</td>
														</c:otherwise>
													</c:choose>		
													
													<c:choose>
														<c:when test="${(actionBean.kpExtendedStorageRates[unified.id-1] != null)}">
															<td><s:text class="tableTextArea" name="kpExtendedStorageRates[${unified.id-1}].actualFee" id="kpExtendedStorageRates[${unified.id-1}].actualFee" formatType="currency"></s:text>(max \$${unified.fee})</td>
														</c:when>
														<c:otherwise>
															<td><s:text class="tableTextArea" name="kpExtendedStorageRates[${unified.id-1}].actualFee" id="kpExtendedStorageRates[${unified.id-1}].actualFee" formatType="currency">\$0.00</s:text>(max \$${unified.fee})</td>
														</c:otherwise>
													</c:choose>																					
												</tr>
												</c:if>
											</c:forEach>
										</c:when>
									</c:choose>
								</tbody>
							</table>
							</li>
							</c:if>
							</ol>
					</div>
					<!-- /FORM CONTENT -->
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="rightalign half">
							<s:link beanclass="org.webdev.kpoint.action.ViewKinekPointActionBean">Cancel</s:link>
							<span class="separator">|</span>
							<s:submit class="button" name="savePrices" value="Save Changes"/>
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