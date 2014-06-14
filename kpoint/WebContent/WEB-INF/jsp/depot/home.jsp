<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/depot/layout.jsp">
	<s:layout-component name="pageTitle">Kinek</s:layout-component>
	
  	<s:layout-component name="body">
  	
  	<script type="text/javascript">
	// <![CDATA[
	       jQuery(document).ready(function(){
	               jQuery('#nav li:eq(0)').addClass('active');
	       });
	// ]]>
	</script>

	<!-- HOMEWRAPPER -->
	<div id="homeWrapper" class="clearfix">
	
		<!-- HOMEBOX -->
		<div id="homeBox" class="clearfix">
			
			<h1>What we do</h1>
			<p>Kinek makes it easy for online consumers to receive their packages. By establishing KinekPoints in successful businesses like yours, Kinek provides online shoppers with the ability to pick up their parcels at safe, convenient locations. The Kinek service gives customers an additional reason to visit your location - resulting in additional foot traffic and revenue.</p>
			
			<s:link beanclass="org.webdev.kpoint.action.depot.HowItWorksActionBean" title="Read More">&nbsp;</s:link>
				
		</div>
		<!-- /HOMEBOX -->
		
		<!-- DEMONSTRATION -->
		<div id="demonstration" class="clearfix">
			<img src="resource/samples/sample_homebox_demonstration.jpg" alt="Watch Overview" width="102" height="129" />
			<p>See how shopping on her favourite websites leads Jenny to your physical retail location<br />
			<s:link beanclass="org.webdev.kpoint.action.depot.HowItWorksVideoActionBean" title="Watch Overview">&nbsp;</s:link>
			</p>
		</div>
		<!-- /DEMONSTRATION -->

		<script type="text/javascript">
		// <![CDATA[
			jQuery(document).ready(function(){				
				jQuery('#kpLogin').validate();				
			});
		// ]]>
		</script>
		
		<!-- LOGINFORM -->
		<s:form beanclass="org.webdev.kpoint.action.LoginActionBean" name="kpLogin" id="kpLogin" method="post">
			<fieldset>
				<h3>Sign In</h3>				
				
				<ol>
					<li>
						<s:label for="username">Username:</s:label><br />
						<s:text name="username" id="username" class="{required:true}" />
					</li>
					<li>
						<s:label for="passwd">Password:</s:label><br />
						<s:password name="passwd" id="passwd" class="{required:true}" />
		  			</li>
					<li>
						<s:submit name="login" value="Sign In" class="button" style="margin-bottom: 10px;" /><br/>
						<s:link beanclass="org.webdev.kpoint.action.ForgotPasswordActionBean">Forgot your password</s:link>
					</li>
				</ol>
			</fieldset>
		</s:form>
		<!-- /LOGINFORM -->
				
		<div style="clear:both;"></div>
				
	</div>
	<!-- /HOMEWRAPPER -->

  </s:layout-component>
</s:layout-render>
	
	