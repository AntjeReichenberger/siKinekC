<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>


<c:choose>
	<c:when test="${actionBean.activeTrackedPackage.hasMap}">
		<script type="text/javascript">
			function displayTrackingDetails(){
				jQuery('table tbody tr:odd').addClass('even');
				loadGoogleMap(${actionBean.packageActivitiesJSON});			
			}
		</script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
			function displayTrackingDetails(){
				//Map could not load properly
			}
		</script>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${actionBean.activeTrackedPackage.isAvailableInCourierSystem}">
		<table class="">

			<thead>
				<tr>
					<th colspan="2" valign="middle">Tracking Information for
						${actionBean.activeTrackedPackage.nickname}
						(${actionBean.activeTrackedPackage.trackingNumber})</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td style="width: 100px"><b>Status:</b>
					</td>
					<td><b>${actionBean.activeTrackedPackage.status}</b>
					</td>
				</tr>
				<c:if test="${actionBean.activeTrackedPackage.arrivalDate != null}">
					<tr>
						<td style="width: 100px"><b>Arrival Date:</b>
						</td>
						<c:choose>
							<c:when
								test="${actionBean.activeTrackedPackage.arrivalDateHasTime}">
								<td><fmt:formatDate
										value="${actionBean.activeTrackedPackage.arrivalDate.time}"
										dateStyle="full" type="both" />
								</td>
							</c:when>
							<c:otherwise>
								<td><fmt:formatDate
										value="${actionBean.activeTrackedPackage.arrivalDate.time}"
										dateStyle="full" />
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:if>
			</tbody>
		</table>

		<table>
			<tbody>
				<tr>
					<td colspan="2"><b>Additional Package Information</b>
					</td>
				</tr>
				<c:if test="${actionBean.activeTrackedPackage.weight != null}">
					<tr>
						<td style="width: 100px"><b>Weight:</b>
						</td>
						<td>${actionBean.activeTrackedPackage.weight}${actionBean.activeTrackedPackage.weightType}</td>
					</tr>
				</c:if>
				<c:if test="${actionBean.activeTrackedPackage.dateShipped != null}">
					<tr>
						<td style="width: 100px"><b>Estimated Departure:</b>
						</td>
						<c:choose>
							<c:when
								test="${actionBean.activeTrackedPackage.dateShippedHasTime}">
								<td><fmt:formatDate
										value="${actionBean.activeTrackedPackage.dateShipped.time}"
										dateStyle="full" type="both" />
								</td>
							</c:when>
							<c:otherwise>
								<td><fmt:formatDate
										value="${actionBean.activeTrackedPackage.dateShipped.time}"
										dateStyle="full" />
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:if>
				<c:if test="${actionBean.activeTrackedPackage.shipmentType != null}">
					<tr>
						<td style="width: 100px"><b>Shipping Method:</b>
						</td>
						<td>${actionBean.activeTrackedPackage.shipmentType}</td>
					</tr>
				</c:if>
				<c:if
					test="${actionBean.activeTrackedPackage.shipToAddress != null}">
					<tr>
						<td style="width: 100px"><b>Shipped To:</b>
						</td>
						<td>${actionBean.activeTrackedPackage.shipToAddress}</td>
					</tr>
				</c:if>
				<c:if
					test="${actionBean.activeTrackedPackage.shipFromAddress != null}">
					<tr>
						<td style="width: 100px"><b>Shipped From:</b>
						</td>
						<td>${actionBean.activeTrackedPackage.shipFromAddress}</td>
					</tr>
				</c:if>
			</tbody>
		</table>

		<div id="searchWrapper" class="clearfix">
			<c:choose>
				<c:when test="${actionBean.activeTrackedPackage.hasMap}">
					<!-- SEARCHWRAPPER -->

					<!-- RESULTS -->
					<div id="results">
						<div id="errors"></div>
						<stripes:errors />
						<stripes:messages />
						<!-- RESULTSLIST -->
						<div id="listWrapper1" class="scroll-pane trackingResults">
							<ul id="list" class="trackingResults"></ul>
						</div>
						<!-- /RESULTSLIST -->
					</div>
					<!-- /RESULTS -->
					<!-- DIRECTIONS -->
					<div id="directions"></div>
					<!-- /DIRECTIONS -->
					<!-- MAPCONTAINER -->
					<div id="mapContainer">
						<!-- MAP -->
						<div id="map"
							style="position: relative; width: 636px; height: 504px;"></div>
					</div>
					<!-- /MAPCONTAINER -->

				</c:when>
				<c:otherwise>
					<p>A map for your package could not be created at this time.</p>
				</c:otherwise>
			</c:choose>
			</div>
			</c:when>
			<c:otherwise>
			<table class="">
				<thead>
					<tr>
						<th valign="middle">Tracking Information for
							${actionBean.activeTrackedPackage.nickname}
							(${actionBean.activeTrackedPackage.trackingNumber})</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>There are currently no details available for this tracking number. It will likely be updated shortly, but if it doesn't, double check that the correct courier is selected.</td>
					</tr>
				</tbody>
			</table>	
			</c:otherwise>
	</c:choose>
