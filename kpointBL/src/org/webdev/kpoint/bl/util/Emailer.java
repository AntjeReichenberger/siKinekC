package org.webdev.kpoint.bl.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.pojo.Email;

import com.sun.mail.smtp.SMTPTransport;

public class Emailer {

	private static String mailer = ExternalSettingsManager.getEmailSenderMailer();
	private static String replyTo = ExternalSettingsManager.getEmailSenderReplyTo();
	private static String host = ExternalSettingsManager.getEmailSenderHost();
	private static String pwd = ExternalSettingsManager.getEmailSenderPassword();
	private static String from = ExternalSettingsManager.getEmailSenderFrom();

	private static final KinekLogger logger = new KinekLogger(Emailer.class);
	
	public boolean sendHtmlEmail(Email email, EmailType type) {
		String to = email.getTo();
		boolean success = false;
		
		if (email.getFr() == null || email.getFr().length() <= 0) {
			email.setFr(from);
		}
		
		// Create properties for the Session
		Properties props = new Properties();

		// If using static Transport.send(),
		// need to specify the mail server here
		props.put("mail.smtp.host", host);
		props.put("mail.debug", "true");
		props.put("mail.smtp.auth", "false");

		try {
			Session session = Session.getInstance(props);
			// Get a Transport object to send e-mail
			SMTPTransport bus = (SMTPTransport) session.getTransport("smtp");

			try {
				// Instantiate a message
				Message msg = new MimeMessage(session);

				// Set message attributes
				if (email.getFr() != null)
					msg.setFrom(new InternetAddress(email.getFr()));
				else
					msg.setFrom();

				msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, true));

				if (!email.getBcc().isEmpty())
					msg.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(email.getBcc(), true));
				
				msg.setReplyTo(InternetAddress.parse(replyTo));
				msg.setSubject(email.getSubject());
				msg.setHeader("X-Mailer", mailer);
				msg.setSentDate(new Date());

				MimeMultipart mp = new MimeMultipart();

				// Set message content and send
				MimeBodyPart mbp1 = new MimeBodyPart();
				
				// Set content format
				DataHandler handler = null;
				if(type == EmailType.HTML)
					handler = new DataHandler(new HTMLDataSource(email.getBody()));
				else if(type == EmailType.PlainText)
					handler = new DataHandler(new PlainTextDataSource(email.getBody()));
				
				mbp1.setDataHandler(handler);
				mp.addBodyPart(mbp1);

				String attachment = email.getFileAttachment();
				if (attachment != null && attachment.length() > 0) {
					MimeBodyPart mbp2 = new MimeBodyPart();
					mbp2.attachFile(attachment);
					mp.addBodyPart(mbp2);
				}

				msg.setContent(mp);
				msg.saveChanges();
				int tries = 3;
				while (tries != 0) {
					try {
						bus.connect(host, email.getFr(), pwd);
						bus.sendMessage(msg, msg.getAllRecipients());
						tries = 0;
						success = true;
					} 
					catch (MessagingException mex) {
						if (bus.isConnected())
							bus.close();
						Thread.sleep(5000);
						tries--;
					} 
					catch (Exception e) {
						success = true;
						tries = 0;
					}
				}
			}
			finally {
				if (bus.isConnected())
					bus.close();
			}
		} catch (Exception e) {
			logger.error(new ApplicationException("An error occurred sending an email", e));
		}
		return success;
	}
	
	public enum EmailType
	{
		HTML,
		PlainText
	}

	/*
	 * Inner class to act as a JAF datasource to send HTML e-mail content
	 */
	static class HTMLDataSource implements DataSource {
		private String html;

		public HTMLDataSource(String htmlString) {
			html = htmlString;
		}

		// Return html string in an InputStream.
		// A new stream must be returned each time.
		public InputStream getInputStream() throws IOException {
			if (html == null)
				throw new IOException("Null HTML");
			return new ByteArrayInputStream(html.getBytes());
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("This DataHandler cannot write HTML");
		}

		public String getContentType() {
			return "text/html";
		}

		public String getName() {
			return "JAF text/html dataSource to send e-mail only";
		}
	}
	
	/*
	 * Inner class to act as a JAF datasource to send plain text e-mail content
	 */
	static class PlainTextDataSource implements DataSource {
		private String text;

		public PlainTextDataSource(String text) {
			this.text = text;
		}

		// Return text string in an InputStream.
		// A new stream must be returned each time.
		public InputStream getInputStream() throws IOException {
			if (text == null)
				throw new IOException("Null text");
			return new ByteArrayInputStream(text.getBytes());
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("This DataHandler cannot write HTML");
		}

		public String getContentType() {
			return "text/plain";
		}

		public String getName() {
			return "JAF text/plain dataSource to send e-mail only";
		}
	}

} // End of class