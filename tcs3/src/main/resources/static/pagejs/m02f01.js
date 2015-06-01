/**
 * 
 */
var AppRouter = Backbone.Router.extend({
	initialize : function(options) {
		this.defaultBreadCrumb = Handlebars.compile($("#defaultBreadCrumbTemplate").html());
		this.newQuotationTemplateBreadCrumb = Handlebars.compile($("#newQuotationTemplateBreadCrumbTemplate").html());
		this.editQuotationTemplateBreadCrumb =Handlebars.compile($("#editQuotationTemplateBreadCrumbTemplate").html());
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
    	this.searchView.$el.empty();
    	this.tableResultView.$el.empty();
    	var json={};
    	json.templateId=id;
    	this.$breadcrubmEl.html(this.editQuotationTemplateBreadCrumb(json));
    	
    	this.quotaionTemplateView.editQuotationTemplate(id);
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
    	
    	this.quotationTemplates = new App.Pages.QuotationTemplates();
    	
    	
    	this.currentGroupOrg=null;
    	this.nameQuery=null;
    	this.codeQuery=null;
    },
    
 // Template
	//orgSelectionTemplate : Handlebars.compile($("#orgSelectionTemplate").html()),
	//searchViewTemplate : Handlebars.compile($("#searchViewTemplate").html()),
 
    
    events: {
    	"click #newQuotationTemplateBtn" : "newQuotationTemplate",
    	"change #mainOrgSlt" : "onChangeMainOrg",
    	"change #groupOrgSlt" : "onChangeGroupOrg",
    	"change .txtInput" : "onChangeTxtInput",
    	"click #searchQuotationTemplateBtn" : "onClickSearchQuotationTemplateBtn"
    	
    },
    
    onChangeTxtInput: function(e) {
    	var value = $(e.currentTarget).val();
    	var field = $(e.currentTarget).attr("data-field");
    	
    	if(field == "name") {
    		this.nameQuery=value;
    	} else if (filed == "code") {
    		this.codeQuery=value;
    	}
    	
    },
    

    onClickSearchQuotationTemplateBtn: function(e) {
    	e.preventDefault();
    	appRouter.tableResultView.serachQuotationTemplate(this.nameQuery, this.codeQuery, this.currentMainOrg, this.currentGroupOrg, 1);
    	return false;
    },
  
    newQuotationTemplate : function() {
    	appRouter.navigate("newQuotationTemplate", {trigger: true})
    },
    
    onChangeGroupOrg: function(e) {
    	var newOrgId=e.currentTarget.value;
    	if(newOrgId == 0) {
    		this.currentGroupOrg = null;
    	} else {
    		this.currentGroupOrg = App.Models.Organization.findOrCreate({id: newOrgId});
    	}
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
		this.tableResultViewTemplate = Handlebars.compile($('#tableResultViewTemplate').html());
		
		this.templates = new App.Pages.QuotationTemplates();
		
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
		if(targetPage > this.templates.page.totalPages) {
			alert('หน้าของข้อมูลที่ระบุมีมากกว่าจำนวนหน้าทั้งหมด กรุณาระบุใหม่');
			$(e.currentTarget).val(oldValue);
			return;
		}
		this.searchAndRenderPage(targetPage);
	},
    render: function() {
    	var json = {};
    	json.page = this.templates.page;
		json.content = this.templates.toJSON();
    	this.$el.html(this.tableResultViewTemplate(json));
    	
    	return this;
    },
    searchAndRenderPage: function(pageNumber) {
    	this.templates.fetch({
    		data: {
    			nameQuery : this.nameQuery,
    			codeQuery : this.codeQuery,
    			mainOrgId : this.currentMainOrg.get('id'),
    			groupOrgId : this.currentGroupOrg == null ? 0 : this.currentGroupOrg.get('id')
    		},
    		type: 'POST',
    		url: appUrl("QuotationTemplate/findByField/page/"+pageNumber),
    		success: _.bind(function(collection, response, options) {
    			this.render();
    		},this)
    	})
    },
	
    serachQuotationTemplate: function(nameQuery, codeQuery, currentMainOrg, currentGroupOrg, pageNumber) {
    	this.nameQuery = nameQuery;
    	this.codeQuery = codeQuery;
    	this.currentMainOrg = currentMainOrg;
    	this.currentGroupOrg = currentGroupOrg;
    	this.currentPage = pageNumber;
    	
    	this.searchAndRenderPage(pageNumber);

    },
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
		 this.parentView = null;
		 this.testMethods = new App.Pages.TestMethods();
		 this.selected = new App.Collections.TestMethods();
	 },
	 setParentView : function(view) {
		this.parentView=view; 
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
		 if(this.mode == "newTestMethodItem" || this.mode == "editTestMethodItem") {
//			 var testMethodId = this.$el.find('.testMethodRdo:checked').val();
//			 
//			 var testMethod = App.Models.TestMethod.find({id: testMethodId});
//			 
//			 if(testMethod == null) {
//				 alert('กรุณาเลือกรายการทดสอบ');
//				 return;
//			 }
//			 
//			 var findItem =  this.currentQuotationTemplate.get('testMethodItems')
//			 		.find(function(item){
//			 			if(item.get('testMethod') != null) { 
//			 				return item.get('testMethod').get('id') == testMethod.get('id');
//			 			}
//			 			return false;
//			 		});
//			 
//			 if(findItem != null) {
//				 alert('รายการทดสอบนี้มีอยู่ในต้นแบบแล้ว กรุณาเลือกรายการใหม่');
//				 return;
//			 }
//			 
//			 // now copy value to current
//			 this.currentItem.set('testMethod', testMethod);
//			 this.currentItem.set('fee', testMethod.get('fee'));
//			 
//			 if(this.currentItem.get('quantity') == null) {
//			 	this.currentItem.set('quantity', 1);
//			 }
			 this.selected.forEach(function(testMethod, index, list) {
				 var item = new App.Models.TestMethodQuotationItem();
				 
				 item.set('testMethod', testMethod);
				 item.set('fee', testMethod.get('fee'));
				 if(item.get('quantity') == null) {
					 	item.set('quantity', 1);
				 } 
				 
				 
				 var findItem =  this.currentQuotationTemplate.get('testMethodItems')
				 		.find(function(item){
				 			if(item.get('testMethod') != null) { 
				 				return item.get('testMethod').get('id') == testMethod.get('id');
				 			}
				 			return false;
				 		});
				 if(findItem == null) {
				 	this.currentQuotationTemplate.get('testMethodItems').add(item);
				 }
			 }, this);
			 
			 
			 
			 
		 } else if(this.mode == "newTestMethodGroup" || this.mode == "editTestMethodGroup"){
			 var name = this.$el.find('#testMethodItemName').val();
			 if(name == null || name.length == 0) {
				 alert('กรุณาระบุชื่อกลุ่มรายการ');
				 return;
			 }
			 this.currentItem.set('name', name);
			 this.currentQuotationTemplate.get('testMethodItems').add(this.currentItem);
		 }
		 
		 // do save

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
		 this.selected.reset();
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
	/**
	 * @memberOf QuotaionTemplateView
	 */
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
		"change #groupOrgSlt" : "onMainGroupChange",
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
		    return false;
		} 
		
		return false;
		
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
	editQuotationTemplate: function(id) {
		this.currentQuotationTemplate = App.Models.QuotationTemplate.findOrCreate({id: id});
		this.testMethodItemModal.setQuotationTemplate(this.currentQuotationTemplate);
		this.render();
		
	},
	fixHelperModified : function(e, tr) {
		var $originals = tr.children();
		var $helper = tr.clone(); 
		$helper.children().each(function(index)  {
			$(this).width($originals.eq(index).width()+10);     
		});
		return $helper;     
	}, 
	reorderQutationItem : function() {
		var count = 1;
		var oldItems = this.currentQuotationTemplate.get('testMethodItems');
		var newItems = new App.Collections.TestMethodQuotationItems();
		 $("#quotationTemplateItemTbl tbody tr").each(function(index, tr) {
			 
			 
			 var itemIndex = $(tr).attr('data-index');
			 var item = oldItems.at(itemIndex);
			 if(item.get('testMethod') != null) {
				 $(tr).find('.index').html(count);
				 count++;
			 }
			 $(tr).attr('data-index', index);
			 newItems.push(item);
		 });
		 
		 
		 
		 this.currentQuotationTemplate.set('testMethodItems', newItems);
	},
	renderQuotationItemTbl: function() {
		
		var json = this.currentQuotationTemplate.get('testMethodItems').toJSON();
		if(json != null) {
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
		
		 // now make 'em sortable
		 $("#quotationTemplateItemTbl tbody").sortable({
			 placeholder: "highlight",
			 handle : ".handle",
			 helper: this.fixHelperModified,
			 stop: _.bind(function( event, ui ) {
				this.reorderQutationItem();
			 },this)
		 }).disableSelection();
		
		 
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
    	
    	var groupOrg =this.currentQuotationTemplate.get('groupOrg');
    	if(groupOrg != null && groupOrg.get('id') != null) {
    		this.$el.find('#groupOrgSlt').val(groupOrg.get('id'));
    	}
    	
    	return this;
    }
});
