<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/tagsearch.jsp">
	<s:layout-component name="contents">

		<!-- STEPS -->
		<ul id="steps">
			<li class="active"><a href="#step1" title="Find Person"><span>Step 1:</span> Find Person</a></li>
			<li><a href="#step2" title="Search Results"><span>Step 2:</span> Search Results</a></li>
		</ul>
		<!-- /STEPS -->

		<!-- STEPCONTAINER -->
		<div id="stepContainer">
			<!-- STEP TITLE -->
			<h1><span>1</span>Unknown Kinek# - Find Person</h1>
			<!-- /STEP TITLE -->
			<stripes:errors />
			<stripes:messages />
			
			<!-- ACCEPTDELIVERY STEP1 -->
			<s:form beanclass="org.webdev.kpoint.action.KinekNumberSearchActionBean">
				<fieldset>
					<s:hidden name="via" />
					<s:hidden name="redirectLocation" />
					<s:hidden name="reasonId" />
					<s:hidden name="customInfo" />
					<s:hidden name="courierId" />
					<!-- BLOCK -->
					<ol class="clearfix half">
						<li>
							<s:label for="kinekNumber">Kinek #</s:label><span class="required">*</span><br />
							<s:text name="kinekNumber" id="kinekNumber" />
							<small class="validation">* Enter the Kinek# provided by the customer.</small>
						</li>
						<li class="rightalign">
							<s:submit name="searchByKinekNumber" class="button" value="Find a Person" />
						</li>
					</ol>
					<!-- /BLOCK --> 
					
					<!-- BLOCK -->
					<ol class="clearfix half">
						<li class="half">
							<s:label for="firstName">First Name</s:label><br />
							<s:text name="firstName" id="firstName" />
							<small class="validation">* Enter the first name of the customer.</small>
						</li>
						<li class="half">
							<s:label for="lastName">Last Name</s:label><br />
							<s:text name="lastName" id="lastName" />
							<small class="validation">* Enter the last name of the customer.</small>
						</li>
						<li class="half">
							<s:label for="phone">Phone Number</s:label><br />
							<s:text name="phone" id="phone" />
							<small class="validation">* Enter the phone number of the customer.</small>
						</li>
						<li class="rightalign">
							<s:submit name="searchByContact" class="button" value="Find a Person" />
						</li>
					</ol>
					<!-- /BLOCK --> 
					
					<!-- FORM CONTENT -->
					<div class="formContent" style="clear:both;">
						<p><small>Fields marked with <span class="required">*</span> are required to proceed.</small></p>
					</div>
					<!-- /FORM CONTENT -->
				</fieldset>
			</s:form> <!-- /ACCEPTDELIVERY STEP1 -->
		</div>
		<!-- /STEPCONTAINER -->
	</s:layout-component>
</s:layout-render>
