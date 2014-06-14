<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>


<s:layout-definition>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
			
			<!-- HELPPANEL -->
 			<div id="helpPanel" class="clearfix">
			
				<!-- HELPTEXT -->
				<div id="helpText" style="background-image: url(resource/samples/sample_helpgraphic.png);">
					<h3>Pick-Up</h3>
					<p>In order to process a pick-up from your depot, you must first find the customer's package using the search form(s) in step 1. Searching by the customer's Kinek# will show a list of all parcels at your location for that customer. If the Kinek# is missing or truncated, you may also search by first name, last name, state or country. You must then verify the customers ID by requesting Government Issue ID, which matches the addressing details listed on the Kinek account. Once customers ID has been verified choose the customer's name and then select Find Packages. Find packages will retrieve a list of packages available for pick-up. You then select the correct package(s), check to see if proof-of-purchase is required, note any taxable or non-taxable dues and complete the pick-up.</p>
				</div>
				<!-- /HELPTEXT -->
			
				<!-- CLOSEBTN -->
				<a href="javascript:void();" title="Show Instructional Video" id="helpPanel_closebtn"></a>
				<!-- /CLOSEBTN -->

			</div>
			<!-- /HELPPANEL -->
 
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