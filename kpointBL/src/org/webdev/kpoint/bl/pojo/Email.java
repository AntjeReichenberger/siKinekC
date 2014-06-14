package org.webdev.kpoint.bl.pojo;

public class Email {

	private String to = "";
	private String replyTo = "";
	private String cc = "";
	private String bcc = "";
	private String fr = "";
	private String subject = "";
	private String body = "";
	private String fileAttachment = "";
	
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	
	public String getFileAttachment() {
		return fileAttachment;
	}
	public void setFileAttachment(String fileAttachment) {
		this.fileAttachment = fileAttachment;
	}
	
	public String getFr() {
		return fr;
	}
	public void setFr(String fr) {
		this.fr = fr;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}	

	public String getBcc() {
		return this.bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
		
	}
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	public String getReplyTo() {
		return replyTo;
	}
	
	
	
}
