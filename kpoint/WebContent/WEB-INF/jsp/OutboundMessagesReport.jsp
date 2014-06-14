<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">
		
		<!-- CONTENTWRAPPER -->
		<div id="contentWrapper" class="clearfix">
		
			<!-- CONTENT -->
			<div id="content" class="wide clearfix">
		
				<!-- INTERFACEWRAPPER -->
				<div id="interfaceWrapper">
		
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
					
						<!-- STEP TITLE -->
						<h1>Outbound Messages Report</h1>
						<!-- /STEP TITLE -->
					
						<!-- KinekPoint Change Report -->
						<s:form beanclass="org.webdev.kpoint.action.OutboundMessagesReportActionBean">
						
							<table class="superplain" style="width:750px;">
								<tr>
									<td>
										<s:label for="messageTriggerId">Message Type Filter</s:label><br />
										<s:select name="messageTriggerId" id="messageTriggerId" style="width:200px;">
											<s:option value="0">Show All Message Types</s:option>
											<s:options-collection collection="${actionBean.messageTriggers}" label="name" value="id" />
										</s:select>
									</td>
									<td>
										<s:label for="messageMediaId">Medium Filter</s:label><br />
										<s:select name="messageMediaId" id="messageMediaId" style="width:200px;">
											<s:option value="0">Show All Media</s:option>
											<s:options-collection collection="${actionBean.messageMedias}" label="name" value="id" />
										</s:select>
									</td>
									<td style="vertical-align:bottom;">
										<s:link beanclass="org.webdev.kpoint.action.OutboundMessagesReportActionBean" style="vertical-align:middle;">Reset</s:link>
										<span class="separator">|</span>
										<s:submit name="search" class="button" value="View Report" style="margin: 10px 0px 10px 10px;" />
									</td>
								</tr>
							</table>

							<hr style="margin-bottom: 20px;"/>
							
							<s:label for="ReferralConversions" class="tabletotal">Results found: ${actionBean.listLength}</s:label>
														
							<!-- FORM CONTENT -->
							<div class="formContent">
								<table id="ReferralConversions" class="sortable">
									<thead>
										<tr>
											<th>Message Type</th>
											<th>Message Medium</th>
											<th>Recipient</th>
											<th>Sent Date</th>
										</tr>
									</thead>
									<tbody>
									<c:choose>
										<c:when test="${fn:length(actionBean.messagesForReport) > 0}">
											<c:forEach items="${actionBean.messagesForReport}" var="message" varStatus="loop">
												<tr>
												<td>${message.trigger.name}</td>
												<td>${message.medium.name}</td>
												<td>
												<c:choose>
													<c:when test="${message.recipientEmail !=  null}">
														${message.recipientEmail}
													</c:when>
													<c:when test="${message.recipientCell !=  null}">
														${message.recipientCell}
													</c:when>
												</c:choose>
												</td>
												<td><fmt:formatDate value="${message.sentDate.time}" type="date" pattern="MMMMM dd, yyyy (hh:mm aa)"/></td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr><td colspan="7" class="emptymessage">We could not find any Outbound System Messages to match the criteria you provided.</td></tr>
										</c:otherwise>
									</c:choose>
									</tbody>
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
			jQuery(document).ready(function() { 
				//jQuery("#ReferralConversions").tablesorter(); 

				jQuery("#ReferralConversions").bind("sortEnd",function() { 
			        resetRowClasses(document.getElementById("ReferralConversions"));
			    }); 

				jQuery("#ReferralConversions").bind("sortStart",function() { 
			        clearRowClasses(document.getElementById("ReferralConversions"));
			    }); 
			});

			function resetRowClasses(table) {
				var rows = table.rows;
				for(var i = 1; i < rows.length; i++) {
					if(i % 2 == 0) {
						rows[i].className = "";
					}
					else {
						rows[i].className = "even";
					}
				}
			}

			function clearRowClasses(table) {
				var rows = table.rows;
				for(var i = 1; i < rows.length; i++) {
					rows[i].className = "even";
				}
			}

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
		                            var date = trim(s).match(/^(\w{1,10})[ ](\d{1,2}),[ ](\d{4})\s+\(\d{1,2}?:\d{1,2}\s+(AM|PM)\)$/);                                
									//console.log("format date: "+date+" | "+date[1]+" "+date[2]+" "+date[3]+" "+date[4]);
		                            //November 02, 2009
		                             if(date!=null){                                       
									  // console.log("format date: "+date);
									  
									 // console.log(trim(s).match(/\(\d{1,2}?:\d{1,2}\s+(AM|PM)\)/));
				
									  var timeAMPM=trim(s).match(/\(\d{1,2}?:\d{1,2}\s+(AM|PM)\)/);
									  var time=timeAMPM[0];			
									  var ampm=timeAMPM[1];
				
									  var hour=time.substring(1,3);
									  var min=time.substring(4,6);
				
									  if(ampm=='PM'){
									 		hour=parseInt(hour)+12;
									 }	
									  
		                               var m = months[date[1]];
		                               var d = String(date[2]);
		                               if (d.length == 1) {d = "0" + d;}
		                               var y = date[3];
									   
		                
		 							   var time=parseFloat(new Date(y,m,d,hour,min).getTime());
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
						   jQuery("#ReferralConversions").tablesorter({
								//debug:true,
								headers:{0:{sorter:'text'},								 
								         1:{sorter:'text'},
										 2:{sorter:'text'},										 
										 3:{sorter: 'MMMMDDYYYY'}
								}		 
							});  
						});						
		</script>
		
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
	</s:layout-component>
</s:layout-render>