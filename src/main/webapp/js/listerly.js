function ListerlyLog(enableLogging) {
	this.loggingEnabled = enableLogging;
	this.logMethods = {};
}

ListerlyLog.prototype.log = function(message) {
	if (this.loggingEnabled) {
		if (arguments && arguments.callee && arguments.callee.caller) {
			if (this.logMethods["*"] || this.logMethods[arguments.callee.caller.name]) {
				console.log(message);
			}
		} else {
			console.log(message);
		}
	}
};

ListerlyLog.prototype.enableLogging = function(method) {
	if (typeof(method)==='undefined') {
		this.loggingEnabled = true;
	} else {
		this.logMethods[method] = true;
	}
}

function Listerly() {
	this.currentListView = undefined;
	this.log = new ListerlyLog();
	this.LayoutEnum = {
		GRID: 1,
		LIST: 2
	}
	this.actions = {
		hideArea: function (e) {
			// console.log("Context for hideArea");
			// console.log(this);
			// console.log(e);
			$('div.page-content').block({ message: $('#loadingMessage')  });
		},
		showArea: function () {
			// console.log("Context for showArea");
			// console.log(this);
			$('div.page-content').unblock();
		},
		listcheckbox: function(action, dataid) {
			// console.log(this);
			// console.log(action);
			// console.log(dataid);
			console.log($( "[data-action='listcheckbox'][data-id='" + dataid +"']" ));
			$( "[data-action='listcheckbox'][data-id='" + dataid +"']" ).hide();
			$( "[data-action='listcheckboxchecked'][data-id='" + dataid +"']" ).show();
		},
		listcheckboxchecked: function(action, dataid) {
			console.log($( "[data-action='listcheckboxchecked'][data-id='" + dataid +"']" ));
			$( "[data-action='listcheckboxchecked'][data-id='" + dataid +"']" ).hide();
			$( "[data-action='listcheckbox'][data-id='" + dataid +"']" ).show();
		}
	};
}
	
Listerly.prototype.init = function() {
	$('body').on('click', '[data-action]', function() {
	    var action = $(this).data('action');
	    var dataid = $(this).data('id');
		$(this).blur();
		// console.log(this);
		// console.log("Action: " + action);
		// console.log("ID: " + dataid);
	    if (action in listerly.actions) {
	        listerly.actions[action].apply(this, [action, dataid]);
	    }
	});
	
	$(window).on('hashchange', function() {
		alert(location.hash);
	});
	
	// location.hash="#/space/1";
	this.storage = new ListerlyStorage();
	this.mainView = new ListerlyMainView();
	this.currentListView = new ListerlyView();
	this.currentListView.init(this.LayoutEnum.GRID);
}
	
Listerly.prototype.getView = function() {
	return this.currentListView;
}

function ListerlyMainView() {
	$('#listerly-sidebar-collapse').on(ace.click_event, function(){
		$minimized = $('#sidebar').hasClass('menu-min');
		listerly.mainView.sidebar_collapsed(!$minimized);//@ ace-extra.js
		listerly.currentListView.reLayout();
	});
	var sidebar = listerly.storage.get('sidebar');
	if (sidebar == 'collapsed') {
		this.sidebar_collapsed(true);
	} else {
		this.sidebar_collapsed(false);
	}
	
}

ListerlyMainView.prototype.sidebar_collapsed = function(collpase) {
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

function ListerlyStorage() {
}

ListerlyStorage.prototype.set = function(field, value) {
	$.jStorage.set(field, value, {TTL: 6048000});
}

ListerlyStorage.prototype.get = function(field) {
	return $.jStorage.get(field);
}