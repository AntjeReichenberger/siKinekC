package org.webdev.kpoint.managers;

import java.util.List;

import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.State;

public class LookupManager {

	
	public List<State> getAllStates() throws Exception {
		return new StateDao().fetch();
	}
	
}
