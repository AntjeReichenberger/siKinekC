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
						<h1>Referral Conversion Report</h1>
						<!-- /STEP TITLE -->
					
						<!-- Referral Conversion  Report -->
						<s:form beanclass="org.webdev.kpoint.action.ReferralConversionReportActionBean" focus="">
						
							<stripes:errors/>
							<stripes:messages/>
							
							<script type="text/javascript">
							// <![CDATA[
								jQuery(document).ready(function(){
									
									//Enable date pickers
									jQuery('.date-pick').datePicker({
										startDate: '01/01/2009',
										endDate: (new Date()).asString()
									});
									
									//Set start date range so it cant be after end date
									jQuery('#date_referralmin').bind(
										'dpClosed',
										function(e, selectedDates) {
											var d = selectedDates[0];
											if (d) {
												d = new Date(d);
												jQuery('#date_referralmax').dpSetStartDate(d.addDays(1).asString());
											}
										}
									);
									
									//Set end date range so it cant be before start date
									jQuery('#date_referralmax').bind(
										'dpClosed',
										function(e, selectedDates) {
											var d = selectedDates[0];
											if (d) {
												d = new Date(d);
												jQuery('#date_referralmin').dpSetEndDate(d.addDays(-1).asString());
											}
										}
									);
								
								});
							
							// ]]>
							</script>
				
							<fieldset>
								
								<!-- BLOCK -->
								<ol class="clearfix">
									<li style="width: 130px;">
										<s:label for="filterOption">Filter On</s:label><br />
										<s:select name="filterOption" id="filterOption" style="width:150px;">
											<s:option value="1">Referral Date</s:option>
											<s:option value="2">Conversion Date</s:option>
										</s:select>							
									</li>
									<li style="width: 380px">
										<label for="minReferralDate">Date Range </label><span class="required">*</span><br/>
										<s:text name="minDate" id="date_referralmin" class="date-pick" />
										<s:text name="maxDate" id="date_referralmax" class="date-pick" />							
									</li>
									<li style="width: 500px; padding-right: 0px;">
										<s:link beanclass="org.webdev.kpoint.action.ReferralConversionReportActionBean" style="vertical-align:middle;">Reset</s:link>
										<span class="separator">|</span>
										<s:submit name="fetchReferralConversionReport" class="button" value="View Report" style="margin: 10px 0 0 0;" />
										<s:submit name="export" class="button" value="Export" style="margin: 10px 0px 0px 10px;" />
									</li>			
								</ol>
								<!-- /BLOCK -->
								
							</fieldset>
	
							<hr style="clear:both;" />
							
							<s:label for="ReferralConversions" class="tabletotal">Results found: ${actionBean.listLength}</s:label>
	
							<!-- FORM CONTENT -->
							<div class="formContent">
								<table id="ReferralConversions" class="sortable">
									<thead>
										<tr>
											<th>Prospect's Name</th>
											<th>Prospect's Email</th>
											<th>Referrer</th>
											<th>Referral Date</th>
											<th>Conversion Date</th>
											<th>Referral Credit Date</th>
										</tr>
									</thead>
									<tbody>									
										<c:choose>
											<c:when test="${fn:length(actionBean.prospectsForExport) > 0}">
												<c:forEach items="${actionBean.prospectsForExport}" var="prospect" varStatus="loop">
													<tr>
														<td>${prospect.name}</td>
														<td>${prospect.email}</td>
														<td>${prospect.referrer.firstName} ${prospect.referrer.lastName}</td>
														<td><fmt:formatDate value="${prospect.referralDate.time}" type="date" pattern="MMMMM dd, yyyy"/></td>
														<td><fmt:formatDate value="${prospect.conversionDate.time}" type="date" pattern="MMMMM dd, yyyy"/></td>
														<c:choose>
															<c:when test="${prospect.credit.id > 0}">
																<td align="left"><fmt:formatDate value="${prospect.credit.issueDate.time}" type="date" pattern="MMMMM dd, yyyy"/></td>
															</c:when>
															<c:otherwise>
																<td align="left"></td>
															</c:otherwise>
														</c:choose>
													</tr>
													<c:set var="i" scope="page" value="${i+1}"/>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr><td colspan="7" class="emptymessage">We could not find any referral records to match the criteria you provided.</td></tr>
											</c:otherwise>
										</c:choose>									
									</tbody>
								</table>								
							</div>
							<!-- /FORM CONTENT -->
						
						</s:form>
						<!-- /Referral Conversion Report -->
					
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
						   jQuery("#ReferralConversions").tablesorter({
								//debug:true,
								headers:{0:{sorter:'text'},
										 1:{sorter:'text'},
								         2:{sorter:'text'},										 								 
										 3:{sorter:'MMMMDDYYYY'},
										 4:{sorter:'MMMMDDYYYY'},
										 5:{sorter: 'MMMMDDYYYY'}
										 
								}		 
							});  
						});			
		</script>
		
		<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
	</s:layout-component>
</s:layout-render>