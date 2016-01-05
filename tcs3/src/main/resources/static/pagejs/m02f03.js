/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.showQuotationBreadCrumb = Handlebars.compile($("#showQuotationBreadCrumbTemplate").html());
		
		this.$breadcrubmEl = $("#breadcrumb");
		
		
		// now we're ready for initialize the view
		this.searchView = new SearchView({el: '#searchView'});
		this.tableResultView = new TableResultView({el: '#tableResultView'});
		this.quotaionView = new QuotaionView({el: '#quotaionView'});
		
		
	},
    routes: {
        "showQuotation/:id" : "showQuotation",
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	this.tableResultView.$el.empty();
    	this.quotaionView.$el.empty();
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	
    	this.searchView.render();
    	
    },
    
    showQuotation: function(id) {
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	this.$breadcrubmEl.html(this.showQuotationBreadCrumb({id: id}));
    	
    	this.quotaionView.showQuotation(id);
    	
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
    	
    	this.quotations = new App.Pages.Quotations();
    	this.searchModel = new App.Models.Quotation();
    	this.searchModel.set('contact', new App.Models.Customer());
    	this.searchModel.set('company', new App.Models.Company());
    	
    	this.currentGroupOrg=null;
    	this.query={};
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"change #mainOrgSlt" : "onChangeMainOrg",
    	"change #groupOrgSlt" : "onChangeGroupOrg",
    	"change .txtInput" : "onChangeTxtInput",
    	"click #searchQuotationBtn" : "onClickSearchQuotationBtn"
    	
    },
    
    onChangeTxtInput: function(e) {
    	var value = $(e.currentTarget).val();
    	var field = $(e.currentTarget).attr("data-field");
    	
    	if(field== "quotationNoTxt") {
    		this.searchModel.set('quotationNo', value);
    	} else if(field=="customerNameTxt") {
    		this.searchModel.get('contact').set('firstName', value);
    	} else if (field =="companyTxt") {
    		this.searchModel.get('company').set('nameTh', value);
    	}
    	
    },
    

    onClickSearchQuotationBtn: function(e) {
    	e.preventDefault();
    	appRouter.tableResultView.serachQuotation(this.searchModel, 1);
    	return false;
    },
    
    onChangeGroupOrg: function(e) {
    	var newOrgId=e.currentTarget.value;
    	if(newOrgId == 0) {
    		this.currentGroupOrg = null;
    	} else {
    		this.currentGroupOrg = App.Models.Organization.findOrCreate({id: newOrgId});
    	}
    	this.searchModel.set('groupOrg',this.currentGroupOrg);
    	this.query.groupOrgId=newOrgId;
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
    	this.searchModel.set('mainOrg',this.currentMainOrg);
    	this.query.mainOrgId=newOrgId;
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
    	json.model = this.searchModel.toJSON();
    	this.$el.html(this.searchViewTemplate(json));
    	this.renderOrgSlt();
    	return this;
    	
    }
});


