<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
	<!-- NAV -->
	<ul id="nav" class="sf-menu clearfix">

		<!-- DELIVERY -->
		<li class="parent">
			<s:link beanclass="org.webdev.kpoint.action.DeliveryActionBean" title="Delivery">Delivery</s:link>
			<ul>
				<li><s:link beanclass="org.webdev.kpoint.action.DeliveryActionBean" title="Accept Delivery">Accept Delivery</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.RedirectActionBean" title="Redirect Delivery">Redirect Delivery</s:link></li>
			</ul>
		</li>
		<!-- /DELIVERY -->

		<!-- PICKUP -->
		<li><s:link beanclass="org.webdev.kpoint.action.PickupActionBean" title="Pick-Up">Pick-Up</s:link></li>
		<!-- /PICKUP -->

					
		<!-- ADMINISTRATION -->
		<li class="parent">
			<a href="javascript:void();" title="Administration">Administration</a>
			<ul>
				<li>
					<a href="javascript:void();" title="Depot Profile">Depot Profile</a>
					<ul>
						<li><s:link beanclass="org.webdev.kpoint.action.ViewKinekPointActionBean" title="View Depot Profiles">View Depot Profiles</s:link></li>
					</ul>
				</li>
				<li>
					<a href="javascript:void();" title="Manage Users">Manage Users</a>
					<ul>
						<li><s:link beanclass="org.webdev.kpoint.action.ManageUsersActionBean" title="View Users">View Users</s:link></li>
						<li><s:link beanclass="org.webdev.kpoint.action.AddUserActionBean" title="Create New User">Create New User</s:link></li>
					</ul>					
				</li>				 
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" target="_blank" title="Marketing Materials">Marketing Materials
						<s:param name="action" value="marketing" />
					</s:link>
				</li>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Training Tutorials">Training Tutorials
						<s:param name="action" value="training" />
					</s:link>
				</li>
			</ul>		
		</li>
		<!-- /ADMINISTRATION -->
		
		<!--  REPORTS  -->
		<li class="parent">
			<a href="javascript:void();" title="Report">Reports</a>
			<ul>
			<li><s:link beanclass="org.webdev.kpoint.action.ParcelReportActionBean" title="Parcel Report">Parcel Report</s:link></li>
			<li><s:link beanclass="org.webdev.kpoint.action.AccountStatusReportActionBean" title="Account Status ">Account Status</s:link></li>
			</ul>
		</li>		

	</ul>
	<!-- NAV -->