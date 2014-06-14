<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">Kinek News</s:layout-component>
	
  	<s:layout-component name="body">

  	<style type="text/css">
  		.bold
  		{
  			font-weight: bold;
  		}
  		
  		.reglist
  		{
  			margin-left: 45px;
  		}
  		
  		.reglist li 
  		{
			list-style-type:disc;
		}
  		
  	</style>

	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="wide clearfix">
			
			<!-- STEP TITLE -->
			<h1>News and Press</h1>
			<!-- /STEP TITLE -->
			
			<s:messages />
			
			<p style="margin-top: 20px;">
				Welcome to Kinek's News Room. This page contains the latest 
				information about our products, partnerships and company. If you are a 
				reporter or analyst please e-mail <a href="mailto:PR2010@kinek.com">PR2010@kinek.com</a> to: 
			</p>
			
			<ul class="reglist">
				<li>Schedule an interview with an executive from Kinek</li>
				<li>Request screen shots for publication.</li>
				<li>Request photos of management for publication.</li>
			</ul>
			
			
			<h2 style="margin: 35px 0px 10px 0px; font-size: 16px">Press Releases</h2>
			
			<c:forEach items="${actionBean.pressItems}" var="press" varStatus="loop">
				<c:choose>
					<c:when test="${press.externalUrl != null}">
						<p><a href="${press.externalUrl}" target="_blank">${press.title}</a></p>
					</c:when>
					<c:when test="${press.externalUrl == null && press.content == null}">
						<p>${press.title}</p>
					</c:when>
					<c:otherwise>
						<p><a href="NewsItem.action?id=${press.id}">${press.title}</a></p>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
			<h2 style="margin: 35px 0px 10px 0px; font-size: 16px">Recent News</h2>
			
			<c:forEach items="${actionBean.newsItems}" var="news" varStatus="loop">
				<c:choose>
					<c:when test="${news.externalUrl != null}">
						<p><a href="${news.externalUrl}" target="_blank">${news.title}</a></p>
					</c:when>
					<c:when test="${news.externalUrl == null && news.content == null}">
						<p>${news.title}</p>
					</c:when>
					<c:otherwise>
						<p><a href="NewsItem.action?id=${news.id}">${news.title}</a></p>
					</c:otherwise>
				</c:choose>
			</c:forEach>
						
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->
	

  </s:layout-component>
</s:layout-render>
	
