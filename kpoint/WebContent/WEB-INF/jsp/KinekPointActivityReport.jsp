<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">
	<script type="text/javascript">
	// <![CDATA[
		jQuery(document).ready(function(){
			
			//Enable date pickers
			jQuery('.date-pick').datePicker({
				startDate: '01/01/2009'
			});

			//Country toggle
					jQuery('select#stateId').html("");
					//List all provinces
					jQuery('select#stateId').append("<option value='0' selected='selected'>Select a State / Province</option>");
			
				//Choose USA
					jQuery('select#stateId').append("<option value='1'>Alabama</option>");
					jQuery('select#stateId').append("<option value='2'>Alaska</option>");
					jQuery('select#stateId').append("<option value='3'>Arizona</option>");
					jQuery('select#stateId').append("<option value='4'>Arkansas</option>");
					jQuery('select#stateId').append("<option value='5'>California</option>");
					jQuery('select#stateId').append("<option value='6'>Colorado</option>");
					jQuery('select#stateId').append("<option value='7'>Connecticut</option>");
					jQuery('select#stateId').append("<option value='8'>Delaware</option>");
					jQuery('select#stateId').append("<option value='9'>District of Columbia</option>");
					jQuery('select#stateId').append("<option value='10'>Florida</option>");
					jQuery('select#stateId').append("<option value='11'>Georgia</option>");
					jQuery('select#stateId').append("<option value='12'>Hawaii</option>");
					jQuery('select#stateId').append("<option value='13'>Idaho</option>");
					jQuery('select#stateId').append("<option value='14'>Illinois</option>");
					jQuery('select#stateId').append("<option value='15'>Indiana</option>");
					jQuery('select#stateId').append("<option value='16'>Iowa</option>");
					jQuery('select#stateId').append("<option value='17'>Kansas</option>");
					jQuery('select#stateId').append("<option value='18'>Kentucky</option>");
					jQuery('select#stateId').append("<option value='19'>Louisiana</option>");
					jQuery('select#stateId').append("<option value='20'>Maine</option>");
					jQuery('select#stateId').append("<option value='21'>Maryland</option>");
					jQuery('select#stateId').append("<option value='22'>Massachusetts</option>");
					jQuery('select#stateId').append("<option value='23'>Michigan</option>");
					jQuery('select#stateId').append("<option value='24'>Minnesota</option>");
					jQuery('select#stateId').append("<option value='25'>Mississippi</option>");
					jQuery('select#stateId').append("<option value='26'>Missouri</option>");
					jQuery('select#stateId').append("<option value='27'>Montana</option>");
					jQuery('select#stateId').append("<option value='28'>Nebraska</option>");
					jQuery('select#stateId').append("<option value='29'>Nevada</option>");
					jQuery('select#stateId').append("<option value='30'>New Hampshire</option>");
					jQuery('select#stateId').append("<option value='31'>New Jersey</option>");
					jQuery('select#stateId').append("<option value='32'>New Mexico</option>");
					jQuery('select#stateId').append("<option value='33'>New York</option>");
					jQuery('select#stateId').append("<option value='34'>North Carolina</option>");
					jQuery('select#stateId').append("<option value='35'>North Dakota</option>");
					jQuery('select#stateId').append("<option value='36'>Ohio</option>");
					jQuery('select#stateId').append("<option value='37'>Oklahoma</option>");
					jQuery('select#stateId').append("<option value='38'>Oregon</option>");
					jQuery('select#stateId').append("<option value='39'>Pennsylvania</option>");
					jQuery('select#stateId').append("<option value='40'>Rhode Island</option>");
					jQuery('select#stateId').append("<option value='41'>South Carolina</option>");
					jQuery('select#stateId').append("<option value='42'>South Dakota</option>");
					jQuery('select#stateId').append("<option value='43'>Tennessee</option>");
					jQuery('select#stateId').append("<option value='44'>Texas</option>");
					jQuery('select#stateId').append("<option value='45'>Utah</option>");
					jQuery('select#stateId').append("<option value='46'>Vermont</option>");
					jQuery('select#stateId').append("<option value='47'>Virginia</option>");
					jQuery('select#stateId').append("<option value='48'>Washington</option>");
					jQuery('select#stateId').append("<option value='49'>West Virginia</option>");
					jQuery('select#stateId').append("<option value='50'>Wisconsin</option>");
					jQuery('select#stateId').append("<option value='51'>Wyoming</option>");
					//Choose canada
					jQuery('select#stateId').append("<option value='57'>Alberta</option>");
					jQuery('select#stateId').append("<option value='56'>British Columbia</option>");
					jQuery('select#stateId').append("<option value='59'>Manitoba</option>");
					jQuery('select#stateId').append("<option value='61'>Newfoundland and Labrador</option>");
					jQuery('select#stateId').append("<option value='62'>New Brunswick</option>");
					jQuery('select#stateId').append("<option value='54'>Northwest Territories</option>");
					jQuery('select#stateId').append("<option value='63'>Nova Scotia</option>");
					jQuery('select#stateId').append("<option value='52'>Nunavut</option>");
					jQuery('select#stateId').append("<option value='55'>Ontario</option>");
					jQuery('select#stateId').append("<option value='64'>Prince Edward Island</option>");
					jQuery('select#stateId').append("<option value='53'>Quebec</option>");
					jQuery('select#stateId').append("<option value='58'>Saskatchewan</option>");
					jQuery('select#stateId').append("<option value='60'>Yukon</option>");

					jQuery('select#stateId').val("${actionBean.stateId}");
		});
	
	// ]]>
	</script>
	<!-- STEPS -->

		<!-- CONTENTWRAPPER -->
		<div id="contentWrapper" class="clearfix">
		
			<!-- CONTENT -->
			<div id="content" class="wide clearfix">
		
				<!-- INTERFACEWRAPPER -->
				<div id="interfaceWrapper">
		
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
					
						<!-- STEP TITLE -->
						<h1>KinekPoint Activity Report</h1>
						<!-- /STEP TITLE -->
						<stripes:errors/>
						<stripes:messages/>

						<!-- KinekPoint Change Report -->
						<s:form beanclass="org.webdev.kpoint.action.KinekPointActivityActionBean">
							<table class="superplain" style="width: 750px;">
								<tbody>
									<tr>
										<td>
											<s:label for="depot">KinekPoint:</s:label><br/>
											<s:select name="depotId" id="depotId" style="width: 300px;">
												<s:option value="" label="Select a KinekPoint" />
												<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
											</s:select>
											<small class="validation">* Which KinekPoint is the search constrained to?</small>
										</td>
										<td>
											<s:label for="constraint">Region Constraint:</s:label><br />
											<s:select name="stateId" id="stateId">
												<s:options-collection collection="${actionBean.states}" label="name" value="stateId" id="stateId" />
											</s:select>
											<small class="validation">* Which region is the search constrained to?</small>
										</td>
									</tr>
									<tr>
										<td>
											<s:label for="city">City:</s:label><br/>
											<s:text name="city" id="city" style="width: 300px;"/>
											<small class="validation">* Which city is the search constrained to?</small>
										</td>
										<td>
										  <label for="date_start">Created Date Range: </label><br />
										  <s:text name="startDate" id="date_start" class="date-pick" /><s:text name="endDate" id="date_end" class="date-pick" />
										  <small class="validation">* When was the depot created?</small>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<s:link beanclass="org.webdev.kpoint.action.KinekPointActivityActionBean" style="vertical-align:middle;">Reset</s:link>
											<span class="separator">|</span>
											<s:submit name="search" value="Search" class="button" />
										</td>
									</tr>
								</tbody>
							</table>
							
							<s:label for="KinekPointsForReport" class="tabletotal">Results found: ${actionBean.listLength}</s:label>
							
							<!-- FORM CONTENT -->
							<div class="formContent">
									<table id="KinekPointsForReport" class="sortable">
										<thead>
											<tr>
												<th>KinekPoint Name</th>
												<th>Date Registered</th>
												<th>City</th>
												<th>State / Province</th>
												<th>Parcels Received</th>
	  											<th>Parcels Picked-up</th>
											</tr>
										</thead>
										<tbody>
											<c:choose>
												<c:when test="${actionBean.goodResults}">
													<c:forEach items="${actionBean.kinekPointsForReport}" var="container" varStatus="loop">
														<tr>
															<td>${container.depot.name}</td>
															<td><fmt:formatDate value="${container.depot.createdDate.time}" type="date" pattern="MMMMM dd, yyyy"/></td> 
															<td>${container.depot.city}</td>
															<td>${container.depot.state.name}</td>
															<td class="numerical"><fmt:formatNumber value="${container.parcelsReceivedCount}" type="number"></fmt:formatNumber></td> 
															<td class="numerical"><fmt:formatNumber value="${container.parcelsPickedupCount}" type="number"></fmt:formatNumber></td>
														</tr>
													</c:forEach>
												</c:when>
												<c:when test="${actionBean.noResults}">
													<tr>
														<td colspan="6" class="emptymessage">
															We are unable to find any KinekPoints that match the criteria you have provided.
														</td>
													</tr>
												</c:when>
												<c:when test="${actionBean.tooManyResults}">
													<tr>
														<td colspan="6" class="emptymessage">
															The criteria you have provided matches too many KinekPoints (&gt; 100,000).<br/>
															Please refine your criteria information to narrow the search
														</td>
													</tr>
												</c:when>
											</c:choose>
										</tbody>
										<c:if test="${actionBean.goodResults}">
											<tfoot>
												<tr>
													<th colspan="4">Totals:</th>
													<td class="numerical"><fmt:formatNumber value="${actionBean.parcelsPendingCount}" type="number" /></td> 
													<td class="numerical"><fmt:formatNumber value="${actionBean.parcelsPickedupCount}" type="number" /></td>
												</tr>
											</tfoot>
										</c:if>
									</table>
								
							</div>
							<!-- /FORM CONTENT -->
						
						</s:form>
						<!-- /KinekPoint Change Report -->
					
					</div>
					<!-- /STEPCONTAINER -->
					
				</div>
				<!-- /INTERFACE -->
				
			</div>
			<!-- /CONTENT -->
			
		</div>
		<!-- /CONTENTWRAPPER -->
		
		
		<script type="text/javascript" src="resource/js/jquery.tablesorter.min.js"></script>
		<script type="text/javascript">
			//jQuery(document).ready(function() { jQuery("#KinekPointsForReport").tablesorter(); });
	function trim(stringToTrim) {
		return stringToTrim.replace(/^\s+|\s+$/g,"");
	}

			   var months = new Array();
               months["January"] = "00";
               months["February"] = "01";
               months["March"] = "02";
               months["April"] = "03";
               months["May"] = "04";
               months["June"] = "05";
               months["July"] = "06";
               months["August"] = "07";
               months["September"] = "08";
               months["October"] = "09";
               months["November"] = "10";
               months["December"] = "11";
			   
			   

			   var dateRegistered='';
               jQuery.tablesorter.addParser({
                       //set unique id
                       id:'MMMMDDYYYY',
                       is:function(s){
                               return false;
                       },
                       format:function(s){
                            //custom format
                            var date = trim(s).match(/^(\w{1,10})[ ](\d{1,2}),[ ](\d{4})$/);                                
							//console.log("format date: "+date);
                            //November 02, 2009
                             if(date!=null){                                       
							  // console.log("format date: "+date);
                               var m = months[date[1]];
                               var d = String(date[2]);
                               if (d.length == 1) {d = "0" + d;}
                               var y = date[3];
                
 							   var time=parseFloat(new Date(y,m,d).getTime());
                               //console.log('format time: '+time);
                            
                               return time;
                             }else{
                                 return 0;	
                             }
                       },
                       //set type as numeric
                       type:'numeric'
               });

				jQuery(function(){ 
				   jQuery("#KinekPointsForReport").tablesorter({
						//debug:true,
						headers:{0:{sorter:'text'},
								 1:{sorter:'MMMMDDYYYY'},
						         2:{sorter:'text'},
								 3:{sorter:'text'},			 								 
								 4:{sorter: 'integer'},
								 5:{sorter: 'integer'}
						}		 
					});  
				});						
		</script>
		
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
	</s:layout-component>
</s:layout-render>