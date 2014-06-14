	<!-- HELPPANEL -->
	<div id="helpPanel" class="clearfix">
		
		<!-- HELPVIDEO -->
		<div id="helpVideo">
			
			<!-- HELPVIDEOPLAYER -->
			<div id="helpVideoPlayer"></div>
			<script type="text/javascript">
			// <![CDATA[
				var flashvars = {};
				
				var params = { 
					allowfullscreen: "true",
					allowscriptaccess: "always"
				};
			
				swfobject.embedSWF("resource/flash/helpVideoPlayer.swf", "helpVideoPlayer", "252", "188", "9.0.0", "resource/flash/expressInstall.swf", flashvars, params);
			// ]]>
			</script>			
			
			<!-- /HELPVIDEOPLAYER -->
			
		</div>
		<!-- /HELPVIDEO -->
		
		<!-- HELPTEXT -->
		<div id="helpText" style="background-image: url(resource/samples/sample_helpgraphic.png);">
			<h3>Accepting a delivery</h3>
			<p>In order to successfully accept a package into your depot you must fill out the required fields in the form below. Beginning with the Courier name, this field corresponds to the company who delivered the package to your depot. Secondly the Kinek number, this field is unique to the sender and will be used when verifying the package at the pick-up stage.</p>
		</div>
		<!-- /HELPTEXT -->
		
		<!-- CLOSEBTN -->
		<a href="javascript:void();" onclick="toggleHelpPanel();" title="" id="helpPanel_expandhelp"></a>
		<!-- /CLOSEBTN -->
		
	</div>
	<!-- HELPPANEL -->		