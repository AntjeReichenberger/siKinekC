<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>



<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
		
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
			jQuery('#description').maxLength(1000);

		});	
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
						<!--  Organization TITLE DECISION  -->
						<c:choose>
							<c:when test="${actionBean.organizationId == 0}">
								<li class="active"><a href="#" title="Create New Organization">Create New Organization</a></li>
							</c:when>
							<c:otherwise>
								<li class="active"><a href="#" title="Edit Organization">Edit Organization</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
						<c:choose>
							<c:when test="${actionBean.organizationId == 0}"><h1>Create New Organization</h1></c:when>
							<c:otherwise><h1>Edit Organization</h1></c:otherwise>
						</c:choose>
						
						<!-- EDITDEPOTPROFILE STEP1 -->
						<s:form beanclass="org.webdev.kpoint.action.ManageOrganizationActionBean">
												
							<s:errors/>
							<s:messages/>
						
							<fieldset>
								<s:hidden name="organizationId"></s:hidden>
													
								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="half">
										<s:label for="name">Name</s:label><span class="required">*</span><br />
										<s:text name="name" id="name" maxlength="50" value="" />
										<small class="validation">* Enter Organization name. (Only accepts 50 characters)</small>
									</li>																	
								</ol>
								<!-- /BLOCK -->															

								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="half">
										<s:label for="description">Description</s:label><br />
										<s:textarea name="description" id="description" rows="5" cols="5"></s:textarea>
										<small class="validation">* Enter Organization description. (Only accepts 1000 characters)</small>
									</li>
								</ol>		
								
								<!-- BLOCK -->
								<ol class="clearfix">
									<li class="leftalign">
										
										<a href="/kpoint/Organization.action?action=View" title="Cancel">Cancel</a>
										<span class="separator">|</span>
										
										<c:choose>
											<c:when test="${actionBean.organizationId == 0}">
												<s:submit name="createOrganization" class="button" value="Create Organization"/>
											</c:when>
											<c:otherwise>
												<s:submit name="editOrganization" class="button" value="Save Changes"/>
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