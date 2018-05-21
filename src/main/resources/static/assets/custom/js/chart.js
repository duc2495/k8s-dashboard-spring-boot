var size = 0;
var dataset;
var totalPoints = 13;
var updateInterval = 60000;
var now = new Date().getTime();

var options = {
	series : {
		lines : {
			lineWidth : 1.2
		},
		bars : {
			align : "center",
			fillColor : {
				colors : [ {
					opacity : 1
				}, {
					opacity : 1
				} ]
			},
			barWidth : 500,
			lineWidth : 1
		}
	},
	xaxis : {
		mode : "time",
		tickSize : [ 60, "second" ],
		tickFormatter : function(v, axis) {
			var date = new Date(v);

			if (date.getSeconds() % 60 == 0) {
				var hours = date.getHours() < 10 ? "0" + date.getHours() : date
						.getHours();
				var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes()
						: date.getMinutes();
				return hours + ":" + minutes;
			} else {
				return "";
			}
		},
		axisLabel : "Time",
		axisLabelUseCanvas : true,
		axisLabelFontSizePixels : 12,
		axisLabelFontFamily : 'Verdana, Arial',
		axisLabelPadding : 10
	},
	yaxes : [ {
		min : 0,
		max : 100,
		tickSize : 5,
		tickFormatter : function(v, axis) {
			if (v % 10 == 0) {
				return v + "%";
			} else {
				return "";
			}
		},
		axisLabel : "CPU loading",
		axisLabelUseCanvas : true,
		axisLabelFontSizePixels : 12,
		axisLabelFontFamily : 'Verdana, Arial',
		axisLabelPadding : 6
	} ],
	legend : {
		noColumns : 0,
		position : "nw"
	},
	grid : {
		backgroundColor : {
			colors : [ "#ffffff", "#EDF5FF" ]
		}
	}
};

function convertStrToArr(str) {
	var data = [];
	var arr1 = str.split("]");
	var arr2 = arr1[0].split(" ");
	var temp = [ arr2[0].substr(2, 13), arr2[1] ];
	data.push(temp);
	for (var i = 1; i < arr1.length - 2; i++) {
		var arr2 = arr1[i].split(" ");
		var temp = [ arr2[1].substr(1, 13), arr2[2] ];
		data.push(temp);
	}
	return data;
}

$(document)
		.ready(
				function() {
					var element = document.getElementById('sizeApps');
					if (element != null) {
						size = element.value;
						size = parseInt(size) + 1;
						for (var j = 1; j < size; j++) {
							var strActualValue = document
									.getElementById("flot-actual-value" + j).value;
							var strPredictValue = document
									.getElementById("flot-predict-value" + j).value;
							var actualValue = convertStrToArr(strActualValue);
							var lastActualValue = actualValue[actualValue.length - 1];
							var predictValue = convertStrToArr(strPredictValue);
							var lastPredictValue = predictValue[predictValue.length - 1];
							var check = document
									.getElementById("check-predict-value" + j).value;
							if (check == "true") {
								dataset = [
										{
											label : "Actual CPU:"
													+ lastActualValue[1] + "%",
											data : actualValue,
											lines : {
												fill : true,
												lineWidth : 1.2
											},
											color : "#FF0000"
										},
										{
											label : "Predict CPU:"
													+ lastPredictValue[1] + "%",
											data : predictValue,
											lines : {
												fill : true,
												lineWidth : 1.2
											},
											color : "#00FF00"
										} ];
							} else {
								dataset = [ {
									label : "Actual CPU:" + lastActualValue[1]
											+ "% ",
									data : actualValue,
									lines : {
										fill : true,
										lineWidth : 1.2
									},
									color : "#FF0000"
								} ];
							}
							$
									.plot($("#flot-placeholder" + j), dataset,
											options);
						}
					}
				});
