<html>
<head>
<base target="_parent" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjqRM5kPRvQCQX5qwhwD4Y4u4GKGqHpUk&sensor=false"></script>	
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/prototype/1/prototype.js"></script>	
<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerLabel.js"></script>
<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerElement.js"></script>
<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerEvent.js"></script>
<script type="text/javascript" src="resource/js/KinekPointLocator.js"></script>
<script type="text/javascript" src="resource/js/AdditionalDepotInfo.js" ></script>
<script type="text/javascript" src="resource/js/infobubble-compiled.js"></script>
<script type="text/javascript" src="resource/js/time.js"></script>
<script type="text/javascript" src="resource/wordpress/js/wp_findkp_result.js"></script>
<script type="text/javascript" src="resource/js/jScrollPane.js"></script>
<script type="text/javascript" src="resource/js/jquery.mousewheel.js"></script>
<link type='text/css' rel='stylesheet' href='resource/css/plugins/jScrollPane.css'>
<link type='text/css' rel='stylesheet' href='resource/wordpress/css/wp_search.css'>
<link type='text/css' rel='stylesheet' href='resource/wordpress/css/wp_general.css'>
<link type='text/css' rel='stylesheet' href='resource/wordpress/css/wp_messages.css'>
<!--[if IE ]><link rel='stylesheet' href='resource/wordpress/css/wp_ie8.css' type='text/css' media='screen,projection' /><![endif]-->
<!--[if IE 7]><link rel='stylesheet' href='resource/wordpress/css/wp_ie7.css' type='text/css' media='screen,projection' /><![endif]-->
</head>
<body>
<p style="margin-top:-20px"><script type="text/javascript">jQuery(document).ready(function(){loadInitialCordinate('${actionBean.criteria}');jQuery('#findkpWrapper').show();});</script></p>
<div id="findkpWrapper" style="display:none">	
	<div id="findkpContentWrapper">	
		<a name="returnToSearch" href="/find-a-kinekpoint" id="returnLink">Return to Search</a>
		<ul id="maptabs" class="noListStyle">
			<li id="resultsTab" class="active"><span>Results</span></li>
		</ul>		
		<div id="searchWrapper" class="clearfix" style="position:relative">
			<div id="results">
				<p><small>You searched "<span>${actionBean.displayCriteria}</span>".</small></p>
				<p id="noKp" class="hiddenFindKp">
					<small>There were no KinekPoints found in your original search area.  
						We have expanded the search to show you the closest KinekPoints.  
						</small>
				</p>
				<p id="foundKp" class="hiddenFindKp">
					<small>There are <span id="depotCount">0</span> KinekPoints available in this area</small>
				</p>
				<!-- This form tag is necessary to hold the hiddent values. -->
				<form name="findDepots" id="findDepots" method="post" action="WPFindKinekPoint.action?findDepots">
					<div id="listWrapper1" class="scroll-pane">
						<ul id="list" class="noListStyle"></ul>
					</div>
					<input type="hidden" name="criteria" id="criteria" value="${actionBean.criteria}" />
					<input type="hidden" name="displayCriteria" id="displayCriteria" value="${actionBean.displayCriteria}" />
					<input type="hidden" name="radius" id="radius" value="${actionBean.radius}" />
					<input type="hidden" name="maxLongitude" id="maxLongitude" value="${actionBean.maxLongitude}" />
					<input type="hidden" name="	minLongitude" id="minLongitude" value="${actionBean.minLongitude}" />
					<input type="hidden" name="maxLatitude" id="maxLatitude" value="${actionBean.maxLatitude}" />
					<input type="hidden" name="minLatitude" id="minLatitude" value="${actionBean.minLatitude}"/>
					<input type="hidden" name="searchPointLatitude" id="searchPointLatitude" value="${actionBean.searchPointLatitude}" />
					<input type="hidden" name="searchPointLongitude" id="searchPointLongitude" value="${actionBean.searchPointLongitude}"/>
					<input type="hidden" name="borderProvinceId" id="borderProvinceId" value="${actionBean.borderProvinceId}" />			
 				</form>
			</div> <!-- //result -->	
			
				<!-- DIRECTIONS -->
				<div id="directions">
					<p id="notLoginMsg" class="bold">Please sign up and select a KinekPoint to get directions</p>
				</div>
				<!-- /DIRECTIONS -->
			
				<!-- MAPCONTAINER -->
				<div id="mapContainer">					
					<div id="map" style="border: 1px solid E4E4E4;position:relative;"></div>					
				</div>	
			<!-- /MAPCONTAINER -->
		</div> <!-- //searchWrapper -->
	</div> <!-- //contentWrapper -->
</div><!-- //wrapper -->
</body>
</html>