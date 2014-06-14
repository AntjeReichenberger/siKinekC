<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">About Kinek</s:layout-component>
  	<s:layout-component name="body">
  	
  	<style type="text/css">
	  	.headshot
	  	{
	  		float: left;
	  		padding: 0px 5px 0px 0px;
	  	}  		
  	</style>
  
  	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">
	
		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<!-- INTERFACEWRAPPER -->
			<div id="interfaceWrapper">
			
				<!-- INTERFACE -->				
				<div id="interface">
				
					<!-- STEPS -->
					<ul id="steps">
						<li class="active"><a href="#" title="About Us">About Us</a></li>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
				<!-- ABOUT US -->
			
				<h1>About Kinek</h1>
				<div style="display:inline-block;width:65%;">
	
				<p>Kinek is a nationwide network of businesses, also known as KinekPoints, who essentially store your packages for you. Who hasn't had the experience of missing a delivery, having a package gone missing, or trying to track a package down from a courier outlet? It can be a nightmare, so Kinek was formed. Kinek has built most of its network between 2009-2011 with roughly 1,000 locations to date. They plan on setting up many more quite quickly, so don't worry if there isn't one next to your work or home just yet.</p>
				<p>Kinek is also home to the popular border network along the U.S/Canadian border. Canadians visit Kinek border locations every day to pick up their U.S. purchases that cannot be delivered to Canada. Over 50% of U.S. retailers don't ship to Canada and Kinek is the secure solution Canadians love.</p>
				<p>Kinek is also partnering with ecommerce companies to fulfill their customer's wants and needs and is able to provide consumers who shop online with an alternative address that is convenient for them.   It's a simple concept but it speaks volumes when you really need it.</p>
				<!-- /ABOUT US -->
				</div>
				<div style="display:inline-block;float:right;">
					<img src="resource/images/package_heads.jpg"/>
				</div>				
					</div>
					<!-- /STEPCONTAINER -->
				
				</div>
				<!-- /INTERFACE -->
				
			</div>
			<!-- /INTERFACEWRAPPER -->

		</div>
		<!-- /CONTENT -->

	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>