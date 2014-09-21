/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.searchView = new SearchView({el: '#searchView'});
		this.tableResultView = new TableResultView({el: '#tableResultView'});
		this.quotaionTemplateView = new QuotaionTemplateView({el: '#quotaionTemplateView'});
		
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newQuotationTemplateBreadCrumb = Handlebars.compile($("#newQuotationTemplateBreadCrumbTemplate").html());
		this.$breadcrubmEl = $("#breadcrumb");
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
    	console.log("newQuotationTemplate");
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
    	
    	this.mainOrgCollection = new App.Collections.Organizations();
    	this.groupOrgCollection = new App.Collections.Organizations();
    
    	this.mainOrgCollection.url = appUrl('Organization/')
    	this.currentMainOrg = App.Models.Organization.findOrCreate({id: mainOrgId});
    	
    	this.mainOrgCollection.fetch({
    		success: _.bind(function() {
    			
    			this.groupOrgCollection.url = appUrl('Organization') + '/' + this.currentMainOrg.get('id') + '/children';
    			this.groupOrgCollection.fetch({
    				success: _.bind(function() {
    					
    				},this)
    			});
    		}, this)
    	});
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
    	console.log(json);
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
    	console.log(json);
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

var QuotaionTemplateView =  Backbone.View.extend({
	initialize: function(options){
		this.quotationTemplateViewTemplate = Handlebars.compile($("#quotationTemplateViewTemplate").html());
		this.currentQuotaionTemplate = new App.Models.QuotationTemplate();
	},
	
	events: {
		"click #backBtn" : "back",
		"change .txtInput" : "onTxtChange",
		"click #saveBtn" : "onSaveBtn"
	},
	
	onTxtChange: function(e) {
		
		var field = $(e.currentTarget).attr('data-id');
		var value = $(e.currentTarget).val();
		
		this.currentQuotaionTemplate.set(field, value);
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
    	
    	return this;
    }
});
