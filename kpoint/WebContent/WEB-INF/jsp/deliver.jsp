<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-definition>
	<s:layout-render name="/WEB-INF/jsp/layout.jsp">
		<s:layout-component name="body">
			
			<!-- HELPPANEL -->
			<div id="helpPanel" class="clearfix" >
			
				<!-- HELPTEXT -->
				<div id="helpText" style="background-image: url(resource/samples/sample_helpgraphic.png);">
					<h3>Accepting a delivery</h3>
					<p>In order to accept a package for your depot, you must first locate the Kinek# on the shipping label of the delivered package. Then enter the Kinek# and search for the matching customer account. If the Kinek# is missing or truncated, you can also search by entering a partial Kinek#, first name, last name or phone#.
					Choose the potential customers that may match the details on the shipping label and select next. Then enter the courier information and optional fields if required. Optional fields can be used for your store's internal tracking.
					Select accept delivery and a notification will be sent to the customers advising them that their package is ready for pick-up at your location.
					If more than one customer is chosen, the system will advise all potential recipients that they may have received a package and will be required to present ID and proof-of-purchase in order to pick-up their package.
					</p>
				</div>
				
				<!-- /HELPTEXT -->
			
				<!-- CLOSEBTN -->
				<a href="javascript:void();" title="Show Instructional Video" id="helpPanel_closebtn"></a>
				<!-- /CLOSEBTN -->

			</div>
			<!-- /HELPPANEL -->

			<!-- CONTENTWRAPPER -->
			<div id="contentWrapper" class="clearfix">
			<%//@ include file="/WEB-INF/jsp/includes/sidebar.jsp"%>

			<!-- CONTENT -->
			<div id="content" class="clearfix"><!-- INTERFACEWRAPPER -->
			<div id="interfaceWrapper"><!-- INTERFACE -->
			
			<div id="interface">
				${contents}
			</div>
			<!-- /INTERFACE --></div>
			<!-- /INTERFACEWRAPPER --></div>
			<!-- /CONTENT --></div>
			<!-- /CONTENTWRAPPER -->

			<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>

		</s:layout-component>
	</s:layout-render>
</s:layout-definition>