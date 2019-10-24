/**
 * 
 */

var AppRouter = Backbone.Router.extend({
	/**
	 * @memberOf AppRouter
	 */
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.$breadcrubmEl = $("#breadcrumb");
		
		
		// now we're ready for initialize the view
		this.searchView = new SearchView({el: '#searchView'});
		this.tableResultView = new TableResultView({el: '#tableResultView'});
		
		
	},
    routes: {
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	this.tableResultView.$el.empty();
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	
    	
    	this.searchView.render();
    	
    }
    
    
});

var SearchView = Backbone.View.extend({
	/**
	 * @memberOf SearchView
	 */
	initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.orgSelectionTemplate = Handlebars.compile($("#orgSelectionTemplate").html());
    	
    	
    	// Global variable better be there
    	this.mainOrgCollection = mainOrgs;
    	this.currentMainOrg = userMainOrg;
    	
    	this.groupOrgCollection = groupOrgs.clone();
    	
    	this.quotationTemplates = new App.Pages.QuotationTemplates();
    	
    	this.searchModel = new App.Models.QuotationTemplate();
    	
    	if(this.currentMainOrg.get('id') == 3) {
    		this.searchModel.set("mainOrg", null);
        	
    	} else {
    		this.searchModel.set("mainOrg", this.currentMainOrg);
        		
    	}
    	
    	this.currentGroupOrg=null;
    	this.nameQuery=null;
    	this.codeQuery=null;
    },
    
    events: {
    	"change .formTxt" : "onChangeFormTxt",
    	"change .formSlt" : "onChangeFormSlt",
    	
    	"click #searchRequestBtn" : "onClickSearchRequestBtn"
    	
    },
    onChangeFormSlt: function(e) {
    	var id=$(e.currentTarget).val();
    	var field=$(e.currentTarget).attr('data-field'); 
    	var model;
    	
    	if(field=="mainOrg") {
    		this.currentMainOrg = App.Models.Organization.findOrCreate({id: id});
    		if( id == 0 ) {
    			this.renderOrgSlt();
    			model = null;
    		} else {
        		model = this.currentMainOrg;
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
    
    onChangeFormTxt: function(e) {
    	var value = $(e.currentTarget).val();
    	var field = $(e.currentTarget).attr("data-field");
    	
    	if(field == "name") {
    		this.nameQuery=value;
    	} else if (field == "code") {
    		this.codeQuery=value;
    	}
    	
    	this.searchModel.set(field, value);
    	
    },
    

    onClickSearchRequestBtn: function(e) {
    	e.preventDefault();
    	appRouter.tableResultView.serachRequestOverdue(this.searchModel, 1);
    	return false;
    },    
 
    
    renderOrgSlt: function() {
    	var json = {};
    	
    	if(this.currentMainOrg.get('id') == 0 || this.currentMainOrg.get('id') == 3) {
    		 json.mainGroup = null;
    	} else {
    		json.mainGroup = this.groupOrgCollection.toJSON();
    	}
    	
    	this.$el.find('#orgSlt').html(this.orgSelectionTemplate(json));
    	return this;
    },
    
    render: function() {
    	var json = {};
    	json.mainOrg = new Array();
    	json.mainOrg.push({id:0,abbr: 'ทุกหน่วยงาน'});
    	$.merge(json.mainOrg, this.mainOrgCollection.toJSON());
    	if(this.currentMainOrg == null) {
    		this.currentMainOrg = App.Models.Organization.findOrCreate({id: 0});
        		
    	}
    	
    	
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
		
		this.searchModel = new App.Models.QuotationTemplate();
		this.requests = new App.Pages.Requests();
		
		
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
		
		appRouter.navigate('QuotationTemplate/' +templateId, {trigger: true});
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
		
		for(var i=0; i< json.content.length; i++){
			var fromNow = moment(json.content[i].estimatedReportDate, "DD/MM/YYYY").fromNow();
    		json.content[i].fromNowString = fromNow;
    	}
		
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
    		url: appUrl("Request/findOverdue/page/"+pageNumber),
    		success: _.bind(function(collection, response, options) {
    			this.render();
    		},this)
    	})
    },
	
    serachRequestOverdue: function(searchModel, pageNumber) {
    	this.currentPage = pageNumber;
    	
    	this.searchModel = searchModel;
    	
    	this.searchAndRenderPage(pageNumber);

    },
});

