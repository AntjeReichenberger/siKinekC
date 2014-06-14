<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/pickup.jsp">
	<s:layout-component name="contents">
	<script type="text/javascript">
		jQuery(document).ready(function()
		{
			jQuery('#selectAll').click(function()
			{
				jQuery(":input[@name='selectedCredits']").attr('checked', jQuery('#selectAll').is(':checked'));
			});
		}); 
	</script>

	<ul id="steps"> 
		<li><a href="#step1" title="ID Verification"><span>Step 1:</span> ID Verification</a></li>
		<li class="active"><a href="#step2" title="Package Summary"><span>Step 2:</span> Package Summary</a></li>
		<li><a href="#step3" title="Pickup Confirmation"><span>Step 3:</span> Pickup Confirmation</a></li>
	</ul>
	<!-- /STEPS -->
	
	<!-- STEPCONTAINER -->
	<div id="stepContainer">
		
		<!-- STEP TITLE -->
		<h1><span>2</span> Pick-Up - Select Available Credits</h1>
		<!-- /STEP TITLE -->
		
		<stripes:errors/>
		
		<!-- ACCEPTDELIVERY STEP1 -->
		<s:form beanclass="org.webdev.kpoint.action.PickupActionBean" >
			<s:hidden name="selectedPackagesIds" />
			<s:hidden name="selectedConsumer" />
			<fieldset>
				<!-- FORM CONTENT -->
				<div class="formContent">
					<table>
						<thead>
							<tr>
								<th><input type="checkbox" id="selectAll" /></th>
								<th>Promotion</th>
								<th>Expiry Date</th>
								<th>Credit Value</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${actionBean.availableConsumerCredits}" var="container" varStatus="loop">
								<tr>
									<td><s:checkbox name="consumerCreditIds" value="${container.credit.id}" /></td>
									<td>${container.credit.promotion.title}</td>
									<td><fmt:formatDate value="${container.credit.promotion.endDate}" pattern="MMMMM dd, yyyy"></fmt:formatDate></td>
									<td><fmt:formatNumber value="${container.dollarValue}" type="currency" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>						
				</div>
				<!-- /FORM CONTENT -->

				<!-- BLOCK -->
				<ol class="clearfix">																		
					<li class="rightalign">
						<s:submit name="chooseCredits" class="button" value="Apply Selected Credits" />
					</li>								
				</ol>
				<!-- /BLOCK -->
				
			</fieldset>
		</s:form>
		<!-- /ACCEPTDELIVERY STEP1 -->
		
	</div>
	<!-- /STEPCONTAINER -->
	</s:layout-component>
</s:layout-render>
