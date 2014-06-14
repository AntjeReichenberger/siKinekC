	
MarkerElement=function(){

	var eleId=0;
	var width="auto";
	var position="absolute";
	var imgWidth="100%";
	var imgHeight="100%";
	var imgPath="";
	var spanLeft="auto";
	var spanRight="auto";
	var label="New Label";

	this.setId=function(id){
		eleId=id;
	}	
	
	this.setDivWidth=function(w){
		width=w;
	}
		
	this.createMarker=function(){
		var div=document.createElement("div");
		div.setAttribute("id","marker_div_"+eleId);
		div.style.border="none";	
		div.style.width=width;
		div.style.position=position;
		div.style.align="center";
		return div;
	} 	
	
	this.setImageDimension=function(iW,iH){
		imgWidth=iW;
		imgHeight=iH;
	}
	
	this.setImagePath=function(path){
		imgPath=path;
	}
	this.createImageHolder=function(){
		// Create an IMG element and attach it to the DIV.
		var img = document.createElement("img");
		img.src = imgPath;
		img.setAttribute("id","marker_img_"+eleId);
		img.style.width = imgWidth;
		img.style.height = imgHeight;
		img.style.cursor="pointer";
		
		return img;
	}
	
	this.setLabel=function(l){
		if(l!="H")
			label=l;
		else
			label="";
	}
	
	this.createLabelHolder=function(){	
		var span=document.createElement("span");
		var text=document.createTextNode(label);	
		span.appendChild(text);
		span.setAttribute("id","marker_span_"+eleId);
		span.style.position=position;
		span.style.fontWeight="bold";
		span.style.color="white";			
		span.style.left="0";
		if(isIE){
			span.style.left="0%";
		}
		span.style.width="100%";
		span.style.textAlign="center";
		span.style.top="15%";
		span.style.fontSize="11px";
		span.style.cursor="pointer";
		
		return span;
	}
}	

		
			

	
	