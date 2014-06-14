<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
	<!-- NAV -->
	<ul id="nav" class="sf-menu clearfix">

		<!-- HOME -->
		<li><a href="/kpoint/Login.action" title="Home">Home</a></li>
		<!-- /HOME -->

		<!-- DELIVERY -->
		<li class="parent">
			<a href="/kpoint/Deliver.action" title="Delivery">Delivery</a>
			<ul>
				<li><s:link beanclass="org.webdev.kpoint.action.DeliveryActionBean" title="Accept Delivery">Accept Delivery</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.RedirectActionBean" title="Redirect Delivery">Redirect Delivery</s:link></li>
			</ul>
		</li>
		<!-- /DELIVERY -->

		<!-- PICKUP -->
		<li><s:link beanclass="org.webdev.kpoint.action.PickupActionBean" title="Pick-Up">Pick-Up</s:link></li>
		<!-- /PICKUP -->

		<c:if test="${actionBean.activeUser.adminAccessCheck}">
					
		<!-- ADMINISTRATION -->
		<li class="parent">
			<a href="javascript:void();" title="Administration">Administration</a>
			<ul>
				<li>
					<a href="javascript:void();" title="Depot Profile">Depot Profile</a>
					<ul>
						<li><s:link beanclass="org.webdev.kpoint.action.ViewKinekPointActionBean" title="View Depot Profile">View Depot Profile</s:link></li>
						<li>
							<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean" title="Edit Depot Profile">Edit Depot Profile
								<s:param name="depotId" value="-2" />
							</s:link>
						</li>
					</ul>
				</li>
				<li>
					<a href="javascript:void();" title="Manage Users">Manage Users</a>
					<ul>
						<li><s:link beanclass="org.webdev.kpoint.action.ManageUsersActionBean" title="View Users">View Users</s:link></li>
						<li><s:link beanclass="org.webdev.kpoint.action.AddUserActionBean" title="Create New User">Create New User</s:link></li>
					</ul>					
				</li>
				<li><a href="/kpoint/" title="Marketing Materials">Marketing Materials</a></li>
				<li><a href="/kpoint/" title="Training Tutorials">Training Tutorials</a></li>
				<li><a href="/kpoint/ParcelReport.action" title="Parcel Report">Parcel Report</a></li>
			</ul>		
		</li>
		<!-- /ADMINISTRATION -->
		
		</c:if> 

	</ul>
	<!-- NAV -->