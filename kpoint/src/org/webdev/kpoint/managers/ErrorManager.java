package org.webdev.kpoint.managers;

import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.util.ApplicationProperty;

public class ErrorManager {
	
	// PickupActionBean
	
	/**
	 * Returns the error message for no search results from a kinek number search 
	 */
	public static ValidationError getPickupNoKinekNumberResults() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.pickup.search.notagresults"));
	}

	/**
	 * Returns the error message for no search results from a contact search 
	 */
	public static ValidationError getPickupNoContactResults() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.pickup.search.nocontactresults"));
	}
	
	
	/**
	 * Returns the error message when a user does not select any packages to pickup 
	 */
	public static ValidationError getPickupSelectAtLeastOne() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.pickup.search.atleastone"));
	}
	
	/**
	 * Returns the error message when a user selects packages for pickup from two or more different kinek numbers
	 */
	public static ValidationError getPickupSelectAtMostOneKinekNumber() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.pickup.select.onetagnumber"));
	}
	
	// ForgotPasswordActionBean
	
	public static ValidationError getForgotPasswordNoUserFound() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.forgotPassword.noUserFound"));
	}
	
	// ResetpasswordActionBean
	
	public static ValidationError getResetPasswordInvalidUserId() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.resetPassword.invalidUserId"));
	}
	
	public static ValidationError getResetPasswordPasswordMismatch() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.resetPassword.passwordMismatch"));
	}
	
	// KinekNumberSearchActionBean
	
	public static ValidationError getKinekNumberSearchNoUserSelected() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.tagSearch.noUserSelected"));
	}
	
	// Manage Depot
	
	public static ValidationError getManageDepotNoCreditCardsSelected() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.manageDepot.noCreditCardsSelected"));
	}
	
	// ReminderEmailActionBean
	
	public static ValidationError getReminderEmailNoPackages() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.reminderEmail.noPackages"));
	}
	
	public static ValidationError getCalendarConverterInvalidFormat() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.calendarConverter.invalidFormat"));
	}
	
	// InvoiceSearchActionBean
	
	public static ValidationError getInvoiceSearchNoParamtersSelected() {
		return new SimpleError(ApplicationProperty.getInstance().getProperty("error.invoiceSearch.noParamatersSelected"));
	}
}
