package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Surrogate.action")
public class ManageSurrogateActionBean extends AccountDashboardActionBean {

	private Action action;
	public static enum Action { View, Edit, Create, Delete };
	
	private String pdf1583Form;
	private String uploadKPUserId1;
	private String uploadKPUserId2;
	private String uploadKPSurrogateId1;
	private String uploadKPSurrogateId2;
	private String uploadSurrogatePicture;
	
	private String firstName;
	private String lastName;
	private String streetAddress;
	private int stateId;
	private int countryId;
	private String zip;
	private String email;
	
	private List<State> states;
	private List<Country> countries;

	@DefaultHandler
	public Resolution view() throws Exception {
		if(action == Action.Create){			
			createSurrogate();
		}else if(action == Action.Edit){
			
		}else if(action == Action.View || action == Action.Delete){		
			//fetch list of Surrogates
			return UrlManager.getViewSurrogate();
		}
		
		//fetch country.
		CountryDao countryDao = new CountryDao();		
		countries = countryDao.fetch();
		return UrlManager.getCreateSurrogate();
	}
	
	
	public Resolution gotoCreateSurrogate(){				
		RedirectResolution resolution = new RedirectResolution(ManageSurrogateActionBean.class);
		resolution.addParameter("action", "Create");
		return resolution;						
	}
	
	public Resolution createSurrogate(){
		RedirectResolution resolution = new RedirectResolution(ManageSurrogateActionBean.class);
		resolution.addParameter("action", "view");
		return resolution;		
	} 
	
	//getter//setter
	public void setPdf1583Form(String pdf1583Form){
		this.pdf1583Form = pdf1583Form;
	}
	public String getPdf1583Form(){
		
		if(pdf1583Form == null){
			pdf1583Form = ApplicationProperty.getInstance().getProperty("surrogate.pdf.path.1583form");
		}
		
		return pdf1583Form;
	}

	public String getUploadKPUserId1() {
		return uploadKPUserId1;
	}


	public void setUploadKPUserId1(String uploadKPUserId1) {
		this.uploadKPUserId1 = uploadKPUserId1;
	}


	public String getUploadKPUserId2() {
		return uploadKPUserId2;
	}


	public void setUploadKPUserId2(String uploadKPUserId2) {
		this.uploadKPUserId2 = uploadKPUserId2;
	}


	public String getUploadKPSurrogateId1() {
		return uploadKPSurrogateId1;
	}


	public void setUploadKPSurrogateId1(String uploadKPSurrogateId1) {
		this.uploadKPSurrogateId1 = uploadKPSurrogateId1;
	}


	public String getUploadKPSurrogateId2() {
		return uploadKPSurrogateId2;
	}


	public void setUploadKPSurrogateId2(String uploadKPSurrogateId2) {
		this.uploadKPSurrogateId2 = uploadKPSurrogateId2;
	}

	public String getUploadSurrogatePicture() {
		return uploadSurrogatePicture;
	}


	public void setUploadSurrogatePicture(String uploadSurrogatePicture) {
		this.uploadSurrogatePicture = uploadSurrogatePicture;
	}

	
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getStreetAddress() {
		return streetAddress;
	}


	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}


	public int getStateId() {
		return stateId;
	}


	public void setStateId(int stateId) {
		this.stateId = stateId;
	}


	public int getCountryId() {
		return countryId;
	}


	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}


	public String getZip() {
		return zip;
	}


	public void setZip(String zip) {
		this.zip = zip;
	}


	public String getEmail() {
		return email;
	}


	public List<State> getStates() {
		
		if(states == null){
			states = new ArrayList<State>();
		}
		
		return states;
	}


	public void setStates(List<State> states) {
		this.states = states;
	}

	public Resolution getStatesOptionElements() throws Exception {
		
		StringBuilder optionElements = new StringBuilder();
		
		CountryDao countryDao = new CountryDao();
		Country country = countryDao.read(countryId); 
		StateDao stateDao = new StateDao();			
		states = stateDao.fetch(country);
		
		if(states != null){
		
			for(State state:states){
				String option = "<option name="+state.getName()+" value="+state.getStateId()+" />";
				optionElements.append(option);
			}
		}
		return new StreamingResolution("text",optionElements.toString());
	}

	public List<Country> getCountries() {
		return countries;
	}


	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setAction(Action action){
		this.action = action;
	}
	public Action getAction(){
		return action;
	}
}
