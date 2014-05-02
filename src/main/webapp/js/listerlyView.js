var kLV_MIN_LAYOUT_WIDTH = 640;

function ListerlyMainView() {
	$('#listerly-sidebar-collapse').on(ace.click_event, function(){
		$minimized = $('#sidebar').hasClass('menu-min');
		listerly.mainView.sidebar_collapsed(!$minimized);//@ ace-extra.js
		listerly.currentListView.reLayout();
	});
	this.savedSidebarState();
	this.formMappings = {
		userprofile: 
			{
				modal: "#userProfileModal", 
			  	fields: [
					{name: "id", type: "hidden"}, 
			 		{name: "firstName", type: "text"}, 
			 		{name: "lastName", type: "text"}, 
					{name: "email", type: "email"}
				]
			}, 
	}
}

ListerlyMainView.prototype.blockMainScreen = function ListerlyMainView_blockMainScreen() {
	$("#main-container").block({ 
		message: $('#loadingMessage'),             
		css: {
			top:  ($(window).height() - 100) /2 + 'px', 
        	left: ($(window).width() - 100) /2 + 'px', 
        	width: '100px',
			border: '1px solid #111',
			'background-color': '#fff',
			'padding-top': '10px',
			filter:'alpha(opacity=60)',
			opacity:'.6'
    	}
	});
}

ListerlyMainView.prototype.unblockElement = function ListerlyMainView_unblockElement(elemSelector) {
    $(elemSelector).unblock();
}

ListerlyMainView.prototype.blockElement = function ListerlyMainView_blockElement(elemSelector) {
	$(elemSelector).block({ 
		message: $('#loadingMessage'),
		baseZ: 3000,         
	});
}

ListerlyMainView.prototype.unblockMainScreen = function ListerlyMainView_unblockMainScreen() {
	$("#main-container").unblock()
}

ListerlyMainView.prototype.savedSidebarState = function ListerlyMainView_savedSidebarState() {
	var sidebar = listerly.storage.get('sidebar');
	if (sidebar == 'collapsed') {
		this.sidebar_collapsed(true);
	} else {
		this.sidebar_collapsed(false);
	}
}

ListerlyMainView.prototype.sidebar_collapsed = function ListerlyMainView_sidebar_collapsed(collpase) {
	collpase = collpase || false;

	var sidebar = document.getElementById('sidebar');
	var icon = document.getElementById('listerly-sidebar-collapse').querySelector('[class*="icon-"]');
	var $icon1 = icon.getAttribute('data-icon1');//the icon for expanded state
	var $icon2 = icon.getAttribute('data-icon2');//the icon for collapsed state

	if(collpase) {
		ace.addClass(sidebar , 'menu-min');
		ace.removeClass(icon , $icon1);
		ace.addClass(icon , $icon2);

		ace.settings.set('sidebar', 'collapsed');
		listerly.storage.set('sidebar','collapsed');
	} else {
		ace.removeClass(sidebar , 'menu-min');
		ace.removeClass(icon , $icon2);
		ace.addClass(icon , $icon1);

		ace.settings.unset('sidebar', 'collapsed');
		listerly.storage.set('sidebar','expanded');
	}
}

ListerlyMainView.prototype.setUser = function ListerlyMainView_setUser(user) {
	if (user) {
		$(".nav .header-user .user-menu").remove();
		var foo = $("#loggedInDropdown").html();
		$(".nav .header-user").append(foo);
		$(".user-info").html('<small class="blue2">Logged in as:</small>' + user.firstName);
	} else {
		$(".nav .header-user .user-menu").remove();
		var foo = $("#loggedOutDropdown").html();
		$(".nav .header-user").append(foo);
		$(".user-info").html('<small class="blue2">Not logged in</small>');
	}
}

