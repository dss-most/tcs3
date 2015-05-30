/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newQuotationBreadCrumb = Handlebars.compile($("#newQuotationBreadCrumbTemplate").html());
		
		this.$breadcrubmEl = $("#breadcrumb");
		
		
		// now we're ready for initialize the view
		this.searchView = new SearchView({el: '#searchView'});
		this.tableResultView = new TableResultView({el: '#tableResultView'});
		this.quotaionView = new QuotaionView({el: '#quotaionView'});
		
		
	},
    routes: {
        "newQuotationFromTemplate/:id" : "newQuotation",
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	this.tableResultView.$el.empty();
    	this.quotaionView.$el.empty();
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	
    	this.searchView.render();
    	
    },
    
    newQuotation: function(quotationTemplateId) {
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	this.$breadcrubmEl.html(this.newQuotationBreadCrumb());
    	
    	this.quotaionView.newQuotation(quotationTemplateId);
    	
    }
    
});



var SearchView = Backbone.View.extend({
	

    initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.orgSelectionTemplate = Handlebars.compile($("#orgSelectionTemplate").html());
    	
    	
    	// Global variable better be there
    	this.mainOrgCollection = mainOrgs;
    	this.currentMainOrg = userMainOrg;
    	this.groupOrgCollection = groupOrgs.clone();
    	
    	this.quotationTemplates = new App.Pages.QuotationTemplates();
    	
    	
    	this.currentGroupOrg=null;
    	this.nameQuery=null;
    	this.codeQuery=null;
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"click #newQuotationTemplateBtn" : "newQuotationTemplate",
    	"change #mainOrgSlt" : "onChangeMainOrg",
    	"change #groupOrgSlt" : "onChangeGroupOrg",
    	"change .txtInput" : "onChangeTxtInput",
    	"click #searchQuotationTemplateBtn" : "onClickSearchQuotationTemplateBtn"
    	
    },
    
    onChangeTxtInput: function(e) {
    	var value = $(e.currentTarget).val();
    	var field = $(e.currentTarget).attr("data-field");
    	
    	if(field == "name") {
    		this.nameQuery=value;
    	} else if (field == "code") {
    		this.codeQuery=value;
    	}
    	
    },
    

    onClickSearchQuotationTemplateBtn: function(e) {
    	e.preventDefault();
    	appRouter.tableResultView.serachQuotationTemplate(this.nameQuery, this.codeQuery, this.currentMainOrg, this.currentGroupOrg, 1);
    	return false;
    },
  
    newQuotationTemplate : function() {
    	appRouter.navigate("newQuotationTemplate", {trigger: true})
    },
    
    onChangeGroupOrg: function(e) {
    	var newOrgId=e.currentTarget.value;
    	if(newOrgId == 0) {
    		this.currentGroupOrg = null;
    	} else {
    		this.currentGroupOrg = App.Models.Organization.findOrCreate({id: newOrgId});
    	}
    },
    onChangeMainOrg:function(e) {
    	var newOrgId=e.currentTarget.value;
    	this.currentMainOrg = App.Models.Organization.findOrCreate({id: newOrgId});
    	this.groupOrgCollection.url = appUrl('Organization') + '/' + this.currentMainOrg.get('id') + '/children';
    	this.groupOrgCollection.fetch({
			success: _.bind(function() {
				
				
				this.renderOrgSlt();
			},this)
		});
    },
    
    renderOrgSlt: function() {
    	var json = {};
    	json.mainGroup = this.groupOrgCollection.toJSON();
    	this.$el.find('#orgSlt').html(this.orgSelectionTemplate(json));
    	return this;
    },
    
    render: function() {
    	var json = {};
    	json.mainOrg =  this.mainOrgCollection.toJSON();
    	for(var i=0; i< json.mainOrg.length; i++){
    		if(json.mainOrg[i].id == this.currentMainOrg.get('id')) {
    			json.mainOrg[i].selected = true;
    		}
    	}
    	this.$el.html(this.searchViewTemplate(json));
    	this.renderOrgSlt();
    	return this;
    	
    }
});


