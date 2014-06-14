<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard-kpresults.css" type="text/css" />
	<script src="resource/js/time.js" type="text/javascript"></script>
	
	<style type="text/css">
		#hoursInfoField p {
			margin: 0px;
			padding: 0px;	
		}
		
		#extraInfoField p {
			margin: 0px;
			padding: 0px;
		}
	</style>
	
	<script type="text/javascript">
		jQuery(document).ready(function(){
			if (jQuery('#showDirectionsTab').val() == "true") {
				jQuery('#selected').hide();
				jQuery('#saveKPBox').hide();
				jQuery('#maptabs').hide();
				jQuery('#pageTitle').show();
				jQuery('#startAddressTxt').val('');
				jQuery('#startAddress').show();
				jQuery('#results').hide();
				jQuery('#directions').show();
				setOrigin(jQuery('#userProfileAddress').val());
				var selected = jQuery('#list li.selected');
				showDirections(selected);
				//setOriginCity(jQuery('#getUserProfileCity').val());
				//showDirections();
			}
			
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
					hideStartAddressPanel();
					jQuery('#startAddress').hide();
				}
				else if(self.html() == 'Directions' && jQuery('#directions').is(':hidden')) {
					
					jQuery('#startAddressTxt').val('');
					jQuery('#startAddress').show();
					jQuery('#results').hide();
					jQuery('#directions').show();
					var selected = jQuery('#list li.selected');
					setOrigin(jQuery('#userProfileAddress').val());
					showDirections(selected);
				}
			});
			loadInitialCordinateForLoginUser('${actionBean.criteria}','${actionBean.userProfileAddress}', jQuery('#saveKPButton'));
		});

		function showStartAddressPanel(){
			jQuery('div#startAddressPanel').slideDown();
		}

		function hideStartAddressPanel(){
			jQuery('div#startAddressPanel').slideUp();
		}

		function getStartAddressDirection(){
			hideStartAddressPanel();
			setOrigin(jQuery('#startAddressTxt').val());
				showDirections(jQuery('#list li.selected'));
		}

		function startAddressTxt_onKeyPress(e){
			var intKey;
			if (window.event) {
				intKey = e.keyCode;
			}
			else {
				intKey = e.which;
			}
			
			if (intKey == 13) {
				if (window.event) {
					e.returnValue = false;
				    e.cancel = true;
				}
				getStartAddressDirection();
				return false;
			}
			return true;
		}
	</script>
	
	<script type="text/javascript">
		window.onload = function() {  
			var is_chrome = navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
			if (is_chrome)
			{
				var cssCode = "table.details thead { border-bottom: 1px solid #058FD3 !important; }";
				cssCode += ".directionsCell { border-bottom: 1px solid #000 !important; }";
	            
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
	
	<br class="clear" />
	
	<div id="adbModule" class="searchresults">
		<!-- CONTENT -->
		<s:errors />
		<s:form name="findDepots" id="findDepots" action="/ChooseDefaultKinekPoint.action">
		
		<div id="pageTitle" class="directionsTitle">
			<h1>Get Directions</h1>
		</div>
		
		<div id="saveKPBox" class="clearfix">					  	
		  	<span class="saveKPSpan">
				<s:link name="returnToSearch" beanclass="org.webdev.kpoint.action.ChooseDefaultKinekPointActionBean" class="buttonLink" >Return to Search
					<s:param name="returnFromResults" value="true"/>
					<s:param name="borderProvinceId" value="${actionBean.borderProvinceId}"/>
				</s:link>
				<s:hidden name="depotId" id="depotId" />
			</span> 	
			
		</div>
		<s:hidden name="showDirectionsTab" id="showDirectionsTab"></s:hidden>
		<s:hidden name="directionsDestination" id="directionsDestination"></s:hidden>
	
		<div class="clearfix"></div>
	
		<!-- HIDDEN SELECTED -->
		<div id="moreselected" class="jQuerySlideFix">
			<table class="superplain details notop">
				<thead>
					<tr>
						<th colspan="4">Price per Package</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="bold labelcol">Receiving Fee</td>
						<td class="valcol">
							$<span id="receivingFeeField"></span>
						</td>
						<td class="bold labelcol">Parcel Size Allowance</td>
						<td class="valcol">
							<span id="sizeAllowanceField"></span>
						</td>
					</tr>		
				</tbody>			
		  	</table>

			<table class="superplain details notop" id="additionalInfoTable">
				<thead>
					<tr>
						<th>Additional KinekPoint Information</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<span id="extraInfoField"></span>
						</td>
					</tr>
				</tbody>
			</table>
			
			<table id="featuresSection" class="superplain details notop">
				<thead>
					<tr>
						<th colspan="4">Services &amp; Features</th>
					</tr>
				</thead>
				<tbody id="featuresBody">
					<tr>
						<td class="bold labelcol">Service Available In</td>
						<td class="valcol">
							<span id="languagesField"></span>
						</td>
						<td class="bold labelcol">Methods of Payment</td>
						<td class="valcol">
							<span id="paymentMethodsField"></span> 
						</td>
					</tr>
					<tr>
						<td class="bold labelcol">Parcels with Duty &amp; Taxes</td>
						<td class="valcol">
							<span id="dutyAndTaxField"></span>
						</td>
						<td class="bold labelcol">Other Features</td>
						<td class="valcol">
							<span id="featuresField"></span>
						</td>
					</tr>
					<tr>
						<td class="bold labelcol">Accepted Credit Cards</td>
						<td class="valcol">
							<span id="creditCardsField"></span>
						</td>			
					</tr>		
				</tbody>			
		  	</table>	
		</div>
		<!-- /HIDDEN SELECTED -->
	
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
						<s:link beanclass="org.webdev.kpoint.action.DashboardSuggestAKinekPointActionBean" title="Suggest a new KP location">click here</s:link>.
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
					<s:hidden name="userProfileAddress" id="userProfileAddress" />
					<s:hidden name="getUserProfileCity" id="getUserProfileCity" />
				</fieldset>
			</div> 
			<!-- /RESULTS -->
			
			<!-- hidden direction -->				
			<div id="startAddress">
				<a class="startAddressLink" href="javascript:showStartAddressPanel()">Click here to change your start address</a>				
				<div id="startAddressPanel">						
					<span>From:</span>
					<input type="text" class="input" id="startAddressTxt" name="borderSelectedKPTxt" onkeypress="return startAddressTxt_onKeyPress(event);"/>														
					<span id="startAddressBtn" onClick="getStartAddressDirection()">GO</span>												
				</div>							
			</div>
			<!-- //hidden direction -->
			
			<!-- DIRECTIONS -->
			<div id="directions">
			</div>
			<!-- /DIRECTIONS -->

			<!-- MAPCONTAINER -->
			<div id="mapContainer">
	
				<!-- MAP -->
				<div id="map" onclick="map_OnClick()" style="position:relative; width:636px; height:500px;"></div>
				<div id="direction_map" style="position:relative; width:636px; height:500px;"></div>
				<!-- /MAP -->				
				 				 
				<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjqRM5kPRvQCQX5qwhwD4Y4u4GKGqHpUk&sensor=false"></script>
				
				<script type="text/javascript" src="resource/js/KinekPointLocator.js"></script>
				<script type="text/javascript" src="resource/js/KinekPointMapDirection.js"></script>
				<script type="text/javascript" src="resource/js/infobubble-compiled.js"></script>
				<script type="text/javascript" src="resource/js/AdditionalDepotInfo.js"></script>
				<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerLabel.js"></script>
				<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerElement.js"></script>
				<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerEvent.js"></script>	
			</div>
			<!-- /MAPCONTAINER -->	
			
		</div>
		<!-- /SEARCHWRAPPER -->
		
		</s:form>

		<br class="clear" />
	</div>

</s:layout-component>
</s:layout-render>
