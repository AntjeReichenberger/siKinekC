var map = null;
var infoWindowList = [];
var infoWindowIndex = 0;
  
function initializeMap() {
	var latlng = new VELatLong(48.922499,-93.164062);
    
    map = new VEMap("map");
    map.LoadMap(latlng, 4);
    map.HideDashboard();
}

function addMarkers(latLonList){
	for(var i = 0; i < latLonList.length; i++){
		var lat = latLonList[i].getLatitude();
		var lon = latLonList[i].getLongitude();
		var cont = latLonList[i].getContent();
	    var latlon = new VELatLong(lat,lon);
		
		var shape = new VEShape(VEShapeType.Pushpin, latlon);
		shape.SetDescription(cont);
		map.AddShape(shape);
	}
}