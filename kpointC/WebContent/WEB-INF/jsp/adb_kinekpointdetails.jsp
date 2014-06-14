<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
		<link rel="stylesheet" href="resource/css/pages/kinekpointdetails.css" type="text/css" media="screen,projection" />		
			
		<div class="clearfix"></div>
		<br class="clear">
		<div id="adbModule" class="searchresults">	
			<div id="returnKPsBox">					  	
				<s:link name="returnToMyKinekPoints" beanclass="org.webdev.kpoint.action.MyKinekPointsActionBean" class="buttonLink" >Back to My KinekPoints</s:link>
			</div>	
			
			<div id="pageTitle">
				<h1>KinekPoint Details</h1>
			</div>
				<s:errors />
				<s:messages />	
				
					
			<div id="tabs">
				<ul>					
					<li id="detailTb"><a href="#detailId">Details</a></li>
					<li id="mapTb"><a href="#mapId">Map</a></li>					
					<li id="directionTb"><a href="#directionId">Directions</a></li>					
				</ul>
				
				<div id="mapId">		
					<div id="map" style="position:relative; width:100%; height:400px;"></div> 			
				</div>
				<div id="directionId">			
 						<div id="directions"></div>														
					<div id="direction_map" style="position:relative; width:540px; height:400px;"></div>
				</div>
				<div id="detailId">					
					<table id="detailTable" class="superplain details detailswrapper">
						<thead>
							<tr>
								<th colspan="2">KinekPoint Details</th>
							</tr>
						</thead>
						<tbody id="detailsBody">
							<tr>
								<td>
									<table class="superplain details notop left">
										<tbody>
											<tr>
												<td class="bold labelcol">Location Name</td>
												<td class="valcol">
													<span id="nameField">${actionBean.depot.name}</span><br />
												</td>
											</tr>
											<tr>
												<td class="bold labelcol">Address</td>
												<td class="valcol">
													<span id="address1Field">${actionBean.depot.address1}, #${actionBean.kinekNumber}</span><br />
													<span id="nameField">${actionBean.depot.name}</span><br />													
													<span id="cityField">${actionBean.depot.city}</span>, <span id="stateField">${actionBean.depot.state.stateProvCode}</span><br />
													<span id="zipField">${actionBean.depot.zip}</span>
													<br /><br />
												</td>
											</tr>
											<tr>
												<td class="bold labelcol">Phone</td>
												<td class="valcol">
													<span id="phoneField">${actionBean.depot.phone}</span>
												</td>
											</tr>
										</tbody>
									</table>
								</td>
								
								<c:choose>
								<c:when test="${actionBean.hasPackageRates}">
									<td><span id="ratesLabel" class="bold">Price per Package</span><br />
									<div id='ratesTable' style="margin-top:5px">
									<table>
										<c:forEach items="${actionBean.depot.kpPackageRates}" var="rate" varStatus="status">
											<tr><td style="width:150px">${rate.unifiedPackageRate.packageWeightGroup.friendlyLabel}</td>
											<td><fmt:formatNumber value="${rate.actualFee}" type="currency"></fmt:formatNumber></td></tr>
										</c:forEach>
										<tr><td>Skid/Pallet Fee</td>
											<c:choose>
												<c:when test="${(actionBean.kpSkidRate != null)}">
													<td><fmt:formatNumber value="${actionBean.kpSkidRate.actualFee}" type="currency"></fmt:formatNumber></td>
												</c:when>
												<c:otherwise>
													<td>Not supported</td>
												</c:otherwise>
											</c:choose>
										</tr>
									</table>						
									</div></td>
								</c:when>
								<c:otherwise>
									<td></td>
								</c:otherwise>
								</c:choose>
								
							</tr>
							<tr>
								<td class="bottomRow" style="padding-top:20px;"><span class="bold">Hours of Operation</span><br />
								<table class="superplain details hours notop left" style="margin-top:5px">
										<tbody>
											<tr id="mondayRow">
												<td class="labelcol">Monday</td>
												
												<c:choose>
													<c:when test="${actionBean.depot.operatingHours.closedMonday}">
														<td class="valcol closed">Closed</td>
													</c:when>
													<c:otherwise>
													<td class="valcol open">
														<span id="mondayStartField"><fmt:formatDate value="${actionBean.depot.operatingHours.mondayStart}" type="both" pattern="h:mm a" /></span>
														&nbsp;to&nbsp;
														<span id="mondayEndField"><fmt:formatDate value="${actionBean.depot.operatingHours.mondayEnd}" type="both" pattern="h:mm a" /></span>
													</td>
													</c:otherwise>
												</c:choose>	
											</tr>
											<tr id="tuesdayRow">
												<td>Tuesday</td>
												
												<c:choose>
													<c:when test="${actionBean.depot.operatingHours.closedTuesday}">
														<td class="valcol closed">Closed</td>
													</c:when>
													<c:otherwise>
													<td class="valcol open">
														<span id="tuesdayStartField"><fmt:formatDate value="${actionBean.depot.operatingHours.tuesdayStart}" type="both" pattern="h:mm a" /></span>
														&nbsp;to&nbsp;
														<span id="tuesdayEndField"><fmt:formatDate value="${actionBean.depot.operatingHours.tuesdayEnd}" type="both" pattern="h:mm a" /></span>
													</td>
													</c:otherwise>
												</c:choose>	
											</tr>
											<tr id="wednesdayRow">
												<td>Wednesday</td>
												
												<c:choose>
													<c:when test="${actionBean.depot.operatingHours.closedWednesday}">
														<td class="valcol closed">Closed</td>
													</c:when>
													<c:otherwise>
													<td class="valcol open">
														<span id="wednesdayStartField"><fmt:formatDate value="${actionBean.depot.operatingHours.wednesdayStart}" type="both" pattern="h:mm a" /></span>
														&nbsp;to&nbsp;
														<span id="wednesdayEndField"><fmt:formatDate value="${actionBean.depot.operatingHours.wednesdayEnd}" type="both" pattern="h:mm a" /></span>
													</td>
													</c:otherwise>
												</c:choose>	
											</tr>
											<tr id="thursdayRow">
												<td>Thursday</td>
												
												<c:choose>
													<c:when test="${actionBean.depot.operatingHours.closedThursday}">
														<td class="valcol closed">Closed</td>
													</c:when>
													<c:otherwise>
													<td class="valcol open">
														<span id="thursdayStartField"><fmt:formatDate value="${actionBean.depot.operatingHours.thursdayStart}" type="both" pattern="h:mm a" /></span>
														&nbsp;to&nbsp;
														<span id="thursdayEndField"><fmt:formatDate value="${actionBean.depot.operatingHours.thursdayEnd}" type="both" pattern="h:mm a" /></span>
													</td>
													</c:otherwise>
												</c:choose>	
												
											</tr>
											<tr id="fridayRow">
												<td>Friday</td>
												
												<c:choose>
													<c:when test="${actionBean.depot.operatingHours.closedFriday}">
														<td class="valcol closed">Closed</td>
													</c:when>
													<c:otherwise>
													<td class="valcol open">
														<span id="fridayStartField"><fmt:formatDate value="${actionBean.depot.operatingHours.fridayStart}" type="both" pattern="h:mm a" /></span>
														&nbsp;to&nbsp;
														<span id="fridayEndField"><fmt:formatDate value="${actionBean.depot.operatingHours.fridayEnd}" type="both" pattern="h:mm a" /></span>
													</td>
													</c:otherwise>
												</c:choose>	
											</tr>
											<tr id="saturdayRow">
												<td>Saturday</td>
												
												<c:choose>
													<c:when test="${actionBean.depot.operatingHours.closedSaturday}">
														<td class="valcol closed">Closed</td>
													</c:when>
													<c:otherwise>
													<td class="valcol open">
														<span id="saturdayStartField"><fmt:formatDate value="${actionBean.depot.operatingHours.saturdayStart}" type="both" pattern="h:mm a" /></span>
														&nbsp;to&nbsp;
														<span id="saturdayEndField"><fmt:formatDate value="${actionBean.depot.operatingHours.saturdayEnd}" type="both" pattern="h:mm a" /></span>
													</td>
													</c:otherwise>
												</c:choose>	
											</tr>
											<tr id="sundayRow">
												<td>Sunday</td>
												
												<c:choose>
													<c:when test="${actionBean.depot.operatingHours.closedSunday}">
														<td class="valcol closed">Closed</td>
													</c:when>
													<c:otherwise>
													<td class="valcol open">
														<span id="sundayStartField"><fmt:formatDate value="${actionBean.depot.operatingHours.sundayStart}" type="both" pattern="h:mm a" /></span>
														&nbsp;to&nbsp;
														<span id="sundayEndField"><fmt:formatDate value="${actionBean.depot.operatingHours.sundayEnd}" type="both" pattern="h:mm a" /></span>
													</td>
													</c:otherwise>
												</c:choose>	

											</tr>
											
											<c:choose>
													<c:when test="${actionBean.hasHours}">
														<tr id="hoursInfoRow">
															<td>Holiday Hours</td>
															<td class="valcol open">
																<span id="hoursInfoField">${actionBean.depot.operatingHours.hoursInfo}</span>
															</td>
														</tr>
													</c:when>
													<c:otherwise>
														<tr id="hoursInfoRow"><td></td><td></td></tr>
													</c:otherwise>
												</c:choose>	
											
											
										</tbody>
									</table>
								</td>
										
								<c:choose>
									<c:when test="${actionBean.hasExtendedRates}">
									<td class="bottomRow" style="padding-top:20px;"><span id="extendedRatesLabel" class="bold">Extended Storage Rates - per package per day</span><br />
										<div id='extendedRatesTable' style="margin-top:5px">
										<table>
										<c:forEach items="${actionBean.depot.kpExtendedStorageRates}" var="extended" varStatus="status">
											<c:if test="${extended.actualFee > 0}">
											<tr><td style="width:150px">${extended.unifiedExtendedStorageRate.storageWeightGroup.friendlyLabel}</td>
											<td style="width:150px">${extended.unifiedExtendedStorageRate.storageDuration.friendlyLabel}</td>
											<td><fmt:formatNumber value="${extended.actualFee}" type="currency"></fmt:formatNumber></td></tr>
											</c:if>
										</c:forEach>
										</table>
										</div>
										<c:if test="${actionBean.depot.extraInfo != null && fn:length(actionBean.depot.extraInfo) > 0}">
											<table style="margin-top:20px;">				
								 				<tr><td colspan="3"><span class="bold">Additional Info</span><br/><div style="margin-top:5px">${actionBean.depot.extraInfo}</div></td></tr>								 											 			
								 			</table>
										</c:if>
										
										</td>									
									</c:when>
									<c:otherwise>
										<td class="bottomRow" style="padding-top:20px;">
										<c:if test="${actionBean.depot.extraInfo != null && fn:length(actionBean.depot.extraInfo) > 0}">
											<table>				
								 				<tr><td><span class="bold">Additional Info</span><br/><div style="margin-top:5px">${actionBean.depot.extraInfo}</div></td></tr>								 											 			
								 			</table>
										</c:if>
										</td>
									</c:otherwise>
								</c:choose>	
	
								
							</tr>						
						</tbody>
		  			</table>		  					
			</div>	<!-- //detailId -->
			
			<c:if test="${actionBean.depot.depotId != actionBean.user.depot.depotId}">
			<table class="superplain">
				<tr>
					<td align="right">
					<s:form name="removeKPFrm" action="/MyKinekPoints.action">
						<s:hidden name="depotId" value="${depot.depotId}"/>
						<s:submit name="removeKinekPoint" value="Remove KinekPoint" class="button" style="font-size: 13px !important;"></s:submit>
					</s:form>
					</td>
				</tr>
			</table>
			</c:if>
		</div>

		<div id="userAddress" style="display:none">${actionBean.userAddress}</div>

		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjqRM5kPRvQCQX5qwhwD4Y4u4GKGqHpUk&sensor=false"></script>
		<script type="text/javascript" src="resource/js/KinekPointMapDirection.js"></script>
		<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerLabel.js"></script>
		<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerElement.js"></script>
		<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerEvent.js"></script>	
		
		<script type="text/javascript">

			var kpAddress = "";
			var geolat = "";
			var geolong = "";
		
			jQuery(document).ready(
				function(){
					jQuery("#tabs").tabs({			
						
						select: function(e,ui){																			
									var tabId = ui.panel.id;								
									
									if(tabId == 'mapId'){										
										setTimeout('createMap();',10); 					
									}else if(tabId == 'directionId'){										
										setTimeout('createMapDirection()',10);
									}else if(tabId == 'detailId'){
										
									}
								}
					});
														
				}
			);		

			function showDetails(depotId,glat,glong){						

				jQuery("#tabs").tabs('select',0);	
				
				document.getElementById("tabs").style.display="block";

				getDetails(depotId);				

				geolat = glat;
				geolong = glong;
				
			}

			function setKPAddress(kpAddress1,kpCity, kpZip,kpStateName){
				kpAddress = kpAddress1+", "+kpCity+", "+kpZip+", "+kpStateName;
			}
			function createMapDirection(){
				setKPAddress(jQuery("#address1Field").html(),jQuery("#cityField").html(),jQuery("#zipField").html(),jQuery("#stateField").html());
				jQuery("#directions").html("");			
				kpdir = new KinekPointMapDirection('direction_map', 'directions');	
				kpdir.setUserOrigin(jQuery("#userAddress").html());
				kpdir.setKpDestination(kpAddress);
				kpdir.getKinekPointDirectionMap();
			}			

	         var map = null;
	         
	         function GetMap()
	         {
	            // Initialize the map
	             var coordinate = new google.maps.LatLng(${actionBean.depot.geolat},${actionBean.depot.geolong});
	        	 gmap= new google.maps.Map(document.getElementById("map"),{
	        			center:coordinate,
	        			zoom: 8,
	        			mapTypeId:'roadmap',
	        			mapTypeControlOptions:{style: google.maps.MapTypeControlStyle.DROPDOWN_MENU}	
	        		});
	        	 var marker = new google.maps.Marker({
	        	      position: coordinate, 
	        	      map: gmap, 
	        	      title:jQuery("#nameField").text()
	        	  });
	        	 
	         }

	         function createMap()
	         {
	        	 GetMap();  
	         }
		</script>	
			
		</div>
	</s:layout-component>
</s:layout-render>