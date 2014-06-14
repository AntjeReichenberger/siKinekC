var markers = [];
var homeMarker = null;
var homeGeomatry = null;
var origin = "";
var save;
var infoWindow = null;
var latlngs = [];
var newLocations = null;
var infoWindowList = [];
var infoWinIndex = 0;
var expanding = false;
var selectedItem=null;
var kpListElement=null;
var selectedItemId = null;
var showBubble = true;
var additionalDepotInfo = null;

var scrollPaneSettings = {							
		scrollbarOnLeft: true,
		showArrows: true, 
		scrollbarWidth: 17,
		arrowSize: 17,
		contentWidth: 0
	};

var overlay;

var isIE = false;
var elementId = 0;	 
var eventObj;

var marker_image = new Image();
marker_image.src = "resource/icons/mapmarker_wide_grey.png";	
var marker_image_act = new Image();
marker_image_act.src = "resource/icons/mapmarker_wide_red.png";		
var marker_home = new Image();
marker_home.src = "resource/icons/marker_home.png";

var requestedPageName="";

function loadInitialCordinate(kpAddress) {
	var homeCordinate = getCoordinate(kpAddress, loadGoogleMap);		
	origin = kpAddress;
}

function loadInitialCordinateForLoginUser(kpAddress,userAddress,saveButton) {
	var homeCordinate = getCoordinate(kpAddress, loadGoogleMap);		
	origin = userAddress;
	save = saveButton;
}

function setOrigin(address){
	origin=address;
}

