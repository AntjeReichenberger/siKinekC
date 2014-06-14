package org.webdev.kpoint.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.User;

/**
 * Servlet implementation class TrackingUpdatesMonitor.  This is used to monitor for updates to tracking numbers that
 * are registered with the Kinek system.  When updates are available, the Kinek system will process them and notify the
 * user.
 */
public class EmailTester extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final KinekLogger logger = new KinekLogger(EmailTester.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmailTester() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(ExternalSettingsManager.getIsInDebugMode()){
			UserDao userDao = new UserDao();
			PackageReceiptDao receiptDao = new PackageReceiptDao();
			EmailManager emailManager = new EmailManager();
			try {
				//User account that the emails will be sent to
				User selectedUser = userDao.read(669);
				
				List<PackageReceipt> receipts = receiptDao.fetch(selectedUser.getUserId());
				PackageReceipt singlePackage = null; 
				for(PackageReceipt receipt:receipts){
					if(receipt.getPackages().size() == 1){
						singlePackage = receipt;
					}
				}
				if(singlePackage != null){
					emailManager.sendAcceptDeliveryEmail(singlePackage, selectedUser);
					emailManager.sendMultipleAcceptDeliveryEmail(singlePackage, selectedUser);		
				}
				
				PackageReceipt multiplePackages = null; 
				for(PackageReceipt receipt:receipts){
					if(receipt.getPackages().size() > 1){
						multiplePackages = receipt;
					}
				}
				if(multiplePackages != null){
					emailManager.sendAcceptDeliveryEmail(multiplePackages, selectedUser);
					emailManager.sendMultipleAcceptDeliveryEmail(multiplePackages, selectedUser);
				}
				emailManager.sendFinalSignupEmail(selectedUser);
				
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}
}
