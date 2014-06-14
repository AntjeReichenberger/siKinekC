function init(){
	var applewayVideo=new KinekPoint();
	applewayVideo.setLatitude(48.936577);
	applewayVideo.setLongitude(-119.43387);
	applewayVideo.setContent(getHtmlContent("Appleway Video","border"));
	
	var redRiverRepair=new KinekPoint();
	redRiverRepair.setLatitude(48.966949);
	redRiverRepair.setLongitude(-97.244732);
	redRiverRepair.setContent(getHtmlContent("Red River Repair","redriver"));
	
	var petesAppliance=new KinekPoint();
	petesAppliance.setLatitude(46.469552);
	petesAppliance.setLongitude(-84.36676);
	petesAppliance.setContent(getHtmlContent("Pete's Appliance","petes"));
	
	var bayBrokerage1=new KinekPoint();
	bayBrokerage1.setLatitude(42.979723);
	bayBrokerage1.setLongitude(-82.422352);
	bayBrokerage1.setContent(getHtmlContent("Bay Brokerage","bb"));
				
	var fbPackageShippingCenter=new KinekPoint();
	fbPackageShippingCenter.setLatitude(42.715426);
	fbPackageShippingCenter.setLongitude(-82.492604);
	fbPackageShippingCenter.setContent(getHtmlContent("FB Package Shipping Center","fbpackage"));
	
	var computingExpress=new KinekPoint();
	computingExpress.setLatitude(42.329965);
	computingExpress.setLongitude(-83.046856);
	computingExpress.setContent(getHtmlContent("Computing Express","computingexpress"));
	
	var bayBrokerage2=new KinekPoint();
	bayBrokerage2.setLatitude(42.982047);
	bayBrokerage2.setLongitude(-78.885269);
	bayBrokerage2.setContent(getHtmlContent("Bay Brokerage","baybrokerage"));
				
	var theUpsStore=new KinekPoint();
	theUpsStore.setLatitude(43.172259);
	theUpsStore.setLongitude(-79.036331);
	theUpsStore.setContent(getHtmlContent("UPS Store","lewiston"));
	
	var wellesleyIslandBuildingSupply=new KinekPoint();
	wellesleyIslandBuildingSupply.setLatitude(44.310399);
	wellesleyIslandBuildingSupply.setLongitude(-75.993805);
	wellesleyIslandBuildingSupply.setContent(getHtmlContent("Wellesley Island Building Supply","buildingsupply"));
	
	var borderMailServices=new KinekPoint();
	borderMailServices.setLatitude(44.987445);
	borderMailServices.setLongitude(-73.450212);
	borderMailServices.setContent(getHtmlContent("Border Mail service","bordermail"));
	
	var wetherbyQuickStop=new KinekPoint();
	wetherbyQuickStop.setLatitude(44.995276);
	wetherbyQuickStop.setLongitude(-72.672672);
	wetherbyQuickStop.setContent(getHtmlContent("Wetherby's Quick Stop","wetherbys"));
	
	var normanGJensen=new KinekPoint();
	normanGJensen.setLatitude(45.004622);
	normanGJensen.setLongitude(-72.090912);
	normanGJensen.setContent(getHtmlContent("Norman G Jensen","normanjensen"));
	
	var ANDeringer=new KinekPoint();
	ANDeringer.setLatitude(45.190718);
	ANDeringer.setLongitude(-67.28332);
	ANDeringer.setContent(getHtmlContent("A.N. Deringer","border"));
	
	var anchorDev=new KinekPoint();
	anchorDev.setLatitude(44.31335);
	anchorDev.setLongitude(-76.00041);
	anchorDev.setContent(getHtmlContent("Anchor Development","anchor"));
	
	var latLonList = [applewayVideo, redRiverRepair, petesAppliance,
			   bayBrokerage1, fbPackageShippingCenter, computingExpress,
			   bayBrokerage2, theUpsStore, wellesleyIslandBuildingSupply,
			   borderMailServices, normanGJensen, ANDeringer, anchorDev];
	
	initializeMap();
	addMarkers(latLonList);
}

function getHtmlContent(header,landingpage){
	var body = "<a href='http://www.kinek.com/" + landingpage + "'>Click here</a> to learn more and sign up with Kinek to ship" + 
			 " to this location! Kinek will send you a text message and email notification when your package arrives.";

	return html = "<div style='font-family:arial; font-size:10px'>" +
				"<span style='font-size:12px'><strong>" + header + "</strong></span><br/><br/>" +
				"<p>" + body + "</p>" +
				"</div>";
}

window.onload=init;