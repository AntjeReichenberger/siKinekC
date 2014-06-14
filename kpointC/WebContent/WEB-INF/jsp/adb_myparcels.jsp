<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
<script type="text/javascript" src="resource/js/csspopup.js"></script>
	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard-myparcels.css" type="text/css" />
	
	<div class="clearfix"></div>
	<div id="adbModule">	
	
		<!-- MYPARCELS-->
		<div id="pageTitle">
			<h1>My Packages - ${actionBean.packageStatus}</h1>
			<br />
		</div>
		
		<table id="myparcels" class="sortable">
		<thead>
			<tr>
				<th>Depot</th>
				<th>Notification Date</th>
				<th>Courier</th>
				<th>From</th>
				<c:if test="${actionBean.packageStatus == 'Pending Pick-up'}">
					<th class="hideSortable">Has this been picked up?
						<img src="resource/images/help_icon.png" onmouseover="openInfoDialog('infoPopUpDiv', this)" onmouseout="closeInfoDialog('infoPopUpDiv')"></img>
					</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:if test="${actionBean.totalRecords == 0}">
				<c:choose>
					<c:when test="${actionBean.packageStatus == 'Pending Pick-up'}">
						<td colspan="5">We could not find any packages associated with your account.</td>
					</c:when>
					<c:otherwise>
						<td colspan="4">We could not find any packages associated with your account.</td>
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:forEach items="${actionBean.filteredPackageReceipts}" var="pr" varStatus="loop">
				<c:forEach items="${pr.packages}" var="p" varStatus="loop" >
					<tr>
						<td>${pr.kinekPoint.name}</td>
						<td><fmt:formatDate value="${pr.receivedDate}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>
						<td>${p.courier.name}</td>
						<td>${p.shippedFrom}</td>
						<c:if test="${actionBean.packageStatus == 'Pending Pick-up'}">
							<td><a href="#" onclick="savePackageId(${p.packageId}); openDialog('popUpDiv')">Mark package as picked up</a></td>
						</c:if>
					</tr>
				</c:forEach>
			</c:forEach>												
		</tbody>
		</table>
		
		<s:form action="/MyParcels.action" class="myparcels">
			<div id="pagingcontrols">
				<c:choose>
					<c:when test="${actionBean.currentPage != 0}">
						<s:submit name="firstPage" value="First" class="buttonLink" />
					</c:when>
					<c:otherwise>
					 	<s:label name="firstPage" class="nolink">First</s:label>
				 	</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${actionBean.currentPage != 0}">
						<s:submit name="previousPage" value="Prev" class="buttonLink" />
					</c:when>
					<c:otherwise>
					 	<s:label name="previousPage" class="nolink">Prev</s:label>
				 	</c:otherwise>
				</c:choose>
				
				<s:label name="leftBracketLabel"  class="nolink pageinfo">[</s:label><s:label name="resultStartIndex" class="nolink pageinfo">${actionBean.resultStartIndex}</s:label>
				<c:choose>
					<c:when test="${actionBean.resultStartIndex >= actionBean.totalRecords}">
						<s:label name="ofLabel"  class="nolink pageinfo">of</s:label> <s:label name="resultTotalRecords"  class="nolink pageinfo">${actionBean.totalRecords}</s:label> <s:label name="resultsLabel"  class="nolink pageinfo">results]</s:label>
					</c:when>
					<c:otherwise>
						<s:label name="dashLabel"  class="nolink pageinfo">-</s:label> <s:label name="resultEndIndex"  class="nolink pageinfo">${actionBean.resultEndIndex}</s:label> <s:label name="ofLabel2"  class="nolink pageinfo">of</s:label> <s:label name="resultTotalRecords" class="nolink pageinfo">${actionBean.totalRecords}</s:label> <s:label name="resultsLabel2" class="nolink pageinfo">results]</s:label>
				 	</c:otherwise>	 	
				</c:choose>
				
				<c:choose>
					<c:when test="${actionBean.resultEndIndex < actionBean.totalRecords}">
						<s:submit name="nextPage" value="Next" class="buttonLink" />
					</c:when>
					<c:otherwise>
					 	<s:label name="nextPage" class="nolink">Next</s:label>
				 	</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${actionBean.resultEndIndex < actionBean.totalRecords}">
						<s:submit name="lastPage" value="Last" class="buttonLink" />
					</c:when>
					<c:otherwise>
					 	<s:label name="lastPage" class="nolink">Last</s:label>
				 	</c:otherwise>
				</c:choose>
			</div>
			<s:hidden name="packageStatus"></s:hidden>
		</s:form>

		<div id="dialog-modal" title="Has this been picked up?" class="hidden">
			Are you sure this package has been picked up?
			<s:form beanclass="org.webdev.kpoint.action.MyParcelsActionBean" class="myparcels" >
				<s:submit name="packageAlreadyPickedUp" id="packageAlreadyPickedUp" value="yes"></s:submit>
				<s:hidden name="packageId" id="packageId"></s:hidden>
				<s:hidden name="packageStatus"></s:hidden>
			</s:form>
		</div>
		
		<div id="infoPopUpDiv" title="Has this been picked up?" class="hidden infoPopUpDiv">
			If you have already picked up this package you can click the "Mark package as picked up" link to change its status to "Picked-up".
		</div>
	</div>
	
	<script type="text/javascript" src="resource/js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			jQuery("#myparcels").tablesorter({
				headers: {
					4: {
						sorter: false
					}
				}
			});
			
			jQuery('#dialog-modal').dialog({
				autoOpen: false,
				height: 140,
				width: 320,
				resizable: false,
				modal: true,
				buttons: {
					"No": function() {
						closeDialog('popUpDiv');
					},
					"Yes": function() {
						jQuery('#packageAlreadyPickedUp').click();
					}
				}
			});
		});

		function savePackageId(id) {
			jQuery('#packageId').val(id);
		}

		function openDialog(windowname) {
			jQuery('#dialog-modal').dialog("open");
		}

		function closeDialog(windowname) {
			jQuery('#dialog-modal').dialog("close");
		}
	</script>
	
</s:layout-component>
</s:layout-render>