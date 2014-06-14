//SET NOCONFLICT TO WORK WITH OTHER LIBRARIES
jQuery.noConflict();

//CUSTOM JQUERY FUNCTIONS
jQuery(document).ready(function(){

	//Set radius and border for the site wrapper
	//jQuery('#wrapper').cornerz({radius:0, background: "#c1c1c1"});
		
	//Set radius and border for the interface wrapper
	//jQuery('#contentWrapper').cornerz({radius:4, background: "#f7f7f7"});
	
	//Set radius and border for searchWrapper
	jQuery('#searchWrapper').cornerz({radius:4, background: "#fff"});
	
	//Stripe table rows
	jQuery('table tbody tr:odd').addClass('even');
	
	//Style table
	jQuery('table thead th:first-child').css('border-color','#25a6e6');
	
	jQuery('ol li:first-child, ul li:first-child').addClass('first');
	jQuery('ol li:last-child, ul li:last-child').addClass('last');
	
	//Add css class of disabled to fields with attribute disabled
	jQuery('input:disabled, select:disabled, button:disabled').addClass('disabled');
	
	
	//Add focused class to parents of form elements on focus
	jQuery('input, select, textarea').focus(function(){
		//Check if field has validation message
        if(jQuery(this).siblings('small').length > 0){
			jQuery(this).parent().addClass('focused');
        }
    });
	
	//Remove focused & error class to parents of form elements on blur
	jQuery('input, select, textarea').blur(function(){
		jQuery(this).parent().removeClass('focused');
		jQuery(this).parent().removeClass('error');		
    });

	
	//Clear all inputs on focus
	//jQuery('input').clearInput();
	jQuery('input#criteria').clearInput();
	jQuery('input#searchCriteria').clearInput();
	
	jQuery('a#bookmark').jFav();
	
});

jQuery.fn.clearInput = function(){
	return this.focus(function(){
		if(this.value == this.defaultValue){
			this.value = "";
		}
	}).blur(function(){
		if(!this.value.length){
			this.value = this.defaultValue;
		}
	});
};

