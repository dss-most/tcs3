/**
 * 
 */
//Globally register console
if (typeof console === 'undefined') {
    console = { log: function() {} };
}

var __shortMonths = ['ม.ค.','ก.พ.','มี.ค.','เม.ย.','พ.ค.','มิ.ย.','ก.ค.','ส.ค.','ก.ย.','ต.ค.','พ.ย.','ธ.ค.'];
var __longMonths = ['มกราคม','กุมภาพันธ์','มีนาคม','เมษายน','พฤษภาคม','มิถุนายน','กรกฎาคม','สิงหาคม','กันยายน','ตุลาคม','พฤศจิกายน','ธันวาคม'];

Handlebars.registerHelper('formatNumber', function(number) {
	return __addCommas(number);
});

Handlebars.registerHelper('formatShortDate', function(dateStr) {
	var s = "";
	var year = parseInt(dateStr[0]+dateStr[1]+dateStr[2]+dateStr[3])+543
	var month = parseInt(dateStr[5]+dateStr[6])-1;
	
	var date = parseInt(dateStr[8]+dateStr[9]);
	
	
	
	return date + " " + __shortMonths[month] + " " + year.toString()[2]+year.toString()[3];
});

Handlebars.registerHelper('formatLongDate', function(dateStr) {
	var s = "";
	var year = parseInt(dateStr[0]+dateStr[1]+dateStr[2]+dateStr[3])+543
	var month = parseInt(dateStr[5]+dateStr[6])-1;
	
	var date = parseInt(dateStr[8]+dateStr[9]);
	
	
	
	return date + " " + __longMonths[month] + " " + year.toString()[0]+year.toString()[1]+year.toString()[2]+year.toString()[3];
});

function __addCommas(nStr)
{
	if(nStr == null || isNaN(nStr)) {
		return '-';
	}
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

function __loaderHtml() {
	return "<div class='loader-center'><div class='loader'></div>";
}

function __loaderInEl($el) {
	$el.html(__loaderHtml());
	$el.find('.loader').loader();
}

function __setSelect(array, model) {
	if(model == null) return;
	
	for(var i=0; i< array.length; i++ ) {
		if(array[i].id == model.get('id')) {
			array[i].selected = true;
			return;
		}
	}
}