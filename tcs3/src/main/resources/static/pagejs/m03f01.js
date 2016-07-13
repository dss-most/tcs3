

/**
 * @class
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newBreadCrumb = Handlebars.compile($("#newBreadCrumbTemplate").html());
		this.editBreadCrumb = Handlebars.compile($("#editBreadCrumbTemplate").html());
		
		this.$breadcrubmEl = $("#breadcrumb");
		
		
		// now we're ready for initialize the view
		this.searchView = new SearchView({el: '#searchView'});
		this.tableResultView = new TableResultView({el: '#tableResultView'});
		this.formView = new FormView({el: '#formView'});
		
		
	},
    routes: {
        "newRequestFromQuotation/:id" : "newRequestFromQuotation",
        "newRequest" : "newRequest",
        "Request/:id" : "showRequest",
        "search" : "searchRequest",
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    searchRequest : function() {
    	this.formView.$el.empty();
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	this.searchView.render();
    	this.tableResultView.render();
    },
    
    defaultRoute: function(action) {
    	this.tableResultView.$el.empty();
    	this.formView.$el.empty();
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	
    	this.searchView.render();
    	
    },
    showRequest: function(requestId) {
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	
    	
    	this.formView.editRequest(requestId);
    	
    },
    updateEditBreadCrump: function(reqNo) {
    	this.$breadcrubmEl.html(this.editBreadCrumb({reqNo: reqNo}));
    },
    newRequestFromQuotation: function(quotationId) {
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	this.$breadcrubmEl.html(this.newBreadCrumb());
    	
    	this.formView.newRequest(quotationId);
    	
    },
    
    newRequest: function() {
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	this.$breadcrubmEl.html(this.newBreadCrumb());
    	
    	this.formView.newRequest();
    	
    }
    
});



var SearchView = Backbone.View.extend({
	

    initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.orgSelectionTemplate = Handlebars.compile($("#orgSelectionTemplate").html());
    	
    	this.newRequestFromQuotationModal = new NewRequestFromQuotationModal({
    		el: "#requstFromQuotationModal"
    	});
    	
    	
    	// Global variable better be there
    	this.mainOrgCollection = mainOrgs;
    	this.currentMainOrg = userMainOrg;
    	this.groupOrgCollection = groupOrgs.clone();
    	
    	this.searchModel = new App.Models.Request();
    	this.searchModel.set("mainOrg", null);
    	
    	this.currentGroupOrg=null;
    	this.nameQuery=null;
    	this.codeQuery=null;
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"click #newRequestBtn" : "newRequest",
    	"click #newRequestFromQuotationBtn" : "newRequestFromQuotation",
    	"change .txtInput" : "onChangeTxtInput",
    	"change .formSlt" : "onChangeFormSlt",
    	"click #searchRequestBtn" : "onClickSearchRequestBtn"
    	
    },
    onChangeFormSlt: function(e) {
    	var id=$(e.currentTarget).val();
    	var field=$(e.currentTarget).attr('data-field'); 

    	var model;
    	
    	if(field == "sampleType") {
    		model = App.Models.SampleType.findOrCreate({id:id});
    	} else if(field=="mainOrg") {
    		if(id == 0) {
        		this.currentGroupOrg = null;
        		model = null;
        		this.groupOrgCollection.reset();
        		this.renderOrgSlt();
        		
        	} else {
        		this.currentMainOrg = App.Models.Organization.findOrCreate({id: id});
        		model=this.currentMainOrg;
        		this.groupOrgCollection.url = appUrl('Organization') + '/' + this.currentMainOrg.get('id') + '/children';
            	this.groupOrgCollection.fetch({
        			success: _.bind(function() {
        				this.renderOrgSlt();
        			},this)
        		});		
        	}
    		
    		
    		
    	} else if(field=="groupOrg") {
    		if(id == 0) {
        		this.currentGroupOrg = null;
        		model = null;
        	} else {
        		this.currentGroupOrg = App.Models.Organization.findOrCreate({id: id});
        		model = this.currentGroupOrg;
        	}
    	} else {
    		return false;
    	}
    	
    	
    	this.searchModel.set(field, model);
    },
    onChangeTxtInput: function(e) {
    	var value = $(e.currentTarget).val();
    	var field = $(e.currentTarget).attr("data-field");
    	
    	this.searchModel.set(field, value);
    },
    

    onClickSearchRequestBtn: function(e) {
    	e.preventDefault();
    	appRouter.tableResultView.search(this.searchModel, 1);
    	return false;
    },
  
    newRequest : function() {
    	
    	appRouter.navigate("newRequest", {trigger: true})
    },
    newRequestFromQuotation: function() {
    	this.newRequestFromQuotationModal.render();
    	
    	
    },
    renderOrgSlt: function() {
    	var json = {};
    	json.mainGroup = this.groupOrgCollection.toJSON();
    	this.$el.find('#orgSlt').html(this.orgSelectionTemplate(json));
    	return this;
    },
    
    render: function() {
    	var json = {};
    	
    	json.sampleTypes=new Array();
		json.sampleTypes.push({id:0,nameTh: 'กรุณาเลือกประเภทตัวอย่าง'});
		$.merge(json.sampleTypes, sampleTypes.toJSON());
    	
    	
    	json.mainOrg = new Array();
    	json.mainOrg.push({id:0,abbr: 'กรุณาเลือกหน่วยงาน'});
    	 
 		$.merge(json.mainOrg, this.mainOrgCollection.toJSON());
    	for(var i=0; i< json.mainOrg.length; i++){
    		if(this.currentMainOrg!= null && json.mainOrg[i].id == this.currentMainOrg.get('id')) {
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
		
		this.requests = new App.Pages.Requests();
		this.searchModel = new App.Models.Request();
		
		this.currentMainOrg=null;
		this.currentGroupOrg=null;
    	this.nameQuery=null;
    	this.codeQuery=null;
    	this.currentPage=null;
	},
	events: {
		"click .templatesPageNav" : "onClickPageNav",		         
		"change #templatesPageTxt" : "onChangeTemplatesPageTxt",
		
		"click .editBtn" : "onClickEditBtn"
	},
	onClickEditBtn: function(e) {
		e.preventDefault();
		var requestId = $(e.currentTarget).parents('tr').attr('data-id');
		appRouter.navigate('Request/' +requestId, {trigger: true});
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
		if(targetPage > this.requests.page.totalPages) {
			alert('หน้าของข้อมูลที่ระบุมีมากกว่าจำนวนหน้าทั้งหมด กรุณาระบุใหม่');
			$(e.currentTarget).val(oldValue);
			return;
		}
		this.searchAndRenderPage(targetPage);
	},
    render: function() {
    	var json = {};
    	json.page = this.requests.page;
		json.content = this.requests.toJSON();
    	this.$el.html(this.tableResultViewTemplate(json));
    	
    	return this;
    },
    searchAndRenderPage: function(pageNumber) {
    	this.$el.html(__loaderHtml());
    	this.requests.fetch({
    		data: JSON.stringify(this.searchModel.toJSON()),
    		type: 'POST',
    		dataType: 'json',
    		contentType: 'application/json',
    		url: appUrl("Request/findByField/page/"+pageNumber),
    		success: _.bind(function(collection, response, options) {
    			this.render();
    		},this)
    	})
    },
	
    search: function(searchModel, pageNumber) {
    	this.currentPage = pageNumber;
    	
    	this.searchModel = searchModel;
    	
    	this.searchAndRenderPage(pageNumber);

    },
});

var NewRequestFromQuotationModal = Backbone.View.extend({
	initialize: function(options){
		 this.requstFromQuotationTemplate = Handlebars.compile($('#requstFromQuotationTemplate').html());
		 this.quotation =null;
		 this.parentView=null;
	 },
	events: {
		"searched.fu.search #quotationSrh" : "onSearchQuotation",
		"click #newRequestFromQuotationModalCloseBtn" : "onClickCloseBtn",
		"keydown #quotationNoTxt" : "onKeyDownQuotationNoTxt"
	},
	onKeyDownQuotationNoTxt: function(e) {
		console.log("keyCode: " + e.keyCode); 
	},
	onClickCloseBtn: function() {
		 this.$el.modal('hide');
		 return false;
	 },
	onSearchQuotation: function(e) {
		if(this.quotation != null) {
			Backbone.Relational.store.unregister(this.quotation);
		}
		
		var quotationNo =  this.$el.find('#quotationNoTxt').val();
		this.$('#requestFromQuotationAlert').empty();
		
		this.quotation = App.Models.Quotation.findOrCreate({quotationNo: quotationNo});


		this.quotation.fetch({
			url: appUrl('Quotation/findByQuotationNo'),
			method: "POST",
			data: {
				quotationNo: quotationNo
			},
			success: _.bind(function(model, response, options)  {
				this.onClickCloseBtn();
				// and redirect to
				appRouter.navigate("newRequestFromQuotation/"+this.quotation.get('id'), {trigger: true})
			},this),
			error: _.bind(function(model, response, options)  {
				this.$('#requestFromQuoationAlert').html('<div class="alert alert-warning" role="alert">ไม่พบหมายเลชใบเสนอราคาที่ระบุ กรุณาระบุใหม่</div>');
			},this)
		});
		
	},
	render: function() {
		this.$el.find('.modal-body').html(this.requstFromQuotationTemplate());
		this.$el.find('#quotationSrh').search();
		 
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		
		setTimeout(function() { $('#quotationNoTxt').focus(); }, 300);
		
		return this;
	}
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
		 
		 this.parentView.currentRequest.set('company', company);
		 if(company.get('addresses').length == 0) {
			 this.parentView.currentRequest.set('addressCompanyAddress', company.get('oldAddress'));
		 } else {
			 this.parentView.currentRequest.set('addressCompanyAddress', company.get('addresses').at(0));
		 }
		 
		 this.parentView.currentRequest.set('contact', company.get('people').at(0));
		 
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
	/**
	 * @memberOf TestMethodItemModal
	 */
	 initialize: function(options){
		 this.testMethodGroupModalBodyTemplate = Handlebars.compile($('#testMethodGroupModalBodyTemplate').html());
		 this.testMethodItemModalBodyTemplate = Handlebars.compile($('#testMethodItemModalBodyTemplate').html());
		 this.testMethodSearchTblTemplate = Handlebars.compile($('#testMethodSearchTblTemplate').html());
		 this.mode="";
		 this.currentItem = null;
		 this.currentSample = null;
		 this.parentView = null;
		 this.testMethods = new App.Pages.TestMethods();
		 this.selected = new App.Collections.TestMethods();
	 },
	 setParentView : function(view) {
		this.parentView=view; 
	 },
	 setCurrentSample: function(sample) {
		this.currentSample = sample; 
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
					if(this.mode=="newTestMethodItem" || this.mode == "newTestMethodItemAllSample") {
						json.editMode = false;
						this.$el.find('#testMethodSearchTbl').html(this.testMethodSearchTblTemplate(json));
					} else {
						json.editMode = true;
						this.$el.find('#testMethodSearchTbl').html(this.testMethodSearchTblTemplate(json));
					}
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
		 if(this.mode == "editTestMethodItem") {
			 var testMethodId = this.$el.find('.testMethodRdo:checked').val();
			 
			 var testMethod = App.Models.TestMethod.find({id: testMethodId});
			 
			 if(testMethod == null) {
				 alert('กรุณาเลือกรายการทดสอบ');
				 return;
			 }
			 
			 var findItem =  this.currentSample.get('jobs')
			 		.find(function(item){
			 			if(item.get('testMethod') != null) { 
			 				return item.get('testMethod').get('id') == testMethod.get('id');
			 			}
			 			return false;
			 		});
			 
			 if(findItem != null) {
				 alert('รายการทดสอบนี้มีอยู่ในต้นแบบแล้ว กรุณาเลือกรายการใหม่');
				 return;
			 }
			 
			 // now copy value to current
			 this.currentItem.set('testMethod', testMethod);
			 this.currentItem.set('fee', testMethod.get('fee'));
			 
			 if(this.currentItem.get('quantity') == null) {
			 	this.currentItem.set('quantity', 1);
			 }
		 } else if(this.mode == "newTestMethodItem") {
			 this.selected.forEach(function(testMethod, index, list) {
				 var item = new App.Models.LabJob();
				 
				 item.set('testMethod', testMethod);
				 item.set('fee', testMethod.get('fee'));
				 if(item.get('quantity') == null) {
					 	item.set('quantity', 1);
				 }
				 
				 var findItem =  this.currentSample.get('jobs')
				 		.find(function(item){
				 			if(item.get('testMethod') != null) { 
				 				return item.get('testMethod').get('id') == testMethod.get('id');
				 			}
				 			return false;
				 		});
				 if(findItem == null) {
				 	this.currentSample.get('jobs').add(item);
				 }
			 }, this);
			 
		 } else if(this.mode == "newTestMethodItemAllSample") {
			 
			 this.currentRequest.get('samples').each( function(sample){
				 
				 
				 for(var i=0; i<this.selected.length; i++) {
					 var itemSelected = this.selected.at(i);
					 
					 var findItem =  sample.get('jobs')
				 		.find(function(job){
				 			if(job.get('testMethod') != null) { 
				 				return job.get('testMethod').get('id') == itemSelected.get('id');
				 			}
				 			return false;
				 	});
					 
					if(findItem == false || findItem == null) {
						 var item = new App.Models.LabJob();
						 
						 item.set('testMethod', itemSelected);
						 item.set('fee', itemSelected.get('fee'));
						 if(item.get('quantity') == null) {
							 	item.set('quantity', 1);
						 }
					 	sample.get('jobs').add(item);
					}
					
				 }
				 this.parentView.renderLabJobTbl(sample);
				 
			 },this);
			 
		 } else if(this.mode == "newTestMethodGroup" || this.mode == "editTestMethodGroup"){
			 var name = this.$el.find('#testMethodItemName').val();
			 if(name == null || name.length == 0) {
				 alert('กรุณาระบุชื่อกลุ่มรายการ');
				 return;
			 }
			 this.currentItem.set('name', name);
			 
			// do save
			 this.currentRequest.get('testMethodItems').add(this.currentItem);
		 }
		 
		 if(this.currentSample != null) {
		 	this.parentView.renderLabJobTbl(this.currentSample);
		 }
		 
		 this.$el.modal('hide');
	 },
	 setRequest: function(request) {
		this.currentRequest =  request;
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
		 if(this.mode == "newTestMethodItem" || this.mode == "newTestMethodItemAllSample") {
//			 this.currentItem = 
//				 new App.Models.TestMethodQuotationItem();
			 
			 
			 this.$el.find('.modal-header span').html("เพิ่มรายการทดสอบ");
			 this.$el.find('.modal-body').html(this.testMethodItemModalBodyTemplate());
			 this.$el.find('#testMethodSrh').search();
			 
		 } else if(this.mode == "newTestMethodGroup"){
			 this.currentItem = 
				 new App.Models.TestMethodQuotationItem();
			 this.$el.find('.modal-header span').html("เพิ่มกลุ่มรายการทดสอบ");
			 this.$el.find('.modal-body').html(this.testMethodGroupModalBodyTemplate());	
			 
		 }  else if(this.mode == "editTestMethodGroup"){
			 this.$el.find('.modal-header span').html("แก้ไขกลุ่มรายการทดสอบ");
			 json = this.currentItem.toJSON();
			 this.$el.find('.modal-body').html(this.testMethodGroupModalBodyTemplate(json));
		 } else if(this.mode = "editTestMethodItem") {
			 this.$el.find('.modal-header span').html("แก้ไขรายการทดสอบ");
			 json = this.currentItem.toJSON();
			 this.$el.find('.modal-body').html(this.testMethodItemModalBodyTemplate(json));
			 this.$el.find('#testMethodSrh').search();
		 }	
		 
		 this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		 
		 return this;
	 }
});

