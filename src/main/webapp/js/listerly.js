var gEnableLogging = true;
var gLogMethods = {
	"routes.IMPORTANT": true, 
	"routes": false,
	"userprofile": false, 
	"states": false, 
	"states.2": false,
	"loadspace": true
};

function listerly_fixFunctionNames(inproto) {
	for (x in inproto) {
		if (listerly_isFunction(inproto[x])) {
			if (!inproto[x].name) {
				inproto[x].name = inproto.constructor.name + "__" + x;
			}
		}
	}
}

function listerly_isFunction(obj) {
  return !!(obj && obj.constructor && obj.call && obj.apply);
}

function Listerly() {
	this.loggingEnabled = gEnableLogging;
	this.logMethods = gLogMethods;
	this.currentListView = undefined;
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
			// console.log($( "[data-action='listcheckbox'][data-id='" + dataid +"']" ));
			$( "[data-action='listcheckbox'][data-id='" + dataid +"']" ).hide();
			$( "[data-action='listcheckboxchecked'][data-id='" + dataid +"']" ).show();
		},
		listcheckboxchecked: function(action, dataid) {
			// console.log($( "[data-action='listcheckboxchecked'][data-id='" + dataid +"']" ));
			$( "[data-action='listcheckboxchecked'][data-id='" + dataid +"']" ).hide();
			$( "[data-action='listcheckbox'][data-id='" + dataid +"']" ).show();
		},
		userprofilesave: function(action, dataid) {
			listerly.saveUserProfile();
		}
	};
}
	
Listerly.prototype.init = function listerly_init() {
	
	var initialRequest = location.hash;
	this.storage = new ListerlyStorage();
	this.mainView = new ListerlyMainView();
	this.router = new ListerlyRouter(this, initialRequest);
	
	this.mainView.blockMainScreen();
	
	this.setupListeners();

	// Check login state and set up login pane
	this.checkLoginState();
	
	// See if we should preload a space
	this.currentListView = new ListerlyView();
	this.currentListView.init(this.LayoutEnum.LIST);
}

Listerly.prototype.logMessage = function listerly_log(message) {
	if (this.loggingEnabled) {
		if (arguments && arguments.callee && arguments.callee.caller) {
			var methodName = arguments.callee.caller.name;
			// console.log("Method name: "+ methodName);
			if (this.logMethods["*"] || this.logMethods[methodName]) {
				if (methodName) console.log(methodName + ": " + message);
				else console.log(message);
			}
		} else {
			console.log(message);
		}
	}
};

Listerly.prototype.logObject = function listerly_logObject(category, message, object) {
	if (this.loggingEnabled) {
		var methodName = category;
		// console.log("Method name: "+ methodName);
		if (this.logMethods["*"] || this.logMethods[methodName]) {
			if (methodName) console.log(methodName + ": " + message);
			console.log(object);
		}
	}
};

Listerly.prototype.log = function listerly_log(category, message) {
	if (message == undefined) return this.logMessage(category);
	
	if (this.loggingEnabled) {
		var methodName = category;
		// console.log("Method name: "+ methodName);
		if (this.logMethods["*"] || this.logMethods[methodName]) {
			if (methodName) console.log(methodName + ": " + message);
			else console.log(message);
		}
	}
};

Listerly.prototype.enableLogging = function listerly_enableLogging(method) {
	if (typeof(method)==='undefined') {
		this.loggingEnabled = true;
	} else {
		this.logMethods[method] = true;
	}
}

Listerly.prototype.checkLoginState = function listerly_checkLoginState() {
	this.startLoadingResource();
	$.ajax({
		dataType: "json",
		url: "/api/v1/user/current",
		data: {},
		async: true,
		cache: false,
		context: this,
		success: function finishedLoadingUserCallback(data, textStatus, jqXHR) {
			this.log("Login status: " + textStatus);
			this.finishedLoadingUser(data);
			this.finishedLoadingResource();
		}
	});
}

Listerly.prototype.finishedLoadingUser = function listerly_finishedLoadingUser(user) {
	this.setCurrentUser(user);
	if (user) {
		this.loadSpacesForUser(user);
	}
};

Listerly.prototype.setCurrentUser = function listerly_setCurrentUser(user) {
	this.user = user;
	this.mainView.setUser(user);
}

Listerly.prototype.loadSpacesForUser = function listerly_loadSpacesForUser(user) {
	this.log("Loading Spaces For User.");
	this.startLoadingResource();
	$.ajax({
		dataType: "json",
		url: "/api/v1/user/spaces",
		data: {},
		async: true,
		cache: false,
		context: this,
		success: function finishedLoadingSpacesCallback(data, textStatus, jqXHR) {
			this.log("Spaces status: " + textStatus);
			//this.finishedLoadingUser(data);
			this.finishedLoadingSpaces(data);
			this.finishedLoadingResource();
		}
	});
}

Listerly.prototype.finishedLoadingSpaces = function listerly_finishedLoadingSpaces(data) {
	//this.log(data);
	this.spaces = data;
	this.mainView.showSpaceList(data);
}

