KinekPoint=function(){
	
	var latitude=null;
	var longitude=null;
	var content=null;
	
	this.setLatitude=function(lat){
		latitude=lat;
	};
	this.setLongitude=function(lon){
		longitude=lon;
	};
	this.setContent=function(txt){
		content=txt;
	};
	
	this.getLatitude=function(){
		return latitude;
	};
	this.getLongitude=function(){
		return longitude;
	};
	this.getContent=function(){
		return content;
	};
};