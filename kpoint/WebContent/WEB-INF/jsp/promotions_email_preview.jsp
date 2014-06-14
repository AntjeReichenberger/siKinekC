<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/promotions.jsp">
	<s:layout-component name="contents">

		<h1>KP Email Preview</h1>

		<stripes:errors/>
		<stripes:messages/>
		
		<style type="text/css">
			
			table.bg1 
			{
				background-color: #c1c1c1;
				width: 800px;
				border: solid 1px;
				border-color: #E3E3E3;
			}
			
			table.bg1 td, table.bg1 tbody
			{
				border-style: none;
			}
			
			table.bg2 
			{
				background-color: #ffffff;
				margin: 0px;
			}
			
			td.body 
			{
				padding: 0px 20px 0px 20px;
				background-repeat: repeat-y;
				background-position: bottom center;
				background-color: #ffffff;
				font-family:"Lucida Sans Unicode","Lucida Grande",Verdana,Arial,Helvetica,sans-serif;
			}
			
			td.mainbar h1 {
		   font-family: "Lucida Sans Unicode", "Lucida Grande", Verdana, Arial, Helvetica, sans-serif;
			font-size: 16px;
			font-weight: normal;
			color: #777;
		   margin: 0 0 14px 0;
		   padding: 0;
		}
		
		td.mainbar p {
		   font-family: "Lucida Sans Unicode", "Lucida Grande", Verdana, Arial, Helvetica, sans-serif;
			font-size: 12px;
			font-weight: normal;
			color: #777;
		   margin: 0 0 14px 0;
		   padding: 0;
		}
		
		td.mainbar p a {
		   font-family: "Lucida Sans Unicode", "Lucida Grande", Verdana, Arial, Helvetica, sans-serif;
			font-size: 12px;
			font-weight: normal;
			color: #058fd3;
		}
		
		td.mainbar ul {
		   font-family: "Lucida Sans Unicode", "Lucida Grande", Verdana, Arial, Helvetica, sans-serif;
			font-size: 12px;
			font-weight: normal;
			color: #777;
		   margin: 0 0 14px 24px;
		   padding: 0;
		}
		
		#emaillogo {
			margin-bottom: 20px;
		}
		
		td.mainbar ul li a {
		   font-family: "Lucida Sans Unicode", "Lucida Grande", Verdana, Arial, Helvetica, sans-serif;
			font-size: 12px;
			font-weight: normal;
			color: #777;
		}
			
		</style>

		<s:form beanclass="org.webdev.kpoint.action.ManagePromotionsActionBean">
			<fieldset>
					<s:hidden name="depotId"/>
					<s:hidden name="promotionId"/>
					<s:hidden name="emailPreview"/>
					<s:hidden name="emailMessage"/>

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
					<li class="clearfix">
						<label>Email Body:</label><br />
						<div class="emailPreview">
							<table border="0" cellspacing="0" cellpadding="0" class="bg1">
							   <tr style="background-image: none;">
							      <td align="center">
							         
							         <table width="800" border="0" cellspacing="0" cellpadding="0" class="bg2">
							            <tr>
							               <td valign="top" class="body">
							                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
							                     <tr>
							                        <td valign="top" class="mainbar" align="left">
							                        
							                        	<img id="emaillogo" width="326" height="50" alt="Kinek Home" src="resource/images/logo_top.png"/>
							                        
							                        	<p>
															A new Kinek promotional campaign has been setup and your KinekPoint
															has been setup as a key participant.
															
															The details of the new promotion are recapped below.
														</p>
														
														<p>
															<span style="font-weight: bold">${actionBean.promotion.title}</span><br/>
															${actionBean.promotion.description}<br/><br/>
															CODE: (${actionBean.promotion.code})<br/>
															
															Start Date: <fmt:formatDate value="${actionBean.startingDate}" pattern="MMMMM dd, yyyy" /><br/>
															End Date: <fmt:formatDate value="${actionBean.endingDate}" pattern="MMMMM dd, yyyy" /><br/>
															Consumer Value: <fmt:formatNumber value="${actionBean.promotion.consumerCreditAmount }" type="currency" minFractionDigits="2" maxFractionDigits="2" /><br/>
															KinekPoint Value: <fmt:formatNumber value="${actionBean.promotion.depotCreditAmount }" type="currency" minFractionDigits="2" maxFractionDigits="2" /><br/>
															# available: ${actionBean.promotion.availabilityCount }<br/>
														</p>
														
														<p>${actionBean.emailMessage }<p>
														
														<p>Sincerely,</p>
							
														<p><strong>Kinek Staff</strong></p>
														
							                        </td>
							                     </tr>
							                  </table>
							               </td>
							            </tr>
							         </table>	         
							      </td>
							   </tr>
							</table>
						</div>
					</li>
				</ol>

				<ol class="clearfix">
					<li class="rightalign">
						<s:submit name="editEmail" value="Edit" class="button" />
						<s:submit name="sendEmail" value="Send" class="button" />
					</li>
				</ol>
				
			</fieldset>
		</s:form>

	</s:layout-component>
 </s:layout-render>
