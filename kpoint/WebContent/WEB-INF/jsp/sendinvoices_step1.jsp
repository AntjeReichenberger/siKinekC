<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="body">

		<div id="contentWrapper" class="clearfix">
			<div id="content" class="wide clearfix">
			
				<s:form beanclass="org.webdev.kpoint.action.SendInvoicesActionBean">
				
					<h1>Send Invoices</h1>
					
					<s:messages/>
					<s:errors/>
					
					<script type="text/javascript">

						function setHiddenNumber(button) {
							var hidden = document.getElementsByName("invoiceNumber");
							hidden.value = button.value;
						}
					
					</script>
					
					<style type="text/css">
						.detailsubmit
						{
							background-color:transparent;
							border-style:none;
							color:blue;
							cursor:pointer;
							padding:0;
							text-decoration:underline;
							width:auto;
						}
					</style>

					Create invoices for 
					<c:choose>
						<c:when test="${actionBean.monthsAvailable}">
							<s:select name="month" id="month" style="width:210px; margin-left:10px;">
								<s:option value="">Choose a month</s:option>
								<s:options-collection collection="${actionBean.months}" />
						  	</s:select>
						  	
						  	<s:submit name="generate" value="Create Invoices" class="button" />
				  		</c:when>
				  		<c:otherwise>
				  			<s:select name="month" id="month" style="width:210px; margin-left:10px;" disabled="true">
								<s:options-collection collection="${actionBean.months}" />
						  	</s:select>
						  	
						  	<s:submit name="generate" value="Create Invoices" class="buttonDisabled" disabled="true" />
				  		</c:otherwise>
				  	</c:choose>

					
					
				</s:form>
				
			</div>
		</div>

		<%@ include file="/WEB-INF/jsp/includes/footer.jsp"%>

	</s:layout-component>
</s:layout-render>