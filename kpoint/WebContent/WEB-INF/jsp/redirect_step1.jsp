<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/redirect.jsp">
	<s:layout-component name="contents">
	
		<!-- STEPS -->
		<ul id="steps">
			<li class="active"><a href="#step1" title="Enter Package Information"><span>Step 1:</span> Enter Package Information</a></li>
			<li><a href="#step2" title="Redirect Package"><span>Step 2:</span> Redirect Package</a></li>
		</ul>
		<!-- /STEPS -->


		<!-- STEPCONTAINER -->
		<div id="stepContainer">
		<s:form beanclass="org.webdev.kpoint.action.RedirectActionBean">
			<fieldset><!-- STEP TITLE -->
				<h1><span>1</span>Redirect Delivery - Enter Package Information</h1>
				<!-- /STEP TITLE -->
				<stripes:errors />
				
				<stripes:messages />
				
				<!-- BLOCK -->
				<ol class="clearfix">
					<li class="half">
						<s:label for="courierId">Courier</s:label><span class="required">*</span><br />
						<s:select name="courierId" id="courierId">
							<s:option label="Please select a courier" value=""></s:option>
							<s:options-collection collection="${actionBean.couriers}" label="name" value="courierId" />
						</s:select>
						<small class="validation">* choose a courier from the list above to begin</small>
					</li>
					<li class="half">
						<s:label for="redirectLocation">Redirected To</s:label><span class="required">*</span><br />
						<s:text name="redirectLocation" id="redirectLocation" />
						<small class="validation">* Enter the location the courier will be holding/storing the package.</small>
					</li>
					<li class="half">
						<s:label for="reasonId">Reason</s:label><span class="required">*</span><br />
						<s:select name="reasonId" id="reasonId">
							<s:option label="Please select a redirect reason" value=""></s:option>
							<s:options-collection collection="${actionBean.redirectReasons}" label="name" value="reasonId" />
						</s:select>
						<small class="validation">* Please select a reason for redirecting the package.</small>
					</li>
					<li class="half">
						<s:label for="kinekNumber">Kinek #</s:label><span class="required">*</span><br />
						<s:text name="kinekNumber" id="kinekNumber" />
						<small class="validation">* Requires a Kinek# found on the address label of the package.</small>
					</li>
					<li class="half">
						<s:label for="customInfo">customInfo </s:label><br />
						<s:text name="customInfo" id="customInfo" />
						<small class="validation">* Optional field that can be used to enter courier custom information.</small>
					</li>
				
					<c:if test="${actionBean.activeUser.depotAdminAccessCheck && fn:length(actionBean.userDepots) > 1}">
					<li class="half">
						<s:label for="depotId">Depot:<span class="required" style="padding: 0px 2px 0px 4px;">*</span></s:label>
						<s:select name="depotId" id="depotId">
							<s:option label="Please select a depot" value=""></s:option>
							<s:options-collection collection="${actionBean.userDepots}" label="nameAddress1City" value="depotId" />
						</s:select>	
					</li>
					</c:if>					
				</ol>
				<!-- /BLOCK -->
				<s:hidden name="via" id="via" value="${actionBean.via}"></s:hidden>
				<!-- BLOCK -->
	
				<ol class="clearfix">
					<li class="rightalign half">
						<s:submit name="kinekNumberSearch" value="Unknown Kinek#" class="buttonLink"/>
						<s:submit name="search" class="button" value="Verify Redirect" />
					</li>
				</ol>
				<!-- /BLOCK --> <!-- FORM CONTENT -->
				<div class="formContent">
					<p><small>Fields marked with <span class="required">*</span> are required to proceed.</small></p>
				</div>
				<!-- /FORM CONTENT -->
			</fieldset>
		</s:form>
		</div>
		<!-- /STEPCONTAINER -->
	</s:layout-component>
</s:layout-render>