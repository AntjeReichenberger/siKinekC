<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/depot/layout.jsp">
	<s:layout-component name="pageTitle">Kinek</s:layout-component>
	
  	<s:layout-component name="body">
  	
  	<script type="text/javascript">
	// <![CDATA[
	       jQuery(document).ready(function(){
	               jQuery('#nav li:eq(2)').addClass('active');
	       });
	// ]]>
	</script>

	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
			
			<a name="top" id="top"></a>
			
			<h1>Frequently Asked Questions</h1>
			
			<!-- FAQ -->
			<div id="faq">
				
				<ul class="list bulleted">
					<li><a href="#faq1" title="Are there KinekPoint opportunities in my area?">Are there KinekPoint opportunities in my area?</a></li>
					<li><a href="#faq2" title="What types of businesses can become KinekPoints?">What types of businesses can become KinekPoints?</a></li>
					<li><a href="#faq3" title="How much does it cost to become a KinekPoint?">How much does it cost to become a KinekPoint?</a></li>
					<li><a href="#faq4" title="What fees are charged to customers?">What fees are charged to customers?</a></li>
					<li><a href="#faq5" title="Do KinekPoints accept COD packages?">Do KinekPoints accept COD packages?</a></li>
					<li><a href="#faq6" title="How do I go about becoming a KinekPoint?">How do I go about becoming a KinekPoint?</a></li>
					<li><a href="#faq7" title="How long is the process and what is involved?">How long is the process and what is involved?</a></li>
					<li><a href="#faq8" title="What other KinekPoints are operating in my area?">What other KinekPoints are operating in my area?</a></li>
				</ul>
				
				<a name="faq1" id="faq1"></a>
				<p><strong>Are there KinekPoint opportunities in my area?</strong><br />
				Kinek currently has opportunities for KinekPoints across North America with plans to expand the network globally. If you are a business operation and are interested in becoming a KinekPoint, please <s:link beanclass="org.webdev.kpoint.action.depot.BecomeAKinekPointActionBean" title="Become A KinekPoint">click here</s:link>. If you are located outside of North America and would like to receive information as Kinek expands into other countries, please <s:link beanclass="org.webdev.kpoint.action.depot.ContactActionBean" title="Contact Us">click here</s:link>. (<a href="#top" title="top">top</a>)</p>
				
				<br />
				
				<a name="faq2" id="faq2"></a>				
				<p><strong>What types of businesses can become KinekPoints?</strong><br />
				Almost any business has the potential to become a KinekPoint so long as it has established hours of operation, a computer with an internet connection, and an area in which deliveries can be securely stored. Examples of KinekPoints include convenience stores, retail locations, video outlets, and home-based businesses. (<a href="#top" title="top">top</a>)</p>
				
				<br />
				
				<a name="faq3" id="faq3"></a>
				<p><strong>How much does it cost to become a KinekPoint?</strong><br />
				There is no cost for signing up and inclusion in the KinekPoint network is free. You charge a fee for parcels entering your location and Kinek collects a portion of this fee based on your location. Kinek will send you a monthly email invoice via PayPal for fees incurred. (<a href="#top" title="top">top</a>)</p>
				
				<div class="clearfix">
					
					<!-- PayPal Logo --><a href="javascript:void(0);" onclick="javascript:window.open('https://www.paypal.com/us/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside','olcwhatispaypal','toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=400, height=350');" style="float: left; margin-left: 20px; border-bottom:none;"><img  src="https://www.paypal.com/en_US/i/bnr/bnr_paymentsBy_150x60.gif" border="0" alt="Additional Options"></a><!-- PayPal Logo -->				
					
				</div>
				
				<br />
				
				<a name="faq4" id="faq4"></a>
				<p><strong>What fees are charged to customers?</strong><br />
				Fees are set by individual KinekPoints. Each KinekPoint charges a fee to accept a parcel. While Kinek recommends a fee of $1.50-$2.50, it is up to the individual KinekPoint to determine how much to charge for the service. Factors to consider when selecting a fee include geographical location, available storage space, and the number of competing KinekPoints in the area.</p>
				<p>A KinekPoint may also choose to accept parcels that the government has charged with duty. In such cases, the depot pays the duty charge and collects the fee from the customer when they pick up their parcel. Depots are not obligated to accept duty parcels, however, they must note whether or not they accept duty parcels in KinekPoint Depot Management. (<a href="#top" title="top">top</a>)</p>
				
				<br />
				
				<a name="faq5" id="faq5"></a>
				<p><strong>Do KinekPoints accept COD packages?</strong><br />
				KinekPoints do not accept COD packages. (<a href="#top" title="top">top</a>)</p>
				
				<br />
				
				<a name="faq6" id="faq6"></a>
				<p><strong>How do I go about becoming a KinekPoint?</strong><br />
				To become a KinekPoint, please click here and fill out the resulting form. A Kinek representative will contact you within three business days. (<a href="#top" title="top">top</a>)</p>

				<br />
				
				<a name="faq7" id="faq7"></a>
				<p><strong>How long is the process and what is involved?</strong><br />
				Becoming a KinekPoint is simple. Once you have submitted your request, a Kinek representative will contact you and ask you a few questions about your business. As part of the process, a Kinek representative may visit your location. Once your request has been approved, you will be given a username and password that allows you access to KinekPoint Depot Management. You will also be provided with valuable resources to assist you with using and promoting the service. The length of this process may vary by location but is generally quite short. (<a href="#top" title="top">top</a>)</p>
				
				<br />
				
				<a name="faq8" id="faq8"></a>
				<p><strong>What other KinekPoints are operating in my area?</strong><br />
				To find KinekPoints in your area, visit <a href="${externalSettingsManager.consumerPortalBaseUrl}/Home.action" title="kinek.com">kinek.com</a> and typing your address in the "Search" field. (<a href="#top" title="top">top</a>)</p>
				
			</div>
			<!-- /FAQ -->	
						
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>
	
	