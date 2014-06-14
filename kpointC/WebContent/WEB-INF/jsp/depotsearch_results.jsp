<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Search for KinekPoints</s:layout-component>

	<s:layout-component name="body">	
	<link rel="stylesheet" href="resource/css/pages/depotsearch.css" type="text/css" media="screen,projection" />
	
	<script src="resource/js/time.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		jQuery(document).ready(function(){
			jQuery('#nav li:eq(5)').addClass('active');
			//Toggle additional kp details
			jQuery('a.more').toggle(function() {
				var moreDiv = jQuery("div#moreselected");
				moreDiv
					.height(moreDiv.height())
					.width('auto')
					.hide()
					.removeClass("jQuerySlideFix")				
					.slideDown();
				jQuery('a.more').html('Hide pricing details...');
				
			}, function() {
				var moreDiv = jQuery("div#moreselected");
				moreDiv.width(moreDiv.width());
				moreDiv.slideUp(function () {
					moreDiv
						.addClass("jQuerySlideFix")
						.show();
				});
				jQuery('a.more').html('Show pricing details...');
			});

			//Map tab changing
			jQuery('ul#maptabs span').click(function(e) {
				e.preventDefault();
				var self = jQuery(this);
				var item = self.closest('li');
				var tabList = jQuery('ul#maptabs');
				tabList.find('li').removeClass('active');
				item.addClass('active');
				
				if(self.html() == 'Results' && jQuery('#results').is(':hidden')) {
					jQuery('#results').show();
					jQuery('#directions').hide();
					hideDirections();
					document.getElementById("notLoginMsg").style.display='none';
				}
				else if(self.html() == 'Directions' && jQuery('#directions').is(':hidden')) {
					jQuery('#results').hide();
					jQuery('#directions').show();		
					document.getElementById("notLoginMsg").style.display='block';				
				}else{
					document.getElementById("notLoginMsg").style.display='none';
					}
			});
			loadInitialCordinate('${actionBean.criteria}');
		});
	</script>
	
	<script type="text/javascript">
		window.onload = function() {  
			var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
			if (is_chrome)
			{
				var cssCode = "table.details thead { border-bottom: 1px solid #058FD3 !important; }";
	            
	            var styleElement = document.createElement("style");
	            styleElement.type = "text/css";
	            if (styleElement.styleSheet) {
	                styleElement.styleSheet.cssText = cssCode;
	            } else {
	                styleElement.appendChild(document.createTextNode(cssCode));
	            }
	            document.getElementsByTagName("head")[0].appendChild(styleElement);
			}
	    };
	</script>

	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<s:link name="returnToSearch" beanclass="org.webdev.kpoint.action.FindDepotActionBean" class="buttonLink" id="returnLink" >Return to Search</s:link>
	
		<ul id="maptabs">
			<li id="resultsTab" class="active"><span>Results</span></li>
			<li id="directionsTab"><span>Directions</span></li>
		</ul>
	
		<!-- SEARCHWRAPPER -->
		<div id="searchWrapper" class="clearfix">

			<!-- RESULTS -->
			<div id="results">
				<p><small>You searched "<span>${actionBean.displayCriteria}</span>".</small></p>
				<p id="noKp" class="hidden">
					<small>
						There were no KinekPoints found in your original search area.  
						We have expanded the search to show you the closest KinekPoints.  
						If you'd like to suggest a closer KinekPoint location, 
						<s:link beanclass="org.webdev.kpoint.action.SuggestAKinekPointActionBean" title="Suggest a new KP location">click here</s:link>.
					</small>
				</p>
				<p id="foundKp" class="hidden">
					<small>
						There are <span id="depotCount">0</span> KinekPoints available in this area
					</small>
				</p>
				
				<div id="errors"></div>
				<stripes:errors/>
				<stripes:messages/>
		
				<s:form name="findDepots" id="findDepots" action="/DepotSearch.action" >

					<!-- RESULTSLIST -->
					<div id="listWrapper1" class="scroll-pane">
						<ul id="list"></ul>
					</div>
					<!-- /RESULTSLIST -->
				
					<fieldset>
						<s:hidden name="criteria" id="criteria" />
						<s:hidden name="displayCriteria" id="displayCriteria" />
						<s:hidden name="radius" id="radius" />
						<s:hidden name="maxLongitude" id="maxLongitude" />
						<s:hidden name="minLongitude" id="minLongitude" />
						<s:hidden name="maxLatitude" id="maxLatitude" />
						<s:hidden name="minLatitude" id="minLatitude" />
						<s:hidden name="searchPointLatitude" id="searchPointLatitude" />
						<s:hidden name="searchPointLongitude" id="searchPointLongitude" />
						<s:hidden name="borderProvinceId" id="borderProvinceId" />
					</fieldset>
				 </s:form>
			</div>
			<!-- /RESULTS -->
			
			<!-- DIRECTIONS -->
			<div id="directions">
				
			</div>
			<!-- /DIRECTIONS -->
			<p id="notLoginMsg" class="bold">Please sign up and select a KinekPoint to get directions</p>
			<!-- MAPCONTAINER -->
			<div id="mapContainer">
				<!-- MAP -->
				<div id="map"></div>
				<div id="direction_map"></div>
				<!-- /MAP -->

				<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjqRM5kPRvQCQX5qwhwD4Y4u4GKGqHpUk&sensor=false"></script>
				<script type="text/javascript" src="resource/js/KinekPointLocator.js"></script>
				<script type="text/javascript" src="resource/js/KinekPointMapDirection.js"></script>
				<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerLabel.js"></script>
				<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerElement.js"></script>
				<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerEvent.js"></script>	
			</div>

			<!-- /MAPCONTAINER -->

		</div>
	<!-- /SEARCHWRAPPER -->
	<div id="hiddenFieldForSelectedKP"></div>
</div>
<!-- /CONTENTWRAPPER -->

	</s:layout-component>
</s:layout-render>

