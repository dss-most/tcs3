function appUrl(url) {
	return '/tcs3/REST/'+url;
}

(function(){

window.App = {
  Models: {},
  Collections: {},
  Views: {}
};


App.Models.Organization = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasOne,
		key: 'parent',
		relatedModel: 'App.Models.Organization'
	}],
	urlRoot: appUrl('Organization')
});

App.Collections.Organizations = Backbone.Collection.extend({
  model: App.Models.Organization
});

App.Models.TestMethod = Backbone.RelationalModel.extend({
	relations: [],
	urlRoot: appUrl('TestMethod')
});
App.Collections.TestMethods = Backbone.Collection.extend({
	model: App.Models.TestMethod
});
App.Collections.TestMethodQuotationTemplateItems = Backbone.Collection.extend({
	model: App.Models.TestMethodQuotationTemplateItem
});

App.Models.TestMethodQuotationTemplateItem = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasMany,
		key: 'testMethods',
		relatedModel: 'App.Models.TestMethod',
		collectionType: 'App.Collections.TestMethods'
	}]
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
		relatedModel: 'App.Models.Organization',
	},{
		type: Backbone.HasOne,
		key: 'mainOrg',
		relatedModel: 'App.Models.Organization',
	}],
	urlRoot: appUrl('QuotationTemplate')
});



})();