var exec = require('cordova/exec');
module.exports = {
	startNavi: function(start, end, success, error) {
		if (!start.lat || !start.lon || !end.lat || !end.lon) {
			error("Missing parameter ");
			return;
		}
		if (device.platform === "iOS") {
			var pointsInfo = {
				startPointX: start.lon,
				startPointY: start.lat,
				endPointX: end.lon,
				endPointY: end.lat,
			};
			exec(success, error, "Baidu", "startNavi", [pointsInfo]);
		} else {
			exec(success, error, "NavigatorPlugin", "startNavi", [start.lat, start.lon, start.des ? start.des : "", end.lat, end.lon, end.des ? end.des : ""]);
		}
	}
};