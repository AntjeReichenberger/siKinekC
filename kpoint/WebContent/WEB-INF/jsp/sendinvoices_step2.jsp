<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">

		<div id="contentWrapper" class="clearfix">
			<div id="content" class="wide clearfix">
			
				<s:form beanclass="org.webdev.kpoint.action.SendInvoicesActionBean">
				
					<h1>Send Invoices</h1>
					<s:messages/>
					<s:errors/>
					
					<script type="text/javascript">

						function setHiddenNumber(button) {
							var hidden = document.getElementsByName("invoiceNumber")[0];
							hidden.value = button.value;
						}
					
					</script>
					
					<style type="text/css">
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
					</style>
					
					<div style="margin-bottom: 20px;">
						Create invoices for 
						<s:select name="month" id="month" style="width:210px; margin-left:10px;">
							<s:option value="">Choose a month</s:option>
							<s:options-collection collection="${actionBean.months}" />
					  	</s:select>
	
						<s:submit name="generate" value="Create Invoices" class="button" />
					</div>
					
					<c:choose>
						<c:when test="${actionBean.invoicesFound}">
							<div style="float: right">
								<s:submit name="send" value="Send Invoices" class="button" style="margin-left: 0px;" />
							</div>
						
							<table id="invoicetable" cellspacing="0" cellpadding="0" class="sortable">
								<thead>
									<tr>
										<th>Invoice #</th>
										<th>KinekPoint Name</th>
										<th>KinekPoint Email</th>
										<th>Amount Due</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${actionBean.invoices}" var="invoice" varStatus="loop">
										<tr>
											<td width="10%"><s:submit name="details" value="${invoice.invoiceNumber}" class="detailsubmit" onclick="setHiddenNumber(this);" /></td>
											<td width="30%">${invoice.depot.name}</td>
											<td width="30%">${invoice.depot.email}</td>
											<td width="30%">${invoice.amountDueStr}</td>
										</tr>	
									</c:forEach>
								</tbody>
							</table>							
		
							<div style="float: right">
								<s:submit name="send" value="Send Invoices" class="button" style="margin-left: 0px;" />
							</div>
						</c:when>
						<c:otherwise>
							<p>No invoice records exist.</p>
						</c:otherwise>
					</c:choose>

					<s:hidden  name="invoiceNumber" />
					<s:hidden  name="invoiceNumbers" />
					
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