<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/adb_emailcontactinfo.jsp">
	<s:layout-component name="emailcontactinfo">
	
	<script type="text/javascript">
		function validatePreview()
		{
			var message = "<div id=\"message\" class=\"error\">";
			var isValid = true;
			
			if (jQuery('#emailAddress').val() == "")
			{
				isValid = false;
				message += "<p>Please enter an Email address to preview</p>";
			}
			
			message += "</div>";

			if(!isValid)
			{
				jQuery('#errors').html(message);
			}
			else
			{
				jQuery('#errors').html("");
			}

			return isValid;
		}
	</script>
	
	<s:form beanclass="org.webdev.kpoint.action.EmailContactInfoActionBean">
	<s:messages />
	
	<table class="superplain contentwrapper">
		<tr>
			<td class="leftcol">
				<div>
					<p>At Kinek we take pride in making things easy.</p>
					<p>With that in mind you can send your Kinek # and KinekPoint address to your own email address.  That way, it will always be at hand when you need to receive a package.</p>
					<p>You can even email your Kinek Address to your family and friends so they know where to ship your gift packages. </p>
				</div>
			</td>
			<td class="rightcol">
				<div class="errorMessage">
					<s:errors />
					<div id="errors"></div>
				</div>
			  
				<s:hidden name="referrer"></s:hidden>
				<table class="superplain formwrapper">
					<tr>
						<td class="headertext" colspan="3">
							Always have your Kinek Address with you or email it to your friends.
						</td>
					</tr>
					<tr class="email">
						<td><s:label for="emailAddress">Email Address:</s:label></td>
						<td colspan="2"><s:text name="emailAddress" id="emailAddress"></s:text></td>
					</tr>
					<tr class="message">
						<td><label for="customMessage">Custom Message:</label></td>
						<td colspan="2"><s:textarea name="customMessage" id="customMessage" rows="4"></s:textarea></td>
					</tr>
				</table>
				<table class="superplain buttonswrapper">
					<tr>
						<td>
							<s:submit name="preview" value="Preview Email" class="button" onclick="return validatePreview();"/>
							<s:submit name="send" value="Send Email" class="button" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	
	</s:form>
	</s:layout-component>
</s:layout-render>
