<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/depot.jsp">
	<s:layout-component name="contents">

		<!-- STEPS -->
		<ul id="steps">
			<li class="active"><a href="#step1" title="Choose Depot"><span>Step 1:</span> Choose Depot</a></li>
			<li><a href="#step2" title="Manage Users"><span>Step 2:</span> Manage Users</a></li>
		</ul>
		<!-- /STEPS -->
		
		<!-- STEPCONTAINER -->
		<div id="stepContainer">
			
			<!-- STEP TITLE -->
			<h1><span>1</span> Choose Depot</h1>
			<!-- /STEP TITLE -->
			
			<stripes:errors/>
			
			<stripes:messages/>
		    <script type="text/javascript">
		    jQuery(document).ready(function(){
		        function invoke(form, event, container) {
		            params = {};
		            if (event != null) params = event + '&' + jQuery(form).serialize();            
		            jQuery.post(form.action,
		                    params,
		                    function (xml) {
		                        jQuery(container).html(xml);
		                    });
		        }

				//Retrieves the depots for the specified state
		        jQuery(function() {
		        	jQuery('#stateId').change(function() {
		         		invoke(jQuery('form')[0], 'getDepotsAjax', '#depotId');
		            });
		        });
		    });
		    </script>
			
			<!-- ACCEPTDELIVERY STEP1 -->
			<s:form beanclass="org.webdev.kpoint.action.ManageUsersActionBean">
				<fieldset>								
					<!-- BLOCK -->
					<ol class="clearfix">
						<li class="half">
							<s:label for="stateId">State/Province/Region</s:label><span class="required">*</span><br />
							<s:select name="stateId" id="stateId">
								<s:option value="">Choose a State/Province/Region from the supplied list</s:option>
								<s:options-collection collection="${actionBean.activeStates}" label="name" value="stateId" />
							</s:select>
							<small class="validation">* requirements listed here</small>
						</li>
						<li class="half">
							<s:label for="depotId">Depot Name</s:label><span class="required">*</span><br />
							<s:select name="depotId" id="depotId">
								<s:option value="">Please select a state before selecting a depot</s:option>
							</s:select>
							<small class="validation">* requirements listed here</small>
						</li>																										
						<li class="rightalign">
							<s:submit name="userSearch" value="Manage Users" class="button" /> 
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
			<!-- /ACCEPTDELIVERY STEP1 -->
			
		</div>
		<!-- /STEPCONTAINER -->
	</s:layout-component>
</s:layout-render>