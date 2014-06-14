var map = null;
var expanding = false;
var homeGeomatry = null;
var newLocations = null;
var latlngs = [];
var selectedItemId = null;
var origin = "";
var originCity = "";
var save;
var kpAddress;
var kpDir;
var mouseDown = false;
var viewChanged = false;
var infoHover = false;
var infoTimer = 250;
var infoTimerID;
var trackLocation;

var foundLocation;
var registerHandler;
var startLocation = new Microsoft.Maps.Location(46.99999988079071,-119.00000028312206);
var viewBoundaries = new Microsoft.Maps.LocationRect(startLocation,50,50);

var isSecondTry = false;


function loadInitialCordinate(kpAddressIn) {
		
	var mapOptions =
	{credentials:jQuery('#bingMapsCred').val(),	
     mapTypeId: Microsoft.Maps.MapTypeId.road,
     enableSearchLogo: false,
     enableClickableLogo: false,
     bounds: viewBoundaries
    };
	map = new Microsoft.Maps.Map(document.getElementById("map"), mapOptions);
	window.setTimeout(function () {jQuery('.NavBar_typeButtonContainer').hide();}, 100);
	
	kpAddress = kpAddressIn;
	map.getCredentials(MakeGeocodeRequest);	
}

function loadInitialCordinateForLoginUser(kpAddressIn,userAddress, saveButton) {

	
	var mapOptions =
	{credentials:jQuery('#bingMapsCred').val(),	
     mapTypeId: Microsoft.Maps.MapTypeId.road,
     enableSearchLogo: false,
     enableClickableLogo: false,
     bounds: viewBoundaries
	};
	map = new Microsoft.Maps.Map(document.getElementById("map"), mapOptions);
	//kpdir = new KinekPointMapDirection('direction_map', 'directions');
	kpAddress = kpAddressIn;
	origin = userAddress;
	save = saveButton;
	
	window.setTimeout(function () {jQuery('.NavBar_typeButtonContainer').hide();}, 100);
	map.getCredentials(MakeGeocodeRequest);	
}

function MakeGeocodeRequest(credentials)
{
   var geocodeRequest = "http://dev.virtualearth.net/REST/v1/Locations/" + kpAddress + "?output=json&jsonp=GeocodeCallback&key=" + credentials;
   CallRestService(geocodeRequest);
}

function CallRestService(request) 
{
   var script = document.createElement("script");
   script.setAttribute("type", "text/javascript");
   script.setAttribute("src", request);
   document.body.appendChild(script);
}

function GeocodeCallback(result) 
{   
	if (result &&
           result.resourceSets &&
           result.resourceSets.length > 0 &&
           result.resourceSets[0].resources &&
           result.resourceSets[0].resources.length > 0) 
	{
		isSecondTry = false;
    	// Set the map view using the returned bounding box
    	var bbox = result.resourceSets[0].resources[0].bbox;
    	var viewBoundaries = Microsoft.Maps.LocationRect.fromLocations(new Microsoft.Maps.Location(bbox[0], bbox[1]), new Microsoft.Maps.Location(bbox[2], bbox[3]));
    	map.setView({ bounds: viewBoundaries});
    	foundLocation = viewBoundaries.center;

    	// Add a pushpin at the found location
    	var location = new Microsoft.Maps.Location(result.resourceSets[0].resources[0].point.coordinates[0], result.resourceSets[0].resources[0].point.coordinates[1]);
    	var pushpin = new Microsoft.Maps.Pushpin(location, {icon: '', typeName: 'homeMarker', width: 27, height: 42});
    	map.entities.push(pushpin);
    	addressFound();
    }
	else{
		if(!isSecondTry){
			isSecondTry = true;
			map.getCredentials(MakeGeocodeRequest);	
		}
		else{
			showErrorMessage();
		}
	}
}

