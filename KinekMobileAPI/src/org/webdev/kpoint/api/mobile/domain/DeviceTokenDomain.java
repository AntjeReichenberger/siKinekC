package org.webdev.kpoint.api.mobile.domain;

import org.webdev.kpoint.bl.persistence.DeviceTokenDao;
import org.webdev.kpoint.bl.pojo.DeviceToken;


public class DeviceTokenDomain extends Domain{
		
	/**
	 * Add the device token for the specified user.  If the token already exists for this user or another user, it will
	 * not be added.
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static DeviceToken createToken(DeviceToken token) throws Exception{
		DeviceTokenDao dao = new DeviceTokenDao();
		DeviceToken existingToken = dao.fetch(token.getToken());
		if(existingToken != null)
		{
			//token already exists, do not add it again
			return existingToken;
		}
			
		//token does not exist, create it
		int id = dao.create(token);
		token.setId(id);
		return token;
	}

	public static void removeToken(DeviceToken token) throws Exception{
		DeviceTokenDao dao = new DeviceTokenDao();
		DeviceToken fullToken = dao.fetch(token.getToken());
		if(fullToken != null)
			dao.delete(fullToken);
	}
}
