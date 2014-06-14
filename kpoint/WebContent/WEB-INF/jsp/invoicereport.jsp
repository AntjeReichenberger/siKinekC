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
						<h1>Invoice Report</h1>
						<!-- /STEP TITLE -->
					
						<!-- Invoice Report -->
						<s:form beanclass="org.webdev.kpoint.action.InvoiceReportActionBean" focus="">
						
							<stripes:errors/>
							<stripes:messages/>
							
							<style type="text/css">
							
							#filtertable
							{
								width: 600px;
							}
							
							#filtertable td
							{
								vertical-align: middle;
							}
							
							.bold
							{
								font-weight: bold;	
							}		
							
							#totalTable
							{
								margin-top: 0px;
							}	
							
							#invoiceTable
							{
								margin-bottom: 0px;
							}
							
							.spaceleft
							{
								margin-left: 20px;
							}
							
							.padlesstop td
							{
								padding-top: 0px;
							}
												
							</style>
				
							<fieldset>
							
								<table id="filtertable" class="superplain">
									<tr>
										<td colspan="5"/>
										<td style="vertical-align: bottom; padding-bottom: 0px;">
											<s:label for="month1">Month</s:label>
										</td>
										<td style="vertical-align: bottom; padding-bottom: 0px;">
											<s:label for="year1">Year</s:label>
										</td>
									</tr>
									<tr class="padlesstop">
										<td>
											<s:label for="depotId" style="width: 75px;">KinekPoint:</s:label>
										</td>
										<td>
											<s:select name="depotId" id="depotId" style="width: 200px;">
												<s:option value="">All Depots</s:option>
									    		<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
									  		</s:select>
										</td>
										<td>
											<s:label for="status" class="spaceleft" style="width: 85px;">Invoice Status:</s:label>
										</td>
										<td>
											<s:select name="status" id="status" style="width: 200px;" >
												<s:option value="">All Statuses</s:option>
									    		<s:options-collection collection="${actionBean.statuses}" />
									  		</s:select>
										</td>
										<td>
											<s:label for="month1" class="spaceleft" style="width: 40px;">From:</s:label>
										</td>
										<td>
											<s:select name="month1" id="month1" style="width: 130px;">
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
											<s:text name="year1" id="year1" maxlength="4" style="width: 60px;" />
										</td>
									</tr>
									<tr>
										<td>
											<s:label for="city" style="width: 75px;">City:</s:label>
										</td>
										<td>
											<s:text name="city" id="city" style="width: 190px;"/>
										</td>
										<td>
											<s:label for="stateId" class="spaceleft" style="width: 85px;">State:</s:label>
										</td>
										<td>
											<s:select name="stateId" id="stateId" style="width: 200px;">
												<s:option value="">All States</s:option>
									    		<s:options-collection collection="${actionBean.states}" label="name" value="stateId" />
									  		</s:select>
										</td>
										<td>
											<s:label for="month2" class="spaceleft" style="width: 40px;">To:</s:label>
										</td>
										<td>
											<s:select name="month2" id="month2" style="width: 130px;">
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
											<s:text name="year2" id="year2" maxlength="4" style="width: 60px;" />
										</td>
									</tr>
								</table>

								<s:link beanclass="org.webdev.kpoint.action.InvoiceReportActionBean" style="vertical-align:middle;">Reset</s:link>
								<span class="separator">|</span>
								<s:submit name="search" class="button" value="View Report" style="margin: 10px 0px 10px 10px;" />
	
							</fieldset>

							<hr style="clear:both;" />
							
							<s:label for="invoiceTable" class="tabletotal">Results found: ${actionBean.listLength}</s:label>
							
							<!-- FORM CONTENT -->
							<div class="formContent">
								<table id="invoiceTable" class="sortable">
									<thead>
										<tr>
											<th>Invoice Number</th>
											<th>KinekPoint Name</th>											
											<th>State/Province</th>
											<th>City</th>
											<th>Invoice Month</th>
											<th>Invoice Status</th>
											<c:choose>
												<c:when test="${actionBean.showPaymentDates}">
													<th>Payment Date</th>
												</c:when>
											</c:choose>													
											<th>Parcels Received</th>
											<th>Parcels Picked Up</th>
											<th>Amount Paid</th>
											<th>Amount Owing</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(actionBean.invoices) > 0}">
												<c:forEach items="${actionBean.invoices}" var="invoice" varStatus="loop">
													<tr>
														<td>${invoice.invoiceNumber }</td>
														<td>${invoice.depot.name}</td>														
														<td>${invoice.depot.state.name}</td>
														<td>${invoice.depot.city}</td>
														<td><fmt:formatDate value="${invoice.startDate}" type="date" pattern="MMMMM, yyyy"/></td>
														<td>${invoice.status}</td>
														<c:choose>
															<c:when test="${actionBean.showPaymentDates}">
																<td><fmt:formatDate value="${invoice.paymentDate}" type="date" pattern="MMMMM dd, yyyy"/></td>
															</c:when>
														</c:choose>															
														<td>${invoice.packagesReceived}</td>
														<td>${invoice.packagesPickedUp}</td>
														<td><fmt:formatNumber value="${invoice.amountPaid}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
														<td><fmt:formatNumber value="${invoice.amountOwing}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
													</tr>
												</c:forEach>												
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${actionBean.showPaymentDates}">
														<tr>
															<td colspan="11" class="emptymessage">We could not find any invoice records to match the criteria you provided.</td>
														</tr>
													</c:when>
													<c:otherwise>
														<tr>
															<td colspan="10" class="emptymessage">We could not find any invoice records to match the criteria you provided.</td>
														</tr>
													</c:otherwise>
												</c:choose>												
											</c:otherwise>
										</c:choose>
										</tbody>
										<c:choose>
											<c:when test="${fn:length(actionBean.invoices) > 0}">
												<tfoot>
													<tr>
														<c:choose>
															<c:when test="${actionBean.showPaymentDates}">
																<th colspan="7">Totals</th>
															</c:when>
															<c:otherwise>
																<th colspan="6">Totals</th>
															</c:otherwise>
														</c:choose>													
														<td>${actionBean.packagesReceivedSum}</td>
														<td>${actionBean.packagesPickedUpSum}</td>
														<td><fmt:formatNumber value="${actionBean.amountPaidSum}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
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