function addressFound() {		
	//search nearby depots
	//register center_changed event handler so if center of map is changed then it will populate new
	//max,min lat and lng and invoke ajax to get nearest depots
	setZoom();
	jQuery('#searchPointLatitude').val(foundLocation.latitude);
	jQuery('#searchPointLongitude').val(foundLocation.longitude);
	registerHandler = Microsoft.Maps.Events.addHandler(map, "viewchangeend", registerAjaxInvoker);
	Microsoft.Maps.Events.addHandler(map, 'viewchangestart', closeInfoWindows);
	Microsoft.Maps.Events.addHandler(map, "mousedown", mouseDownEvent);
	Microsoft.Maps.Events.addHandler(map, "mouseup", mouseUp);
}

function setZoom() {
	//If border search, zoom out to see entire province
	var zoomLevel = 11;
	if(isBorderSearch()) {
		zoomLevel = 6;
	}
	else {
		switch (String(Math.round(jQuery('#radius').val()))) {
			case "5":
				zoomLevel = 12;
				break;
			case "10":
				zoomLevel = 11;
				break;
			case "50":
				zoomLevel = 8;
				break;
			case "100":
				zoomLevel = 7;
				break;
			case "200":
				zoomLevel = 6;
				break;
		}
	}
	map.setView({zoom: zoomLevel});
}

function isBorderSearch() {
	return jQuery('#borderProvinceId').val() != '0';
}

function registerAjaxInvoker() {
	if(mouseDown == false && !(jQuery('#direction_map').is(":visible"))){
	var view = map.getBounds();
	var topLeft = view.getNorthwest();
	var bottomRight = view.getSoutheast();
	
	jQuery('#maxLongitude').val(topLeft.longitude);
	jQuery('#maxLatitude').val(topLeft.latitude);
	jQuery('#minLongitude').val(bottomRight.longitude);
	jQuery('#minLatitude').val(bottomRight.latitude);
	
	invoke($('findDepots'), 'findDepots', update);
	}
	else if (mouseDown == true){
		viewChanged = true;
	}
}

function invoke(form, event, handler) {
	var params = Form.serialize(form, {submit:event});
	new Ajax.Request(form.action, {method:'post', parameters:params, onSuccess:handler});
}

/** Function that handles the update when the async request returns. */
function update(xhr) {
	var output = eval("(" + xhr.responseText + ")");
	
	var currentPagePath = window.location.pathname;	
	var currentPageNameWithSessionId = currentPagePath.substring(currentPagePath.lastIndexOf('/') + 1);
	
	//if page contains session id then remove the session id.
	var currentPageName = currentPageNameWithSessionId.split(";")[0];
	setRequestedPageName(currentPageName);

	if(!expanding) {
		cleanKinekPointResultLabels();
	}
	
	//closeInfoWindows();
	jQuery("#prospects").hide();
	
	newLocations = new Array();
	if(output.depots.length == 0) {
		jQuery('#noKp').show();
		map.setView({zoom: map.getZoom()-1});
		showExpandingMessage();
		expanding = true;
		return;
	}
	else {
		if(!expanding) {
			jQuery('#foundKp').show();
		}
		else {
			hideExpandingMessage();
		}
		
		jQuery("#depotCount").text(output.depots.length);		
		expanding = false;
		
		for(var i = 0; i < output.depots.length; i++) {
			newLocations[i] = output.depots[i];
		}
	}	
	jQuery('#errors').html("");				
	processDepotsOnMap(newLocations);
}

//requested page name depotsearch_result or adb_choosedefaultkinekpoint_result
function setRequestedPageName(pageName){
	requestedPageName = pageName;
}

function getRequestedPageName() {
	return requestedPageName;
}

function cleanKinekPointResultLabels(){
	jQuery('#foundKp').hide();
	jQuery('#noKp').hide();
}

function showExpandingMessage() {
	var message = jQuery('<div></div>')
		.addClass('growl')
		.html('No results found.<br></br>Expanding search...');

	jQuery('#mapContainer').append(message);
	message.fadeIn('1000');
}

function hideExpandingMessage() {
	jQuery('.growl').fadeOut('1000');
}

function showErrorMessage() {
	var message = jQuery('<div></div>')
		.html('Bing Maps has encountered an error or the address you entered does not exist. Please try again.');
	jQuery('#searchWrapper').empty();
	jQuery('#searchWrapper').append(message);
	message.fadeIn('1000');
}

