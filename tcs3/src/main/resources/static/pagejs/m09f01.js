/**
 * 
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
        "new" : "newForm",
        "search" : "search",
        "Edit/:id" : "editForm",
        "*actions": "defaultRoute" // Backbone will try match the route above first
    },
    
    defaultRoute: function(action) {
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	this.formView.$el.empty();
    	this.searchView.render();
    	this.tableResultView.$el.empty();    	
    },
    
    search: function() {
    	
    	this.$breadcrubmEl.html(this.defaultBreadCrumb());
    	this.formView.$el.empty();
    	this.searchView.render();
    	this.tableResultView.render();
    },
    
    newForm: function() {
    	this.tableResultView.$el.empty();
    	this.searchView.$el.empty();
    	this.$breadcrubmEl.html(this.newBreadCrumb());
    	
    	this.formView.newForm();
    },
    
    editForm: function(id){
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	
    	var json = this.fromView.entity.toJSON();
    	this.$breadcrubmEl.html(this.editBreadCrumb(json));
    	
    	this.formView.editForm(id);
    }
    
    
});



var SearchView = Backbone.View.extend({
	

    initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.searchModel = new appModel();
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"change .txtInput" : "onChangeTxtInput",
    	"click #searchBtn" : "onClickSearch",
    	"click #newBtn" : "onClickNewBtn"
    		
    },
    onClickNewBtn: function(e) {
    	appRouter.navigate("new", {trigger: true});
    },
    onClickSearch: function(e) {
    	e.preventDefault();
    	appRouter.tableResultView.searchAndRenderPage(1, this.searchModel.toJSON());
    	return false;
    },
    
    onChangeTxtInput: function(e) {
    	var value = $(e.currentTarget).val();
    	var field = $(e.currentTarget).attr('data-field');
    	
    	this.searchModel.set(field, value);
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
		
		this.collection = new appCollection();
		this.searchEntity = new appModel();
		
		this.tableResultViewTemplate = Handlebars.compile($("#tableResultViewTemplate").html());
	},
	
	events: {
		"click .pageNav" : "onClickPageNav",		         
		"change #paageTxt" : "onChangePageTxt",
		"click .entityLnk" : "onClickEntityLnk"
	},
	
	onClickEntityLnk: function(e) {
		e.preventDefault();
		var id = $(e.currentTarget).parents('tr').attr('data-id');
		
		
		appRouter.navigate('edit/' +id, {trigger: true});
		
		return false;
	},
	onChangePageTxt: function(e) {
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
    searchAndRenderPage: function(pageNumber, searchModelJson) {
		__loaderInEl(this.$el);
		console.log("seachModelJson: " + searchModelJson);
    	this.collection.fetch({
    		data:  JSON.stringify(searchModelJson),
    		dataType: 'json',
    		type: 'POST',
    		contentType: 'application/json',
    		url: appUrl("Promotion/findByExample/page/"+pageNumber),
    		success: _.bind(function(collection, response, options) {
    			console.log(response);
    			this.render();
    		},this)
    	})
    },
    render: function() {
    
	    var json = {};
		json.page = this.collection.page;
		json.content = this.collection.toJSON();
		this.$el.html(this.tableResultViewTemplate(json));
		
		return this;
    }
});

var FormView =  Backbone.View.extend({
	initialize: function(options){
		this.entity = null;
		this.formViewTemplate = Handlebars.compile($("#formViewTemplate").html());
	},
	
	events: {
		"change .txtInput" : "onChangeTxtInput",
		"click #saveBtn" : "onClickSaveBtn",
		"click #backBtn" : "onClickBackBtn"
	},
	onChangeTxtInput: function(e) {
		var field = $(e.currentTarget).attr('data-field');
		var value = $(e.currentTarget).val();
		
		this.entity.set(field, value);
	},
	onClickSaveBtn: function(e) {
		
		this.entity.save(null, {
			success:_.bind(function(model, response, options) {
				if(response.status != 'SUCCESS') {
					alert(response.status + " :" + response.message);
				}
				this.entity.set('id', response.data);
				alert("บันทึกข้อมูลแล้ว");
		},this)});
	},
	
	onClickBackBtn : function() {
		appRouter.navigate("search", {trigger: true});
	},
	
	
	newForm: function() {
		this.entity = new appModel();
		this.render();
	},
	editForm: function(id) {
		this.entity = appModel.findOrCreate({id: id});
		this.entify.fetch({
			success: _.bind(function(model, response, options) {
				this.render();
			},this)
		});
	
		
	},
	render: function() {
		var json  = this.entity.toJSON();
		this.$el.html(this.formViewTemplate(json));
		
		return this;
	}

});
