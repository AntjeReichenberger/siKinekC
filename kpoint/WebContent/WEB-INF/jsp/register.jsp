<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<s:layout-render name="/WEB-INF/jsp/layout.jsp">
  <s:layout-component name="body">
  
  <jsp:useBean id="lookupManager" scope="page" class="org.webdev.kpoint.managers.LookupManager"/>
  
  
<s:form beanclass="org.webdev.kpoint.action.SignupActionBean">

<stripes:errors/>
<br><br>
  First Name:<br>
  <s:text name="user.firstName"/><br><br>
    Last Name:<br>
  <s:text name="user.lastName"/><br><br>
    address line 1:<br>
  <s:text name="user.address1"/><br><br>
    address line 2:<br>
  <s:text name="user.address2"/><br><br>
    city:<br>
  <s:text name="user.city"/><br><br>
  
	state: <stripes:select name="stateId">
		<stripes:option value="">Select One</stripes:option>
        <stripes:options-collection collection="${lookupManager.allStates}"
                                                    label="name" value="stateId"/>
                    </stripes:select><br><br>

  
  Zip:<br>
  <s:text name="user.zip"/><br><br>
  
  Phone:<br>
  <s:text name="user.phone"/><br><br>
    
  Email:<br>
  <s:text name="user.email"/><br><br>
  
    Kinek Number:<br>
  <s:text name="user.kinekNumber"/><br><br>
  

  Username:<br>
  <s:text name="user.username"/><br><br>

  Password:<br>
  <s:password name="user.password"/><br><br>

  Confirm password:<br>
  <s:password name="confirmPassword"/><br><br>

  <s:submit name="register" value="Register"/>
</s:form>

<p>
  If you've registered but haven't activated your account,
  <s:link beanclass="org.webdev.kpoint.action.HomeActionBean">
    click here
  </s:link>
</p>

<p>
  If your account is already active, you can proceed to
  <s:link beanclass="org.webdev.kpoint.action.HomeActionBean">
    login
  </s:link>
</p>

  </s:layout-component>
</s:layout-render>