function processDepotsOnMap(newLocations) {
	elementId = 0;
	clearMarkers();
	
	if (jQuery('#depotId').val() != null && jQuery('#depotId').val() != "" && jQuery('#depotId').val() > 1) {
		selectedItemId = jQuery('#depotId').val();
	}
	//Pushpin is the object placed on the map.
	//Marker is the unique class of the individual pushpin
	for (var i = 0; i < newLocations.length; i++) {		
		var lbl = i + 1;
		var pushpin = new Microsoft.Maps.Pushpin(new Microsoft.Maps.Location(newLocations[i].geolat, newLocations[i].geolong), 
				{icon: '', 
				width: 31, 
				height: 26,
				text: i.toString()});
		
		if (selectedItemId != null && selectedItemId == newLocations[i].depotId) {
			pushpin.setOptions({typeName: 'marker_img_' + i +' selected'}); 
		} else {
			pushpin.setOptions({typeName: 'marker_img_' + i +' marker'}); 
		}			
		
		//For infobox
		Microsoft.Maps.Pushpin.prototype.title = null;
		pushpin.title = newLocations[i].name;
		Microsoft.Maps.Pushpin.prototype.description = null;
		pushpin.description = newLocations[i].address1;
				
		//close the infobox when the map is panned or zoomed
		Microsoft.Maps.Events.addHandler(pushpin, "mouseover", mapHover);
    	Microsoft.Maps.Events.addHandler(pushpin, "mouseout", mapBlur);
    	map.entities.push(pushpin);
    	jQuery('.marker_img_'+i).html(lbl);
	}
	createSideBarList(newLocations);	
}


function clearMarkers() {
	//When map view changes, all pins are repopulated, search result pin is maintained.
	if(!(jQuery('#direction_map').is(":visible"))){
		var searchPin = map.entities.get(0);
		map.entities.clear();
		map.entities.push(searchPin);
	}
}

function mapHover(mapEvent) {
	if (mapEvent.targetType = "pushpin")
	{
		clearTimeout(infoTimerID);
		var pushpin = mapEvent.target;
		if (pushpin.getText() != "")
		{
			var item = getItemFromPushpin(pushpin);			
			selectItem(item, true);
		}
	}
	return true;
}

function mapBlur(mapEvent) {
	if (mapEvent.targetType = "pushpin")
	{
		var pushpin = mapEvent.target;
		if (pushpin.getText() != "")
		{
			var marker = jQuery('.marker_img_' + pushpin.getText());
			unHighlightMarker(marker);
			
			var item = getItemFromPushpin(pushpin);
			item.removeClass('active');
			infoTimerID = setTimeout('closeInfoWindows()', infoTimer);			
		}
	}
}

function mouseDownEvent(mapEvent){
	mouseDown = true;
}

function mouseUp(mapEvent){
	mouseDown = false;	
	if(viewChanged == true){
		registerAjaxInvoker();
		viewChanged = false;
	}
}

function infoBoxHover(){
	if (!infoHover){
		infoHover = true;
	}
}

function infoBoxOut(){
	if(infoHover){
		infoHover = false;
	}
}

function getItemFromPushpin(pushpin) {
	var index = pushpin.getText();
	return jQuery('#marker_li_' + index);
}

