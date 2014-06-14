function KinekPointMapDirection(mapId, directionsPanelId) {
	var mapId = mapId;
	var directionsPanelId = directionsPanelId;
	var dirMap = null;
	var userOrigin = "";
	var userOriginCity = "";
	var kpDestination = "";
	
	this.setUserOrigin = function(origin) {
		userOrigin = origin;
	};
	
	this.setUserOriginCity = function(originCity) {
		userOriginCity = originCity;
	};
	
	this.setKpDestination = function(destination) {
		kpDestination = destination;
	};
	
	this.getUserOrigin = function() {
		return userOrigin;
	};
	
	this.getUserOriginCity = function() {
		return userOriginCity;
	};
	
	this.getKpDestination = function() {
		return kpDestination;
	};
	this.getDirMap = function() {
		return dirMap;
	};
	this.getDirectionsPanelId = function() {
		return directionsPanelId;
	};
	
	this.getKinekPointDirectionMap = function(){
		var mapOptions =
		{credentials:jQuery('#bingMapsCred').val(),	
	     mapTypeId: Microsoft.Maps.MapTypeId.road,
	     enableSearchLogo: false,
	     enableClickableLogo: false,
	     bounds: viewBoundaries
		};
		dirMap = new Microsoft.Maps.Map(document.getElementById("direction_map"), mapOptions);
		dirMap.getCredentials(MakeRouteRequest);
	};
	
	function MakeRouteRequest(credentials)
    {
		var routeRequest = "http://dev.virtualearth.net/REST/v1/Routes?wp.0=" + userOrigin + "&wp.1=" + kpDestination + "&routePathOutput=Points&output=json&jsonp=RouteCallback&key=" + credentials;
		CallRestService(routeRequest);
    }
	
	 function CallRestService(request) 
     {
        var script = document.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", request);
        document.body.appendChild(script);
     }
}

function RouteCallback(result) {
    if (result &&
          result.resourceSets &&
          result.resourceSets.length > 0 &&
          result.resourceSets[0].resources &&
          result.resourceSets[0].resources.length > 0) {
          
    		isSecondTry = false;
            // Set the map view
            var bbox = result.resourceSets[0].resources[0].bbox;
            var viewBoundaries = Microsoft.Maps.LocationRect.fromLocations(new Microsoft.Maps.Location(bbox[0], bbox[1]), new Microsoft.Maps.Location(bbox[2], bbox[3]));
            kpdir.getDirMap().setView({ bounds: viewBoundaries});


            // Draw the route
            var routeline = result.resourceSets[0].resources[0].routePath.line;
            var routepoints = new Array();
            
            for (var i = 0; i < routeline.coordinates.length; i++) {

                routepoints[i]=new Microsoft.Maps.Location(routeline.coordinates[i][0], routeline.coordinates[i][1]);
            }
            
            // Draw the route on the map
            var routeshape = new Microsoft.Maps.Polyline(routepoints, {strokeColor:new Microsoft.Maps.Color(200,0,0,200)});
            kpdir.getDirMap().entities.push(routeshape);
            renderDirection(result.resourceSets[0].resources[0]);
        }
	else{
		if(!isSecondTry){
			isSecondTry = true;
			kpdir.getDirMap().getCredentials(MakeRouteRequest);
		}
		else{
			showErrorMessage();
		}
	}
}

