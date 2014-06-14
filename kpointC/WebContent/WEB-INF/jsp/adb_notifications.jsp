<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
	<s:layout-component name="contents">	
	<link rel="stylesheet" href="resource/css/pages/dashboard/dashboard-notifications.css" type="text/css" />

	<script type="text/javascript">
	// <![CDATA[
		jQuery(document).ready(function(){
			jQuery('#adpnav li:eq(2)').addClass('active');
		});
	// ]]>
	</script>
	
	<br class="clear"/>
	
	<div id="adbModule" style="width:875px !important">
		<div id="pageTitle">
			<h1>Edit Notifications</h1>
			<br />
		</div>
		
		<s:form beanclass="org.webdev.kpoint.action.NotificationsActionBean" id="notifications">			
			<fieldset class="userform">
				<s:errors />
				<s:messages />
				
				<table class="superplain form">
					<tr>
						<td class="labelright">
							<label for="user.cellPhone" class="wide">Cell/Mobile Number:&nbsp;</label>
						</td>
						<td class="valright">
							<s:text name="user.cellPhone" id="cellPhone"/>
							<img src="resource/images/help_icon.png" id="cellPhoneInfo" onmouseover="openInfoDialog('cellPhoneInfoPopUpDiv', this)" onmouseout="closeInfoDialog('cellPhoneInfoPopUpDiv')"></img>
						</td>
					</tr>
					<tr>
						<td class="labelright">
							<label for="user.receiveEmailTrackingStatus">Receive tracking notifications via email:</label>							
						</td>
						<td class="valright" id="receiveEmailTrackingStatusChkBox" style="vertical-align:middle">
							<s:checkbox id="receiveEmailTrackingStatusChkBox" name="receiveEmailTrackingStatus" class="checkbox"/>
						</td>
					</tr>
					<tr>
						<td class="labelright">
							<label for="receiveTextMsgTrackingStatus">Receive tracking notifications via text msg:</label>							
						</td>
						<td class="valright" align="left" style="vertical-align:middle">
							<s:checkbox id="receiveTextMsgTrackingStatusChkBox" name="receiveTextMsgTrackingStatus" class="checkbox"/>
						</td>
					</tr>
					<tr>
						<td class="labelright">
							<label for="receiveTextMsgDeliveryStatus">Receive delivery notifications via text msg:</label>							
						</td>
						<td class="valright" align="left" style="vertical-align:middle">
							<s:checkbox id="receiveTextMsgDeliveryStatusChkBox" name="receiveTextMsgDeliveryStatus" class="checkbox"/>
						</td>
					</tr>
					<tr>
						<td class="labelright"></td>
						<td class="valright">
							<s:submit name="storeUser" id="changeSubmit" value="Save Changes" class="button" />
						</td>
					</tr>
				</table>
			
				<div id="cellPhoneInfoPopUpDiv" title="Cell/Mobile Number" class="hidden infoPopUpDiv">
					Enter to receive text message delivery notifications.
				</div>
			
				<div id="primaryPhoneinfoPopUpDiv" title="Primary Contact Number" class="hidden infoPopUpDiv">
					Enter your phone number so KinekPoints can contact you if any problems arise.
				</div>
			
				<div id="profileInfoPopUpDiv" title="Change Your Address" class="hidden infoPopUpDiv">
					When you pick up your packages from a KinekPoint, we ask to see 2 pieces of ID to compare against your Kinek profile. This is part of the process we use to ensure the security of your packages. However, as part of this process, we require that your address information cannot be edited after it has been entered.
				</div>
			</fieldset>
		</s:form>
	</div>
	
	<script type="text/javascript">
	// <![CDATA[
		   jQuery(document).ready(function(){
			 	//Set input mask for phone field
				jQuery('#cellPhone').mask('(999) 999-9999');

				//Add cell field placeholder text
				var cellInput = jQuery('#user\\.cellPhone');
				if(cellInput.val() == '') {
					cellInput
						.attr('placeholder', 'Enter to receive text notifications')
						.val(cellInput.attr('placeholder'))
						.focus(function() {
							var $this = jQuery(this);
							if($this.val() == $this.attr('placeholder')) {
								$this.val('');
							}
						})
						.blur(function() {
							var $this = jQuery(this);
							if($this.val() == '') {
								$this.val($this.attr('placeholder'));
							}
						});
					jQuery('#changeSubmit').click(function() {
						if(cellInput.val() == cellInput.attr('placeholder')) {
							cellInput.val('');
						}
					});
				}
		   });
	// ]]>
	</script>

	</s:layout-component>
</s:layout-render>