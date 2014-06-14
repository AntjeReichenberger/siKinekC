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
					
				</s:form>
				
			</div>
		</div>

		<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>

	</s:layout-component>
</s:layout-render>