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
						<h1>Credit Redemption Report</h1>
						<!-- /STEP TITLE -->
					
						<!-- PromotionRedemptionReport-->
						<s:form beanclass="org.webdev.kpoint.action.PromotionRedemptionReportActionBean">
							<table class="superplain" style="width:700px;">
							<tr>
							<td  nowrap>
								<s:label for="creditReportType.depot" class="radio" style="padding-left: 0px;">
									<s:radio name="creditReportType" value="depot" class="radio" id="creditReportType.depot" style="margin: 0px;" /> Depot Credits
								</s:label>
								<br />
								<s:label for="creditReportType.consumer" class="radio" style="padding-left: 0px;">
									<s:radio name="creditReportType" value="consumer" class="radio" id="creditReportType.consumer" checked="checked" style="margin: 0px;" /> Consumer Credits
								</s:label>
							</td>
							<td align="right" nowrap>
							<br />
								<s:label for="filterOption">Issue Reason:</s:label>
								<s:select name="issueReason" id="issueReason" style="width:125px;">
									<s:option value="0">Show All</s:option>
									<s:option value="1">Referral</s:option>
									<s:option value="2">Registration</s:option>
								</s:select>
								<br />
								<s:checkbox name="onlyRedeemed" id="onlyRedeemed" class="checkbox" style="margin: 0px;" />
								<s:label for="onlyRedeemed"> Only Redeemed</s:label>&nbsp;
							</td>
							<td align="right">
								<s:link beanclass="org.webdev.kpoint.action.PromotionRedemptionReportActionBean" style="vertical-align:middle;">Reset</s:link>
								<span class="separator">|</span>
								<s:submit name="search" class="button" value="View Report" style="margin: 10px 0px 10px 0px;" />
							</td>
							</tr>
							</table>	
							
					
							
															
										
							
							<hr style="clear:both;" />
							
							<s:label for="Credits" class="tabletotal">Results found: ${actionBean.listLength}</s:label>
									
							<!-- FORM CONTENT -->
							<div class="formContent">
									<table id="Credits" class="sortable">
									<c:choose>
										<c:when test="${actionBean.creditReportType == 'consumer'}">
											<thead>
												<tr>
													<th>Consumer Name</th>
													<th>Promotion</th>
													<th>Status</th>
													<th>Reason</th>
													<th>Issue Date</th>
													<th>Redemption Date</th>

												</tr>
											</thead>
											<tbody>
											<c:choose>
												<c:when test="${fn:length(actionBean.issuedConsumerCredits) > 0}">
													<c:forEach items="${actionBean.issuedConsumerCredits}" var="consumercredit" varStatus="loop">
														<tr>
															<td>${consumercredit.user.lastName}, ${consumercredit.user.firstName}</td>
															<td>${consumercredit.promotion.title}</td>
															<td>${consumercredit.creditStatus.status}</td>
															<td>${consumercredit.issueReason.title}</td>
															<td><fmt:formatDate value="${consumercredit.issueDate}" type="date" pattern="MMMMM dd, yyyy"/></td>
															<td><fmt:formatDate value="${consumercredit.pickup.pickupDate}" type="date" pattern="MMMMM dd, yyyy"/></td>

														</tr>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr><td colspan="7" class="emptymessage">We could not find any Consumer Credits to match the criteria you provided.</td></tr>
												</c:otherwise>
											</c:choose>
											</tbody>
										</c:when>
										<c:when test="${actionBean.creditReportType == 'depot'}">
												<thead>
												<tr>
													<th>Depot Name</th>
													<th>Promotion</th>
													<th>Status</th>
													<th>Reason</th>
													<th>Issue Date</th>
													<th>Redemption Date</th>
													<th>Redemption Invoice #</th>
												</tr>
											</thead>
											<tbody>
											<c:choose>
												<c:when test="${fn:length(actionBean.issuedKinekPointCredits) > 0}">
													<c:forEach items="${actionBean.issuedKinekCredits}" var="depotcredit" varStatus="loop">
														<tr>
															<td>${depotcredit.depot.name}</td>
															<td>${depotcredit.promotion.title}</td>
															<td>${depotcredit.creditStatus.status}</td>
															<td>${depotcredit.issueReason.title}</td>
															<td><fmt:formatDate value="${depotcredit.issueDate.time}" type="date" pattern="MMMMM dd, yyyy"/></td>
															<td><fmt:formatDate value="${depotcredit.redemptionDate.time}" type="date" pattern="MMMMM dd, yyyy"/></td>
															<td>${depotcredit.redemptionInvoice.invoiceNumber}</td>
														</tr>
													</c:forEach>
												</c:when>
												<c:otherwise>
													<tr><td colspan="7" align="center"><br /><br />We could not find any Depot Credits to match the criteria you provided.<br /><br /></td></tr>
												</c:otherwise>
											</c:choose>
											</tbody>
										</c:when>
									</c:choose>
									</table>
							</div>
							<!-- /FORM CONTENT -->
						
						</s:form>
						<!-- /PromotionRedemptionReport-->
					
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
				jQuery("#Credits").tablesorter(); 

				jQuery("#Credits").bind("sortEnd",function() { 
			        resetRowClasses(document.getElementById("Credits"));
			    }); 

				jQuery("#Credits").bind("sortStart",function() { 
			        clearRowClasses(document.getElementById("Credits"));
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