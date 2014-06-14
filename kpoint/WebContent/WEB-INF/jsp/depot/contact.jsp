<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/depot/layout.jsp">
	<s:layout-component name="pageTitle">Kinek</s:layout-component>
	
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
	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
			
			<h1>Contact Us</h1>

			<div style="width: 65%; display: inline-block; float: left;">
				<table class="superplain clear">
					<tr>
						<td>
							<p class="bold">
								Kinek US
							</p>

							<p>
								181 Fremont Street<br/>
								San Francisco<br/>
								California<br/>
								94105
							</p>
							
							<table class="phonetable">
								<tr>
									<td>Phone</td>
									<td style="padding-left: 10px;">+1.408.524.4208</td>
								</tr>
								<tr>
									<td>Fax</td>
									<td style="padding-left: 10px;">+1.408.773.8462</td>
								</tr>
							</table>					
						</td>
						<td style="height: 178px;">
							<p class="bold">
								Kinek Canada
							</p>

							<p>
								91 Canterbury Street,<br/>
								Saint John<br/>
								New Brunswick<br/>
								E2L 2C7
							</p>
							
							<table class="phonetable">
								<tr>
									<td>Phone</td>
									<td style="padding-left: 10px;">+1.408.400.3712</td>
								</tr>
								<tr>
									<td>Fax</td>
									<td style="padding-left: 10px;">+1.506.694.1260</td>
								</tr>
							</table>	
						</td>
					</tr>
					<tr>
						<td>
							<p>
								<span class="bold">KinekPoint Partner Support Contact</span><br/>
								+1.408.524.4208	<br/>
								<a class="email" href="mailto:KinekPartnerSupport@kinek.com">KinekPartnerSupport@kinek.com</a>
							</p>							
						</td>
						<td>
							<p>
								<span class="bold">Kinek Consumer Support Contact</span><br/>
								+1.408.400.3712<br/>
								<a class="email" href="mailto:KinekSupport@kinek.com">KinekSupport@kinek.com</a>
							</p>						
						</td>
					</tr>
				</table>
			</div>
			<div style="width: 300px; display: inline-block; float: right;">
				<img src="resource/images/contactus.png" alt="Kinek Office" style="margin-top: 20px;" />
			</div>
					
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->

  </s:layout-component>
</s:layout-render>
	
	