<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/pickup.jsp">
	<s:layout-component name="contents">
		<ul id="steps">
			<li><a href="#step1" title="ID Verification"><span>Step 1:</span> ID Verification</a></li>
			<li class="active"><a href="#step2" title="Package Summary"><span>Step 2:</span> Package Summary</a></li>
			<li><a href="#step3" title="Pickup Confirmation"><span>Step 3:</span> Pickup Confirmation</a></li>
		</ul>
		
		<div id="stepContainer">
			
			<h1><span>2</span> Pick-Up - Package Summary</h1>
			
			<stripes:errors/>
			
			<script type="text/javascript">
				jQuery(document).ready(function(){
					//document.getElementsByName("checkall")[0].checked = true;
					
					//selectAll();
					checkSelected();
				});
	
				function selectAll() {
					
					var box = document.getElementsByName("checkall")[0];
					var rows = document.getElementById("packagesTable").rows;
	
					for(var i = 1; i < rows.length; i++) {
						if (rows[i].cells[0].childNodes[0].tagName == "INPUT") {
							rows[i].cells[0].childNodes[0].checked = box.checked;
						}
					}
					checkSelected();
				}
	
				function checkSelected() {
					var selected = false;
					var table = document.getElementById("packagesTable");
					if (!table) {
						return;
					}
					
					var rows = table.rows;
					var numberSelected = 0;
					var checkStr = "";
					
					for(var i = 1; i < rows.length; i++) {
						if (rows[i].cells[0].childNodes[0].tagName == "INPUT" && (rows[i].cells[0].childNodes[0].checked==true)) {
							selected = true;
							checkStr += rows[i].cells[0].childNodes[0].value + ",";
							numberSelected++;
							//alert("select: "+i+" : "+rows[i].cells[0].childNodes[0].checked);
						}else {
							//uncheck if any chk box is unselected	
							//alert("unselect "+i+" : "+rows[i].cells[0].childNodes[0].checked);
							if(rows[i].cells[0].childNodes[0].checked==false){
								document.getElementsByName("checkall")[0].checked = false;
							}
						}
					}

					if (numberSelected == rows.length - 4) {
						document.getElementsByName("checkall")[0].checked = true;
					}
	
					if(selected) {
						var buttons = document.getElementsByName("completePickup");
						buttons[0].disabled = false;
						buttons[0].className = "button";
						
						checkStr = checkStr.substring(0, checkStr.length-1);
					}
					else {
						var buttons = document.getElementsByName("completePickup");
						buttons[0].disabled = true;
						buttons[0].className = "buttonDisabled";
					}
					
					updateTotals();
				}

						
				function updateTotals()
				{
					var table = document.getElementById("packagesTable");
					if (!table) {
						return;
					}
					var rows = table.rows;
					var total = 0;

					for(var i = 1; i < rows.length; i++) {
						if (rows[i].cells[0].childNodes[0].tagName == "INPUT" && rows[i].cells[0].childNodes[0].checked) {
							//If the KP doesn't accept tax and duty there will be 8 columns, otherwise there will be 9
							if (rows[i].cells[9] == undefined && rows[i].cells[8] != undefined) {
								var cur = rows[i].cells[8].childNodes[0].data;
							}
							else if(rows[i].cells[8] == undefined) {
								var cur = rows[i].cells[7].childNodes[0].data;
							}
							else{
								var cur = rows[i].cells[9].childNodes[0].data;
							}
							cur = parseFloat(cur.substring(1,cur.length));
							total += cur;
						}
					}

					var totalCredits = document.getElementById("totalCredit").innerHTML;
					totalCredits = parseFloat(totalCredits.substring(1,totalCredits.length));
					var totalFees = total - totalCredits;
					if(totalFees < 0)
					{
						totalFees = 0;
					}
					
					document.getElementById("totalReceivingFee").innerHTML = "$"+roundNumber(total,2);
					document.getElementById("totalFee").innerHTML = "$"+roundNumber(totalFees,2);
				}

				/*
				 * Rounds a number to the given number of decimal places
				 */
				function roundNumber(num, dec) {
					var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
					return result.toFixed(2);
				}
				
				var preventDefault = false;
				function completePickup_onclick(e) {
					if (preventDefault) {
						if (e && e.preventDefault)
							e.preventDefault();
						else if (window.event && window.event.returnValue)
							window.eventReturnValue = false;
						return false;
						}
					else {
						preventDefault = true;
					}

					return true;
				}
			</script>
			
			<s:form beanclass="org.webdev.kpoint.action.PickupActionBean" >
				<s:hidden name="consumerCreditIds" />
				<s:hidden name="depotId" />
				<s:hidden name="selectedConsumer" />
				<fieldset>
					<!-- FORM CONTENT -->
					<div class="formContent">
						<table id="packagesTable">
							<thead>
								<tr>
									<th style="width:23px;"><s:checkbox name="checkall" onclick="selectAll()" style="width: auto;"></s:checkbox></th>
									<th>Proof of Purchase</th>
									<th>Shipped From</th>
									<th>Courier</th>
									<th>Delivery Date</th>
									<th>Custom Info</th>
									<th>Weight Class</th>
									<th>Receiving Fee</th>
									<c:if test="${actionBean.hasExtendedFees}">
										<th>Storage Fee</th>
									</c:if>
									<c:if test="${actionBean.kinekPoint.acceptsDutyAndTax}">
										<th>Tax &amp; Duty</th>
									</c:if>
									<th>Package Total</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${actionBean.receiptsAvailableForPickup}" var="receipt" varStatus="loop">
									<c:if test="${receipt.redirectLocation == null}">
										<c:forEach items="${receipt.packages}" var="pack" varStatus="loop">
											<c:if test="${pack.pickupDate == null}">
												<tr>
													<td><s:checkbox name="selectedPackagesIds" value="${pack.packageId}" onclick="checkSelected()" style="width: auto;" /></td>
													<td>
														<c:choose>
															<c:when test="${receipt.requiresProofOfPurchase}">
																<label>Required</label>
															</c:when>
															<c:otherwise>
																<label>Not Required</label>
															</c:otherwise>
														</c:choose>
													</td>
													<td>${pack.shippedFrom}</td>
													<td>${pack.courier.name}</td>
													<td><fmt:formatDate value="${receipt.receivedDate}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>
													<td>${pack.customInfo}</td>
													<td>${pack.packageWeightGroup.friendlyLabel}</td>
													<td><fmt:formatNumber value="${pack.pickupFee + pack.skidFee}" type="currency"></fmt:formatNumber></td>
													<c:if test="${actionBean.hasExtendedFees}">
														<td><fmt:formatNumber value="${pack.extendedDurationFee}" type="currency"></fmt:formatNumber></td>
													</c:if>
													<c:if test="${actionBean.kinekPoint.acceptsDutyAndTax}">
														<td><fmt:formatNumber value="${pack.dutyAndTax}" type="currency"></fmt:formatNumber></td>
													</c:if>
													<td><fmt:formatNumber value="${pack.pickupFee + pack.dutyAndTax + pack.extendedDurationFee + pack.skidFee}" type="currency"></fmt:formatNumber></td>
												</tr>
											</c:if>
										</c:forEach>
									</c:if>
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<c:choose>
										<c:when test="${actionBean.kinekPoint.acceptsDutyAndTax && actionBean.hasExtendedFees}">
											<td rowspan="3" colspan="8">&nbsp;</td>
										</c:when>
										<c:when test="${actionBean.kinekPoint.acceptsDutyAndTax || actionBean.hasExtendedFees}">
											<td rowspan="3" colspan="7">&nbsp;</td>
										</c:when>
										<c:otherwise>
											<td rowspan="3" colspan="6">&nbsp;</td>
										</c:otherwise>
									</c:choose>
										
									<th colspan="2">Non-Taxable Amount: </th>
									<td id="totalReceivingFee"><fmt:formatNumber value="${actionBean.totalReceivingFee}" type="currency" /></td>
								</tr>
								<tr>
									<th colspan="2">Credit Amount: 
										<c:choose>
										<c:when test="${actionBean.consumerCreditsExist}">
											<s:submit 
												class="buttonLink" 
												name="selectCredits" 
												value="(Add Credits)" 
												title="Add Credits" />
										</c:when>
										<c:otherwise>
											(No Credits to apply)
										</c:otherwise>
										</c:choose>
									</th>
									<td id="totalCredit"><fmt:formatNumber value="${actionBean.totalCredits}" type="currency" /></td>
								</tr>
								<tr>
									<th colspan="2">Total:</th>
									<td id="totalFee"><strong><fmt:formatNumber value="${actionBean.totalFees}" type="currency" /></strong></td>
								</tr>
							</tfoot>
						</table>
					</div>

					<ol class="clearfix">																		
						<li class="rightalign">
							<s:submit class="buttonLink" name="view" value="Cancel" title="Cancel" />
							<span class="separator">|</span>
							<s:submit name="completePickup" class="button" value="Complete Pickup" onclick="return completePickup_onclick(event);"/>
						</li>								
					</ol>
					
				</fieldset>
			</s:form>
			
		</div>
	</s:layout-component>
</s:layout-render>
