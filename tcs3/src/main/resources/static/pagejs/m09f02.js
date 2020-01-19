/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {

		
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newBreadCrumb = Handlebars.compile($("#newOfficerBreadCrumbTemplate").html());
		this.editBreadCrumb = Handlebars.compile($("#editOfficerBreadCrumbTemplate").html());
		this.$breadcrubmEl = $("#breadcrumb");
		
		this.allRoles = new App.Collections.DssRoles();
		this.allRoles.fetch({
			url: appUrl("User/allRoles")
		});
		
		this.allOrgs = new App.Collections.Organizations();
		this.jsonOrg = {};
		this.allOrgs.fetch({
			url: appUrl("Organization/allOrgs"),
			success: _.bind(function(collection, response, options) {
    			// we ordering Org here
				collection.forEach(function(e) {
					if(e.get('parent') != null) {
						var pOrg = App.Models.Organization.find(e.get('parent').get('id'));
						
						if( pOrg.get('children') == null ) {
							var c = new App.Collections.Organizations()
							pOrg.set('children', c);
						}
						
						pOrg.get('children').push(e);
					}
				});
				
				
    		},this)
		});
		
		
		
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
    	
    	var json =  App.Models.Officer.findOrCreate({id: id}).toJSON();
    	this.$breadcrubmEl.html(this.editBreadCrumb(json));
    	
    	this.formView.editForm(id);
    }
    
    
});



var SearchView = Backbone.View.extend({
	

    initialize: function(options){
    	this.searchViewTemplate = Handlebars.compile($("#searchViewTemplate").html());
    	this.searchModel = new App.Models.Officer();
    	this.queryTxt = "";
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"change .txtInput" : "onChangeTxtInput",
    	"click #searchOfficerBtn" : "onClickSearch",
    	"click #newOfficerBtn" : "onClickNewBtn"
    		
    },
    onClickNewBtn: function(e) {
    	appRouter.navigate("new", {trigger: true});
    },
    onClickSearch: function(e) {
    	e.preventDefault();
    	appRouter.tableResultView.searchOfficer(this.searchModel.toJSON(),1);
    	return false;
    },
    
    onChangeTxtInput: function(e) {
    	var value = $(e.currentTarget).val();
    	var field = $(e.currentTarget).attr('data-field');
    	this.queryTxt = value;
    	this.searchModel.set(field, value);
    },
    
    render: function() {
    	var json = {};
    	json.queryTxt = this.queryTxt;
    	this.$el.html(this.searchViewTemplate(json));
    	return this;
    	
    }
});


var TableResultView = Backbone.View.extend({
	initialize: function(options){
		
		this.officers = new App.Pages.Officers();
		this.queryTxt = "";
		
		this.tableResultViewTemplate = Handlebars.compile($("#officerTblTemplate").html());
	},
	
	events: {
		"click .pageNav" : "onClickPageNav",		         
		"change #paageTxt" : "onChangePageTxt",
		"click .entityLnk" : "onClickEntityLnk"
	},
	
	onClickEntityLnk: function(e) {
		e.preventDefault();
		var id = $(e.currentTarget).parents('tr').attr('data-id');
		
		
		appRouter.navigate('Edit/' +id, {trigger: true});
		
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
		console.log(this.queryTxt);
		
		var targetPage=$(e.currentTarget).attr('data-targetPage');
		this.searchAndRenderPage(targetPage);
	},
    searchAndRenderPage: function(pageNumber) {
		__loaderInEl(this.$el);
		this.officers.fetch({
    		data : {
				queryTxt : this.queryTxt
			},
			type: 'POST',
    		url: appUrl("User/findOfficer/page/"+pageNumber),
    		success: _.bind(function(collection, response, options) {
    			this.render();
    		},this)
    	})
    },
    searchOfficer : function(searchModelJson, pageNumber) {
    	if(searchModelJson == null ) {
			this.queryTxt = "";
		} else {
			this.queryTxt = searchModelJson.queryTxt;			
		}
    	this.currentPage = pageNumber;
    	this.searchAndRenderPage(pageNumber);

    },
    render: function() {
    
	    var json = {};
		json.page = this.officers.page;
		json.content = this.officers.toJSON();
	
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
		this.entity = new App.Models.Officer();
		this.render();
	},
	editForm: function(id) {
		this.entity = App.Models.Officer.findOrCreate({id: id});
		
		
		
		this.entity.fetch({
			url: appUrl("User/Officer/"+id),
			success: _.bind(function(model, response, options) {
				this.render();
			},this)
		});
	
		
	},
	render: function() {
		var json  = this.entity.toJSON();
		json.allRoles = appRouter.allRoles.toJSON();
		var user = this.entity.get('dssUser');
		var workAt = this.entity.get('workAt');
		var workAtId = -1;
		if(workAt != null) {
			workAtId = workAt.get('id');
		}
		if(user != null) {
		
			json.allRoles.forEach(function(e) {
				var role=App.Models.DssRole.find({id:e.id});
				if(user.get('dssRoles').contains(role)) {
					e.isChecked = true;
					
				}
			});
		}
		
		var dss = appRouter.allOrgs.find({id: 0});
		json.orgs = [];
		json.orgs.push(dss.toJSON());
		dss.get('children').forEach(function(e) {
		
			
			var j = e.toJSON();
			j.level="--";
			if(workAtId == e.id) {
				j.isSelected = true;
			}
			json.orgs.push(j);
			
		
			e.get('children').forEach(function(c) {
				var j = c.toJSON();
				j.level="----";
				if(workAtId == c.id) {
					j.isSelected = true;
				}
				
				
				json.orgs.push(j);
			});
			
		});
		
		console.log(json.orgs);
		
		this.$el.html(this.formViewTemplate(json));
		
		return this;
	}

});
