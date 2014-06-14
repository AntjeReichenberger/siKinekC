<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
		<link rel="stylesheet" href="resource/css/pages/trackinglist.css"
			type="text/css" media="screen,projection" />
		<link rel="stylesheet"
			href="resource/css/pages/dashboard/dashboard-kpresults.css"
			type="text/css" media="screen,projection" />
		<script type="text/javascript" src="resource/js/KinekPointTrack.js"></script>
		<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAjqRM5kPRvQCQX5qwhwD4Y4u4GKGqHpUk&sensor=false"></script>
		<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerLabel.js"></script>
		<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerElement.js"></script>
		<script type="text/javascript" src="resource/js/mapmarkerpkg/MarkerEvent.js"></script>	
		
		<div class="clearfix"></div>
		<script type="text/javascript">
		 	jQuery(document).ready(function() {
		 		jQuery( '.trackingLink a' )
			    .click(function(event) {
			    	event.preventDefault();
			    	submitTracking(jQuery(event.currentTarget).attr("class").substring(2));
			        return false;
			    });
		 		initializeTrackingLabelEvent();
			 });
		 	
		 	function submitNickname(trackingName, trackingId, isAccepted) {
			    var params = "packageNickname="+trackingName+"&trackingId="+trackingId+"&isAccepted=+"+isAccepted+"+&_eventName=changeNickname";
			    jQuery.ajax({
			    	  type: 'POST',
			    	  url: "Tracking.action",
			    	  data: params,
			    	  success: function(data) {
					    	var returnName = data.split(',');
					    	var trackingId = returnName[0];
					    	var packageNicknameDiv =  jQuery('<div></div>')
								.attr('id','n-'+ trackingId)
								.addClass('nicknameContainer')
								.append(returnName[1]);					    						    	
					    	var trackLabel = jQuery('<span></span>')
								.addClass('ui-icon ui-icon-pencil trackingLabelEdit');
					    	jQuery('#l-'+trackingId).empty().append(packageNicknameDiv).append(trackLabel);
					    	initializeTrackingLabelEvent();
					    },
			    	  dataType:"html"
			    	});
			}
		 	
		 	function initializeTrackingLabelEvent(){	
		 		jQuery( '.trackingLabelEdit' )
			    .click(function(event) {
			    	event.preventDefault();
			    	trackingId = jQuery(event.currentTarget).parent().attr("id").substring(2);
			    	
					var saveNickname = jQuery('<input></input>')
						.attr('type','text')
						.attr('id','save-' + trackingId)
						.attr('size','11')
						.attr('value',jQuery('#n-' + trackingId).html())
						.addClass('saveNickname');
						
					var approve = jQuery('<span></span>')
						.attr('id','approve-' + trackingId)
						.addClass('ui-icon ui-icon-check approve');
					
					var reject = jQuery('<span></span>')
						.attr('id','reject-' + trackingId)
						.addClass('ui-icon ui-icon-close reject');

					var saveNicknameDiv = jQuery('<div></div>').append(saveNickname).append(approve).append(reject);
					
					jQuery('#l-' + trackingId).html(saveNicknameDiv);
					
					jQuery('.approve')
				    .click(function(event) {
				    	event.preventDefault();	
				    	var trackingId = jQuery(this).attr("id").substring(8);
				    	var trackingName = jQuery('#save-' + trackingId).attr("value");
				    	submitNickname(trackingName,trackingId,true);
				    });
					
					jQuery('.reject')
				    .click(function(event) {
				    	event.preventDefault();
				    	var trackingId = jQuery(this).attr("id").substring(7);
				    	var trackingName = jQuery('#save-' + trackingId).attr("value");
				    	submitNickname(trackingName,trackingId,false);
				    });
					
			    });
		 	}
			
			function submitTracking(trackingId) {
			    var params = "activeTrackingId="+trackingId+"&_eventName=submit";
			    jQuery.ajax({
			    	  type: 'POST',
			    	  url: "Tracking.action",
			    	  data: params,
			    	  success: function(data) {
					    	jQuery('#newTrackingDetails').html(data);
					    	//document.getElementById('newTrackingDetails').innerHTML = data;
					    	displayTrackingDetails();
					    },
			    	  dataType:"html"
			    	});
			    jQuery('#newTrackingDetails').html("<center><img src=\"resource/images/ajax.gif\"/></center>");
			    return false;
			}
	    </script>
	    
	    <style type="text/css">
		    .jScrollPaneContainer {
				height: 380px !important;
				width: 250px !important;
			}
	    </style>
	    <br class="clear">
		<div id="adbModule" class="searchresults"><!-- view Surrogate -->
		<s:errors /> <s:messages /> <!-- view Tracking List --> 
		<div id="pageTitle">
			<h1>My Tracked Packages</h1>
		</div>
		
		<div id="descriptionHeader">
			<p>Tracking your packages has never been easier... Simply select the add button below or forward any email with a tracking number to <a href="mailto:track@kinekusa.com">track@kinekusa.com</a> 
			and we will send you updates the moment your package makes a move!</p>
			<p>Oh yeah, the email must be sent from the same email address used to sign up
			with Kinek and be sure to turn on tracking notifications in your profile.</p>	
		</div>
		

		<s:form
			name="viewTrakingListFrm" action="/Tracking.action">
			<table class="">
				<thead>
					<tr>
						<th></th>
						<th>Package Nickname</th>
						<th>Tracking Number</th>
						<th>Courier</th>
						<th>Current Status</th>					
						<th>Current Location</th>
						<th>Delivery ETA</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${fn:length(actionBean.userTrackings)>0}">
							<c:forEach items="${actionBean.userTrackings}" var="userTracking"
								varStatus="loop">
								<tr>
									<td style="width: 20px"><s:checkbox name="selectedTrackingNumbers"
										value="${userTracking.tracking.id}"></s:checkbox></td>
									<td style="width: 120px" id="l-${userTracking.tracking.id}"><div class="nicknameContainer" id="n-${userTracking.tracking.id}">${userTracking.packageNickname}</div><span class="ui-icon ui-icon-pencil trackingLabelEdit"></span></td>	
									<td style="width: 100px">
									<div class=trackingLink ><a class="t-${userTracking.tracking.id}" } href="#">
										${userTracking.tracking.trackingNumber}</a></div></td>
									<td style="width: 50px">${userTracking.tracking.courier.name}</td>	
									<td style="width: 100px">${userTracking.tracking.currentStatus}</td>	
									<td style="width: 120px">${userTracking.tracking.currentLocation}</td>
									<td style="width: 80px"><fmt:formatDate
										value="${userTracking.tracking.estimatedArrival.time}"
										dateStyle="default" /></td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<td colspan="7" align="center">You currently do not have any packages being tracked.</td>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			<table class="superplain">
				<tr>
					<td align="right"><s:submit name="deletePackage" value="Delete Package" class="button"></s:submit> 
						<s:submit name="addPackage" value="Add Package" class="button"></s:submit></td>
				</tr>
			</table>
			
		</s:form>

		<div id="newTrackingDetails"></div>
		</div>
		
	</s:layout-component>
</s:layout-render>