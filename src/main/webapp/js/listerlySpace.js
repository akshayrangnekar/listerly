function ListerlySpaceController(listerly) {
	this.listerly = listerly;
}

Handlebars.registerHelper('spacelist', function(context, options) {
	var ret = "";
	
	// for (var i = 0; i < this.space.)
});

ListerlySpaceController.prototype.setupSpace = function(space) {
	if (space) {
		if (space.id == this.spaceMeta.id) {
			this.space = space;
			this.setupView();
		} else {
			this.listerly.logObject("loadspace", "This is not the space you're looking for: " + this.spaceMeta, space);
			return false;
		}
	} else {
		if (!this.space) {
			this.listerly.logObject("loadspace", "This is not the space you're looking for: " + this.spaceMeta, space);
			return false;
		} else {
			this.setupView();
		}
	}
	return true;
}

ListerlySpaceController.prototype.init = function(spaceM, viewM) {
	this.spaceMeta = spaceM;
	this.viewMeta = viewM;
	this.loadSpaceFromServer(spaceM);
	this.listerly.mainView.setSidebarSpaceAndBreadcrumbs(spaceM, viewM);
}

ListerlySpaceController.prototype.setupView = function() {
	if (!this.viewMeta) {
		this.view = this.space.views[0];
	} else {
		for (var i = 0; i < this.space.views.length; i++) {
			if (this.space.views[i].uuid == this.viewMeta.uuid) {
				this.view = this.space.views[i];
			}
		}
	}
	this.listerly.logObject("loadspace", "Setting up model for view of space: " + this.spaceMeta.id + " and view ", this.view);
}

ListerlySpaceController.prototype.setupModel = function() {
	var model = new ListerlySpaceModel(this.listerly, this);
	var field = undefined;

	for (var i = 0; i < this.space.fields.length; i++) {
		if (this.space.fields[i].uuid == this.view.primaryFieldUuid) {
			field = this.space.fields[i];
			this.listerly.logObject("loadspace", "setupView:Found correct field for view: ", field);
		}
	}
	model.numberOfLists = field.options.length;
	model.lists = [];
	
	for (var i = 0; i < field.options.length; i++) {
		var option = field.options[i];
		var list = {};
		list.title = "Title: " + option.display;
		list.uuid = option.uuid;
		list.color = option.colorCode;
		model.lists.push(list);
		this.listerly.logObject("loadspace", "Created a list: ", list);
	}
	
	model.layoutType = this.view.layoutType;
	
	return model;
}

ListerlySpaceController.prototype.getLayoutType = function() {
	return this.view.layoutType;
}

ListerlySpaceController.prototype.loadSpaceFromServer = function loadSpaceFromServer(spaceM) {
	this.listerly.log("loadspace", "Loading Space: " + spaceM.id);
	$.ajax({
		dataType: "json",
		url: "/api/v1/space/" + spaceM.id,
		data: {},
		async: true,
		cache: false,
		context: this,
		success: function finishedLoadingSpacesCallback(data, textStatus, jqXHR) {
			this.listerly.logObject("loadspace", "Spaces status: " + textStatus, data);
			this.loadSpaceCallback(data.response);
			this.listerly.mainView.unblockMainScreen();
		}
	});
}

ListerlySpaceController.prototype.loadSpaceCallback = function(space) {
	this.listerly.log("loadspace", "Got back a space: " + space.name);
	if (this.setupSpace(space)) {
		this.model = this.setupModel(space)
		this.spaceView = new ListerlySpaceView(this.model);
		this.listerly.mainView.showSubview(this.spaceView);
	} else {
		this.listerly.log("loadspace", "Not showing space. Must have clicked a couple.");
	}
}

function ListerlySpaceModel(listerly, controller) {
	this.log = listerly.log;
	this.logObject = listerly.logObject;
	this.controller = controller;
}


