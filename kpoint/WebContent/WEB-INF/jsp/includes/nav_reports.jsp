<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
	<!-- NAV -->
	<ul id="nav" class="sf-menu clearfix">
		<!-- ADMINISTRATION -->
		<li class="parent">
			<a href="javascript:void();" title="Administration">Administration</a>
			<ul>
				<li><s:link beanclass="org.webdev.kpoint.action.ParcelReportActionBean" title="Parcel Report">Parcel Report</s:link></li>
				<li><s:link beanclass="org.webdev.kpoint.action.UserReportActionBean" title="User Report">User Report</s:link></li>
			</ul>		
		</li>
		<!-- /ADMINISTRATION -->
	</ul>
	<!-- NAV -->