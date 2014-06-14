<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">
	
		<style type="text/css">
			#previewbody 
			{
				background-color: #c1c1c1;
			}
			
			table.bg1 
			{
				background-color: #c1c1c1;
				width: 800px;
				border: solid 1px;
				border-color: #E3E3E3;
			}
			
			table.bg1 td, table.bg1 tbody
			{
				border-style: none;
			}
			
			table.bg2 
			{
				background-color: #ffffff;
				margin: 0px;
			}
			
			td.body 
			{
				padding: 0px 20px 0px 20px;
				background-repeat: repeat-y;
				background-position: bottom center;
				background-color: #ffffff;
				font-family:"Lucida Sans Unicode","Lucida Grande",Verdana,Arial,Helvetica,sans-serif;
			}
			
			#infotable td
			{
				padding: 2px 10px 2px 2px;
				font-size: 12;
			}
			
			#packagestable
			{
				width: 100%;
				margin-bottom: 10px;
				border-collapse: collapse;
				border-spacing: 0px;
				border-color: #E3E3E3;
			}
			
			#packagestable td, #totalstable td
			{
				border: solid 1px;
				border-color: #E3E3E3;
				padding: 5px;
				font-size: 12;
				text-align: right;
			}
			
			#packagestable tbody tr
			{
				background:#FFFFFF url(resource/images/table_tbody_odd_bg.jpg) repeat-x scroll left bottom;
			}
			
			#packagestable tbody tr.even
			{
				background:#FFFFFF url(resource/images/table_tbody_even_bg.jpg) repeat-x scroll left bottom;
			}
			
			#totalstable
			{
				border-collapse: collapse;
				border-spacing: 0px;	
				margin: 0px;		
			}
			
			#totalsdiv
			{
				float: right;
			}
			
			#packagestable th
			{
				border:1px solid #057FBB;
				color:#FFFFFF;
				font-size:12px;
				font-weight:bold;
				padding:5px 8px 6px;
			}
			
			#packagestable tr.header 
			{
				background:#25A6E6 url(resource/images/table_th_bg.jpg) repeat-x scroll left bottom;
				border:1px solid #25A6E6;
			}
			
			.spacer
			{
				width: 50px;
			}
			
			.left
			{
				text-align: left;
			}
			
			#packagetitle
			{
				width: 100%;
				text-align: center;
				margin-top: 30px;
				margin-bottom: 5px;
				font-weight: bold;
				font-size: 14px;
			}
			
			.blue
			{
				color:#058FD3;
				font-weight:bold;
			}
			
		</style>
		
		<div id="#previewbody">
		<table border="0" cellspacing="0" cellpadding="0" class="bg1">
		   <tr style="background-image: none;">
		      <td align="center">
		         
		         <table width="800" border="0" cellspacing="0" cellpadding="0" class="bg2">
		            <tr>
		               <td valign="top" class="body">
		                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		                     <tr>
		                        <td valign="top" class="mainbar" align="left">
		                        
		                        	<h1>Invoice Preview</h1>
									
									<!-- KP Info  -->
									<table id="infotable" cellspacing="0" cellpadding="0">
										<tr>
											<td class="leftalign">KinekPoint:</td>
											<td class="leftalign">${actionBean.invoice.depot.name}</td>
											<td class="spacer"></td>
											<td class="leftalign">Invoice #</td>
											<td class="leftalign">${actionBean.invoice.invoiceNumber}</td>
											<td></td>
										</tr>
										<tr>
											<td></td>
											<td class="leftalign">${actionBean.invoice.depot.address1}</td>
											<td class="spacer"></td>
											<td class="leftalign">Invoice Period:</td>
											<td class="leftalign"><fmt:formatDate value="${actionBean.invoice.startDate}" pattern="MMMMM dd, yyyy" /> - <fmt:formatDate value="${actionBean.invoice.endDate}" pattern="MMMMM dd, yyyy" /></td>
											<td></td>
										</tr>
										<tr>
											<td></td>
											<td class="leftalign">${actionBean.invoice.depot.city}, ${actionBean.invoice.depot.state.name}</td>
											<td class="spacer"></td>
											<td class="leftalign">Invoice Date:</td>
											<td class="leftalign"><fmt:formatDate value="${actionBean.invoice.createdDate}" pattern="MMMMM dd, yyyy" /></td>
											<td></td>
										</tr>
										<tr>
											<td></td>
											<td class="leftalign">${actionBean.invoice.depot.zip}</td>
											<td class="spacer"></td>
											<td class="leftalign blue">Due Date:</td>
											<td class="leftalign blue"><fmt:formatDate value="${actionBean.invoice.dueDate}" pattern="MMMMM dd, yyyy" /></td>
											<td></td>
										</tr>
									</table>
									
									<!-- Packages  -->
									<div id="packagetitle">Monthly Activity</div>
									<table id="packagestable" cellspacing="0" cellpadding="0">
										<thead>
											<tr class="header">
												<th>Custom Info</th>
												<th>Courier</th>
												<th>Delivery Date</th>
												<th>Pickup Date</th>
												<th>Receiving Fee</th>
												<th>Kinek Fee</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${actionBean.invoice.filteredPackageReceipts}" var="receipt" varStatus="loop">
												<c:forEach items="${receipt.packages}" var="package" varStatus="loop">
													<tr> 
														<td width="16%">${package.customInfo}</td>
														<td width="16%">${package.courier.name}</td>
														<td width="18%"><fmt:formatDate value="${receipt.receivedDate}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>
														<td width="18%"><fmt:formatDate value="${package.pickupDate.time}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>
														<td width="16%"><fmt:formatNumber value="${actionBean.invoice.depot.receivingFee}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
														<td width="16%"><fmt:formatNumber value="${actionBean.invoice.kinekFee}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
													</tr>	
												</c:forEach>
											</c:forEach>
										</tbody>
									</table>		
								
									<div id="totalsdiv">
									<!-- Totals -->
									<table id="totalstable">
										<tr>
											<td>Total Kinek Parcels Received</td>
											<td style="width: 105px;" class="spacer"></td>
											<td style="width: 105px;">${actionBean.invoice.packageCount}</td>
										</tr>
										<tr>
											<td>Sub-Total Fees</td> 
											<td><fmt:formatNumber value="${actionBean.invoice.feeReceivingTotal}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
											<td><fmt:formatNumber value="${actionBean.invoice.feeKinekTotal}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
										</tr>
										<tr>
											<td>Less Discounts</td>
											<td><fmt:formatNumber value="${actionBean.invoice.discountReceivingTotal}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
											<td><fmt:formatNumber value="${actionBean.invoice.discountKinekTotal}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
										</tr>
										<tr>
											<td>Total Revenue</td>
											<td><fmt:formatNumber value="${actionBean.invoice.revenueReceivingTotal}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
											<td><fmt:formatNumber value="${actionBean.invoice.revenueKinekTotal}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
										</tr>
										<tr>
											<td>Total Kinek Fees Owing</td>
											<td class="spacer"></td>
											<td><fmt:formatNumber value="${actionBean.invoice.revenueKinekTotal}" type="currency" minFractionDigits="2" maxFractionDigits="2" /></td>
										</tr>
									</table>
									</div>
									
		                        </td>
		                     </tr>
		                  </table>
		               </td>
		            </tr>
		         </table>	         
		      </td>
		   </tr>
		   <tr align="right">
		   	<td>		   	
			   	<s:form name="mainForm" action="">
					<s:hidden name="invoiceNumber" />
					<s:hidden name="invoiceNumbers" />
					<s:hidden name="checks" />
					<s:hidden name="month" />
					<s:hidden name="depot" />
					<s:hidden name="year" />
					
					<s:submit name="continueSend" value="Continue" class="button" style="margin-left: 0px; margin-bottom:10px; margin-right:25px;" />
				</s:form>		   		
		   	</td>
		   </tr>
		</table>
		</div>

		<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>

	</s:layout-component>
</s:layout-render>