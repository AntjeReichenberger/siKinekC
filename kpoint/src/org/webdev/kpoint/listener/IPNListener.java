package org.webdev.kpoint.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.InvoiceDao;
import org.webdev.kpoint.bl.persistence.PaymentDao;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.pojo.Payment;
import org.webdev.kpoint.bl.pojo.PaymentStatus;
import org.webdev.kpoint.bl.util.ApplicationProperty;

/**
 * The IPNListener is a servlet that constantly listens for IPN notifications
 * from the paypal system.  Paypal will send notifications to the listener
 * for completed payments, declined payments, and pending payments.
 */
public class IPNListener extends HttpServlet {
	private static final KinekLogger logger = new KinekLogger(IPNListener.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IPNListener() {
    	super();   
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// read post from PayPal system and add 'cmd'
		Enumeration<?> en = request.getParameterNames();
		String str = "cmd=" + ApplicationProperty.getInstance().getProperty("invoice.paypal.receiveCommand");
		while(en.hasMoreElements()){
			String paramName = (String) en.nextElement();
			String paramValue = request.getParameter(paramName);
			str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue, "UTF-8");
		}

		// post back to PayPal system to validate
		URL u = new URL(ExternalSettingsManager.getInvoicePaypalURL());
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(str);
		pw.close();

		BufferedReader in = new BufferedReader(
		new InputStreamReader(uc.getInputStream()));
		String res = in.readLine();
		in.close();	
		
		try{
		
		//check notification validation
		if(res.equals("VERIFIED")) {
			
			//get payment information from request
			String paymentStatus = request.getParameter("payment_status");
			String txnId = request.getParameter("txn_id");
			String invoiceNumber = request.getParameter("invoice");
			Invoice invoice = new InvoiceDao().read(invoiceNumber);
			String paymentAmount = request.getParameter("mc_gross");
			String paymentCurrency = request.getParameter("mc_currency");
			
			//build payment object
			Payment	payment = new Payment();
			payment.setAmount(new BigDecimal(Double.parseDouble(paymentAmount)));
			payment.setCurrencyCode(paymentCurrency);
			payment.setInvoice(invoice);
			payment.setPayerEmail(request.getParameter("payer_email"));
			payment.setPaymentDate(new Date());
			payment.setTransactionId(txnId);		
			
			
			// check if the payment status is Declined or Pending			
			if(paymentStatus.equals("Pending"))
			{
				String log = generateLogMessage(ApplicationProperty.getInstance().getProperty("error.ipnListener.paymentPending"), request);
				/// <TODO> Log error
				/// logger.error(log);
				return;
			}
			else if(paymentStatus.equals("Declined"))
			{
				// if payment was declined, create error log and send email to invoice admin containing
				// details on the payment
				String log = generateLogMessage(ApplicationProperty.getInstance().getProperty("error.ipnListener.paymentDeclined"), request);
				/// <TODO> Log error
				/// logger.error(log);
				
				Invoice inv = new InvoiceDao().read(request.getParameter("invoice"));
				
				String subject = ApplicationProperty.getInstance().getProperty("email.ipnListener.paymentDeclined.subject");
				subject = subject.replace("attb_invoiceNumber", inv.getInvoiceNumber());
				subject = subject.replace("attb_kp_name", inv.getKinekPoint().getName());
				new EmailManager().sendInvoiceLogEmail(ExternalSettingsManager.getEmailSenderFrom(), log, subject);
				
				
				//set payment status to declined and insert record
				PaymentStatus status = new PaymentStatus();
				status.setId(ExternalSettingsManager.getPaymentStatusIdDeclined());
				payment.setStatus(status);				
				insertPaymentRecord(payment, request);

				return;
			}

			// check that transaction has not already been processed			
			Payment prevPayment = new PaymentDao().read(txnId);
			if(prevPayment != null)
			{
				String log = generateLogMessage(ApplicationProperty.getInstance().getProperty("error.ipnListener.previouslyProcessed"), request);
				/// <TODO> Log error
				/// logger.error(log);
				return;
			}
			
			// check that invoice number is valid			
			if(invoice == null)
			{
				String log = generateLogMessage(ApplicationProperty.getInstance().getProperty("error.ipnListener.nonexistingInvoice"), request);
				/// <TODO> Log error
				/// logger.error(log);
				return;
			}
			
			// check that payment amount is correct			
			if(Double.parseDouble(paymentAmount) != invoice.getAmountDue().doubleValue())
			{				
				String log = generateLogMessage(ApplicationProperty.getInstance().getProperty("error.ipnListener.incorrectAmount"), request);
				/// <TODO> Log error
				/// logger.error(log);
				return;
			}
			
			// check that payment currency is correct			
			if(!paymentCurrency.equals(invoice.getCurrency()))
			{
				String log = generateLogMessage(ApplicationProperty.getInstance().getProperty("error.ipnListener.incorrectCurrency"), request);
				/// <TODO> Log error
				/// logger.error(log);
				return;
			}

			
			//if all checks have passed, set status to approved and insert record
			PaymentStatus status = new PaymentStatus();
			status.setId(ExternalSettingsManager.getPaymentStatusIdApproved());
			payment.setStatus(status);
			
			boolean inserted = insertPaymentRecord(payment, request);
			if(!inserted) {
				return;
			}
			
			//send invoice paid email to depot
			new EmailManager().sendInvoicePaidEmail(txnId);
		}
		else {
			// if ipn is not verified, create error log and send email to invoice admin containing
			// details on the payment
			String log = generateLogMessage(ApplicationProperty.getInstance().getProperty("error.ipnListener.verificationFailed"), request);
			/// <TODO> Log error
			/// logger.error(log);
			
			Invoice inv = new InvoiceDao().read(request.getParameter("invoice"));

			String subject = ApplicationProperty.getInstance().getProperty("email.ipnListener.verificationFailed.subject");
			subject = subject.replace("attb_invoiceNumber", inv.getInvoiceNumber());			
			new EmailManager().sendInvoiceLogEmail(ExternalSettingsManager.getEmailSenderFrom(), log, subject);
		}
		
		}
		catch(Exception e){
			logger.error(new ApplicationException(e));
		}
	}
	
