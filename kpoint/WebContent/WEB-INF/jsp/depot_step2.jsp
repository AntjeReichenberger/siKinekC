<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/depot.jsp">
	<s:layout-component name="contents">
	<jsp:useBean id="settingsManager" scope="application" class="org.webdev.kpoint.util.SettingsManagerHelper"/>
	
	<script src="resource/js/nicEdit.js" type="text/javascript"></script>
		<script type="text/javascript">
			bkLib.onDomLoaded(function() {
				new nicEditor({buttonList : ['bold', 'italic', 'underline'], iconsPath : 'resource/images/nicEditorIcons.gif'}).panelInstance('depot.extraInfo');
			});	
		</script>
		
		<style type="text/css">
			textarea {
			  width: 100%;
			  min-width: 100%;
			  max-width: 100%;
			}
			
			p {
				margin: 0px;
				padding: 0px;
			}
		</style>
	
	<script type="text/javascript" src="resource/js/jquery.maxlength.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			var creditCardElementId = '#payMethod_' + '${settingsManager.depotPaymentTypeCreditCardId}';
			jQuery(creditCardElementId).click(function(){
				jQuery('#creditCardTypes').slideToggle();
				if (jQuery(this).is(':checked')) {
					// do nothing
				} else {
					jQuery('#creditCardTypes input').removeAttr('checked');
				}
			});
			if (jQuery(creditCardElementId).is(":checked")) {
				jQuery('#creditCardTypes').show();
			} else {
				jQuery('#creditCardTypes').hide();
			}

			jQuery('#depot\\.extraInfo').maxLength(1000);

		});
	</script>
		<!-- STEPS -->
		<ul id="steps">
			<c:choose>
			<c:when test="${actionBean.createDepot}">
				<li><a href="#step1" title="Edit KinekPoint Information"><span>Step 1:</span> KinekPoint Information</a></li>
				<li class="active"><a href="#step2" title="Edit Services &amp; Features"><span>Step 2:</span> Services &amp; Features</a></li>
				<li><a href="#step3" title="Edit Pricing"><span>Step 3:</span> Pricing</a></li>
				<li><a href="#step4" title="Edit Hours of Operation"><span>Step 4:</span> Hours of Operation</a></li>
			</c:when>
			<c:otherwise>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean">
						<s:param name="depotId" value="${actionBean.depotId}" />
						KinekPoint Information
					</s:link>
				</li>
				<li class="active">
					<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean" event="viewFeatures">
						<s:param name="depotId" value="${actionBean.depotId}" />
						Services &amp; Features
					</s:link>
				</li>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean" event="viewPrices">
						<s:param name="depotId" value="${actionBean.depotId}" />
						Pricing
					</s:link>
				</li>
				<li>
					<s:link beanclass="org.webdev.kpoint.action.ManageDepotActionBean" event="viewHours">
						<s:param name="depotId" value="${actionBean.depotId}" />
						Hours of Operation
					</s:link>
				</li>
			</c:otherwise>
			</c:choose>
		</ul>
		<!-- /STEPS -->
		
		<!-- STEPCONTAINER -->
		<div id="stepContainer">
			
			<h1 style="float:right;">KinekPoint: ${actionBean.depotName}</h1>
			
			<!-- STEP TITLE -->
			<h1>
				<c:choose>
					<c:when test="${actionBean.createDepot}">
						<span>2</span> 
					</c:when>
				</c:choose>
				Edit Services &amp; Features
			</h1>
			<!-- /STEP TITLE -->

			<stripes:errors/>
			
			<stripes:messages/>
			
			<!-- EDITDEPOTPROFILE STEP2 -->
			<s:form beanclass="org.webdev.kpoint.action.ManageDepotActionBean">
				<fieldset>
				
					<s:hidden name="depotId"/>
					<s:hidden name="createDepot"/>
					<s:hidden name="depot.depotId"/>
										
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<label>Service Available In</label><br />
							<c:forEach items="${actionBean.languages}" var="lang" varStatus="loop">
								<s:label class="checkbox" for="language_${lang.languageId}"><s:checkbox name="languageIds" class="checkbox" value="${lang.languageId}" id="language_${lang.languageId}" /> ${lang.name}</s:label>	
							</c:forEach>
							<small class="validation">* Check which languages are provided by your depot</small>
						</li>
						<li class="half">
							<label>Methods of Payments</label><br />
							<c:forEach items="${actionBean.payMethods}" var="pm" varStatus="loop">
								<s:label class="checkbox" for="payMethod_${pm.payMethodId}"><s:checkbox class="checkbox" name="paymentMethodIds" value="${pm.payMethodId}" id="payMethod_${pm.payMethodId}" /> ${pm.name}</s:label>	
							</c:forEach>
							<small class="validation">* Check which methods of payments are provided by your depot</small>
						</li>																	
					</ol>
					<!-- /BLOCK -->																

					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<label>Other Features</label><br />
							<c:forEach items="${actionBean.features}" var="f" varStatus="loop">
								<s:label class="checkbox" for="feature_${f.featureId}"><s:checkbox class="checkbox" name="featureIds" value="${f.featureId}" id="feature_${f.featureId}" /> ${f.name}</s:label>													
     						</c:forEach>
							<small class="validation">* Check which languages are provided by your depot</small>
						</li>
						<li class="half" id="creditCardTypes">
							<label>Accepted Credit Cards</label><br />
							<c:forEach items="${actionBean.creditCards}" var="card" varStatus="loop">
								<s:label class="checkbox" for="creditCard_${card.cardId}"><s:checkbox class="checkbox" name="creditCardIds" value="${card.cardId}" id="creditCard_${card.cardId}" /> ${card.name}</s:label>	
							</c:forEach>
							<small class="validation">* Check which credit cards are accept by your depot</small>
						</li>
					</ol>
					<!-- /BLOCK -->
						
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li>
							<s:label for="depot.extraInfo">Enter Your Additional Information Here...</s:label><br />
							<s:textarea name="depot.extraInfo" id="depot.extraInfo" rows="13" cols="14"></s:textarea>
							<small class="validation">* Enter additional information (maximum 1000 characters)</small>
						</li>																	
					</ol>
					<!-- /BLOCK -->
					
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="rightalign half">
							<s:link beanclass="org.webdev.kpoint.action.ViewKinekPointActionBean">Cancel</s:link>
							<span class="separator">|</span>
							<s:submit class="button" name="saveFeatures" value="Save Changes" /> 
						</li>									
					</ol>
					<!-- /BLOCK -->

					<!-- FORM CONTENT -->
					<div class="formContent">
						<p><small>Fields marked with <span class="required">*</span> are required to proceed.</small></p>
					</div>
					<!-- /FORM CONTENT -->
					
				</fieldset>
			</s:form> 
			<!-- /EDITDEPOTPROFILE STEP2 -->
			
		</div>
		<!-- /STEPCONTAINER -->
 	</s:layout-component>
</s:layout-render>