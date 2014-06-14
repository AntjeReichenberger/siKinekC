<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/deliver.jsp">
	<s:layout-component name="contents">
		<!-- STEPS -->
		<ul id="steps">
			<li class="active"><a href="#step1" title="Choose Package Recipient"><span>Step 1:</span>Choose Package Recipients</a></li>
			<li><a href="#step2" title="Enter Package Information"><span>Step 2:</span>Enter Package Information</a></li>
		</ul>
		<!-- /STEPS -->
		
		<!-- STEPCONTAINER -->
			<div style="float:right !important; margin-right:280px; margin-top: 10px;">
				<%@ include file="/WEB-INF/jsp/includes/sidebar.jsp"%>
			</div>				
		<div id="stepContainer">
		<s:form beanclass="org.webdev.kpoint.action.DeliveryActionBean">
			<!-- STEP TITLE -->
			<h1><span>1</span>Accept Delivery - Choose Package Recipients</h1>
			
			<!-- /STEP TITLE -->
			
			<script type="text/javascript">

			   jQuery(document).ready(function(){
				 	//Set input mask for phone field
					jQuery('#phone').mask('(999) 999-9999');
			   });
			   
			function setNextBtnState() {
				var selected = false;

				var inputEle=document.getElementsByTagName("input");

				var inputLength=inputEle.length;	
				for(var i=0; i<inputLength; i++){
					if(inputEle[i].name=="selectedConsumer"){
						if(inputEle[i].checked){
							selected=true;
							break;
						}
					}
				}

				if(selected) {
					var button = document.getElementById("deliverNextTabBtn");
					button.disabled = false;
					button.className = "button";
				}
				else {
					var button = document.getElementById("deliverNextTabBtn");
					button.disabled = true;
					button.className = "buttonDisabled";
				}
			}
			function CheckUserBox(e,kinekNumber){
				if(e.preventDefault) e.preventDefault();
				else e.returnValue = false;
				jQuery("#checkBox_"+ kinekNumber).attr('checked', true);
				setNextBtnState();
				if(!jQuery("input[name=next]").attr("disabled")){	
					setTimeout("jQuery('input[name=next]').click()",50);
				}
			}
		
		</script>			
			
			<s:messages />
			
			<s:errors />
			
			<!-- ACCEPTDELIVERY STEP1 -->
				<fieldset>
					
					<!-- BLOCK -->
					<table border="0" class="superplain" style="width:900px; margin-top:0px;">
						<tr>
							<td colspan="6">
							<p>Enter the Kinek # found on the package.</p>
							</td>
						</tr>
						<tr>				
							<td width="85px" style="padding-top:18px;" >
								<s:label for="kinekNumber">Kinek Number</s:label> 
							</td>
							<td width="180px" style="padding-top:15px;">
								<s:text size="7" name="kinekNumber" style="width:180px"/>								
								<!-- <small class="validation">* Requires a Kinek# found on the address label of the package</small>  -->
							</td>
							<td colspan="2" align="left">	
								<s:hidden name="via" id="via"></s:hidden>
								<div id="userSearchBtn" ><s:submit name="userSearch" class="button" value="Search" /></div>	
							</td>
							<td colspan="2">
							</td>
						</tr>
						<tr>
							<td colspan="6">
							<p>No Kinek # on the package?  You can also search by one or all of the following details.</p>
							</td>
						</tr>
						<tr>
						    <td><s:label for="firstName" style="padding-top:4px;" >First Name</s:label></td>
							<td><s:text name="firstName" id="firstName" style="width: 180px;" /></td>
							<td><s:label for="lastName" style="padding-left:15px;padding-top:4px;" >Last Name</s:label></td>
							<td><s:text name="lastName" id="lastName" style="width: 180px;" /></td>			
							<td width="50px" style="padding-top:12px;" ><s:label for="phone"></s:label></span></td>																									
							<td width="180px"><s:text id="phone" size="7" name="phone" style="width:180px"/></td>
						</tr>
					</table>
					<!-- FORM CONTENT -->					
				</fieldset>				
					<!-- FORM CONTENT -->
					
							<div class="formContent">
								<table id="usersTable" class="sortable">
									<thead>
										<tr>
											<th></th>
											<th style="text-align: center !important;">Kinek Number</th>
											<th style="text-align: center !important;">Customer Name</th>
											<th>Address</th>
											<th style="text-align: center !important;">Phone</th>	
										</tr>
									</thead>
									<tbody>
									
										<c:choose>
											<c:when test="${fn:length(actionBean.userSearchResults) > 0}">
												<c:forEach items="${actionBean.userSearchResults}" var="user" varStatus="loop">
													<tr>
														<td><s:checkbox name="selectedConsumer" value="${user.kinekNumber}" id="checkBox_${user.kinekNumber}" class="checkBox" onclick="setNextBtnState()" /></td>
														<td align="center"><a href="#" onclick='return CheckUserBox(event,"${user.kinekNumber}")'>${user.kinekNumber}</a></td>
														<td align="center"><a href="#" onclick='return CheckUserBox(event,"${user.kinekNumber}")'>${user.firstName} ${user.lastName}</a></td>
														<td>
															${user.address1} ${user.address2}, ${user.city}, ${user.state.name}, ${user.state.country.name}  ${user.zip} 
														</td>	
														<td align="center">${user.phone}</td>
													</tr>	
												</c:forEach>
											</c:when>
											<c:otherwise>
													<tr>
														<td colspan="5" class="emptymessage">No users were found that match the search criteria you provided.</td>
													</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>	
							</div>		
							
							<div>
								<s:submit id="deliverNextTabBtn" name="next" value="Next" class="buttonDisabled" style="margin: 0 15px 0 0px; float: right;" disabled="true" />
								<div style="clear:both"></div>
							</div>	
					
			<!-- /ACCEPTDELIVERY STEP1 -->
		</s:form>  	
		</div>
		<!-- /STEPCONTAINER -->
	 </s:layout-component>
 </s:layout-render>