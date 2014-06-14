package org.webdev.kpoint.api.mobile.domain;

import java.util.List;

import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.State;

public class StateDomain extends Domain{
	
	public static List<State> getStates() throws Exception{
		StateDao dao = new StateDao();
		List<State> states = dao.fetch(); 
		return states;
	}
	
}
