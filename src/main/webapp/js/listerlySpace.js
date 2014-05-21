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
	this.listerly.logObject("loadspace.2", "Setting up model for view of space: " + this.spaceMeta.id + " and view ", this.view);
}

ListerlySpaceController.prototype.setupModel = function() {
	var model = new ListerlySpaceModel(this.listerly, this);
	
	model.init(this.space, this.view);
	this.listerly.logObject("loadspace.2", "Set up model", model);
	
	return model;
}

ListerlySpaceController.prototype.getLayoutType = function() {
	return this.view.layoutType;
}

ListerlySpaceController.prototype.loadSpaceFromServer = function loadSpaceFromServer(spaceM) {
	this.listerly.log("loadspace.2", "Loading Space: " + spaceM.id);
	$.ajax({
		dataType: "json",
		url: "/api/v1/space/" + spaceM.id,
		data: {},
		async: true,
		cache: false,
		context: this,
		success: function finishedLoadingSpacesCallback(data, textStatus, jqXHR) {
			this.listerly.logObject("loadspace.2", "Spaces status: " + textStatus, data);
			this.loadSpaceCallback(data.response);
			this.listerly.mainView.unblockMainScreen();
		}
	});
}

ListerlySpaceController.prototype.loadSpaceCallback = function(space) {
	this.listerly.log("loadspace.2", "Got back a space: " + space.name);
	if (this.setupSpace(space)) {
		this.model = this.setupModel(space)
		this.spaceView = new ListerlySpaceView(this.model);
		this.listerly.mainView.showSubview(this.spaceView);
	} else {
		this.listerly.log("loadspace", "Not showing space. Must have clicked a couple.");
	}
}

function ListerlySpaceModel(listerly, controller) {
	this.listerly = listerly;
	this.controller = controller;
}

ListerlySpaceModel.prototype.init = function(space, view) {
	var field = undefined;
	var fieldMap = {};

	// Find the correct field
	for (var i = 0; i < space.fields.length; i++) {
		if (space.fields[i].uuid == view.primaryFieldUuid) {
			field = space.fields[i];
			this.listerly.logObject("loadspace.2", "setupView:Found correct field for view: ", field);
		}
		fieldMap[space.fields[i].uuid] = space.fields[i];
	}
	
	this.fieldMap = fieldMap;

	// this.setField(field);
	this.setupModelLists(field);
	this.layoutType = view.layoutType;
	this.setupModelItems(space.items, field, fieldMap, view);
	this.removeTempMaps();
	this.listerly.logObject("loadspace", "Finished model init", this);
}

ListerlySpaceModel.prototype.removeTempMaps = function() {
	this.listMap = undefined;
	this.fieldMap = undefined;
}

ListerlySpaceModel.prototype.setupModelLists = function(field) {
	this.listerly.logObject("loadspace", "Field: ", field);
	this.numberOfLists = field.options.length;
	this.lists = [];
	
	this.listMap = {};
	
	for (var i = 0; i < field.options.length; i++) {
		var option = field.options[i];
		var list = {};
		list.title = "" + option.display;
		list.uuid = option.uuid;
		list.color = option.colorCode;
		list.items = new Array();
		this.lists.push(list);
		this.listMap[list.uuid] = list;
		this.listerly.logObject("loadspace.2", "Created a list: ", list);
	}
}

ListerlySpaceModel.prototype.setupModelItems = function(items, field, fieldMap, view) {
	var itemMap = {};
	for (var i in items) {
		var item = items[i];
		var modelItem = {};
		modelItem.id = item.id;
		modelItem.displayFields = new Array();
		this.listerly.logObject("loadspace.2", "Item: ", item);
		itemMap[item.id] = item;
		for (var j in item.values) {
			var itemValue = item.values[j];
			if (itemValue.fieldId == field.uuid) {
				var itemList = itemValue.fieldValue;
				this.listMap[itemList].items.push(modelItem);
			} else {
				var theField = fieldMap[itemValue.fieldId];
				var itemDisplayField = {"type": theField.type};
				switch(theField.type) {
				case "text":
					itemDisplayField.value = itemValue.fieldValue;
					modelItem.displayFields.push(itemDisplayField);
				    break;
				case "select":
				case "select-fixed":
					for (var k in theField.options) {
						if (theField.options[k].uuid == itemValue.fieldValue) {
							itemDisplayField.value = theField.options[k];
						}
					}
					modelItem.displayFields.push(itemDisplayField);
				    break;
				case "booleanDate":
					if (view.checkboxFieldUuid == itemValue.fieldId) {
						//console.log("Found checkbox field");
						if (itemValue.fieldValue) {
							modelItem.checked = true;
						} else {
							modelItem.checked = false;
						}
					} 
					break;
				default:
				    console.log(theField.type);
				}
			}
		}
	}
	this.listerly.logObject("loadspace.2", "ItemMap: ", itemMap);
	this.listerly.logObject("loadspace.2", "ListMap: ", this.listMap);
	this.listerly.logObject("loadspace.2", "Lists: ", this.lists);
}

