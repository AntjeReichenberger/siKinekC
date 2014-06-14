package org.webdev.kpoint.bl.util;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.MessageDao;
import org.webdev.kpoint.bl.pojo.Message;
import org.webdev.kpoint.bl.pojo.MessageMedia;
import org.webdev.kpoint.bl.pojo.MessageTrigger;

public class MessageLogger
{
	public MessageLogger()
	{
	}
	
	public void logSMSMessage(int triggerId, String recipientNumber, String messageBody) throws Exception
	{
		MessageTrigger trigger = getMessageTrigger(triggerId);
		logSMSMessage(trigger, recipientNumber, messageBody);
	}
	
	public void logSMSMessage(MessageTrigger trigger, String recipientNumber, String messageBody) throws Exception
	{
		MessageMedia media = getMessageMedia(ExternalSettingsManager.getMessageMedium_SMS());
		String recipientEmail = null;
		log(media, trigger, recipientEmail, recipientNumber, messageBody);
	}
	
	public void logEmailMessage(int triggerId, String recipientEmail, String messageBody) throws Exception
	{
		MessageTrigger trigger = getMessageTrigger(triggerId);
		logEmailMessage(trigger, recipientEmail, messageBody);
	}
	
	public void logEmailMessage(MessageTrigger trigger, String recipientEmail, String messageBody) throws Exception
	{
		MessageMedia media = getMessageMedia(ExternalSettingsManager.getMessageMedium_Email());
		String recipientCell = null;
		log(media, trigger, recipientEmail, recipientCell, messageBody);
	}
	
	private void log(MessageMedia media, MessageTrigger trigger, String recipientEmail, String recipientCell, String messageBody) throws Exception
	{
		Message message = new Message();
		message.setMedium(media);
		message.setTrigger(trigger);
		message.setRecipientEmail(recipientEmail);
		message.setRecipientCell(recipientCell);
		message.setContents(messageBody);

		new MessageDao().create(message);
	}
	
	private MessageMedia getMessageMedia(int mediaId)
	{
		MessageMedia media = new MessageMedia();
		media.setId(mediaId);
		return media;
	}
	
	public MessageTrigger getMessageTrigger(int triggerId)
	{
		MessageTrigger trigger = new MessageTrigger();
		trigger.setId(triggerId);
		return trigger;
	}
}
