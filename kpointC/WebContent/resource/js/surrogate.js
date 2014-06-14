function getStates(countryId){			
	jQuery.ajax({
		  url: "Surrogate.action",
		  data: {"statesOptionElements":"","countryId":countryId},
		  dataType: "json",
		  success: function(data, textStatus, jqXHR){ 
			  populateStates(data);			  
		  }
		  
		});	
}


function populateStates(data){
	alert(data);
}