package org.webdev.kpoint.action;

import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.InvoiceDao;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.bl.util.Encryption;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/Payment.action")
public class PayPalRedirectActionBean  extends BaseActionBean {
	
	private String num;
	private String cmd;
	private String encrypt;

	@DefaultHandler
	public Resolution view() throws Exception  {
		Encryption enc = new Encryption(ApplicationProperty.getInstance().getProperty("encryption.key"));
		String decryptedNum = enc.decrypt(num);
		Invoice invoice = new InvoiceDao().read(decryptedNum);
		encrypt = Encryption.getPayPalEncryptionValue(invoice);
		cmd = ApplicationProperty.getInstance().getProperty("invoice.paypal.formCommand");
		return UrlManager.getPayPalRedirect();
	}
	
	public String getNum() {
		return num;
	}
	
	public void setNum(String num) {
		this.num = num;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}
}
