$(document).ready(function() {
			$("#various3").fancybox({
				'width'				: 750,
				'height'			: 470,
				'autoScale'			: false,
				'transitionIn'		: 'none',
				'transitionOut'		: 'none',
				'type'				: 'iframe',
				onClosed: function() {
					location.reload();
				}
			});
		});
