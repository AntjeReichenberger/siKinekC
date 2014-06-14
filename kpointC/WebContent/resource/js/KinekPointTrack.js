var markers = [];

var infoWindow = null;
var latlngs = [];
var newLocations = null;
var infoWindowList = [];
var infoWinIndex = 0;
var expanding = false;
var selectedItem=null;
var kpListElement=null;
var selectedItemId = null;
var isIE = false;
var trackRequests = new Array();

var labelValue = 0;

var elementId = 0;	 
var eventObj;

var resultSet = new Array();

//JSON object with package data
var trackLocation;

var geocoder;

var marker_image = new Image();
marker_image.src = "resource/icons/mapmarker_wide_grey.png";	
var marker_image_act = new Image();
marker_image_act.src = "resource/icons/mapmarker_wide_red.png";		

var requestedPageName="";

function reset(){
	markers = [];
	infoWindow = null;
	latlngs = [];
	newLocations = null;
	infoWindowList = [];
	infoWinIndex = 0;
	expanding = false;
	selectedItem=null;
	kpListElement=null;
	selectedItemId = null;
	isIE = false;
	trackRequests = new Array();
	labelValue = 0;
	elementId = 0;	 
	eventObj = null;
	resultSet = new Array();
	//JSON object with package data
	trackLocation = null;
	geocoder = null;	
}


function loadGoogleMap(locations) {
    reset();
	gmap= new google.maps.Map(document.getElementById("map"),{
		center:new google.maps.LatLng(46.99999988079071,-119.00000028312206),
		mapTypeId:'roadmap',
		zoom: 4,
		mapTypeControlOptions:{style: google.maps.MapTypeControlStyle.DROPDOWN_MENU}	
	});
	
	var browserName=navigator.appName;
	if (browserName=="Netscape"){
		isIE=false;
	}else{
		isIE=true;
	}
	trackLocation = locations.activities;
	codeAddresses();
}

//requested page name depotsearch_result or adb_choosedefaultkinekpoint_result
function setRequestedPageName(pageName){
	requestedPageName=pageName;
}

function getRequestedPageName(){return requestedPageName;}

function codeAddresses() {
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
		   trackRequests[i] = currentLocation.join(",");
	   }
	   else{
		  trackRequests[i] = "";
	   }
   }
   loopRequests(1);
}

function loopRequests(requestNum){
	makeRequest(requestNum);
}

function makeRequest(requestNum){

geocoder = new google.maps.Geocoder();
		geocoder.geocode( { 'address': trackRequests[requestNum-1]}, function(results, status) {
		  if (status == google.maps.GeocoderStatus.OK) {
			var latlng = results[0].geometry.location;
			var bounds = new google.maps.LatLngBounds(latlng);	
			
			var imgSrc = "";
			imgSrc = marker_image;
			
			var lbl= trackRequests.length - labelValue;
			var lblMarker=new LabelMarker(bounds,imgSrc,lbl,gmap);
			markers.push(lblMarker);
			resultSet.push(results[0].geometry.location);
			
			if(labelValue == trackRequests.length-1){
				finishRequests();
			}
			else{
			//check for identical geocode values to minimize requests	
				for(var i = (labelValue+1); i < trackRequests.length; i++){
					if(trackRequests[i] == trackRequests[i-1]){
						labelValue = labelValue + 1;
						var lbl=trackRequests.length - labelValue;
						var lblMarker=new LabelMarker(bounds,imgSrc,lbl,gmap);
						markers.push(lblMarker);
						resultSet.push(results[0].geometry.location);
					}
					else{
						break;
					}
				}
				if(labelValue == trackRequests.length){
					finishRequests();
				}
				else{
					labelValue++;				
					loopRequests(labelValue+1);
				}
			}
		  } else if(status == google.maps.GeocoderStatus.OVER_QUERY_LIMIT) {
			//console.log("Geocode was not successful for the following reason: " + status);
			setTimeout("loopRequests(labelValue)",2000);
		  }
		  else{	
			loopRequests(requestNum+1);
		  }
		});	
}

function finishRequests(){
	eventObj=new MarkerEvent();
	eventObj.registerEventHandler(document.getElementById("map"),"mouseover",mapHover);
	eventObj.registerEventHandler(document.getElementById("map"),"mouseout",mapBlur);
	eventObj.registerEventHandler(document.getElementById("map"),"click",mapClick);
	drawLine(resultSet);
	centerMap();
	createSideBarList(trackLocation);
}

