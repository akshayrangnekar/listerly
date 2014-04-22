var gEnableLogging = true;
var gLogMethods = {"*": true};

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
	
Listerly.prototype.init = function() {
	
	this.storage = new ListerlyStorage();
	this.mainView = new ListerlyMainView();
	
	this.mainView.blockMainScreen();
	this.setupListeners();

	// Check login state and set up login pane
	this.checkLoginState();
	
	// See if we should preload a space
	this.currentListView = new ListerlyView();
	this.currentListView.init(this.LayoutEnum.LIST);

}

Listerly.prototype.log = function(message) {
	if (this.loggingEnabled) {
		if (arguments && arguments.callee && arguments.callee.caller) {
			var methodName = arguments.callee.caller.name;
			console.log("Method name: "+ methodName);
			if (this.logMethods["*"] || this.logMethods[methodName]) {
				if (methodName) console.log(methodName + ":" + message);
				else console.log(message);
			}
		} else {
			console.log(message);
		}
	}
};

Listerly.prototype.enableLogging = function(method) {
	if (typeof(method)==='undefined') {
		this.loggingEnabled = true;
	} else {
		this.logMethods[method] = true;
	}
}

Listerly.prototype.checkLoginState = function() {
	this.startLoadingResource();
	$.ajax({
		dataType: "json",
		url: "/api/v1/user/current",
		data: {},
		async: true,
		cache: false,
		context: this,
		success: function(data, textStatus, jqXHR) {
			this.log("Login status: " + textStatus);
			this.finishedLoadingResource();
			this.finishedLoadingUser(data);
		}
	});
}

function listerly_finishedLoadingUser(user) {
	this.user = user;
	this.mainView.setUser(user);
	if (user && !user.profileComplete) {
		this.log("Showing profile modal");
		// $("#loginModal").modal();
		this.mainView.showForm("userprofile", user);
	} else {
		this.log("Not Showing profile modal");
	}
}
Listerly.prototype.finishedLoadingUser = listerly_finishedLoadingUser;

Listerly.prototype.saveUserProfile = function() {
	var isValid = listerly.mainView.validateForm("userprofile");
	if (isValid) {
		var editedUser = listerly.mainView.getFormValues("userprofile");
		if (this.didObjectChange(editedUser, this.user)) {
			this.log("Object did change.");
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
					this.mainView.unblockElement("#userProfileModal");
					listerly.mainView.hideForm("userprofile");
					this.log("Set profile status: " + textStatus);
					this.log(data);
					this.finishedLoadingUser(data);
				}
			});
		} else {
			this.log("Object didn't change.");
			listerly.mainView.hideForm("userprofile");
		}
		console.log(editedUser);
	} else {
		console.log("Nope, not valid");
	}
}

Listerly.prototype.didObjectChange = function(newObject, originalObject) {
	var changed = false;
	for (var key in newObject) {
	  if (newObject.hasOwnProperty(key)) {
		  if (newObject[key] != originalObject[key]) changed=true;
	  }
	}
	return changed;
}

Listerly.prototype.setLoadingResourceCount = function(count) {
	this.loadingCount = count;
}

Listerly.prototype.startLoadingResource = function() {
	var count = this.loadingCount;
	if (count == undefined) {
		count = 0;
	}
	count++;
	this.loadingCount = count;
}

Listerly.prototype.finishedLoadingResource = function() {
	var count = this.loadingCount;
	count--;
	if (count == 0) {
		this.completedLoading();
	}
	this.loadingCount = count;
}

Listerly.prototype.completedLoading = function() {
	this.mainView.unblockMainScreen();
}

Listerly.prototype.setupListeners = function() {
	$('body').on('click', '[data-action]', function() {
	    var action = $(this).data('action');
	    var dataid = $(this).data('id');
		$(this).blur();
	    if (action in listerly.actions) {
	        listerly.actions[action].apply(this, [action, dataid]);
	    }
	});
	
	$(window).on('hashchange', function() {
		alert(location.hash);
		listerly.log("Location: " + location.hash);
	});
}
	
Listerly.prototype.getView = function() {
	return this.currentListView;
}

function ListerlyStorage() {
}

ListerlyStorage.prototype.set = function(field, value) {
	$.jStorage.set(field, value, {TTL: 6048000});
}

ListerlyStorage.prototype.get = function(field) {
	return $.jStorage.get(field);
}
