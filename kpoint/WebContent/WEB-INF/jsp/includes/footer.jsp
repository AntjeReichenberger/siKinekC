<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
</div>
<!-- /WRAPPER -->

<!-- FOOTER -->
<div id="footer" class="clearfix">
	
	<!-- COPYRIGHT -->
	<p class="copyright">&copy; Copyright 2000-2011, Kinek</p>
	<!-- /COPYRIGHT -->
	
	<!-- BOTTOMNAV -->
	<p class="bottomnav">
		<c:if test="${actionBean.activeUser == null}">
			<a href="${actionBean.baseDepotPortalUrl}/DepotHome.action" title="KinekPoint Portal">KinekPoint Portal</a> 
			<span class="separator">|</span>
		</c:if> 
		<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Privacy Policy">
			Privacy Policy
			<s:param name="action">privacy</s:param>
		</s:link>
		<c:if test="${actionBean.activeUser != null}">
			<span class="separator">|</span>
			<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="KinekPoint Agreement">
				KinekPoint Agreement
				<s:param name="action">agreement</s:param>
			</s:link>
		</c:if>
		<span class="separator">|</span> 
		<s:link beanclass="org.webdev.kpoint.action.ViewStaticPageActionBean" title="Terms of Use">
			Terms of Use
			<s:param name="action">terms</s:param>
		</s:link>
	</p>
	<!-- /BOTTOMNAV -->
	
</div>
<!-- /FOOTER -->