var TableResultView = Backbone.View.extend({
	initialize: function(options){
		this.tableResultViewTemplate = Handlebars.compile($('#tableResultViewTemplate').html());
		
		this.templates = new App.Pages.QuotationTemplates();
		
		this.currentMainOrg=null;
		this.currentGroupOrg=null;
    	this.nameQuery=null;
    	this.codeQuery=null;
    	this.currentPage=null;
	},
	events: {
		"click .templatesPageNav" : "onClickPageNav",		         
		"change #templatesPageTxt" : "onChangeTemplatesPageTxt",
		"click .templateLnk" : "onClickTemplateLnk"
	},
	onClickTemplateLnk: function(e) {
		e.preventDefault();
		var templateId = $(e.currentTarget).parents('tr').attr('data-id');
		
		appRouter.navigate('newQuotationFromTemplate/' +templateId, {trigger: true});
		return false;
		
	},
	onClickPageNav: function(e) {
		var targetPage=$(e.currentTarget).attr('data-targetPage');
		this.searchAndRenderPage(targetPage);
	},
	onChangeTemplatesPageTxt: function(e) {
		 var oldValue=e.target.getAttribute('value')
		
		var targetPage=$(e.currentTarget).val();
		//now check
		targetPage=parseInt(targetPage);
		if(targetPage > this.templates.page.totalPages) {
			alert('หน้าของข้อมูลที่ระบุมีมากกว่าจำนวนหน้าทั้งหมด กรุณาระบุใหม่');
			$(e.currentTarget).val(oldValue);
			return;
		}
		this.searchAndRenderPage(targetPage);
	},
    render: function() {
    	var json = {};
    	json.page = this.templates.page;
		json.content = this.templates.toJSON();
    	this.$el.html(this.tableResultViewTemplate(json));
    	
    	return this;
    },
    searchAndRenderPage: function(pageNumber) {
    	this.templates.fetch({
    		data: {
    			nameQuery : this.nameQuery,
    			codeQuery : this.codeQuery,
    			mainOrgId : this.currentMainOrg.get('id'),
    			groupOrgId : this.currentGroupOrg == null ? 0 : this.currentGroupOrg.get('id')
    		},
    		type: 'POST',
    		url: appUrl("QuotationTemplate/findByField/page/"+pageNumber),
    		success: _.bind(function(collection, response, options) {
    			this.render();
    		},this)
    	})
    },
	
    serachQuotationTemplate: function(nameQuery, codeQuery, currentMainOrg, currentGroupOrg, pageNumber) {
    	this.nameQuery = nameQuery;
    	this.codeQuery = codeQuery;
    	this.currentMainOrg = currentMainOrg;
    	this.currentGroupOrg = currentGroupOrg;
    	this.currentPage = pageNumber;
    	
    	this.searchAndRenderPage(pageNumber);

    },
});

var CompanyModal = Backbone.View.extend({
	 initialize: function(options){
		 this.companyModalBodyTemplate = Handlebars.compile($('#companyModalBodyTemplate').html());
		 this.companySearchTblTemplate = Handlebars.compile($('#companySearchTblTemplate').html());
		 this.companies = new App.Pages.Companies();
		 this.parentView=null;
	 },
	 events: {
		 "click #companyModalCloseBtn" : "onClickCloseBtn",
		 "click #companyModalSaveBtn" : "onClickSaveBtn",
		 "searched.fu.search #companySrh" : "onSearchCompany",
		 "click .testMethodPageNav" : "onClickPageNav",
		 "change #testMethodPageTxt" : "onChangeTestMethodPageTxt"
	 },
	 onClickSaveBtn: function(e) {
		 var companyId = this.$el.find('.companyRdo:checked').val();
		 
		 var company = App.Models.Company.find({id: companyId});
		 
		 this.parentView.currentQuotation.set('company', company);
		 if(company.get('addresses').length == 0) {
			 this.parentView.currentQuotation.set('address', company.get('oldAddress'));
		 } else {
			 this.parentView.currentQuotation.set('address', company.get('addresses').at(0));
		 }
		 
		 this.parentView.currentQuotation.set('contact', company.get('people').at(0));
		 
		 this.parentView.renderCompany();
		 
		 this.$el.modal('hide');
	 },
	 onClickCloseBtn: function() {
		 this.$el.modal('hide');
		 return false;
	 },
	 onChangeTestMethodPageTxt: function(e) {
			var targetPage=$(e.currentTarget).val();
			//now check
			targetPage=parseInt(targetPage);
			if(targetPage > this.companies.page.totalPages) {
				alert('หน้าของข้อมูลที่ระบุมีมากกว่าจำนวนหน้าทั้งหมด กรุณาระบุใหม่');
				return;
			}
			this.search(targetPage);
		 },
		 onClickPageNav: function(e) {
			var targetPage=$(e.currentTarget).attr('data-targetPage');
			this.search(targetPage);
			
		 },
	 search: function(pageNumber) {
		 var query = this.$el.find('#queryTxt').val();
		 this.companies.fetch({
	    		data: {
	    			nameQuery : query,
	    		},
	    		type: 'POST',
	    		url: appUrl("Company/findByName/page/"+pageNumber),
	    		success: _.bind(function(collection, response, options) {
	    			this.$el.find('.loader').loader('destroy');
					var json={};
					json.page = this.companies.page;
					json.content = this.companies.toJSON();
					
					this.$el.find('#companySearchTbl').html(this.companySearchTblTemplate(json));
	    		},this)
	    	})
	 },
	 onSearchCompany: function(e) {
		 // put spinning
		this.$el.find('#companySearchTbl').html('<div class="loader"></div>');
		this.$el.find('.loader').loader();
		this.search(1);
		
	 },
	 setParentView: function(parent) {
		 this.parentView = parent;
	 },
	 render: function() {
		 this.$el.find('.modal-header span').html("ค้นหาชื่อบริษัท");
		 this.$el.find('.modal-body').html(this.companyModalBodyTemplate());
		 this.$el.find('#companySrh').search();
		 
		 this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		 
		 return this;
	 }
	 
});

