package org.webdev.kpoint.managers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.util.ApplicationProperty;

public class UrlManager {
	public static Resolution getError() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.error"));
	}
	
	public static Resolution getLoginForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.login.form"));
	}
	
	public static Resolution getSignUpForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.signup.form"));
	}
	
	public static Resolution getContactUsForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.contactUs.form"));
	}
	
	public static Resolution getAboutUsForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.aboutUs.form"));
	}
	
	public static Resolution getForgotPasswordForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.forgotPassword.form"));
	}
	
	public static Resolution getResetPasswordForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.resetPassword.form"));
	}
	
	public static Resolution getViewDepot() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.viewDepot"));
	}
	
	public static Resolution getDepotSearch() { 
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.depotSearch"));
	}

	public static Resolution getMyParcels() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.myParcels"));
	}
	
	public static Resolution getMyProfile() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.myProfile"));
	}
	
	public static Resolution getNotifications() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.notifications"));
	}

	public static Resolution getSMSContactInfoForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.smsContactInfo.form"));
	}
	
	public static Resolution getChooseDefaultKinekPointSearch() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.chooseDefaultKinekPoint.search"));
	}
	
	public static Resolution getChooseDefaultKinekPointResults() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.chooseDefaultKinekPoint.results"));
	}
	
	public static Resolution getChangePassword() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.changePassword"));
	}

	public static Resolution getKinekPointSearch() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.depotSearch"));
	}
	
	public static Resolution getSuggestAKP() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.suggestAKP"));
	}
	
	public static Resolution getADBSuggestAKP() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.suggestAKP"));
	}
	
	public static Resolution getCompleteYourProfile() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.completeYourProfile"));
	}
	
	public static Resolution getChooseAKinekPoint() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.chooseAKinekPoint"));
	}
	
	public static Resolution getChooseAKinekPointResults() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.chooseAKinekPoint.results"));
	}
	
	public static Resolution getRegistrationComplete() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.registrationComplete"));
	}
	
	public static Resolution getDashboardFAQ() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.adb.faq"));
	}
	
	public static Resolution getFAQ() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.faq"));
	}
	
	public static Resolution getMyKinekPoints(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.mykinekpoints"));
	}
	
	public static Resolution getFamilyPickUp(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.familypickup"));
	}
	
	public static Resolution getCreateSurrogate(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.surrogate"));
	}
	
	public static Resolution getViewSurrogate(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.surrogate.view"));
	}	
	
	public static Resolution getAddTrackingList(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.trakinglist.add"));
	}
	public static Resolution getViewTrackingList(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.trakinglist.view"));
	}	
	public static Resolution getKinekPointDetails(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.kinekpointdetails"));
	}
	
	public static Resolution getViewSearchTracking(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.searchtracking"));
	}	
	
	/***	
	 * @return WordPress base URL as string.
	 * 
	 * WordPress is a seperate application and outsite of the Stripes environment. 
	 * So we need base url to access WordPress resources (e.g: specific page).
	 * We cannot reach or locate seperate application's resource(s) (e.g: wordpress) using ForwardResolution.
	 * For this reason, we will just pass the base url of the wordpress as string.
	 */
	public static String getWordPressBaseUrl(){
		return ExternalSettingsManager.getWordPressURL();
	}
		
	public static Resolution getWordPressUrl(){
		return new Resolution(){
			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				// TODO Auto-generated method stub
				res.sendRedirect(ExternalSettingsManager.getWordPressURL());
			}	  		
	  	};
	}
}
