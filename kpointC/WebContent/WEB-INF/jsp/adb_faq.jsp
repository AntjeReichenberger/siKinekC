<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
  	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard-faq.css" type="text/css" media="screen,projection" />
	
	<script type="text/javascript">
	var delay = 0;
	jQuery(document).ready(function() {
		 		jQuery('#1')
			    .click(function(event) {
			    	event.preventDefault();
			    	jQuery('#common').hide(delay);
			    	jQuery('#pricinghours').hide(delay);
			    	jQuery('#orderingOnline').hide(delay);
			    	jQuery('#myKinekPoint').hide(delay);
			    	jQuery('#packagesQ').hide(delay);
			    	jQuery('#border').show(delay);
			    });
		 		jQuery('#2')
		 		.click(function(event) {
			    	event.preventDefault();
			    	jQuery('#common').hide(delay);
			    	jQuery('#border').hide(delay);
			    	jQuery('#orderingOnline').hide(delay);
			    	jQuery('#myKinekPoint').hide(delay);
			    	jQuery('#packagesQ').hide(delay);
			    	jQuery('#pricinghours').show(delay);
			    });
		 		jQuery('#3')
		 		.click(function(event) {
			    	event.preventDefault();
			    	jQuery('#common').hide(delay);
			    	jQuery('#border').hide(delay);
			    	jQuery('#pricinghours').hide(delay);
			    	jQuery('#myKinekPoint').hide(delay);
			    	jQuery('#packagesQ').hide(delay);
			    	jQuery('#orderingOnline').show(delay);
			    });
		 		jQuery('#4')
		 		.click(function(event) {
			    	event.preventDefault();
			    	jQuery('#common').hide(delay);
			     	jQuery('#border').hide(delay);
			    	jQuery('#pricinghours').hide(delay);
			    	jQuery('#orderingOnline').hide(delay);		    	
			    	jQuery('#packagesQ').hide(delay);
			    	jQuery('#myKinekPoint').show(delay);
			    });
		 		jQuery('#5')
		 		.click(function(event) {
			    	event.preventDefault();
			    	jQuery('#common').hide(delay);
			    	jQuery('#border').hide(delay);
			    	jQuery('#pricinghours').hide(delay);
			    	jQuery('#orderingOnline').hide(delay);
			    	jQuery('#myKinekPoint').hide(delay);
			    	jQuery('#packagesQ').show(delay);			    	
			    });
			 });
	</script>
	
	<br class="clear" />
		
	<div id="adbModule">
			
		<a name="top" id="top"></a>
		
		<!-- STEP TITLE -->
		<h1>Frequently Asked Questions</h1>
		<!-- /STEP TITLE -->
				
		<div id="categories">
		<ul>
			<li id="1"><img title="Shipping to the Border" src="/kpointC/resource/images/faq_ShippingToBorder.jpg" alt="Shipping to the border" width="86" height="86" /></li>
			<li id="2"><img title="Kinek Pricing and hours" src="/kpointC/resource/images/faq_PricingHours.jpg" alt="Pricing and hours" width="86" height="86" /></li>
			<li id="3"><img title="Ordering Online and sending to KinekPoint" src="/kpointC/resource/images/faq_OrderingOnline.jpg" alt="Ordering Online" width="86" height="86" /></li>
			<li id="4"><img title="My KinekPoint" src="/kpointC/resource/images/faq_MyKinekPoint.jpg" alt="KinekPoint questions" width="86" height="86" /></li>
			<li id="5"><img title="Questions about picking up packages" src="/kpointC/resource/images/faq_PickingUpPackages.jpg" alt="Picking up packages" width="86" height="86" /></li>
		</ul>
		</div>
		
		<br />
			
		<div id="common">
			<p class="faqitem"><strong>How much does it cost to use the Kinek service?</strong><br/>
			Each KinekPoint has a per package fee shown on their profile. There are no monthly or sign up fees to use Kinek. You can simply search a KinekPoint on the Find a KinekPoint tab and select a KinekPoint from the map. Once selected, a pop up window will show you the KinekPoints lowest rate. You can also sign up for free to view the KinekPoint(s) rates by weight.</p>
			<p class="faqitem"><strong>What about Duties and Taxes?</strong><br/>
			Kinek does not collect duties and taxes on packages received at our border KinekPoints. It is the responsibility of the customer to claim their items when crossing the border and pay the appropriate fees. All products, including tobacco, face duties and taxes if you have been out of the country for less than 48-hours.</p>
			<p class="faqitem"><strong>How long do I have until I need to pick-up my package?</strong><br/>
			You have 30-days to pick-up your package from a KinekPoint. In some circumstances, extended storage can be arranged for an additional fee. For full pick-up information, please visit your KinekPoints profile page by using our map function and then clicking on their location.</p>
		</div>
		
		<div id="border" style="display:none">
		<p class="faqitem"><strong>What do I need in order to cross into the United States?</strong><br/>
		In order to pick up your packages you will need to cross the border. It is required that you have a passport (or a Nexus pass at Nexus enabled crossings).</p>
		
		<p class="faqitem"><strong>What should I have when crossing back into Canada?</strong><br/>
		You should ensure that you have the order confirmation from the retailer (that shows the value of the item) with you when bringing your purchases back into Canada. You will need the order confirmation to determine how much money, if any, in fees you owe border services.</p>
		
		<p class="faqitem"><strong>What fees will I have to pay at the border?</strong><br/>
		When crossing back into Canada keep in mind that there are fees you may have to pay. Usually you will have to pay the tax of the province you're crossing back into (e.g., Ontario is 13%) you may also be required to pay duty on your items depending on what they are and where they were manufactured. By shipping to a border KinekPoint, you may avoid international shipping and brokerage fees, which are typically much greater than tax and duty.</p>
		
		<p class="faqitem"><strong>Which border location is right for you?</strong><br/>
		To make it easy, we have broken down our locations by the Canadian cities/regions
		they serve. Simply click on the name of the KinekPoint to view their profile
		page and sign-up!</p>
		
		<div class="locationlistwrapper">
		<ul>
			<li><strong>Montreal:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Champlain, New York
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=973">Bay Brokerage</a> (can accept any size or weight packages)</li>
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=345">Border Mail Services</a></li>
		</ul>
		</li>
			<li>Swanton, VT
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=1062">Business America Services</a> (can accept any size or weight packages)</li>
		</ul>
		</li>
		</ul>
		</li>
			<li><strong>Sherbrooke:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Derby Line, Vermont
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=802">Norman G. Jensens</a> (can accept any size or weight packages)</li>
		</ul>
		</li>
		</ul>
		</li>
			<li><strong>Ottawa region, Brockville:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Ogdensburg, New York (only 45 minutes from Ottawa)
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=1005">NAC Logistics</a> (can accept any size or weight)</li>
		</ul>
		</li>
		</ul>
		</li>
			<li><strong>Kingston, Belleville, Gananoque, Napanee:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Wellesley Island, New York (35 minutes from Kingston)
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=803">Bay Brokerage</a>	(can accept any size or weight)</li>
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=842">Wellesley	Island Building Supply</a> (can accept any size or weight)</li>
		</ul>
		</li>
		</ul>
		</li>
		<li><strong>Greater Toronto Area:</strong>
			<ul class="faqBorderMapUSACity">
				<li>Niagara Falls, New York
					<ul class="faqBorderMapDepotList">
						<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=861">Bay Brokerage</a>	(can accept any size or weight)</li>
						<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=850">UPS Store</a></li>
					</ul>
				</li>
			</ul>
			<ul class="faqBorderMapUSACity">
				<li>Buffalo, New York
					<ul class="faqBorderMapDepotList">
						<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=1143">Disc and Belleville</a></li>
					</ul>
				</li>
			</ul>
		</li>
		
		<li><strong>Windsor:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Detroit, Michigan
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=824">Computing	Express</a></li>
		</ul>
		</li>
		</ul>
		</li>
			<li><strong>London/Sarnia:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Port Huron
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=882">Bay Brokerage</a>	(can accept any size or weight)</li>
		</ul>
		</li>
			<li>Marine City, Michigan
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=148">FB Package and Shipping</a></li>
		</ul>
		</li>
		</ul>
		</li>
			<li><strong>Sault Ste. Marie:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Sault Ste. Marie, Michigan
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=875">Pete's Appliance</a> (can accept any size or weight packages)</li>
		</ul>
		</li>
		</ul>
		</li>
			<li><strong>Winnipeg:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Pembina, North Dakota
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=849">Red River	Repair</a> (can accept any size or weight packages)</li>
		</ul>
		</li>
		</ul>
		</li>
		<li><strong>Kelowna/Blaine/Osoyoos, British Columbia:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Washington
				<ul class="faqBorderMapDepotList">
					<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=938">Appleway Video</a></li>
					<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=1089">Edge Logistics</a></li>
				</ul>
			</li>
		</ul>
		</li>
			<li><strong>New Brunswick/Nova Scotia/PEI:</strong>
		<ul class="faqBorderMapUSACity">
			<li>Calais, Maine
		<ul class="faqBorderMapDepotList">
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=767">Calais Ace Homecenter</a> (can accept any size or weight packages)</li>
			<li><a href="/kpointC/ChooseDefaultKinekPoint.action?depotId=653">AN Deringer</a> (can accept any size or weight packages)</li>
		</ul>
		</li>
		</ul>
		</li>
		</ul>
		</div>
		<!-- Border KinekPoint static locator to display markers on the border area -->
		(<a title="top" href="#top">top</a>)<br/><br/>
		
		<p class="faqitem"><strong>I want to use one of Kinek's border KinekPoints as
		my US shipping address, which one is closest to me?</strong><br/>
		Kinek has a network of KinekPoints all along the US/Canadian border that will
		let you receive packages from US companies. In order to find one of these locations
		and save it as your default KinekPoint, simply sign-up, complete your profile,
		and then search the name of the border city you would like to ship to. For instance,
		if you live in Toronto, you could search for "Buffalo, New York" or "Port Huron,
		Michigan". The map would then display the KinekPoints we have in the area. Simply
		click on the location and then click the "Add this KinekPoint" button.
		(<a title="top" href="#top">top</a>)</p>
		
		<p class="faqitem"><strong>I don't see any KinekPoints around me when I search
		the map</strong><br/>
		Simply send us an email to support@kinek.zendesk.com and we will work on acquiring a KinekPoint in your area.
		<br /><br />
		1) If you're Canadian, check to see if there is a KinekPoint in a border city
		near you. If there is, you can use that location as your US shipping address
		to save hundreds of dollars when shopping online by shipping packages there
		from US retailers. No more brokerage or international shipping fees!
		<br /><br />
		2) You can also use our nationwide network of KinekPoints when you're travelling.
		For instance, if you're going to be in New York for meetings, use one of our
		local KinekPoints to receive an online order while you're there. Going to San
		Francisco for vacation? Use one of our KinekPoints there to receive and hold
		a package for you. (<a title="top" href="#top">top</a>)</p>
		
		<p class="faqitem"><strong>What about Duties and Taxes?</strong><br/>
		Kinek does not collect duties and taxes on packages received at our border KinekPoints.
		It is the responsibility of the customer to claim their items when crossing
		the border and pay the appropriate fees. All products, including tobacco, face
		duties and taxes if you have been out of the country for less than 48-hours. For more information about duties and taxes on items you bring across the border please contact Border Information Services at 1 (800) 461-9999. (<a title="top" href="#top">top</a>)</p>
		
		<p class="faqitem"><strong>Your personal exemption limits are<sup>*</sup>:</strong><br/>
		<strong class="nohighlight">Out of the country for 24 hours:</strong>
		<br />
		You can	claim up to $50 without paying any duties. <strong class="nohighlight">IMPORTANT</strong> - you
		cannot include tobacco or alcohol products in this exemption.
		<br />
		<strong class="nohighlight">Out of the country for 48 hours or more:</strong>
		<br />
		You can claim up to $400 without paying any duties. For tobacco products, you
		can claim up to 200 cigarettes, 50 cigars or cigarillos; and 200g (7 oz.) of
		manufactured tobacco.
		<br />
		<strong class="nohighlight">Out of the country for 7 days or more:</strong>
		<br />
		You can claim up to $750 without paying any duties. For tobacco products, your
		exemption is the same as it is for after 48-hours.
		*Please note that these are for general information use only and exemption information
		may have been updated by Canadian Border Services Agency (CBSA). For complete
		personal exemption information, please visit:
		<a href="http://www.cbsa-asfc.gc.ca/publications/pub/bsf5056-eng.html#s2x9">
		http://www.cbsa-asfc.gc.ca/publications/pub/bsf5056-eng.html#s2x9</a>. (<a title="top" href="#top">top</a>)</p>
		
		<p class="faqitem"><strong>How long do I have until I need to pick-up my package?</strong><br/>
		You have 30-days to pick-up your package from a KinekPoint. In some circumstances
		extended storage can be arranged for an additional fee. For full pick-up information,
		please visit your KinekPoints profile page by using our map function and then
		clicking on their location.  (<a title="top" href="#top">top</a>)</p>
		</div>
		
		
		<div id="pricinghours" style="display:none">
			<p class="faqitem"><strong>How do I find out the hours of operation for a KinekPoint?</strong><br/>
			Each location has different hours of operation, and you can view these by visiting
			that KinekPoint's profile page. However, we always recommend calling the KinekPoint
			ahead of time to make sure they are open on a particular weekend.</p>
	
			<p>Hours of operation, package size allowance, receiving fee, contact information,
			and all kinds of other useful information can be found in each KinekPoint's
			profile page. To find a KinekPoint's profile page, use the Find a KinekPoint tab (or in your account) and search for the area you would like to receive
			packages in. Our map will then display all the KinekPoints near that area. Click
			on the one that interests you and you will be taken to that KinekPoint's profile
			page. </p>
	
			<p class="faqitem"><strong>How much does it cost to use the Kinek service?</strong><br/>
			Each KinekPoint has a per package fee shown on their profile. There are no monthly or sign up fees to use Kinek. You can simply search a KinekPoint on the Find a KinekPoint tab and select a KinekPoint from the map. Once selected, a pop up window will show you the KinekPoints lowest rate. You can also sign up for free to view the KinekPoint(s) rates by weight.</p>
	
			<p class="faqitem"><strong>How do I pay for this service?</strong><br/>
			You will pay your per-package receiving fee to the KinekPoint when you pick up your package.</p>						
		</div>
		
		<div id="orderingOnline" style="display:none">
			<p class="faqitem"><strong>When shopping online how do I direct my packages to a KinekPoint?</strong><br/>
			You are free to ship purchases made online or offline to a KinekPoint of your choosing. When completing the shipping details all you do is enter your name, Kinek#, KinekPoint Name and KinekPoint Address into the Shipping Address section of the company you are ordering from. This formatting is shown in your Kinek account. When your package arrives, Kinek will send you an email and a text message to let you know your package has arrived and is ready for pick-up at a time of your convenience. </p>
	
			<p class="faqitem"><strong>Why register for a Kinek number?</strong><br/>
			Not home during shipping hours? Tired of receiving missed delivery notices or trying to track down your packages? Worried that packages left unattended on your doorstep or in the hall of your condo complex may be stolen? When you join Kinek you can rest easy knowing that someone will always be there at a secure, trusted KinekPoint to receive your packages for you. </p>
			<p class="faqitem"><strong>What if my Family or Friends want to send me packages?</strong><br/>
			Nothing is better than receiving a package from family or friends and Kinek makes it easier than ever. Send them your Kinek#, the name of your KinekPoint and your KinekPoint address as it is displayed in your Kinek account. When they send a package purchased online or offline all they need to do is use your Kinek address and it will safely arrive at your KinekPoint. As soon as it arrives, we'll notify you by text message and email. (<a title="top" href="#top">top</a>) 
			</p>
			<p class="faqitem"><strong>How long do I have to wait before my account is set up?</strong><br/>
			There is no waiting period after you sign up for an account. You can begin shipping to your new Kinek address immediately after you select a KinekPoint. Check your inbox for a welcome email or find your address by logging in to your Kinek account. (<a title="top" href="#top">top</a>)
			</p>	
		</div>
		
		<div id="myKinekPoint" style="display:none">
			<p class="faqitem"><strong>Why register for a Kinek number?</strong><br/>
			Not home during shipping hours? Tired of receiving missed delivery notices or trying to track down your packages? Worried that packages left unattended on your doorstep or in the hall of your condo complex may be stolen? When you join Kinek you can rest easy knowing that someone will always be there at a secure, trusted KinekPoint to receive your packages for you.</p>
			<p class="faqitem"><strong>How do I know my package has been delivered?</strong><br/>
			When you sign up with Kinek you are saying good-bye to the days of coming home to a missed delivery notice stuck on your door. When your package arrives at your local KinekPoint you receive an electronic notification either by email or SMS text message to let you know your package has arrived and is ready for pick-up at your convenience.</p>
			<p class="faqitem"><strong>Can I ship to different KinekPoints?</strong><br/>
			One of the many benefits of Kinek is that you can send your packages to the KinekPoint of your choice. It might be easier to send packages to the KinekPoint near your work or along your daily commute. Or perhaps you are travelling and you want your packages sent to a KinekPoint close to your relatives, your hotel or RV campground as you travel from city to city.</p>
			<p class="faqitem"><strong>I did not receive a welcome/package notification email from Kinek. What do I do?</strong><br/>
			If you have not received a Kinek email please check your junk/spam folder. Please add us to your safe list if found in the junk/spam folder. If you still cannot locate the welcome email please contact us. (<a title="top" href="#top">top</a>)</p>
			<p class="faqitem"><strong>How much does it cost to use the Kinek service?</strong><br/>
			Each KinekPoint chooses a per package fee shown on their profile. There are no monthly or sign up fees. You can simply search a KinekPoint on the Find a KinekPoint tab and select a KinekPoint from the map. Once selected, click on "show pricing details" on their profile page. You can also view the pricing of your KinekPoint when you sign into your account. (<a title="top" href="#top">top</a>)</p>
			<p class="faqitem"><strong>Do KinekPoints accept COD packages?</strong><br/>
			KinekPoints do not accept COD packages. (<a title="top" href="#top">top</a>)
			</p>
			<p class="faqitem"><strong>How long do I have until I need to pick-up my package?</strong><br/>
			You have 30-days to pick-up your package from a KinekPoint. In some circumstances extended storage can be arranged for an additional fee. For full pick-up information, please visit your KinekPoints profile page by using our map function and then clicking on their location. (<a title="top" href="#top">top</a>)</p>
			<p class="faqitem"><strong>Can Kinek receive regular mail?</strong><br/>
			KinekPoints currently do not receive regular mail including bills or any documents sent via Canada Post or USPS. If you're not sure if your item is regular mail or a package please contact us before sending your item. (<a title="top" href="#top">top</a>)</p>		
		</div>
		
		<div id="packagesQ" style="display:none">
			<p class="faqitem"><strong>I did not receive a welcome/package notification email from Kinek. What do I do?</strong><br />
			If you have not received a Kinek email please check your junk/spam folder. Please add us to your safe list if found in the junk/spam folder. If you still cannot locate the welcome email please contact us.</p>
			<p class="faqitem"><strong>What do I need to bring with me in order to pick up my package?</strong><br/>
			You will need to have 2 government issued photo ID's as well as the purchase confirmation (or receipt) sent to you by the retailer in order to pick up the packages.</p>
			<p class="faqitem"><strong>Do you offer package forwarding?</strong><br/>
			We do not currently offer a package forwarding service, but you can contact the KinekPoint directly to see if they will work something out for you. You can view a KinekPoint's phone number on their profile only after signing up and then signing into your account.</p>
			<p class="faqitem"><strong>Do KinekPoints accept COD packages?</strong><br/>
			KinekPoints do not accept COD packages. (<a title="top" href="#top">top</a>)
			</p>
			<p class="faqitem"><strong>Can I assign someone else to pick up my package?</strong><br/>
			Yes, it is possible for you to assign somebody else (family member living in the same household) to pick up your package on your behalf.  You simply need to provide them with a photocopy of your ID (government issued, photo) as well as a copy of the order confirmation from the retailer. (<a title="top" href="#top">top</a>)</p>
		</div>
   	</div>

  </s:layout-component>
</s:layout-render>