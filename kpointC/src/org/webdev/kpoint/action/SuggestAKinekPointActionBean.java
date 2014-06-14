package org.webdev.kpoint.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.persistence.KinekPointProspectLocationDao;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.KinekPointProspectLocation;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/SuggestAKinekPoint.action")
public class SuggestAKinekPointActionBean extends BaseActionBean {
    private KinekPointProspectLocation prospect;
    private String stateHidden;

	@DefaultHandler
    public Resolution view() throws UnsupportedEncodingException {
		return UrlManager.getSuggestAKP();
	}
	
	@ValidationMethod(on="createProspect")
	public void createProspectValidation(ValidationErrors errors) throws UnsupportedEncodingException {
    	if(prospect == null || prospect.getCity() == null || prospect.getCity() == "")
    		errors.add("prospect.city", new SimpleError("City is a required field"));
    	
    	if(prospect == null || prospect.getZip() == null || prospect.getZip() == "")
    		errors.add("prospect.zip", new SimpleError("Zip is a required field"));
    	
    	if(stateHidden == null || stateHidden == "")
    		errors.add("prospect.state", new SimpleError("State is a required field"));
    }
    
    public Resolution createProspect() throws Exception {
    	User activeUser = getActiveUser();
    	if(activeUser != null)
    		prospect.setRecommendedByUser(activeUser);
    	prospect.setState(new State(Integer.parseInt(stateHidden)));
    	
    	KinekPointProspectLocationDao kppDao = new KinekPointProspectLocationDao();
    	kppDao.create(prospect);    	
    	
    	setSuccessMessage(MessageManager.getKPProspectSuccess());
    	
		return UrlManager.getSuggestAKP();
    }
	
	public List<Country> getCountries() throws Exception {
		return new CountryDao().fetch();
	}

	public KinekPointProspectLocation getProspect() {
		return prospect;
	}

	public void setProspect(KinekPointProspectLocation prospect) {
		this.prospect = prospect;
	}
	
	public String getStateHidden() {
		return stateHidden;
	}

	public void setStateHidden(String stateHidden) {
		this.stateHidden = stateHidden;
	}
}
