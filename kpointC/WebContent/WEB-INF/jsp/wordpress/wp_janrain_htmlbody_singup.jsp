<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ taglib prefix="janrain" uri="http://janrain4j.googlecode.com/tags" %>
<jsp:useBean id="externalSettingsManager" scope="application" class="org.webdev.kpoint.util.ExternalSettingsManagerHelper"/>
<div>
<!-- http://localhost:8080/kpointC/JanrainSignupCallback.action -->
	<janrain:signInEmbedded width="350" flags="hide_sign_in_with" tokenUrl="${externalSettingsManager.consumerPortalBaseUrl}/JanrainSignupCallback.action"/>
</div>