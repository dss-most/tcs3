/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newCompanyBreadCrumb = Handlebars.compile($("#newCompanyBreadCrumbTemplate").html());
		this.editCompayBreadCrumb = Handlebars.compile($("#editCompanyBreadCrumbTemplate").html());
		this.$breadcrubmEl = $("#breadcrumb");
		
		
		// now we're ready for initialize the view
		this.searchView = new SearchView({el: '#searchView'});
		this.tableResultView = new TableResultView({el: '#tableResultView'});
		this.companyCustomerView = new CompanyCustomerView({el: '#companyCustomerView'});
		
		
	},
    routes: {
        "newCompany" : "newCompany",
        "searchCompany" : "searchCompany",
        "Company/:id" : "editCompany",
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	this.tableResultView.$el.empty();
    	this.companyCustomerView.$el.empty();
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	
    	this.searchView.render();
    	
    },
    
    searchCompany: function() {
    	
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	this.companyCustomerView.$el.empty();
    	this.searchView.render();
    	this.tableResultView.render();
    },
    
    newCompany: function() {
    	this.tableResultView.$el.empty();
    	this.searchView.$el.empty();
    	this.$breadcrubmEl.html(this.newCompanyBreadCrumb());
    	
    	this.companyCustomerView.newCompany();
    },
    
    editCompany: function(id){
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	var json={};
    	json.companyId=id;
    	this.$breadcrubmEl.html(this.editCompayBreadCrumb(json));
    	
    	this.companyCustomerView.editCompany(id);
    }
    
    
});



var SearchView = Backbone.View.extend({
	

    initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.companyNameQuery = null;
    	this.customerNameQuery = null;
    	
    	this.companyNameQuery =null;
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"change .txtInput" : "onChangeTxtInput",
    	"click #searchCompanyBtn" : "onClickSearchCompany",
    	"click #newCompanyBtn" : "onClickNewCompanyBtn"
    		
    },
    onClickNewCompanyBtn: function(e) {
    	appRouter.navigate("newCompany", {trigger: true});
    },
    onClickSearchCompany: function(e) {
    	e.preventDefault();
    	appRouter.tableResultView.searchCompanyCustomer(this.companyNameQuery, this.customerNameQuery, 1);
    	return false;
    },
    
    onChangeTxtInput: function(e) {
    	var value = $(e.currentTarget).val();
    	var field = $(e.currentTarget).attr('data-field');
    	
    	if(field == 'companyName') {
    		this.companyNameQuery = value;
    	} else if(field = 'customerName') {
    		this.customerNameQuery = value;
    	}
    },
    
    render: function() {
    	var json = {};
    	json.companyNameQuery = this.companyNameQuery;
    	this.$el.html(this.searchViewTemplate(json));
    	return this;
    	
    }
});


