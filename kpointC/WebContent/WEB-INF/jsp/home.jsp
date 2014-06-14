<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Kinek</s:layout-component>
  	<s:layout-component name="body">
  	<link rel="stylesheet" href="resource/css/pages/home.css" type="text/css" media="screen,projection" />

<script type="text/javascript">
// <![CDATA[
	jQuery.noConflict();

	jQuery(document).ready(function(){
		theme: 'dark_rounded';
	});

// ]]>
</script>	
	

	<c:choose>
	<c:when test="${actionBean.signedIn}">
		<div id="homeSearchWrapper" class="clearfix signedin">
	</c:when>
	<c:otherwise>
		<div id="homeSearchWrapper" class="clearfix">
	</c:otherwise>
	</c:choose>
	
		<div id="homeSearch" class="clearfix">
			
	
			<div id="homeSearchContent">
			
				<div id="kbox">
					<a target="new" href="http://www.youtube.com/watch?v=PUXqG7SPKgc" title="Avoid stolen packages.  Avoid missed delivers.  Watch the video."></a>
				</div>      
				
				<div id="kmessage">
					<div class="line1"><strong>Shop online, but not home for deliveries?</strong></div>
					<div class="line3">Are you a working professional or frequent traveler?  Live in an apartment, gated community, university residence, or military base? If so, you've most likely struggled with home deliveries.<br><br /></div>
					<div class="line3">Kinek's nationwide network of KinekPoints will receive your online orders and notify you when they arrive. <strong>Shop online, and keep your orders safe ... ship them to a KinekPoint!</strong><br><br /></div>
					<s:link beanclass="org.webdev.kpoint.action.SignupActionBean" title="Sign Up Now">&nbsp;</s:link>
				</div>
				
			</div>
		</div>
		
		<div id="affiliate">
			<c:forEach items="${actionBean.randomAffiliates}" var="randomAffiliate" varStatus="loop">
				${randomAffiliate.url}
			</c:forEach>
		</div>
		
		<div id="homeBoxes" class="clearfix">
			
			<div id="tweetandetail">
				<div id="followUs">
					<img id="birdie" src="resource/images/twitterbird.jpg"/>
					<a class="twitterfollow" target='new' href="http://www.twitter.com/kinek"><img id="twitter" alt="Kinek Twitter" src="resource/images/follow_us-a.png"/></a>
				</div>
				<div id="solutionProvider">
					<a class="etailbadge" target='new' href="http://www.wbresearch.com/etailusawest/"><img alt="Preferred Solution Provider" src="resource/images/etailbadge.png"/></a>
				</div>
			</div>
			
			
			<div id="shiptoborder">
				<p> 
				Looking for a US Shipping Address along the US border?
				<br class="clear" />
				<br class="clear" />
				<c:choose>
					<c:when test="${actionBean.signedIn}">
						<a href="ChooseDefaultKinekPoint.action?isBorderSearch=true" title="Ship to the Border">Click Here</a>
					</c:when>
					<c:otherwise>
						<a href="DepotSearch.action?isBorderSearch=true" title="Ship to the Border">Click Here</a>
					</c:otherwise>
				</c:choose>
				<br class="clear" />
				<br class="clear" />
				Order from US sites and save by shipping to the border.</p>				
				<p><a href="Why.action#canadianborder" title="Ship to the Border">Learn More</a></p>
			</div>
		
			<div id="becomeakinekpoint">
				<p>Are you a business interested in becoming a KinekPoint?
				<br class="clear" />
				<a href="${externalSettingsManager.depotPortalBaseUrl}/HowItWorks.action" title="KinekPoint Partner Information">Learn More</a>
				</p>
				<br class="clear" />
				<p><a href="${externalSettingsManager.depotPortalBaseUrl}/Login.action" title="KinekPoint Partner Login">KinekPoint Partners login here</a></p>
			</div>
		</div>
		<br class="clear" />
	</div>
	
</s:layout-component>
</s:layout-render>	