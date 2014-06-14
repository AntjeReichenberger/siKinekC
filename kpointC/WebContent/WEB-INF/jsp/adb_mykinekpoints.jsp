<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
		
		<link rel="stylesheet" href="resource/css/pages/mykinekpoints.css" type="text/css" media="screen,projection" />		
			
		<div class="clearfix"></div>
		<br class="clear">
		<div id="adbModule" class="searchresults">	
			
			<!-- MY KINEKPOINTS-->
			<div id="pageTitle">
				<h1>My KinekPoints</h1>
			</div>
			
				<s:errors />
				<s:messages />	
		
				<div id="myKinekPoints_wrapper">
				<ul id="myKinekPointsList">		
					<c:forEach items="${actionBean.kinekPoints}" var="myKPs" varStatus="loop">
						<li class="ui-corner-all ui-widget ui-state-default">
							<div>
							<c:choose>
								<c:when test="${myKPs.depotId != actionBean.user.depot.depotId}">
									<span id="click_${myKPs.depotId}" style="float:right" class="removeKP ui-icon ui-icon-circle-close"></span>
									<s:link style="float: right; border-bottom-width: 0px;" event="setDefaultKinekPoint" beanclass="org.webdev.kpoint.action.MyKinekPointsActionBean">
										<s:param name="depotId" value="${myKPs.depotId}"></s:param>
										<span style="float:right" class="kp regularKP"></span>
									</s:link>									
								</c:when>
								<c:otherwise>
									<span style="float:right" class="favoriteKP"></span>
								</c:otherwise>
							</c:choose>
								<br />
								${actionBean.consumerFullName}<br />							
								${myKPs.address1}, #${actionBean.kinekNumber}<br />
								${myKPs.name}<br />
								${myKPs.city}, ${myKPs.state.stateProvCode}<br />
								${myKPs.zip}
							</div>
							<div id="hoursPricing">
									<s:link beanclass="org.webdev.kpoint.action.KinekPointDetailsActionBean">View Hours &amp; Pricing
										<s:param name="depotId" value="${myKPs.depotId}"></s:param>									
									</s:link>
								</div>
						</li>
					</c:forEach>	
					<li class="ui-corner-all ui-widget ui-state-default">
					<s:form name="addNewKPFrm" action="/MyKinekPoints.action">
						<s:submit name="addNewKinekPoint" class="add" value="">Add New KinekPoint</s:submit>
					</s:form>						
					</li>
					
				</ul>	
				</div>
				
				<div id="dialog" title="Confirmation Required">Are you sure you wish to delete this KinekPoint?</div>
					
		</div>
		<script type="text/javascript">
		
		var currentEvent;
		
		jQuery(document).ready(
				function(){
					jQuery(".removeKP").click(function (event) {
							var split = jQuery(this).attr('id').split('_');
							var depotId = split[1];
							event.preventDefault();		
							currentEvent = jQuery(this);
							
							jQuery("#dialog").dialog({
							      buttons : {
							        "Confirm" : function() {
							        	invokeRemoveFavorite(jQuery('form')[0], 'removeFavoriteKinekPoint',depotId);
							        	jQuery(this).dialog("close");
							        },
							        "Cancel" : function() {
							        	jQuery(this).dialog("close");
							        }
							      }
							    });
							jQuery("#dialog").dialog("open");
					});	
					
					jQuery(".ui-icon").hover(
							function(){ 
								jQuery(this).addClass("ui-state-hover"); 
							},
							function(){ 
								jQuery(this).removeClass("ui-state-hover"); 
							}
					);
					jQuery(".kp").hover(
							function(){ 
								jQuery(this).addClass("favoriteKP");
								jQuery(this).removeClass("regularKP");
							},
							function(){ 
								jQuery(this).addClass("regularKP");
								jQuery(this).removeClass("favoriteKP"); 
							}
					);
					
					jQuery("#dialog").dialog({
					      autoOpen: false,
					      draggable: false,
					      resizable: false,
					      modal: true,
				           width: 300,
				           height: 125
					    });
				}
			);		
		
		function invokeRemoveFavorite(form, event, depotId) {
            params = {};
            if (event != null) params = event + '&' + 'depotId=' + depotId;            
            jQuery.post(form.action,
                    params,
                    function (xml) {
            			if(xml == 'true'){
            				currentEvent.parent().parent().hide("slow");
            			}
            			else{
            				return false;
            			}
                    });
        }
		
		
		
		</script>
		
	</s:layout-component>
</s:layout-render>