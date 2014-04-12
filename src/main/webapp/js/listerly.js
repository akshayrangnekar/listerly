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
	this.mView = undefined;
	this.log = new ListerlyLog();
	this.LayoutEnum = {
		GRID: 1,
		LIST: 2
	}
	this.actions = {
		hideArea: function (e) {
			console.log("Context for hideArea");
			console.log(this);
			console.log(e);
			$('div.page-content').block({ message: $('#loadingMessage')  });
		},
		showArea: function () {
			console.log("Context for showArea");
			console.log(this);
			$('div.page-content').unblock();
		}
	};
}
	
Listerly.prototype.init = function() {
	$('body').on('click', '[data-action]', function() {
	    var action = $(this).data('action');
		console.log("Here: " + action);
	    if (action in listerly.actions) {
	        listerly.actions[action].apply(this, arguments);
	    }
	});
	
	this.mView = new ListerlyView();
	this.mView.init(this.LayoutEnum.GRID);
}
	
Listerly.prototype.getView = function() {
	return this.mView;
}
