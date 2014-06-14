<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-definition>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard-smscontactinfo.css" type="text/css" />
	
	<br class="clear" />
	
	<div id="adbModule">
		<div id="pageTitle">
			<h1>Text your Kinek Info</h1>
			<br />
		</div>
			
		${smscontactinfo}	
	</div>
</s:layout-component>
</s:layout-render>
</s:layout-definition>