function loadGoogleMap(cord) {
	//If border search, zoom out to see entire province
	var zoomLevel = 11;
	if(isBorderSearch()) {
		zoomLevel = 5;
	}
	else {
		switch (jQuery('#radius').val()) {
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
	
	homeGeomatry = cord;
    gmap= new google.maps.Map(document.getElementById("map"),{
		center:new google.maps.LatLng(cord.location.lat(),cord.location.lng()),
		zoom: zoomLevel,
		mapTypeId:'roadmap',
		mapTypeControlOptions:{style: google.maps.MapTypeControlStyle.DROPDOWN_MENU}	
	});

	var browserName=navigator.appName;
	if (browserName=="Netscape"){
		isIE=false;
	}else{
		isIE=true;
	}
 
	//search nearby depots
	//register center_changed event handler so if center of map is changed then it will populate new
	//max,min lat and lng and invoke ajax to get nearest depots	

	jQuery('#searchPointLatitude').val(cord.location.lat());
	jQuery('#searchPointLongitude').val(cord.location.lng());
	
	jQuery('#maxLongitude').val(cord.viewport.getNorthEast().lng());
	jQuery('#maxLatitude').val(cord.viewport.getNorthEast().lat());
	jQuery('#minLongitude').val(cord.viewport.getSouthWest().lng());
	jQuery('#minLatitude').val(cord.viewport.getSouthWest().lat());		
	
	overlay = new google.maps.OverlayView();
    overlay.draw = function() {};
    overlay.setMap(gmap);
	
	google.maps.event.addListener(gmap, "idle", registerAjaxInvoker);
	google.maps.event.addListener(gmap, 'zoom_changed', function() {
	    closeInfoWindows();
	});
}

function registerAjaxInvoker() {
	if(gmap.getBounds()!=null && gmap.getBounds()!='undefined' && jQuery('#directions').is(':hidden')){ //first load it will be undefined	
		jQuery('#maxLongitude').val(gmap.getBounds().getNorthEast().lng());
		jQuery('#maxLatitude').val(gmap.getBounds().getNorthEast().lat());
		jQuery('#minLongitude').val(gmap.getBounds().getSouthWest().lng());
		jQuery('#minLatitude').val(gmap.getBounds().getSouthWest().lat());		
		invoke($('findDepots'), 'findDepots', update);
	}
}

function invoke(form, event, handler) {
	var params = Form.serialize(form, {submit:event});
	new Ajax.Request(form.action, {method:'post', parameters:params, onSuccess:handler});
}

//requested page name depotsearch_result or adb_choosedefaultkinekpoint_result
function setRequestedPageName(pageName){
	requestedPageName=pageName;
}

function getRequestedPageName(){return requestedPageName;}

/** Function that handles the update when the async request returns. */
function update(xhr) {
	var output = eval("(" + xhr.responseText + ")");
	
	var currentPagePath = window.location.pathname;	
	var currentPageNameWithSessionId = currentPagePath.substring(currentPagePath.lastIndexOf('/') + 1);
	
	//if page contains session id then remove the session id.
	var currentPageName=currentPageNameWithSessionId.split(";")[0];	
	setRequestedPageName(currentPageName);

	if(!expanding) {
		cleanKinekPointResultLabels();
	}
	
	//closeInfoWindows();	
	jQuery("#prospects").hide();
	
	newLocations = new Array();
	if(output.depots.length == 0) {
		jQuery('#noKp').show();
		gmap.setZoom(gmap.getZoom()-1);
		showExpandingMessage();
		expanding = true;
		return;
	}
	else {
		if(!expanding) {
			jQuery('#results').addClass('found');
			jQuery('#foundKp').show();
		}
		else {
			jQuery('#results').removeClass('found');
			hideExpandingMessage();
		}
		
		jQuery('#listWrapper1').jScrollPaneRemove();
		jQuery('#listWrapper1').jScrollPane(scrollPaneSettings);
		
		jQuery("#depotCount").text(output.depots.length);		
		expanding = false;
		
		for(var i = 0; i < output.depots.length; i++) {
			newLocations[i] = output.depots[i];
		}
	}
		
	jQuery('#errors').html("");				
	processDepotsOnMap(newLocations);
}//end update

function processDepotsOnMap(newLocations){
	elementId=0;
	clearMarkers();
	
	var homeLatLng = new google.maps.LatLng(homeGeomatry.location.lat(),homeGeomatry.location.lng());

	//create Home marker
	var bounds = new google.maps.LatLngBounds(homeLatLng);
	homeMarker = new LabelMarker(bounds,marker_home,"H",gmap);
	
	//create marker on the map
	for ( var i = 0; i < newLocations.length; i++) {
		//Get lat&long from array
		var latlng = new google.maps.LatLng(newLocations[i].geolat,newLocations[i].geolong);
		latlngs[i]=latlng;
		var bounds = new google.maps.LatLngBounds(latlng);	
		
		var imgSrc = "";
		if(selectedItemId!=null){
			imgSrc = selectedItemId == newLocations[i].depotId ? marker_image_act : marker_image;
		}else{		
			imgSrc = i == 0 ? marker_image_act : marker_image;
		}
				
		var lbl=i+1;
		var lblMarker=new LabelMarker(bounds,imgSrc,lbl,gmap);
		markers.push(lblMarker);		
	}
	
    eventObj=new MarkerEvent();
	eventObj.registerEventHandler(document.getElementById("map"),"mouseover",mapHover);
	eventObj.registerEventHandler(document.getElementById("map"),"mouseout",mapBlur);
	eventObj.registerEventHandler(document.getElementById("map"),"click",mapClick);
	
	//create sidebar list
	createSideBarList(newLocations);
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
	
	var firstDepotId=newLocations[0].depotId;
	
	jQuery("#list").empty();
	jQuery(markers).each(function(i, marker) {
		var marker = markers[i];		
		var name = newLocations[i].name;
		var address1 = newLocations[i].address1;
		var address2 = newLocations[i].address2;
		var distance = newLocations[i].distance;		
		var destination = address1 + " " + address2;

		var liElement = jQuery('<li></li>')
			.attr('id', "marker_li_"+i)
			.attr('name', 'item_'+newLocations[i].depotId);		
		
		var aElement = jQuery('<a></a>')
			.attr('id', "marker_a_"+i)
			.attr('href', "#")
			.attr('title', name)
			.click(function(e) {
				e.preventDefault();
				var item = jQuery(this).closest('li');
				selectItem(item, true);
			});
		
		var markerElement = jQuery('<span></span>')
			.addClass('marker')
			.attr('id', "marker_marker_"+i)
			.html(parseInt(i)+1);
		
		var detailsStr = '<strong id=marker_strong_'+i+'>'+ name + '</strong> '
			+ '(' + distance.toFixed(1) + ' mi.)'
			+ '<br />'
			+ address1;
		if(address2 && address2 != "null" && address2 != "") {
			detailsStr += ', ' + address2;
		}
		
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
		
		
		//Add hidden fields to item		
		var hiddenFields = createDepotHiddenFields(newLocations[i]);
		for(var k = 0; k < hiddenFields.length; k++) {
			jQuery(hiddenFields[k]).appendTo(liElement);
		}
	});

	//Add fancy scroll bar
	jQuery('#listWrapper1').jScrollPane(scrollPaneSettings);

	//Add first and last classes
	jQuery('ul li:first-child, ol li:first-child').addClass('first');
	jQuery('ul li:last-child, ol li:first-child').addClass('last');	
	
	//Select first item if none previously select. Otherwise, check if previously selected
	//is still in list. If so, reselect
	var newSelect = null;
	//var showBubble = false;
	if(selectedItemId != null) {
		newSelect = getItemFromId(selectedItemId);
		//showBubble = true;
	}
	if(newSelect == null) {
		newSelect = jQuery('#list li:first-child');
		//showBubble = false;
	}
	
	selectItem(newSelect, showBubble);
	showBubble = false;
	
	//Register hover event handlers
	eventObj.registerEventHandler(document.getElementById("list"),"mouseover",listHover);
	eventObj.registerEventHandler(document.getElementById("list"),"mouseout",listBlur);
	
}

//helper methods
function getCoordinate(address, callback){
	var geocoder=new google.maps.Geocoder();
	geocoder.geocode({address:address},function(result,status) {
		if (status==google.maps.GeocoderStatus.OK) {
			callback(result[0].geometry);
		}		
	});
}

function clearMarkers(){
	if(homeMarker != null && markers != null) {
		homeMarker.setMap(null);
		for(var i = 0; i < markers.length; i++){			
			markers[i].setMap(null);
		}
		homeMarker = null;
		markers = new Array();
	}
}

function cleanKinekPointResultLabels(){
	jQuery('#foundKp').hide();
	jQuery('#noKp').hide();
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
		//showBubble = true;
	}
}   

function createDepotHiddenDepotIdField(depot){
	createHiddenField('depotIdHF', depot.depotId);
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

function updateCurrentDepotFields(item) {
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
}

function showDirections(item) {
	//Ensure proper tab is displayed
	if(jQuery('#resultsTab').is('.active')) {
		jQuery('#resultsTab').removeClass('active');
		jQuery('#results').hide();
		jQuery('#directionsTab').addClass('active');
		jQuery('#directions').show();
	}
	
	if(getRequestedPageName().toLowerCase()!="depotsearch.action"){
		//Get destination coordinates
		var destination = item.find('[name=address1HF]').val() + ', ' + item.find('[name=cityHF]').val() + ', ' + item.find('[name=stateHF]').val(); 
	
		jQuery("#directions").html("");
		//Instantiate and setup directions map
		kpdir = new KinekPointMapDirection('direction_map', 'directions');
		kpdir.setUserOrigin(origin);
		kpdir.setKpDestination(destination);
		kpdir.getKinekPointDirectionMap();
	
		//Swap maps
		jQuery('#map').hide();
		jQuery('#direction_map').show();
		jQuery('#legend').hide();
		
		if(document.getElementById("notLoginMsg")!= null){
			document.getElementById("notLoginMsg").style.display='none';
		}
		
	}
	else{
		if(document.getElementById("notLoginMsg")!= null){
			document.getElementById("notLoginMsg").style.display='block';
		}
		closeInfoWindows();
	}
	
	//clean the start address;
	if(getRequestedPageName().toLowerCase()!= "depotsearch.action"){
		document.getElementById("startAddressTxt").value="";	
		document.getElementById("startAddress").style.display='block';
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
	
	//Refresh kp map
	registerAjaxInvoker();
}

function createHoursTable(){
var hoursTable = jQuery('<table id="hours"></table>').addClass("superplain notop");	
	
	var mondayRow = jQuery('<tr></tr>').addClass("nopadding");
	var mondayLbl = jQuery('<td></td>').html("Mon.");
	var mondayValue;
	if(getAdditionalDepotInfo().getMondayClosed() == "true"){
		mondayValue = "Closed";	
	}
	else{
		mondayValue = getAdditionalDepotInfo().getMondayStart() + ' - ' + getAdditionalDepotInfo().getMondayEnd();
	}
	var mondayTime = jQuery('<td id="mondayTimeField"></td>').html(mondayValue);
	mondayRow.append(mondayLbl).append(mondayTime);
	hoursTable.append(mondayRow);
	
	var tuesdayRow = jQuery('<tr></tr>').addClass("nopadding");;
	var tuesdayLbl = jQuery('<td></td>').html("Tue.");	
	var tuesdayValue;
	if(getAdditionalDepotInfo().getTuesdayClosed() == "true"){
		tuesdayValue = "Closed";	
	}
	else{
		tuesdayValue = getAdditionalDepotInfo().getTuesdayStart() + ' - ' + getAdditionalDepotInfo().getTuesdayEnd();
	}
	var tuesdayTime = jQuery('<td id="tuesdayTimeField"></td>').html(tuesdayValue);
	tuesdayRow.append(tuesdayLbl).append(tuesdayTime);
	hoursTable.append(tuesdayRow);
	
	var wednesdayRow = jQuery('<tr></tr>').addClass("nopadding");
	var wednesdayLbl = jQuery('<td></td>').html("Wed.");	
	var wednesdayValue;
	if(getAdditionalDepotInfo().getWednesdayClosed() == "true"){
		wednesdayValue = "Closed";	
	}
	else{
		wednesdayValue = getAdditionalDepotInfo().getWednesdayStart() + ' - ' + getAdditionalDepotInfo().getWednesdayEnd();
	}
	var wednesdayTime = jQuery('<td id="wednesdayTimeField"></td>').html(wednesdayValue);
	wednesdayRow.append(wednesdayLbl).append(wednesdayTime);
	hoursTable.append(wednesdayRow);
	
	
	var thursdayRow = jQuery('<tr></tr>').addClass("nopadding");
	var thursdayLbl = jQuery('<td></td>').html("Thu.");	
	var thursdayValue;
	if(getAdditionalDepotInfo().getThursdayClosed() == "true"){
		thursdayValue = "Closed";	
	}
	else{
		thursdayValue = getAdditionalDepotInfo().getThursdayStart() + ' - ' + getAdditionalDepotInfo().getThursdayEnd();
	}
	var thursdayTime = jQuery('<td id="thursdayTimeField"></td>').html(thursdayValue);
	thursdayRow.append(thursdayLbl).append(thursdayTime);
	hoursTable.append(thursdayRow);
	
	
	var fridayRow = jQuery('<tr></tr>').addClass("nopadding");
	var fridayLbl = jQuery('<td></td>').html("Fri.");	
	var fridayValue;
	if(getAdditionalDepotInfo().getFridayClosed() == "true"){
		fridayValue = "Closed";	
	}
	else{
		fridayValue = getAdditionalDepotInfo().getFridayStart() + ' - ' + getAdditionalDepotInfo().getFridayEnd();
	}
	var fridayTime = jQuery('<td id="fridayTimeField"></td>').html(fridayValue);
	fridayRow.append(fridayLbl).append(fridayTime);
	hoursTable.append(fridayRow);
	
	var saturdayRow = jQuery('<tr></tr>').addClass("nopadding");
	var saturdayLbl = jQuery('<td></td>').html("Sat.");	
	var saturdayValue;
	if(getAdditionalDepotInfo().getSaturdayClosed() == "true"){
		saturdayValue = "Closed";	
	}
	else{
		saturdayValue = getAdditionalDepotInfo().getSaturdayStart() + ' - ' + getAdditionalDepotInfo().getSaturdayEnd();
	}
	var saturdayTime = jQuery('<td id="saturdayTimeField"></td>').html(saturdayValue);
	saturdayRow.append(saturdayLbl).append(saturdayTime);
	hoursTable.append(saturdayRow);
	
	var sundayRow = jQuery('<tr></tr>').addClass("nopadding");
	var sundayLbl = jQuery('<td></td>').html("Sun.");	
	var sundayValue;
	if(getAdditionalDepotInfo().getSundayClosed() == "true"){
		sundayValue = "Closed";	
	}
	else{
		sundayValue = getAdditionalDepotInfo().getSundayStart() + ' - ' + getAdditionalDepotInfo().getSundayEnd();
	}
	var sundayTime = jQuery('<td id="sundayTimeField"></td>').html(sundayValue);
	sundayRow.append(sundayLbl).append(sundayTime);
	hoursTable.append(sundayRow);
	
	return hoursTable;
}

function openInfoWindow(item){
	var latlng = new google.maps.LatLng(
			item.find('[name=latHF]').val(), 
			item.find('[name=longHF]').val());
	
	var nameDiv = jQuery('<div></div>')
		.addClass('directionName')
		.html(item.find('[name=nameHF]').val());
	var add1Div = jQuery('<div></div>').html(item.find('[name=address1HF]').val());
	var add2Div = jQuery('<div></div>').html(item.find('[name=address2HF]').val());
	
	var hoursDiv = jQuery('<div></div>')
	.addClass('directionName')
	.html("Hours");
	
	var hoursTable = createHoursTable();
	var rates = getAdditionalDepotInfo().getKPPackageRates();
	
	var lowestRate = 0; 
	for(var i = 0; i < rates.length; i++){
		if(i == 0){
			lowestRate = parseFloat(rates[i].feeOverride).toFixed(2);
		}
		else{
			if(lowestRate > parseFloat(rates[i].feeOverride)){
				lowestRate = parseFloat(rates[i].feeOverride).toFixed(2);
			}	
		}
	}
	
	var ratesDiv = jQuery('<div></div>')
	.addClass('directionName')
	.html("Price per package");
	
	var rateDiv = jQuery('<div></div>')
	.html("Rates as low as $" + lowestRate);
	
	var wrapper;
	if (getRequestedPageName().toLowerCase() != "wpfindkinekpoint.action") {

		var saveKPLink = jQuery('<input></input>')
			.attr('type','submit')
			.attr('id','saveKPButton')
			.attr('name','setDefault')
			.attr('value',"Add this KinekPoint")			
			.addClass('button');

		var saveKPDiv = jQuery('<div></div>')
			.append(saveKPLink).css("text-align","center");

		
		wrapper = jQuery('<div></div>')
			.append(nameDiv)
			.append(add1Div)
			.append('<br />')
			//.append(add2Div)
			.append(hoursDiv)
			.append(hoursTable)
			.append(ratesDiv)
			.append(rateDiv)
			.append('<br />')
			//.append(directionsLink)
			.append(saveKPDiv);	
	}
	else {
		var signUpImage = jQuery('<img></img>').attr('src','resource/wordpress/images/SignUpNowSmall.png');
		
		var signUpLink = jQuery('<a></a>')
			.append(signUpImage)
			.attr('href', '/sign-up')
			.addClass('directionLink');
			
		var signUpDiv = jQuery('<div></div>').append(signUpLink).css("text-align","center");

		
		wrapper = jQuery('<div></div>')
			.append(nameDiv)
			.append(add1Div)
			//.append(add2Div)
			.append(hoursDiv)
			.append(hoursTable)
			.append(ratesDiv)
			.append(rateDiv)
			.append('<br />')
			.append(signUpDiv);	
	}
	closeInfoWindows();	
	
	var test= overlay.getProjection().fromLatLngToDivPixel(latlng);
	latlng = overlay.getProjection().fromDivPixelToLatLng(new google.maps.Point(test.x, test.y-26));
	
	
	var window = new InfoBubble();
	window.setContent(wrapper[0]);
	window.setPosition(latlng);	
	window.setMaxWidth(250);
	window.setMinWidth(200);
	//window.setOptions({disableAutoPan:true});
	window.open(gmap);
	
	showBubble = false;
	infoWindowList[infoWinIndex++] = window;
}

function closeInfoWindows() {
	for(var i = 0; i < infoWindowList.length; i++) {
		infoWindowList[i].close();
	}
	infoWindowList = new Array();
	infoWinIndex = 0;
}

function map_OnClick() {
	//showBubble = false;
	//closeInfoWindows();
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
	var index = parseInt(marker[0].id.replace('marker_img_', '')) - 1;
	return jQuery('#marker_li_'+index);
}

function getMarkerFromItem(item) {
	var index = parseInt(item[0].id.replace('marker_li_', '')) + 1;
	return jQuery('#marker_img_'+index);
}

function highlightMarker(marker) {
	marker.attr('src', marker_image_act.src);
}

function unhighlightMarker(marker) {
	marker.attr('src', marker_image.src);
}

function selectItem(item, showBubble) {
	
	var nameAttr=new Array();
	nameAttr=item.attr('name').split("_");
	var depotId=nameAttr[1];
	
	//fetch additional data for the depotId
	new Ajax.Request('/kpointC/DepotSearch.action',
		{
			method: 'post',
			parameters: 'depotId=' + depotId + '&isSelectedKpFromMapList=true',
			onFailure: function(transport) {
				//alert(eval("(" + transport.responseText + ")"));
			},
			onSuccess: function(depotJson) {
				var additionalDepotInfoJsonObj = eval("(" + depotJson.responseText + ")");
				//create AdditionalDepotInfo object to hold additional depot data
				additionalDepotInfo = new AdditionalDepotInfo();
				setAdditionDepotInfo(additionalDepotInfo, additionalDepotInfoJsonObj);
				updateCurrentDepotFields(item);
				
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
						curMarker.attr('src', marker_image.src);
						curMarker.removeClass('selected');
					});
				
				//Highlight item
				item.addClass('selected');
				item.addClass('active');

				//Highlight marker
				highlightMarker(marker);
				marker.addClass('selected');
				
				if(showBubble) {
					//Open info window
					openInfoWindow(item);
				}
				jQuery(save).removeClass('buttonDisabled').addClass('button');
				jQuery(save).removeAttr('disabled');
			}	
	    });
}

function setAdditionDepotInfo(additionalDepotInfo,depot){
	
	if(depot.creditCards.length>0){
		additionalDepotInfo.setCreditCards(depot.creditCards.join(', '));
	}
	additionalDepotInfo.setDutyAndTax(depot.dutyAndTax);
	additionalDepotInfo.setEmail(depot.email);
	additionalDepotInfo.setExtraInfo(depot.extraInfo);
	
	if(depot.features.length>0){
		additionalDepotInfo.setFeatures(depot.features.join(', '));
	}
	
	if(depot.languages.length>0){
		additionalDepotInfo.setLanguages(depot.languages.join(', '));
	}
	
	additionalDepotInfo.setName(depot.name);
	
	additionalDepotInfo.setMondayStart(formatDepotTime(depot.operatingHours.mondayStart));
	additionalDepotInfo.setMondayEnd(formatDepotTime(depot.operatingHours.mondayEnd));
	additionalDepotInfo.setMondayClosed(depot.operatingHours.closedMonday);
	
	additionalDepotInfo.setTuesdayStart(formatDepotTime(depot.operatingHours.tuesdayStart));
	additionalDepotInfo.setTuesdayEnd(formatDepotTime(depot.operatingHours.tuesdayEnd));
	additionalDepotInfo.setTuesdayClosed(depot.operatingHours.closedTuesday);
	
	additionalDepotInfo.setWednesdayStart(formatDepotTime(depot.operatingHours.wednesdayStart));
	additionalDepotInfo.setWednesdayEnd(formatDepotTime(depot.operatingHours.wednesdayEnd));
	additionalDepotInfo.setWednesdayClosed(depot.operatingHours.closedWednesday);
	
	additionalDepotInfo.setThursdayStart(formatDepotTime(depot.operatingHours.thursdayStart));
	additionalDepotInfo.setThursdayEnd(formatDepotTime(depot.operatingHours.thursdayEnd));
	additionalDepotInfo.setThursdayClosed(depot.operatingHours.closedThursday);
	
	additionalDepotInfo.setFridayStart(formatDepotTime(depot.operatingHours.fridayStart));
	additionalDepotInfo.setFridayEnd(formatDepotTime(depot.operatingHours.fridayEnd));
	additionalDepotInfo.setFridayClosed(depot.operatingHours.closedFriday);
	
	additionalDepotInfo.setSaturdayStart(formatDepotTime(depot.operatingHours.saturdayStart));
	additionalDepotInfo.setSaturdayEnd(formatDepotTime(depot.operatingHours.saturdayEnd));
	additionalDepotInfo.setSaturdayClosed(depot.operatingHours.closedSaturday);
	
	additionalDepotInfo.setSundayStart(formatDepotTime(depot.operatingHours.sundayStart));
	additionalDepotInfo.setSundayEnd(formatDepotTime(depot.operatingHours.sundayEnd));
	additionalDepotInfo.setSundayClosed(depot.operatingHours.closedSunday);
	
	additionalDepotInfo.setHoursInfo(depot.operatingHours.hoursInfo);
	
	if(depot.payMethods.length>0){
		additionalDepotInfo.setPayMethods(depot.payMethods.join(', '));
	}
	additionalDepotInfo.setPhone(depot.phone);
	additionalDepotInfo.setReceivingFee(depot.receivingFee);
	additionalDepotInfo.setSizeAllowance(depot.sizeAllowance);
	additionalDepotInfo.setState(depot.state);
	additionalDepotInfo.setZip(depot.zip);
	
	additionalDepotInfo.setKPPackageRates(depot.kpPackageRates);
	additionalDepotInfo.setKPExtendedStorageRates(depot.kpExtendedStorageRates);
	additionalDepotInfo.setKPSkidRate(depot.kpSkidRate);
}

function getAdditionalDepotInfo(){
	return additionalDepotInfo;	
}

function isBorderSearch() {
	return jQuery('#borderProvinceId').val() != '0';
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
