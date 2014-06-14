var userId=0;

function setUserId(id){
	userId = id;	
}

function getDepots(roleId){	
	
	var tDate = new Date();

	var currenttime = ""
	+ tDate.getHours() + ":"
	+ tDate.getMinutes() + ":"
	+ tDate.getSeconds() + ":"
	+tDate.getMilliseconds();
	
	if(userId == -1){
		jQuery.ajax({
		  url: "User.action?random="+currenttime,
		  data: {"displayDepots":"","selectedRoleId":roleId},
		  dataType: "json",
		  success: function(data, textStatus, jqXHR){ 
			  addOptionElement(data);			  
		  }		  
		});
	}else{
		jQuery.ajax({
			  url: "User.action?random="+currenttime,
			  data: {"displayDepots":"","selectedRoleId":roleId,"userId":userId},
			  dataType: "json",
			  success: function(data, textStatus, jqXHR){ 
				  addOptionElement(data);			  
			  }		  
			});		
	}
}


function addOptionElement(data){

	var defaultDepotId = data.defaultDepotId;
	
	var select = document.getElementById("user.depot");
	
	if ( select.hasChildNodes() )
	{
	    while ( select.childNodes.length >= 1 )
	    {
	    	select.removeChild( select.firstChild );       
	    } 
	}
	
	var optionTitle = document.createElement("option");
	optionTitle.text = "Please select a depot";
	optionTitle.value = "0";
	
	try {									 	    	
	 	select.add(optionTitle,null); // standards compliant; doesn't work in IE		   	
	}catch(ex) {									 	    	
	     select.add(optionTitle); // IE only
	}	
	
	var object = data.depots;
	var len = object.length;
	
	for(var i=0; i<len; i++){
		var option = document.createElement("option");
		if(object[i] != null && object[i].name != null){
			option.text = object[i].name;
			option.value = object[i].id;
		
			if(option.value == defaultDepotId){
				option.selected = 'selected';
			}
		
			try {									 	    	
				select.add(option,null); // standards compliant; doesn't work in IE		   	
			}catch(ex) {									 	    	
				select.add(option); // IE only
			}
		}
	}
	
	if(len == 0){


	}
}