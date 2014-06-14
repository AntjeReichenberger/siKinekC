   function formatTime(timeClass, spacing) {

		var timeLong = jQuery(timeClass).filter(function(index) {
			var time = jQuery.trim($(this).innerHTML);
			var timeArray = time.split(':');
			return (timeArray[0].length == 2);
		});

		var timeShort = jQuery(timeClass).filter(function(index) {
			var time = jQuery.trim($(this).innerHTML);
			var timeArray = time.split(':');
			return (timeArray[0].length == 1);
		});

		if(timeShort.length > 0 && timeLong.length > 0) {
			timeShort.each(function(index) {
				$(this).innerHTML = spacing + jQuery.trim($(this).innerHTML);
			});
		}
   }
