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

var listerly = (function() {
	var mContentHeight = 0;
	var mView = undefined;
	var log = new ListerlyLog();

	var LayoutEnum = {
		GRID: 1,
		LIST: 2
	}
	
	function init() {
		mView = new ListerlyView();
		mView.init(LayoutEnum.GRID);
	}
	
	function getView() {
		return mView;
	}
	
	return {
		init: init,
		log: log,
		LayoutEnum: LayoutEnum,
		getView: getView
	}
})();
