<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
	<!-- NAV -->
	<ul id="nav" class="sf-menu clearfix">
					
		<!-- ADMINISTRATION -->
		<li class="parent">
			<a href="javascript:void();" title="Administration">Administration</a>
			<ul>
				<li>
					<a href="javascript:void();" title="Depot Profile">Depot Profile</a>
					<ul>
						<li><s:link beanclass="org.webdev.kpoint.action.FindDepotActionBean" title="View Depots">View Depots</s:link></li>
						<li>
							<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean" title="Create New Depot">Create New Depot
								<s:param name="action" value="Create" />
							</s:link>
						</li>
					</ul>
				</li>
				<li>
					<a href="javascript:void();" title="Manage Users">Manage Users</a>
					<ul>
						<li><s:link beanclass="org.webdev.kpoint.action.ManageUsersActionBean" title="View Users">View Users</s:link></li>
						<li><s:link beanclass="org.webdev.kpoint.action.AddUserActionBean" title="Create New User">Create New User</s:link></li>					</ul>					
				</li>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Marketing Materials">Marketing Materials
						<s:param name="action" value="marketing" />
					</s:link>
				</li>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Training Tutorials">Training Tutorials
						<s:param name="action" value="training" />
					</s:link>
				</li>
				<li>
					<a href="javascript:void();" title="Manage Organization">Manage Organization</a>
					<ul>	
						<li>
							<s:link beanclass="org.webdev.kpoint.action.ManageOrganizationActionBean" title="View Organization">View Organization
								<s:param name="action" value="View" />
							</s:link>
						</li>										
						<li>
							<s:link beanclass="org.webdev.kpoint.action.ManageOrganizationActionBean" title="Create Organization">Create Organization
								<s:param name="action" value="Create" />
							</s:link>
						</li>					
					</ul>
				</li>		
				
				<li>
					<a href="javascript:void();" title="Manage Coupons">Manage Coupons</a>
					<ul style="width:18em !important">										
						<li>
							<s:link beanclass="org.webdev.kpoint.action.ManageCouponsActionBean" title="Create KinekPoint Coupon">Create KinekPoint Coupon
								<s:param name="action" value="Create" />
								<s:param name="couponType" value="KP" />
							</s:link>
						</li>
						<li>
							<s:link beanclass="org.webdev.kpoint.action.ManageCouponsActionBean" title="Create Generic Coupon">Create Generic Coupon
								<s:param name="action" value="Create" />
								<s:param name="couponType" value="Generic" />
							</s:link>
						</li>											
					</ul>
				</li>				
				
			</ul>		
		</li>
		<!-- /ADMINISTRATION -->
		
		<!-- INVOICING -->
		<li class="parent">
			<a href="javascript:void();" title="Invoicing">Invoicing</a>
			<ul>
				<li><s:link beanclass="org.webdev.kpoint.action.SendInvoicesActionBean" title="Generate Invoices">Generate Invoices</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.InvoiceSearchActionBean" title="Invoice Search">Invoice Search</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.InvoiceReportActionBean" title="Invoice Report">Invoice Report</s:link></li>
			</ul>		
		</li>
		<!-- /INVOICING -->
		
		<!-- PROMOTIONS -->
		<li class="parent">
			<a href="javascript:void();" title="Promotion">Promotions</a>
			<ul>
				<li><s:link beanclass="org.webdev.kpoint.action.ManagePromotionsActionBean" title="Add Promotion">Add Promotion</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.ExportPromotionsActionBean" title="Export Promotions">Export Promotions</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.PromotionRedemptionReportActionBean" title="Credit Redemption Report">Redemption Report</s:link></li>
			</ul>		
		</li>
		<!-- /PROMOTIONS -->
		
		<!-- REPORTS -->
		<li class="parent">
			<a href="javascript:void();" title="Report">Reports</a>
				<ul>
					<li><s:link beanclass="org.webdev.kpoint.action.ConsumerActivityActionBean" title="Consumer Activity">Consumer Activity</s:link></li>
					<li><s:link beanclass="org.webdev.kpoint.action.KinekPointActivityActionBean" title="KinekPoint Activity">KinekPoint Activity</s:link></li>
					<li><s:link beanclass="org.webdev.kpoint.action.ParcelReportActionBean" title="Parcel Report">Parcel Report</s:link></li>
					<li><s:link beanclass="org.webdev.kpoint.action.UserReportActionBean" title="User Report">User Report</s:link></li>
					<li><s:link beanclass="org.webdev.kpoint.action.KinekPointChangeReportActionBean" title="KinekPoint Change Report">KinekPoint Change Report</s:link></li>
					<li><s:link beanclass="org.webdev.kpoint.action.OutboundMessagesReportActionBean" title="Outbound Message Report">Message Report</s:link></li>
					<li><s:link beanclass="org.webdev.kpoint.action.ReferralConversionReportActionBean" title="Referral Conversion Report">Referral Report</s:link></li>
					<li><s:link beanclass="org.webdev.kpoint.action.ExportSystemDataActionBean" title="System Data Report">System Data</s:link></li>
				</ul>
			</li>
		<!-- /REPORTS -->

	</ul>
	<!-- NAV -->