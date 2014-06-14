<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>



<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
		<script type="text/javascript" src="resource/js/jquery.tablesorter.js"></script>
		<script type="text/javascript">
			function trim(stringToTrim) {
				return stringToTrim.replace(/^\s+|\s+$/g,"");
			}

			var months = new Array();
	        months["January"] = "00";
	        months["February"] = "01";
	        months["March"] = "02";
	        months["April"] = "03";
	        months["May"] = "04";
	        months["June"] = "05";
	        months["July"] = "06";
	        months["August"] = "07";
	        months["September"] = "08";
	        months["October"] = "09";
	        months["November"] = "10";
	        months["December"] = "11";

		   	var dateRegistered='';
		   	
            jQuery.tablesorter.addParser({
            	//set unique id
            	id:'MMMMDDYYYY',
                is:function(s) {
                	return false;
                },
                format:function(s){
                	//custom format
                	var date = trim(s).match(/^(\w{1,10})[ ](\d{1,2}),[ ](\d{4})$/);

                	//November 02, 2009
                	if(date != null) {
                    	var m = months[date[1]];
                        var d = String(date[2]);
                        if (d.length == 1) {d = "0" + d;}
                        var y = date[3];
         
	   					var time = parseFloat(new Date(y,m,d).getTime());
                        //console.log('format time: '+time);
                     
                        return time;
                    } else {
                    	return 0;
                    }
                },
                //set type as numeric
                type:'numeric'
            });

			jQuery(function(){
			   jQuery("#orglist").tablesorter({
					//debug:true,
					headers:{0:{sorter:'text'},
							 1:{sorter:'text'},					         
							 2:{sorter:'MMMMDDYYYY'},
							 3:{sorter:false}
					}
				});
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
						<li class="active"><a href="#" title="View Organization">View Organization</a></li>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
						<h1>View Organization</h1>						
						<!-- EDITDEPOTPROFILE STEP1 -->
						<s:form beanclass="org.webdev.kpoint.action.ManageOrganizationActionBean">
												
							<s:errors/>
							<s:messages/>
						
							<fieldset>
								<s:hidden name="organizationId"></s:hidden>
								<!-- FORM CONTENT -->
								<table id="orglist" class="sortable">
									<thead>
										<tr>
											<th>Name</th>
											<th>Description (First 100 Characters)</th>
											<th>Created Date</th>
											<th class="nosort">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${actionBean.organizations != null && fn:length(actionBean.organizations) > 0}">
											<c:forEach items="${actionBean.organizations}" var="org" varStatus="loop">
											<tr>
												<td width="25%">${org.name}</td>
												<td width="45%">${fn:substring(org.description,0,100)}</td>
												<td width="15%"><fmt:formatDate value="${org.createdDate}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>		
												<td width="10%"><a href="/kpoint/Organization.action?action=Edit&organizationId=${org.organizationId}" title="Edit Organization">Edit Organization</a></td>
											</tr>	
											</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td colspan='4' class="orgRecordNotFound">No records are available.</td>
												</tr>
											</c:otherwise>
										</c:choose>											
									</tbody>
								</table>												
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