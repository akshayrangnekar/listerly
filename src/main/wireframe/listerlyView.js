function ListerlyView() {
	var mContentHeight = 0;
	
	function init() {
		initializeListeners();
		resizeEverything();
	}
	
	function initializeListeners() {
		$(window).resize(resizeEverything);
	}
	
	function resizeEverything() {
		resizeMainAreaHeight();
		resizeListHeights();
	}
	
	function resizeMainAreaHeight() {
		var dimensions = {};
		calculateDimensions(dimensions);
		
		mContentHeight = dimensions["lists-height"];
		$(".page-content").height(mContentHeight);
		$(".lists").height(2000);
		$(".lists").width(1400);
	}
	
	function calculateDimensions(dimensions) {
		
		// Figure out layout - how many wide, how many tall
		
		// Figure out 
		
		var oH = $( window ).innerHeight();
		var bH = $('body').innerHeight();
		var headerH = $(".navbar").height(); 
		var topBarH = $("#breadcrumbs").height();
		var myH  = (oH - (headerH + topBarH));
		listerly.log.log("myH: " + myH);
		dimensions["lists-height"] = myH;
	}
	
	function resizeListHeights() {
		$(".listerly-list").each(function() {
			$( this ).find(".widget-body").height((mContentHeight) - 54);
		});
	}
		
	return {
		init: init,
		resizeEverything: resizeEverything
	}
};