package org.webdev.kpoint.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.persistence.OrganizationDao;
import org.webdev.kpoint.bl.pojo.Organization;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Organization.action")
public class ManageOrganizationActionBean extends BaseActionBean{

	public static enum Action { View, Edit, Create };
		
	private Action action;
	private int organizationId;
	@Validate(on={"createOrganization","editOrganization"}, required=true)
	private String name;
	private String description;
	private List<Organization> organizations;
	
	@DefaultHandler
	public Resolution view() throws Exception {
		if(action == Action.View){
			
			fetchOrganizations();			
			return UrlManager.getViewOrganization();
			
		}else if(action == Action.Edit){			
			readOrganization(organizationId);							
		}
		
		return UrlManager.getOrganization();
	}
	
	public void fetchOrganizations() throws Exception {		
		OrganizationDao orgDao = new OrganizationDao();
		organizations = orgDao.fetch();		
	}
	
	public void readOrganization(int organizationId) throws Exception {
		
		OrganizationDao orgDao = new OrganizationDao();
		Organization org = orgDao.read(organizationId);
		name = org.getName();
		if(org.getDescription() != null && !org.getDescription().equals("")){
			description = org.getDescription();
		}
		
	}
	
	@ValidationMethod(on={"createOrganization","editOrganization"})
	public void checkOrganizationNameAgainstDB() throws Exception {
		OrganizationDao orgDao = new OrganizationDao();
		Organization org = orgDao.read(name);
		
		//check DB's organization.name is same as submitted name	
		if(org != null && org.getName().equalsIgnoreCase(name)){
			
			//In edit mode user might change the only description but not name. Therefore, app tried to insert same name. 
			//For this reason, we need to check that if the id is same then user is in edit mode then don't display error msg, 
			//otherwise display. 
			if(org.getOrganizationId() != organizationId){
				getContext().getValidationErrors().add("name", new SimpleError("Organization is existed already."));
			}
		}		
	}
	
	public Resolution createOrganization() throws Exception {
		
		OrganizationDao orgDao = new OrganizationDao();

		Organization org = new Organization();
		org.setName(name);
		org.setDescription(description);
					
		orgDao.create(org);
		
		setSuccessMessage(MessageManager.getOrganizationCreateSuccess());
		
		return new RedirectResolution(ManageOrganizationActionBean.class);
	}
	
	
	public Resolution editOrganization() throws Exception {
		
		OrganizationDao orgDao = new OrganizationDao();
		
		Organization org = orgDao.read(organizationId);		
		org.setName(name);
		org.setDescription(description);					
		orgDao.update(org);
		
		RedirectResolution resolution = new RedirectResolution(ManageOrganizationActionBean.class);
		resolution.addParameter("action", "Edit");
		resolution.addParameter("organizationId", organizationId);
		setSuccessMessage(MessageManager.getOrganizationEditSuccess());
		
		return resolution;
	}
	
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setOrganizations(List<Organization> organizations){
		this.organizations = organizations;
	}
	public List<Organization> getOrganizations(){
		return organizations;
	}
	
	public void setAction(Action action){
		this.action = action;
	}
	public Action getAction(){
		return action;
	}
	
}
