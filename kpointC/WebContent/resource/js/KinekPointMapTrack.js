//bing tracking map
var map = null;

//JSON object with package data
var trackLocation;
var bingLocation1 = new Array();

//route information populated by information from bing
var waypoints = null;

//array of pushpins populated by information from waypoints
var pushpins = new Array();

//highlighted due to hover over
var currentActive = null;
var highlightedIndex = null;

//highlighted due to being clicked
var selectedIndex = null;
var currentSelected = null;
var isSecondTry = false;

var notMapped = new Array();

//initial startup
function loadTrackCordinate(locations) {
	var startLocation = new Microsoft.Maps.Location(46.99999988079071,-119.00000028312206);
	var viewBoundaries = new Microsoft.Maps.LocationRect(startLocation,50,50);
	
	var mapOptions =
	{credentials:jQuery('#bingMapsCred').val(),	
     mapTypeId: Microsoft.Maps.MapTypeId.road,
     enableSearchLogo: false,
     enableClickableLogo: false,
     bounds: viewBoundaries
    };
	bingLocation1 = new Array();
	waypoints = null;
	pushpins = new Array();
	currentActive = null;
	highlightedIndex = null;
	selectedIndex = null;
	currentSelected = null;
	notMapped = new Array();

	map = new Microsoft.Maps.Map(document.getElementById("map"), mapOptions);
	window.setTimeout(function () {jQuery('.NavBar_typeButtonContainer').hide();}, 100);
	trackLocation = locations.activities;
	trackCoordinates();
}

//Check to see what kind of request has to be made to bing if any at all
function trackCoordinates() {	
	if(trackLocation != null && trackLocation.length != 0){
		map.getCredentials(MakeGeocodeTrackRequest);
	}
}

//Populates a bing request URL with given information from inputed JSON object (trackLocation)
function MakeGeocodeTrackRequest(credentials)
{   
   var trackRequest = "http://dev.virtualearth.net/REST/v1/Routes?";

   var notMappedCounter = 0;
   var modifier = 0;
   
   for(var i =0; i < trackLocation.length; i++){
	   var currentLocation = new Array();
	   var j = 0;
	   if(trackLocation[i].address1 != "null"){
		   currentLocation[j] = trackLocation[i].address1;
		   j++;
	   }
	   if(trackLocation[i].stateProv != "null"){
		   currentLocation[j] = trackLocation[i].stateProv;
		   j++;
	   }
	   if(trackLocation[i].country != "null"){
		   currentLocation[j] = trackLocation[i].country;
		   j++;
	   }
	   if(!(currentLocation.length < 2)){
		   trackRequest = trackRequest + "wp." + (i + modifier) + "=" + currentLocation.join(",") + "&";
	   }
	   else{;
		   notMapped[notMappedCounter] = i;
		   notMappedCounter++;
		   modifier--;
	   }
   }
   trackRequest +=	"routePathOutput=Points&output=json&jsonp=TrackCallback&key=" + credentials;
   
   
   
   var mod = 0;
   var bing = 0;
   for(var k=0; k < trackLocation.length; k++){
	   if(jQuery.inArray(k + mod, notMapped) == -1){
		   bingLocation1[bing] = trackLocation.length - k;
		   bing++;
	   } 
   }
   CallRestService(trackRequest);
}



function CallRestService(request) 
{
   var script = document.createElement("script");
   script.setAttribute("type", "text/javascript");
   script.setAttribute("src", request);
   document.body.appendChild(script);
}

//Return from GeocodeTrackRequest
function TrackCallback(result) {
    if (result &&
          result.resourceSets &&
          result.resourceSets.length > 0 &&
          result.resourceSets[0].resources &&
          result.resourceSets[0].resources.length > 0) {
          
    	isSecondTry = false;
        // Set the map view
        var bbox = result.resourceSets[0].resources[0].bbox;
        var viewBoundaries = Microsoft.Maps.LocationRect.fromLocations(new Microsoft.Maps.Location(bbox[0], bbox[1]), new Microsoft.Maps.Location(bbox[2], bbox[3]));
        map.setView({ bounds: viewBoundaries});

        // Draw the route
        var routeline = result.resourceSets[0].resources[0].routePath.line;
        var routepoints = new Array();
        
        for (var i = 0; i < routeline.coordinates.length; i++) {
            routepoints[i]=new Microsoft.Maps.Location(routeline.coordinates[i][0], routeline.coordinates[i][1]);
        }
        // Draw the route on the map
        var routeshape = new Microsoft.Maps.Polyline(routepoints, {zIndex:(-trackLocation.length), strokeColor:new Microsoft.Maps.Color(200,0,0,200)});
        map.entities.push(routeshape);
        renderDirection(result.resourceSets[0].resources[0]);
        }
    else{
    	if(!isSecondTry){
    		isSecondTry = true;
    		trackCoordinates();
    	}
    	else{
    		showErrorMessage();
    	}
    }
}

