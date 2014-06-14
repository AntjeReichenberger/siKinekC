<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Choose A KinekPoint</s:layout-component>

	<s:layout-component name="body">	
	<link rel="stylesheet" href="resource/css/pages/chooseakinekpoint.css" type="text/css" media="screen,projection" />
	
	<script src="resource/js/time.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		jQuery(document).ready(function(){
			if ("${actionBean.showAllTabs}" == "true") {
            	jQuery('#nav li:eq(4)').addClass('active');
			}
            else {
            	jQuery('#nav li:eq(0)').addClass('active');
            }
			
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
					hideStartAddressPanel();
					jQuery('#startAddress').hide();
				}
				else if(self.html() == 'Directions' && jQuery('#directions').is(':hidden')) {
					closeInfoWindows();
					jQuery('#startAddressTxt').val('');
					jQuery('#startAddress').show();
					jQuery('#results').hide();
					jQuery('#directions').show();
					var selected = jQuery('#list li.selected');
					setOrigin(jQuery('#userProfileAddress').val());
					showDirections(selected);
				}
			});

			window.scroll(0, 1000);
			loadInitialCordinateForLoginUser('${actionBean.criteria}','${actionBean.userProfileAddress}', jQuery('#saveKPButton'));
			window.scroll(0, 0);
			
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

	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content">
	
			<img src="resource/images/RegistrationProgressBarChooseKP.gif" id="profileprogress"></img>
	
			<s:form name="findDepots" id="findDepots" action="/ChooseAKinekPoint.action">
				<div id="saveKPBox" class="clearfix">
				  	<span class="saveKPSpan">
						<s:link name="returnToSearch" beanclass="org.webdev.kpoint.action.ChooseAKinekPointActionBean" class="buttonLink" >Return to Search
							<s:param name="returnFromResults" value="true"/>
							<s:param name="borderProvinceId" value="${actionBean.borderProvinceId}"/>
						</s:link>	
						<s:hidden name="depotId" id="depotId" />			
						<!--<s:submit name="setDefault" value="Save KinekPoint" class="buttonDisabled" id="saveKPButton" disabled="true" />-->
					</span>
				</div>
		
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
						<div id="map" style="position:relative; width:636px; height:500px;"></div>
						<div id="direction_map" style="position:relative; width:636px; height:500px;"></div>
						<!-- /MAP -->				
						 				 
						<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjqRM5kPRvQCQX5qwhwD4Y4u4GKGqHpUk&sensor=false"></script>
						<script type="text/javascript" src="resource/js/AdditionalDepotInfo.js"></script>
						<script type="text/javascript" src="resource/js/KinekPointLocator.js"></script>
						<script type="text/javascript" src="resource/js/infobubble-compiled.js"></script>
						<script type="text/javascript" src="resource/js/KinekPointMapDirection.js"></script>
						<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerLabel.js"></script>
						<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerElement.js"></script>
						<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerEvent.js"></script>	
					</div>
					<!-- /MAPCONTAINER -->	
				</div>
				<!-- /SEARCHWRAPPER -->
				
			</s:form>
		</div>
		<!-- /CONTENT -->
	</div>
	<!-- /CONTENTWRAPPER -->

	</s:layout-component>
</s:layout-render>

