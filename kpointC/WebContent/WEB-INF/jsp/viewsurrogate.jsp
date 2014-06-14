<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
		<div class="clearfix"></div>
		<div id="adbModule">	
			<!-- view Surrogate -->
			<s:errors />
			<s:messages />
			View Surrogate
			<!-- //view Surrogate -->
			<s:form name="viewSurrogateFrm" action="/Surrogate.action">
				<s:submit name="gotoCreateSurrogate" value="Add Surrogate" class="button"></s:submit>				
				<s:submit name="deleteSurrogate" value="Delete Surrogate" class="button"></s:submit>
				<s:hidden name="action"></s:hidden>
			</s:form>	
		</div>	
	</s:layout-component>
</s:layout-render>