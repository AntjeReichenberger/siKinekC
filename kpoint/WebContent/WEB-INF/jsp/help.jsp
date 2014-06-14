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
						<li class="active"><a href="#" title="Help">Help</a></li>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
			
						<!-- STEP TITLE -->
						<h1>Help</h1>
						<!-- /STEP TITLE -->
						
						<h2>Training Videos</h2>
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
						
						<br />

						<h2>Frequently Asked Questions</h2>

						<p><strong>How much do I charge the customer when they come to pick up their package?</strong></p>
						<p>When you complete the steps for a pick-up, the system will list all charges associated to the parcel(s).</p>
						<p>Each KinekPoint charges a receiving fee located in the Depot Profile. If you have Depot Admin privileges, you can easily navigate to Administration -&gt;Depot Profile and locate the receiving fee your KinekPoint charges. If you have received a binder from Kinek, you can also check if your Administrator has included the receiving fee in the Depot Profile section.</p>
						
						<br />
						
						<p><strong>When do we pay our Kinek charges?</strong></p>
						<p>You will be billed monthly unless otherwise noted. If fewer than 5 packages are delivered to your location in any particular month, you may not be billed until the next month.</p>
						
						<br />
						
						<p><strong>There is no Kinek# on the package, what do I do?</strong></p>
						<p>Simply hit the unknown Kinek# on the Accept Delivery screen and search for the customer by name and/or phone number (if provided on the package). If there is more than one person with the exact same name and/or phone number, then select all of the potential matches and click 'Send Notification(s)'. If you find an exact match, then select them and click 'Send Notification(s)'.</p>
						<p>If you selected multiple possible recipients one will arrive at your location with a proof of purchase and their Kinek#. Accept the Delivery in the system then immediately Pick-Up the package.</p>
						
						<br />
						
						<p><strong>A package was too large for my location, so I redirected it. The courier left a delivery notice but I forgot to get the Kinek# before he left. What do I do?</strong></p>
						<p>Log into Depot Management and enter the details into Redirect Delivery. Click ?Unknown Kinek#? and search for the user by name and/or phone number. Select the recipient and click 'Send Notification(s)' if there is only one possible candidate. If two or more people have the exact same name, select all that apply and click 'Send Notification(s)'.</p>

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