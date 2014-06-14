<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Why Kinek?</s:layout-component>
  	
  	<s:layout-component name="body">
  	<link rel="stylesheet" href="resource/css/pages/why.css" type="text/css" media="screen,projection" />

	<script type="text/javascript">
	// <![CDATA[
	       jQuery(document).ready(function(){
	               jQuery('#nav li:eq(2)').addClass('active');
	       });
	// ]]>
	   	jQuery.noConflict();

		jQuery(document).ready(function(){
			theme: 'dark_rounded';	
		});
	</script>
	
	<%
	
		boolean loggedIn = false;
		String loggedInMap = "ChooseDefaultKinekPoint.action?";
		String loggedOutMap = "DepotSearch.action?";
	%>
	
	<c:choose>
	<c:when test="${actionBean.signedIn}">
		<% 
			loggedIn = true;
		%>
	</c:when>
	</c:choose>
	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<a name="top" id="top"></a>
				<div id="whyPageKbox">
					<a target="new" href="http://www.youtube.com/watch?v=PUXqG7SPKgc" title="Avoid stolen packages.  Avoid missed delivers.  Watch the video."></a>
				</div> 
			<div id="why">
			<!-- STEP TITLE -->
			<h1>Why Kinek?</h1>
			<!-- /STEP TITLE -->
						
			<ul class="list bulleted why">				
				<li><a href="#why1" title="You work all day">You work all day</a></li>
				<li><a href="#why2" title="Canadian cross-border shopping">Canadian cross-border shopping</a></li>
				<li><a href="#why3" title="You live in an condo, apartment, gated community, or only have a PO Box">You live in an condo, apartment, gated community, or only have a PO Box</a></li>
				<li><a href="#why4" title="University students">University students</a></li>
				<li><a href="#why5" title="Military bases">Military bases</a></li>
				<li><a href="#why6" title="Wedding Gifts">Wedding Gifts</a></li>
				<li><a href="#why7" title="Going on vacation or a business trip">Going on vacation or a business trip</a></li>				
			</ul>
			<br />			
						
			<p>Kinek now has close to 1000 (and growing!) authorized KinekPoints across the United States and Canada and you're wondering how you can use them to make life a little less stressful.</p> 
			<p>KinekPoints solve the frustration involved with receiving packages from online and catalogue shopping, or even just receiving a package from friends or family. We've all dealt with coming home from work to find a "missed delivery" slip on our front door. You then spend the next day or two trying to track it down or arrange to be at home for the next delivery attempt. Sometimes delivery companies will even leave an expensive package sitting on your front step for anyone to walk over and take, or for it to be rained of snowed on.</p>
			<p>Kinek solves these problems by offering you a network of almost 1000 locations across the North America where you can have your packages shipped to.  Can't find one near you? Suggest a location and we'll find one for you. These locations are open during all delivery hours, <b>ensuring you will never miss a delivery again.</b></p>
			
			
			<br class="clear" />			
			<h4 class="nomargin large">When would I use Kinek?</h4>
			<p>To get you started, we've put together some example situations below. If you've found another use that really helps you out and we don't have it listed, give us a shout!</p>
			<p>
				No matter what KinekPoint you ship to, you will receive an instant text message (SMS) and email notification when it arrives! You can then pick it up at a time that is convenient for you.
   			   (<a href="#top" title="top">top</a>)
			</p> 
			
			<a name="why1" id="why1"></a>
			<h4 class="nomargin">You work all day</h4>
			<p>The biggest problem with ordering something online is that the delivery hours are the exact same as the time most of us are at work. Since most of us aren't at home, we get that pesky missed delivery slip stuck on our front door, or have our expensive order left on our front step.</p>
			<p>
				By using Kinek's network of alternate delivery locations (KinekPoints), you will never have to worry about missing a delivery again or having one left on your front step. You simply use our site to find a KinekPoint that is close to your home, work, or daily commute and register with Kinek to have your packages shipped to it. As soon as your package arrives at a KinekPoint, you will receive an instant text message (SMS) and email notification letting you know it's ready to be picked up. Just drop by to pickup your package(s) when it is convenient for you!
				(<a href="#top" title="top">top</a>)
			</p>
			
			<br class="clear" />
			<a name="canadianborder" id="canadianborder"></a>
			<a name="why2" id="why2"></a>			
			<h4 class="nomargin">Canadian cross-border shopping</h4>
			<img class="whyshot" src="resource/images/why_border.jpg"></img>
			<p>One of the most popular uses of the KinekPoint network is that we enable almost 15 million Canadians to use our border locations as their US shipping address. Canadians use our border KinekPoints because many e-commerce companies won't ship to Canada. Even if they will ship to Canada, they often charge expensive brokerage and international shipping fees.</p>
			<p>By shipping to one of Kinek's many border KinekPoints, Canadians can order from US companies and benefit from the greater selection of products, much lower prices, and a strong Canadian dollar. When your package arrives at one of our border locations, you will receive an instant text message (SMS) and email notification letting you know it is ready to be picked up. You can then drive down at a time convenient for you, pick it up, and bring it back across the border yourself. This lets you save hundreds, and sometimes thousands of dollars on your cross border shopping.</p>			
			<div class="locationlistwrapper">
			<ul>
				<li>
					<b>Montreal:</b>
					<ul class="faqBorderMapUSACity">
						<li>
							Champlain, New York
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=973">Bay Brokerage</a> (can accept any size or weight packages)</li>
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=345">Border Mail Services</a></li>
							</ul>
						</li>
						<li>
							Swanton, VT
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=1062">Business America Services</a> (can accept any size or weight packages)</a></li>
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=802">Norman G. Jensens</a> (can accept any size or weight packages)</li>								
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=1005">NAC Logistics</a> (can accept any size or weight)</li>								
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=803">Bay Brokerage</a> (can accept any size or weight)</li>								
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=842">Wellesley Island Building Supply</a> (can accept any size or weight)</li>
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=861">Bay Brokerage</a> (can accept any size or weight)</li>								
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=850">UPS Store</a></li>
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=824">Computing Express</a></li>
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=882">Bay Brokerage</a> (can accept any size or weight)</li>								
							</ul>							
						</li>
						<li>
							Marine City, Michigan
							<ul class="faqBorderMapDepotList">
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=148">FB Package and Shipping</a></li>								
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=875">Pete's Appliance</a> (can accept any size or weight packages)</li>								
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=849">Red River Repair</a> (can accept any size or weight packages)</li>								
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=1089">Edge Logistics</a></li>								
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=938">Appleway Video</a></li>								
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
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=767">Calais Ace Homecenter</a> (can accept any size or weight packages)</li>
								<li><a href="/kpointC/<% out.print(((loggedIn) ? loggedInMap : loggedOutMap)); %>depotId=653">AN Deringer</a> (can accept any size or weight packages)</li>																
							</ul>							
						</li>
					</ul>				
				</li>																			
			</ul>					
			</div>			
			<p><br/>
				Not only can Canadians use our border locations to save on cross border shopping, but they can also use our extensive network of authorized KinekPoint locations throughout the US. Going to Florida on vacation? Order online and have your items waiting for you at a KinekPoint near your hotel when you arrive! Same goes for any other state and city!
				(<a href="#top" title="top">top</a>)
			</p>
			
			
			<br class="clear" />
			<a name="why4" id="why4"></a>
			<h4 class="nomargin">University Students</h4>
			<img class="whyshot" src="resource/images/why_university.jpg"></img>
			<span class="whysubheading">Online shopping</span>
			<p>Living in residence or in a student community makes it difficult for students to receive packages from online shopping. Many e-commerce sites won't ship to residences, forcing students to find other places to have their packages delivered. If you live off campus, chances are you spend most of your day in class which means you aren't home during the day for package deliveries.</p> 
			<p>By signing up for Kinek, you can avoid all these situations by shipping directly to a local KinekPoint. Your package will never be missed since all our locations are open during delivery hours, and, more often than not, into the evenings and weekends as well.</p>
			<span class="whysubheading">Care packages from home</span>
			<p>Not only can you use KinekPoints for online shopping, but you can also have packages shipped to you from friends and family. Don't worry about missing that exam care package from Mom, just have her send it to you using your Kinek shipping address!
				(<a href="#top" title="top">top</a>)
			</p>
			
			
			
			<br class="clear" />
			<a id="why5" name="why5"></a>
			<h4 class="nomargin">Military Bases</h4>
			<img class="whyshot" src="resource/images/why_military.jpg"></img>
			<p>
				Most military base personnel only have access to a PO Box and because of this are limited in the types of mail they can receive. To help solve this problem for military personnel, Kinek has established many KinekPoints near bases. This lets members of the military order from online stores and have their items shipped directly to a KinekPoint, avoiding all the hassles of receiving packages on base.
				(<a href="#top" title="top">top</a>)
			</p>
			
			<br class="clear" />
			<br class="clear" />
			<br class="clear" />
			<a id="why3" name="why3"></a>
			<h4 class="nomargin">Apartment Buildings / PO Box only residences / Gated communities</h4>
			<img class="whyshot" src="resource/images/why_apartment.jpg"></img>
			<p>Many people live in apartment buildings and regularly deal with the frustrations of home delivery. Often times this is because they can't order from certain e-commerce companies because they only have a PO box.</p> 
			<p>While some apartments have a concierge or doorman, many do not, meaning there is often no safe place to leave a package when you aren't at home.</p>
			<p>
				Kinek solves this by offering an extensive network of conveniently located KinekPoints where users can have their packages shipped to. By using Kinek, you'll never miss a delivery again and will be able to pick up your package at a time that is convenient for you.
				(<a href="#top" title="top">top</a>)
			</p>
			
			<br class="clear" />
			<a id="why6" name="why6"></a>
			<h4 class="nomargin">Wedding Gifts</h4>
			<img class="whyshot" src="resource/images/why_weddings.jpg"></img>
			<p>Many people often travel long distances for weddings or often send gifts even if they are unable to attend. . Why pay twice for shipping?  Once to receive your gift purchased, and a second time to bring it the wedding.  Eliminate hauling their wedding gift across the country when ordering online by having it shipped to a KinekPoint near the bride and groom's special event  So stop paying those extra fees to the airline for packing gifts with your luggage and have it delivered to a KinekPoint.</p>
			<p>
				Kinek solves this by offering an extensive network of conveniently located KinekPoints where users can have their packages shipped to. By using Kinek, you'll never miss a delivery again and will be able to pick up your package at a time that is convenient for you.
				(<a href="#top" title="top">top</a>)
			</p>
			
			
			<br class="clear" />
			<a id="why7" name="why7"></a>
			<h4 class="nomargin">Vacations/Business trips</h4>
			<img class="whyshot" src="resource/images/why_business.jpg"></img>
			<p>Another very popular use of Kinek is while on vacation or on a business trip. Why?</p> 
			<ul class="bulleted businesslist">
				<li><b>Ever traveled and wished you could mail something ahead to pick up when you arrived?</b> This is becoming more and more popular because airlines are charging more than ever before for checked luggage.</li>
				<li><b>Most hotels won't let you have items shipped to them.</b> By using Kinek, you can have online orders sent to a KinekPoint near your hotel. No need to deal with the hotel or worry about the safety of the package.</li>
				<li><b>RV owners</b> are on the road all the time, moving from state to state. Wouldn't it be great if you could have access to a nationwide network of  over 700 delivery locations where you can have online/catalogue orders shipped to?</li>
				<li><b>Business trips.</b> Have a meeting in Miami? Denver? Houston? San Francisco? Anywhere in the United States? Need to have something shipped in advance of the tradeshow. Chances are we have a conveniently located KinekPoint near your destination that you can use to have online orders shipped to. (<a href="#top" title="top">top</a>)</li>				
			</ul>
			</div>
	
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>