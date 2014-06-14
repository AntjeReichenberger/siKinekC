<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>



<s:layout-render name="/WEB-INF/jsp/layout.jsp">

  <s:layout-component name="body">    	
	   	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">
	<script type="text/javascript">

		jQuery(document).ready(function()
		{			
			jQuery("#alwaysShowCoupon_NO").attr("checked","true");

			//Enable date pickers
			jQuery('.date-pick').datePicker({
				
				startDate: '01/01/2010'
				
			});			

			//Set start date range so it cant be after end date
			
			//Add days actually adds days to the date object, this is why coupon end date does not add 1 day
			var date = new Date();
			jQuery('#couponStartDate').dpSetStartDate(date.asString());
			jQuery('#couponExpiryDate').dpSetStartDate(date.addDays(1).asString());
			
			
			jQuery('#couponEndDate').dpSetStartDate(date.asString());
			
			jQuery('#couponStartDate').bind(
						'dpClosed',
						function(e, selectedDates)
						{
							var d = selectedDates[0];
							if(d){
								d = new Date(d);
								jQuery('#couponEndDate').dpSetStartDate(d.addDays(1).asString());
							}
						}
			);

			jQuery('#couponEndDate').bind(
					'dpClosed',
					function(e, selectedDates)
					{
						var d = selectedDates[0];
						if(d){
							d = new Date(d);
							jQuery('#couponStartDate').dpSetEndDate(d.addDays(-1).asString());
						}
					}
			); 

			jQuery('#descriptionId').maxLength(1000);
				
		});

		function disableRegionAndOrgDD(){
			if(document.getElementById("kinekPointDD").selectedIndex != 0){
				document.getElementById("regionDD").disabled = true;
				document.getElementById("organizationDD").disabled = true;
			}else{
				document.getElementById("regionDD").disabled = false;
				document.getElementById("organizationDD").disabled = false;
			}
		}
	</script>  
		<!-- CONTENT -->
		<div id="content" class="wide clearfix">

	
			<!-- INTERFACEWRAPPER -->
	 	<div id="interfaceWrapper">
				
				<!-- INTERFACE -->				
			<div id="interface">
				
					<!-- STEPS -->
					<ul id="steps">
						<!--  Organization TITLE DECISION  -->
						<c:choose>
							<c:when test="${actionBean.couponType == 'KP'}">
								<li class="active"><a href="#" title="Create New Coupon">Create KinekPoint Coupon</a></li>
							</c:when>
							<c:otherwise>
								<li class="active"><a href="#" title="Create New Coupon">Create Generic Coupon</a></li>
							</c:otherwise>
						</c:choose>														
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
						<c:choose>
							<c:when test="${actionBean.couponType == 'KP'}"><h1>Create KinekPoint Coupon</h1></c:when>
							<c:otherwise><h1>Create Generic Coupon</h1></c:otherwise>
						</c:choose>	
						<!-- EDITDEPOTPROFILE STEP1 -->
						<s:form beanclass="org.webdev.kpoint.action.ManageCouponsActionBean" enctype="false" >
												
							<s:errors/>
							<s:messages/>
						
							<fieldset>								
								<!-- /BLOCK -->								
								<!-- FORM CONTENT -->
								<div class="formContent couponForm">								
																	
									<ol class="clearfix"">
										<li class="half">
											<label>Title</label><span class="required">*</span><br />
											<s:text name="title" id="couponTitle" maxlength="100" style="width:550px"></s:text>
											<small class="validation">* Enter the title of the coupon (Maximum length: 100 characters)</small>											
										</li>																
									</ol>
									<ol class="clearfix">
										<li class="half">
											<label for="description">Description</label><span class="required">*</span><br />
											<s:textarea name="description" id="descriptionId" cols="50" rows="5" style="width:550px" ></s:textarea>	
											<small class="validation">* Enter the description of the coupon  (Maximum length: 1000 characters)</small>									
										</li>																
									</ol> 
									<ol class="clearfix"">
										<li class="half">
											<label>Coupon Image</label><span class="required">*</span><br />
											<s:file name="couponImage" id="couponImage" accept="image/gif, image/jpeg, image/png"></s:file>
											<small>* Select an image for upload (Required Size: ${actionBean.imgWidth}px by ${actionBean.imgHeight}px, Maximum Disk Size: ${actionBean.imgSize/1024}kb)</small>											
										</li>																
									</ol>						
									<ol class="clearfix"">
										<li class="half">
											<label>Expiry Date</label><span class="required">*</span><br/>
											<s:text name="expiryDate" id="couponExpiryDate" class="date-pick"></s:text>		
											<small class="validation">* Select the expiry date of the coupon</small>									
										</li>																
									</ol>	
									<ol class="clearfix"">
										<li class="half">
											<label>Start Date</label><span class="required">*</span><br />
											<s:text name="startDate" id="couponStartDate" class="date-pick"></s:text>		
											<small class="validation">* Select the start date of the coupon</small>									
										</li>																
									</ol>
									<ol class="clearfix"">
										<li class="half">
											<label>End Date</label><span class="required">*</span><br />
											<s:text name="endDate" id="couponEndDate" class="date-pick"></s:text>		
											<small class="validation">* Select the end date of the coupon</small>									
										</li>																
									</ol>									
									
									<!-- <div class="alwaysShowCoupon"> -->
									<ol class="clearfix">
										<li class="half">									
										<label class="centerBoxLabel">Always Show Coupon<span class="required">*</span> 
										<s:radio class="checkRadio" value="YES" id="alwaysShowCoupon_YES" name="alwaysShowCoupon" style="margin-bottom:2px"></s:radio>&nbsp;YES
										<s:radio class="checkRadio" value="NO" id="alwaysShowCoupon_NO" name="alwaysShowCoupon" checked="true" style="margin-bottom:2px"></s:radio>&nbsp;NO</label>
										</li>
									</ol> 
									<!-- </div> -->
									<!-- LIST of TARGET EMAILS -->
									
									<ol class="clearfix">
										<li class="half">
											<label style="padding-bottom:5px;">Target Emails</label><span class="required">*</span>											
											<c:choose>
											<c:when test="${actionBean.couponType == 'KP'}">
												<div class="chkBoxSpace"><label class="centerBoxLabel"><s:checkbox class="checkRadio" name="targetEmailDelivery" value="targetEmail.delivery"></s:checkbox>&nbsp;Delivery</label></div>
												<!--<s:checkbox class="checkbox" name="targetEmailDeliveryReminder" value="targetEmail.deliveryReminder"></s:checkbox>Delivery Reminder<br/>-->
												<div class="chkBoxSpace"><label class="centerBoxLabel"><s:checkbox class="checkRadio" name="targetEmailWelcome" value="targetEmail.welcome"></s:checkbox>&nbsp;Welcome</label></div>
											</c:when>
											<c:otherwise>
												<div class="chkBoxSpace"><label class="centerBoxLabel"><s:checkbox class="checkRadio" name="targetEmailRegistrationReminder" value="targetEmail.registrationReminder"></s:checkbox>&nbsp;Registration Reminder</label></div>
											</c:otherwise>
											</c:choose>
											
											<!--<small class="validation">* Select one or more target email(s)</small> -->
										</li>											
									</ol>
									
									<!--  Regional Constraint, Organization Constraint KinekPoint Constraint -->																																																			
									<div id="kpCouponConstraints" class="${(actionBean.couponType == 'KP') ? '' : 'hideConstraints' }">																																												
									<ol class="clearfix">
										<li class="half">
											<label>Region</label><br />
											<s:select name="regionId"  id="regionDD">
												<s:option value="" label="Select a region"></s:option>
												<s:options-collection collection="${actionBean.regions}" label="name" value="regionId" />
											</s:select>
										</li>																
									</ol>
													

									<ol class="clearfix">
										<li class="half">
											<label>Organization</label><br />
											<s:select name="organizationId" id="organizationDD">
												<s:option value="" label="Select an organization"></s:option>
												<s:options-collection collection="${actionBean.organizations}" label="name" value="organizationId" />
											</s:select>
										</li>																
									</ol>													

									<ol class="clearfix">
										<li class="half">
											<label>KinekPoint</label><br />
											<s:select name="kinekPointId" id="kinekPointDD" onchange="disableRegionAndOrgDD()">
												<s:option value="" label="Select a KinekPoint"></s:option>
												<s:options-collection collection="${actionBean.kinekPoints}" label="nameAddress1City" value="depotId" />
											</s:select>
										</li>																
									</ol>
									</div>
									<s:hidden name="couponType"></s:hidden>
									<div class="couponSubmitButton">
										<s:submit name="createCoupon" class="button" value="Create Coupon"/><br></br>
										<p style="margin-left:10px"><small>Fields marked with <span class="required">*</span> are required to proceed.</small></p>
									</div>										
								</div>
								<!-- /FORM CONTENT -->
								
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
