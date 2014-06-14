<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">

		<div id="contentWrapper" class="clearfix"><!-- CONTENT -->
		<div id="content" class="wide clearfix">
		
		<s:form beanclass="org.webdev.kpoint.action.ExportSystemDataActionBean" focus="">
		
				<!-- STEP TITLE --><h1>Export System Data</h1><!-- /STEP TITLE -->
	
				<stripes:errors/>
				<stripes:messages/>
				
			<script type="text/javascript">
			// <![CDATA[
				jQuery(document).ready(function(){
					
					//Enable date pickers
					jQuery('.date-pick').datePicker({
						startDate: '01/01/2009',
						endDate: (new Date()).asString()
					});
					
					//Set start date range so it cant be after end date
					jQuery('#date_start').bind(
						'dpClosed',
						function(e, selectedDates) {
							var d = selectedDates[0];
							if (d) {
								d = new Date(d);
								jQuery('#date_end').dpSetStartDate(d.addDays(1).asString());
							}
						}
					);
					
					//Set end date range so it cant be before start date
					jQuery('#date_end').bind(
						'dpClosed',
						function(e, selectedDates) {
							var d = selectedDates[0];
							if (d) {
								d = new Date(d);
								jQuery('#date_start').dpSetEndDate(d.addDays(-1).asString());
							}
						}
					);
				
				});
			
			// ]]>
			</script>
				
			<h2>Consumer Report</h2>

			<!-- CONSUMERREPORT -->
			<fieldset>
				
				<!-- BLOCK -->
				<ol class="clearfix">
					<li style="width: 130px;" class="third">
						<s:label for="filterOption">Filter On</s:label><span class="required">*</span><br />
						<select name="filterOption" id="filterOption" style="width:125px;">
							<option value="1">Sign up date</option>
							<option value="2">Most Recent Parcel</option>
						</select>							
					</li>
					<li style="width: 380px" class="third">
						<label for="date_start">Date Range </label><span class="required">*</span><br/>
						<s:text name="startDate" id="date_start" class="date-pick" />
						<s:text name="endDate" id="date_end" class="date-pick" />							
					</li>
					<li style="width: 20%;" class="third inline">
						<stripes:submit name="fetchConsumerReport" class="button" value="Get Report" style="margin: 10px 0 0 0;" />
					</li>									
				</ol>
				<!-- /BLOCK -->
				
			</fieldset>
			<!-- /CONSUMERREPORT -->
		
		</s:form>
		</div>
		<!-- /CONTENT --></div>
		<!-- /CONTENTWRAPPER -->

		<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>

	</s:layout-component>
</s:layout-render>