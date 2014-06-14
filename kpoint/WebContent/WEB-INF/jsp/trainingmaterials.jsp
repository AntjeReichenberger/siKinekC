<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
  
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
						<li class="active"><a href="#" title="Training Tutorials">Training Tutorials</a></li>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
			
						<!-- STEP TITLE -->
						<h1>Training Tutorials</h1>
						<!-- /STEP TITLE -->
						
						<p>The following videos will help you carry out the basic tasks involved in Depot Management. You may also view each of these videos on their corresponding pages throughout Depot Management.</p>
						
						<!-- TRAININGVIDEOS -->
						<ul id="trainingVideos" class="clearfix">
							<li>
								<a target="new" href="http://www.youtube.com/watch?v=cksbe9PR9Go" title="Accepting a Delivery">
									<img src="resource/videos/video_acceptdelivery.jpg" width="200" height="149" alt="Accepting a Delivery" />
								</a>
								<p>Accepting a Delivery</p>
							</li>
							<li>
								<a target="new" href="http://www.youtube.com/watch?v=h-cFVRQjtWk" title="Pick-Up">
									<img src="resource/videos/video_pickupdelivery.jpg" width="200" height="149" alt="Pick-Up" />
								</a>
								<p>Pick-Up</p>
							</li>
						</ul>
						<!-- /TRAININGVIDEOS -->

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

	<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
	
  </s:layout-component>
</s:layout-render>