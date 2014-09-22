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
    	
    	this.quotaionTemplateView.newQuotationTemplate();
    	
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
		 this.testMethodGroupModalBodyTemplate = Handlebars.compile($('#testMethodGroupModalBodyTemplate').html());
		 this.testMethodItemModalBodyTemplate = Handlebars.compile($('#testMethodItemModalBodyTemplate').html());
		 this.testMethodSearchTblTemplate = Handlebars.compile($('#testMethodSearchTblTemplate').html());
		 this.mode="";
		 this.currentItem = null;
		 this.parentView = null;
		 this.testMethods = new App.Pages.TestMethods();
	 },
	 setParentView : function(view) {
		this.parentView=view; 
	 },
	 events: {
		 "click #testMethodModalCloseBtn" : "onClickCloseBtn",
		 "click #testMethodModalSaveBtn" : "onClickSaveBtn",
		 "searched.fu.search #testMethodSrh" : "onSearchTestMethod",
		 "click .testMethodPageNav" : "onClickPageNav",
		 "change #testMethodPageTxt" : "onChangeTestMethodPageTxt"
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
	 onClickSaveBtn: function(e) {
		 if(this.mode == "newTestMethodItem" || this.mode == "editTestMethodItem") {
			 var testMethodId = this.$el.find('.testMethodRdo:checked').val();
			 
			 var testMethod = App.Models.TestMethod.find({id: testMethodId});
			 
			 if(testMethod == null) {
				 alert('กรุณาเลือกรายการทดสอบ');
				 return;
			 }
			 
			 var findItem =  this.currentQuotationTemplate.get('testMethodItems')
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
			 
		 } else if(this.mode == "newTestMethodGroup" || this.mode == "editTestMethodGroup"){
			 var name = this.$el.find('#testMethodItemName').val();
			 if(name == null || name.length == 0) {
				 alert('กรุณาระบุชื่อกลุ่มรายการ');
				 return;
			 }
			 this.currentItem.set('name', name);
		 }
		 
		 // do save
		 this.currentQuotationTemplate.get('testMethodItems').add(this.currentItem);
		 this.parentView.renderQuotationItemTbl();
		 this.$el.modal('hide');
	 },
	 setQuotationTemplate: function(quotationTemplate) {
		this.currentQuotationTemplate =  quotationTemplate;
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
		 if(this.mode == "newTestMethodItem") {
			 this.currentItem = 
				 new App.Models.TestMethodQuotationTemplateItem();
			 this.$el.find('.modal-header span').html("เพิ่มรายการทดสอบ");
			 this.$el.find('.modal-body').html(this.testMethodItemModalBodyTemplate());
			 this.$el.find('#testMethodSrh').search();
			 
		 } else if(this.mode == "newTestMethodGroup"){
			 this.currentItem = 
				 new App.Models.TestMethodQuotationTemplateItem();
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

var QuotaionTemplateView =  Backbone.View.extend({
	initialize: function(options){
		this.quotationTemplateViewTemplate = Handlebars.compile($("#quotationTemplateViewTemplate").html());
		this.orgSelectionTemplate = Handlebars.compile($("#orgSelectionTemplate").html());
		this.quotationTemplateItemTblTemplate= Handlebars.compile($("#quotationTemplateItemTblTemplate").html());
		
		this.currentQuotationTemplate = null;
		
		this.testMethodItemModal = new TestMethodItemModal({el : '#testMethodModal'});
		this.testMethodItemModal.setParentView(this);
	},
	
	events: {
		"click #backBtn" : "back",
		"change .txtInput" : "onTxtChange",
		"change #mainGroupSlt" : "onMainGroupChange",
		"click #saveBtn" : "onSaveBtn",
		
		"click .itemLnk" : "onClickItem",
		"changed.fu.spinbox .itemQuantitySbx" : "onChangeItemQuantitySbx",
		"click .removeItemBtn" : "onClickRemoveItem",
		
		"click #newTestMethodGroupBtn" : "onClickNewTestMethodGroupBtn",
		"click #newTestMethodItemBtn" : "onClickNewTestMethodItemBtn"
	},
	
	onChangeItemQuantitySbx: function(e,v) {
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var item = this.currentQuotationTemplate.get('testMethodItems').at(index);
		item.set('quantity', v);
		//this.renderQuotationItemTbl();
		$(e.currentTarget).parent().next().html(item.get('quantity') * item.get('testMethod').get('fee'));
		
		var sumTotal = 0;
		this.currentQuotationTemplate.get('testMethodItems').each(function(itemLoop) {
			if(itemLoop.get('quantity') != null && itemLoop.get('quantity') > 0) {
				sumTotal += itemLoop.get('quantity') * itemLoop.get('testMethod').get('fee');
			}
		});
		
		this.$el.find('#sumTotalItem').html("<b>" + sumTotal + "</b>");		
	},
	onClickRemoveItem: function(e) {
		
		var index=$(e.currentTarget).parents('tr').attr('data-index');
		var item = this.currentQuotationTemplate.get('testMethodItems').at(index);
		
		var r = confirm('คุณต้องการลบรายการทดสอบ ' + item.get('testMethod').get('code') + ' ?');
		if (r == true) {
			this.currentQuotationTemplate.get('testMethodItems').remove(item);
			this.renderQuotationItemTbl();
		} else {
		    return;
		} 
		
		
		
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
    	var item = this.currentQuotationTemplate.get('testMethodItems').at(index);
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
		
		this.currentQuotationTemplate.set(field, value);
	},
	
	onMainGroupChange: function(e) {
		var mainGroupId = $(e.currentTarget).val();
		if(mainGroupId == 0) {
			this.currentQuotationTemplate.set("groupOrg", null);
		} else {
			var groupOrg = App.Models.Organization.findOrCreate({id: mainGroupId});
			this.currentQuotationTemplate.set("groupOrg", groupOrg);
		}
	},
	
	onSaveBtn : function(e) {
		if(this.currentQuotationTemplate.get('name') == null) {
			alert('กรุณาระบุชื่อผลิตภํณฑ์');
			return;
		}
		
		if(this.currentQuotationTemplate.get('code') == null) {
			alert('กรุณาระบุรหัสต้นแบบใบเสนอราคา');
			return;
		}
		
		if(this.currentQuotationTemplate.get('groupOrg') == null) {
			alert('กรุณาระบุหน่วยงานรับผิดชอบ');
			return;
		}
		
		if(this.currentQuotationTemplate.get('testMethodItems').length == 0) {
			alert('กรุณาระบุรายการทดสอบ');
			return;
		}
		
		this.currentQuotationTemplate.save(null, {
			success:_.bind(function(model, response, options) {
				if(response.status != 'SUCCESS') {
					alert(response.status + " :" + response.message);
				}
				this.currentQuotationTemplate.set('id', response.data);
				alert("บันทึกข้อมูลแล้ว");
		},this)});
	},
	
	back : function() {
		appRouter.navigate("", {trigger: true});
	},
	newQuotationTemplate : function() {
		this.currentQuotationTemplate = new App.Models.QuotationTemplate();
		this.testMethodItemModal.setQuotationTemplate(this.currentQuotationTemplate);
		this.render();
	},
	editQuotation: function(id) {
		this.currentQuotationTemplate = App.Models.QuotationTemplate.findOrCreate({id: id});
		this.testMethodItemModal.setQuotationTemplate(this.currentQuotationTemplate);
		this.render();
	},
	
	renderQuotationItemTbl: function() {
		var json = this.currentQuotationTemplate.get('testMethodItems').toJSON();
		if(json != null && json.length > 0) {
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
	    	this.$el.find("#quotationTemplateItemTbl")
	    		.html(this.quotationTemplateItemTblTemplate(json));
	    	this.$el.find('.itemQuantitySbx').spinbox();
		}
    	return this;
	},
	
    render: function() {
    	var json = {};
    	if(this.currentQuotationTemplate != null) {
    		json = this.currentQuotationTemplate.toJSON();
    	}
    	
    	this.$el.html(this.quotationTemplateViewTemplate(json));
    	this.renderQuotationItemTbl();
    	
    	json = {};
    	json.mainGroup = groupOrgs.toJSON();
    	
    	this.$el.find("#quotationMainOrgSlt")
    		.html("<label for='quotationMainOrg'>หน่วยงานที่รับผิดชอบหลัก</label>")
    		.append(this.orgSelectionTemplate(json));
    	
    	
    	return this;
    }
});
