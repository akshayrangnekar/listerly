
function ListerlyStorage() {
}

ListerlyStorage.prototype.set = function(field, value) {
	$.jStorage.set(field, value, {TTL: 6048000});
}

ListerlyStorage.prototype.get = function(field) {
	return $.jStorage.get(field);
}