function renderDirection(route) {
    // Unroll route
    var legs = route.routeLegs;
    var distance = "<strong>" + route.travelDistance.toFixed(1) + " km - about " + convertTimeToString(route.travelDuration) + "</strong>";
    var numTurns = 0;
    var leg = null;

    // Get intermediate legs
    var turns = "<table class=\"adp-directions\">";
    for(var i = 0; i < legs.length; i++)
    {
    	// Get this leg so we don't have to derefernce multiple times
    	leg = legs[i];  // Leg is a VERouteLeg object
    	
        // Unroll each intermediate leg
        var turn = null;  // The itinerary leg
           
        //leg.Itinerary.Items
        for(var j = 0; j < leg.itineraryItems.length; j ++)
        {
        	numTurns++;
        	turns += "<tr>";
        	
        	turn = leg.itineraryItems[j];  // turn is a VERouteItineraryItem object
        	if (numTurns == 1) {
        		turns += "<td class=\"directionsCell\" style=\"width: 5%; border-bottom: 1px dotted #000;\">";
        		turns += "<img src=\"/kpointC/resource/icons/mapicon_start.gif\"></img>";
        	}
        	else if (numTurns == leg.itineraryItems.length) {
        		turns += "<td style=\"width: 5%;\">";
        		turns += "<img src=\"/kpointC/resource/icons/mapicon_end.gif\"></img>";
        	}
        	else {
        		turns += "<td class=\"directionsCell\" style=\"width: 5%; border-bottom: 1px dotted #000;\">";
            	turns += (numTurns - 1) + ".";
        	}
        	turns += "</td>";
        	
        	if (numTurns == leg.itineraryItems.length) {
        		turns += "<td style=\"width: 68%;\">";
        	}
        	else {
        		turns += "<td class=\"directionsCell\" style=\"width: 68%; border-bottom: 1px dotted #000;\">";
        	}
        	turns += turn.instruction.text;
        	turns += "</td>";
        	
        	if (numTurns == leg.itineraryItems.length) {
        		turns += "<td style=\"width: 27%;\">";
        	}
        	else {
        		turns += "<td class=\"directionsCell\" style=\"width: 27%; border-bottom: 1px dotted #000;\">";
        	}
        	turns += turn.travelDistance.toFixed(1) + " km";
        	turns += "</td>";
        	
        	turns += "</tr>";
        	
        	var pinOptions; 
        	if(j == 0){
        		pinOptions = {
        			icon: 'resource/icons/mapicon_start.gif',
        			typeName: 'startMarker'
            	};        		
        	}
        	else if (j == leg.itineraryItems.length-1){
        		pinOptions = {
            		icon: 'resource/icons/mapicon_end.gif',
            		typeName: 'endMarker'
                };      		
        	}
        	else{
        		pinOptions = {	
                	typeName: 'dirMarker',
                	text: j.toString()
                };  
        	}
        	Microsoft.Maps.Pushpin.prototype.instruction;
        	var pushpin = new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(turn.maneuverPoint.coordinates[0], turn.maneuverPoint.coordinates[1]),pinOptions);
        	pushpin.instruction = turn.instruction.text;
    		Microsoft.Maps.Events.addHandler(pushpin, "mouseover", dirMapHover);
        	Microsoft.Maps.Events.addHandler(pushpin, "mouseout", dirMapBlur);
        	
         	kpdir.getDirMap().entities.push(pushpin);
        }
    }
    turns += "</table>";

    var fullRoute = distance;
    fullRoute += "<br />";
    fullRoute += "<br />";
    fullRoute += turns;
    
    document.getElementById(kpdir.getDirectionsPanelId()).innerHTML = fullRoute;
}

function dirMapHover(mapEvent) {
	if (mapEvent.targetType = "pushpin")
	{
		clearTimeout(infoTimerID);
		openDirWindow(mapEvent.target);
	}
	return true;
}

function dirMapBlur(mapEvent) {
	if (mapEvent.targetType = "pushpin")
	{
		infoTimerID = setTimeout('closeInfoWindows()', infoTimer);			
	}
}

function openDirWindow(pushpin){
	
	var add1Div = jQuery('<div></div>').html(pushpin.instruction);

	var wrapper = jQuery('<div style=\"text-align: left;\"></div>')
			.append(add1Div);
	
	clearTimeout(infoTimerID);
	var pix = kpdir.getDirMap().tryLocationToPixel(pushpin.getLocation(), Microsoft.Maps.PixelReference.control);

	var ibDescription = document.getElementById('ibDescription');
	ibDescription.innerHTML = wrapper[0].innerHTML;
	
	var infobox = document.getElementById('infoBox');
	infobox.style.top = (pix.y - 60) + "px";
	infoBox.style.left = (pix.x + 15) + "px";
	infoBox.style.visibility = "visible";
	document.getElementById('direction_map').appendChild(infoBox);
}

//helper methods
function convertTimeToString(seconds){
	var min = Math.round(seconds / 60);
	var hour = 0;
	if (min >= 60) {
		hour = Math.round(min / 60);
		min = min % 60;
	}
	var day = 0;
	if (hour >= 24) {
		day = Math.round(hour / 24);
		hour = hour % 24;
	}
	
	var time = "";
	if (day > 1) {
		time += day + " days ";
	}
	else if (day == 1) {
		time += day + " day ";
	}
	
	if (hour > 1) {
		time += hour + " hours ";
	}
	else if (hour == 1) {
		time += hour + " hour ";
	}
	
	if (min > 1) {
		time += min + " mins ";
	}
	else if (min == 1) {
		time += min + " min ";
	}
	
	return time;
}


