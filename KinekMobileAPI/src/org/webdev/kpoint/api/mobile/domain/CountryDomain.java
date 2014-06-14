package org.webdev.kpoint.api.mobile.domain;

import java.util.List;

import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.pojo.Country;

public class CountryDomain extends Domain{
	
	public static List<Country> getCountries() throws Exception{
		CountryDao dao = new CountryDao();
		List<Country> countries = dao.fetch(); 
		return countries;
	}
	
}
