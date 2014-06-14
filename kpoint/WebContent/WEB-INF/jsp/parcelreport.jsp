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
				
					<!-- STEP TITLE --><h1>Parcel Report</h1><!-- /STEP TITLE -->
				
					<stripes:errors/>
					<stripes:messages/>
				
					<script type="text/javascript">
					// <![CDATA[
						jQuery(document).ready(function(){
							//Enable date pickers
							jQuery('.date-pick').datePicker({
								startDate: '01/01/2009'
							});
						});
				
					// ]]>
					</script>	
				
					<!-- ACCEPTDELIVERY STEP1 -->
					<s:form beanclass="org.webdev.kpoint.action.ParcelReportActionBean" name="parcelReport" id="parcelReport">
						<fieldset>
				
							<!-- BLOCK -->
							<ol class="clearfix">
				
								<c:choose>
									<c:when test="${actionBean.activeUser.adminAccessCheck}">
									<li style="width: 100px;">
										<s:label for="depotId">Depot</s:label><br />
										<s:select name="depotId" id="depotId" style="width:100px;">
											<option value="">Please select a KinekPoint</option>
											<!-- <option value="-1">ALL DEPOTS</option>  -->
									    	<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
									  	</s:select>
									</li>
									</c:when>
									<c:when test="${actionBean.activeUser.reportAccessCheck}">
										<li style="width: 100px;">
											<s:label for="depotId">Depot</s:label><br />
											<s:select name="depotId" id="depotId" style="width:100px;">
												<option value="">Please select a KinekPoint</option>
										    	<s:options-collection collection="${actionBean.depots}" label="nameAddress1City" value="depotId" />
										  	</s:select>
										</li>
									</c:when>
								</c:choose>
								<li style="width: 100px;">
									<s:label for="reportId">Status</s:label><br />
									<s:select name="reportId" id="reportId" style="width:100px;">
										<s:options-collection collection="${actionBean.reports}" label="name" value="reportId" />
									</s:select>
								</li>
								<li style="width: 390px;">
									<label for="date_start">Date Range </label><br />
									<s:text name="startDate" id="date_start" class="date-pick" /><s:text name="endDate" id="date_end" class="date-pick" />
								</li>

								<li style="width: 300px;">
									<s:link beanclass="org.webdev.kpoint.action.ParcelReportActionBean" style="vertical-align:middle;">Reset</s:link>
									<span class="separator">|</span>
									<s:submit name="search" class="button" value="View Report" style="margin: 10px 0px 10px 10px;" />
								</li>
							</ol>
							
							<div style="width:20% !important; margin-left:27px !important">							
								<c:choose>
									<c:when test="${actionBean.activeUser.depotAdminAccessCheck}">
										<label for="depotId" style="color:#666">Depot</label>
										<s:select name="depotId" id="depotId">
											<option value="">Please select a KinekPoint</option>
											<s:option value="-1">ALL DEPOTS</s:option>
											<s:options-collection collection="${actionBean.userDepots}" label="nameAddress1City" value="depotId" />
										</s:select>	
									</c:when>
								</c:choose>
							</div>								
																
							<hr/>
							
							<s:label for="parceltable" class="tabletotal" style="margin-left: 0px;">Results found: ${actionBean.listLength}</s:label>
				
							<!-- /BLOCK -->
							<table id="parceltable" class="sortable">
								<thead>
									<tr>
										<c:if test="${actionBean.depotId == -1}">
											<th>Depot Name</th>
										</c:if>
										<th>Customer Name</th>
										<th>Kinek #</th>
										<th>Courier</th>
										<th>Custom Info</th>
										<c:choose>
											<c:when test="${actionBean.reportId != 3}">
												<th>Price</th>
											</c:when>
										</c:choose>
										<c:choose>
											<c:when test="${actionBean.reportId != 4}">
												<th>Delivery Date</th>
											</c:when>
										</c:choose>
										<c:choose>
											<c:when test="${actionBean.reportId == 0 || actionBean.reportId == 3}">
												<th>Redirect Description</th>
											</c:when>
										</c:choose>
										<c:choose>
											<c:when test="${actionBean.reportId == 0 || actionBean.reportId == 2 || actionBean.reportId == 4}">
												<th>Pick-Up Date</th>
											</c:when>
										</c:choose>
										<c:choose>
											<c:when test="${actionBean.reportId == 4}">
												<th>Receiving Fee</th>
												<th>Kinek Fee</th>
											</c:when>
										</c:choose>
									</tr>
								</thead>
								<tbody>
								<c:choose>
									<c:when test="${fn:length(actionBean.filteredPackageReceipts) > 0}">		
										<c:forEach items="${actionBean.filteredPackageReceipts}" var="pr" varStatus="loop">
											<c:forEach items="${pr.packages}" var="p" varStatus="loop">
												<tr>
													<c:if test="${actionBean.depotId == -1}">
														<td>${pr.kinekPoint.name} (${pr.kinekPoint.address1},${pr.kinekPoint.city})</td>
													</c:if>
													<td>
														<c:forEach items="${pr.packageRecipients}" var="recipient" varStatus="loop">
															<c:choose>
															<c:when test="${loop.index == 0}" >
																${recipient.fullName}
															</c:when>
															<c:otherwise>
																, ${recipient.fullName}
															</c:otherwise> 
															</c:choose>
														</c:forEach>
													</td>
													<td>
														<c:forEach items="${pr.packageRecipients}" var="recipient" varStatus="loop">
															<c:choose>
															<c:when test="${loop.index == 0}" >
																${recipient.kinekNumber}
															</c:when>
															<c:otherwise>
																, ${recipient.kinekNumber}
															</c:otherwise> 
															</c:choose>
														</c:forEach>
													</td>
													<td>${p.courier.name}</td>
													<td>${p.customInfo}</td>
													<c:choose>
														<c:when test="${actionBean.reportId != 3}">
															<td>
																<c:choose>
																	<c:when test="${pr.redirectReason.name != null}">
																		N/A
																	</c:when>
																	<c:otherwise>
																		<c:choose>
																			<c:when test="${p.pickupFee + p.dutyAndTax + p.extendedDurationFee + p.skidFee > 0}">
																				<fmt:formatNumber value="${p.pickupFee + p.dutyAndTax + p.extendedDurationFee + p.skidFee}" type="currency"></fmt:formatNumber>
																			</c:when>
																			<c:otherwise>
																				Free
																			</c:otherwise>
																		</c:choose>
																	</c:otherwise>
																</c:choose>
															</td>
														</c:when>
													</c:choose>	
													<c:choose>
														<c:when test="${actionBean.reportId != 4}">
															<td><fmt:formatDate value="${pr.receivedDate}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>
														</c:when>
													</c:choose>
													<c:choose>
														<c:when test="${actionBean.reportId == 0 || actionBean.reportId == 3}">
															<td>${pr.redirectReason.name}</td>
														</c:when>
													</c:choose>
													<c:choose>
														<c:when test="${actionBean.reportId == 0 || actionBean.reportId == 2 || actionBean.reportId == 4}">
															<td><fmt:formatDate value="${p.pickupDate.time}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>
														</c:when>
													</c:choose>
													<c:choose>
														<c:when test="${actionBean.reportId == 4}">
															<td><fmt:formatNumber value="${p.receivingFee}" type="currency"></fmt:formatNumber></td>
															<td><fmt:formatNumber value="${p.kinekFee}" type="currency"></fmt:formatNumber></td>
														</c:when>
													</c:choose>
												</tr>
											</c:forEach>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<c:choose>
												<c:when test="${actionBean.reportId == 0 && actionBean.depotId != -1}">
													<td colspan="8" class="emptymessage">
														We are unable to find any parcels that match the criteria you have provided.
													</td>
												</c:when>
												<c:when test="${actionBean.reportId == 0 && actionBean.depotId == -1}">
													<td colspan="9" class="emptymessage">
														We are unable to find any parcels that match the criteria you have provided.
													</td>
												</c:when>
												<c:when test="${actionBean.reportId == 1}">
													<td colspan="6" class="emptymessage">
														We are unable to find any parcels that match the criteria you have provided.
													</td>
												</c:when>
												<c:when test="${actionBean.reportId == 2}">
													<td colspan="7" class="emptymessage">
														We are unable to find any parcels that match the criteria you have provided.
													</td>
												</c:when>
												<c:when test="${actionBean.reportId == 3}">
													<td colspan="6" class="emptymessage">
														We are unable to find any parcels that match the criteria you have provided.
													</td>
												</c:when>
												<c:when test="${actionBean.reportId == 4}">
													<td colspan="8" class="emptymessage">
														We are unable to find any parcels that match the criteria you have provided.
													</td>
												</c:when>
											</c:choose>												
										</tr>
									</c:otherwise>
								</c:choose>
								</tbody>
							
								<c:choose>
									<c:when test="${actionBean.reportId == 4 && fn:length(actionBean.packages) > 0}">
										<tfoot>
											<tr>
												<c:choose>
													<c:when test="${actionBean.reportId == 0}">
														<th colspan="4">Total:</th>
													</c:when>
													<c:when test="${actionBean.reportId == 1}">
														<th colspan="2">Total:</th>
													</c:when>
													<c:when test="${actionBean.reportId == 2 || actionBean.reportId == 3}">
														<th colspan="3">Total:</th>
													</c:when>
													<c:when test="${actionBean.reportId == 4}">
														<th colspan="4">Total:</th>
													</c:when>
												</c:choose>
												
												<td><fmt:formatNumber value="${actionBean.totalReceivingFees}" type="currency"></fmt:formatNumber></td>
												<td><fmt:formatNumber value="${actionBean.totalKinekFees}" type="currency"></fmt:formatNumber></td>
											</tr>
										</tfoot>
									</c:when>
								</c:choose> 
							</table>
				
							<!-- /FORM CONTENT -->									
				
						</fieldset>
					</s:form>   
					<!-- /ACCEPTDELIVERY STEP1 -->
				
				</div>
				<!-- /STEPCONTAINER -->
				
			</div>
			<!-- /INTERFACEWRAPPER -->
						
		</div>
		<!-- /CONTENT -->
		
	</div>
	<!-- /CONTENTWRAPPER -->
	
	<script type="text/javascript" src="resource/js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript">
		//jQuery(document).ready(function() { jQuery("#parceltable").tablesorter(); });
		
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
				   jQuery("#parceltable").tablesorter({
						//debug:true,
						headers:{0:{sorter:'text'},								 
						         1:{sorter:'text'},
								 2:{sorter:'text'},
								 3:{sorter:'MMMMDDYYYY'},			 								 
								 4:{sorter: 'text'},
								 5:{sorter: 'MMMMDDYYYY'}
						}		 
					});  
				});			
		
	</script>

	<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
  </s:layout-component>
</s:layout-render>