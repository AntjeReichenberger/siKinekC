<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
  	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">

		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<!-- INTERFACEWRAPPER -->
			<div id="interfaceWrapper">
				
				<!-- INTERFACE -->				
				<div id="interface">
					
					<div id="stepContainer">
				
						<!-- STEP TITLE -->
						<h1>User Report</h1>
						<!-- /STEP TITLE -->
						
						<!-- PARCELREPORT -->

						<s:form beanclass="org.webdev.kpoint.action.UserReportActionBean">
							<fieldset>
								
								<!-- BLOCK -->
								<ol class="clearfix">
									<li>
										<s:label for="year">Year</s:label><br />
										<s:select name="year" id="year" style="width: 120px; margin-right: 30px;">
											<s:option value="">All</s:option>
											<s:options-collection collection="${actionBean.years}" />
										</s:select>
										<s:link beanclass="org.webdev.kpoint.action.UserReportActionBean" style="vertical-align: middle;">Reset</s:link>
										<span class="separator">|</span>
										<s:submit name="search" class="button" value="View Report" style="margin: 10px 0px 10px 10px;" />
									</li>									
								</ol>
								<!-- /BLOCK -->
								
								<hr/>
								
								<!-- FORM CONTENT -->
								<div class="formContent">
									<c:forEach items="${actionBean.userReportData}" var="reportYear" varStatus="loop">
									<table>
										<thead>
											<tr>
												<th>Year</th>
												<th>Month</th>
												<th>Users</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td rowspan="12">${reportYear.year}</td>
												<td>January</td>
												<td>${reportYear.totalUsersByMonth[0]}</td>
											</tr>
											<tr>
												<td>February</td>
												<td>${reportYear.totalUsersByMonth[1]}</td>
											</tr>
											<tr>
												<td>March</td>
												<td>${reportYear.totalUsersByMonth[2]}</td>
											</tr>
											<tr>
												<td>April</td>
												<td>${reportYear.totalUsersByMonth[3]}</td>
											</tr>
											<tr>
												<td>May</td>
												<td>${reportYear.totalUsersByMonth[4]}</td>
											</tr>
											<tr>
												<td>June</td>
												<td>${reportYear.totalUsersByMonth[5]}</td>
											</tr>
											<tr>
												<td>July</td>
												<td>${reportYear.totalUsersByMonth[6]}</td>
											</tr>
											<tr>
												<td>August</td>
												<td>${reportYear.totalUsersByMonth[7]}</td>
											</tr>
											<tr>
												<td>September</td>
												<td>${reportYear.totalUsersByMonth[8]}</td>
											</tr>
											<tr>
												<td>October</td>
												<td>${reportYear.totalUsersByMonth[9]}</td>
											</tr>
											<tr>
												<td>November</td>
												<td>${reportYear.totalUsersByMonth[10]}</td>
											</tr>
											<tr>
												<td>December</td>
												<td>${reportYear.totalUsersByMonth[11]}</td>
											</tr>
											<tr>
												<td colspan="2">Total Users</td>
												<td>${reportYear.totalUsers}</td>
											</tr>
										</tbody>
									</table>
									</c:forEach>
								</div>
								<!-- /FORM CONTENT -->
								
							</fieldset>

						</s:form>
						<!-- /PARCELREPORT -->

					</div>
              	 
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