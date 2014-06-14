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
		<s:link beanclass="org.webdev.kpoint.action.depot.HowItWorksActionBean" title="Summary">Summary</s:link> / <s:link beanclass="org.webdev.kpoint.action.depot.HowItWorksVideoActionBean" title="Video Overview" class="here">Video Overview</s:link>
	</div>
	<!-- /TABS -->
	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
			
			<h1>How It Works</h1>
			
			<div id="howitworksVideoWrapper">
				<div id="howitworksVideo"><a target="new" href="http://www.youtube.com/watch?v=an_5GL5WPtI" title="How It Works Video"><img src="resource/images/howitworksvideo.jpg" alt="How It Works Video"/></a></div>
			</div>
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>
	
	