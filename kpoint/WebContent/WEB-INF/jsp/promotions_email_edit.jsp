<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/promotions.jsp">
	<s:layout-component name="contents">

		<h1>KP Email Preview</h1>

		<stripes:errors/>
		<stripes:messages/>

		<s:form beanclass="org.webdev.kpoint.action.ManagePromotionsActionBean">
			<fieldset>
					<s:hidden name="depotId"/>
					<s:hidden name="promotionId"/>

			
				<ol class="clearfix">
					<li class="half">
						An Email will be sent to the selected KinekPoint, ${actionBean.depotName},<br/>
						to inform them of the promotion campaign details. Feel free to<br/>
						add an additional message before you send the email.<br/><br/><br/>
						<label>KinekPoint: </label> ${actionBean.depotName}<br/>
						<label>Recipient: </label> ${actionBean.depotEmail}
					</li>
				</ol>

				<ol class="clearfix">
					<li class="half">
						<label for="emailMessage">Additional Message:</label>
						<s:textarea name="emailMessage" id="emailMessage" rows="10"></s:textarea>
					</li>
				</ol>

				<ol class="clearfix">
					<li class="rightalign">
						<s:submit name="previewEmail" value="Preview" class="button" />
						<s:submit name="sendEmail" value="Send" class="button" />
					</li>
				</ol>
				
			</fieldset>
		</s:form>

	</s:layout-component>
 </s:layout-render>
