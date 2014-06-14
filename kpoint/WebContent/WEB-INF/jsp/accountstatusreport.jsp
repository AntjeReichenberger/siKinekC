<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">		
		<!-- CONTENTWRAPPER -->
		<div id="contentWrapper" class="clearfix">		
			<!-- CONTENT -->
			<div id="content" class="wide clearfix">		
				<!-- INTERFACEWRAPPER -->
				<div id="interfaceWrapper">		
					<!-- STEPCONTAINER -->
					<div id="stepContainer">

						<!-- STEP TITLE -->
						<h1>Account Status Report</h1>
						<!-- /STEP TITLE -->
					
						<!-- Account Status Report -->
						<s:form beanclass="org.webdev.kpoint.action.AccountStatusReportActionBean" focus="">
						
							<stripes:errors/>
							<stripes:messages/>
							
							<style type="text/css">							
								.bold
								{
									font-weight: bold;	
								}	
								
								.dateTable
								{
									background-color:transparent;
									margin: 0px 0px 20px 0px;
									width:auto;
								}		
								
								.dateTable tbody
								{
									border-style: none;
								}
								
								.dateTable tr
								{
									background-image:none;
									border-style:none;
								}		
								
								.dateTable td
								{
									border-style:none;
									padding:0 10px 0 0;
									vertical-align: middle;
								}		
								
								#totalTable
								{
									margin-top: 0px;
								}	
								
								#invoiceTable
								{
									margin-bottom: 0px;
								}
													
							</style>
							
							<table class="dateTable">
								<tr>
									<td></td>
									<td>
										<s:label for="month1">Month</s:label><br/>
									</td>
									<td>
										<s:label for="year1">Year</s:label>
									</td>
								</tr>
								<tr>
									<td>From:</td>
									<td>
										<s:select name="month1" id="month1" style="width: 150px;">
											<s:option value="">Select a month</s:option>
											<s:option value="1">January</s:option>
											<s:option value="2">February</s:option>
											<s:option value="3">March</s:option>
											<s:option value="4">April</s:option>
											<s:option value="5">May</s:option>
											<s:option value="6">June</s:option>
											<s:option value="7">July</s:option>
											<s:option value="8">August</s:option>
											<s:option value="9">September</s:option>
											<s:option value="10">October</s:option>
											<s:option value="11">November</s:option>
											<s:option value="12">December</s:option>
									  	</s:select>
									</td>
									<td>	
										<s:text name="year1" id="year1" maxlength="4" style="width: 50px;" />
									</td>
								</tr>
								<tr>
									<td style="padding-top: 10px;">To:</td>
									<td style="padding-top: 10px;">
										<s:select name="month2" id="month2" style="width: 150px;">
											<s:option value="">Select a month</s:option>
											<s:option value="1">January</s:option>
											<s:option value="2">February</s:option>
											<s:option value="3">March</s:option>
											<s:option value="4">April</s:option>
											<s:option value="5">May</s:option>
											<s:option value="6">June</s:option>
											<s:option value="7">July</s:option>
											<s:option value="8">August</s:option>
											<s:option value="9">September</s:option>
											<s:option value="10">October</s:option>
											<s:option value="11">November</s:option>
											<s:option value="12">December</s:option>
									  	</s:select>
									</td>
									<td style="padding-top: 10px;">	
										<s:text name="year2" id="year2" maxlength="4" style="width: 50px;" />
									</td>
								</tr>								
								<tr>
									<td style="padding-top: 10px;">Depot:</td>
									<td colspan="2" style="padding-top: 10px;">
										<c:choose>
										<c:when test="${actionBean.activeUser.depotAdminAccessCheck}">											
											<s:select name="selectedDepotId">
												<option value="" >Please select a KinekPoint</option>
												<s:option value="-1">ALL DEPOTS</s:option>
												<s:options-collection collection="${actionBean.userDepots}" label="nameAddress1City" value="depotId" />
											</s:select>
										</c:when>
										</c:choose>
									</td>
								</tr>
							</table>
							
							<s:link beanclass="org.webdev.kpoint.action.AccountStatusReportActionBean" style="vertical-align:middle;">Reset</s:link>
							<span class="separator">|</span>
							<s:submit name="search" class="button" value="View Report" style="margin: 10px 0px 15px 0px;" />				
	
							<hr style="clear:both;" />
							
							<s:label for="invoiceTable" class="tabletotal">Results found: ${actionBean.listLength}</s:label>
							
							<!-- FORM CONTENT -->
							<div class="formContent">

										<table id="invoiceTable" class="sortable">
											<thead>
												<tr>
													<c:if test="${actionBean.depotId == -1}">
														<th>Depot Name</th>
													</c:if>
													<th>Invoice Number</th>
													<th>Month</th>
													<th>Issued Date</th>									
													<th>Status</th>
													<th>Payment Date</th>
													<th>Parcels Received</th>
													<th>Parcels Picked Up</th>
													<th>Invoice Amount</th>
													<th>Promotional Discounts</th>	
													<th>Amount Owing</th>
												</tr>
											</thead>
											<tbody>
											<c:choose>
												<c:when test="${actionBean.invoicesFound}">
													<c:forEach items="${actionBean.invoices}" var="invoice" varStatus="loop">
														<tr>
															<c:if test="${actionBean.depotId == -1}">
																<td>${pr.kinekPoint.name} (${pr.kinekPoint.address1},${pr.kinekPoint.city})</td>
															</c:if>
															<td>${invoice.invoiceNumber}</td>
															<td><fmt:formatDate value="${invoice.startDate}" type="date" pattern="MMMMM, yyyy"/></td>
															<td><fmt:formatDate value="${invoice.lastIssuedDate}" type="date" pattern="MMMMM dd, yyyy"/></td>
															<td>${invoice.status}</td>
															<td><fmt:formatDate value="${invoice.paymentDate}" type="date" pattern="MMMMM dd, yyyy"/></td>
															<td>${invoice.packagesReceived}</td>
															<td>${invoice.packagesPickedUp}</td>
															<td><fmt:formatNumber value="${invoice.amountDue}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>	
															<td><fmt:formatNumber value="${invoice.discountKinekTotal}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
															<td><fmt:formatNumber value="${invoice.amountOwing}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
														</tr>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr><td colspan="11" class="emptymessage">We could not find any invoice records to match the criteria you provided.</td></tr>
												</c:otherwise>
											</c:choose>
											</tbody>
											<c:choose>
												<c:when test="${actionBean.invoicesFound}">
													<tfoot>
														<tr>
															<th colspan="5">Totals</th>
															<td>${actionBean.packagesReceivedSum}</td>
															<td>${actionBean.packagesPickedUpSum}</td>
															<td><fmt:formatNumber value="${actionBean.invoiceAmountSum}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
															<td><fmt:formatNumber value="${actionBean.promotionalDiscountsSum}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
															<td><fmt:formatNumber value="${actionBean.amountOwingSum}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
														</tr>
													</tfoot>
												</c:when>
											</c:choose>
										</table>	
									
					
							</div>				
						</s:form>
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
			jQuery(document).ready(function() { 
				jQuery("#invoiceTable").tablesorter(); 
	
				jQuery("#invoiceTable").bind("sortEnd",function() { 
			        resetRowClasses(document.getElementById("invoiceTable"));
			    }); 
	
				jQuery("#invoiceTable").bind("sortStart",function() { 
			        clearRowClasses(document.getElementById("invoiceTable"));
			    }); 
			});
	
			function resetRowClasses(table) {
				var rows = table.rows;
				for(var i = 1; i < rows.length; i++) {
					if(i % 2 == 0) {
						rows[i].className = "";
					}
					else {
						rows[i].className = "even";
					}
				}
			}
	
			function clearRowClasses(table) {
				var rows = table.rows;
				for(var i = 1; i < rows.length; i++) {
					rows[i].className = "even";
				}
			}
		</script>
		
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
	</s:layout-component>
</s:layout-render>