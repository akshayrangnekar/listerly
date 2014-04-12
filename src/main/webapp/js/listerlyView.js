var kLV_MIN_LAYOUT_WIDTH = 640;

function ListerlyView() {
};

ListerlyView.prototype.init = function (layoutType) {
	this.layoutType = layoutType;
	this.initializeListeners();

	// Make sure model fields are loaded
	this.loadModelFieldsDUMMY();
	
	// Calculate "magic numbers" 
	this.calculateStyleMagicNumbers();
	
	// Then relayout
	this.reLayout();
}

ListerlyView.prototype.loadModelFieldsDUMMY = function() { 
	// TODO: GET THIS FROM THE MODEL
	var numL = $(".listerly-list").length;
	var magicNumbers = {};
	
	magicNumbers.numberOfLists = numL;
	this.magicNumbers = magicNumbers;
}

ListerlyView.prototype.calculateStyleMagicNumbers = function() {
	var magicNumbers = this.magicNumbers;
	var numL = magicNumbers.numberOfLists;

	if (numL == 0) {
		listerly.log.log("There are no lists.");
	} else {
		var elem = $(".listerly-list");
		magicNumbers.listMarginTop = parseInt(elem.css("margin-top"));
		magicNumbers.listMarginBottom = parseInt(elem.css("margin-bottom"));
		magicNumbers.listMarginLeft = parseInt(elem.css("margin-left"));
		magicNumbers.listMarginRight = parseInt(elem.css("margin-right"));
		magicNumbers.listBorderTop = parseInt(elem.css("borderTopWidth"));
		magicNumbers.listBorderBottom = parseInt(elem.css("borderBottomWidth"));
		magicNumbers.listBorderLeft = parseInt(elem.css("borderLeftWidth"));
		magicNumbers.listBorderRight = parseInt(elem.css("borderRightWidth"));
		magicNumbers.listOuterHeight = magicNumbers.listMarginTop + magicNumbers.listMarginBottom 
			+ magicNumbers.listBorderTop + magicNumbers.listBorderBottom;
		magicNumbers.singleListWidth = parseInt(elem.innerWidth());
		magicNumbers.listOuterWidth = magicNumbers.listMarginLeft + magicNumbers.listMarginRight 
			+ magicNumbers.listBorderLeft + magicNumbers.listBorderRight;
		magicNumbers.totalListWidth = magicNumbers.singleListWidth + magicNumbers.listOuterWidth;
		var headerElem = elem.find(".listerly-list-header");
		if (headerElem) {
			magicNumbers.headerElemHeight = headerElem.outerHeight();
			magicNumbers.listContentHeightAllowance = magicNumbers.listOuterHeight + magicNumbers.headerElemHeight;
		}
		
		var settingElem = $("#settings-container");
		magicNumbers.settingsWidth = settingElem.outerWidth();
	}
	this.magicNumbers = magicNumbers;
}

ListerlyView.prototype.reLayout = function() {
	var dimensions = {};
	this.calculateDimensions(dimensions);
	this.determineLayout(dimensions);
	this.sizeMainArea(dimensions);
	this.resizeListHeights(dimensions);
	$('.dd').nestable({maxDepth: 4, group: 1});
}

ListerlyView.prototype.determineLayout = function (dimensions) {
	// Am I big enough to layout? 
	$("#lists").removeClass();
	if (dimensions["lists-width"] < kLV_MIN_LAYOUT_WIDTH) {
		dimensions.layout = "VERTICALS";
		// If not, switch to vertical
		$("#lists").addClass("verticals");
	}
	else {
		// Otherwise 
		if (this.layoutType == listerly.LayoutEnum.GRID) {
			dimensions.layout = "GRID";
			$("#lists").addClass("grid");
		} else if (this.layoutType == listerly.LayoutEnum.LIST) {
			dimensions.layout = "LIST";
			$("#lists").addClass("list");
		} else {
			console.log("WTF!");
		}
	}
} 

ListerlyView.prototype.initializeListeners = function () {
	$(window).on("resize.ListerlyView", $.proxy(this.reLayout, this)); //Use jQuery proxy to preserve context of this
}

ListerlyView.prototype.sizeMainArea = function(dimensions) {
	$(".page-content").height(dimensions["lists-height"]);

	$("#lists").removeAttr('style');
	
	switch (dimensions.layout){
	case "VERTICALS":
		$("#lists").css("width", dimensions["lists-width"]);
		dimensions.singleListHeight = ((dimensions["lists-height"] - (2 * this.magicNumbers.listContentHeightAllowance)) / 2);
		break;
	case "GRID":
		var listWidth = this.magicNumbers.totalListWidth;
		var numL = this.magicNumbers.numberOfLists;
		var numAcross = Math.ceil((numL / 2));
		var totalWidth = (numAcross * listWidth) + this.magicNumbers.settingsWidth;
		
		var availableWidth = dimensions["lists-width"]  - this.magicNumbers.settingsWidth;
		var availableListWidth = Math.floor(availableWidth/numAcross);
		var innerAvailableListWidth = availableListWidth - this.magicNumbers.listOuterWidth;
		
		if (innerAvailableListWidth > this.magicNumbers.singleListWidth) {
			dimensions.singleListWidth = Math.floor(innerAvailableListWidth);
		}
		
		$("#lists").height(dimensions["lists-height"]);
		$("#lists").css("min-width", "" + totalWidth + "px");
		dimensions.singleListHeight = (((dimensions["lists-height"] - (2 * this.magicNumbers.listContentHeightAllowance)) / 2) - 1);
		break;
	case "LIST":
		var listWidth = this.magicNumbers.totalListWidth;
		var numL = this.magicNumbers.numberOfLists;
		var totalWidth = (numL * listWidth) + this.magicNumbers.settingsWidth;
		$("#lists").height(dimensions["lists-height"]);
		$("#lists").css("min-width", "" + totalWidth + "px");
		dimensions.singleListHeight = dimensions["lists-height"] - this.magicNumbers.listContentHeightAllowance - 1;
		break;
	default:
		console.log("WTF");
	}
}

ListerlyView.prototype.resizeListHeights = function (dimensions) {
	var lHeight = dimensions.singleListHeight;
	var lWidth = dimensions.singleListWidth;
	$(".listerly-list").each(function() {
		$( this ).removeAttr('style');
		if (lHeight) {
			$( this ).find(".widget-body").height(lHeight);
		}
		if (lWidth) {
			$( this ).width(lWidth);
		}
	});
}

ListerlyView.prototype.calculateDimensions = function (dimensions) {
	var oH = $( window ).innerHeight();
	var headerH = $(".navbar").height(); 
	var topBarH = $("#breadcrumbs").height();
	var myH  = (oH - (headerH + topBarH));
	listerly.log.log("myH: " + myH);
	dimensions["lists-height"] = myH;

	var oW = $(window).innerWidth();
	var sideW = $("#sidebar").width();
	if ($("#sidebar").is(":visible")) {
		dimensions["lists-width"] = (oW - sideW);
	} else {
		dimensions["lists-width"] = (oW);
	}
}

// ListerlyView.prototype.layoutEverything = function () {
// 	this.resizeMainAreaHeight();
// 	this.resizeListHeights();
// }

// ListerlyView.prototype.resizeMainAreaHeight = function () {
// 	var dimensions = {};
// 	this.calculateDimensions(dimensions);
// 	
// 	this.mContentHeight = dimensions["lists-height"];
// 	$(".page-content").height(this.mContentHeight);
// 	$(".lists").height(2000);
// 	$(".lists").css("min-width", "1400px");
// }