function createSideBarList(newLocations){
	//Create list of markers
	var firstDepotId = newLocations[0].depotId;
	
	jQuery("#list").empty();
	for (var i = 0; i < newLocations.length; i++) {
		var name = newLocations[i].name;
		var address1 = newLocations[i].address1;
		var address2 = newLocations[i].address2;
		var distance = newLocations[i].distance;
		var destination = address1 + " " + address2;

		var liElement = jQuery('<li></li>')
			.attr('id', "marker_li_" + i)
			.attr('name', 'item_' + newLocations[i].depotId);
		
		var aElement = jQuery('<a></a>')
			.attr('id', "marker_a_" + i)
			.attr('href', "#")
			.attr('title', name)
			.click(function(e) {
				e.preventDefault();
				var item = jQuery(this).closest('li');
				selectItem(item, false);
			});
		
		var markerElement = jQuery('<span></span>')
			.addClass('marker')
			.attr('id', "marker_marker_" + i)
			.html(parseInt(i) + 1);
		
		var detailsStr = '<strong id=marker_strong_' + i + '>' + name + '</strong> '
			+ '(' + distance.toFixed(1) + ' mi.)'
			+ '<br />'
			+ address1;
		if(address2 && address2 != "null" && address2 != "") {
			detailsStr += ', ' + address2;
		}
		
		var detailsElement = jQuery('<span></span>')
			.attr('id', "marker_details_" + i)
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
				var marker = getMarkerFromItem(jQuery(this));
				highlightMarker(marker);
				var pushpin = getPushpinFromItem(jQuery(this));
				openInfoWindow(pushpin);
			}, function() {
				//Set normal image
				jQuery(this).removeClass('active');
				var marker = getMarkerFromItem(jQuery(this));
				unHighlightMarker(marker);
			});
		
		jQuery('#list').append(liElement);
		
		//Add hidden fields to item
		var hiddenFields = createDepotHiddenFields(newLocations[i]);
		for(var k = 0; k < hiddenFields.length; k++) {
			jQuery(hiddenFields[k]).appendTo(liElement);
		}
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
	
	//Select first item if none previously select. Otherwise, check if previously selected
	//is still in list. If so, reselect
	var newSelect = null;
	if(selectedItemId != null) {
		newSelect = getItemFromId(selectedItemId);
	}
	if(newSelect == null) {
		newSelect = jQuery('#list li:first-child');
	}
	
	selectItem(newSelect, false);
}

function createDepotHiddenFields(depot) {
	var fields = new Array();
	
	fields.push(createHiddenField('depotIdHF', depot.depotId));
	fields.push(createHiddenField('nameHF', depot.name));
	fields.push(createHiddenField('latHF', depot.geolat));
	fields.push(createHiddenField('longHF', depot.geolong));
	fields.push(createHiddenField('address1HF', depot.address1));
	fields.push(createHiddenField('address2HF', depot.address2));
	fields.push(createHiddenField('cityHF', depot.city));
	fields.push(createHiddenField('stateHF', depot.state));
	
	return fields;
}

function createHiddenField(name, value) {
	var val = value == null || value == 'null' ? '' : value;
	return jQuery('<input type="hidden"></input>')
		.attr('name', name)
		.val(val);
}

function getItemFromId(id) {
	var item = jQuery('[name=item_' + id + ']');
	if (item.length > 0) {
		return item;
	}
	else {
		return null;
	}
}

function selectItem(item, showBubble) {
	var nameAttr = new Array();
	nameAttr = item.attr('name').split("_");
	var depotId = nameAttr[1];
	
	//fetch additional data for the depotId
	new Ajax.Request('/kpointC/DepotSearch.action',
		{
			method: 'post',
			parameters: 'depotId=' + depotId + '&isSelectedKpFromMapList=true',
			onFailure: function(transport) {
				alert(eval("(" + transport.responseText + ")"));
			},
			onSuccess: function(depotJson) {
				//var additionalDepotInfoJsonObj = eval("(" + depotJson.responseText + ")");
				//create AdditionalDepotInfo object to hold additional depot data
				//additionalDepotInfo = new AdditionalDepotInfo();
				//setAdditionDepotInfo(additionalDepotInfo, additionalDepotInfoJsonObj);
				updateCurrentDepotFields(item);
				
				if(showBubble) {
					var pushpin = getPushpinFromItem(item);
					openInfoWindow(pushpin);
				}
			}	
	    });
	//-------------------------------------
	
	var marker = getMarkerFromItem(item);
	
	//Set currently selected id variable
	selectedItemId = depotId;
	
	//Remove currently selected and unhighlight marker
	item.closest('#list').find('li')
		.removeClass('selected')
		.removeClass('active')
		.each(function() {
			var curMarker = getMarkerFromItem(jQuery(this));
			unHighlightMarker(curMarker);
			unselectMarker(curMarker);
		});
	
	//Highlight item
	item.addClass('selected');
	item.addClass('active');
	
	//Highlight marker
	highlightMarker(marker);
	selectMarker(marker);
	
	jQuery(save).removeClass('buttonDisabled').addClass('button');
	jQuery(save).removeAttr('disabled');
}

