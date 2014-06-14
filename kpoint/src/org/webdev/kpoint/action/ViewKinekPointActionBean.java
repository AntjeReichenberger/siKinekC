package org.webdev.kpoint.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.persistence.KPExtendedStorageRateDao;
import org.webdev.kpoint.bl.persistence.KPPackageRateDao;
import org.webdev.kpoint.bl.persistence.KPSkidRateDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.UnifiedExtendedStorageRateDao;
import org.webdev.kpoint.bl.persistence.UnifiedPackageRateDao;
import org.webdev.kpoint.bl.persistence.UnifiedSkidRateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KPExtendedStorageRate;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.KPSkidRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.UnifiedExtendedStorageRate;
import org.webdev.kpoint.bl.pojo.UnifiedPackageRate;
import org.webdev.kpoint.bl.pojo.UnifiedSkidRate;
import org.webdev.kpoint.bl.pojo.User;

@UrlBinding("/ViewDepot.action")
public class ViewKinekPointActionBean extends SecureActionBean {
	
    private KinekPoint depot = new KinekPoint();
    private int depotId;
    private int selectedDepotId;
    private List<KinekPoint> depots = null;
    
    private boolean hasExtendedRates = false;
    
    private List<UnifiedPackageRate> unifiedRates = new ArrayList<UnifiedPackageRate>();
	private List<KPPackageRate> kpPackageRates = new ArrayList<KPPackageRate>();
	
    private List<UnifiedExtendedStorageRate> unifiedExtendedStorageRates = new ArrayList<UnifiedExtendedStorageRate>();
	private List<KPExtendedStorageRate> kpExtendedStorageRates = new ArrayList<KPExtendedStorageRate>();
	
	@SuppressWarnings("unused")
	private UnifiedSkidRate unifiedSkidRate;
	private KPSkidRate kpSkidRate;
    
	@DefaultHandler
    public Resolution view() throws Exception {
		if (depotId > 0 && getActiveUser().getAdminAccessCheck()) {
			depot = new KinekPointDao().read(depotId);			
		}
		else if (getActiveUser().getDepotAdminAccessCheck()) {
			List<KinekPoint> kpList = new UserDao().fetchUserKinekPoints(getActiveUser().getUserId());
			if(kpList.size() > 0){
				depot = kpList.get(0);
			}
			depotId = depot.getDepotId();
			selectedDepotId = depotId;
		}
		else {
			depot = new KinekPointDao().read(getActiveUser().getKinekPoint().getDepotId());
			depotId = depot.getDepotId();
			selectedDepotId = depotId;
		}		
		
		UnifiedPackageRateDao unifiedPackageRateDao = new UnifiedPackageRateDao();		
		unifiedRates = unifiedPackageRateDao.fetch();
		
		UnifiedExtendedStorageRateDao unifiedExtendedStorageRateDao = new UnifiedExtendedStorageRateDao();		
		unifiedExtendedStorageRates = unifiedExtendedStorageRateDao.fetch();
		
		UnifiedSkidRateDao unifiedSkidRateDao = new UnifiedSkidRateDao();		
		unifiedSkidRate = unifiedSkidRateDao.read(1);
		
		List<KPPackageRate> kpPackageRatesList = new ArrayList<KPPackageRate>(depot.getKpPackageRates());
		
		int j = 0;
		for(int i = 0; i < unifiedRates.size() && j < kpPackageRatesList.size();i++){
			if(kpPackageRatesList.get(j).getUnifiedPackageRate().getId() == i+1){
				kpPackageRates.add(kpPackageRatesList.get(j));
				j++;
			}
			else{
				kpPackageRates.add(null);
			}
		}
		depot.filterExtendedRates();
		List<KPExtendedStorageRate> kpExtendedStorageList = new ArrayList<KPExtendedStorageRate>(depot.getKpExtendedStorageRates());
		
	
		j = 0;
		for(int i = 0; i < unifiedExtendedStorageRates.size() && j < kpExtendedStorageList.size();i++){
			if(kpExtendedStorageList.get(j).getUnifiedExtendedStorageRate().getId() == i+1){
				kpExtendedStorageRates.add(kpExtendedStorageList.get(j));
				if(kpExtendedStorageList.get(j).getActualFee().compareTo(BigDecimal.ZERO) != 0){
					hasExtendedRates = true;
				}
				j++;
			}
			else{
				kpExtendedStorageRates.add(null);
			}
		}
		List<KPSkidRate> kpSkidRates = new ArrayList<KPSkidRate>(depot.getKpSkidRate());
		if(kpSkidRates.size() > 0)
			kpSkidRate = kpSkidRates.get(0);
		
        return new ForwardResolution("/WEB-INF/jsp/viewdepot.jsp");
    }
	
	public List<KPExtendedStorageRate> getKpExtendedStorageRates() {
		return kpExtendedStorageRates;
	}

	public void setKpExtendedStorageRates(
			List<KPExtendedStorageRate> kpExtendedStorageRates) {
		this.kpExtendedStorageRates = kpExtendedStorageRates;
	}

	public KPSkidRate getKpSkidRate() {
		return kpSkidRate;
	}

	public void setKpSkidRate(KPSkidRate kpSkidRate) {
		this.kpSkidRate = kpSkidRate;
	}

	/**
	 * Forward the user to the Edit Depot page for the depot they are currently viewing
	 * @return
	 */
	public Resolution editDepot() {
		RedirectResolution resolution = new RedirectResolution(ManageDepotActionBean.class);
		resolution.addParameter("action", ManageDepotActionBean.Action.Edit);
		if(selectedDepotId == 0 || selectedDepotId == depotId){
			resolution.addParameter("depotId", depotId);
		}else{
			resolution.addParameter("depotId", selectedDepotId);
		}
		return resolution;
	}
	
	public KinekPoint getDepot() {
		return depot;
	}

	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}
	
	public List<KinekPoint> getDepots() throws Exception {
		if(depots == null){
			UserDao userDao = new UserDao();
			int userId = getActiveUser().getUserId();
			depots = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(userId)); 
			
		}
		return depots;
	}
	
	public void setDepots(List<KinekPoint> depots){
		this.depots = depots;
	}
	
	public void setSelectedDepotId(int selectedDepotId){
		this.selectedDepotId = selectedDepotId;
	}
	
	public int getSelectedDepotId(){
		return selectedDepotId;
	}
	
	public Resolution getSelectedDepotDetails() throws Exception {
		depot = new KinekPointDao().read(selectedDepotId);
		return new ForwardResolution("/WEB-INF/jsp/viewdepot.jsp");
	}
	
	public List<KPPackageRate> getKpPackageRates() {
		return kpPackageRates;
	}

	public void setKpPackageRates(List<KPPackageRate> kpPackageRates) {
		this.kpPackageRates = kpPackageRates;
	}

	public List<UnifiedPackageRate> getUnifiedRates() throws Exception {
		UnifiedPackageRateDao unifiedPackageRateDao = new UnifiedPackageRateDao();		
		return unifiedPackageRateDao.fetch();
	}
	
	public List<UnifiedExtendedStorageRate> getUnifiedExtendedStorageRates() {
		return unifiedExtendedStorageRates;
	}

	public void setHasExtendedRates(boolean hasExtendedRates) {
		this.hasExtendedRates = hasExtendedRates;
	}

	public boolean isHasExtendedRates() {
		return hasExtendedRates;
	}

}
