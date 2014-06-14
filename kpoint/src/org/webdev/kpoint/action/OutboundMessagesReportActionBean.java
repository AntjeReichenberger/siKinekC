package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.MessageMediaDao;
import org.webdev.kpoint.bl.pojo.MessageMedia;
import org.webdev.kpoint.bl.persistence.MessageTriggerDao;
import org.webdev.kpoint.bl.pojo.MessageTrigger;
import org.webdev.kpoint.bl.persistence.MessageDao;
import org.webdev.kpoint.bl.pojo.Message;

@UrlBinding("/OutboundMessagesReport.action")
public class OutboundMessagesReportActionBean extends SecureActionBean {
	
	// Message list
	private List<Message> messagesForReport = new ArrayList<Message>();
	
	//Filter fields
	private int messageMediaId;
	private int messageTriggerId;	
	
	@DefaultHandler
    public Resolution view() throws Exception {
		messagesForReport = new MessageDao().fetch();
        return UrlManager.getOutboundMessagesReport();
    }

    public Resolution search() throws Exception {					
		messagesForReport = new MessageDao().fetch(messageMediaId, messageTriggerId);
        return UrlManager.getOutboundMessagesReport();
    }
	
    
	public List<Message> getMessagesForReport() {
		return messagesForReport;
	}
	
	public void setMessagesForReport(List<Message> messagesForReport) {
		this.messagesForReport = messagesForReport;
	}
	
	public List<MessageMedia> getMessageMedias() throws Exception {
		return new MessageMediaDao().fetch();
	}
	
    public int getMessageMediaId() {
		return messageMediaId;
	}

	public void setMessageMediaId(int messageMediaId) {
		this.messageMediaId = messageMediaId;
	}
	
	public List<MessageTrigger> getMessageTriggers() throws Exception {
		return new MessageTriggerDao().fetch();
	}
	
    public int getMessageTriggerId() {
		return messageTriggerId;
	}

	public void setMessageTriggerId(int messageTriggerId) {
		this.messageTriggerId = messageTriggerId;
	}
	
	public int getListLength() {
		return messagesForReport.size();
	}
}
