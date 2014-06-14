<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/adb_smscontactinfo.jsp">
	<s:layout-component name="smscontactinfo">
		<script type="text/javascript">
			// <![CDATA[
			jQuery(document).ready(function(){
				//Set input mask for cell phone field
				jQuery('#user\\.phone').mask('(999) 999-9999');
			});
			// ]]>
		</script>
			
		<s:form beanclass="org.webdev.kpoint.action.SMSContactInfoActionBean">
			<s:messages />
			
			<table class="superplain contentwrapper">
				<tr>
					<td class="text">
						<p>
							At Kinek we take pride in making things easy.
						</p>
						<p>
							With that in mind you can send your Kinek # and KinekPoint address to your mobile phone.  
							That way, it will always be at hand when you need to receive a package.
						</p>
						<p>
							You can even send your Kinek Address to your family and friends so they know where to ship your gift packages.
						</p>
					</td>
					<td class="formwrapper">
						<table class="superplain form">
							<s:errors />
							
							<tr>
								<td class="headertext" colspan="3">
									Always have your Kinek Address with you or text it to your friends.
								</td>
							</tr>
							<tr class="mobile">
								<td class="label">
									<s:label for="mobilePhoneNumber">Mobile #:</s:label>
								</td>
								<td class="input" colspan="2">
									<s:text name="mobilePhoneNumber" id="user.phone"></s:text>
								</td>
							</tr>
							<tr>
								<td colspan="3">
									<span class="italic">Kinek will not send advertising messages to your phones.</span>
								</td>
							</tr>
						</table>
						
						<table class="superplain buttonwrapper">
							<tr>
								<td>
									<s:submit name="sendContactSMS" value="Send Text Message" class="button" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</s:form>
	</s:layout-component>
</s:layout-render>