	/**
	 * Creates a log/email message containing information on the payment notification
	 * @param message The specific message for the log
	 * @param request The http request containing ipn information
	 * @return The log string
	 */
	private String generateLogMessage(String message, HttpServletRequest request) throws Exception{
		
		String currentDate = new String();
		String paymentDate = new String();
		try {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z");
			GregorianCalendar cur = new GregorianCalendar();			
			cur.setTime(new Date());				
			GregorianCalendar payment = new GregorianCalendar();			
			payment.setTime(df.parse(request.getParameter("payment_date")));	
			
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy h:mm:ss a z");
			currentDate = sdf.format(cur.getTime());
			paymentDate = sdf.format(payment.getTime());
		} catch (ParseException e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Message", message);
            logger.error(new ApplicationException("An error occurred creating IPN log message.", e), logProps);
		}
		
		Invoice inv = new InvoiceDao().read(request.getParameter("invoice"));
		
		String body =	"Message: " + message + "\n" +
						"Log time: " + currentDate + "\n\n" +
						"Invoice Number: " + inv.getInvoiceNumber() + "\n" +
						"Amount: " + NumberFormat.getCurrencyInstance().format(Double.parseDouble(request.getParameter("mc_gross"))) + "\n" +
						"Currency: " + request.getParameter("mc_currency") + "\n" +
						"Payer Email: " + request.getParameter("payer_email") + "\n" +
						"Payment Date: " + paymentDate + "\n" +
						"Transaction ID: " + request.getParameter("txn_id") + "\n" +
						"Status: " + request.getParameter("payment_status");
		return body;
	}
	
	/**
	 * Inserts a payment record. If insertion fails, creates a log
	 * message and sends an email to the invoice admin
	 * @param payment The payment to insert
	 * @param request The ipn request from paypal
	 * @return True if successfully inserted, false otherwise
	 */
	private boolean insertPaymentRecord(Payment payment, HttpServletRequest request) throws Exception {
		try {
			new PaymentDao().create(payment);
		}
		catch(Exception e) {
            logger.error(new ApplicationException("IPN Payment record could not be created.", e));
		
            // if payment record is not created correctly, create error log and send email to invoice admin containing
			// details on the payment
			String log = generateLogMessage(ApplicationProperty.getInstance().getProperty("error.ipnListener.recordCreationFailed"), request);
			/// <TODO> Log error
			/// logger.error(log);

			String subject = ApplicationProperty.getInstance().getProperty("email.ipnListener.recordCreationFailed.subject");
			subject = subject.replace("attb_invoiceNumber", request.getParameter("invoice"));
			new EmailManager().sendInvoiceLogEmail(ExternalSettingsManager.getEmailSenderFrom(), log, subject);
			
			return false;
		}
		
		return true;
	}
}
