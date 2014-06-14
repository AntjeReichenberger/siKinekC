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
						<h1>KinekPoint Change Report</h1>
						<!-- /STEP TITLE -->
					
						<!-- KinekPoint Change Report -->
						<s:form beanclass="org.webdev.kpoint.action.KinekPointChangeReportActionBean">
							
							<s:submit name="export" value="Export" class="floatRight button" style="margin:-50px 15px 0 0;" />
						
							<!-- FORM CONTENT -->
							<div class="formContent">
									<table>
										<thead>
											<tr>
												<th>Depot Name</th>
												<th>Date of Change</th>
												<th>Type of Change</th>
												<th>Changes Made</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach items="${actionBean.depotHistory}" var="history" varStatus="loop">
											<tr>
												<td><a href="/kpoint/ViewDepot.action?depotId=${history.depotId}" title="View Depot">${history.name}</a></td>
												<td><fmt:formatDate value="${history.changedDate.time}" pattern="MMMMM dd, yyyy"/></td>
												<td>${history.typeOfChange}</td>
												<td>${history.changesMade}</td>
											</tr>
										</c:forEach>
										</tbody>
									</table>
								
							</div>
							<!-- /FORM CONTENT -->
						
						</s:form>
						<!-- /KinekPoint Change Report -->
					
					</div>
					<!-- /STEPCONTAINER -->
					
				</div>
				<!-- /INTERFACE -->
				
			</div>
			<!-- /CONTENT -->
			
		</div>
		<!-- /CONTENTWRAPPER -->
		
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
	</s:layout-component>
</s:layout-render>