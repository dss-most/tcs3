/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newQuotationTemplateBreadCrumb = Handlebars.compile($("#newQuotationTemplateBreadCrumbTemplate").html());
		this.$breadcrubmEl = $("#breadcrumb");
		
		
		// now we're ready for initialize the view
		this.searchView = new SearchView({el: '#searchView'});
		this.tableResultView = new TableResultView({el: '#tableResultView'});
		this.quotaionTemplateView = new QuotaionTemplateView({el: '#quotaionTemplateView'});
		
		
	},
    routes: {
        "newQuotationTemplate" : "newQuotationTemplate",
        "QuotationTemplate/:id" : "editQuotationTemplate",
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	this.tableResultView.$el.empty();
    	this.quotaionTemplateView.$el.empty();
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	
    	this.searchView.render();
    	
    },
    
    newQuotationTemplate: function() {
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	this.$breadcrubmEl.html(this.newQuotationTemplateBreadCrumb());
    	
    	this.quotaionTemplateView.render();
    	
    },
    
    editQuotationTemplate: function(id){
    	
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
    	
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"click #newQuotationTemplateBtn" : "newQuotationTemplate",
    	"change #mainOrgSlt" : "onChangeMainOrg"
    	
    },
    
  
    newQuotationTemplate : function() {
    	appRouter.navigate("newQuotationTemplate", {trigger: true})
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

	},
    
    render: function() {
    	return this;
    }
});

var TestMethodItemModal = Backbone.View.extend({
	 initialize: function(options){
		 this.testMethodModalBodyTemplate = Handlebars.compile($('#testMethodModalBodyTemplate').html());
	 },
	 
	 events: {
		 "click #testMethodModalCloseBtn" : "onClickCloseBtn",
		 "click #testMethodModalSaveBtn" : "onClickSaveBtn"
	 },
	 
	 onClickCloseBtn: function() {
		 this.$el.modal('hide');
		 return false;
	 },
	 
	 render: function() {
		 this.$el.find('.modal-header span').html("เพิ่มรายการทดสอบ");
		 this.$el.find('.modal-body').html(this.testMethodModalBodyTemplate());
		 this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		 
		 return this;
	 }
});

var QuotaionTemplateView =  Backbone.View.extend({
	initialize: function(options){
		this.quotationTemplateViewTemplate = Handlebars.compile($("#quotationTemplateViewTemplate").html());
		this.currentQuotaionTemplate = new App.Models.QuotationTemplate();
		this.orgSelectionTemplate = Handlebars.compile($("#orgSelectionTemplate").html());
		
		this.testMethodItemModal = new TestMethodItemModal({el : '#testMehtodModal'});
	},
	
	events: {
		"click #backBtn" : "back",
		"change .txtInput" : "onTxtChange",
		"change #mainGroupSlt" : "onMainGroupChange",
		"click #saveBtn" : "onSaveBtn",
		
		"click #newTestMethodItemBtn" : "onClickNewTestMethodItemBtn"
	},
	
	onClickNewTestMethodItemBtn: function(e) {
		this.testMethodItemModal.render();
    },
	    
	
	onTxtChange: function(e) {
		
		var field = $(e.currentTarget).attr('data-id');
		var value = $(e.currentTarget).val();
		
		this.currentQuotaionTemplate.set(field, value);
	},
	
	onMainGroupChange: function(e) {
		var mainGroupId = $(e.currentTarget).val();
		if(mainGroupId == 0) {
			this.currentQuotaionTemplate.set("groupOrg", null);
		} else {
			var groupOrg = App.Models.Organization.findOrCreate({id: mainGroupId});
			this.currentQuotaionTemplate.set("groupOrg", groupOrg);
		}
	},
	
	onSaveBtn : function(e) {
		this.currentQuotaionTemplate.save(null, {
			success:_.bind(function(model, response, options) {
			alert("saved!");
		},this)});
	},
	
	back : function() {
		appRouter.navigate("", {trigger: true});
	},
	
    render: function() {
    	this.$el.html(this.quotationTemplateViewTemplate());
    	
    	var json = {};
    	json.mainGroup = groupOrgs.toJSON();
    	
    	this.$el.find("#quotationMainOrgSlt")
    		.html("<label for='quotationMainOrg'>หน่วยงานที่รับผิดชอบหลัก</label>")
    		.append(this.orgSelectionTemplate(json));
    	
    	return this;
    }
});
