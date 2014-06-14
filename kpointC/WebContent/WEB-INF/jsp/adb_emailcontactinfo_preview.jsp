<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/adb_emailcontactinfo.jsp">
	<s:layout-component name="emailcontactinfo">
			<s:form beanclass="org.webdev.kpoint.action.EmailContactInfoActionBean">
				<fieldset>
					<s:errors />
					<s:messages />
					
					<s:hidden name="referrer"></s:hidden>
					<ol class="clearfix">
						<li>
							<s:label for="emailAddress">Email Address</s:label><br />
							${actionBean.emailAddress}
							<s:hidden name="emailAddress" id="emailAddress"></s:hidden>
						</li>
						<li class="clearfix">
							<label>Email Body</label><br />
							<s:hidden name="customMessage" id="customMessage" />
							<div class="emailPreview">
								${actionBean.emailPreview}
							</div>
						</li>
					</ol>
					<ol class="clearfix">
						<li class="rightalign">
							<s:submit name="view" value="Edit Email" class="button" />
							<s:submit name="send" value="Send Email" class="button" />
						</li>
					</ol>
				</fieldset>
			</s:form>
	</s:layout-component>
</s:layout-render>
