function getPopupPos(windowname, control) {
	var pos = find_pos(control);
			
	var left = pos[0] + control.offsetWidth;
	var top = pos[1] - (jQuery('#' + windowname).height()/2);
		
	//works as long as popup width is 300, using windowname.width for some reason gives wrong values?
	if (document.body.offsetWidth < left + 300) {
		left -= (left + 300 - document.body.offsetWidth);
		top = pos[1] + control.offsetHeight;
	}
	
	return [left,top];
}

//calculates the left and top of the passed in element relative to the screen
function find_pos(obj) {
	var curleft = 0;
	var curtop = 0;
	if (obj.offsetParent) {
		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	}
	
	if (typeof pageYOffset != 'undefined') {
        //most browsers
		curleft -= pageXOffset;
		curtop -= pageYOffset;
    }
    else {
        var B = document.body; //IE 'quirks'
        var D = document.documentElement; //IE with doctype
        D= (D.clientHeight)? D : B;

		curleft -= D.scrollLeft;
		curtop -= D.scrollTop;
    }
	
	return [curleft,curtop];
}

function openInfoDialog(windowname, control) {
	var pos = getPopupPos(windowname, control);
	
	jQuery('#' + windowname).dialog({
		position: [pos[0], pos[1]],
		width: 300,
		resizable:false,
		closeText:'hide'
	});
}

function closeInfoDialog(windowname) {
	jQuery('#' + windowname).dialog("destroy");
}