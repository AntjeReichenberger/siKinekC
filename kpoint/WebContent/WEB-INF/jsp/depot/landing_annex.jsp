<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/depot/layout.jsp">
	<s:layout-component name="pageTitle">Kinek</s:layout-component>
	
  	<s:layout-component name="body">
  	
  	<link rel="stylesheet" href="resource/css/depot/specialsAnnex.css" type="text/css"/>
  	
  	<!-- HOMEWRAPPER -->
	<div id="homeWrapper" class="clearfix">
	
		<!-- SPECIAL OFFER -->
		<div id="specialOfferBox" class="clearfix">
			<div  style="margin-left: 150px;" id="specialofferText">
				<h1>PARCEL RECEIVING FOR A FEE</h1>
				<p>We help drive customers in the door.</p>
			</div>
			<div id="specialofferDecoration">
				<div id="specialofferAction">
					<a href="${externalSettingsManager.depotPortalBaseUrl}/BecomeAKinekPoint.action" title="Sign Up Now!"></a>
				</div>
			</div>
			
		</div>
		<!-- /SPECIAL OFFER -->
		
		<!-- OFFER CONTENT -->
		<div id="offercontent" class="clearfix">
		
			<!-- HOW-IT-WORKS -->
			<div id="howitworks" class="clearfix">
				<h2>How it Works</h2>
				<p class="first">Jenny isn't home during the day. Through Kinek, she finds out that your location will accept and hold her parcel and that she'll automatically be notified, via email, when it arrives.</p>
				<p class="second">Not only do you receive a handling fee (which you control), you also have a guaranteed transaction with the possibility of additional sales. Offering the kinek service generates upside business for your location.</p>
			</div>
			<!-- /HOW-IT-WORKS -->

			<!-- WHY-IT-WORKS -->
			<div id="whyitworks" class="clearfix">
				<h2>Why it Works</h2>
				<p class="first">E-commerce is a $156B industry and e-commerce sales will account for an estimated 7% of overall retail revenue this year1. We help you turn online sales at popular retailers (like eBay and Amazon) into walk-in traffic for your location.</p>
				<p class="second">Consumers are wary about having parcels left unattended on their doorstep, especially with rising crime rates. </p>
				<p class="third">You set your own receiving fee. Kinek collects one dollar of this fee per package. We only get paid after you get paid.</p>
				<p class="fourth">It's free to sign up. All you need is an internet connection - no special equipment. </p>
				<p class="fifth">Free video training and free inclusion in Kinek search results.</p>
				<a href="${externalSettingsManager.depotPortalBaseUrl}/BecomeAKinekPoint.action" title="Sign Up Now!"></a>
			</div>
			<!-- /WHY-IT-WORKS -->
			<div style="clear:both;"></div>
			<!-- OFFER-NOTES -->
			<div id="offernotes" class="clearfix">
				<!--  <p><sup>*</sup> Offer applies to first 500 AMPC members who sign up by November 1, 2009.</p>-->
				<p><sup>1</sup> BizReport, Forrester: Growth forecast for 2009 online retail sales, January 30, 2009</p>
			</div>
			<!-- /OFFER-NOTES -->
		</div>
		<!-- /OFFER CONTENT -->
				
		<div style="clear:both;"></div>
				
	</div>
	<!-- /HOMEWRAPPER -->

  </s:layout-component>
</s:layout-render>
	
	