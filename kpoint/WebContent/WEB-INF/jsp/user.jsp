<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>



<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
    <script src="resource/js/adduser.js" type="text/javascript"></script>
	<script type="text/javascript">
	// <![CDATA[
		jQuery(document).ready(function()
		{
			jQuery('select#user\\.depot').val("${actionBean.user.depot.depotId}");

			//Set input mask for phone field
			jQuery('#user\\.cellPhone').mask('(999) 999-9999');

		});
	// ]]>
	</script>
  
  
   	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">


		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<!-- INTERFACEWRAPPER -->
			<div id="interfaceWrapper">
				
				<!-- INTERFACE -->				
				<div id="interface">
				
					<!-- STEPS -->
					<ul id="steps">
						<!--  USER TITLE DECISION  -->
						<c:choose>
							<c:when test="${actionBean.userId == 0}">
								<li class="active"><a href="#" title="Create New User">Create New User</a></li>
							</c:when>
							<c:otherwise>
								<li class="active"><a href="#" title="Edit User">Edit User</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
						<c:choose>
							<c:when test="${actionBean.userId == 0}"><h1>Create New User</h1></c:when>
							<c:otherwise><h1>Edit User</h1></c:otherwise>
						</c:choose>
						
						<!-- EDITDEPOTPROFILE STEP1 -->
						<s:form beanclass="org.webdev.kpoint.action.AddUserActionBean">
							<s:errors/>
							<s:messages/>
						
							<fieldset>
								<s:hidden name="userId"></s:hidden>								
								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="half">
										<label>Status</label><span class="required">*</span><br />
										<s:label for="user.enabled.true" class="radio"><s:radio name="user.enabled" id="user.enabled.true" value="true" class="radio" /> Active </s:label>
										<s:label for="user.enabled.false" class="radio"><s:radio name="user.enabled" id="user.enabled.false" value="false" class="radio" /> Inactive</s:label>
									</li>																
								</ol>
								
								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="half">
										<s:label for="user.firstName">First Name</s:label><span class="required">*</span><br />
										<s:text name="user.firstName" id="user.firstName" />
										<small class="validation">* Enter first name.</small>
									</li>
									<li class="half">
										<s:label for="user.lastName">Last Name</s:label><span class="required">*</span><br />
										<s:text name="user.lastName" id="user.lastName" />
										<small class="validation">* Enter last name.</small>
									</li>																	
								</ol>
								<!-- /BLOCK -->															

								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="half">
										<s:label for="user.username">User Name</s:label><span class="required">*</span><br />
										<s:text name="user.username" id="user.username" />
										<small class="validation">* Enter a user name.</small>
									</li>
									<li class="half">
										<s:label for="user.email">Email Address</s:label><span class="required">*</span><br />
										<s:text name="user.email" id="user.email" />
										<small class="validation">* Enter a valid email address.</small>
									</li>																	
								</ol>
								<!-- /BLOCK -->	
								
								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="half">
										<s:label for="user.password">Password</s:label><span class="required">*</span><br />
										<s:password name="user.password" id="user.password" repopulate="true"/>
										<small class="validation">* Please enter a temporary password.</small>
									</li>
									<li class="half">
										<s:label for="confirmPassword">Confirm Password</s:label><span class="required">*</span><br />
										<s:password name="confirmPassword" id="confirmPassword" repopulate="true"/>
										<small class="validation">* Just to be sure!</small>
									</li>
									
									<li class="half">
										<s:label for="user.roleId">Roles</s:label><span class="required">*</span><br />
										<s:select name="user.roleId" id="user.roleId" onchange="ShowHideDepotListBox()">
											<s:option value="">Please select a role</s:option>
											<s:options-collection collection="${actionBean.displayRoles}" label="name" value="roleId" />
									  	</s:select>
										<small class="validation">* choose a role from the list above</small>
									</li>
									
									<c:if test="${actionBean.userId == 0 || actionBean.activeUser.adminAccessCheck}">	
									<c:choose>
										<c:when test="${(actionBean.activeUser.adminAccessCheck) || (actionBean.activeUser.depotAdminAccessCheck)}">
											<li class="half" id="depotsListBox" style="display:none; padding:0px !important; margin:0px !important;"> 
												<table class="superplain" style="width:560px; margin-top:4px; margin-left:20px;">
													<tr>
													  <td style="width:260px">				
														<s:label for="depots">List of depots</s:label><br />
														<s:select name="listDepots" id="listDepots" multiple="true" size="5" >													
															<s:options-collection collection="${actionBean.depotListBox}" label="nameAddress1City" value="depotId" />
													  	</s:select>											
													   </td>
													   <td style="width:40px; vertical-align: middle">
														<button style="width:40px" type="button" name="addSelectedDepotBtn" id="addSelectedDepotBtn" onclick="addDepot()">&#62;</button>
														<button style="width:40px" type="button" name="removeSelectedDepotBtn" id="removeSelectedDepotBtn" onclick="removeDepot()">&#60;</button>
													   </td>
													   <td  style="width:260px"> 
											   			<s:label for="selectedDepots">Selected Depots</s:label><br />
														<s:select id="selectedDepots" name="selectedDepots"  multiple="true" size="5" >
															<s:options-collection collection="${actionBean.selectedDepotListBox}" label="nameAddress1City" value="depotId" />
														</s:select>
													   </td>
													</tr>
												</table>												
											</li>										
										</c:when>
									</c:choose>	
									</c:if>																
								</ol>
								<!-- /BLOCK -->									
								
								<ol class="clearfix">
									<li class="half">
										<s:label for="user.cellPhone">Mobile Phone</s:label><br />
										<s:text name="user.cellPhone" id="user.cellPhone" />
										<small class="validation">* Enter a mobile phone number.</small>
									</li>						    
									<c:choose>
										<c:when test="${actionBean.activeUser.adminAccessCheck}">
											<li id="defaultDepot" class="half">
												<s:label for="depot">Default Depot</s:label><span class="required">*</span><br />
												<s:select name="depot" id="user.depot" ></s:select>
												<small class="validation">* choose a depot from the list above</small>
											</li>										
										</c:when>
										<c:when test="${actionBean.activeUser.depotAdminAccessCheck}">
											<li id="defaultDepot" class="half">
												<s:label for="depot">Default Depot</s:label><span class="required">*</span><br />
												<s:select name="depot" id="user.depot" >
													<s:options-collection collection="${actionBean.depotListCreate}" label="nameAddress1City" value="depotId" />
												</s:select>
												<small class="validation">* choose a depot from the list above DEPOT</small>
											</li>										
										</c:when>
										

										<c:otherwise>
											<!-- This cannot be a stripes control or it gets auto-filled improperly -->
											<input name="depot" value="${activeUser.depot.depotId}" type="hidden" />
										</c:otherwise>
									</c:choose>									
								</ol>
								
								<!-- BLOCK -->
								<ol class="clearfix">
																	
								</ol>
								<!-- /BLOCK -->	
								
								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="rightalign">
										<a href="./ManageUsers.action" title="Cancel">Cancel</a>
										<span class="separator">|</span>
										
										<c:choose>
											<c:when test="${actionBean.userId == 0}">
												<s:submit name="createUser" class="button" value="Create New User"/>
											</c:when>
											<c:otherwise>
												<s:submit name="editUser" class="button" value="Save Changes"/>
											</c:otherwise>
										</c:choose>
									</li>									
								</ol>
								<!-- /BLOCK -->

								<!-- FORM CONTENT -->
								<div class="formContent">
									<p><small>Fields marked with <span class="required">*</span> are required to proceed.</small></p>
								</div>
								<!-- /FORM CONTENT -->
								<s:hidden id="depotIds" name="selectedDepotIds" ></s:hidden>
								<script type="text/javascript">
	
								<c:choose>
								<c:when test="${actionBean.activeUser.adminAccessCheck || actionBean.userId == 0}">
									
										jQuery(document).ready(
												function(){
													 ShowHideDepotListBox();
												}
										);
	
										function invokeJ(form, event, container) {
								            params = {};
								            if (event != null) params = event + '&' + jQuery('#user\\.roleId').serialize() + '&' + 'userId=' + ${actionBean.userId};            
								            jQuery.post(form.action,
								                    params,
								                    function (xml) {
								                        jQuery('#user\\.depot').html(xml);
								                    });
								        }
										
										function addDepot(){
											var depotsEle = document.getElementById("listDepots");
											
	
											if(depotsEle.selectedIndex != -1){
												var selectedItemEle = depotsEle.options[depotsEle.selectedIndex];
										 		var selectedDepotsEle = document.getElementById("selectedDepots");
										 		var selectedDepotsLen = selectedDepotsEle.length;
										 		var defaultDepotEle = document.getElementById("user.depot");
	
										 		//added selected list box item in the hidden fields
										 		var depotIdsHF = document.getElementById("depotIds");
										 		
											 	var selectedOption = document.createElement("option");
											 	var defaultDepotOption = document.createElement("option");
											 	
											 		selectedOption.text = selectedItemEle.text;
											 		selectedOption.value = selectedItemEle.value;
	
											 		defaultDepotOption.text = selectedItemEle.text;
											 		defaultDepotOption.value = selectedItemEle.value;
											 											 		
												 	depotIdsHF.value += selectedOption.value+","; 	
											 	
											 		//alert(selectedItemEle+" "+depotIdsHF.value);
	
											 		//remove from the List of depots list box
											 		depotsEle.remove(depotsEle.selectedIndex);
										 		
											 	    try {									 	    	
										 		    	selectedDepotsEle.add(selectedOption,null); // standards compliant; doesn't work in IE
										 		    	defaultDepotEle.add(defaultDepotOption,null);
										 	    	 }
										 	     	catch(ex) {									 	    	
										 	        	selectedDepotsEle.add(selectedOption); // IE only
										 	        	defaultDepotEle.add(defaultDepotOption);
										 	     	}
											}
										}
	
										function removeDepot(){
											var selectedDepotsEle = document.getElementById("selectedDepots");
											var defaultDepotEle = document.getElementById("user.depot");
											var selectedDepotsLen = selectedDepotsEle.length;
											var defaultDepotLen = defaultDepotEle.length;
											
											if(selectedDepotsEle.selectedIndex != -1){
												var selectedItemEle = selectedDepotsEle.options[selectedDepotsEle.selectedIndex];  
	
										 		var depotsEle = document.getElementById("listDepots");
	
											 	var depotOption = document.createElement("option");
											 	
											 	
											 		depotOption.text = selectedItemEle.text;
											 		depotOption.value = selectedItemEle.value;
											 	
											 		
											 	var depotIdsHF = document.getElementById("depotIds");
	
											 		//TODO: remove selected id from depotIdsHF 
							 						if(depotIdsHF.value != ""){
								 						var depotIdsArray = depotIdsHF.value.split(",");
							 							var foundIndex = found(selectedItemEle.value,depotIdsArray);
							 							if(foundIndex != -1){
							 								depotIdsArray.splice(foundIndex,1);
							 								depotIdsHF.value = depotIdsArray;
							 							}
							 							//alert("searched Item:"+foundIndex);						 						
							 						}
						 						
										 			//alert(depotIdsHF.value);
	
										 			//remove from the List of depots list box
										 			var selectedItemValue = selectedDepotsEle.options[selectedDepotsEle.selectedIndex].value;	
										 			selectedDepotsEle.remove(selectedDepotsEle.selectedIndex);
	
										 			//delete the item which is matched against selectedItemValue
										 			
										 			for(var j=0; j<defaultDepotLen; j++){
											 			var defaultDepotId = defaultDepotEle.options[j].value;
										 				if(selectedItemValue == defaultDepotId){
										 					defaultDepotEle.remove(j);
										 					break;
											 			}
										 			}
		
										 			
										 	    	try {									 	    	
										 	    		depotsEle.add(depotOption,null); // standards compliant; doesn't work in IE									 	    		
										 	     	}
										 	     	catch(ex) {									 	    	
										 	        	depotsEle.add(depotOption); // IE only
										 	     	}
											}										
										}
	
										function found(searchItem,depotIds){
											
											var foundIndex = -1;
											
											var length = depotIds.length;
											for(var i=0; i<length; i++){											
												if(depotIds[i] == searchItem){
													foundIndex = i;												
													break;
												}
											}
											return foundIndex;
										}
	
										function ShowHideDepotListBox(){
	
											var roleEle = document.getElementById("user.roleId");
											var selectedItemEle = roleEle.options[roleEle.selectedIndex];
	
											
											if(selectedItemEle.value ==  ${actionBean.depotAdminRoleId}){
												var depotListBox = document.getElementById("depotsListBox");
												depotListBox.style.display="block";
												var defaultDepot = document.getElementById("defaultDepot");
												defaultDepot.style.display="none";
												
											}else{
												var depotListBox = document.getElementById("depotsListBox");
												depotListBox.style.display="none";
												var defaultDepot = document.getElementById("defaultDepot");
												defaultDepot.style.display="block";
												
												document.getElementById("depotIds").value="";
												var select = document.getElementById("selectedDepots");
												var depotsEle = document.getElementById("listDepots");
																																		
												if ( select.hasChildNodes() )
												{
												    while ( select.childNodes.length >= 1 )
												    {											    
													    if(select.firstChild.text != undefined){	
													 		var depotOption = document.createElement("option");
												 			depotOption.text = select.firstChild.text;
												 			depotOption.value = select.firstChild.value;
											 			
											 	    		try {									 	    	
											 	    			depotsEle.add(depotOption,null); // standards compliant; doesn't work in IE									 	    		
											 	     		}
											 	     		catch(ex) {									 	    	
											 	        		depotsEle.add(depotOption); // IE only
											 	     		}													
													    }
												    	select.removeChild( select.firstChild );       
												    } 
												}
												if(!(jQuery('#user\\.depot').val() > 0)){
													invokeJ(jQuery('form')[0], 'getDepotsAjax', '#user.depot');
												}
											}
	
											getDepots(selectedItemEle.value);
										}
									
								</c:when>
								<c:otherwise>
									jQuery(document).ready(
											function(){
												 ShowHideDepotListBox();
											}
									);
								
									function ShowHideDepotListBox(){

										var roleEle = document.getElementById("user.roleId");
										var selectedItemEle = roleEle.options[roleEle.selectedIndex];

										
										if(selectedItemEle.value ==  ${actionBean.depotAdminRoleId}){
											var defaultDepot = document.getElementById("defaultDepot");
											defaultDepot.style.display="none";
											
										}else{
											var defaultDepot = document.getElementById("defaultDepot");
											defaultDepot.style.display="block";
										}		
									}
								
								</c:otherwise>
								</c:choose>
								</script>
								
							</fieldset>
						</s:form>
						<!-- /EDITDEPOTPROFILE STEP1 -->
						
					</div>
					<!-- /STEPCONTAINER -->

				</div>
				<!-- /INTERFACE -->
				
			</div>
			<!-- /INTERFACEWRAPPER -->
						
		</div>
		<!-- /CONTENT -->
		
	</div>
	<!-- /CONTENTWRAPPER -->
	
	
	<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>
  </s:layout-component>
</s:layout-render>