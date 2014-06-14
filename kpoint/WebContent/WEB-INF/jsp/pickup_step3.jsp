<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<s:layout-render name="/WEB-INF/jsp/pickup.jsp">
	<s:layout-component name="contents">
		<ul id="steps">
			<li><a href="#step1" title="ID Verification"><span>Step 1:</span> ID Verification</a></li>
			<li><a href="#step2" title="Package Summary"><span>Step 2:</span> Package Summary</a></li>
			<li class="active"><a href="#step3" title="Pickup Confirmation"><span>Step 3:</span> Pickup Confirmation</a></li>
		</ul>
		
		<div id="stepContainer">
			
			<h1><span>3</span> Pick-Up - Confirmation</h1>
			
			<stripes:errors/>
						
			<s:form beanclass="org.webdev.kpoint.action.PickupActionBean" >
				
				<fieldset>
					<!-- FORM CONTENT -->
					<div style="font-size:13px;">The package(s) have been successfully received by: ${actionBean.firstName} ${actionBean.lastName}, Kinek#: ${actionBean.kinekNumber}</div>
					<table style="width:650px" class="superplain">
						<tr>
							<td style="width:105px">Confirmation #:</td>
							<td>${actionBean.transactionId}</td>
						</tr>
						<tr>
							<td style="width:105px">Pick-up Date:</td>
							<td><%
									String DATE_FORMAT = "MMMM dd,yyyy";
						    		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
						    		String receivedDateStr=sdf.format(new Date());	
								%><%=receivedDateStr%>
							</td>
						</tr>
						<tr>
						 	<td>Total package(s):</td>
						 	<td>${actionBean.totalPackage}</td>
						</tr>
					</table>
				</fieldset>
			</s:form>
			
		</div>
	</s:layout-component>
</s:layout-render>
