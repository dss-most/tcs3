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

function index(obj,i) {
	if(obj != null) {		
		return obj[i] != null ? obj[i] : "";
	} else {
		return "";
	}
}

Handlebars.registerHelper('cbkInput', function(model, field, desc, label, labelSize, fieldSize, state) {
	var aValue = "";
	
	if(model != null) {
		aValue = field.split('.').reduce(index, model);
		
	}
	
	var labelStr = '';
	if(labelSize > 0){
		labelStr='col-sm-' +labelSize + ' ';
	}
	
	var fieldStr = '';
	var fieldEndDivStr = '';
	if(fieldSize > 0){
		fieldStr = "	<div class='col-sm-"+fieldSize+"'> \n";
		fieldEndDivStr = "</div>\n";
	}
	
	var requiredTxt = "";
	if(state == "required") {
		requiredTxt = " required='required' ";
	}
	
	var marginStr="";
	if(state=="no-margin-bottom") {
		marginStr = " style='margin-bottom:0px;' ";
	}
	
	var paddingStr = "";
	if(label == null || label.length ==0) {
		paddingStr = " style='padding-top: 0px;' ";
	}
	
	var s = "" +
		"<div class='form-group' "+ marginStr +"> \n" +
		"	<label for='"+ field+"Rdo' class='"+labelStr+"control-label'>"+label+"</label> \n" +
		fieldStr;
	s+= "<div class='checkbox' "+paddingStr+">";
	
	var checkStr = "";
	if(aValue==true) {
		checkStr = " checked='checked' ";
	
	}
	
	s += 	"<label class='radio-inline'> \n" +
			"	<input type='checkbox' class='cbkInput' id='"+field+"CbkInput'" +
			 		    requiredTxt + checkStr +
			"			data-field='"+field+"'> \n" +
			desc + 
			"</label>";
	
	s+= "</div>"
	
	s = s +	fieldEndDivStr +
	"</div>"; 
	
	return new Handlebars.SafeString(s);
	
});


function sltInputHtml(sltList, valueField, descField, model, field, emptyValue, label, labelSize, fieldSize, state){
	var aValue = "";
	
	if(model != null) {
		aValue = field.split('.').reduce(index, model);
	}
	
	var labelStr = '';
	if(labelSize > 0){
		labelStr='col-sm-' +labelSize + ' ';
	}
	
	var fieldStr = '';
	var fieldEndDivStr = '';
	if(fieldSize > 0){
		fieldStr = "	<div class='col-sm-"+fieldSize+"'> \n";
		fieldEndDivStr = "</div>\n";
	}
	
	var requiredTxt = "";
	if(state == "required") {
		requiredTxt = " required='required' ";
	}
	
	var s = "" +
		"<div class='form-group'> \n" +
		"	<label for='"+ field+"Rdo' class='"+labelStr+"control-label'>"+label+"</label> \n" +
		fieldStr;
	
	s += "	<select  class='form-control fromSlt' id='' data-field=''> \n";
	
	if(emptyValue!=null && emptyValue.length >0) {
		s += "<option value=0>"+emptyValue+"</option>\n";
	}
	if(sltList != null) {
	 	for(var i=0; i<sltList.length; i++) {
			var checkStr = "";

			if(aValue==sltList[i][valueField]) {
				checkStr = " selected='selected' ";
				
			}
			
			s += 	"<option id='"+sltList[i][valueField]+"SltInput'" +
					" 			value='"+ sltList[i][valueField] + "' " + requiredTxt + checkStr +
					"			data-field='"+field+"'> \n" +
					sltList[i][descField] + 
					"</option>";
		}
	}
	s+= "</select> ";
		
	
	s = s +	fieldEndDivStr +
		"</div>"; 
	
	return new Handlebars.SafeString(s);
	
}

Handlebars.registerHelper('sltInput', sltInputHtml);

Handlebars.registerHelper('rdoInput', function(rdoList, model, field, label, labelSize, fieldSize, state) {
	var aValue = "";
	
	if(model != null) {
		aValue = field.split('.').reduce(index, model);
		
	}
	
	var labelStr = '';
	if(labelSize > 0){
		labelStr='col-sm-' +labelSize + ' ';
	}
	
	var fieldStr = '';
	var fieldEndDivStr = '';
	if(fieldSize > 0){
		fieldStr = "	<div class='col-sm-"+fieldSize+"'> \n";
		fieldEndDivStr = "</div>\n";
	}
	
	var requiredTxt = "";
	if(state == "required") {
		requiredTxt = " required='required' ";
	}
	
	var s = "" +
		"<div class='form-group'> \n" +
		"	<label for='"+ field+"Rdo' class='"+labelStr+"control-label'>"+label+"</label> \n" +
		fieldStr;
	s += "<div class='radio'>";
	
	for(var i=0; i<rdoList.length; i++) {
		var checkStr = "";
		if(aValue==rdoList[i].value) {
			checkStr = " checked='checked' ";
			
		}
		
		s += 	"<label class='radio-inline'> \n" +
				"	<input type='radio' class='rdoInput' name='"+field+"Rdo' id='"+rdoList[i].id+"RdoInput'" +
				" 			value='"+ rdoList[i].value + "' " + requiredTxt + checkStr +
				"			data-id='"+rdoList[i].id+"' data-field='"+field+"'> \n" +
				rdoList[i].desc + 
				"</label>";
	}
	s += "</div>";
	
	s = s +	fieldEndDivStr +
		"</div>"; 
	
	return new Handlebars.SafeString(s);
	
});

Handlebars.registerHelper('plus', function(a, b) {
	return parseInt(a) + parseInt(b);

});
	

Handlebars.registerHelper('txtInput', function(model, field, label, labelSize, fieldSize, state) {
	
	var aValue = "";
	
	if(model != null) {
		aValue = field.split('.').reduce(index, model);
		
	}
	
	
	var readOnlyTxt = ""; 
	
	if(state == "readonly") {
		readOnlyTxt = "readonly";
	}
	
	var requiredTxt = "";
	if(state == "required") {
		requiredTxt = " required='required' ";
	}
	
	var labelStr = '';
	if(labelSize > 0){
		labelStr='col-sm-' +labelSize + ' ';
	}
	
	var fieldStr = '';
	var fieldEndDivStr = '';
	if(fieldSize > 0){
		fieldStr = "	<div class='col-sm-"+fieldSize+"'> \n";
		fieldEndDivStr = "</div>\n";
	}
	
	var s = "" +
			"<div class='form-group'> \n" +
			"	<label for='"+ field+"Txt' class='"+labelStr+"control-label'>"+label+"</label> \n" +
			fieldStr;
	if(state == "static") {
		s= s+ "	<p class='form-control-static' id='"+ field+"Txt' data-field='"+field+"'>"+ aValue +"</p>\n"
	} else {
		s= s+ "	<input type='text' class='form-control formTxt' id='"+ field+"Txt' data-field='"+field+"' value='"+aValue+"' "+readOnlyTxt+requiredTxt+"></input> \n";
	}
			
	s = s +	fieldEndDivStr +
			"</div>"; 
	
	return new Handlebars.SafeString(s);
});

function __addCommas(nStr)
{
	if(nStr == null || isNaN(nStr)) {
		return '-';
	}
	nStr += '';
	var x = nStr.split('.');
	var x1 = x[0];
	var x2 = x.length > 1 ? '.' + x[1] : '';
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