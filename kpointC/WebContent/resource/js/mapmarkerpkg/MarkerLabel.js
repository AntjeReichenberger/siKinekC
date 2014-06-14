function LabelMarker(bounds,image,text,map){
	this.bounds_=bounds;
	this.image_=image.src;
	this.text_=text;
	this.map_=map;	
	
	this.div_=null;
	
	this.setMap(map);
	
}
    
  LabelMarker.prototype=new google.maps.OverlayView();

  LabelMarker.prototype.onAdd=function(){
	
	var markerObj=new MarkerElement();
	markerObj.setId(elementId);
	if(this.text_=="H")
		markerObj.setImageDimension(27,42);
	else
		markerObj.setImageDimension(31,26);
	markerObj.setImagePath(this.image_);
	markerObj.setLabel(this.text_);
	
	var marker=markerObj.createMarker(); //return marker as div element
	var imageHolder=markerObj.createImageHolder(); //image holder will be created for marker
	var labelHolder=markerObj.createLabelHolder(); //text label will created
	
	marker.appendChild(imageHolder);
	marker.appendChild(labelHolder);
	
	//if home then zIndex will be greater value
	if(this.text_=="H")marker.style.zIndex=1000;
	
	//console.log("next id: "+elementId);
	elementId++; //elementId must be set to 0 if map is moved
	this.div_=marker;
	
	var panes=this.getPanes();
	panes.overlayImage.appendChild(marker);

  };
  
  LabelMarker.prototype.draw=function(){
    var overlayProjection = this.getProjection();

	var position = overlayProjection.fromLatLngToDivPixel(this.bounds_.getCenter());	
	//console.log("position "+position);
	
	var div = this.div_;
  
	if(this.text_=="H")
		div.style.left = (position.x-(27/2)) + 'px';
	else
		div.style.left = (position.x-(31/2)) + 'px';
	div.style.top = (position.y-26) + 'px';
	//div.style.width = (position.x - position.x) + 'px';
	//div.style.height = (position.y - position.y) + 'px';	   	
	
  };
  
  LabelMarker.prototype.onRemove = function() {
	this.div_.parentNode.removeChild(this.div_);	
	this.div_ = null;
  };