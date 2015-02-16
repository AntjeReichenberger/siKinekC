<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/pickup.jsp">
	<s:layout-component name="contents">
		<script type="text/javascript" src="resource/js/jquery.tablesorter.js"></script>
					<!-- STEPS -->
					<ul id="steps">
						<li class="active"><a href="#step1" title="ID Verification"><span>Step 1:</span> ID Verification</a></li>
						<li><a href="#step2" title="Package Summary"><span>Step 2:</span> Package Summary</a></li>
						<li><a href="#step3" title="Pickup Confirmation"><span>Step 3:</span> Pickup Confirmation</a></li>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
						
						<!-- STEP TITLE -->
						<h1><span>1</span> Pick-Up - ID Verification</h1>
						<!-- /STEP TITLE -->
						
						<stripes:errors/>
						
						<stripes:messages/>
						
						<script type="text/javascript">
							function setUserSearchState() {
								var selected = false;
								var dropdown = document.getElementById("depotId");
								if(dropdown == null || dropdown.selectedIndex > 0){
									selected = true;	
								}
								else{
									selected = false;
								}

								if(selected) {
									var buttons = document.getElementsByName("userSearch");
									buttons[0].disabled = false;
									buttons[0].className = "button";
								}
								else {
									var buttons = document.getElementsByName("userSearch");
									buttons[0].disabled = true;
									buttons[0].className = "buttonDisabled";
								}
							}
						
							function setFindPackagesButtonState() {
								var selected = false;
								var table = document.getElementById("usersTable");
								if (!table) {
									return;
								}
								
								var rows = table.rows;
								for(var i = 1; i < rows.length; i++) {
									if (rows[i].cells[0].childNodes[0].tagName == "INPUT" && rows[i].cells[0].childNodes[0].checked) {
										selected = true;
									}
								}
	
								if(selected) {
									var buttons = document.getElementsByName("getPackagesForUser");
									buttons[0].disabled = false;
									buttons[0].className = "button";
								}
								else {
									var buttons = document.getElementsByName("getPackagesForUser");
									buttons[0].disabled = true;
									buttons[0].className = "buttonDisabled";
								}
							}

							jQuery(document).ready(function(){
							setUserSearchState();
						 	jQuery('select#state').append("<option selected='selected'>Choose a State from the supplied list</option>");
				
							//Country toggle
							function countryOnChange() {
								jQuery('select#state').html("");
								
								//Choose canada
								if (jQuery("select#country").val() == '2') {
									jQuery('select#state').html("");
									//List all provinces
									jQuery('select#state').append("<option value='' selected='selected'>Choose a Province/Region from the list</option>");
									jQuery('select#state').append("<option value='57'>Alberta</option>");
									jQuery('select#state').append("<option value='56'>British Columbia</option>");
									jQuery('select#state').append("<option value='59'>Manitoba</option>");
									jQuery('select#state').append("<option value='61'>Newfoundland and Labrador</option>");
									jQuery('select#state').append("<option value='62'>New Brunswick</option>");
									jQuery('select#state').append("<option value='54'>Northwest Territories</option>");
									jQuery('select#state').append("<option value='63'>Nova Scotia</option>");
									jQuery('select#state').append("<option value='52'>Nunavut</option>");
									jQuery('select#state').append("<option value='55'>Ontario</option>");
									jQuery('select#state').append("<option value='64'>Prince Edward Island</option>");
									jQuery('select#state').append("<option value='53'>Quebec</option>");
									jQuery('select#state').append("<option value='58'>Saskatchewan</option>");
									jQuery('select#state').append("<option value='60'>Yukon</option>");
							 	//Choose USA
								} else if (jQuery("select#country").val() == '1') {
									jQuery('select#state').html("");
									//List all states
									jQuery('select#state').append("<option value='' selected='selected'>Choose a State from the supplied list</option>");
									jQuery('select#state').append("<option value='1'>Alabama</option>");
									jQuery('select#state').append("<option value='2'>Alaska</option>");
									jQuery('select#state').append("<option value='3'>Arizona</option>");
									jQuery('select#state').append("<option value='4'>Arkansas</option>");
									jQuery('select#state').append("<option value='5'>California</option>");
									jQuery('select#state').append("<option value='6'>Colorado</option>");
									jQuery('select#state').append("<option value='7'>Connecticut</option>");
									jQuery('select#state').append("<option value='8'>Delaware</option>");
									jQuery('select#state').append("<option value='9'>District of Columbia</option>");
									jQuery('select#state').append("<option value='10'>Florida</option>");
									jQuery('select#state').append("<option value='11'>Georgia</option>");
									jQuery('select#state').append("<option value='12'>Hawaii</option>");
									jQuery('select#state').append("<option value='13'>Idaho</option>");
									jQuery('select#state').append("<option value='14'>Illinois</option>");
									jQuery('select#state').append("<option value='15'>Indiana</option>");
									jQuery('select#state').append("<option value='16'>Iowa</option>");
									jQuery('select#state').append("<option value='17'>Kansas</option>");
									jQuery('select#state').append("<option value='18'>Kentucky</option>");
									jQuery('select#state').append("<option value='19'>Louisiana</option>");
									jQuery('select#state').append("<option value='20'>Maine</option>");
									jQuery('select#state').append("<option value='21'>Maryland</option>");
									jQuery('select#state').append("<option value='22'>Massachusetts</option>");
									jQuery('select#state').append("<option value='23'>Michigan</option>");
									jQuery('select#state').append("<option value='24'>Minnesota</option>");
									jQuery('select#state').append("<option value='25'>Mississippi</option>");
									jQuery('select#state').append("<option value='26'>Missouri</option>");
									jQuery('select#state').append("<option value='27'>Montana</option>");
									jQuery('select#state').append("<option value='28'>Nebraska</option>");
									jQuery('select#state').append("<option value='29'>Nevada</option>");
									jQuery('select#state').append("<option value='30'>New Hampshire</option>");
									jQuery('select#state').append("<option value='31'>New Jersey</option>");
									jQuery('select#state').append("<option value='32'>New Mexico</option>");
									jQuery('select#state').append("<option value='33'>New York</option>");
									jQuery('select#state').append("<option value='34'>North Carolina</option>");
									jQuery('select#state').append("<option value='35'>North Dakota</option>");
									jQuery('select#state').append("<option value='36'>Ohio</option>");
									jQuery('select#state').append("<option value='37'>Oklahoma</option>");
									jQuery('select#state').append("<option value='38'>Oregon</option>");
									jQuery('select#state').append("<option value='39'>Pennsylvania</option>");
									jQuery('select#state').append("<option value='40'>Rhode Island</option>");
									jQuery('select#state').append("<option value='41'>South Carolina</option>");
									jQuery('select#state').append("<option value='42'>South Dakota</option>");
									jQuery('select#state').append("<option value='43'>Tennessee</option>");
									jQuery('select#state').append("<option value='44'>Texas</option>");
									jQuery('select#state').append("<option value='45'>Utah</option>");
									jQuery('select#state').append("<option value='46'>Vermont</option>");
									jQuery('select#state').append("<option value='47'>Virginia</option>");
									jQuery('select#state').append("<option value='48'>Washington</option>");
									jQuery('select#state').append("<option value='49'>West Virginia</option>");
									jQuery('select#state').append("<option value='50'>Wisconsin</option>");
									jQuery('select#state').append("<option value='51'>Wyoming</option>");
								}
							}
				
							jQuery('select#country').change(countryOnChange);
							countryOnChange();
							populateState();
							
							 jQuery("#usersTable").tablesorter({
								//debug:true,
								sortList: [[2,0]] ,
								headers:{0:{sorter:false},
										 1:{sorter:false},					         
										 2:{sorter:'text'},
										 3:{sorter:false}
								}
							});
								
						   	}); 

							function populateState()
							{
								if(document.getElementById("stateHidden").value != null)
								{
									SetCheckedValue(document.getElementById("state"), document.getElementById("stateHidden").value);
								}
							}

							function updateStateHidden()
							{
								document.getElementById('stateHidden').value = document.getElementById('state').value;
							}

							function SetCheckedValue(radioObj, newValue) 
							{
								if(!radioObj)
									return;
								var radioLength = radioObj.length;
								if(radioLength == undefined) {
									radioObj.checked = (radioObj.value == newValue.toString());
									return;
								}
								for(var i = 0; i < radioLength; i++) {
									radioObj[i].selected = false;
									if(radioObj[i].value == newValue.toString()) {
										radioObj[i].selected = true;
									}
								}
							}
							function CheckUserRadio(e,kinekNumber){
								if(e.preventDefault) e.preventDefault();
								else e.returnValue = false;
								jQuery("#radio_"+ kinekNumber).attr('checked', true);
								setFindPackagesButtonState();
								if(!jQuery("input[name=next]").attr("disabled")){	
									setTimeout("jQuery('input[name=getPackagesForUser]').click()",50);
								}
							}
						</script>
					
						<!-- ACCEPTDELIVERY STEP1 -->
						<s:form beanclass="org.webdev.kpoint.action.PickupActionBean">
							<fieldset>
								<table border="0" class="superplain" style="width: 1050px; margin-top:0px">
									<tr>
										<td colspan="5">
										Enter Kinek # to find a consumer's package(s).
										</td>
									</tr>
									<tr>
										<td style="width: 100px;padding-top:12px;">
											<s:label for="kinekNumber">Depot:</s:label>
										</td>
										<td>
											<s:text name="kinekNumber" id="kinekNumber" style="width: 240px;" />
										</td>
										
										<c:choose>
										<c:when test="${actionBean.activeUser.depotAdminAccessCheck && fn:length(actionBean.userDepots) > 1}">
										<td style="width: 100px;padding-top:12px;">
											<s:label for="depotId">Depot:<span class="required" style="padding: 0px 2px 0px 4px;">*</span></s:label>
										</td>
										
										<td colspan="2">
											<s:select name="depotId" id="depotId" onchange="setUserSearchState()">
												<s:option label="Please select a depot" value=""></s:option>
												<s:options-collection collection="${actionBean.userDepots}" label="nameAddress1City" value="depotId" />
											</s:select>	
										</td>
										</c:when>
										<c:otherwise>
											<td colspan="3">
										</c:otherwise>
										</c:choose>
									</tr>
									<tr>
										<td colspan="5">
										You can also search by the entering one or all of the following details.
										</td>
									</tr>
									<tr>
										<td style="width: 100px;padding-top:12px;">
											<s:label for="firstName">First Name:</s:label>
										</td>
										<td>
											<s:text name="firstName" id="firstName" style="width: 240px;" />
										</td>
										<td style="width: 100px;padding-top:12px;">
											<s:label for="lastName">Last Name:</s:label>
										</td>
										<td colspan="2">
											<s:text name="lastName" id="lastName" style="width: 240px;" />
										</td>
									</tr>
									<tr>
										<td style="width: 100px;padding-top:12px;" >
											<s:label for="CountryId" class="label">Country:</s:label>
										</td>
										<td style="padding-top:12px;">
											<s:select name="country" id="country" style="width: 252px;">
												<s:options-collection collection="${actionBean.countries}" label="name" value="countryId" />
										  	</s:select>
										</td>
										<td style="width: 100px;padding-top:12px;">
											<s:label for="State" class="label">State / Province:</s:label>
										</td>
										<td>
											<s:hidden name="stateHidden" id="stateHidden"></s:hidden>
											<s:select name="state" id="state" style="width: 252px;" onchange="updateStateHidden();">
									  		</s:select>
										</td>
										<td>
											<s:submit name="userSearch" class="buttonDisabled" style="margin-top:-13px;" value="Search" disabled="true"/> 		
										</td>
									</tr>
								</table>
							</fieldset>
							
							<!-- FORM CONTENT -->
							<div class="formContent">
								<table id="usersTable" class="sortable">
									<thead>
										<tr>
											<th></th>
											<th>Kinek Number</th>
											<th>Customer Name</th>
											<th>Address</th>	
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${fn:length(actionBean.userSearchResults) > 0}">
												<c:forEach items="${actionBean.userSearchResults}" var="user" varStatus="loop">
													<tr>
														<td><s:radio name="selectedConsumer" value="${user.kinekNumber}" class="radio" id ="radio_${user.kinekNumber}" onclick="setFindPackagesButtonState()" /></td>																				
															<td><a href="#" onclick='return CheckUserRadio(event,"${user.kinekNumber}")'>${user.kinekNumber}</a></td>
															<td><a href="#" onclick='return CheckUserRadio(event,"${user.kinekNumber}")'>${user.firstName} ${user.lastName}</a></td>
															<td>
																${user.address1} ${user.address2}, ${user.city}, ${user.state.name}, ${user.state.country.name}  ${user.zip} 
															</td>
													</tr>
												</c:forEach>												
											</c:when>
											<c:otherwise>
													<tr>
														<td colspan="4" class="emptymessage">No users were found that match the search criteria you provided.</td>
													</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>	
							</div>
							
							<div>
								<s:submit id="getPackagesForUser" name="getPackagesForUser" value="Find Packages" class="buttonDisabled" style="margin: 0 15px 0 0px; float: right;" disabled="true"/>
								<div style="clear:both"></div>
							</div>
							
						</s:form>   
						<!-- /ACCEPTDELIVERY STEP1 -->
						
					</div>
					<!-- /STEPCONTAINER -->
	</s:layout-component>
</s:layout-render>
