<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/jsp/layout.jsp">
	<s:layout-component name="pageTitle">How Kinek Works</s:layout-component>

  	<s:layout-component name="body">
  	<link rel="stylesheet" href="resource/css/pages/howitworks.css" type="text/css" media="screen,projection" />
 
	<script type="text/javascript">
	// <![CDATA[
		jQuery(document).ready(function() {
			jQuery('#nav li:eq(1)').addClass('active');
		});
	// ]]>
	
		jQuery.noConflict();

		jQuery(document).ready(function(){
            theme: 'dark_rounded';
        });
	</script>
	
	<!-- CONTENTWRAPPER -->
	<div id="contentWrapper">
	
		<!-- CONTENT -->
		<div id="content" class="clearfix">
		<h1>How It Works</h1>
		<p class="how">Using Kinek is easy! Just follow the 3 steps below or watch the How To video.</p>
		
		<table id="howwrapper">
		<tr>
		<td>
			<div id="howbox">
				<div id="howboxtop">
					&nbsp;
				</div>
				
				<div id="howboxcontent">
					<div id="howpoint1">
						<p class="stepnumber">Step One</p>
						<p class="stepinstructions">Sign up for a Kinek account to get your Kinek #.  &nbsp;<s:link beanclass="org.webdev.kpoint.action.SignupActionBean" id="signup" title="Sign Up">Click here to sign up now</s:link>.</p>
					</div>
					
					<div id="howpoint2">
						<p class="stepnumber">Step Two</p>
						<p class="stepinstructions">Choose a KinekPoint that is convenient for you.</p>
					</div>
				
					<div id="howpoint3">
						<p class="stepnumber">Step Three</p>
						<p class="stepinstructions">Order online using your new Kinek shipping address! After choosing a default KinekPoint, your new shipping address will be displayed in your profile. This address includes your Kinek# which is used to notify you once your package arrives. Simply copy and paste this address into any online retailer's order form.</p>
					</div>
				</div>
				
				<div id="howboxbottom">
					&nbsp;
				</div>
			</div>
		</td>
		
		<td>
			<div id="howvideo">
				<a target="new" href="http://www.youtube.com/watch?v=5jD6J7W6VpU" title="Using Kinek is easy! Just watch the How To video."></a>
			</div>
		
		</td>
		</tr>
		</table>
		
		<br />
		<p class="how">That's it! Kinek will notify you as soon as your package arrives at your KinekPoint. You can then drop by and pick it up at a time of your convenience. <s:link beanclass="org.webdev.kpoint.action.SignupActionBean" id="signup" title="Get Started!">Click here to get started</s:link>!</p>
		
		</div>
		<!-- /CONTENT -->
	
	</div>
	<!-- /CONTENTWRAPPER -->
  

  </s:layout-component>
</s:layout-render>