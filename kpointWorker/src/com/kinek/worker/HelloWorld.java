package com.kinek.worker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;

import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.ApplicationProperty;
// import org.webdev.kpoint.util.ConfigurationReader;
// import org.webdev.kpoint.util.ConfigurationReader;
// import org.webdev.kpoint.rest.Delivery.UserSession;

public class HelloWorld {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedReader br = null;
		Properties temp = new Properties();
		
		
		try {
			temp.load(new FileInputStream("C:\\development\\KinekWorkspaces\\KpointWorker\\res\\external.properties"));
			Enumeration<Object> enumeration = temp.keys();
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				String val = (String) temp.getProperty(key) ;
				// System.out.println(key + "=" + val) ;
				ApplicationProperty.getInstance().addProperty(key, temp.getProperty(key));
				
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		System.out.println ("Hello World") ;
		UserDao userDao = new UserDao(); 
		
		try {
			User user = new UserDao().authenticateAdmin("jeremy","kppass1");
			
			if (user != null) {
				
				String address1 = user.getAddress1() ;
				String email = user.getEmail() ;
				System.out.println ("ADDRESS:" + address1) ;
				System.out.println ("EMAIL:" + email) ;
			} else {
				System.out.println ("NO USER!") ;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println ("Hello World2") ;
	}

	
	
}
