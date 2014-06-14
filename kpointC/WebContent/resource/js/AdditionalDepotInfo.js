AdditionalDepotInfo=function(){
	
	var creditCards="";
	var dutyAndTax="";
	var email="";
	var extraInfo="";
	var features="";
	var languages="";
	var name="";
	var sundayStart="";
	var sundayEnd="";
	var closedSunday=false;
	var mondayStart="";
	var mondayEnd="";
	var closedMonday=false;
	var tuesdayStart="";
	var tuesdayEnd="";
	var closedTuesday=false;
	var wednesdayStart="";
	var wednesdayEnd="";
	var closedWednesday=false;
	var thursdayStart="";
	var thursdayEnd="";
	var closedThursday=false;
	var fridayStart="";
	var fridayEnd="";
	var closedFriday=false;
	var saturdayStart="";
	var saturdayEnd="";
	var closedSaturday=false;
	var hoursInfo="";
	var payMethods="";
	var phone="";
	var receivingFee="";
	var sizeAllowance="";
	var state="";
	var zip="";
	var kpPackageRates="";
	var kpExtendedStorageRates="";
	var kpSkidRate="";
	
	this.setCreditCards=function(cards){
		creditCards=cards
	}
	this.getCreditCards=function(){
		return creditCards;
	}
	this.setDutyAndTax=function(dTax){
		dutyAndTax=dTax
	}
	this.getDutyAndTax=function(){
		return dutyAndTax;
	}	
	this.setEmail=function(eml){
		email=eml;
	}
	this.getEmail=function(){
		return email;
	}		
	this.setExtraInfo=function(xtraInfo){
		extraInfo=xtraInfo;
	}
	this.getExtraInfo=function(){
		return extraInfo;
	}
	this.setFeatures=function(f){
		features=f;
	}
	this.getFeatures=function(){
		return features;
	}	
	this.setLanguages=function(langs){
		languages=langs;
	}
	this.getLanguages=function(){
		return languages;
	}	
	this.setName=function(n){
		name=n;
	}
	this.getName=function(){
		return name;
	}	
	this.setSundayStart=function(start){
		sundayStart=start;
	}
	this.getSundayStart=function(){
		return sundayStart;
	}	
	this.setSundayEnd=function(end){
		sundayEnd=end;
	}	
	this.getSundayEnd=function(){
		return sundayEnd;
	}	
	this.setSundayClosed=function(closed){
		closedSunday=closed;
	}	
	this.getSundayClosed=function(){
		return closedSunday;
	}	
	this.setMondayStart=function(start){
		mondayStart=start;
	}
	this.getMondayStart=function(){
		return mondayStart;
	}	
	this.setMondayEnd=function(end){
		mondayEnd=end;
	}	
	this.getMondayEnd=function(){
		return mondayEnd;
	}	
	this.setMondayClosed=function(closed){
		closedMonday=closed;
	}	
	this.getMondayClosed=function(){
		return closedMonday;
	}	
	
	this.setTuesdayStart=function(t){
		tuesdayStart=t;
	}		
	this.getTuesdayStart=function(){
		return tuesdayStart;
	}	
	this.setTuesdayEnd=function(end){
		tuesdayEnd=end;
	}	
	this.getTuesdayEnd=function(){
		return tuesdayEnd;
	}	
	this.setTuesdayClosed=function(closed){
		closedTuesday=closed;
	}	
	this.getTuesdayClosed=function(){
		return closedTuesday;
	}
	this.setWednesdayStart=function(wed){
		wednesdayStart=wed;
	}	
	this.getWednesdayStart=function(){
		return wednesdayStart;
	}	
	this.setWednesdayEnd=function(end){
		wednesdayEnd=end;
	}	
	this.getWednesdayEnd=function(){
		return wednesdayEnd;
	}	
	this.setWednesdayClosed=function(closed){
		closedWednesday=closed;
	}	
	this.getWednesdayClosed=function(){
		return closedWednesday;
	}
	this.setThursdayStart=function(start){
		thursdayStart=start;
	}	
	this.getThursdayStart=function(){
		return thursdayStart;
	}	
	this.setThursdayEnd=function(end){
		thursdayEnd=end;
	}	
	this.getThursdayEnd=function(){
		return thursdayEnd;
	}	
	this.setThursdayClosed=function(closed){
		closedThursday=closed;
	}	
	this.getThursdayClosed=function(){
		return closedThursday;
	}	
	this.setFridayStart=function(f){
		fridayStart=f;
	}		
	this.getFridayStart=function(){
		return fridayStart;
	}	
	this.setFridayEnd=function(end){
		fridayEnd=end;
	}	
	this.getFridayEnd=function(){
		return fridayEnd;
	}	
	this.setFridayClosed=function(closed){
		closedFriday=closed;
	}	
	this.getFridayClosed=function(){
		return closedFriday;
	}
	this.setSaturdayStart=function(start){
		saturdayStart=start;
	}		
	this.getSaturdayStart=function(){
		return saturdayStart;
	}	
	this.setSaturdayEnd=function(end){
		saturdayEnd=end;
	}	
	this.getSaturdayEnd=function(){
		return saturdayEnd;
	}	
	this.setSaturdayClosed=function(closed){
		closedSaturday=closed;
	}	
	this.getSaturdayClosed=function(){
		return closedSaturday;
	}	
	this.setHoursInfo=function(hrs){
		hoursInfo=hrs;
	}	
	this.getHoursInfo=function(){
		return hoursInfo;
	}	
	this.setPayMethods=function(payMthds){
		payMethods=payMthds;
	}
	this.getPayMethods=function(){
		return payMethods;
	}	
	this.setPhone=function(ph){
		phone=ph;
	}
	this.getPhone=function(){
		return phone;
	}
	this.setReceivingFee=function(fee){
		receivingFee=fee;
	}
	this.getReceivingFee=function(){
		return receivingFee;
	}
	this.setSizeAllowance=function(sizeAllow){
		sizeAllowance=sizeAllow;
	}
	this.getSizeAllowance=function(){
		return sizeAllowance;
	}
	this.setState=function(st){
		state=st;
	}
	this.getState=function(){
		return state;
	}
	this.setZip=function(z){
		zip=z;
	}
	this.getZip=function(){
		return zip;
	}
	this.setKPPackageRates=function(r){
		kpPackageRates=r;
	}
	this.getKPPackageRates=function(){
		return kpPackageRates;
	}
	this.setKPExtendedStorageRates=function(e){
		kpExtendedStorageRates=e;
	}
	this.getKPExtendedStorageRates=function(){
		return kpExtendedStorageRates;
	}
	this.setKPSkidRate=function(s){
		kpSkidRate=s;
	}
	this.getKPSkidRate=function(){
		return kpSkidRate;
	}
}