Listerly.prototype.saveUserProfile = function listerly_saveUserProfile() {
	var isValid = listerly.mainView.validateForm("userprofile");
	if (isValid) {
		var editedUser = listerly.mainView.getFormValues("userprofile");
		if (this.didObjectChange(editedUser, this.user)) {
			this.log("userprofile", "Object did change.");
			this.mainView.blockElement("#userProfileModal");
			$.ajax({
				dataType: "json",
				url: "/api/v1/user/current",
				data: JSON.stringify(editedUser),
				async: true,
				cache: false,
				context: this,
				contentType: "application/json",
				processData: false,
				type: 'POST',
				success: function(data, textStatus, jqXHR) {
					this.finishedUserProfile(data)
				}
			});
		} else {
			this.log("userprofile", "Object didn't change.");
			this.finishedUserProfile(undefined);
		}
		this.log(editedUser);
	} else {
		this.log("userprofile", "Nope, not valid");
	}
}

Listerly.prototype.finishedUserProfile = function listerly_finishedUserProfile(data) {
	this.mainView.unblockElement("#userProfileModal");
	listerly.mainView.hideForm("userprofile");
	if (data) {
		this.logObject("userprofile", "Set profile data: ", data);
		this.setCurrentUser(data);
	}
	this.router.setLastLocation();
} 

Listerly.prototype.didObjectChange = function listerly_didObjectChange(newObject, originalObject) {
	var changed = false;
	for (var key in newObject) {
	  if (newObject.hasOwnProperty(key)) {
		  if (newObject[key] != originalObject[key]) changed=true;
	  }
	}
	return changed;
}

Listerly.prototype.setLoadingResourceCount = function listerly_setLoadingResourceCount(count) {
	this.loadingCount = count;
}

Listerly.prototype.startLoadingResource = function listerly_startLoadingResource() {
	var count = this.loadingCount;
	if (count == undefined) {
		count = 0;
	}
	count++;
	this.loadingCount = count;
}

Listerly.prototype.finishedLoadingResource = function listerly_finishedLoadingResource() {
	var count = this.loadingCount;
	count--;
	if (count == 0) {
		this.completedLoading();
	}
	this.loadingCount = count;
}

Listerly.prototype.completedLoading = function listerly_completedLoading() {
	this.mainView.unblockMainScreen();
	this.router.setLocation("#/checkProfile");
}

Listerly.prototype.checkProfile = function listerly_checkProfile() {
	// Now see what page to load
	// If the profile isn't complete set to profile
	if (this.user && !this.user.profileComplete) {
		this.log("states", "Now switching to profile.");
		// $("#loginModal").modal();
		//this.mainView.showForm("userprofile", user);
		this.router.setLocation("#/profile");
	} else if (this.user) {
		// Otherwise set to page to load
		// Otherwise set to dashboard
		this.log("states", "Not Showing profile modal");
		this.router.setLastLocation();
	} else {
		this.router.setLocation("#/welcome");
	}
}

Listerly.prototype.loadSpace = function listerly_loadSpace(pathDetails) {
	this.logObject("loadspace", "Loading space with arguments: ", pathDetails);
	var spaceId = pathDetails.space;
	this.log("loadspace", "Finding space with id: " + spaceId);
	var spaceM = this.getSpaceMetadata(pathDetails.space);
	var spaceVM = undefined;
	if (spaceM && pathDetails.view) {
		spaceVM = this.getSpaceViewMetadata(spaceM, pathDetails.view);
	}
	this.mainView.setSidebarSpaceAndBreadcrumbs(spaceM, spaceVM);
}

Listerly.prototype.getSpaceMetadata = function(spaceId) {
	this.log("loadspace", "Finding space with id: " + spaceId);
	if (this.spaces) {
		var arr = this.spaces.spaces;
		for (var i=0; i<arr.length; i++) {
			this.log("loadspace", "Comparing with space with id: " + arr[i].id);
		    if (arr[i].id == spaceId) return arr[i];
		}
	}
}

Listerly.prototype.getSpaceViewMetadata = function(space, viewUuid) {
	if (space) {
		for (var i=0; i < space.views.length; i++) {
			var vw = space.views[i];
			if (vw.uuid == viewUuid) {
				return vw;
			}
		}
	}
}

Listerly.prototype.setupListeners = function listerly_setupListeners() {
	$('body').on('click', '[data-action]', function() {
	    var action = $(this).data('action');
	    var dataid = $(this).data('id');
		$(this).blur();
	    if (action in listerly.actions) {
	        listerly.actions[action].apply(this, [action, dataid]);
	    }
	});
	
	$(window).on('hashchange', function() {
		//alert(location.hash);
		listerly.log("states", "hashchange location: " + location.hash);
		listerly.router.loadLocation(location.hash);
	});
}
	
Listerly.prototype.getView = function listerly_getView() {
	return this.currentListView;
}
