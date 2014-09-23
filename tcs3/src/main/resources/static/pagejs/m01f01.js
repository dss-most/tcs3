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
        "Company/:id" : "editCompany",
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	this.tableResultView.$el.empty();
    	this.companyCustomerView.$el.empty();
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	
    	this.searchView.render();
    	
    },
    
    newCompany: function() {
    	
    },
    
    editCompany: function(id){
    	
    }
    
    
});



var SearchView = Backbone.View.extend({
	

    initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.companyNameQuery = null;
    	this.customerNameQuery = null;
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"change .txtInput" : "onChangeTxtInput",
    	"click #searchCompanyBtn" : "onClickSearchCompany"
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



var CompanyCustomerView =  Backbone.View.extend({
	

});
