<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<%@ taglib prefix="janrain" uri="http://janrain4j.googlecode.com/tags" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Unmap OpenID Account</s:layout-component>
	
	<s:layout-component name="body">
  	<link rel="stylesheet" href="resource/css/pages/login.css" type="text/css" media="screen,projection" />
  	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<div id="content">

			<div id="signInHeader">
				<h1>Unmap OpenID Accounts</h1>
				<br/><br/>
			</div>

			<s:errors />
			<s:messages />
			
			<s:form name="unmap" id="unmap" action="UnmapOpenID.action">
				<s:hidden name="key"/>
			
				<div id="leftForm">
					<div class="loginTitle">
						Enter the user name and password to Unmap
					</div>
					<table class="superplain form">
						<tr>
							<td class="labelcol">
								<s:label for="username">Email </s:label>
							</td>
							<td class="valcol">
								<s:text name="username" />
							</td>	
						</tr>
						<tr>
							<td>
								<s:label for="passwd">Password </s:label>
							</td>
							<td>
								<s:password name="passwd" /><br />
							</td>	
						</tr>
						<tr>
							<td class="buttonwrapper">
								
							</td>
							<td class="buttonwrapper">
								<s:submit name="unmap" value="unmap" class="button" />
							</td>
						</tr>
					</table>
				</div>
								
			</s:form>
		</div>
	</div>
	<!-- /CONTENTWRAPPER -->
	</s:layout-component>
</s:layout-render>