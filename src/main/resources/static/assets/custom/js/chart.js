var cpuPredict = [], cpuCurrent = [];
var listData = [];
var number = 3;
var dataset;
var totalPoints = 10;
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

function initData() {

	for (var j = 0; j < number; j++) {
		var fixTime = now - totalPoints * updateInterval - now % 60000;
		var cpuPredictTemp = [], cpuCurrentTemp = [];
		for (var i = 0; i < totalPoints; i++) {
			var temp = [ fixTime += updateInterval, 10 ];
			cpuPredictTemp.push(temp);
			cpuCurrentTemp.push(temp);
		}
		for (var i = 0; i < 3; i++) {
			var temp = [ fixTime += updateInterval, 10 ];
			cpuPredictTemp.push(temp);
		}
		cpuPredict.push(cpuPredictTemp);
		cpuCurrent.push(cpuCurrentTemp);
	}

}

function GetData() {
	$.ajaxSetup({
		cache : false
	});

	$.ajax({
		url : "http://www.jqueryflottutorial.com/AjaxUpdateChart.aspx",
		dataType : 'json',
		success : update,
		error : function() {
			setTimeout(GetData, updateInterval);
		}
	});
}

var temp;

function update(_data) {
	cpu.shift();
	cpuCore.shift();

	now += updateInterval - now % 60000;

	temp = [ now + 3 * updateInterval, _data.cpu ];
	cpu.push(temp);

	temp = [ now, _data.core ];
	cpuCore.push(temp);

	dataset = [ {
		label : "Predict CPU:" + _data.cpu + "%",
		data : cpu,
		lines : {
			fill : true,
			lineWidth : 1.2
		},
		color : "#00FF00"
	}, {
		label : "Current CPU:" + _data.core + "%",
		data : cpuCore,
		lines : {
			fill : true,
			lineWidth : 1.2
		},
		color : "#FF0000"
	} ];

	$.plot($("#flot-placeholder1"), dataset, options);
	setTimeout(GetData, updateInterval);
}

$(document).ready(function() {

	for (var j = 0; j < number; j++) {
		initData();

		dataset = [ {
			label : "Predict CPU",
			data : cpuPredict[j],
			lines : {
				fill : true,
				lineWidth : 1.2
			},
			color : "#00FF00"
		}, {
			label : "Current CPU",
			data : cpuCurrent[j],
			lines : {
				fill : true,
				lineWidth : 1.2
			},
			color : "#FF0000"
		} ];

		$.plot($("#flot-placeholder" + (j + 1)), dataset, options);
		setTimeout(GetData, updateInterval);
	}
});