var TestMethodItemModal = Backbone.View.extend({
	 initialize: function(options){
		 this.testMethodGroupModalBodyTemplate = Handlebars.compile($('#testMethodGroupModalBodyTemplate').html());
		 this.testMethodItemModalBodyTemplate = Handlebars.compile($('#testMethodItemModalBodyTemplate').html());
		 this.testMethodSearchTblTemplate = Handlebars.compile($('#testMethodSearchTblTemplate').html());
		 this.mode="";
		 this.currentItem = null;
		 this.parentView = null;
		 this.testMethods = new App.Pages.TestMethods();
		 this.selected = new App.Collections.TestMethods();
	 },
	 setParentView : function(view) {
		this.parentView=view; 
	 },
	 events: {
		 "click #testMethodModalCloseBtn" : "onClickCloseBtn",
		 "click #testMethodModalSaveBtn" : "onClickSaveBtn",
		 "searched.fu.search #testMethodSrh" : "onSearchTestMethod",
		 "click .testMethodPageNav" : "onClickPageNav",
		 "change #testMethodPageTxt" : "onChangeTestMethodPageTxt",
		 
		 "click .testMethodRdo" : "onClickTestMethodRdo"
	 },
	 
	 onChangeTestMethodPageTxt: function(e) {
		var targetPage=$(e.currentTarget).val();
		//now check
		targetPage=parseInt(targetPage);
		if(targetPage > this.testMethods.page.totalPages) {
			alert('หน้าของข้อมูลที่ระบุมีมากกว่าจำนวนหน้าทั้งหมด กรุณาระบุใหม่');
			return;
		}
		this.search(targetPage);
	 },
	 onClickPageNav: function(e) {
		var targetPage=$(e.currentTarget).attr('data-targetPage');
		this.search(targetPage);
		
	 },
	 search: function(pageNumber) {
		 var query = this.$el.find('#queryTxt').val();
		 this.testMethods.fetch({
				url: appUrl('TestMethod/findByNameOrCode/page/'+pageNumber),
				type: 'POST',
				data: {
					query: query
				},
				success: _.bind(function(collection, response, options)  {
					this.$el.find('.loader').loader('destroy');
					var json={};
					json.page = this.testMethods.page;
					json.content = this.testMethods.toJSON();
					
					this.$el.find('#testMethodSearchTbl').html(this.testMethodSearchTblTemplate(json));
					
				},this)
			})
	 },
	 
	 onSearchTestMethod: function(e) {
		 // put spinning
		this.$el.find('#testMethodSearchTbl').html('<div class="loader"></div>');
		this.$el.find('.loader').loader();
		this.search(1);
		
		
	 },
	 onClickTestMethodRdo: function(e) {
		 var testMethodId=$(e.currentTarget).val();
		 var testMethod = App.Models.TestMethod.find({id: testMethodId});
		 if($(e.currentTarget).is(':checked')) {
			 this.selected.push(testMethod);
		 } else {
			 this.selected.remove(testMethod);
		 }
	 },
	 onClickSaveBtn: function(e) {
		 if(this.mode == "newTestMethodItem" || this.mode == "editTestMethodItem") {
//			 var testMethodId = this.$el.find('.testMethodRdo:checked').val();
//			 
//			 var testMethod = App.Models.TestMethod.find({id: testMethodId});
//			 
//			 if(testMethod == null) {
//				 alert('กรุณาเลือกรายการทดสอบ');
//				 return;
//			 }
//			 
//			 var findItem =  this.currentQuotation.get('testMethodItems')
//			 		.find(function(item){
//			 			if(item.get('testMethod') != null) { 
//			 				return item.get('testMethod').get('id') == testMethod.get('id');
//			 			}
//			 			return false;
//			 		});
//			 
//			 if(findItem != null) {
//				 alert('รายการทดสอบนี้มีอยู่ในต้นแบบแล้ว กรุณาเลือกรายการใหม่');
//				 return;
//			 }
//			 
//			 // now copy value to current
//			 this.currentItem.set('testMethod', testMethod);
//			 this.currentItem.set('fee', testMethod.get('fee'));
//			 
//			 if(this.currentItem.get('quantity') == null) {
//			 	this.currentItem.set('quantity', 1);
//			 }
			 
			 this.selected.forEach(function(testMethod, index, list) {
				 item = new App.Models.TestMethodQuotationItem();
				 
				 item.set('testMethod', testMethod);
				 item.set('fee', testMethod.get('fee'));
				 if(item.get('quantity') == null) {
					 	item.set('quantity', 1);
				 }
				 
				 var findItem =  this.currentQuotation.get('testMethodItems')
				 		.find(function(item){
				 			if(item.get('testMethod') != null) { 
				 				return item.get('testMethod').get('id') == testMethod.get('id');
				 			}
				 			return false;
				 		});
				 if(findItem == null) {
				 	this.currentQuotation.get('testMethodItems').add(item);
				 }
			 }, this);
			 
			 
			 
		 } else if(this.mode == "newTestMethodGroup" || this.mode == "editTestMethodGroup"){
			 var name = this.$el.find('#testMethodItemName').val();
			 if(name == null || name.length == 0) {
				 alert('กรุณาระบุชื่อกลุ่มรายการ');
				 return;
			 }
			 this.currentItem.set('name', name);
			 
			// do save
			 this.currentQuotation.get('testMethodItems').add(this.currentItem);
		 }
		 
		 
		 this.parentView.renderQuotationItemTbl();
		 this.$el.modal('hide');
	 },
	 setQuotation: function(quotation) {
		this.currentQuotation =  quotation;
	 },
	 setCurrentItem : function(item) {
		this.currentItem = item; 
	 },
	 onClickCloseBtn: function() {
		 this.$el.modal('hide');
		 return false;
	 },
	 setMode: function(mode) {
		this.mode = mode; 
	 },
	 render: function() {
		 var json = {};
		 this.selected.reset();
		 if(this.mode == "newTestMethodItem") {
			 this.currentItem = 
				 new App.Models.TestMethodQuotationItem();
			 this.$el.find('.modal-header span').html("เพิ่มรายการทดสอบ");
			 this.$el.find('.modal-body').html(this.testMethodItemModalBodyTemplate());
			 this.$el.find('#testMethodSrh').search();
			 
		 } else if(this.mode == "newTestMethodGroup"){
			 this.currentItem = 
				 new App.Models.TestMethodQuotationItem();
			 this.$el.find('.modal-header span').html("เพิ่มกลุ่มรายการทดสอบ");
			 this.$el.find('.modal-body').html(this.testMethodGroupModalBodyTemplate());			
		 }  else if(this.mode == "newTestMethodGroup"){
			 this.$el.find('.modal-header span').html("แก้ไขกลุ่มรายการทดสอบ");
			 json = this.currentItem.toJSON();
			 this.$el.find('.modal-body').html(this.testMethodGroupModalBodyTemplate(json));
		 }
		 
		 this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		 
		 return this;
	 }
});

