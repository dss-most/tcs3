function appUrl(url) {
	return '/tcs3/REST/'+url;
}

(function(){

window.App = {
  Models: {},
  Collections: {},
  Pages: {},
  Views: {}
};

Backbone.PageCollection = Backbone.Collection.extend({
	parse: function(response) {
		if(response.status == 'SUCCESS') {
			this.page = {};
			this.page.first = response.data.first;
	
			this.page.last = response.data.last;
			this.page.lastPage = response.data.lastPage;
			this.page.firstPage = response.data.firstPage;
			this.page.totalElements = parseInt(response.data.totalElements);
			this.page.totalPages = parseInt(response.data.totalPages);
			this.page.size = parseInt(response.data.size);
			this.page.number = parseInt(response.data.number);
			this.page.pageNumber = parseInt(response.data.number) + 1;
			this.page.numberOfElements = parseInt(response.data.numberOfElements);
			this.page.nextPage = this.page.pageNumber+1;
			this.page.prevPage = this.page.pageNumber-1;
			return response.data.content;
		}
		return null;
	}
});

App.Models.Organization = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'parent',
		relatedModel: 'App.Models.Organization'
	}],
	urlRoot: appUrl('Organization')
});

App.Models.TestMethod = Backbone.RelationalModel.extend({
	urlRoot: appUrl('TestMethod')
});

App.Models.QuotationTemplate = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasMany,
		key: 'testMethodItems',
		relatedModel: 'App.Models.TestMethodQuotationTemplateItem',
		collectionType: 'App.Collections.TestMethodQuotationTemplateItems'
	},{
		type: Backbone.HasOne,
		key: 'groupOrg',
		relatedModel: 'App.Models.Organization'
	},{
		type: Backbone.HasOne,
		key: 'mainOrg',
		relatedModel: 'App.Models.Organization'
	}],
	urlRoot: appUrl('QuotationTemplate')
});
App.Models.Quotation = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasMany,
		key: 'testMethodItems',
		relatedModel: 'App.Models.TestMethodQuotationItem',
		collectionType: 'App.Collections.TestMethodQuotationItems'
	},{
		type: Backbone.HasOne,
		key: 'groupOrg',
		relatedModel: 'App.Models.Organization'
	},{
		type: Backbone.HasOne,
		key: 'mainOrg',
		relatedModel: 'App.Models.Organization'
	},{
		type: Backbone.HasOne,
		key: 'company',
		relatedModel: 'App.Models.Company'
	},{
		type: Backbone.HasOne,
		key: 'address',
		relatedModel: 'App.Models.Address'
	},{
		type: Backbone.HasOne,
		key: 'contact',
		relatedModel: 'App.Models.Customer'
	}],
	urlRoot: appUrl('Quotation')
});

App.Models.TestMethodQuotationTemplateItem = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'testMethod',
		relatedModel: 'App.Models.TestMethod'
	}]
});
App.Models.TestMethodQuotationItem = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'testMethod',
		relatedModel: 'App.Models.TestMethod'
	},{
		type: Backbone.HasOne,
		key: 'quotation',
		relatedModel: 'App.Models.Quotation'
	}]
});
App.Models.Address = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'province',
		relatedModel: 'App.Models.Province'
	},{
		type: Backbone.HasOne,
		key: 'district',
		relatedModel: 'App.Models.District'
	}]
});

App.Models.Customer = Backbone.RelationalModel.extend({
	relations: []
});
App.Models.Province = Backbone.RelationalModel.extend({
	relations: []
});
App.Models.District = Backbone.RelationalModel.extend({
	relations: []
});
App.Models.Company = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasMany,
		key: 'addresses',
		relatedModel: 'App.Models.Address',
		collectionType: 'App.Collections.Addresses'
			
	},{
		type: Backbone.HasOne,
		key: 'oldAddress',
		relatedModel: 'App.Models.Address'
	},{
		type: Backbone.HasMany,
		key: 'people',
		relatedModel: 'App.Models.Customer',
		collectionType: 'App.Collections.Customers'
	}],
	urlRoot: appUrl('Company')
});

App.Collections.Organizations = Backbone.Collection.extend({
	  model: App.Models.Organization
	});
App.Collections.TestMethods = Backbone.Collection.extend({
	model: App.Models.TestMethod
});
App.Pages.TestMethods = Backbone.PageCollection.extend({
	model: App.Models.TestMethod
});
App.Pages.QuotationTemplates = Backbone.PageCollection.extend({
	model: App.Models.QuotationTemplate
});
App.Pages.Quotation = Backbone.PageCollection.extend({
	model: App.Models.Quotation
});

App.Collections.TestMethodQuotationTemplateItems = Backbone.Collection.extend({
	model: App.Models.TestMethodQuotationTemplateItem
});
App.Collections.TestMethodQuotationItems = Backbone.Collection.extend({
	model: App.Models.TestMethodQuotationItem
});
App.Collections.Addresses = Backbone.Collection.extend({
	model: App.Models.Addresses
});
App.Collections.Customers = Backbone.Collection.extend({
	model: App.Models.Customer
});
App.Collections.Provinces = Backbone.Collection.extend({
	model: App.Models.Province
});
App.Collections.Districts = Backbone.Collection.extend({
	model: App.Models.District
});
App.Pages.Companies = Backbone.PageCollection.extend({
	model: App.Models.Company
});





})();