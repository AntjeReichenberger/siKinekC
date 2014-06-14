<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/depot/layout.jsp">
	<s:layout-component name="pageTitle">Kinek</s:layout-component>
	
  	<s:layout-component name="body">
  	
  	<script type="text/javascript">
	// <![CDATA[
	       jQuery(document).ready(function(){
	               jQuery('#nav li:eq(1)').addClass('active');
	       });
	// ]]>
	</script>

	<!-- TABS -->
	<div id="tabs">
		<s:link beanclass="org.webdev.kpoint.action.depot.HowItWorksActionBean" title="Summary" class="here">Summary</s:link> / <s:link beanclass="org.webdev.kpoint.action.depot.HowItWorksVideoActionBean" title="Video Overview">Video Overview</s:link>
	</div>
	<!-- /TABS -->
	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
			
			<h1>How It Works</h1>
			
			<!-- HOWITWORKS FLASH -->
			<div id="howitworksFlash">
				<img src="resource/flash/flash_howitworks.jpg" alt="How It Works" width="861" height="241"/>
			</div>	
			<!-- /HOWITWORKS FLASH -->
			
			<h2>Service Overview</h2>
			
			<div class="first twocolumn">

				<p>Kinek provides an opportunity for your business to generate additional revenue and traffic. By becoming a KinekPoint you can benefit from the additional foot-traffic the service can bring. Every additional customer is a target for further sales and promotions.</p>
				<p>Each KinekPoint accepts customer parcels from couriers and enters them into our web-based application. All that is needed to run the service is space to store parcels, reliable staff, and an active internet connection. A Kinek# that uniquely identifies each customer is printed on the address label of each package you receive. A staff member enters the Kinek# and the customer instantly receives an email notification alerting them that their parcel is ready for pick up.</p>

			</div>
			<div class="twocolumn">

				<p>Kinek is a flexible service. You control your own features and set the price charged for each package sent to your location. You also control the size of parcels you accept. Users can search for your business via our Google Maps interface and view profile information (including fees and services) for your location.</p>
				<p>Think it may to be too technical or hard to learn? Think again! Training videos teach you the entire service in minutes. Your employees can access these videos at each step throughout the application. The average time to accept a parcel is 30-40 seconds.</p>
				<p>Access to the Kinek web-based application is free and there are no additional set up costs. Kinek charges a nominal fee for each package that is sent to your location (billed monthly via PayPal) regardless of what you charge your end customer. You keep the markup and benefit from additional point of sale purchases.</p>

			</div>
			
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>
	
	