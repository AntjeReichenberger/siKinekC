<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-definition>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">


		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<!-- INTERFACEWRAPPER -->
			<div id="interfaceWrapper">
				
				<!-- INTERFACE -->				
				<div id="interface">
					${contents}
            	</div>
				<!-- /INTERFACE -->
				
			</div>
			<!-- /INTERFACEWRAPPER -->
						
		</div>
		<!-- /CONTENT -->
		
	</div>
	<!-- /CONTENTWRAPPER -->
	
	<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
  </s:layout-component>
</s:layout-render>
</s:layout-definition>