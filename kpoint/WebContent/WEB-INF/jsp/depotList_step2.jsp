<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/depotList.jsp">
	<s:layout-component name="contents">
		<!-- STEPS -->
		<ul id="steps">
			<li><a href="/kpoint/DepotList.action" title="Choose Depot"><span>Step 1:</span>Depot List</a></li>
			<li class="active"><a href="#step2" title="Manage Users"><span>Step 2:</span>Manage Users</a></li>
		</ul>
		<!-- /STEPS -->
		
		<!-- STEPCONTAINER -->
		<div id="stepContainer">
			<!-- ACCEPTDELIVERY STEP1 -->
			<s:form beanclass="org.webdev.kpoint.action.FindDepotActionBean" >
				<!-- STEP TITLE -->
				<h1><span>2</span> Manage Users</h1>
				<!-- /STEP TITLE -->
				
				<s:errors/>
				<s:messages/>
				
				<fieldset>
				
					<!-- FORM CONTENT -->
					<div class="formContent">
						
						<c:if test="${actionBean.activeUser.adminAccessCheck}">
						<table><tr><td align="right"><a href="/kpoint/User.action?depotId=${actionBean.depotId}">Add User Account</a></td></tr></table>
						</c:if>
						
						<h2>Active Users</h2>
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