package org.webdev.kpoint.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.converter.ClassConverter;

@UrlBinding("/SaveContact.action")
public class SaveContactActionBean extends AccountDashboardActionBean {
	private static final String ASCII_NEWLINE = "\r\n";
	private static final Resolution FORM_PAGE = new ForwardResolution("/WEB-INF/jsp/adb_savecontact_form.jsp");
	//private static final Resolution DONE_PAGE = new ForwardResolution("/WEB-INF/jsp/adb_savecontact_done.jsp");
	private User user;

	@DontValidate
	public Resolution cancel() {
		if (referrer != null)
			return new RedirectResolution(referrer);
		return defaultResolution();
	}
	
	@DefaultHandler
	public Resolution view() {
		if (getActiveUser().getDepot().getDepotId() == 1) {
			return defaultResolution();
		}
		
		return FORM_PAGE;
	}
			
	public Resolution vCardDownload() {
		String mimeBody = getDepotVCard(getActiveUser().getDepot());
		
		return new StreamingResolution("text/x-vcard", mimeBody).setFilename("contact.vcf");
	}
	
	private String getDepotVCard(KinekPoint depot) {
		User activeuser = getActiveUser();
		
		StringBuilder b = new StringBuilder();
		b.append("BEGIN:VCARD");
		b.append(ASCII_NEWLINE);
		b.append("VERSION:3.0");
		b.append(ASCII_NEWLINE);
		b.append("CLASS:PUBLIC");
		b.append(ASCII_NEWLINE);
		appendLine(b, "N:", activeuser.getLastName() + ":" + activeuser.getFirstName());
		appendLine(b, "FN:", activeuser.getFullName());
		appendLine(b, "NOTE:", "Kinek #: " + activeuser.getKinekNumber());
		appendLine(b, "ORG:", depot.getName());
		b.append("ADR;TYPE=WORK:;;");
		b.append(depot.getAddress1());
		b.append(", ");
		b.append(getActiveUser().getKinekNumber());
		b.append(" C/O ");
		b.append(depot.getName());
		b.append(";");
		b.append(depot.getCity());
		b.append(";");
		b.append(depot.getState().getName());
		b.append(";");
		b.append(depot.getZip());
		b.append(";");
		b.append(depot.getState().getCountry().getName());
		b.append(ASCII_NEWLINE);
		appendLine(b, "TEL;TYPE=work,voice:", depot.getPhone());
		b.append("END:VCARD");
		return b.toString();
	}
	
	private void appendLine(StringBuilder b, String label, String value) {
		b.append(label);
		b.append(value);
		b.append(ASCII_NEWLINE);
	}
	
	
	

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	
	/**
	 * Used to determine where to return to if an action is taken that leads you from this page
	 * @return This class name
	 */
	/*public String getReferrer() {
		return this.getClass().getName();
	}*/
	
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