function getMarkerFromItem(item) {
	var index = parseInt(item[0].id.replace('marker_li_', ''));
	return jQuery('.marker_img_' + index);
}

function highlightMarker(marker) {
	marker.removeClass("marker");
	marker.addClass("markerActive");
}

function unHighlightMarker(marker) {
	if (!marker.hasClass("selected")) {
		marker.addClass("marker");
	}
	marker.removeClass("markerActive");
}

function selectMarker(marker) {
	marker.addClass("selected");
}

function unselectMarker(marker) {
	marker.addClass("marker");
	marker.removeClass("selected");
}

//add +1 for map entities list due to search result pin
function getPushpinFromItem(item) {
	var index = parseInt(item[0].id.replace('marker_li_', ''));
	return map.entities.get(index+1);
}

function openInfoWindow(pushpin){	
	var item = getItemFromPushpin(pushpin);
	var nameDiv = jQuery('<div></div>')
	.addClass('directionName')
	.html(item.find('[name=nameHF]').val());
	var add1Div = jQuery('<div></div>').html(item.find('[name=address1HF]').val());
	var add2Div = jQuery('<div></div>').html(item.find('[name=address2HF]').val());
	
	var signUpLink = jQuery('<a></a>')
	.html('Sign Up to ship to this KinekPoint')
	.attr('href', '/kpointC/Register.action')
	.addClass('directionLink');

	//depot specific 
	var wrapper;
	
	if (getRequestedPageName().toLowerCase() != "depotsearch.action") {
		var saveKPLink = jQuery('<a></a>')
			.html('Save this KinekPoint')
			.attr('href', '/kpointC/ChooseDefaultKinekPoint.action?setDefault=&depotId=' + item.find('[name=depotIdHF]').val())
			.addClass('saveLink');
		
		wrapper = jQuery('<div style=\"text-align: left;\"></div>')
			.append(nameDiv)
			.append(add1Div)
			.append(add2Div)
			.append('<br />')
			.append(saveKPLink);
	}
	else {
		var signUpLink = jQuery('<a></a>')
			.html('Sign Up to ship to this KinekPoint')
			.attr('href', '/kpointC/Register.action')
			.addClass('directionLink');
		
		wrapper = jQuery('<div></div>')
			.append(nameDiv)
			.append(add1Div)
			.append(add2Div)
			.append('<br />')
			.append(signUpLink);
	}
	
	var ibDescription = document.getElementById('ibDescription');
	ibDescription.innerHTML = wrapper[0].innerHTML;
	
	//rest of my junk
	clearTimeout(infoTimerID);
	var pix = map.tryLocationToPixel(pushpin.getLocation(), Microsoft.Maps.PixelReference.control);

	
	var infoBox = document.getElementById('infoBox');
	infoBox.style.top = (pix.y - 60) + "px";
	infoBox.style.left = (pix.x + 15) + "px";
	infoBox.style.visibility = "visible";
	document.getElementById('map').appendChild(infoBox);
}

function closeInfoWindows() {
	if(!infoHover){
		var infoBox = document.getElementById('infoBox');
		infoBox.style.visibility = "hidden";
	}
	else{
		infoTimerID = setTimeout('closeInfoWindows()', infoTimer);
	}
}

function directionLink_onClick() {
	closeInfoWindows();
	var item = getItemFromId(selectedItemId);
	origin = document.getElementById("userProfileAddress").value;
	showDirections(item);
}



function setOrigin(address) {
	origin = address;
}

function setOriginCity(city) {
	originCity = city;
}

