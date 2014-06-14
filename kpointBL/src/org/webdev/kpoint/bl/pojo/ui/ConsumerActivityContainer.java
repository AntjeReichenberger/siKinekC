package org.webdev.kpoint.bl.pojo.ui;

import java.util.Date;
import org.webdev.kpoint.bl.pojo.User;

public class ConsumerActivityContainer {
	int parcelsPendingCount; 
	int parcelsPickedupCount;
	Date lastPickupDate;
	User consumer;

	public int getParcelsPendingCount()
	{
		return parcelsPendingCount;
	}

	public void setParcelsPendingCount(int parcelsPendingCount)
	{
		this.parcelsPendingCount = parcelsPendingCount;
	}

	public int getParcelsPickedupCount()
	{
		return parcelsPickedupCount;
	}

	public void setParcelsPickedupCount(int parcelsPickedupCount)
	{
		this.parcelsPickedupCount = parcelsPickedupCount;
	}

	public Date getLastPickupDate()
	{
		return lastPickupDate;
	}

	public void setLastPickupDate(Date lastPickupDate)
	{
		this.lastPickupDate = lastPickupDate;
	}
	
	public User getConsumer()
	{
		return consumer;
	}

	public void setConsumer(User consumer)
	{
		this.consumer = consumer;
	}
}
