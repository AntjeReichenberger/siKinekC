package org.webdev.kpoint.bl.api.mapper.response.kinekpoint;

import java.io.Serializable;

public class KinekPointStatistics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8723220083392785349L;
	
	private int id;
	private int dailyReceivedPackages;
	private int weeklyReceivedPackages;
	private int monthlyReceivedPackages;
	private int allTimeReceivedPackages;
	private int dailyPickedUpPackages;
	private int weeklyPickedUpPackages;
	private int monthlyPickedUpPackages;
	private int allTimePickedUpPackages;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getDailyReceivedPackages() {
		return dailyReceivedPackages;
	}
	
	public void setDailyReceivedPackages(int dailyReceivedPackages) {
		this.dailyReceivedPackages = dailyReceivedPackages;
	}
	
	public int getWeeklyReceivedPackages() {
		return weeklyReceivedPackages;
	}
	
	public void setWeeklyReceivedPackages(int weeklyReceivedPackages) {
		this.weeklyReceivedPackages = weeklyReceivedPackages;
	}
	
	public int getMonthlyReceivedPackages() {
		return monthlyReceivedPackages;
	}
	
	public void setMonthlyReceivedPackages(int monthlyReceivedPackages) {
		this.monthlyReceivedPackages = monthlyReceivedPackages;
	}
	
	public int getAllTimeReceivedPackages() {
		return allTimeReceivedPackages;
	}
	
	public void setAllTimeReceivedPackages(int allTimeReceivedPackages) {
		this.allTimeReceivedPackages = allTimeReceivedPackages;
	}
	
	public int getDailyPickedUpPackages() {
		return dailyPickedUpPackages;
	}
	
	public void setDailyPickedUpPackages(int dailyPickedUpPackages) {
		this.dailyPickedUpPackages = dailyPickedUpPackages;
	}
	
	public int getWeeklyPickedUpPackages() {
		return weeklyPickedUpPackages;
	}
	
	public void setWeeklyPickedUpPackages(int weeklyPickedUpPackages) {
		this.weeklyPickedUpPackages = weeklyPickedUpPackages;
	}
	
	public int getMonthlyPickedUpPackages() {
		return monthlyPickedUpPackages;
	}
	
	public void setMonthlyPickedUpPackages(int monthlyPickedUpPackages) {
		this.monthlyPickedUpPackages = monthlyPickedUpPackages;
	}
	
	public int getAllTimePickedUpPackages() {
		return allTimePickedUpPackages;
	}
	
	public void setAllTimePickedUpPackages(int allTimePickedUpPackages) {
		this.allTimePickedUpPackages = allTimePickedUpPackages;
	}
}