function showDirectionsTab() {
	//Ensure proper tab is displayed
	if (jQuery('#resultsTab').is('.active')) {
		jQuery('#resultsTab').removeClass('active');
		jQuery('#results').hide();
		jQuery('#directionsTab').addClass('active');
		jQuery('#directions').show();
	}
	
	//Get destination coordinates
	var destination = jQuery('#directionsDestination').val();
	
	//Swap maps
	jQuery('#map').hide();
	jQuery('#direction_map').show();
	jQuery('#legend').hide();
	
	jQuery("#directions").html("");
	//Instantiate and setup directions map
	kpdir = new KinekPointMapDirection('direction_map', 'directions');	
	kpdir.setUserOrigin(origin);
	kpdir.setUserOriginCity(originCity);
	kpdir.setKpDestination(destination);
	kpdir.getKinekPointDirectionMap();
	

}

function showDirections(item) {
	
	//Ensure proper tab is displayed
	if (jQuery('#resultsTab').is('.active')) {
		jQuery('#resultsTab').removeClass('active');
		jQuery('#results').hide();
		jQuery('#directionsTab').addClass('active');
		jQuery('#directions').show();
	}
	
	if (getRequestedPageName().toLowerCase() != "depotsearch.action") {
		//Get destination coordinates
		var destination = item.find('[name=address1HF]').val() + ', ' + item.find('[name=cityHF]').val() + ', ' + item.find('[name=stateHF]').val();
		
		//Swap maps
		jQuery('#map').hide();
		Microsoft.Maps.Events.removeHandler(registerHandler);
		jQuery('#direction_map').show();
		jQuery('#legend').hide();
		
		jQuery("#directions").html("");
		//Instantiate and setup directions map
		kpdir = new KinekPointMapDirection('direction_map', 'directions');	
		kpdir.setUserOrigin(origin);
		kpdir.setUserOriginCity(originCity);
		kpdir.setKpDestination(destination);
		kpdir.getKinekPointDirectionMap();
	}
	else {
		closeInfoWindows();
	}
	
	//clean the start address;
	if (getRequestedPageName().toLowerCase() != "depotsearch.action") {
		document.getElementById("startAddressTxt").value = "";
		document.getElementById("startAddress").style.display = 'block';
	}
}

function hideDirections() {
	//Ensure proper tab is displayed
	if(jQuery('#directionsTab').is('.active')) {
		jQuery('#directionsTab').removeClass('active');
		jQuery('#directions').hide();
		jQuery('#resultsTab').addClass('active');
		jQuery('#results').show();
	}
	
	//Clear previous directions
	jQuery('#directions').html('');
	
	//Swap maps
	jQuery('#map').show();
	jQuery('#direction_map').hide();
	jQuery('#legend').show();
	Microsoft.Maps.Events.addHandler(map, "viewchangeend", registerAjaxInvoker);
	
	//Refresh kp map
	//registerAjaxInvoker();
}


//format times from hh:mm:ss (24 hour clock) to [h]h:mm [am|pm] (12 hour clock)
function formatDepotTime(time) {
	var timeArray = time.split(':');
	var hour = parseInt(timeArray[0], 10);
	var minute = timeArray[1];
	var timeOfDay = '';
	
	if(hour >= 12) {
		if(hour > 12) // 12PM
			hour -= 12;
		timeOfDay = 'PM';
	}
	else {
		if(hour == 0) // 12AM
			hour += 12;
		timeOfDay = 'AM';
	}
	return (hour + ':' + minute + ' ' + timeOfDay);
}


function updateCurrentDepotFields(item) {
	//var additionalDepotInfo = getAdditionalDepotInfo();
	
	if(!item) {
		jQuery('#detailsBody').hide();
		jQuery('div.morewrapper').hide();
		jQuery('#noResultsBody').show();
		return;
	}
	else {
		jQuery('#detailsBody').show();
		jQuery('div.morewrapper').show();
		jQuery('#noResultsBody').hide();
	}


	jQuery('#depotId').val(item.find('[name=depotIdHF]').val());
	jQuery('#nameField').html(item.find('[name=nameHF]').val());
	jQuery('#nameField2').html(item.find('[name=nameHF]').val());
	jQuery('#address1Field').html(item.find('[name=address1HF]').val());
	jQuery('#address2Field').html(item.find('[name=address2HF]').val());
	jQuery('#cityField').html(item.find('[name=cityHF]').val());
}