//renders pushpins and route on map
function renderDirection(route) {
    /*By using the start locations from routeLegs we can get the longitude and latitude values that we're looking for.
     * However, the very last set of coordinates we need is the end Location on the last routeLeg.
     */
    waypoints = route.routeLegs;

    for(var i = 0; i<waypoints.length; i++){
    	var location = new Microsoft.Maps.Location(waypoints[i].startLocation.point.coordinates[0], waypoints[i].startLocation.point.coordinates[1]);
    	if(i != waypoints.length-1) {	
    		var pushpin = new Microsoft.Maps.Pushpin(location,{text: bingLocation1[i].toString(),zIndex:(-i + 50)});
			map.entities.push(pushpin);
			pushpins[i] = pushpin;
    	}
    	else {
    		var location = new Microsoft.Maps.Location(waypoints[i].startLocation.point.coordinates[0], waypoints[i].startLocation.point.coordinates[1]);
	    	var pushpin = new Microsoft.Maps.Pushpin(location,{text: bingLocation1[i].toString(), zIndex:(-i + 50)});
	    	map.entities.push(pushpin);
	    	pushpins[i] = pushpin;
	    	
	    	location = new Microsoft.Maps.Location(waypoints[i].endLocation.point.coordinates[0], waypoints[i].endLocation.point.coordinates[1]);
	    	pushpin = new Microsoft.Maps.Pushpin(location, {text: bingLocation1[i+1].toString(),zIndex:(-(i+1) + 50)});
	    	map.entities.push(pushpin);
	    	pushpins[i+1] = pushpin;
	    }
    }
    createSideBarTrackList(trackLocation);
}

//creates sidebar with information
function createSideBarTrackList(newLocations){
	
	
	//Create list of markers
	var firstDepotId = 0;
	jQuery("#list").empty();
	
	var j = newLocations.length;
	var x = 0;
	for (var i = 0; i < newLocations.length; i++) {
		
		var aElement = null;
		var liElement = null;
		var name = newLocations[i];
			
		if(jQuery.inArray(i, notMapped) == -1){
			//element is on the map
			var liElement = jQuery('<li></li>')
				.attr('id', "marker_li_" + x)
				.attr('name', 'item_' + x);
			
			var aElement = jQuery('<a></a>')
				.attr('id', "marker_a_" + x)
				.attr('href', "#")
				.attr('title', name.activity);

			aElement.click(function(event){
				event.preventDefault();
				if (currentSelected != null){
					var oldIndex = currentSelected[0].id.replace('marker_a_', '');		
					jQuery('#marker_li_'+oldIndex).removeClass('active');
				}
				currentSelected = jQuery(this);
				var index = parseInt(currentSelected[0].id.replace('marker_a_', ''));
				highlightSelectPushpin(index);	
				
				var bbox;
				if(index != pushpins.length-1)
				bbox = waypoints[index].startLocation.bbox;
				else
				bbox = waypoints[index-1].endLocation.bbox;				
				setTrackView(bbox[0],
						bbox[1],
						bbox[2],
						bbox[3],
						index);
			});
		
			x++;
		}
		else
		{	
			var liElement = jQuery('<li></li>')
			.attr('id', "marker_li_nm" + i)
			.attr('name', 'item_nm' + i);
			
			var aElement = jQuery('<a></a>')
			.attr('id', "marker_a_nm" + i)
			.attr('href', "#")
			.attr('title', name.activity);

			aElement.click(function(event){
				event.preventDefault();
			});
		}
		var markerElement = jQuery('<span></span>')
			.addClass('marker')
			.attr('id', "marker_marker_" + i)
			.html(parseInt(j));
		
		
		//Location Check
		var address;
		if(newLocations[i].stateProv == "null" && newLocations[i].address1 == "null" && newLocations[i].country != "null"){
			address = newLocations[i].country;
		}
		else if(newLocations[i].stateProv == "" && newLocations[i].address1 != ""){
			address = newLocations[i].address1;
		}
		else if(newLocations[i].address1 == ""){
			address = newLocations[i].stateProv;
		}
		else{
			address = newLocations[i].address1 + "," + newLocations[i].stateProv;
		}
		
		//Status unavaiable check
		if(newLocations[i].activity == ""){
			newLocations[i].activity = "Status Unavailable";
		}
		
		//Date unavailable check, we assume if we get a day back, we get the entire date, same thing for time but with hours
		var date;
		if(newLocations[i].day != ""){
			date = newLocations[i].day + "/" + newLocations[i].month + "/" + newLocations[i].year;
			if(newLocations[i].hour != ""){
				date +=  " " + newLocations[i].hour + ":" + newLocations[i].minute + " " + newLocations[i].pm;
			}
		}
		else{
			date = "Date Unavailable";
		}
		
		var detailsStr = '<strong id=marker_strong_' + i + '>' + newLocations[i].activity + '</strong> '
		+ address + '<br />' + date;
		
		//if(address2 && address2 != "null" && address2 != "") {
		//	detailsStr += ', ' + address2;
		//}		
		
		var detailsElement = jQuery('<span></span>')
			.attr('id', "marker_details_" + i)
			.attr('class', 'details')
			.html(detailsStr);
		
		aElement.append(markerElement);
		aElement.append(detailsElement);
		liElement.append(aElement);
		
		if(jQuery.inArray(i, notMapped) == -1){
			//element is on the map
			liElement.hover( function() {	
				currentActive = jQuery(this);
				currentActive.addClass('active');
				var index = parseInt(currentActive[0].id.replace('marker_li_', ''));
				highlightPushpin(index);
			}, function() {
				var index = parseInt(currentActive[0].id.replace('marker_li_', ''));
				if (currentActive != null && selectedIndex != index){
					currentActive.removeClass('active');
				}
				deHighlightPushpin(index);
				highlightedIndex = null;
			});
		}
		jQuery('#list').append(liElement);
		j--;
	}

	//Add fancy scroll bar
	jQuery('#listWrapper1').jScrollPane({
		scrollbarOnLeft: true,
		showArrows: true,
		scrollbarWidth: 17,
		arrowSize: 17
	});
	//Add first and last classes
	jQuery('ul li:first-child, ol li:first-child').addClass('first');
	jQuery('ul li:last-child, ol li:first-child').addClass('last');	
	Microsoft.Maps.Events.addHandler(map, "viewchangeend", viewChangeHighlight);
}