function centerMap(){
	var latLngBounds = new google.maps.LatLngBounds( );
	for ( var i = 0; i < resultSet.length; i++ )
	{
		latLngBounds.extend(resultSet[i]);
	}
	gmap.setCenter(latLngBounds.getCenter());
}


function drawLine(lineCoordinates){
  var travelPath = new google.maps.Polyline({
    path: lineCoordinates,
    strokeColor: "#FF0000",
    strokeOpacity: 1.0,
    strokeWeight: 2,
    zIndex: 100
  });
  travelPath.setMap(gmap);
}

function setSelectedItem(item){
	selectedItem=item;
}
function getSelectedItem(){
	return selectedItem;
}

function setKpListElement(ele){
	kpListElement=ele;
}
function getKpListElement(){
	return kpListElement;
}

function createSideBarList(newLocations){
	//Create list of markers
	
	for(var i = 0; i < markers.length; i++){
		jQuery('#marker_div_' + i).css('z-index',Math.round(((2/(i+1))*500 + 1000000)));
	}
	
	var firstDepotId=0;
	jQuery("#list").empty();
	
	jQuery(markers).each(function(i, marker) {
		var marker = markers[i];		
		var location = newLocations[i];
		var address1 = newLocations[i].address1;

		var liElement = jQuery('<li></li>')
			.attr('id', "marker_li_"+i)
			.attr('name', 'item_'+i);		
		
		var aElement = jQuery('<a></a>')
			.attr('id', "marker_a_"+i)
			.attr('href', "#")
			.attr('title', location.activity)
			.click(function(e) {
				e.preventDefault();
				var item = jQuery(this).closest('li');
				selectItem(item, true);
			});
		
		var markerElement = jQuery('<span></span>')
			.addClass('marker')
			.attr('id', "marker_marker_"+i)
			.html((trackRequests.length - (parseInt(i))));
			
		//Location Check
		var address;
		if(location.stateProv == "null" && location.address1 == "null" && location.country != "null"){
			address = location.country;
		}
		else if(location.stateProv == "null" && location.address1 == "null" && location.country == "null"){
			address = "";
		}
		else if(location.stateProv == "null" && location.address1 != "null"){
			address = location.address1;
		}
		else if(location.address1 == "null"){
			address = location.stateProv;
		}
		else{
			address = location.address1 + "," + location.stateProv;
		}
		
		//Status unavaiable check
		if(location.activity == ""){
			location.activity = "Status Unavailable";
		}
		
		//Date unavailable check, we assume if we get a day back, we get the entire date, same thing for time but with hours
		var date;
		if(location.day != "0"){
			date = location.day + "/" + location.month + "/" + location.year;
			if(location.hour != "0"){
				date +=  " " + location.hour + ":" + location.minute + " " + location.pm;
			}
		}
		else{
			date = "Date Unavailable";
		}
		
		var detailsStr = '<strong id=marker_strong_' + i + '>' + location.activity + '</strong> '
		+ address + '<br />' + date;
		
		var detailsElement = jQuery('<span></span>')
			.attr('id', "marker_details_"+i)
			.attr('class', 'details')
			.html(detailsStr);
		
		aElement.append(markerElement);
		aElement.append(detailsElement);		
		liElement.append(aElement);				
		liElement
			.hover( function() {
				//Set active image
				jQuery('#listWrapper1 li').removeClass('active');
				jQuery(this).addClass('active');
			}, function() {
				//Set normal image
				jQuery(this).removeClass('active');
			});
		
		jQuery('#list').append(liElement);
	});

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
	
	newSelect = jQuery('#list li:first-child');
	selectItem(newSelect);

	//Register hover event handlers
	eventObj.registerEventHandler(document.getElementById("list"),"mouseover",listHover);
	eventObj.registerEventHandler(document.getElementById("list"),"mouseout",listBlur);
}

function initMarkerImage() {	
	var image =  new google.maps.MarkerImage("resource/icons/mapmarker_wide_grey.png",
		new google.maps.Size(27, 42),				
		new google.maps.Point(13, 42));
	return image;
}

