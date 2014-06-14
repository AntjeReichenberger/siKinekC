MarkerEvent=function(){
 
 this.registerEventHandler=function(element,eventHandler,callback){
  //console.log(element)
	if (element.addEventListener){		
		element.addEventListener(eventHandler, callback, false);
	}
	if (element.attachEvent){
		element.attachEvent("on"+eventHandler, callback);
	}
  }
  
  this.getTarget=function(e){
  	var targ;
	if (!e) var e = window.event; // for IE
	if (e.target) targ = e.target; //For Gecko type browsers
	else if (e.srcElement) targ = e.srcElement; //for Safari
	if (targ.nodeType == 3) //for Safari
		targ = targ.parentNode;

	return targ;	
  }
  
}  