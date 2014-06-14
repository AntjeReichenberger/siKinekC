<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-definition>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
  
  			<!-- HELPPANEL -->
 			<div id="helpPanel" class="clearfix">
			
				<!-- HELPTEXT -->
				<div id="helpText" style="background-image: url(resource/samples/sample_helpgraphic.png);">
					<h3>Redirect a delivery</h3>
					<p>In order to redirect a delivery, you must fill out the required fields in the form below. Select the name of the courier that delivered the package from the list of available options. Once you have selected the courier, enter the location the courier will be taking the package to (most couriers provide this information on a missed delivery notice. Next, select the reason as to why you are redirecting the package from the drop down menu. Lastly, make sure you ask for the Kinek# off the package before the courier leaves. Without the Kinek# you will have to use the unknown Kinek# search option found at the bottom of the accept delivery page.<br /><small>Please Note: without a Kinek# or name, it will be impossible to notify the customer that their package has been redirected.</small></p>
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