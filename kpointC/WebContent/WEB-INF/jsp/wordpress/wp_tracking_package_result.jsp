<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<base target="_parent" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjqRM5kPRvQCQX5qwhwD4Y4u4GKGqHpUk&sensor=false"></script>	
<script type="text/javascript" src="resource/js/KinekPointTrack.js"></script>	
<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerLabel.js"></script>
<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerElement.js"></script>
<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerEvent.js"></script>
<script type="text/javascript" src="resource/js/jScrollPane.js"></script>
<script type="text/javascript" src="resource/js/jquery.mousewheel.js"></script>
<link type='text/css' rel='stylesheet' href='resource/wordpress/css/wp_tracking.css'>
<link type='text/css' rel='stylesheet' href='resource/wordpress/css/wp_search.css'>
<link type='text/css' rel='stylesheet' href='resource/wordpress/css/wp_general.css'>
<link type='text/css' rel='stylesheet' href='resource/wordpress/css/wp_messages.css'>
<link type='text/css' rel='stylesheet' href='resource/css/plugins/jScrollPane.css'>
<!--[if IE ]><link rel='stylesheet' href='resource/wordpress/css/wp_ie8.css' type='text/css' media='screen,projection' /><![endif]-->
<!--[if IE 7]><link rel='stylesheet' href='resource/wordpress/css/wp_ie7.css' type='text/css' media='screen,projection' /><![endif]-->

<div id="findkpWrapper" style="display: none">
	<div id="findkpContentWrapper">
		<c:choose>
			<c:when test="${actionBean.activeTrackedPackage.hasMap}">
				<script type="text/javascript">jQuery(document).ready(function(){loadGoogleMap(${actionBean.packageActivitiesJSON}); jQuery('#findkpWrapper').show()});</script>
			</c:when>
		</c:choose>
	
<c:choose>
	<c:when test="${actionBean.activeTrackedPackage.isAvailableInCourierSystem}">
		<table class="pkgtrack tracking">
			<thead>
				<tr>
					<th colspan="2" align="left">Tracking Information for ${actionBean.activeTrackedPackage.trackingNumber}</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td style="width: 160px"><b>Status:</b>
					</td>
					<td><b>${actionBean.activeTrackedPackage.status}</b>
					</td>
				</tr>
				<c:if test="${actionBean.activeTrackedPackage.arrivalDate != null}">
					<tr>
						<td style="width: 160px"><b>Arrival Date:</b>
						</td>
						<c:choose>
							<c:when
								test="${actionBean.activeTrackedPackage.arrivalDateHasTime}">
								<td><s:format
										value="${actionBean.activeTrackedPackage.arrivalDate.time}"
										formatPattern="EEEE, MMMM dd, yyyy hh:mm:ss a" />
								</td>
							</c:when>
							<c:otherwise>
								<td><s:format
										value="${actionBean.activeTrackedPackage.arrivalDate.time}"
										formatPattern="EEEE, MMMM dd, yyyy" />
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:if>
			</tbody>
		</table>

		<table class="pkgtrack tracking">
			<tbody>
				<tr>
					<td colspan="2"><b>Additional Package Information</b>
					</td>
				</tr>
				<c:if test="${actionBean.activeTrackedPackage.weight != null}">
					<tr>
						<td style="width: 160px"><b>Weight:</b>
						</td>
						<td>${actionBean.activeTrackedPackage.weight}${actionBean.activeTrackedPackage.weightType}</td>
					</tr>
				</c:if>
				<c:if test="${actionBean.activeTrackedPackage.dateShipped != null}">
					<tr>
						<td style="width: 160px"><b>Estimated Departure:</b>
						</td>
						<c:choose>
							<c:when
								test="${actionBean.activeTrackedPackage.dateShippedHasTime}">
								<td><s:format
										value="${actionBean.activeTrackedPackage.dateShipped.time}"
										formatPattern="EEEE, MMMM dd, yyyy hh:mm:ss a" />
								</td>
							</c:when>
							<c:otherwise>
								<td><s:format
										value="${actionBean.activeTrackedPackage.dateShipped.time}"
										formatPattern="EEEE, MMMM dd, yyyy" />
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:if>
				<c:if test="${actionBean.activeTrackedPackage.shipmentType != null}">
					<tr>
						<td style="width: 160px"><b>Shipping Method:</b>
						</td>
						<td>${actionBean.activeTrackedPackage.shipmentType}</td>
					</tr>
				</c:if>
				<c:if
					test="${actionBean.activeTrackedPackage.shipToAddress != null}">
					<tr>
						<td style="width: 160px"><b>Shipped To:</b>
						</td>
						<td>${actionBean.activeTrackedPackage.shipToAddress}</td>
					</tr>
				</c:if>
				<c:if
					test="${actionBean.activeTrackedPackage.shipFromAddress != null}">
					<tr>
						<td style="width: 160px"><b>Shipped From:</b>
						</td>
						<td>${actionBean.activeTrackedPackage.shipFromAddress}</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		<br />
		<div id="searchWrapper" class="clearfix">
			<c:choose>
				<c:when test="${actionBean.activeTrackedPackage.hasMap}">
					<div id="results">
						<div id="listWrapper1" class="scroll-pane trackingResults">
							<ul id="list" class="trackingResults"></ul>
						</div>
					</div>
					<div id="directions"></div>
					<div id="mapContainer">
						<div id="map"
							style="border: 1px solid E4E4E4; position: relative; width: 600px; height: 400px;"></div>
					</div>
				</c:when>
				<c:otherwise>
					<p>A map for your package could not be created at this time.</p>
				</c:otherwise>
			</c:choose>
		</div>

		</c:when>
		<c:otherwise>	
			<table class="pkgtrack tracking">
				<tbody>
					<tr>
						<td>There are currently no details available for this tracking number. It will likely be updated shortly, but if it doesn't, double check that the correct courier is selected.</td>
					</tr>
				</tbody>
			</table>	
		</c:otherwise>
		
		</c:choose>

	</div>
</div>