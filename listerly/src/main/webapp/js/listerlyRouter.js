var gInitialRoutes = {
	"checkProfile": {method: "checkProfile", pathargs:false},
	"space": {method: "loadSpace", pathargs:true},
	"profile": {form: "userprofile", arg:"user"},
	"root": {redirect: "dashboard"},
};

function ListerlyRouter(inListerly, initialRequest) {
	this.state = new Array();
	this.listerly = inListerly;
	this.routes = gInitialRoutes;
	if (initialRequest) this.pushState(initialRequest);
}

ListerlyRouter.prototype.loadRouteForLocation = function listerlyRouter_loadRouteForLocation(loc) {
	this.listerly.log("routes", "This is where I would load page for " + loc);
	var bits = loc.split(/#|\//).filter(function(el) {return el.length != 0});;
	//this.listerly.logObject("routes", "Bits of the location:", bits); 
	var reqAction = "root";
	if (bits && bits.length > 0) {
		reqAction = bits[0];
	} 
	
	this.listerly.log("routes", "Loading action: " + reqAction);
	// if (reqAction == "profile") {
	// 	this.listerly.mainView.showForm("userprofile", this.listerly.user);
	// } if (reqAction == "checkProfile") {
	// 	this.listerly["checkProfile"]();
	// }
	var actionRoute = this.routes[reqAction];
	if (actionRoute) {
		if (actionRoute.method) {
			if (actionRoute.pathargs) {
				this.listerly.log("routes", "Calling action method: " + actionRoute.method + " with args");
				this.listerly[actionRoute.method](this.buildRouteArguments(bits));
			}
			else {
				this.listerly.log("routes", "Calling action method: " + actionRoute.method + " without args");
				this.listerly[actionRoute.method]();
			}
		}
		else if (actionRoute.form) {
			if (actionRoute.arg) {
				this.listerly.log("routes", "Showing form: " + actionRoute.form + " with arg: " + actionRoute.arg);
				this.listerly.mainView.showForm(actionRoute.form, this.listerly[actionRoute.arg]);
			} else {
				this.listerly.log("routes", "Showing form: " + actionRoute.form + " without arg");
				this.listerly.mainView.showForm(actionRoute.form);
			}
		} else if (actionRoute.redirect) {
			this.setLocation(actionRoute.redirect);
		}
	} else {
		this.listerly.log("routes.IMPORTANT", "Don't know what to do about action: " + reqAction);
	}
} 

ListerlyRouter.prototype.buildRouteArguments = function(bits) {
	var rval = {};
	for (var i = 0; i < bits.length; i=i+2) {
		this.listerly.log("routes", "Bit Counter: " + i);
		var key = bits[i];
		var val = bits[i+1];
		rval[key] = val;
	}
	this.listerly.logObject("routes", "Returned processed bits: ", rval);
	return rval;
}

ListerlyRouter.prototype.setLocation = function listerlyRouter_setLocation(loc) {
	if (loc == undefined) loc = "#/";
	
	if (loc.lastIndexOf("#/", 0) != 0) {
		loc = "#/" + loc;
	}
	
	this.listerly.log("states", "Setting location to " + loc);
	var currLoc = location.hash;
	if (currLoc == loc) {
		this.listerly.log("states", "Loading location for " + currLoc);
		this.loadLocation(currLoc);
	} else {
		this.listerly.log("states", "Setting hash for " + loc + " as it is not " + currLoc);
		location.hash = loc;
	}
}

ListerlyRouter.prototype.loadLocation = function listerlyRouter_loadLocation(loc) {
	this.listerly.log("states", "Loading location for: " + loc);
	// Push onto state stack
	this.pushState(loc);
	this.loadRouteForLocation(loc);
}

ListerlyRouter.prototype.pushState = function listerlyRouter_pushState(loc) {
	this.listerly.log("states", "Pushing state for: " + loc);
	this.state.push(loc);
	this.listerly.logObject("states.2", "Current state: ", this.state);
}

ListerlyRouter.prototype.popState = function listerlyRouter_popState() {
	var popped = this.state.pop();
	this.listerly.log("states", "Popping state for: " + popped);
	this.listerly.logObject("states.2", "Current state: ", this.state);
	return popped;
}

ListerlyRouter.prototype.setLastLocation = function listerlyRouter_setLastLocation() {
	var lastLoc = this.popState();
	this.listerly.log("states", "Popping off current location: " + lastLoc);
	if (lastLoc) {
		lastLoc = this.popState();
		this.listerly.log("states", "Popping off and returning previous location: " + lastLoc);
	}
	this.setLocation(lastLoc);
}
