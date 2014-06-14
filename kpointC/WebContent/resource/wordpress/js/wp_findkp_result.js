jQuery(document).ready(function(){		

	//Map tab changing
	jQuery('ul#maptabs span').click(function(e) {
		e.preventDefault();
		var self = jQuery(this);
		var item = self.closest('li');
		var tabList = jQuery('ul#maptabs');
		tabList.find('li').removeClass('active');
		item.addClass('active');
		
		if(self.html() == 'Results' && jQuery('#results').is(':hidden')) {
			jQuery('#results').show();
			jQuery('#directions').hide();
		}
		else if(self.html() == 'Directions' && jQuery('#directions').is(':hidden')) {
			closeInfoWindows();
			jQuery('#results').hide();
			jQuery('#directions').show();
		}
	});

	window.scroll(0, 1000);	
	window.scroll(0, 0);
});
