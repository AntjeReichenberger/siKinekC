<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Frequently Asked Questions</s:layout-component>
  	
  	<s:layout-component name="body">
  	<link rel="stylesheet" href="resource/css/pages/faq.css" type="text/css" media="screen,projection" />

	<script type="text/javascript">
	// <![CDATA[
	       jQuery(document).ready(function(){
	               jQuery('#nav li:eq(3)').addClass('active');
	       });
	// ]]>
	</script>
	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<a name="top" id="top"></a>
			
			<!-- STEP TITLE -->
			<h1>Frequently Asked Questions</h1>
			<!-- /STEP TITLE -->

			<div id="categories">
				<a href="/shipping-to-the-border"><img title="Shipping to the Border" src="/resources/images/faq_ShippingToBorder.jpg" alt="Shipping to the border" width="86" height="86" /></a>
				<a href="/pricing-hours"><img title="Kinek Pricing and hours" src="/resources/images/faq_PricingHours.jpg" alt="Pricing and hours" width="86" height="86" /></a>
				<a href="/ordering-online"><img title="Ordering Online and sending to KinekPoint" src="/resources/images/faq_OrderingOnline.jpg" alt="Ordering Online" width="86" height="86" /></a>
				<a href="/my-kinekpoint"><img title="My KinekPoint" src="/resources/images/faq_MyKinekPoint.jpg" alt="KinekPoint questions" width="86" height="86" /></a>
				<a href="/picking-up-packages"><img title="Questions about picking up packages" src="/resources/images/faq_PickingUpPackages.jpg" alt="Picking up packages" width="86" height="86" /></a>
			</div>
			
			
			<br />
			
			<strong>General</strong>
			<ul class="list bulleted faqlist">
				<li><a href="#faq1" title="Why register for a Kinek#?">Why register for a Kinek#?</a></li>
				<li><a href="#faq2" title="How much does it cost to use the Kinek Service?">How much does it cost to use the Kinek Service?</a></li>
				<li><a href="#faq3" title="What if my Kinek# and c/o business name does not fit on the online retailers form?">What if my Kinek# and C/O business name does not fit on the online retailers form?</a></li>
				<li><a href="#faq4" title="Do KinekPoints accept COD packages?">Do KinekPoints accept COD packages?</a></li>
				<li><a href="#faq5" title="How do I know my package has been delivered?">How do I know my package has been delivered?</a></li>
				<li><a href="#faq6" title="When shopping online how do I direct my purchases to a KinekPoint?">When shopping online how do I direct my purchases to a KinekPoint?</a></li>
				<li><a href="#faq7" title="Can I ship to different KinekPoints?">Can I ship to different KinekPoints?</a></li>
				<li><a href="#faq8" title="What if my family and friends want to send me packages?">What if my family and friends want to send me packages?</a></li>
			</ul>
			<br />
			
			<strong>Credit Cards</strong>
			<ul class="list bulleted faqlist">
				<li><a href="#faq9" title="My online order didn't go through because it said my Kinek address did not match my billing address. What should I do?">My online order didn't go through because it said my Kinek address did not match my billing address. What should I do?</a></li>
			</ul>
			<br />
			
			<strong>KinekPoint information (contact info, hours, package size, etc)</strong>
			<ul class="list bulleted faqlist">
				<li><a href="#faq10" title="How do I find out the hours of operation for a KinekPoint?">How do I find out the hours of operation for a KinekPoint?</a></li>
				<li><a href="#faq11" title="Are your border KinekPoints open on weekends?">Are your border KinekPoints open on weekends?</a></li>
				<li><a href="#faq12" title="How do I find a KinekPoint near me?">How do I find a KinekPoint near me?</a></li>
				<li><a href="#faq13" title="I did not receive a welcome/package notification email from Kinek. What do I do?">I did not receive a welcome/package notification email from Kinek. What do I do?</a></li>
				<li><a href="#faq2" title="How much does it cost to use the Kinek Service?">How much does it cost to use the Kinek Service?</a></li>
				<li><a href="#faq14" title="What size items can KinekPoints accept?">What size items can KinekPoints accept?</a></li>
				<li><a href="#faq17" title="I don't see any KinekPoints around me when I search the map">I don't see any KinekPoints around me when I search the map</a></li>
			</ul>
			<br />
			
			<strong>Border KinekPoints for Canadians (use as your US shipping address)</strong>
			<ul class="list bulleted faqlist">
				<li><a href="#faq15" title="Where does Kinek have US border locations for cross border shopping?">Where does Kinek have US border locations for cross border shopping?</a></li>
				<li><a href="#faq16" title="I want to use one of Kinek's border KinekPoints as my US shipping address, which one is closest to me?">I want to use one of Kinek's border KinekPoints as my US shipping address, which one is closest to me?</a></li>				
				<li><a href="#faq13" title="I did not receive a welcome/package notification email from Kinek. What do I do?">I did not receive a welcome/package notification email from Kinek. What do I do?</a></li>
				<li><a href="#faq18" title="What about Duties and Taxes?">What about Duties and Taxes?</a></li>
				<li><a href="#faq19" title="How long do I have until I need to pick-up my package?">How long do I have until I need to pick-up my package?</a></li>
			</ul>
			
			<br /><br />
			
			<a name="faq1" id="faq1"></a>
			<p class="faqitem">
				<strong>Why register for a Kinek number?</strong>
				<br />Not home during shipping hours?  Tired of receiving missed delivery notices or trying to track 
				down your packages? Worried that packages left unattended on your doorstep or in the hall of your condo 
				complex may be stolen?  When you join Kinek you can rest easy knowing that someone will always be there 
				at a secure, trusted KinekPoint to receive your packages for you. Visit the 
				<s:link beanclass="org.webdev.kpoint.action.WhyActionBean" title="\"Why Kinek\"">Why</s:link> page for 
				the many scenarios in which you would find Kinek useful. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq2" id="faq2"></a>
			<p class="faqitem">
				<strong>How much does it cost to use the Kinek Service?</strong>
				<br />Each KinekPoint that accepts packages 
				will charge you a small convenience fee that is shown on their profile page. Fees vary by KinekPoint location 
				so before selecting a KinekPoint as your default location be sure to check over all of their fees and service 
				features. The small fee is more than justified when taking into account the convenience, gas saved, and time 
				spent usually trying to track down your package. Oh yeah ... don't forget that you also eliminate your package 
				being damaged by the elements or potentially be stolen.  (<a href="#top" title="top">top</a>)
			</p>
		
			<br />
			
			<a name="faq3" id="faq3"></a>
			<p class="faqitem">
				<strong>What if my Kinek# and c/o KinekPoint shipping address does not fit within the online retailers form?</strong>
				<br />Online retailer forms vary in size and how many characters they will accept. There will be times when you try 
				to enter your Kinek# and c/o KinekPoint name, that an online retailer's form will not accept the complete address with 
				your Kinek#.  In these scenarios, make sure you include your Kinek# and then type as many characters as you can for the 
				remaining part of the c/o KinekPoint name. The Kinek# is <b>mandatory and MUST</b> be located on the address label.  
				(<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq4" id="faq4"></a>
			<p class="faqitem">
				<strong>Do KinekPoints accept COD packages?</strong>
				<br />KinekPoints do not accept COD packages. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq5" id="faq5"></a>
			<p class="faqitem">
				<strong>How do I know my package has been delivered?</strong>
				<br />When you sign up with Kinek you are saying good-bye to the days of coming home to a missed delivery notice 
				stuck on your door.  When your package arrives at your local KinekPoint you receive an electronic notification 
				either by email or SMS text message to let you know your package has arrived and is ready for pick-up at your 
				convenience. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq6" id="faq6"></a>
			<p class="faqitem">
				<strong>When shopping online how do I direct my packages to a KinekPoint?</strong>
				<br />You are free to ship purchases made online or offline to a KinekPoint of your
				 choosing. When completing the shipping details all you do is enter your name, 
				 Kinek#, KinekPoint Name and KinekPoint Address into the Shipping Address section 
				 of the company you are ordering from. This formatting is shown in your Kinek 
				 account. When your package arrives, Kinek will send you an email and a text message 
				 to let you know your package has arrived and is ready for pick-up at a time of your 
				 convenience. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq7" id="faq7"></a>
			<p class="faqitem">
				<strong>Can I ship to different KinekPoints?</strong>
				<br />One of the many benefits of Kinek is that you can send your packages to the KinekPoint of your choice. It 
				might be easier to send packages to the KinekPoint near your work or along your daily commute. Or perhaps you are 
				travelling and you want your packages sent to a KinekPoint close to your relatives, your hotel or RV campground as 
				you travel from city to city. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq8" id="faq8"></a>
			<p class="faqitem">
				<strong>What if my Family or Friends want to send me packages?</strong>
				<br />Nothing is better than receiving a package from family or friends and Kinek 
				makes it easier than ever. Send them your Kinek#, the name of your KinekPoint 
				and your KinekPoint address as it is displayed in your Kinek account. When they 
				send a package purchased online or offline all they need to do is use your Kinek 
				address and it will safely arrive at your KinekPoint. As soon as it arrives, we'll 
				notify you by text message and email. (<a href="#top" title="top">top</a>)
			</p>

			<br />
			
			<a name="faq9" id="faq9"></a>
			<p class="faqitem">
				<strong>My online order didn't go through because it said my Kinek address did not match my billing address. What should I do?</strong>
				<br />Credit card companies sometimes block orders from going through if the shipping address and billing address do not match. They 
				do this as a safety precaution in case someone steals your card. However, if you would like to use a different address than your billing 
				address, all you need to do is call your credit card company and let them know. They simply make a note in your file and your orders will 
				no longer be blocked. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq10" id="faq10"></a>
			<p class="faqitem">
				<strong>How do I find out the hours of operation for a KinekPoint?</strong>
				<br />Hours of operation, package size allowance, receiving fee, contact information, and all kinds of other useful information can be 
				found in each KinekPoint's profile page. To find a KinekPoint's profile page, use the search bar on the home page (or in your account) 
				and search for the area you would like to receive packages in. Our map will then display all the KinekPoints near that area. Click on 
				the KinekPoint that interests you and you will be taken to their profile page. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq11" id="faq11"></a>
			<p class="faqitem">
				<strong>Are your border KinekPoints open on weekends?</strong>
				<br />Each location has different hours of operation, and you can view these by visiting that KinekPoint's profile page. However, we 
				always recommend calling the KinekPoint ahead of time to make sure they are open that particular weekend. <br /><br />
				Hours of operation, package size allowance, receiving fee, contact information, and all kinds of other useful information can be found 
				in each KinekPoint's profile page. To find a KinekPoint's profile page, use the search bar on the home page (or in your account) and 
				search for the area you would like to receive packages in. Our map will then display all the KinekPoints near that area. Click on the 
				one that interests you and you will be taken to that KinekPoint's profile page. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />

			<a name="faq12" id="faq12"></a>
			<p class="faqitem">
				<strong>How do I find a KinekPoint near me?</strong>
				<br />You can locate a KinekPoint by entering your city, town, or zip code into the search box on the Kinek homepage. Kinek will present 
				a list of KinekPoints in your area and you are free to choose the location that best suits your needs: one close to home, work or along 
				your daily commute. You choose the KinekPoint that best fits your schedule. We are expanding our nationwide network and adding new 
				KinekPoints daily, so if you don't find one in your area check back soon or send us a note and recommend one in your area. 
				(<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq13" id="faq13"></a>
			<p class="faqitem">
				<strong>I did not receive a welcome/package notification email from Kinek. What do I do?</strong>
				<br />If you have not received a Kinek email please check your junk/spam folder. Please add us to your safe list if found in the 
				junk/spam folder.  If you still cannot locate the welcome email please contact us. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq14" id="faq14"></a>
			<p class="faqitem">
				<strong>What size items can KinekPoints accept?</strong><br />
				Size does matter. Each KinekPoint has different options with regard to the size of the packages they will receive for you. Some will 
				receive only small packages while others will receive up to pallets. If you intend on sending a very large item we recommend contacting 
				the KinekPoint in advance, so they can be ready to receive your item. Accepted packages are usually categorized as one of the following 
				three options:<br /><br />
				1. Small - up to 12 feet in overall package size.<br />
				2. Medium - up to 18 feet in overall package size.<br />
				3. Large - any size and weight.<br />				
				* Overall package size = Length x Width x Height of your package <br /><br />				
				Be sure to view your selected KinekPoint's profile to check what size packages they accept. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq15" id="faq15"></a>
			<p class="faqitem">
				<strong>Which border location is right for you?</strong><br/>
				To make it easy, we have broken down our locations by the Canadian cities/regions they serve. Simply click on the name of the KinekPoint 
				to view their profile page and sign-up!
			</p>
			
			<div class="locationlistwrapper">
			<ul>
				<li>
					<b>Montreal:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Champlain, New York
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=973">Bay Brokerage</a> (can accept any size or weight packages)</li>
								<li><a href="/kpointC/DepotSearch.action?depotId=345">Border Mail Services</a></li>
							</ul>
						</li>
						<li>
							Swanton, VT
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=1062">Business America Services</a> (can accept any size or weight packages)</li>
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Sherbrooke:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Derby Line, Vermont
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=802">Norman G. Jensens</a> (can accept any size or weight packages)</li>								
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Ottawa region, Brockville:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Ogdensburg, New York (only 45 minutes from Ottawa)
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=1005">NAC Logistics</a> (can accept any size or weight)</li>								
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Kingston, Belleville, Gananoque, Napanee:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Wellesley Island, New York (35 minutes from Kingston)
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=803">Bay Brokerage</a> (can accept any size or weight)</li>								
								<li><a href="/kpointC/DepotSearch.action?depotId=842">Wellesley Island Building Supply</a> (can accept any size or weight)</li>
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Greater Toronto Area:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Buffalo, New York 
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=861">Bay Brokerage</a> (can accept any size or weight)</li>								
								<li><a href="/kpointC/DepotSearch.action?depotId=850">UPS Store</a></li>
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Windsor:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Detroit, Michigan 
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=824">Computing Express</a></li>
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>London/Sarnia:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Port Huron
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=882">Bay Brokerage</a> (can accept any size or weight)</li>								
							</ul>
						</li>
						<li>
							Marine City, Michigan
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=148">FB Package and Shipping</a></li>								
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Sault Ste. Marie:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Sault Ste. Marie, Michigan
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=875">Pete's Appliance</a> (can accept any size or weight packages)</li>								
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Winnipeg:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Pembina, North Dakota
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=849">Red River Repair</a> (can accept any size or weight packages)</li>								
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Greater Vancouver Area, British Columbia:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Blaine, Washington
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=1089">Edge Logistics</a></li>								
							</ul>							
						</li>
					</ul>				
				</li>
				<li>&nbsp;</li>
				<li>
					<b>Kelowna/Osoyoos, British Columbia:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Oroville, Washington
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=938">Appleway Video</a></li>								
							</ul>
						</li>
					</ul>
				</li>
				<li>&nbsp;</li>
				<li>
					<b>New Brunswick/Nova Scotia/PEI:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Calais, Maine
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/DepotSearch.action?depotId=767">Calais Ace Homecenter</a> (can accept any size or weight packages)</li>
								<li><a href="/kpointC/DepotSearch.action?depotId=653">AN Deringer</a> (can accept any size or weight packages)</li>																
							</ul>
						</li>
					</ul>
				</li>
			</ul>	
			
			</div>
			<br /><br />

			<!-- Border KinekPoint static locator to display markers on the border area -->			
		    	<div id="map" style="position:relative; width:750px; height:370px;"></div>		    
		    	<script type="text/javascript" src="https://ecn.dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=6.3&s=1"></script>
				<script type="text/javascript" src="resource/js/bordermapmarker/BorderKinekPointMarker.js"></script>
				<script type="text/javascript" src="resource/js/bordermapmarker/BorderKinekPoint.js"></script>
		    	<script type="text/javascript" src="resource/js/bordermapmarker/BorderKinekPointLocator.js"></script>
			<!-- //Border KinekPoint static locator to display markers on the border area -->
			
		    (<a href="#top" title="top">top</a>)
		    
		    <br /><br /><br />
		    
		    <a name="faq16" id="faq16"></a>
			<p class="faqitem">
				<strong>I want to use one of Kinek's border KinekPoints as my US shipping address, which one is closest to me?</strong><br />
				Kinek has a network of KinekPoints all along the US/Canadian border that will let you receive packages from US companies. In 
				order to find one of these locations and save it as your default KinekPoint, simply sign-up, complete your profile, and then 
				search the name of the border city you would like to ship to. For instance, if you live in Toronto, you could search for 
				"Buffalo, New York" or "Port Huron, Michigan". The map would then display the KinekPoints we have in the area. Simply click 
				on the location and then click the green "Save this KinekPoint" button. (<a href="#top" title="top">top</a>)
			</p>
			
			<br />

			<a name="faq17" id="faq17"></a>
			<p class="faqitem">
				<strong>I don't see any KinekPoints around me when I search the map</strong><br />
				If you don't see any KinekPoints on the map, be sure to use our "recommend a KinekPoint" form that will be displayed next to 
				the map. When you request a location, we start working on getting a KinekPoint set up their right away!<br /><br /> 
				Just because there isn't a KinekPoint near you, doesn't mean there isn't something you can use Kinek for. You can:<br /><br />
				1) If you're Canadian, check to see if there is a KinekPoint in a border city near you. If there is, you can use that location as 
				your US shipping address to save hundreds of dollars when shopping online by shipping packages there from US retailers. No more 
				brokerage or international shipping fees!<br /><br />
				2) You can also use our nationwide network of KinekPoints when you're travelling. For instance, if you're going to be in New York 
				for meetings, use one of our local KinekPoints to receive an online order while you're there. Going to San Francisco for vacation? 
				Use one of our KinekPoints there to receive and hold a package for you.
				(<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq18" id="faq18"></a>
			<p class="faqitem">
				<strong>What about Duties and Taxes?</strong><br />
				Kinek does not collect duties and taxes on packages received at our border KinekPoints. It is the responsibility of the customer to 
				claim their items when crossing the border and pay the appropriate fees. All products, including tobacco, face duties and taxes if 
				you have been out of the country for less than 48-hours.
				<br /><strong class="nohighlight">Your personal exemption limits are<sup>*</sup>:</strong>
				<br /><strong class="nohighlight">Out of the country for 24 hours:</strong> You can claim up to $50 without paying any duties. <strong>IMPORTANT</strong> - 
				you cannot include tobacco or alcohol products in this exemption.
				<br /><strong class="nohighlight">Out of the country for 48 hours or more:</strong> You can claim up to $400 without paying any duties. For tobacco products, 
				you can claim up to 200 cigarettes, 50 cigars or cigarillos; and 200g (7 oz.) of manufactured tobacco. 
				<br /><strong class="nohighlight">Out of the country for 7 days or more:</strong> You can claim up to $750 without paying any duties. For tobacco products, 
				your exemption is the same as it is for after 48-hours. 
				<br />*Please note that these are for general information use only and exemption information may have been updated by Canadian Border 
				Services Agency (CBSA). For complete personal exemption information, please visit:
				<a HREF="http://www.cbsa-asfc.gc.ca/publications/pub/bsf5056-eng.html#s2x9">http://www.cbsa-asfc.gc.ca/publications/pub/bsf5056-eng.html#s2x9</a>.
				(<a href="#top" title="top">top</a>)
			</p>
			
			<br />
			
			<a name="faq19" id="faq19"></a>
			<p class="faqitem">
				<strong>How long do I have until I need to pick-up my package?</strong>
				<br />You have 30-days to pick-up your package from a KinekPoint. In some circumstances extended storage can be arranged for an 
				additional fee. For full pick-up information, please visit your KinekPoints profile page by using our map function and then 
				clicking on their location. (<a href="#top" title="top">top</a>)
			</p>
			
    	</div>
    	<!-- /CONTENT -->    
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>