var TableResultView = Backbone.View.extend({
	initialize: function(options){
		this.companyNameQuery = null;
		this.customerNameQuery = null;
		this.currentPage = null;
	
		this.companies = new App.Pages.Companies();
		
		this.companySearchTblTemplate = Handlebars.compile($("#companySearchTblTemplate").html());
	},
	
	events: {
		"click .companyPageNav" : "onClickPageNav",		         
		"change #companyPageTxt" : "onChangeCompanyPageTxt",
		"click .companyLnk" : "onClickCompanyLnk"
	},
	
	onClickCompanyLnk: function(e) {
		e.preventDefault();
		
		var companyId = $(e.currentTarget).parents('tr').attr('data-id');
		
		
		appRouter.navigate('Company/' +companyId, {trigger: true});
		
		return false;
	},
	onChangeCompanyPageTxt: function(e) {
		 var oldValue=e.target.getAttribute('value')
		
		var targetPage=$(e.currentTarget).val();
		//now check
		targetPage=parseInt(targetPage);
		if(targetPage > this.companies.page.totalPages) {
			alert('หน้าของข้อมูลที่ระบุมีมากกว่าจำนวนหน้าทั้งหมด กรุณาระบุใหม่');
			$(e.currentTarget).val(oldValue);
			return;
		}
		this.searchAndRenderPage(targetPage);
	},
	onClickPageNav: function(e) {
		var targetPage=$(e.currentTarget).attr('data-targetPage');
		this.searchAndRenderPage(targetPage);
	},
	searchCompanyCustomer : function(companyNameQuery, customerNameQuery, pageNumber) {
		this.companyNameQuery = companyNameQuery;
    	this.customerNameQuery = customerNameQuery;
    	this.currentPage = pageNumber;
    	
    	this.searchAndRenderPage(pageNumber);

    },
    
    searchAndRenderPage: function(pageNumber) {
		__loaderInEl(this.$el);
    	this.companies.fetch({
    		data: {
    			nameQuery : this.companyNameQuery,
    		},
    		type: 'POST',
    		url: appUrl("Company/findByName/page/"+pageNumber),
    		success: _.bind(function(collection, response, options) {
    			this.render();
    		},this)
    	})
    },
    render: function() {
    
	    var json = {};
		json.page = this.companies.page;
		json.content = this.companies.toJSON();
		this.$el.html(this.companySearchTblTemplate(json));
		
		return this;
    }
});

