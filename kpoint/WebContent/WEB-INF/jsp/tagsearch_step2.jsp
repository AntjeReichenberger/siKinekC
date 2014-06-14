<%@ include file="/WEB-INF/jsp/taglibs.jsp"%>
<s:layout-render name="/WEB-INF/jsp/tagsearch.jsp">
	<s:layout-component name="contents">
		<!-- STEPS -->
		<ul id="steps">
			<li><a href="KinekNumberSearch.action?via=${actionBean.via}"
				title="Find Item"><span>Step 1:</span> Find Person</a></li>
			<li class="active"><a href="#step2" title="Search Results"><span>Step
			2:</span> Search Results</a></li>
		</ul>
		<!-- /STEPS -->

		<!-- STEPCONTAINER -->
		<div id="stepContainer"><!-- STEP TITLE -->
		<h1><span>2</span>Unknown Kinek# - Search Results</h1>
		<!-- /STEP TITLE -->
		<stripes:errors />

		<!-- ACCEPTDELIVERY STEP1 -->
		<s:form beanclass="org.webdev.kpoint.action.KinekNumberSearchActionBean">
			<fieldset>
				<s:hidden name="via" />
				<s:hidden name="redirectLocation" />
				<s:hidden name="reasonId" />
				<s:hidden name="courierId" />
				<s:hidden name="customInfo" />
				<s:hidden name="courierId" />
				<s:hidden name="firstName" />
				<s:hidden name="lastName" />
				<s:hidden name="phone" />
				<s:hidden name="kinekNumber" />
				<!-- FORM CONTENT -->
				<div class="formContent">
				<table>
					<thead>
						<tr>
							<th width="15%">Recipient</th>
							<th width="15%">Kinek #</th>
							<th width="15%">Phone #</th>
							<th width="40%">Address</th>
							<th width="15%">Send Notification</th>
						</tr>
					</thead>
	
					<tbody>
						<c:forEach items="${actionBean.users}" var="kpp" varStatus="loop">
							<tr>
								<td width="15%"><strong>${kpp.firstName}
								${kpp.lastName}</strong></td>
								<td width="15%">${kpp.kinekNumber}</td>
								<td width="15%">${kpp.phone}</td>
								<td width="40%">
								<p>${kpp.address1} ${kpp.address2}, ${kpp.city},
								${kpp.state.name}, ${kpp.state.country.name}<br />
								${kpp.zip}, <a href="mailto:${kpp.email}" title="${kpp.email}">${kpp.email}</a></p>
								</td>
								<td width="15%">
									<s:checkbox class="checkbox" name="selectedUserIds" value="${kpp.userId}" />
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				</div>
				<!-- /FORM CONTENT --> <!-- BLOCK -->
				<ol class="clearfix">
					<li class="rightalign half">
						<s:submit class="buttonLink" name="view" value="Search Again" title="Search Again" />
						<span class="separator">|</span>
						<s:submit class="button" name="processResults" id="processResults" value="Continue" />
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
