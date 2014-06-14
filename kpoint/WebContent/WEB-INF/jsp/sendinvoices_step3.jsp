<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">

		<div id="contentWrapper" class="clearfix">
			<div id="content" class="wide clearfix">
			
				<s:form beanclass="org.webdev.kpoint.action.SendInvoicesActionBean">
				
					<h1>Send Invoices</h1>
					
					<s:messages/>
					<s:errors/>
			
					<div style="margin-bottom: 40px;">
						<h2>The invoice process has been successfully completed.</h2>
					</div>

					<s:submit name="success" value="Continue" class="button" style="margin-left: 0px;" />
					
				</s:form>
				
			</div>
		</div>

		<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>

	</s:layout-component>
</s:layout-render>