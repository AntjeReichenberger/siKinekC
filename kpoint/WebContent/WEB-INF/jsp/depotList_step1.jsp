<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/depotList.jsp">
	<s:layout-component name="contents">
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
			   jQuery("#Depots").tablesorter({
					//debug:true,
					headers:{0:{sorter:'text'},
							 1:{sorter:'text'},
					         2:{sorter:'integer'},
							 3:{sorter:'MMMMDDYYYY'},
							 4:{sorter:false}
					}
				});
			});
		</script>
		
		<!-- STEPS -->
		<ul id="steps">
			<li class="active"><a href="#step1" title="Choose Depot"><span>Step 1:</span>Depot List</a></li>
			<li><a href="#step2" title="View Users"><span>Step 2:</span>View Users</a></li>
		</ul>
		<!-- /STEPS -->
		
		<!-- STEPCONTAINER -->
		<div id="stepContainer">
			
			<!-- ACCEPTDELIVERY STEP1 -->
			<s:form beanclass="org.webdev.kpoint.action.FindDepotActionBean" >
				<!-- STEP TITLE -->
				<h1><span>1</span> Manage Depots</h1>
				<!-- /STEP TITLE -->
				
				<stripes:errors/>
				
				<fieldset>
					
					<!-- FORM CONTENT -->
					<div class="formContent">
						<s:label for="statusId">Status </s:label>
						<s:select name="statusId" id="statusId" style="width: 100px; margin: 0px 10px;">
							<s:options-collection collection="${actionBean.statuses}" label="name" value="id" />
						</s:select>
						
						<s:submit name="search" value="Search" class="button" />
						
						<table id="Depots" class="sortable">
							<thead>
								<tr>
									<th width="19%">Name</th>
									<th width="19%">City</th>
									<th width="19%">Phone</th>
									<th width="19%">Created Date</th>
									<th width="24%" class="nosort"></th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${actionBean.depots}" var="depot" varStatus="loop">
								<tr>
									<td width="19%">${depot.name}</td>
									<td width="19%">${depot.city}</td>
									<td width="19%">${depot.phone}</td>
									<td width="19%"><fmt:formatDate value="${depot.createdDate.time}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>
									<td width="24%">
										<a href="/kpoint/Depot.action?depotId=${depot.depotId}" title="Edit Depot">Edit Depot</a>
										&nbsp;&nbsp;
										<a href="/kpoint/ViewDepot.action?depotId=${depot.depotId}" title="View Depot Details">View Depot Details</a>
										&nbsp;&nbsp;
										<a href="/kpoint/DepotList.action?depotId=${depot.depotId}" title="View Users">View Users</a>
									</td>
								</tr>	
							</c:forEach>
																																																															
							</tbody>
						</table>
					</div>
					<!-- /FORM CONTENT -->
					
				</fieldset>
			</s:form>
			<!-- /ACCEPTDELIVERY STEP1 -->
			
		</div>
		<!-- /STEPCONTAINER -->
	 </s:layout-component>
 </s:layout-render>