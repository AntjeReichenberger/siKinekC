package org.webdev.kpoint.managers;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.webdev.kpoint.bl.util.ApplicationProperty;

public class UrlManager {

	public static Resolution getAcceptPackageForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.acceptPackage.form"));
	}

	public static Resolution getAcceptPackageVarification() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.acceptPackage.varification"));
	}

	public static Resolution getRedirectForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.redirectPackage.form"));
	}

	public static Resolution getRedirectVarification() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.redirectPackage.varification"));
	}

	public static Resolution getKinekNumberSearchForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.tagSearch.form"));
	}

	public static Resolution getKinekNumberSearchResults() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.tagSearch.results"));
	}

	public static Resolution getPickUpForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.pickup.form"));
	}

	public static Resolution getPickUpResults() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.pickup.results"));
	}

	public static Resolution getPickUpSummary() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.pickup.summary"));
	}

	public static Resolution getManageDepotAddressForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.manageDepot.addressForm"));
	}

	public static Resolution getManageDepotFeaturesForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.manageDepot.featuresForm"));
	}

	public static Resolution getManageDepotHoursForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.manageDepot.hoursForm"));
	}
	
	public static Resolution getManageDepotPricesForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.manageDepot.pricesForm"));
	}

	public static Resolution getContactForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.contact.form"));
	}
	
	public static Resolution getAboutForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.about.form"));
	}

	public static Resolution getForgotPasswordForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.forgotPassword.form"));
	}

	public static Resolution getResetPasswordForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.resetPassword.form"));
	}

	public static Resolution getLoginForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.login.form"));
	}

	public static Resolution getTermsOfServiceForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.termsOfService.form"));
	}

	public static Resolution getFindDepotResults() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.findDepot.results"));
	}

	public static Resolution getFindDepotUsers() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.findDepot.users"));
	}

	public static Resolution getError() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.error"));
	}

	public static Resolution getUserReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.userReport"));
	}
	
	public static Resolution getAddUserForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.addUser.form"));
	}

	public static Resolution getConsumerReportForm(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.consumerReport.form"));
	}
	
	public static Resolution getWrongPortalForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.wrongPortal.form"));
	}
	
	public static Resolution getReminderEmailForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.reminderEmail.form"));
	}
	
	public static Resolution getReminderEmailPreview() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.reminderEmail.preview"));
	}
	
	public static Resolution getKinekPointChangeReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.kinekPointChangeReport"));
	}
	
	public static Resolution getPayPalRedirect() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.paypalRedirect.form"));
	}

	public static Resolution getSendInvoicesForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.sendInvoices.form"));
	}
	
	public static Resolution getSendInvoicesPreview() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.sendInvoices.preview"));
	}
	
	public static Resolution getSendInvoicesSuccess() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.sendInvoices.success"));
	}
	
	public static Resolution getInvoiceDetailsForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.invoiceDetails.form"));
	}
	
	public static Resolution getInvoiceSearchForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.invoiceSearch.form"));
	}

	public static Resolution getInvoiceSearchResults() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.invoiceSearch.results"));
	}
	
	public static Resolution getInvoiceSearchSuccess() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.invoiceSearch.success"));
	}
	
	public static Resolution getInvoiceReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.invoice.report"));
	}
	
	public static Resolution getManagePromotionsForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.managePromotions.form"));
	}

	public static Resolution getManagePromotionsEmailEdit() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.managePromotions.email.edit"));
	}

	public static Resolution getManagePromotionsEmailPreview() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.managePromotions.email.preview"));
	}

	public static Resolution getManagePromotionsSuccess() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.managePromotions.success"));
	}
	
	public static Resolution getExportPromotions() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.ExportPromotions"));
	}

	public static Resolution getPromotionRedemptionReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.PromotionRedemptionReport"));
	}
	
	public static Resolution getPromotionRegistrationReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.PromotionRegistrationReport"));
	}
	
	public static Resolution getReferralConversionReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.ReferralConversionReport"));
	}
	
	public static Resolution getOutboundMessagesReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.OutboundMessagesReport"));
	}
	
	public static Resolution getAccountStatusReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.accountStatus.report"));
	}

	public static Resolution getConsumerActivityForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.ConsumerActivityReport"));
	}

	public static Resolution getKinekPointActivityForm() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.KinekPointActivityReport"));
	}
	
	public static Resolution getParcelReport() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.parcel.report"));
	}

	public static Resolution getNewsItem() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.news.item"));
	}
	
	public static Resolution getNewsList() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.news.list"));
	}
	
	public static Resolution getDepotNewsItem() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.depotnews.item"));
	}
	
	public static Resolution getDepotNewsList() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.depotnews.list"));
	}
	
	public static Resolution getHomePage() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.home"));
	}
	
	public static Resolution getHowItWorks() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.howitworks.overview"));
	}
	
	public static Resolution getHowItWorksVideo() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.howitworks.video"));
	}

	public static Resolution getBecomeAKinekPointContact() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.becomeakinekpoint.contact"));
	}
	
	public static Resolution getDepotAbout() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.depot.about.form"));
	}
	
	public static Resolution getDepotContact() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.depot.contact.form"));
	}
	
	public static Resolution getOrganization() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.organization"));
	}
	
	public static Resolution getViewOrganization() {
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.organization.view"));
	}
	
	public static Resolution getCoupon(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.coupon"));
	}

	public static Resolution getAlwaysShowCouponConflict(){
		return new ForwardResolution(ApplicationProperty.getInstance().getProperty("url.coupon.alwaysshowconflict"));
	}
	
}
