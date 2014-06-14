package org.webdev.kpoint.bl.api.mapper.request;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -2268091928249244707L;
	
	private int userId = -1;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String stateCode;
	private String countryCode;
	private String zip;
	private String phone;
	private String cellPhone;
	private String email;
	private String password;
	private String agreedToTOS;
	private int referralSourceId;
	private int kinekPointId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {

		if (zip == null)
			zip = "";

		zip = zip.trim();
		zip = zip.replace(" ", "");
		zip = zip.replace("-", "");

		if (zip.length() == 7) {
			zip = zip.substring(0, 3) + zip.substring(4, 7);
		}

		if (zip.length() > 6)
			zip = zip.substring(0, 6);

		this.zip = zip;
	}
	
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		String name = "";
		if (firstName != null)
			name = name + firstName;
		if (lastName != null)
			name = name + " " + lastName;

		return name;
	}

	public String getAgreedToTOS() {
		return agreedToTOS;
	}
	
	
	public void setAgreedToTOS(String agreedToTOS) {
		this.agreedToTOS = agreedToTOS;
	}
	
	
	public int getReferralSourceId() {
		return referralSourceId;
	}
	
	public void setReferralSourceId(int referralSourceId) {
		this.referralSourceId = referralSourceId;
	}
	
	public int getKinekPointId() {
		return kinekPointId;
	}
	
	public void setKinekPointId(int kinekPointId) {
		this.kinekPointId = kinekPointId;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		
		if ( !(o instanceof User) ) return false;
		
		User packageObj = (User)o;
		return this.getUserId() == packageObj.getUserId();
	}
}