var TableResultView = Backbone.View.extend({
	/**
	 * @memberOf TableResultView
	 */
	initialize: function(options){
		this.tableResultViewTemplate = Handlebars.compile($('#tableResultViewTemplate').html());
		
		this.quotations = new App.Pages.Quotations();
		this.searchModel = new App.Models.Quotation();
		
		this.currentMainOrg=null;
		this.currentGroupOrg=null;
    	this.nameQuery=null;
    	this.codeQuery=null;
    	this.currentPage=null;
	},
	events: {
		"click .quotationsPageNav" : "onClickPageNav",		         
		"change #quotationsPageTxt" : "onChangeQuotationsPageTxt",
		"click .templateLnk" : "onClickTemplateLnk"
	},
	onClickTemplateLnk: function(e) {
		e.preventDefault();
		var quotationId = $(e.currentTarget).parents('tr').attr('data-id');
		
		appRouter.navigate('showQuotation/' +quotationId, {trigger: true});
		return false;
		
	},
	onClickPageNav: function(e) {
		var targetPage=$(e.currentTarget).attr('data-targetPage');
		this.searchAndRenderPage(targetPage);
	},
	onChangeQuotationsPageTxt: function(e) {
		 var oldValue=e.target.getAttribute('value')
		
		var targetPage=$(e.currentTarget).val();
		//now check
		targetPage=parseInt(targetPage);
		if(targetPage > this.quotations.page.totalPages) {
			alert('หน้าของข้อมูลที่ระบุมีมากกว่าจำนวนหน้าทั้งหมด กรุณาระบุใหม่');
			$(e.currentTarget).val(oldValue);
			return;
		}
		this.searchAndRenderPage(targetPage);
	},
    render: function() {
    	var json = {};
    	json.page = this.quotations.page;
		json.content = this.quotations.toJSON();
    	this.$el.html(this.tableResultViewTemplate(json));
    	
    	return this;
    },
    searchAndRenderPage: function(pageNumber) {
    	this.$el.html(__loaderHtml());
    	this.quotations.fetch({
    		data: JSON.stringify(this.searchModel.toJSON()),
    		type: 'POST',
    		dataType: 'json',
    		contentType: 'application/json',
    		url: appUrl("Quotation/findByField/page/"+pageNumber),
    		success: _.bind(function(collection, response, options) {
    			this.render();
    		},this)
    	})
    },
	
    serachQuotation: function(searchModel, pageNumber) {
    	this.searchModel = searchModel;
    	this.currentPage = pageNumber;
    	
    	this.searchAndRenderPage(pageNumber);

    },
});

var CompanyModal = Backbone.View.extend({

	 
});

var TestMethodItemModal = Backbone.View.extend({

});

var QuotaionView =  Backbone.View.extend({
	initialize: function(options){
		this.quotationViewTemplate = Handlebars.compile($("#quotationViewTemplate").html());
		this.orgSelectionTemplate = Handlebars.compile($("#orgSelectionTemplate").html());
		this.quotationItemTblTemplate= Handlebars.compile($("#quotationItemTblTemplate").html());
		this.companyInfoTemplate =Handlebars.compile($("#companyInfoTemplate").html());
		this.currentQuotation = null;

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
	
	onTxtChange: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
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
	showQuotation: function(id) {
		this.currentQuotation = App.Models.Quotation.findOrCreate({id: id});
		this.currentQuotation.fetch({
			success: _.bind(function() {
				this.render();		
			}, this)
		});
		
		
		
	},
	
	renderQuotationItemTbl: function() {
		
		var json = this.currentQuotation.toJSON();
		if(json.testMethodItems != null) {
			var index=1;
	    	var totalItems=0;
	    	for(var i=0; i< json.testMethodItems.length; i++) {
	    		if(json.testMethodItems[i].testMethod != null) {
	    			json.testMethodItems[i].index = index++;
	    			totalItems += (json.testMethodItems[i].quantity)*(json.testMethodItems[i].fee);
	    			json.testMethodItems[i].totalLine = (json.testMethodItems[i].quantity)*(json.testMethodItems[i].fee);
	    		}
	    	}
	    	json.totalItems = totalItems;
	    	json.totalSampleNumItems = totalItems * json.sampleNum;
	    	json.total = json.totalSampleNumItems;
	    	
	    	if(json.translateFee > 0) {
	    		json.isTranslate = true;
	    		json.total += json.translateFee;
	    	}
	    	
	    	if(json.copyFee > 0) {
	    		json.isCopy = true;
	    		json.total += json.copyFee;
	    	}
	    	
	    	if(json.coaFee > 0) {
	    		json.isCoa = true;
	    		json.total += json.coaFee;
	    	}
	    	
	    	if(json.etcFee > 0) {
	    		json.isEtc = true;
	    		json.total += json.etcFee;
	    	}
	    	
	    	if(json.promotions.length >0) {
	    		for(var i=0; i<json.promotions.length;i++) {
	    			json.total = json.total - json.promotions[i].discount;
	    		}
	    	}
	    	
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
    	
    	return this;
    }
});