var QuotaionView =  Backbone.View.extend({
	initialize: function(options){
		this.quotationViewTemplate = Handlebars.compile($("#quotationViewTemplate").html());
		this.orgSelectionTemplate = Handlebars.compile($("#orgSelectionTemplate").html());
		this.quotationItemTblTemplate= Handlebars.compile($("#quotationItemTblTemplate").html());
		this.companyInfoTemplate =Handlebars.compile($("#companyInfoTemplate").html());
		this.currentQuotation = null;
		
		this.testMethodItemModal = new TestMethodItemModal({el : '#testMethodModal'});
		this.testMethodItemModal.setParentView(this);
		
		this.companyModal = new CompanyModal({el: '#companyModal'});
		this.companyModal.setParentView(this);
	},
	
	events: {
		"click #backBtn" : "back",
		"change .txtInput" : "onTxtChange",
		"change #groupOrgSlt" : "onMainGroupChange",
		"click #saveQutationBtn" : "onSaveBtn",
		
		"click .itemLnk" : "onClickItem",
		"changed.fu.spinbox .itemQuantitySbx" : "onChangeItemQuantitySbx",
		"click .removeItemBtn" : "onClickRemoveItem",
		
		"click #newTestMethodGroupBtn" : "onClickNewTestMethodGroupBtn",
		"click #newTestMethodItemBtn" : "onClickNewTestMethodItemBtn",
		"click #companyBtn" : "onClickCompanyBtn"
	},
	onClickCompanyBtn: function() {
		this.companyModal.render();
	},
	onChangeItemQuantitySbx: function(e,v) {
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var item = this.currentQuotation.get('testMethodItems').at(index);
		item.set('quantity', v);
		//this.renderQuotationItemTbl();
		$(e.currentTarget).parent().next().html(item.get('quantity') * item.get('testMethod').get('fee'));
		
		var sumTotal = 0;
		this.currentQuotation.get('testMethodItems').each(function(itemLoop) {
			if(itemLoop.get('quantity') != null && itemLoop.get('quantity') > 0) {
				sumTotal += itemLoop.get('quantity') * itemLoop.get('testMethod').get('fee');
			}
		});
		
		this.$el.find('#sumTotalItem').html("<b>" + sumTotal + "</b>");		
	},
	onClickRemoveItem: function(e) {
		
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var item = this.currentQuotation.get('testMethodItems').at(index);
		
		var r = confirm('คุณต้องการลบรายการทดสอบ ' + item.get('testMethod').get('code') + ' ?');
		if (r == true) {
			this.currentQuotation.get('testMethodItems').remove(item);
			this.renderQuotationItemTbl();
		} else {
		    return false;
		} 
		
		return false;
		
	},
	onClickNewTestMethodItemBtn: function(e) {
		this.testMethodItemModal.setMode('newTestMethodItem');
		this.testMethodItemModal.render();
    },
    onClickNewTestMethodGroupBtn: function(e) {
    	this.testMethodItemModal.setMode('newTestMethodGroup');
		this.testMethodItemModal.render();
    },
    onClickItem: function(e) {
    	e.preventDefault();
    	var index = $(e.currentTarget).attr('data-index');
    	var item = this.currentQuotation.get('testMethodItems').at(index);
    	this.testMethodItemModal.setCurrentItem(item);
    	if(item.get('testMethod') == null) {
    		this.testMethodItemModal.setMode('editTestMethodGroup');	
    	} else {
    		this.testMethodItemModal.setMode('editTestMethodItem');
    	}
    	this.testMethodItemModal.render();
    	
    	return false;
    	
    },
	    
	
	onTxtChange: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		if(field == 'estimatedDay') {
			if(isNaN(value)) {
				alert('กรุณาระบุจำนวนวันเป็นตัวเลข');
				$(e.currentTarget).val("");
			} else {
				value = parseInt(value);
			}
		}
		
		this.currentQuotation.set(field, value);
	},
	
	onMainGroupChange: function(e) {
		var mainGroupId = $(e.currentTarget).val();
		if(mainGroupId == 0) {
			this.currentQuotation.set("groupOrg", null);
		} else {
			var groupOrg = App.Models.Organization.findOrCreate({id: mainGroupId});
			this.currentQuotation.set("groupOrg", groupOrg);
		}
	},
	
	onSaveBtn : function(e) {
		if(this.currentQuotation.get('name') == null) {
			alert('กรุณาระบุชื่อผลิตภํณฑ์');
			return;
		}
		
		if(this.currentQuotation.get('code') == null) {
			alert('กรุณาระบุรหัสต้นแบบใบเสนอราคา');
			return;
		}
		
		if(this.currentQuotation.get('groupOrg') == null) {
			alert('กรุณาระบุหน่วยงานรับผิดชอบ');
			return;
		}
		
		if(this.currentQuotation.get('testMethodItems').length == 0) {
			alert('กรุณาระบุรายการทดสอบ');
			return;
		}
		
		this.currentQuotation.save(null, {
			success:_.bind(function(model, response, options) {
				if(response.status != 'SUCCESS') {
					alert(response.status + " :" + response.message);
				}
				this.currentQuotation.set('id', response.data);
				alert("บันทึกข้อมูลแล้ว");
		},this)});
	},
	
	back : function() {
		appRouter.navigate("", {trigger: true});
	},
	newQuotation : function(templateId) {
		var q = new App.Models.Quotation();
		
		// fill info from QuotationTemplate here
		var template =  App.Models.QuotationTemplate.findOrCreate({id: templateId});
		q.set('name', template.get('name'));
		q.set('code', template.get('code'));
		q.set('remark', template.get('remark'));
		q.set('samplePrep', template.get('samplePrep'));
		q.set('sampleNote', template.get('sampleNote'));
		q.set('groupOrg', template.get('groupOrg'));
		q.set('mainOrg', template.get('mainOrg'));
		
		for(var i=0; i<template.get('testMethodItems').length; i++) {
			var templateItem = template.get('testMethodItems').at(i);
			var item = new App.Models.TestMethodQuotationItem();
			item.set('fee', templateItem.get('fee'));
			item.set('name', templateItem.get('name'));
			item.set('quantity', templateItem.get('quantity'));
			item.set('remark', templateItem.get('remark'));
			item.set('testMethod',templateItem.get('testMethod'));
			
			item.set('quotation', q);
			
			q.get('testMethodItems').add(item);
		}
		
		this.currentQuotation = q;
		this.testMethodItemModal.setQuotation(this.currentQuotation);
		this.render();
	},
	editQuotation: function(id) {
		this.currentQuotation = App.Models.Quotation.findOrCreate({id: id});
		this.testMethodItemModal.setQuotation(this.currentQuotation);
		this.render();
		
	},
	
	renderQuotationItemTbl: function() {
		
		var json = this.currentQuotation.get('testMethodItems').toJSON();
		if(json != null) {
			var index=1;
	    	var total=0;
	    	for(var i=0; i< json.length; i++) {
	    		if(json[i].testMethod != null) {
	    			json[i].index = index++;
	    			total += (json[i].quantity)*(json[i].fee);
	    			json[i].totalLine = (json[i].quantity)*(json[i].fee);
	    		}
	    	}
	    	json.total = total;
	    	this.$el.find("#quotationItemTbl")
	    		.html(this.quotationItemTblTemplate(json));
	    	this.$el.find('.itemQuantitySbx').spinbox();
		}
    	return this;
	},
	
	renderCompany: function() {
		var json={};
		if( this.currentQuotation.get('company') !=null) 
			json.company = this.currentQuotation.get('company').toJSON();
		
		if(this.currentQuotation.get('address') != null) {
			var address = this.currentQuotation.get('address')
			json.address = address.toJSON();
			if(json.address.line1 == null || json.address.line1.length == 0) {
				json.address.line1 = json.address.line1FromOldAddress;
				json.address.line2 = json.address.line2FromOldAddress;
				
			}
		}
		
		
		if(this.currentQuotation.get('contact') !=null)
			json.contact = this.currentQuotation.get('contact').toJSON();
		
		this.$el.find('#companyInfoDiv').html(this.companyInfoTemplate(json));
	},
	
    render: function() {
    	var json = {};
    	if(this.currentQuotation != null) {
    		json = this.currentQuotation.toJSON();
    	}
    	
    	this.$el.html(this.quotationViewTemplate(json));
    	this.renderQuotationItemTbl();
    	this.renderCompany();
    	
    	json = {};
    	json.mainGroup = groupOrgs.toJSON();
    	
    	this.$el.find("#quotationMainOrgSlt")
    		.html("<label for='quotationMainOrg'>หน่วยงานที่รับผิดชอบหลัก</label>")
    		.append(this.orgSelectionTemplate(json));
    	
    	var groupOrg =this.currentQuotation.get('groupOrg');
    	if(groupOrg != null && groupOrg.get('id') != null) {
    		this.$el.find('#groupOrgSlt').val(groupOrg.get('id'));
    	}
    	
    	return this;
    }
});
