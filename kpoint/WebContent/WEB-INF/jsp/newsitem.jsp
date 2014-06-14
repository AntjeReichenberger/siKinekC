<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Kinek News</s:layout-component>
	
  	<s:layout-component name="body">
  
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			<s:form beanclass="org.webdev.kpoint.action.depot.NewsItemActionBean">
				<s:hidden name="id" />
				<p>${actionBean.item.content}</p>
			</s:form>
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->	

  </s:layout-component>
</s:layout-render>
	
