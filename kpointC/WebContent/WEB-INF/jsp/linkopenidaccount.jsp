<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Link OpenID Account</s:layout-component>
  	
  	<s:layout-component name="body">
  	<link rel="stylesheet" href="resource/css/pages/linkopenidaccount.css" type="text/css" media="screen,projection" />
  	
	<script type="text/javascript">
		// <![CDATA[
			jQuery(document).ready(function(){
				jQuery('#nav li:eq(0)').addClass('active');
			});

			function input_onKeyPress(e){
				var intKey;
				if (window.event) {
					intKey = e.keyCode;
				}
				else {
					intKey = e.which;
				}
				
				if (intKey == 13) {
					if (window.event) {
						e.returnValue = false;
					    e.cancel = true;
					}
					jQuery('#linkaccountBT').click();
					return false;
				}
				return true;
			}
		// ]]>
	</script>
		
  	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">

			<div id="linkAccountHeader">
				<h1>Link my Account</h1>
				<br/>
			</div>
			
			<s:form beanclass="org.webdev.kpoint.action.LinkOpenIDAccountActionBean" id="linkaccount">
				<fieldset>
					<s:errors />
					<s:messages />
					<div class="searchTitle">
						If you already have a Kinek account and want to link it with your ${actionBean.provider} account please enter your original Kinek log-in information below and click "Link My Account", otherwise click "Create new Account".
					</div>
					
					<div id="basicSearch">
						<div id="leftForm">
							<table class="superplain form">
								<tr>
									<td class="label">
										<s:label for="username">Email </s:label>
									</td>
									<td class="value">
										 <s:text name="username" onkeypress="input_onKeyPress(event);"/>
									</td>
								</tr>
								<tr>
									<td class="label">
										<s:label for="passwd">Password </s:label>
									</td>
									<td class="value">
										<s:password name="passwd" onkeypress="input_onKeyPress(event);"/><br />
									</td>
								</tr>
								<tr>
									<td class="buttonwrapper" colspan="2">
										<s:submit name="skiplink" id="skiplink" value="Create new Account" class="buttonLink" />
										<span class="separator">|</span>
										<s:submit name="link" id="linkaccountBT" value="Link my Account" class="button"  />
									</td>
								</tr>
							</table>
						</div>
					</div>
					
					<s:hidden name="provider"></s:hidden>
				</fieldset>		
			</s:form>
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->
   </s:layout-component>
</s:layout-render>
	