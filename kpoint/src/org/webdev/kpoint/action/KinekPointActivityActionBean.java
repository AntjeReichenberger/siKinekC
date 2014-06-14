package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.pojo.ui.KinekPointActivityContainer;
import org.webdev.kpoint.converter.CalendarConverter;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/KinekPointActivity.action")
public class KinekPointActivityActionBean extends SecureActionBean
{
	//Resolution constant
	private static final Resolution KP_ACTIVITY_PAGE = UrlManager.getKinekPointActivityForm();
	List<KinekPointActivityContainer> kpsForReport = new ArrayList<KinekPointActivityContainer>();
	@Validate(on="search", required=false)
	String city;
	@Validate(on="search", required=false, converter=CalendarConverter.class)
	Calendar startDate;
	@Validate(on="search", required=false, converter=CalendarConverter.class)
	Calendar endDate;
	int stateId;
	int depotId;

	@DefaultHandler
	public Resolution view() throws Exception 
	{
		search();
		return KP_ACTIVITY_PAGE;
	}

	public Resolution search() throws Exception 
	{
		kpsForReport = filterKinekPoints();
		return KP_ACTIVITY_PAGE;
	}
	
	public boolean getNoResults()
	{
		return kpsForReport.isEmpty();
	}

	public boolean getTooManyResults()
	{
		return kpsForReport.size() > 100000;
	}
	
	public boolean getGoodResults()
	{
		return(!getTooManyResults() && !getNoResults());
	}

	public List<KinekPointActivityContainer> filterKinekPoints() throws Exception 
	{
		List<KinekPoint> allKinekPoints = new KinekPointDao().fetchLight(true);
		List<KinekPointActivityContainer> selectedKinekPoints = new ArrayList<KinekPointActivityContainer>();
		
		for (KinekPoint depot : allKinekPoints)
		{
			boolean include = true;
			
			if (city != null && !city.isEmpty() && !depot.getCity().equalsIgnoreCase(city)) include = false;
			if (startDate != null && depot.getCreatedDate().before(startDate)) include = false;
			if (endDate != null && depot.getCreatedDate().after(endDate)) include = false;
			if (stateId != 0 && depot.getState().getStateId() != stateId) include = false;
			if (depotId != 0 && depot.getDepotId() != depotId) include = false;

			if (include)
			{
				KinekPointActivityContainer k = new KinekPointActivityContainer();
				k.setParcelsReceivedCount(this.getParcelsReceivedCount(depot));
				k.setParcelsPickedupCount(this.getParcelsPickedupCount(depot));
				k.setDepot(depot);
				selectedKinekPoints.add(k);
			}
		}

		return selectedKinekPoints;
	}

	public List<KinekPointActivityContainer> getKinekPointsForReport()
	{
		return kpsForReport;
	}

	public Calendar getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Calendar startDate)
	{
		this.startDate = startDate;
	}

	public Calendar getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Calendar endDate)
	{
		this.endDate = endDate;
	}

	public List<KinekPoint> getDepots() throws Exception 
	{
		return new KinekPointDao().fetchLight();
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public int getStateId()
	{
		return stateId;
	}

	public void setStateId(int stateId)
	{
		this.stateId = stateId;
	}

	public List<State> getStates() throws Exception 
	{
		return new StateDao().fetch();
	}

	public int getDepotId()
	{
		return depotId;
	}

	public void setDepotId(int depotId)
	{
		this.depotId = depotId;
	}
	
	public int getParcelsReceivedCount(KinekPoint depot) throws Exception 
	{
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(depot);
		int parcelsReceiced = 0;
		for (PackageReceipt receipt : packageReceipts) {
			parcelsReceiced += receipt.getPackages().size();
		}
		
		return parcelsReceiced;
	}

	public int getParcelsPickedupCount(KinekPoint depot) throws Exception 
	{
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(depot);
		int parcelsPickedup = 0;
		for (PackageReceipt receipt : packageReceipts) {
			for (Package packageObj : receipt.getPackages()) {
				if (packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusPickedUp()) {
					parcelsPickedup++;
				}
			}
		}
		
		return parcelsPickedup;
	}

	public int getKinekPointsCount()
	{
		return kpsForReport.size();
	}

	public int getParcelsPendingCount()
	{
		int tally = 0;
		for (KinekPointActivityContainer container : kpsForReport)
		{
			tally += container.getParcelsReceivedCount();
		}
		return tally;
	}

	public int getParcelsPickedupCount()
	{
		int tally = 0;
		for (KinekPointActivityContainer container : kpsForReport)
		{
			tally += container.getParcelsPickedupCount();
		}
		return tally;
	}
	
	public int getListLength() {
		return kpsForReport.size();
	}
}
