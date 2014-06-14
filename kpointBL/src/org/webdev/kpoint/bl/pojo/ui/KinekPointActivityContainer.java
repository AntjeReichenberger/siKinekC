package org.webdev.kpoint.bl.pojo.ui;

import org.webdev.kpoint.bl.pojo.KinekPoint;

public class KinekPointActivityContainer {
	int parcelsReceivedCount;
	int parcelsPickedupCount;
	KinekPoint depot;

	public int getParcelsReceivedCount()
	{
		return parcelsReceivedCount;
	}

	public void setParcelsReceivedCount(int parcelsReceivedCount)
	{
		this.parcelsReceivedCount = parcelsReceivedCount;
	}

	public int getParcelsPickedupCount()
	{
		return parcelsPickedupCount;
	}

	public void setParcelsPickedupCount(int parcelsPickedupCount)
	{
		this.parcelsPickedupCount = parcelsPickedupCount;
	}

	public KinekPoint getDepot()
	{
		return depot;
	}

	public void setDepot(KinekPoint depot)
	{
		this.depot = depot;
	}
}
