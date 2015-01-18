<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/accountdashboard.jsp">
  	<s:layout-component name="contents">
  	<link rel="stylesheet" href="resource/css/pages/contactus.css" type="text/css" />

	<jsp:useBean id="externalSettingsManager" scope="application" class="org.webdev.kpoint.util.ExternalSettingsManagerHelper"/>

		<!-- CONTENT -->
		<br class="clear" />
		
		<div id="adbModule">
			
			<h1>Contact Us</h1>
			
			<div class="leftcol">
				<table class="superplain clear">
					<tr>
						<td>
							<p class="bold">
								${externalSettingsManager.externalKinekContactUsName}
							</p>
							<p>
								${externalSettingsManager.externalKinekContactUsAddressLine1}<br/>
								${externalSettingsManager.externalKinekContactUsAddressLine2}<br/>
								${externalSettingsManager.externalKinekContactUsAddressLine3}
							</p>							
							<table class="phonetable">
								<tr>
									<td>Email:</td>
									<td class="phoneval">support@kinek.zendesk.com</td>
								</tr>
								<tr>
									<td>Phone:</td>
									<td class="phoneval">1 (866) 451-5565</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td class="phoneval">&nbsp;</td>
								</tr>
							</table>					
						</td>
						<td>
							<p class="bold">
								&nbsp;
							</p>

							<p>
								&nbsp;<br/>
								&nbsp;<br/>
								&nbsp;
							</p>
							
							<table class="phonetable">
								<tr>
									<td>You can also get in touch with us by using our online chat.  If we are offline please leave a message and we will get back to you as soon as possible.</td>
								</tr>
							</table>	
						</td>
					</tr>
				</table>
			</div>
			<div class="rightcol">
				<img src="resource/images/contactus.png" alt="Kinek Office" />
			</div>
			
		</div>
		<!-- /CONTENT -->

  </s:layout-component>
</s:layout-render>
