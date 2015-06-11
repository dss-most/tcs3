package tcs3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.lab.QuotationTemplate;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/REST/QuotationTemplate")
public class QuotationTemplateRestController {
	
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "", method = {RequestMethod.POST}) 
	public ResponseJSend<Long> saveQuotationTemplate(@RequestBody JsonNode node) {
		
		return this.entityService.saveQuotationTemplate(node);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT}) 
	public ResponseJSend<Long> updateQuotationTemplate(
			@RequestBody JsonNode node, @PathVariable Long id) {
		
		return this.entityService.saveQuotationTemplate(node);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.GET}) 
	public QuotationTemplate findQuotationTemplate(@PathVariable Long id) {
		
		return this.entityService.findQuotationTemplate(id);
	}
	
	@RequestMapping(value="/findByField/page/{pageNumber}", method=RequestMethod.POST) 
	public ResponseJSend<Page<QuotationTemplate>> findByField(
			@PathVariable Integer pageNumber,
			@RequestBody JsonNode node) throws JsonMappingException {
		
		return this.entityService.findQuotationTemplateByField(node, pageNumber);
		
	}
	
}