function listHover(event){	 
	 var item = getItemFromTarget(eventObj.getTarget(event));	
	 if(item != null) {
		 var marker = getMarkerFromItem(item);
		 highlightMarker(marker);
		 marker.css('cursor', 'pointer');
	 }
}

function listBlur(event){	 
	 var item = getItemFromTarget(eventObj.getTarget(event));	
	 if(item != null && !item.is('.selected')) {
		 var marker = getMarkerFromItem(item);
		 unhighlightMarker(marker);
	 }
}

function mapHover(event){	 
	var marker = getMarkerFromTarget(eventObj.getTarget(event));
	if(marker != null) {
		var item = getItemFromMarker(marker);
		highlightMarker(marker);
		item.addClass('active');
	}
}

function mapBlur(event){
	 var marker = getMarkerFromTarget(eventObj.getTarget(event));
	 if(marker != null && !marker.is('.selected')) {
		 var item = getItemFromMarker(marker);
		 unhighlightMarker(marker);
		 item.removeClass('active');
	 }
}

function mapClick(event){
	var marker = getMarkerFromTarget(eventObj.getTarget(event));		
	if(marker != null) {
		var item = getItemFromMarker(marker);
		marker.css('cursor', 'pointer');
		marker.addClass('selected');
		
		selectItem(item, true);
	}
}

function getItemFromTarget(target) {
	var item = jQuery(target).closest('li');
	if(item.length > 0) {
		return item;
	}
	else {
		return null;
	}
}

function getItemFromId(id) {
	var item = jQuery('[name=item_'+id+']');
	if(item.length > 0) {
		return item;
	}
	else {
		return null;
	}
}

function getMarkerFromTarget(target) {
	 var targetSplit = target.id.split("_");
	 var markerId = targetSplit[0] + "_" + targetSplit[1];
	  
	 if( (markerId == "marker_img" || markerId == "marker_span") && targetSplit[2] != 0 ) {
		 return jQuery('#marker_img_' + targetSplit[2]);	
	 }
	 else {
		return null; 
	 }
}

function getItemFromMarker(marker) {
	var index = parseInt(marker[0].id.replace('marker_img_', ''));
	return jQuery('#marker_li_'+index);
}

function getMarkerFromItem(item) {
	var index = parseInt(item[0].id.replace('marker_li_', ''));
	return jQuery('#marker_img_'+index);
}

function highlightMarker(marker) {
	var zIndex = 4000000;
	if(marker.hasClass('selected')){
		var zIndex = 3000000;
	}
	var markerDiv = marker.attr('id');
	var split = markerDiv.split("_");	
	jQuery('#marker_div_' +(split[2])).css('z-index',zIndex);
	
	marker.attr('src', marker_image_act.src);
}

function unhighlightMarker(marker) {
	var markerDiv = marker.attr('id');
	var split = markerDiv.split("_");	
	var newValue = ((2/(split[2]+1))*500 + 1000000);
	jQuery('#marker_div_' +split[2]).css('z-index',Math.round(newValue));
	
	marker.attr('src', marker_image.src);
}


function selectItem(item) {
	
	var nameAttr=new Array();
	nameAttr=item.attr('name').split("_");
	var depotId=nameAttr[1];
	
	var marker = getMarkerFromItem(item);
		
	//Set currently selected id variable
	selectedItemId = depotId;
	
	gmap.setCenter(resultSet[depotId]);
	
	//Remove currently selected and unhighlight marker
	item.closest('#list').find('li')
		.removeClass('selected')
		.removeClass('active')
		.each(function() {
			var curMarker = getMarkerFromItem(jQuery(this));
			curMarker.attr('src', marker_image.src);
			curMarker.removeClass('selected');
			if(curMarker.attr('id')){
				var split = curMarker.attr('id').split("_");	
				var newValue = ((2/(split[2]+1))*500 + 1000000);
				jQuery('#marker_div_' +(split[2])).css('z-index',Math.round(newValue));
			}
		});
	
	//Highlight item
	item.addClass('selected');
	item.addClass('active');

	//Highlight marker
	marker.addClass('selected');
	highlightMarker(marker);
}

function showMessage() {
	var message = jQuery('<div></div>')
		.addClass('growl')
		.html('MessageHere');

	jQuery('#mapContainer').append(message);
	message.fadeIn('1000');
}

function hideMessage() {
	jQuery('.growl').fadeOut('1000');
}

