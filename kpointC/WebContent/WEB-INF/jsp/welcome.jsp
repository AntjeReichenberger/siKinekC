<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Welcome to Kinek</s:layout-component>

	<s:layout-component name="body">
	<link rel="stylesheet" href="resource/css/pages/welcome.css" type="text/css" media="screen,projection" />
  	
  	<script type="text/javascript">
	// <![CDATA[
	       jQuery(document).ready(function(){
	               jQuery('#nav li:eq(0)').addClass('active');
	       });
	// ]]>
	</script>

	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
			
			<h1>Welcome!</h1>
			<p>You are now a Kinek member.</p>
			
			<!-- WELCOME -->
			<div id="welcome" class="clearfix">
				
				<!-- HOWITWORKS -->
				<div id="howitworks">
					
					<img src="resource/samples/sample_howitworks_preview.jpg" alt="How it Works" width="410" height="250" />
					
					<h3>See How it Works</h3>
					<p>We want to show you how easy it is to use Kinek. Watch the tutorial to see how easy it is to eliminate missed deliveries.</p>
					<p><a href="HowItWorks.action" title="How it Works" class="button"><span>How it Works</span></a></p>
					
				</div>
				<!-- /HOWITWORKS -->
				
				<!-- DIVERIGHTIN -->
				<div id="diverightin">
					
					<img src="resource/samples/sample_search_preview.jpg" alt="Dive Right In" width="410" height="250" />
					
					<h3>Dive Right In</h3>
					<p>If you are already familiar with the Kinek service and want to get your KinekPoint address, then dive right in. Happy Shopping!</p>
					<p><a href="DepotSearch.action?criteria=${activeUser.city}" title="" class="button"><span>Dive Right In</span></a></p>
				
				</div>
				<!-- /DIVERIGHTIN -->
			
			</div>
			<!-- /WELCOME -->
			
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>