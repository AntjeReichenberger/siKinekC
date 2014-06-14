package org.webdev.kpoint.bl.api.mapper.request.kinekpoint;

import java.io.Serializable;


public class KinekPoint implements Serializable {

	private static final long serialVersionUID = -1526137483605399635L;

	private int depotId;
	private OperatingHours operatingHours;
	
	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}
	public int getDepotId() {
		return depotId;
	}
	
	public void setOperatingHours(OperatingHours operatingHours) {
		this.operatingHours = operatingHours;
	}
	public OperatingHours getOperatingHours() {
		return operatingHours;
	}
}