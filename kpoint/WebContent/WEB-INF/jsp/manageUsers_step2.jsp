<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/depot.jsp">
	<s:layout-component name="contents">

		<!-- STEPS -->
		<ul id="steps">
			<li><a href="#step1" title="Choose Depot"><span>Step 1:</span> Choose Depot</a></li>
			<li class="active"><a href="#step2" title="Manage Users"><span>Step 2:</span> Manage Users</a></li>
		</ul>
		<!-- /STEPS -->
		
		<!-- STEPCONTAINER -->
		<div id="stepContainer">
			
			<!-- STEP TITLE -->
			<h1><span>2</span> Manage Users</h1>
			<!-- /STEP TITLE -->
			
			<!-- ACCEPTDELIVERY STEP1 -->
			<s:form beanclass="org.webdev.kpoint.action.ManageUsersActionBean" >
				<fieldset>
				
					<s:errors/>
					<s:messages/>
					
					<!-- FORM CONTENT -->
					<div class="formContent">
						
						<c:if test="${actionBean.activeUser.adminAccessCheck}">
						<table><tr><td align="right"><a href="/kpoint/User.action?depotId=${actionBean.depotId}">Add User Account</a></td></tr></table>
						</c:if>
						
						<h2>Active Users</h2>
						<c:choose>
							<c:when test="${actionBean.activeUser.depotAdminAccessCheck}">
								<table class="superplain" style="width:650px">				
									<tr>	
										<td><label for="depotId" style="color:#666">Depot</label></td>							
										<td style="width:300px">
								  			<s:select name="depotId" id="depotId">
												<option value="">Please select a KinekPoint</option>
												<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
											</s:select>	
										</td>
										<td>
											<s:submit name="searchByDepotId" value="Search" class="button"></s:submit>
										</td>
									</tr>
								</table>
							</c:when>
						</c:choose>
						<table>
							<thead>
								<tr>
									<th width="15%">Username</th>
									<th width="20%">First Name</th>
									<th width="20%">Last Name</th>
									<th width="15%">Date Created</th>
									<th width="30%">Edit User Account</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${actionBean.activeUsers}" var="user" varStatus="loop">
								<tr>
									<td width="15%">${user.username}</td>
									<td width="20%">${user.firstName}</td>
									<td width="20%">${user.lastName}</td>
									<td width="15%"><fmt:formatDate value="${user.createdDate.time}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>		
									<td width="30%"><a href="/kpoint/User.action?userId=${user.userId}" title="Edit User Account">Edit User Account</a></td>
								</tr>	
							</c:forEach>
																																																																		
							</tbody>
						</table>
						
						<br /><br />
						
						<c:if test="${actionBean.inactiveUserCount > 0}">
						
						<h2>Inactive Users</h2>
						<table>
							<thead>
								<tr>
									<th width="15%">Username</th>
									<th width="20%">First Name</th>
									<th width="20%">Last Name</th>
									<th width="15%">Date Created</th>
									<th width="30%">Edit User Account</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${actionBean.inactiveUsers}" var="user" varStatus="loop">
								<tr>
									<td width="15%">${user.username}</td>
									<td width="20%">${user.firstName}</td>
									<td width="20%">${user.lastName}</td>
									<td width="15%"><fmt:formatDate value="${user.createdDate.time}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>		
									<td width="30%"><a href="/kpoint/User.action?userId=${user.userId}" title="Edit User Account">Edit User Account</a></td>
								</tr>	
							</c:forEach>
																																																																			
							</tbody>
						</table>
						</c:if>
					</div>
					<!-- /FORM CONTENT -->
					
				</fieldset>
			</s:form>
			<!-- /ACCEPTDELIVERY STEP1 -->
			
		</div>
		<!-- /STEPCONTAINER -->
	</s:layout-component>
</s:layout-render>