package org.webdev.kpoint.api.mobile.domain;

import java.util.List;

import org.webdev.kpoint.bl.pojo.TrainingTutorial;
import org.webdev.kpoint.bl.persistence.TrainingTutorialDao;


public class TutorialDomain extends Domain{
	
	public static List<TrainingTutorial> getTrainingTutorials() throws Exception{
		TrainingTutorialDao dao = new TrainingTutorialDao();
		List<TrainingTutorial> tutorials = dao.fetch();
		return tutorials;
	}
	
}