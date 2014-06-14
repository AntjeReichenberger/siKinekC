//SET NOCONFLICT TO WORK WITH OTHER LIBRARIES
jQuery.noConflict();

//CUSTOM JQUERY FUNCTIONS
jQuery(document).ready(function(){

	//Stripe table rows
	jQuery('table tbody tr:odd').addClass('even');
	
	//Add first class to first list item in nav
	jQuery('ul#nav li:first-child').addClass('first');
			
	//Add last class to last list item in dropdown
	jQuery('ul#nav li:last-child').addClass('last');
	jQuery('ul#nav li li:last-child').addClass('last');
	jQuery('ul#nav li li li:last-child').addClass('last');
	
	//Add css class of disabled to fields with attribute disabled
	jQuery('input:disabled, select:disabled, button:disabled').addClass('disabled');
	
	//Style table
	jQuery('table thead th:first-child').css('border-left','none');
	
	//Style mainnav dropdowns with superfish
	jQuery('ul.sf-menu').supersubs({ 
		minWidth:    16,
		maxWidth:    18,
		extraWidth:  1
	}).superfish({ 
		delay:       1000,
		animation:   {opacity:'show',height:'show'},
		speed:       'fast',
		autoArrows:  true,
		dropShadows: false
	});

	//Toggle HelpPanel
	jQuery('a#helpPanel_closebtn').toggle(function(){
		
		jQuery(this).attr('title','Hide Instructional Video');
		
		jQuery('#helpPanel #helpVideo, #helpPanel #helpText').fadeIn();
		jQuery('#helpPanel').animate({ height: "215px" }, 1000 );

	}, function(){
		
		jQuery(this).attr('title','Show Instructional Video');
		
    	jQuery('#helpPanel').animate({ height: "30px" }, 1000 );
    	jQuery('#helpPanel #helpVideo, #helpPanel #helpText').fadeOut();

	});	
	
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
	
});