ListerlyMainView.prototype.showForm = function ListerlyMainView_showForm(key, item) {
	var formMapping = this.formMappings[key];
	if (formMapping) {
		if (formMapping.modal) {
			var theForm = $(formMapping.modal);
			var fields = formMapping.fields;
			for (fieldIdx in fields) {
				var field = (fields[fieldIdx]);
				var formField = theForm.find("#form-field-" + field.name);
				formField.val(item[field.name]);
			}
			theForm.modal();
		} else {
			listerly.log("Lookie here. May be creating a form!");
		}
	} else {
		listerly.log("Unable to find mapping for form: " + key);
	}
}

ListerlyMainView.prototype.validateForm = function ListerlyMainView_validateForm(key) {
	var formMapping = this.formMappings[key];
	var item = {};
	if (formMapping) {
		if (formMapping.modal) {
			var theFormModal = $(formMapping.modal);
			var theForm = theFormModal.find("form");
			var par = theForm.parsley();
			listerly.logObject("userprofile", "Parsley object", par);
			var r = par.validate();
			listerly.log("userprofile", "Valid: " + r);
			return r;
		}
	}
}

ListerlyMainView.prototype.getFormValues = function ListerlyMainView_getFormValues(key) {
	var formMapping = this.formMappings[key];
	var item = {};
	if (formMapping) {
		if (formMapping.modal) {
			var theForm = $(formMapping.modal);
			var fields = formMapping.fields;
			for (fieldIdx in fields) {
				var field = (fields[fieldIdx]);
				var formField = theForm.find("#form-field-" + field.name);
				item[field.name] = formField.val();
			}
			return item;
		} else {
			listerly.log("Where's my form?");
		}
	} else {
		listerly.log("Unable to find mapping for form: " + key);
	}
}

ListerlyMainView.prototype.hideForm = function ListerlyMainView_hideForm(key) {
	var formMapping = this.formMappings[key];
	if (formMapping && formMapping.modal) {
		var theForm = $(formMapping.modal);
		listerly.log("userprofile", "Hiding modal");
		theForm.modal('hide');
	}
}

ListerlyMainView.prototype.showSpaceList = function ListerlyMainView_showSpaceList(spaceList) {
	var menuSource = $('#sidebar-boardlist-template').html();
	var menuTemplate = Handlebars.compile( menuSource );
	var theData = menuTemplate(spaceList);
	$(".space-nav").remove();
	$("#spacelist-holder").append(theData);
}

ListerlyMainView.prototype.setSidebarSpaceAndBreadcrumbs = function ListerlyMainView_setSidebarSpace(space, view) {
	listerly.logObject("loadspace", "loading space", space);
	listerly.logObject("loadspace", "loading space view", view);
	
	$(".sidebar-element.active").removeClass("active");
	$("#breadcrumbs .crumb").remove();
	if (space) {
		$("#sidebar-space-"+space.id).addClass("active");
		$("#breadcrumbs .breadcrumb").append("<li class='crumb'>" + space.name + "</li>")
		if (view) {
			$("#sidebar-space-view-"+view.uuid).addClass("active");
	 		$("#breadcrumbs .breadcrumb").append("<li class='crumb'>" + view.name + "</li>")
		}
	}
}

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
	$('.dd').nestable({maxDepth: 4, group: 1});
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
		listerly.log("There are no lists.");
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
		//dimensions.singleListHeight = ((dimensions["lists-height"] - (2 * this.magicNumbers.listContentHeightAllowance)) / 2);
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

ListerlyView.prototype.calculateDimensions = function ListerlyView_calculateDimensions(dimensions) {
	var oH = $( window ).innerHeight();
	var headerH = $(".navbar").height(); 
	var topBarH = $("#breadcrumbs").height();
	var myH  = (oH - (headerH + topBarH));
	listerly.log("myH: " + myH);
	dimensions["lists-height"] = myH;

	var oW = $(window).innerWidth();
	var sideW = $("#sidebar").width();
	if ($("#sidebar").is(":visible")) {
		dimensions["lists-width"] = (oW - sideW);
	} else {
		dimensions["lists-width"] = (oW);
	}
}

