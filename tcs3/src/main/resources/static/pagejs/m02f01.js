/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.searchView = new SearchView({el: '#searchView'});
		this.tableResultView = new TableResultView({el: '#searchView'});
		this.quotaionTemplateView = new QuotaionTemplateView({el: '#searchView'});
		
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
    	console.log("defaultRoute: "+ action);
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
    },
    
    events: {
    	"click #newQuotationTemplateBtn" : "newQuotationTemplate"
    },
    
    newQuotationTemplate : function() {
    	appRouter.navigate("newQuotationTemplate", {trigger: true})
    },
    
    
    render: function() {
    	this.$el.html(this.searchViewTemplate());
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
	},
	
	events: {
		"click #backBtn" : "back"
	},
	
	back : function() {
		appRouter.navigate("", {trigger: true});
	},
	
    render: function() {
    	this.$el.html(this.quotationTemplateViewTemplate());
    	
    	return this;
    }
});
