//SET NOCONFLICT TO WORK WITH OTHER LIBRARIES
jQuery.noConflict();

//CUSTOM JQUERY FUNCTIONS
jQuery(document).ready(function(){

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
		
		if(jQuery(this).hasClass('error')){
			
			jQuery(this).parent().addClass('error');
			
		}
		else {
			
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