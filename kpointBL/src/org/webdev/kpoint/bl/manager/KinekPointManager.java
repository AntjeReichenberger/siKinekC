package org.webdev.kpoint.bl.manager;


import java.util.ArrayList;
import java.util.List;

import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.KPPackageRateDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.PackageWeightGroup;

public class KinekPointManager {

	private static final KinekLogger logger = new KinekLogger(KinekPointManager.class);
	
	/**
	 * Retrieves the supported weight groups for the KinekPoint
	 * @param kinekPointId the unique identifier of the kinekpoint to fetch weight groups for
	 * @return an ArrayList of KPPackageRate objects
	 * @throws Exception 
	 */
	public static List<PackageWeightGroup> getWeightGroups(int kinekPointId) throws Exception{
		List<KPPackageRate> kpRates = new KPPackageRateDao().fetch(new KinekPointDao().read(kinekPointId));
		List<PackageWeightGroup> kpWeightGroups = new ArrayList<PackageWeightGroup>();
		for(KPPackageRate kpRate : kpRates) {
    		kpWeightGroups.add(kpRate.getUnifiedPackageRate().getPackageWeightGroup());
    	}
		
		return kpWeightGroups;
	}
}
