<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/depot/layout.jsp">
	<s:layout-component name="pageTitle">Kinek</s:layout-component>
	
  	<s:layout-component name="body">
  	
  	<script type="text/javascript">
	// <![CDATA[
	       jQuery(document).ready(function(){
	               jQuery('#nav li:eq(4)').addClass('active');
	       });
	// ]]>
	</script>

	<style type="text/css">
		.continue
		{
			background:transparent url(resource/images/button_enabled.jpg) no-repeat scroll left center;
			height: 42px;
			width: 200px;
			text-align: center;
			padding-top: 25px;
			display: inline-block;
		}
		
		#continue
		{
			color: white;
			border-style: none;
			font-size: 15px;
		}
		
	</style>

	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
			
			<a name="top" id="top"></a>
			
			<p></p><b>Thank you for your application!</b></p>
			
			<p>
				We personally take time to review every application, ensuring that each new location benefits from being part of a respected and top-notch network.
			</p>

			<p>
				A Kinek representative will review the information you provided and contact you by email within two business days to let you know whether or not your application has been approved.  If you do not see this email in your inbox, please check your spam/junk folder.
			</p>
			
			<p>
				If you have any questions, please browse our 
				<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Frequently Asked Questions"><s:param name="action">FAQ</s:param>Frequently Asked Questions</s:link> or 
				<s:link beanclass="org.webdev.kpoint.action.depot.ContactActionBean" title="Contact Us">Contact Us</s:link> page.
			</p>
			
			<p>
				Thank you for your interest in Kinek!
			</p>
			
			<s:link beanclass="org.webdev.kpoint.action.depot.HomeActionBean" title="Continue" id="continue">
				<div class="continue">Continue</div>
			</s:link>				
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>
	
	