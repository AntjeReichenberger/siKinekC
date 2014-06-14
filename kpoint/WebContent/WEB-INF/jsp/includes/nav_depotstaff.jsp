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
		<li class="parent"><s:link beanclass="org.webdev.kpoint.action.PickupActionBean" title="Pick-Up">Pick-Up</s:link></li>
		<!-- /PICKUP -->

	</ul>
	<!-- NAV -->