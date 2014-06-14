<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">
	<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('form input').keypress(function(e){
			if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
	            jQuery('#save').click();
	            return false;
	        } else {
	            return true;
	        }
	      });
	});
	</script>
	
	
		<div class="clearfix"></div>
		<div id="adbModule">	
			<!-- add Tracking List -->
			<s:errors />
			<s:messages />			
			<s:form name="addTrackingFrm" action="/Tracking.action" id="addTrackingFrm">
				<table class="superplain form" style="width:470px">
					<tr>
						<td colspan="2">						
							Want to know where your package is? Simply enter your tracking number below and a tracking name to associate it with. 
							It will be added to your list of tracked packages. If you would like to receive tracking notifications via email or SMS, go to
							your <a href="/kpointC/MyProfile.action">profile page</a>.
						</td>
					</tr>
					<tr>
						<td><s:label for="packageNickname">Package Nickname</s:label></td>
						<td><s:text name="packageNickname" id="packageName"></s:text><span class="required">*</span></td>
					</tr>
					<tr>
						<td><s:label for="trackingNumber">Tracking Number</s:label> </td>
						<td><s:text name="trackingNumber" id="trackingNumber"></s:text><span class="required">*</span></td>
					</tr>
					<tr>
					<td><s:label for="courier">Courier</s:label> </td>
					<td>
						<s:select name="courier" id="courier" >
							<s:option label="Auto Detect" value="" />
							<s:options-collection collection="${actionBean.couriers}" label="name" value="courierId" />
						</s:select>
					</td>	
					</tr>		
					<tr>
						<td colspan="2">
							<s:submit name="cancel" id="cancel" value="Cancel" class="button"></s:submit>
							<s:submit name="save" id="save" value="Save" class="default button"></s:submit>																
						</td>
					</tr>
					<s:hidden name="action"></s:hidden>
				</table>
			</s:form>	
			<!-- // add Tracking List -->
		</div>	
	</s:layout-component>
</s:layout-render>