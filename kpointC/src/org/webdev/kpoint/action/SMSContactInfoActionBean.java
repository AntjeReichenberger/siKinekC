package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import org.webdev.kpoint.converter.ClassConverter;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.manager.SMSManager;
import org.webdev.kpoint.bl.persistence.MessageDao;
import org.webdev.kpoint.bl.pojo.Message;
import org.webdev.kpoint.bl.pojo.MessageMedia;
import org.webdev.kpoint.bl.pojo.MessageTrigger;

@UrlBinding("/SMSContactInfo.action")
public class SMSContactInfoActionBean extends AccountDashboardActionBean
{

	boolean sendToSelf;
	String smsBodyPreview;
	@Validate(required=true, on="sendContactSMS")
	String mobilePhoneNumber;

	/**
	 * Default View
	 * @return
	 */
	@DefaultHandler @DontValidate
	public Resolution view()
	{
		if (getActiveUser().getKinekPoint().getDepotId() == 1) {
			if (referrer != null)
				return new RedirectResolution(referrer);
			return defaultResolution();
		}
		
		return UrlManager.getSMSContactInfoForm();
	}

	/**
	 * Send SMS Resolution
	 * @return
	 */
	public Resolution sendContactSMS() throws Exception
	{
		String message = getSMSFormattedContactInfo();
		sendSMS(mobilePhoneNumber, message);
		
		if (mobilePhoneNumber != null) this.setSuccessMessage(new SimpleMessage("Successfully sent text message to " + mobilePhoneNumber));
		
		return UrlManager.getSMSContactInfoForm();
	}
	
	private String getSMSFormattedContactInfo()
	{
		return getFormattedContactInfo(", ");
	}
	
	private String getFormattedContactInfo(String newLineCharacter)
	{
		User activeUser = getActiveUser();
		String contactInfo = activeUser.getFullName() + newLineCharacter
			+ activeUser.getKinekPoint().getAddress1() + ", #" + activeUser.getKinekNumber() + newLineCharacter	
			+ activeUser.getKinekPoint().getName() + newLineCharacter
			+ activeUser.getKinekPoint().getCity() + ", " + activeUser.getKinekPoint().getState().getName() + newLineCharacter
			+ activeUser.getKinekPoint().getZip();
		return contactInfo;
	}
	
	private void sendSMS(String number, String message) throws Exception
	{
		// Create supporting Media and Trigger for the Message
		MessageMedia contactinfoMedium = new MessageMedia();
		contactinfoMedium.setId(ExternalSettingsManager.getMessageMedium_SMS());
		
		MessageTrigger contactinfoTrigger = new MessageTrigger();
		contactinfoTrigger.setId(ExternalSettingsManager.getMessageTrigger_contactinfo());
		
		SMSManager.sendSMS(number, message);		
		
		// Create a Message 
		Message contactinfoMessageSent = new Message();
		contactinfoMessageSent.setMedium(contactinfoMedium);
		contactinfoMessageSent.setTrigger(contactinfoTrigger);
		contactinfoMessageSent.setRecipientCell(number);
		contactinfoMessageSent.setContents(message);
		
		// Save the new message to the DB
		new MessageDao().create(contactinfoMessageSent);
	}
	
	public String getHtmlFormattedContactInfo()
	{
		return getFormattedContactInfo("<br />");
	}
	
	@DontValidate
	public Resolution returnToMyParcels() {
		return new RedirectResolution(MyParcelsActionBean.class);
	}

	public Boolean getSendToSelf()
	{
		return sendToSelf;
	}

	public void setSendToSelf(Boolean sendToSelf)
	{
		this.sendToSelf = sendToSelf;
	}

	public String getMobilePhoneNumber()
	{
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber)
	{
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	
	@Validate(converter=ClassConverter.class)
	private Class<? extends AccountDashboardActionBean> referrer;

	public Class<? extends AccountDashboardActionBean> getReferrer()
	{
		return referrer;
	}

	public void setReferrer(Class<? extends AccountDashboardActionBean> referrer)
	{
		this.referrer = referrer;
	}
}
