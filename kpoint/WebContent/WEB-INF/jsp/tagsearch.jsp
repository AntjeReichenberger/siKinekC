<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-definition>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
			
			<!-- HELPPANEL -->
 			<div id="helpPanel" class="clearfix">
			
				<!-- HELPVIDEO -->
				<div id="helpVideo">
					<!-- HELPVIDEOPLAYER -->
					<div id="helpVideoPlayer">
						<a href="resource/videos/video_unknowntag.swf" width="640" height="498" title="Accepting a Delivery with Unknown Kinek# :: :: width: 640, height: 498, flashvars: 'autostart=true'" title="Accepting a Delivery with Unknown Kinek#" rel="flash" class="lightview">
							<img src="resource/videos/video_unknowntag.jpg" width="252" height="188" alt="Accepting a Delivery with Unknown Kinek#" />
						</a>	
					</div>
					<!-- /HELPVIDEOPLAYER -->
				</div>
				<!-- /HELPVIDEO -->
			
				<!-- HELPTEXT -->
				<div id="helpText" style="background-image: url(resource/samples/sample_helpgraphic.png);">
					<h3>Unknown Kinek#</h3>
					<p>To find the correct person you are looking for, you can either search by Kinek#, partial Kinek#, first name, last name, phone number, or a combination of the three (the more information you can provide, the more precise the match). If you find the person’s name and it matches the one on the package, then select him/her and click “Send Notification(s)”. If you cannot narrow it down to just one person, select all possible recipients and click continue. This will send an email notifying the customers that there may be a package at your location for them. The sender will come to your location and provide a proof of purchase to verify that they are the owner of the package. In this case only, proof of purchase is required in addition to Kinek# and ID.</p>
				</div>
				<!-- /HELPTEXT -->
			
				<!-- CLOSEBTN -->
				<a href="javascript:void();" title="Show Instructional Video" id="helpPanel_closebtn"></a>
				<!-- /CLOSEBTN -->

			</div>
			<!-- /HELPPANEL --> 

	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">


		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<!-- INTERFACEWRAPPER -->
			<div id="interfaceWrapper">
				
				<!-- INTERFACE -->				
				<div id="interface">
					${contents}              	 
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
</s:layout-definition>