<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">

		<div id="contentWrapper" class="clearfix">
			<div id="content" class="wide clearfix">
			
				<s:form beanclass="org.webdev.kpoint.action.InvoiceSearchActionBean">
				
					<h1>Invoice Search</h1>
					
					<s:messages/>
					<s:errors/>
					
					<style type="text/css">
						
						#searchTable
						{
							width: 560px;
						}
					
						#searchTable td, #searchTable tbody, #searchTable
						{
							border-style: none;
							vertical-align: bottom;
						}
						
						#searchTable td.rowlabel
						{
							vertical-align: middle;
						}
						
						#searchTable tr
						{
							background-image: none;
						}
						
						.detailsubmit
						{
							background-color:transparent;
							border-style:none;
							color:blue;
							cursor:pointer;
							padding:0;
							text-decoration:underline;
							width:auto;
						}
						
						select, input
						{
							width: auto;
						}
					
					</style>
					
					<script type="text/javascript">

						jQuery(document).ready(function(){
							checkSelected();
						});

						function setHiddenNumber(button) {
							var hidden = document.getElementsByName("invoiceNumber")[0];
							hidden.value = button.value;
						}
					
						function selectAll() {
							var box = document.getElementsByName("checkall")[0];
							var rows = document.getElementById("invoicetable").rows;

							for(var i = 1; i < rows.length; i++) {
								if (rows[i].cells[0].childNodes[0].tagName == "INPUT") {
									rows[i].cells[0].childNodes[0].checked = box.checked;
								}
							}
							checkSelected();
						}

						function checkSelected() {
							var selected = false;
							var table = document.getElementById("invoicetable");
							if (!table) {
								return;
							}
							
							var rows = table.rows;
							var checkStr = "";

							for(var i = 1; i < rows.length; i++) {
								if (rows[i].cells[0].childNodes[0].tagName == "INPUT" && rows[i].cells[0].childNodes[0].checked) {
									selected = true;
									checkStr += rows[i].cells[0].childNodes[0].value + ",";
								}
							}

							if(selected) {
								var buttons = document.getElementsByName("send");
								buttons[0].disabled = false;
								buttons[1].disabled = false;
								buttons[0].className = "button";
								buttons[1].className = "button";

								checkStr = checkStr.substring(0, checkStr.length-1);
							}
							else {
								var buttons = document.getElementsByName("send");
								buttons[0].disabled = true;
								buttons[1].disabled = true;
								buttons[0].className = "buttonDisabled";
								buttons[1].className = "buttonDisabled";
							}
						}

					</script>
					
					<table id="searchTable">
						<tr>
							<td class="rowlabel">KinekPoint:</td>
							<td colspan="2">
								<s:select name="depot" id="depot" style="width: 220px;">
									<s:option value="">All KinekPoints</s:option>
									<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
							  	</s:select>
							</td>
						</tr>
						<tr>
							<td class="rowlabel" style="padding-top: 17px;">Month:</td>
							<td>
								<s:select name="month" id="month" style="width: 100px; margin-right: 17px; margin-bottom: 3px;">
									<s:option value="">All Months</s:option>
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
								<s:select name="year" id="year" style="width: 100px; margin-bottom: 3px;">
									<s:option value="">All Years</s:option>
									<s:options-collection collection="${actionBean.years}" />
							  	</s:select>
							</td>
							<td>
								<s:submit name="search" value="Search" class="button" />
							</td>
						</tr>
					</table>
					
					<hr style="margin: 20px;">

					
							<div style="float: right">
								<s:submit name="send" value="Send Invoices" class="buttonDisabled" style="margin-left: 0px;" disabled="true" />
							</div>
						
							<table id="invoicetable" cellspacing="0" cellpadding="0" class="sortable">
								<thead>
									<tr>
										<th><s:checkbox name="checkall" onclick="selectAll()"></s:checkbox></th>
										<th>Invoice #</th>
										<th>KinekPoint Name</th>
										<th>KinekPoint Email</th>
										<th>Invoice Period</th>
										<th>Last Issued</th>
										<th>Amount Due</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${actionBean.invoicesFound}">
											<c:forEach items="${actionBean.invoices}" var="invoice" varStatus="loop">
												<tr>
													<td><s:checkbox name="checks" value="${invoice.invoiceNumber}" onclick="checkSelected()" /></td>
													<td width="16%"><s:submit name="details" value="${invoice.invoiceNumber}" class="detailsubmit" onclick="setHiddenNumber(this);" /></td>
													<td width="16%">${invoice.depot.name}</td>
													<td width="16%">${invoice.depot.email}</td>
													<td width="18%"><fmt:formatDate value="${invoice.startDate}" pattern="MMMMM dd, yyyy" /> - <fmt:formatDate value="${invoice.endDate}" pattern="MMMMM dd, yyyy" /></td>
													<td width="18%"><fmt:formatDate value="${invoice.lastIssuedDate}" pattern="MMMMM dd, yyyy" /></td>
													<td width="16%">${invoice.amountDueStr}</td>
												</tr>	
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="7" class="emptymessage">
													We were unable to find any invoices that match the criteria you have provided.
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							
							<div style="float: right">
								<s:submit name="send" value="Send Invoices" class="buttonDisabled" style="margin-left: 0px;" disabled="true" />
							</div>
						
					
					<s:hidden  name="invoiceNumber" />
					<s:hidden  name="invoiceNumbers" />
					<s:hidden  name="checkedNumbers" />

				</s:form>
				
			</div>
		</div>

		<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>
		
		<script type="text/javascript" src="resource/js/jquery.tablesorter.min.js"></script>
		<script type="text/javascript">
			jQuery(document).ready(function() { 
				jQuery("#invoicetable").tablesorter(); 
	
				jQuery("#invoicetable").bind("sortEnd",function() { 
			        resetRowClasses(document.getElementById("invoicetable"));
			    }); 
	
				jQuery("#invoicetable").bind("sortStart",function() { 
			        clearRowClasses(document.getElementById("invoicetable"));
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

	</s:layout-component>
</s:layout-render>