<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>



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
				
					<!-- STEPS -->
					<ul id="steps">
						<!--  Organization TITLE DECISION  -->
						<li class="active"><a href="#" title="Create New Coupon">Always Show Coupon Conflict</a></li>													
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">						
							<h1>Always Show Coupon Conflict</h1>	
						<!-- EDITDEPOTPROFILE STEP1 -->
							<fieldset>								
								<!-- /BLOCK -->								
								There are existing KP's that already have a Coupon specified as "Always Show"							
							</fieldset>						
						<!-- /EDITDEPOTPROFILE STEP1 -->
						
					</div>
					<!-- /STEPCONTAINER -->

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