package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.persistence.AffiliateDao;
import org.webdev.kpoint.bl.pojo.Affiliate;
import org.webdev.kpoint.managers.UrlManager;


@UrlBinding("/Home.action")
public class HomeActionBean extends BaseActionBean {
    
	private List<Affiliate> randomAffiliates;
	private AffiliateDao affiliateDao;
	
	@DefaultHandler
    public Resolution view() {
		
		//affiliateDao = new AffiliateDao();
		//randomAffiliates = getUniqueRandomAffiliates(affiliateDao.fetch());
				
        //return new ForwardResolution("/WEB-INF/jsp/home.jsp");
		return UrlManager.getWordPressUrl();
    }
	
	@Override
	public boolean getHideSearch() {
		return true;
	}
	
	public void setRandomAffiliates(List<Affiliate> affiliates){
		this.randomAffiliates=affiliates;
	}
	public List<Affiliate> getRandomAffiliates(){
		return randomAffiliates;
	}
	
	private List<Affiliate> getUniqueRandomAffiliates(List<Affiliate> fetchedAffiliates){
		
		List<Affiliate> affiliates=new ArrayList<Affiliate>(); 
		int affiliatesSize=fetchedAffiliates.size();		
		int index=0;
		int [] uniqueIndexes= new int[affiliatesSize];
		
		//initially assign array value -1 		
		for(int i=0; i<affiliatesSize; i++){
			uniqueIndexes[i]=-1;
		}		
		
		Random randomInt=new Random();		
		
		while(true){			
			int randomIndex=randomInt.nextInt(affiliatesSize);					
			if(!checkAvailability(uniqueIndexes,randomIndex)){
				uniqueIndexes[index]=randomIndex;
				affiliates.add(fetchedAffiliates.get(randomIndex));
				index++;				
			}
			if(index>(affiliatesSize-1))
				break;			
		}
		
		return affiliates.subList(0, 7);
	}
	
	private boolean checkAvailability(int[] intArray, int position){
		
		for(int i=0; i<intArray.length; i++){
			if(intArray[i]==position){
				return true;
			}
		}
		return false;
	}
}
