<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/deliver.jsp">
	<s:layout-component name="contents">
		<script type="text/javascript">
			var preventDefault = false;
		
			function accept_onclick(e) {
				if (preventDefault) {
					if (e && e.preventDefault)
						e.preventDefault();
					else if (window.event && window.event.returnValue)
						window.eventReturnValue = false;
					return false;
					}
				else {
					preventDefault = true;
				}

				return true;
			}
		</script>
	
		<!-- STEPS -->
		<ul id="steps">
			<li>
				<a href="Deliver.action" title="Choose Package Recipient"><span>Step 1:</span>Choose Package Recipients</a>
			</li>
			<li class="active">
				<a href="#step2" title="Enter Package Information"><span>Step 2:</span>Enter Package Information</a>
			</li>
		</ul>
		<!-- /STEPS -->

		<!-- STEPCONTAINER -->
					
		<div id="stepContainer">
		<s:form id="formAccept" beanclass="org.webdev.kpoint.action.DeliveryActionBean" >

			<!-- STEP TITLE -->
			<h1><span>2</span>Accept Delivery - Enter Package Information</h1>
			<!-- /STEP TITLE -->

			<stripes:errors />
			
			
			<!-- ACCEPTDELIVERY STEP1 -->
			<fieldset><!-- BLOCK -->
				
				
				<s:hidden name="via" />
					<div class="formContent">
						<c:if test="${actionBean.activeUser.depotAdminAccessCheck && fn:length(actionBean.userDepots) > 1}">
							<div>
								Depot:<span class="required" style="padding: 0px 2px 0px 4px;">*</span>
								<s:select style="width:400px" name="depotId" id="depotId" onchange="jQuery('#changeDepot').trigger('click')">
									<s:option label="Please select a depot" value="0"></s:option>
									<s:options-collection collection="${actionBean.userDepots}" label="nameAddress1City" value="depotId" />
								</s:select>
								</div>
						</c:if>
						<table id="usersTable" class="sortable">
							<thead>
								<tr>
									
									<th style="text-align: center !important;">Courier<span class="required" style="padding: 0px 2px 0px 4px;" >*</span></th>
									<th style="text-align: center !important;">Weight<span class="required" style="padding: 0px 2px 0px 4px;" >*</span></th>
									<th class="skidRates" style="text-align: center !important;">On Skid?<span class="required" style="padding: 0px 2px 0px 4px;" >*</span></th>
									<th class="dutyAndTaxes" style="text-align: center !important;">Duties &amp; Taxes</th>
									<th style="text-align: center !important;">Shipped From</th>
									<th style="text-align: center !important;">Custom Info</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${actionBean.courierIds}" var="courierId" varStatus="loop">
									<tr>
										<c:choose>										
											<c:when test="${actionBean.depotId > 0 || fn:length(actionBean.userDepots) == 1}">	
												<td align="center">
													<s:select style="width:200px" name="courierIds[${loop.index}]">
														<s:option label="Please select a courier" value=""></s:option>
														<s:options-collection collection="${actionBean.couriers}" label="name" value="courierId" />
													</s:select>
												</td>	
												<td align="center">
													<s:select style="width:200px" name="weightIds[${loop.index}]" class="weightIds">
														<s:option label="Please select a weight group" value=""></s:option>
														<s:options-collection collection="${actionBean.kpWeightGroups}" label="friendlyLabel" value="id" />
													</s:select>
												</td>
											</c:when>
											<c:otherwise>
												<td align="center">
													<s:select style="width:200px" name="courierIds[${loop.index}]">
														<s:option label="Please select a depot first" value=""></s:option>
													</s:select>
												</td>
												<td align="center">
													<s:select style="width:200px" name="weightIds[${loop.index}]" class="weightIds">
														<s:option label="Please select a depot first" value=""></s:option>
													</s:select>
												</td>
											</c:otherwise>															
										</c:choose>
										
										<td class="skidRates" align="center">
											<s:select style="width:100px" name="skidIds[${loop.index}]">
												<s:option label="NO" value="false"></s:option>
												<s:option label="YES" value="true"></s:option>
											</s:select>
										</td>
										
										<td class="dutyAndTaxes" align="center"><s:text name="dutyAndTaxes[${loop.index}]" formatType="number" formatPattern="#0.00"  style="width:190px">${dutyAndTaxes[loop.index]}</s:text></td>
										
										<td align="center"><s:text name="shippedFrom[${loop.index}]" maxlength="49" id="shippedFromId" style="width:190px" /></td>
										<td align="center"><s:text name="customInfo[${loop.index}]" maxlength="49" style="width:190px"></s:text></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<s:text name="numberOfPackages" style="visibility: hidden;" />
						<div style="float: right;">
							<c:if test="${actionBean.moreThanOnePackage}">
								<s:submit name="removeLastPackage" class="buttonLink" value="Remove Last Package" />
								<span class="separator">|</span>
							</c:if>
							<s:submit name="addAnotherPackage" class="buttonLink" value="Add Another Package" />
							<span class="separator">|</span>
							<s:submit name="accept" id="accept" class="button" value="Accept Delivery" onclick="return accept_onclick(event);"/>
							
							<s:submit name="changeDepot" id="changeDepot" value="CD" style="display:none"/>
						</div>
					</div>
					
			<!-- /FORM CONTENT --></fieldset>
			<!-- /ACCEPTDELIVERY STEP1 -->
		</s:form>
		
			<script type="text/javascript">
				jQuery(document).ready(function () {
					ChangeWeightDropdowns();
				});
				
				function invokeDuty(form, event, container) {
		            params = {};
		            if (event != null) params = event + '&' + jQuery('#depotId').serialize();            
		            jQuery.post(form.action,
		                    params,
		                    function (xml) {
		            			if(xml == 'true')
		            			{
		            				jQuery('.dutyAndTaxes').show();
		            			}
		            			else{
		            				jQuery('.dutyAndTaxes').hide();
		            			}
		                    });
		        }
				
				function invokeSkidRate(form, event, container) {
		            params = {};
		            if (event != null) params = event + '&' + jQuery('#depotId').serialize();            
		            jQuery.post(form.action,
		                    params,
		                    function (xml) {
		            			if(xml == 'true')
		            			{
		            				jQuery('.skidRates').show();
		            			}
		            			else{
		            				jQuery('.skidRates').hide();
		            			}
		                    });
		        }
				
				function ChangeWeightDropdowns(){
					ShowHideDutyAndTaxes();
					
				}	
				function ShowHideDutyAndTaxes(){
					invokeDuty(jQuery('form')[0], 'getDutyAndTaxesAjax', '.dutyAndTaxes');
					invokeSkidRate(jQuery('form')[0], 'getSkidRateAjax', '.skidRates');
				}
			</script>
		
		</div>
		<!-- /STEPCONTAINER -->
	</s:layout-component>
</s:layout-render>