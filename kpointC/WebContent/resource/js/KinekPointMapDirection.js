function KinekPointMapDirection(mapId, directionsPanelId) {
	var mapId = mapId;
	var directionsPanelId = directionsPanelId;
	var gmap = null;
	var userOrigin = "";
	var kpDestination = "";
	
	this.setUserOrigin=function(origin){
		userOrigin=origin;
	};
	
	this.setKpDestination=function(destination){
		kpDestination=destination;
	};
	
	this.getUserOrigin=function(){
		return userOrigin;
	};
	
	this.getKpDestination=function(){
		return kpDestination;
	};
	
	this.getKinekPointDirectionMap=function(){
		getCoordinate(userOrigin,createDirectionMap);
	};
	
	function createDirectionMap(cord){
    	gmap= new google.maps.Map(document.getElementById(mapId),{
			center:new google.maps.LatLng(cord.location.lat(),cord.location.lng()),
			zoom:11,
			mapTypeId:'roadmap',
			mapTypeControlOptions:{style: google.maps.MapTypeControlStyle.DROPDOWN_MENU}	
		});
		renderDirection();
	}
 
	function renderDirection(){
		var directionsRenderer = new google.maps.DirectionsRenderer();
		directionsRenderer.setMap(gmap);    
		directionsRenderer.setPanel(document.getElementById(directionsPanelId));
 
		var directionsService = new google.maps.DirectionsService();
		var request = {
			origin: userOrigin, 
			destination: kpDestination,
			travelMode: google.maps.DirectionsTravelMode.DRIVING,
			unitSystem: google.maps.DirectionsUnitSystem.METRIC,
			provideRouteAlternatives: false
		};
		directionsService.route(request, function(response, status) {
			if (status == google.maps.DirectionsStatus.OK) {
				directionsRenderer.setDirections(response);
			} else {		
				document.getElementById('map').style.display="none";
				document.getElementById('direction_map').style.display="block";					
				document.getElementById("direction_map").value="Google MAP API V3 Error: "+status;
			}
		});
	}

	
	//helper methods
	function getCoordinate(address, callback) {
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode({address:address}, function(result, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				callback(result[0].geometry);
			}
			else {
				userOrigin = jQuery('#criteria').val();
				getCoordinate(userOrigin, callback);
			}
		});
	}
	

}