var AddressModalView = Backbone.View.extend({
	initialize: function(options){if(options != null) {
			if(options.parentView != null) {
				this.parentView = options.parentView;
			}
		}
		this.currentCompany = null;
		this.currentAddress = null;
		
		this.provinces = new App.Collections.Provinces();
		this.districts = new App.Collections.Districts();
		this.provinces.fetch({
			url: appUrl('Global/provinces'),
			type: 'GET'
		});
		this.addressModalBodyTemplate = Handlebars.compile($("#addressModalBodyTemplate").html());
		this.districtSltTemplate = Handlebars.compile($("#districtSltTemplate").html());
	},
	events : {
		"click #addressModalCloseBtn" : "onClickCloseBtn",
		 "click #addressModalSaveBtn" : "onClickSaveBtn",
		"change .txtInput" : "onChangeTxtInput",
		"change #provinceSlt" : "onChangeProvinceSlt",
		"change #districtSlt" : "onChangeDistrictSlt"
	},
	onClickSaveBtn: function(e) {
		// validate input here...
		
		 // do save
		 this.currentCompany.get('addresses').add(this.currentAddress);
		 this.parentView.renderAddressTbl();
		 this.$el.modal('hide');
	 },
	onClickCloseBtn: function() {
		 this.$el.modal('hide');
		 return false;
	 },
	onChangeTxtInput: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		this.currentAddress.set(field, value);
	},
	onChangeProvinceSlt: function(e) {
		var provinceId = $(e.currentTarget).val();
		this.renderDistrictSlt(provinceId, null);
		
		var province = App.Models.Province.findOrCreate({id: provinceId});
		this.currentAddress.set('province', province);
	},
	onChangeDistrictSlt: function(e) {
		var districtId = $(e.currentTarget).val();
		var district = App.Models.District.findOrCreate({id: districtId});
		this.currentAddress.set('district', district);
	},
	renderDistrictSlt: function(provinceId, districtId) {
		this.districts.fetch({
			url: appUrl('Global/province/' + provinceId + "/districts"),
			type: 'GET',
			success: _.bind(function() {
				var json=this.districts.toJSON();
				this.$el.find('#districtSltDiv').html(this.districtSltTemplate(json));
				
				if(districtId != null) {
					this.$el.find('#districtSlt').val(districtId);
				}
				
			},this)
		});
	},
	
	render : function() {
		var json=this.currentAddress.toJSON();
		json.provinces = this.provinces.toJSON();
		
		this.$el.find('.modal-body').html(this.addressModalBodyTemplate(json));
		
		if(this.currentAddress.get('province') != null && this.currentAddress.get('province').get('id') != null) {
			var provinceId =this.currentAddress.get('province').get('id');
			this.$el.find('#provinceSlt').val(this.currentAddress.get('province').get('id'));
			var districtId = null;
			if(this.currentAddress.get('district') != null && this.currentAddress.get('district').get('id') !=null) {
				districtId = this.currentAddress.get('district').get('id');
			}
			this.renderDistrictSlt(provinceId, districtId);
		} else {
			this.$el.find('#districtSltDiv').html(this.districtSltTemplate())
		}
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	},
	setCurrentCompany: function(company) {
		this.currentCompany = company;
	},
	setCurrentAddressAndRender: function(address){
		this.currentAddress = address;
		this.$el.find('.modal-header span').html("แก้ไขรายการที่อยู่");
		this.render();
		
		return this;
	},
	newAddressAndRender : function() {
		this.currentAddress = new App.Models.Address();
		this.$el.find('.modal-header span').html("เพิ่มรายการที่อยู่");
		this.render();
		
		return this;
	}
});
var PersonModalView = Backbone.View.extend({
	initialize: function(options){
		if(options != null) {
			if(options.parentView != null) {
				this.parentView = options.parentView;
			}
		}
		this.currentCompany = null;
		this.currentPerson = null;
		
		this.personModalBodyTemplate = Handlebars.compile($("#personModalBodyTemplate").html());
	},
	events : {
		"click #personModalCloseBtn" : "onClickCloseBtn",
		 "click #personModalSaveBtn" : "onClickSaveBtn",
		"change .txtInput" : "onChangeTxtInput"
	},
	onClickSaveBtn: function(e) {
		// validate input here...
		
		 // do save
		 this.currentCompany.get('people').add(this.currentPerson);
		 this.parentView.renderPersonTbl();
		 this.$el.modal('hide');
	 },
	onClickCloseBtn: function() {
		 this.$el.modal('hide');
		 return false;
	 },
	onChangeTxtInput: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		this.currentPerson.set(field, value);
	},
	render : function() {
		var json=this.currentPerson.toJSON(); 
		
		this.$el.find('.modal-body').html(this.personModalBodyTemplate(json));
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	},
	setCurrentCompany: function(company) {
		this.currentCompany = company;
	},
	setCurrentPersonAndRender: function(person){
		this.currentPerson = person;
		this.$el.find('.modal-header span').html("แก้ไขรายการผู้ติดต่อ");
		this.render();
		
		return this;
	},
	newPersonAndRender : function() {
		this.currentPerson = new App.Models.Customer();
		this.$el.find('.modal-header span').html("เพิ่มรายการผู้ติดต่อ");
		this.render();
		
		return this;
	}
});
var CompanyCustomerView =  Backbone.View.extend({
	initialize: function(options){
		this.company = null;
			
		this.companyViewTemplate = Handlebars.compile($("#companyViewTemplate").html());
		this.addressTblTemplate = Handlebars.compile($("#addressTblTemplate").html());
		this.personTblTemplate = Handlebars.compile($("#personTblTemplate").html());
		
		this.addressModalView = new AddressModalView({el : '#addressModal', parentView: this});
		this.personModalView = new PersonModalView({el : '#personModal', parentView: this});
	},
	
	events: {
		"click #newPersonBtn" : "onClickNewPersonBtn",		         
		"click #newAddressBtn" : "onClickNewAddressBtn",
		"click .personLnk" : "onClickPersonLnk",
		"click .addressLnk" : "onClickAddressLnk",
		"click .removePersonBtn" : "onClickRemovePersonBtn",
		"click .removeAddressBtn" : "onClickRemoveAddressBtn",
		"change .txtInput" : "onChangeTxtInput",
		"click #saveBtn" : "onClickSaveBtn",
		"click #backBtn" : "onClickBackBtn"
	},
	onChangeTxtInput: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		this.company.set(field, value);
	},
	onClickSaveBtn: function(e) {
		
		this.company.save(null, {
			success:_.bind(function(model, response, options) {
				if(response.status != 'SUCCESS') {
					alert(response.status + " :" + response.message);
				}
				this.company.set('id', response.data);
				alert("บันทึกข้อมูลแล้ว");
		},this)});
	},
	
	onClickBackBtn : function() {
		appRouter.navigate("searchCompany", {trigger: true});
	},
	onClickRemoveAddressBtn: function(e) {
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var item = this.company.get('addresses').at(index);
		
		var r = confirm('คุณต้องการลบรายการที่อยู่ ' + item.get('line1') + " " + item.get('line2'));
		if (r == true) {
			this.company.get('addresses').remove(item);
			this.renderAddressTbl();
		} else {
		    return false;
		} 
		
		return false;
	},
	onClickRemovePersonBtn: function(e) {
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var item = this.company.get('people').at(index);
		
		var r = confirm('คุณต้องการลบรายการผู้ติดต่อ ' + item.get('firstName') + " " + item.get('lastName'));
		if (r == true) {
			this.company.get('people').remove(item);
			this.renderPersonTbl();
		} else {
		    return false;
		} 
		
		return false;
	},
	onClickAddressLnk: function(e) {
		e.preventDefault();
    	var index = $(e.currentTarget).attr('data-index');
    	var address = this.company.get('addresses').at(index);
    	this.addressModalView.setCurrentCompany(this.company);
    	this.addressModalView.setCurrentAddressAndRender(address);
    	
    	return false;
	},
	onClickNewAddressBtn: function() {
		this.addressModalView.setCurrentCompany(this.company);
		this.addressModalView.newAddressAndRender();
	},
	onClickPersonLnk: function(e) {
		e.preventDefault();
    	var index = $(e.currentTarget).attr('data-index');
    	var person = this.company.get('people').at(index);
    	this.personModalView.setCurrentCompany(this.company);
    	this.personModalView.setCurrentPersonAndRender(person);
    	
    	return false;
	},
	onClickNewPersonBtn: function() {
		this.personModalView.setCurrentCompany(this.company);
		this.personModalView.newPersonAndRender();
	},
	
	newCompany: function() {
		this.company = new App.Models.Company();
		this.render();
	},
	editCompany: function(companyId) {
		this.company = App.Models.Company.findOrCreate({id: companyId});
		
		if(this.company.get('addresses').length == 0) {
			var oldAddress = this.company.get('oldAddress');
			var address = new App.Models.Address();
			address.set('line1', oldAddress.get('line1FromOldAddress') );
			address.set('line2', oldAddress.get('line2FromOldAddress') );
			address.set('district', oldAddress.get('district'));
			address.set('province', oldAddress.get('province'));
			address.set('mobilePhone', oldAddress.get('mobilePhone'));
			address.set('fax', oldAddress.get('fax'));
			address.set('phone', oldAddress.get('phone'));
			address.set('zipCode', oldAddress.get('zipCode'));
			
			this.company.get('addresses').add(address);
			
		}
		this.render();
	},
	renderPersonTbl : function() {
		var json=this.company.get('people').toJSON();
		for(var i=0; i<json.length; i++) {
			json[i].index=i+1;
		}
		this.$el.find('#personTbl')
			.html(this.personTblTemplate(json));
		return this;
	},
	renderAddressTbl : function() {
		var json=this.company.get('addresses').toJSON();
		for(var i=0; i<json.length; i++) {
			json[i].index=i+1;
		}
		this.$el.find('#addressTbl')
			.html(this.addressTblTemplate(json));
		return this;
	},
	render: function() {
		var json  = this.company.toJSON();
		this.$el.html(this.companyViewTemplate(json));
		
		this.renderAddressTbl();
		this.renderPersonTbl();
		return this;
	}

});