var FormView =  Backbone.View.extend({
	/**
	 * @memberOf FormView
	 */
	initialize: function(options){
		this.requestViewTemplate = Handlebars.compile($("#requestViewTemplate").html());
		this.orgSelectionTemplate = Handlebars.compile($("#orgSelectionTemplate").html());
		
		this.labJobTblTemplate= Handlebars.compile($("#labJobTblTemplate").html());
		
		this.companyInfoTemplate =Handlebars.compile($("#companyInfoTemplate").html());
		
		this.sampleViewTemplate =Handlebars.compile($("#sampleViewTemplate").html());
		this.currentRequest = null;
		
		this.testMethodItemModal = new TestMethodItemModal({el : '#testMethodModal'});
		this.testMethodItemModal.setParentView(this);
		
		this.companyModal = new CompanyModal({el: '#companyModal'});
		this.companyModal.setParentView(this);
	},
	
	events: {
		"click #backBtn" : "back",
		"change .formTxt" : "onTxtChange",
		"change #etcTxt" : "onEtcTxtChange",
		"change .sampleTxtInput" : "onSampleTxtInputChange",
		"change .rdoInput" : "onRdoChange",
		"change #groupOrgSlt" : "onMainGroupChange",
		
		"change .formSlt" : "onSltChange",
		
		"click #saveQutationBtn" : "onSaveBtn",
		"click .cbkInput" : "onCbkClick",
		
		"click .samplePanelCollapse" : "onClicksamplePanelCollapse",
		"click #collapseAllSampleBtn": "onClickCollapseAllSampleBtn",
		"click #openAllSampleBtn": "onClickOpenAllSampleBtn",
		"click .removeSampleBtn" : "onClickRemoveSampleBtn",
		
		"click .itemLnk" : "onClickItem",
		"changed.fu.spinbox .itemQuantitySbx" : "onChangeItemQuantitySbx",
		"changed.fu.spinbox .sampleNumSbx" : "onChangeSampleNumSbx",
		
		"click .removeItemBtn" : "onClickRemoveItem",
		
		"click #newSampleBtn" : "onClickNewSampleBtn",
		"click #addTestMethodAllSampleBtn" : "oncClickAddTestMethodAllSampleBtn",
		"click .newLabJobBtn" : "onClickNewLabJobBtn",
		"click .copySampleBtn" : "onClickCopySampleBtn",
		
		"click #companyBtn" : "onClickCompanyBtn",
		"click .promotionCbx" : "onClickPromotionCbx"
	},
	
	onCbkClick : function(e) {
		var field=$(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).prop('checked');
		this.currentRequest.set(field, value );
		
	},
	onSltChange : function(e) {
		
		
		var field=$(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		var valueField = $(e.currentTarget).attr('data-valueField');
		var target = null;
		
		
		if(field == "mainOrg") {
			target = App.Models.Organization.find({id: value});
		} else if (field == "sampleReceiverOrg") {
			target = App.Models.Organization.find({id: value});
		} else if ( field == "sampleType") {
			target = App.Models.SampleType.find({id: value});
		} else if (field == "contact") {
			target = App.Models.Customer.find({id: value});
		} else if (field == "addressCompanyAddress") {
			target = App.Models.Address.find({id: value});
			
		} else if ((field == "invoiceAddressCompanyAddress" || field == "reportAddressCompanyAddress") && value != 0) {
			target = App.Models.Address.find({id: value});
		} else if ((field == "invoiceAddressCompanyAddress" || field == "reportAddressCompanyAddress") && value == 0) {
			target = this.currentRequest.get('addressCompanyAddress');
		} 
		
		console.log('field: ' + field);
		
		this.currentRequest.set(field, target );
		
	},
	onRdoChange : function(e) {
		var field=$(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).attr('value');
		this.currentRequest.set(field, value );
		
		// change the selection
		if(field == 'sampleOrg' && value == 'SELF') {
			var el = $('#sampleReceiverOrgDiv');
			var html = sltInputHtml(mainOrgs.toJSON(),"id","name", null,"sampleReceiverOrg","กรุณาเลือกหน่วยงาน","หน่วยงานที่รับตัวอย่าง", 3, 9, "required");
			
			el.html(html.string);
			
		} else if(field == 'sampleOrg' && value == 'SARABUN') {
			var el = $('#sampleReceiverOrgDiv');
			var html = sltInputHtml(sarabunOrgs.toJSON(),"id","name", null,"sampleReceiverOrg","","หน่วยงานที่รับตัวอย่าง", 3, 9, "required");
			this.currentRequest.set('sampleReceiverOrg', sarabunOrgs.at(0));
			el.html(html.string);
		}
		
	},
	onClickCompanyBtn: function() {
		this.companyModal.render();
	},
	
	onClickPromotionCbx: function(e) {
		var promotionId=$(e.currentTarget).attr('data-id');
		var promotion = App.Models.Promotion.findOrCreate({id: promotionId});
		var promotionCheck = $(e.currentTarget).is(':checked');
		if(promotionCheck) {
			var pd = new App.Models.RequestPromotionDiscount();
			pd.set("request", this.currentRequest);
			pd.set("promotion", promotion);
			this.currentRequest.get('promotions').add(pd);
			
		} else{
			var promotions = this.currentRequest.get('promotions');
			var pdFound = promotions.find(function(pd) {return pd.get('promotion').get('id') == promotionId});
			
			promotions.remove(pdFound);
		}
		
		this.calculateTotal();
		
	},
	calculateTotal: function() {
		var sumTotal = 0;
		var sumDiscount = 0;
		
		this.currentRequest.get('samples').each(function(sample, sampleIndex) {
			var sumSample = 0;
			
			sample.get('jobs').each(function(job) {
				if(job.get('quantity') != null && job.get('quantity') > 0) {
					sumTotal += job.get('quantity') * job.get('testMethod').get('fee');
					sumSample += job.get('quantity') * job.get('testMethod').get('fee');
				}
			});
			
			// now update sumSample
			$("#labJobTbl_"+ sampleIndex+ " .sumTotalItem").html(__addCommas(sumSample));
			
		});
		
		this.currentRequest.set('currentTotalItems', sumTotal);
		this.$el.find('#sumTotalItem').html("<b>" + __addCommas(sumTotal) + "</b>");	
		this.$el.find('#sampleNumSpan').html(__addCommas(this.currentRequest.get('samples').length));
		
	
		
		
		// reset all discount display first 
		this.$el.find('.promotionDiscountTxt').html(__addCommas(0));
		
		if(this.currentRequest.get('promotions') != null && this.currentRequest.get('promotions').length > 0) {
			var promotions = this.currentRequest.get('promotions');
			for(var i=0; i<promotions.length; i++) {
				var pd = promotions.at(i);
				var discount = (sumTotal * pd.get('promotion').get('percentDiscount') ) / 100;
				
				sumDiscount += discount;
				pd.set('discount', discount);
				
				// only turn on the one we actually have
				this.$el.find('#promotion_' + pd.get('promotion').get('id') ).html("<b>" + __addCommas(discount) + "</b>");
			}
		} 
		
		// now all the fee
		var invoice = this.currentRequest.get('invoices').at(0);
		if(invoice != null) {
			sumTotal = sumTotal + (invoice.get('copyFee'));
			sumTotal = sumTotal + (invoice.get('translateFee'));
			sumTotal = sumTotal + (invoice.get('coaFee'));
			sumTotal = sumTotal + (invoice.get('etcFee'));
		}
		
		sumTotal = sumTotal - sumDiscount;
		
		this.$el.find('#sumTotal').html("<b>" + __addCommas(sumTotal) + "</b>");
		
	},
	onChangeSampleNumSbx: function(e,v) {
		var sampleIndex = $(e.currentTarget).parents('div.panel').attr('data-sampleIndex');
		var item = this.currentRequest.get('samples').at(sampleIndex);
		
		item.set('item', v);
	},
	onEtcTxtChange : function(e) {
		var invoice = this.currentRequest.get('invoices').at(0);
		var value = $(e.currentTarget).val();
		
		invoice.set('etc', value);
	},
	onChangeItemQuantitySbx: function(e,v) {
		// see where is click
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var sampleIndex = $(e.currentTarget).parents('div.samplePanel').attr('data-sampleIndex');
		
		var invoice = this.currentRequest.get('invoices').at(0);
		
		if(!isNaN(index)) {
			var item = this.currentRequest.get('samples').at(sampleIndex).get('jobs').at(index);
			item.set('quantity', v);
	
			$(e.currentTarget).parent().next().html(__addCommas(item.get('quantity') * item.get('testMethod').get('fee')));
		} else {
			var field=$(e.currentTarget).find('input').attr('data-field');
			
			if(field=='etcFee'){
				invoice.set('etcFee', parseInt(v));
			} else {
				invoice.set(field, v);
				var forField = $(e.currentTarget).attr('data-calculateForField');
				invoice.set(forField, v*100);
				$(e.currentTarget).parent().next().html(__addCommas(v*100));
			}
		}
		
		this.calculateTotal();
			
	},
	onClickRemoveItem: function(e) {
		
		var sampleIndex=$(e.currentTarget).parents('div.samplePanel').attr('data-sampleIndex');
		var itemIndex= $(e.currentTarget).parents('tr').attr('data-index');
		var sample = this.currentRequest.get('samples').at(sampleIndex);
		var item = sample.get('jobs').at(itemIndex);
		var str= '';
		
		if(item.get('testMethod') != null) {
			str= 'คุณต้องการลบรายการทดสอบ ' + item.get('testMethod').get('code') + ' ?';
		} else {
			str= 'คุณต้องการลบรายการ ' + item.get('name');
		}
		var r = confirm(str);
		if (r == true) {
			sample.get('jobs').remove(item);
			this.renderLabJobTbl(sample);
		} else {
		    return false;
		} 
		
		return false;
		
	},
	onClickRemoveSampleBtn : function(e) {
		var sampleIndex = $(e.currentTarget).parents('div.samplePanel').attr('data-sampleIndex');
		var toRemoveDiv = $(e.currentTarget).parents('div.samplePanel');
		// we remove 
		
		var samples = this.currentRequest.get('samples')
		var targetSample = samples.at(sampleIndex);
		
		var sampleNum = parseInt(sampleIndex)+1;
		
		toRemoveDiv.removeClass('panel-default');
		toRemoveDiv.addClass('panel-danger');
		
		var str="คุณต้องการลบตัวอย่างที่ #" + sampleNum + " ?"
		
		var r = confirm(str);
		if (r == false) {
			toRemoveDiv.removeClass('panel-danger');
			toRemoveDiv.addClass('panel-default');
			
			return false;
		} 
		
		samples.remove(targetSample);
		
		
		toRemoveDiv.hide('slow', function(){ 
			toRemoveDiv.remove(); 
		
			$('div.samplePanel').each(function(index, div){
				$(div).attr('data-sampleIndex', index);
				$(div).find('span.sampleNum').html(index+1);
			});
		
		
		});
		
		this.calculateTotal();
		console.log(this.currentRequest.get('samples').toJSON());
		
	},
	oncClickAddTestMethodAllSampleBtn: function(e) {
		var samplesLength  = this.currentRequest.get('samples').length;
		
		if(samplesLength == 0) {
			alert('กรุณาเพิมตัวอย่างทดสอบก่อน');
			return false;
		}
		
		this.testMethodItemModal.setMode('newTestMethodItemAllSample');
		this.testMethodItemModal.setCurrentSample(null);
		this.testMethodItemModal.setCurrentItem(null);
		this.testMethodItemModal.render();
		
	},
	onClickNewLabJobBtn: function(e) {
		var sampleIndex = $(e.currentTarget).parents('div.samplePanel').attr('data-sampleIndex');
		var targetSample = this.currentRequest.get('samples').at(sampleIndex);
		
		this.testMethodItemModal.setMode('newTestMethodItem');
		this.testMethodItemModal.setCurrentSample(targetSample);
		this.testMethodItemModal.render();
    },
    
    onClickCollapseAllSampleBtn: function(e) {
    	$('.panel-collapse.collapse.in').collapse('hide');
    	$('.samplePanelCollapse').find('i').removeClass('fa-chevron-down');
    	$('.samplePanelCollapse').find('i').addClass('fa-chevron-right');
    },
    onClickOpenAllSampleBtn: function(e) {
    	$('.panel-collapse.collapse').collapse('show');
    	$('.samplePanelCollapse').find('i').removeClass('fa-chevron-right');
    	$('.samplePanelCollapse').find('i').addClass('fa-chevron-down');
    },
    onClicksamplePanelCollapse: function(e) {
    	e.preventDefault();
    	
    	var targetDiv = $(e.currentTarget).attr('data-target');
    	
    	
    	$('#'+targetDiv).collapse('toggle');
    	$(e.currentTarget).find('i').toggleClass( "fa-chevron-down" );
    	$(e.currentTarget).find('i').toggleClass( "fa-chevron-right" );
    },
    onClickCopySampleBtn: function(e) {
    	var sampleIndex = $(e.currentTarget).parents('div.samplePanel').attr('data-sampleIndex');
    	var sourceSample = this.currentRequest.get('samples').at(sampleIndex);
    	
    	var newSample = this.onClickNewSampleBtn(e);
    	
    	sourceSample.get('jobs').each(function(job) {
    		var newJob = new App.Models.LabJob();
    		newJob.set('testMethod', job.get('testMethod'));
    		newJob.set('fee', job.get('fee'));
    		newJob.set('quantity', job.get('quantity'));
    		
			newSample.get('jobs').add(newJob);
    	});
    	
    	this.renderLabJobTbl(newSample);
    	
    },
    onClickNewSampleBtn: function(e) {
    	var samples = this.currentRequest.get('samples');
		var aSample = new App.Models.RequestSample();
		var sampleIndex = samples.length;
		
		aSample.set('item',1);
		aSample.set('request', this.currentRequest);
		
		samples.add(aSample);
		
		var json = {};
		json.newClick = true;
		json.model = aSample.toJSON();
		json.index = sampleIndex;
    	
    	var html = this.sampleViewTemplate(json);
    	this.$el.find('#sampleDiv').append(html);
    	this.$('.sampleNumSbx').spinbox();
    	
    	var newSampleIndex = this.currentRequest.get('samples').length - 1;
    	var newDiv = $('div.samplePanel[data-sampleindex='+ newSampleIndex +']');
    	setTimeout(function() {
    		newDiv.removeClass( "panel-success" );
     	    newDiv.addClass("panel-default");
    	}, 1500 );
    	
    	//scroll to the new 
    	$(window).scrollTop(newDiv.position().top);
    	
    	return aSample;
    },
    onClickItem: function(e) {
    	e.preventDefault();
    	var index = $(e.currentTarget).attr('data-index');
    	
    	var sampleIndex = $(e.currentTarget).parents('div.samplePanel').attr('data-sampleIndex');
    	var currentSample = this.currentRequest.get('samples').at(sampleIndex);
    	var item = currentSample.get('jobs').at(index);
    	
    	this.testMethodItemModal.setCurrentItem(item);
    	this.testMethodItemModal.setCurrentSample(currentSample);
    	
    	if(item.get('testMethod') == null) {
    		this.testMethodItemModal.setMode('editTestMethodGroup');	
    	} else {
    		this.testMethodItemModal.setMode('editTestMethodItem');
    	}
    	this.testMethodItemModal.render();
    	
    	return false;
    	
    },
    onSampleTxtInputChange: function(e) {
    	var sampleIndex = $(e.currentTarget).parents('div.samplePanel').attr('data-sampleIndex');
    	var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		var sample = this.currentRequest.get('samples').at(sampleIndex);
		sample.set(field,value);
    },
	
	onTxtChange: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		if(field == 'estimatedWorkingDay') {
			if(isNaN(value)) {
				alert('กรุณาระบุจำนวนวันเป็นตัวเลข');
				$(e.currentTarget).val("");
			} else {
				value = parseInt(value);
			}
		}
		
		this.currentRequest.set(field, value);
	},
	
	onMainGroupChange: function(e) {
		var mainGroupId = $(e.currentTarget).val();
		if(mainGroupId == 0) {
			this.currentRequest.set("groupOrg", null);
		} else {
			var groupOrg = App.Models.Organization.findOrCreate({id: mainGroupId});
			this.currentRequest.set("groupOrg", groupOrg);
		}
	},
	
	onSaveBtn : function(e) {
		
		this.$('div.has-error').removeClass('has-error');
		var hasError = false;
		
		this.$('[required="required"]').each(function(index, target){
			var el = $(target);
			if ( el.is( "select" ) ) {
				if(el.val() == null || el.val() <=0 ) { 
					el.parents("div.form-group").addClass("has-error");
					hasError = true;
				}
			}
			
			if( el.is("div.radio")) {
				
				if( el.find('input:checked').val() == null) {
					el.parents("div.form-group").addClass("has-error");
					hasError = true;
				}
			}
			
		});
		
		
		if(hasError) {
			alert('กรุณาตรวจสอบข้อมูล');
			return;
		}
		
		this.currentRequest.save(null, {
			success:_.bind(function(model, response, options) {
				this.currentRequest.set('data',null);
				if(response.status != 'SUCCESS') {
					alert(response.status + " :" + response.message);
					return;
				}
				window.scrollTo(0, 0);
				this.currentRequest.set('id', response.data.id);
				this.currentRequest.set('reqNo', response.data.reqNo);
				this.currentRequest.set('companyName', response.data.companyName);
				
				this.currentRequest.set('address', response.data.address);
				this.currentRequest.set('invoiceAddress', response.data.invoiceAddress);
				this.currentRequest.set('reportAddress', response.data.reportAddress);
				
				var samples = response.data.samples;
				
				for(var i=0;i<samples.length;i++) {
					var thisSample =this.currentRequest.get('samples').at(i); 
					thisSample.set('id', samples[i].id);
					
					var jobs = samples[i].jobs;
					for(var j=0;j<jobs.length;j++) {
						var thisJob = thisSample.get('jobs').at(j);
						thisJob.set('id', jobs[j].id);
					}
				}
				

				alert("บันทึกข้อมูลแล้ว");
				// currentQuotation is 
				
				this.render();
				
				appRouter.navigate("Request/" + this.currentRequest.get('id'), {trigger: false,replace: true});
				
				appRouter.updateEditBreadCrump(this.currentRequest.get("reqNo"));
				
				
		},this)});
	},
	
	back : function() {
		appRouter.navigate("search", {trigger: true});
	},
	newRequest : function(quotationId) {
		var request = new App.Models.Request();
		
		var invoice = new App.Models.Invoice();
		request.get('invoices').add(invoice);
		
		// fill info from QuotationTemplate here
		var quotation ;
		if(quotationId != null) {
			quotation =  App.Models.Quotation.findOrCreate({id: quotationId});	
			quotation.fetch({
				success: _.bind(function() {
					request.set('quotation', quotation);
					this.currentQuotation = quotation;
					
					request.set('company', this.currentQuotation.get('company'));
					request.set('addressCompanyAddress', this.currentQuotation.get('address'));
					request.set('invoiceAddressCompanyAddress', this.currentQuotation.get('address'));
					request.set('reportAddressCompanyAddress', this.currentQuotation.get('address'));
					request.set('addressTitle', this.currentQuotation.get('company').get('nameTh'));
					request.set('invoiceTitle',  this.currentQuotation.get('company').get('nameTh'));
					request.set('reportTitle',  this.currentQuotation.get('company').get('nameTh'));
					
					request.set('contact', this.currentQuotation.get('contact'));
					request.set('mainOrg', this.currentQuotation.get('mainOrg'));
					request.set('sampleType', this.currentQuotation.get('sampleType'));
					request.set('estimatedWorkingDay', this.currentQuotation.get('estimatedDay'));
					
					for(var i=0;i<this.currentQuotation.get('sampleNum'); i++) {
						var sample = new App.Models.RequestSample();
						sample.set('item',1);
						sample.set('request', request);
						for(var j=0; j<this.currentQuotation.get('testMethodItems').length; j++) {
							var item = this.currentQuotation.get('testMethodItems').at(j);
							
							if(item.get('testMethod') != null) {
								var job = new App.Models.LabJob();
								job.set('testMethod', item.get('testMethod'));
								job.set('fee', item.get('fee'));
								job.set('quantity', item.get('quantity'));
								sample.get('jobs').add(job);
							}
						}
						request.get('samples').add(sample);
						
					}
					
					this.currentRequest= request;			
					this.render();
				}, this)
			});
		} else {
			this.currentQuotation = null;
			this.currentRequest= request;
			this.render();
		}
		this.testMethodItemModal.setRequest(this.currentRequest);
		
		
		
	
	},
	editRequest: function(id) {
		this.currentRequest = App.Models.Request.findOrCreate({id: id});
		this.currentRequest.fetch({
			success: _.bind(function() {
				if(this.currentRequest.get('sampleReceiverOrg').get('id') == 3) {
					this.currentRequest.get('sampleReceiverOrg').set('name', 'ฝ่ายสารบรรณ');
					this.currentRequest.set('sampleOrg', 'SARABUN');
					this.currentRequest.set('sampleOrgSARABUN', true);
				} else {
					this.currentRequest.set('sampleOrg', 'SELF');
					this.currentRequest.set('sampleOrgSELF', true);
				}
				
				appRouter.updateEditBreadCrump(this.currentRequest.get("reqNo"));
				
				this.render();
				
				
			},this)
		})
		
		
	},
	fixHelperModified : function(e, tr) {
		var $originals = tr.children();
		var $helper = tr.clone(); 
		$helper.children().each(function(index)  {
			$(this).width($originals.eq(index).width()+10);     
		});
		return $helper;     
	}, 
	reorderItem : function(e,ui) {
		
		var sampleIndex = $(e.target).parent('table').attr('data-sampleIndex');
		
		
		var count = 1;
		var sample = this.currentRequest.get('samples').at(sampleIndex);
		var oldItems = sample.get('jobs');
		var newItems = new App.Collections.LabJob();
		
		
		 $("#labJobTbl_"+ sampleIndex+ " tbody tr").each(function(index, tr) {
			 
			 
			 var itemIndex = $(tr).attr('data-index');
			 var item = oldItems.at(itemIndex);
			 
			 if(item.get('testMethod') != null) {
				 $(tr).find('.index').html(count);
				 count++;
			 }
			 $(tr).attr('data-index', index);
			 newItems.push(item);
		 });
		 
		 
		 
		 sample.set('jobs', newItems);
	},
	renderLabJobTbl: function(targetSample) {
		var sampleIndex = this.currentRequest.get('samples').indexOf(targetSample);
		
		if(sampleIndex > -1) {
		
			var json = targetSample.toJSON();
			if(json.jobs != null) {
				var index=1;
		    	var total=0;
		    	for(var i=0; i< json.jobs.length; i++) {
		    		if(json.jobs[i].testMethod != null) {
		    			json.jobs[i].index = index++;
		    			total += (json.jobs[i].quantity)*(json.jobs[i].fee);
		    			json.jobs[i].totalLine = (json.jobs[i].quantity)*(json.jobs[i].fee);
		    		}
		    	}
		    	json.totalItems = total;
		    	
		    	json.totalItemSampleNum = json.totalItems * json.sampleNum;
	    	
		    	this.$el.find("div.samplePanel[data-sampleIndex="+sampleIndex+"] table.labJobTbl").html(this.labJobTblTemplate(json));
		    	this.$el.find('.testItemSbx').spinbox();
		    	

			}
			
			
			
			 // now make 'em sortable
			 $("#labJobTbl_"+sampleIndex+" tbody").sortable({
				 placeholder: "highlight",
				 handle : ".handle",
				 helper: this.fixHelperModified,
				 stop: _.bind(function( event, ui ) {
					this.reorderItem(event, ui);
				 },this)
			 }).disableSelection();
			
//			 // at las make sure they are shown
//			 if(this.currentRequest.get('samples').length > 0) {
//				 this.$el.find('#quotationItemTbl').show();
//			 }
			 
			 // and calculate total
			 this.calculateTotal();
	    	return this;

		
		}
 		
		

	},
	
	renderCompany: function() {
		var json={};
//		if( this.currentRequest.get('company') !=null) 
//			json.company = this.currentRequest.get('company').toJSON();
//		
//		if(this.currentRequest.get('address') != null) {
//			var address = this.currentRequest.get('address')
//			json.address = address.toJSON();
//			if(json.address.line1 == null || json.address.line1.length == 0) {
//				json.address.line1 = json.address.line1FromOldAddress;
//				json.address.line2 = json.address.line2FromOldAddress;
//				
//			}
//		}
//		
//		
//		if(this.currentRequest.get('contact') !=null)
//			json.contact = this.currentRequest.get('contact').toJSON();
		
		if( this.currentRequest.get('company') !=null) {
			json.company = this.currentRequest.get('company').toJSON();
			
			if(this.currentRequest.get("reqNo") != null) {
				json.hasRequestNo = true;
				// will display as static text with แก้ไข button
				
				json.company.addressDisplay =  this.currentRequest.get("addressTitle") + "<br/>";
				json.company.addressDisplay += this.currentRequest.get('address').get('address') 
					+ " " + this.currentRequest.get('address').get('amphur')  + " " + this.currentRequest.get('address').get('province');
				
				json.company.reportAddressDisplay = 	this.currentRequest.get("reportTitle") + "<br/>";
				json.company.reportAddressDisplay += this.currentRequest.get('reportAddress').get('address') 
					+ " " + this.currentRequest.get('reportAddress').get('amphur')  + " " + this.currentRequest.get('reportAddress').get('province');
				
				json.company.receiptAddressDisplay =   this.currentRequest.get("invoiceTitle") + "<br/>";
				json.company.receiptAddressDisplay  += this.currentRequest.get('invoiceAddress').get('address') 
					+ " " + this.currentRequest.get('invoiceAddress').get('amphur')  + " " + this.currentRequest.get('invoiceAddress').get('province');
			
			} else {
				json.hasRequestNo = false;
				// will display drop down selection
			}
			
			if(json.company.addresses.length > 0) {
				json.useAddresses = true;
			} else {
				json.useAddresses = false;
			}
			this.$el.find("#companyNameThTxt").html(json.company.nameTh);
			
			var address,contact;
			if(this.currentQuotation != null) {
				address = this.currentQuotation.get('address');
				contact = this.currentQuotation.get('contact');
			} else {
				address = this.currentRequest.get('company').get('addresses');
				contact = this.currentRequest.get('customer');
			}
			
			if(contact == null) {
				json.company.people.unshift({id:0,firstName: 'กรุณาเลือกผู้ติดต่อ'});
			} else {
				__setSelect(json.company.people, contact);
			}
			
			// now the receiptAddress and reportAddress 
			json.company.receiptAddresses = this.currentRequest.get('company').get('addresses').toJSON();
			json.company.reportAddresses = this.currentRequest.get('company').get('addresses').toJSON();
			
			if(address == null) {
				json.company.addresses.unshift({id:0,line1: 'กรุณาเลือกที่อยู่'});
			} else {
				__setSelect(json.company.addresses, address);
			}
			
			if(this.currentRequest.get('receiptAddresses') == null) {
				json.company.receiptAddresses.unshift({id:0,line1: 'เหมือนที่อยู่ด้านบน', selected: 'selected'});
			} else {
				__setSelect(json.company.receiptAddresses, this.currentRequest.get('receiptAddresses'));
			}
			
			if(this.currentRequest.get('reportAddresses') == null) {
				json.company.reportAddresses.unshift({id:0,line1: 'เหมือนที่อยู่ด้านบน', selected: 'selected'});
			} else {
				__setSelect(json.company.reportAddresses, this.currentRequest.get('reportAddresses'));
			}
			
			this.$el.find('#companyInfoDiv').html(this.companyInfoTemplate(json));
			
			if(json.hasRequestNo) {
				// add แก้ไข button for address
				this.$el.find('label[for="addressDisplayTxt"]').append('<button id="editAddressBtn" type="button" style="margin-left:14px;" class="btn btn-primary btn-xs editAddressBtn"><i class="fa fa-edit"></i> แก้ไข</button>');
				this.$el.find('label[for="receiptAddressDisplayTxt"]').append('<button id="editReceiptAddressBtn" type="button" style="margin-left:14px;" class="btn btn-primary btn-xs editAddressBtn"><i class="fa fa-edit"></i> แก้ไข</button>');
				this.$el.find('label[for="reportAddressDisplayTxt"]').append('<button id="editReportAddressBtn" type="button" style="margin-left:14px;" class="btn btn-primary btn-xs editAddressBtn"><i class="fa fa-edit"></i> แก้ไข</button>');
			}
			
		} else {
			//กรณีมีบริษัทแล้ว
			
		}
	},
	
    render: function() {
    	
    	var json = {};
    	if(this.currentRequest != null) {
    		json.model = this.currentRequest.toJSON();
    	}
    	
    	json.sampleTypes = sampleTypes.toJSON();
    	json.mainOrgs = mainOrgs.toJSON();
    	
    	
    	json.speedEnum = speedEnum;
    	json.reportLanguageEnum = reportLanguageEnum;
    	json.reportDeliveryMethodEnum = reportDeliveryMethodEnum;
    	json.sampleOrgEnum = sampleOrgEnum;
    	
    	if(promotions.length > 0) {
    		json.hasPromotions = true;
    		json.allpromotions = promotions.toJSON();
    		
    		if(this.currentRequest.get('promotions') != null && 
    				this.currentRequest.get('promotions').length > 0) {
    			// we have promotion
    			for(var i=0; i<this.currentRequest.get('promotions').length; i++) {
    				var promotion_id = this.currentRequest.get('promotions').at(i).get('promotion').get('id');
    				
    				for(var j=0; j< json.allpromotions.length; j++){
    					if(json.allpromotions[j].id == promotion_id) {
    						json.allpromotions[j].checked = true;
    					}
    				}
    				
    			}
    		}
    		
    	}
    	
    	
    	this.$el.html(this.requestViewTemplate(json));
    	
    	
    	for(var i=0; i< this.currentRequest.get('samples').length; i++) {
    		var sample = this.currentRequest.get('samples').at(i);
    		
    		var json = {};
    		json.model = sample.toJSON();
    		json.index = i;
        	var html = this.sampleViewTemplate(json);
        	this.$el.find('#sampleDiv').append(html);
    
    		this.renderLabJobTbl(sample);
    	}
    	
    	//now enable dateinput
    	
    	
 		
    	
    	this.renderCompany();
		
    	
    	json = {};
    	
    	this.$('#estimatedWorkingDayTxt').mask("?9999");
    	
    	this.$('.feeSbx').spinbox();

    	this.$('.sampleNumSbx').spinbox();
    	
    	this.$("#etcFeeSbx").spinbox({step:100, max: 90000});
    	
		this.calculateTotal();
		
		this.$el.find('#serviceNo').mask("SR#99-99-99-9999");
    	
		
		this.$el.find("#reqDatePicker").bootstrapDP({
    	    format: "dd/mm/yyyy",
    	    language: "th",
    	    maxViewMode: 2,
    	    clearBtn: true,
    	    autoclose: true,
    	    orientation: "bottom auto",
    	    todayHighlight: true
    	});
		
		
		// change the selection สถานที่รับตัวอย่าง
		if(this.currentRequest.get('sampleOrgSELF') == true) {
			var el = $('#sampleReceiverOrgDiv');
			var html = sltInputHtml(mainOrgs.toJSON(),"id","name", this.currentRequest.toJSON(),"sampleReceiverOrg","กรุณาเลือกหน่วยงาน","หน่วยงานที่รับตัวอย่าง", 3, 9, "required");
			
			el.html(html.string);
			
		} else if(this.currentRequest.get('sampleOrgSARABUN') == true) {
			var el = $('#sampleReceiverOrgDiv');
			var html = sltInputHtml(sarabunOrgs.toJSON(),"id","name", null,"sampleReceiverOrg","","หน่วยงานที่รับตัวอย่าง", 3, 9, "required");
			this.currentRequest.set('sampleReceiverOrg', sarabunOrgs.at(0));
			el.html(html.string);
		}
		
    	return this;
    }
});

