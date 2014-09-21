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
		relatedModel: 'App.Models.Organization',
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


App.Models.QuotationTemplate = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasMany,
		key: 'testMethods',
		relatedModel: 'App.Models.TestMethod',
		collectionType: 'App.Collections.TestMethods',
	}],
	urlRoot: appUrl('QuotationTemplate')
});



})();