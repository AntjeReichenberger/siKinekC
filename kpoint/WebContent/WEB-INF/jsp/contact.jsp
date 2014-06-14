<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>


<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Contact Kinek</s:layout-component>
  	<s:layout-component name="body">
  	
  		<style type="text/css">
  		.bold
  		{
  			font-weight: bold;
  		}
  		
  		.email
  		{
  			color: #058FD3 !important;
  			border-bottom:1px solid #058FD3 !important;
  		}
  		
  		.phonetable
  		{
  			width: auto; margin: 0px;
  		}
  		
  		.phonetable td
  		{
  			padding: 0px;
  		}
  		
  	</style>
	<jsp:useBean id="externalSettingsManager" scope="application" class="org.webdev.kpoint.util.ExternalSettingsManagerHelper"/>
  
  	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper" class="clearfix">
	
		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<!-- INTERFACEWRAPPER -->
			<div id="interfaceWrapper">
			
				<!-- INTERFACE -->				
				<div id="interface">
				
					<!-- STEPS -->
					<ul id="steps">
						<li class="active"><a href="#" title="Contact Us">Contact Us</a></li>
					</ul>
					<!-- /STEPS -->
					
					<!-- STEPCONTAINER -->
					<div id="stepContainer">
			
						<h1>Contact Us</h1>
						
						<div style="float:right;position:relative;width:350px;">
							<img src="resource/images/contactus.png" alt="Kinek Office" style="margin-top: -10px;" />
						</div>
						
						<div style="width: 520px; display: inline-block;">
							<table class="superplain clear" style="margin: 0px;">
								<tr>
									<td>
										<p class="bold">
											${externalSettingsManager.externalKinekContactUsName}
										</p>
			
										<p>
											${externalSettingsManager.externalKinekContactUsAddressLine1}<br/>
											${externalSettingsManager.externalKinekContactUsAddressLine2}<br/>
											${externalSettingsManager.externalKinekContactUsAddressLine3}<br/>
											&nbsp;<br/>
										</p>
										
										<table class="phonetable">
											<tr>
												<td>Phone</td>
												<td style="padding-left: 10px;">1 (866) 451-5565</td>
											</tr>
											<tr>
												<td>Fax</td>
												<td style="padding-left: 10px;">1 (408) 773-8462</td>
											</tr>
										</table>					
									</td>
									<td>
										<p class="bold">
											&nbsp;<br/>
										</p>
			
										<p>
											&nbsp;<br/>
											&nbsp;<br/>
											&nbsp;<br/>
											&nbsp;<br/>
										</p>
										
										<table class="phonetable">
											<tr>
												<td>&nbsp;</td>
												<td style="padding-left: 10px;">&nbsp;</td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td style="padding-left: 10px;">&nbsp;</td>
											</tr>
										</table>	
									</td>
								</tr>
								<tr>
									<td>
										<p>
											<span class="bold">KinekPoint Partner Support Contact</span><br/>
											1 (866) 451-5565<br/>
											<a class="email" href="mailto:support@kinek.zendesk.com">support@kinek.zendesk.com</a>
										</p>							
									</td>
									<td>
										<p>
											<span class="bold">&nbsp;</span><br/>
											&nbsp;<br/>
											&nbsp;
										</p>						
									</td>
								</tr>
							</table>
							<br>
							<div>You can also get in touch with us by using our online chat. If we are offline please leave a message and we will get back to you as soon as possible.</div>
						</div>
						
									
					</div>
					<!-- /STEPCONTAINER -->
				
				</div>
				<!-- /INTERFACE -->
				
			</div>
			<!-- /INTERFACEWRAPPER -->

		</div>
		<!-- /CONTENT -->

	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>