function showErrorMessage() {
	var message = jQuery('<div></div>')
		.html('Bing Maps has encountered an error or the address you entered does not exist. Please try again.');
	jQuery('#searchWrapper').empty();
	jQuery('#searchWrapper').append(message);
	message.fadeIn('1000');
}

//Takes a bounding box and what corresponding pushpin was pressed
function setTrackView(b1,b2,b3,b4, pin){
    var viewBoundaries = Microsoft.Maps.LocationRect.fromLocations(new Microsoft.Maps.Location(b1, b2), new Microsoft.Maps.Location(b3, b4));
    map.setView({ bounds: viewBoundaries});
    highlightPushpin(pin);
}

//rehighlights currently selected pins, this deals with the Z index not being drawn properly after a view change in some browsers
function viewChangeHighlight(){
	if(selectedIndex != null)
	highlightSelectPushpin(selectedIndex);
	if(highlightedIndex != null)
	highlightPushpin(highlightedIndex);
}

//highlight/unhighlight the pushpin that has been clicked
function highlightSelectPushpin(pushpinIndex){
	if(selectedIndex != null){
		deHighlightSelectPushpin(selectedIndex);
	}
	var zValue = pushpins[pushpinIndex].getZIndex() + 100;
	selectedIndex = pushpinIndex;
	pushpins[pushpinIndex].setOptions({icon: 'resource/icons/poi_search_highlight.png', zIndex:zValue});
}
function deHighlightSelectPushpin(pushpinIndex){
	var zValue = pushpins[pushpinIndex].getZIndex() - 100;
	pushpins[pushpinIndex].setOptions({icon: 'resource/icons/poi_search.png',zIndex: zValue});
}


//highlight/unhighlight the pushpin that has been hovered over
function highlightPushpin(pushpinIndex){
	if(highlightedIndex != null){
		deHighlightPushpin(highlightedIndex);
	}
	var zValue = pushpins[pushpinIndex].getZIndex() + 200;
	highlightedIndex = pushpinIndex;
	pushpins[pushpinIndex].setOptions({icon: 'resource/icons/poi_search_highlight.png', zIndex:zValue});
}
function deHighlightPushpin(pushpinIndex){
	var zValue = pushpins[pushpinIndex].getZIndex() - 200;
	if(pushpinIndex != selectedIndex){
		pushpins[pushpinIndex].setOptions({icon: 'resource/icons/poi_search.png',zIndex: zValue});
	}
	else{
		pushpins[pushpinIndex].setOptions({zIndex: zValue});
	}
}
