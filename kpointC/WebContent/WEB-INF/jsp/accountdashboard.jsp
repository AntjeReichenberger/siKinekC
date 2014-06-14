<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-definition>
	<s:layout-render name="/WEB-INF/jsp/layout.jsp">
		<s:layout-component name="pageTitle">My Kinek</s:layout-component>
	  	
	  	<s:layout-component name="body">
	  	
		<script type="text/javascript">
		// <![CDATA[
	       jQuery(document).ready(function(){
		       if ("${actionBean.showAllTabs}" == "true") {
               		jQuery('#nav li:eq(4)').addClass('active');
		       }
               else {
              		jQuery('#nav li:eq(0)').addClass('active');
               }
	       });
		// ]]>
		</script>
		  	
	  	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard.css" type="text/css" />
		
		<div id="contentWrapper">			
			<div id="consumerKinekPointAddress">
				<table class="superplain defaultKinekPoint">
					<tr>
						<td class="kinekNumberLabel" colspan="2" style="width:225px !important;">My Kinek Number: <span class="kinekNumberValue">#${actionBean.kinekNumber}</span><img src="resource/images/help_icon.png" onmouseover="openInfoDialog('kinekNumberInfoPopUpDiv', this)" onmouseout="closeInfoDialog('kinekNumberInfoPopUpDiv')"/></td>
					</tr>
					<tr>
						<td class="kinekPoint" colspan="2"><span>My Favorite KinekPoint</span><img src="resource/icons/20-star_fill.png"/><img src="resource/images/help_icon.png" onmouseover="openInfoDialog('shippingAddressInfoPopUpDiv', this)" onmouseout="closeInfoDialog('shippingAddressInfoPopUpDiv')"/></td>
					</tr>
					
					<tr><td class="addressSize">${actionBean.consumerFullName}</td></tr>				
					<tr><td class="addressSize">${actionBean.kinekPoint.address1}, #${actionBean.kinekNumber}</td></tr>
					<tr><td class="addressSize">${actionBean.kinekPoint.name}</td></tr>
					<tr><td class="addressSize">${actionBean.kinekPoint.city}, ${actionBean.kinekPoint.state.stateProvCode}</td></tr>
					<tr><td class="addressSize">${actionBean.kinekPoint.zip}</td></tr>
					<tr><td class="addressSize"><div id="hoursPricingLink">
						<s:link beanclass="org.webdev.kpoint.action.KinekPointDetailsActionBean">View Hours &amp; Pricing
							<s:param name="depotId" value="${actionBean.kinekPoint.depotId}"></s:param>									
						</s:link>
					</div></td></tr>
					
				</table>
			</div>
			
			<div id="adb_nav" class="superplain">
				<table>
						<tr>
							<td class="title" style="width:125px">My KinekPoints</td>
							<td class="title">Package History</td>
							<td class="title" id="accountSettingsTitle">Account Settings</td>
						</tr>
						<tr>
							<td>
								<table id="kpDetails">
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.MyKinekPointsActionBean">View All My KinekPoints</s:link>
									</td></tr>
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.ChooseDefaultKinekPointActionBean">Add Another KinekPoint</s:link>&nbsp;<img src="resource/icons/badge.png"/>
									</td></tr>
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.MyKinekPointsActionBean">Remove a KinekPoint</s:link>
									</td></tr>										
								</table>
							</td>
							<td>
								<table id="packages">
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.MyParcelsActionBean" event="fetchParcels">Pending Pick-up
											<s:param name="packageStatus" value="Pending Pick-up"/>
										</s:link>
									</td></tr>
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.MyParcelsActionBean" event="fetchParcels">Picked-up
											<s:param name="packageStatus" value="Picked-Up"/>
										</s:link>
									</td></tr>
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.MyParcelsActionBean" event="fetchParcels">Redirected
											<s:param name="packageStatus" value="Redirected"/>
										</s:link>
									</td></tr>
									<tr>
										<td>
											<s:link beanclass="org.webdev.kpoint.action.TrackingActionBean">Tracking
												<s:param name="action" value="View"></s:param>
											</s:link>&nbsp;<img src="resource/icons/badge.png"/>
										</td>
									</tr>
								</table>
							</td>
							<td>
								<table id="accountSettings">
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.MyProfileActionBean">Change my Profile</s:link>
									</td></tr>
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.PasswordActionBean">Change my Password</s:link>
									</td></tr>
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.SMSContactInfoActionBean">Text your Kinek Info</s:link>
									</td></tr>
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.EmailContactInfoActionBean">Email your Kinek Info</s:link>
									</td></tr>
									<tr><td>
										<s:link beanclass="org.webdev.kpoint.action.NotificationsActionBean">Notifications</s:link>&nbsp;<img src="resource/icons/badge.png"/>
									</td></tr>
								</table>
							</td>
						</tr>
				</table>	
			</div>
			
			<div id="kinekNumberInfoPopUpDiv" title="My Kinek Number" class="hidden infoPopUpDiv" >
				This is your unique Kinek number, we use it to identify your packages when they arrive. Be sure to include it in the shipping address of each order.  This number will always stay the same.
			</div>
		
			<div id="shippingAddressInfoPopUpDiv" title="My KinekPoint Shipping Address" class="hidden infoPopUpDiv">
				This is your KinekPoint shipping address. Use this address when ordering online to receive a package at this KinekPoint. You can change your KinekPoint at any time by adding another to your My KinekPoints list.
			</div>
			
			<div class="horizontalSeperator"></div>
			
			<div id="content" class="clearfix">
				${contents}
			</div>
		</div>
	  </s:layout-component>
	</s:layout-render>
</s:layout-definition>
