package tcs3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.auth.model.Activeuser;
import tcs3.auth.model.DssUser;
import tcs3.auth.model.SecurityUser;
import tcs3.model.lab.Quotation;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/REST/Quotation")
public class QuotationRestController {
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "", method = {RequestMethod.POST}) 
	public ResponseJSend<Quotation> saveQuotation(@RequestBody JsonNode node, @Activeuser SecurityUser user) {
		
		return this.entityService.saveQuotation(node, user);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT}) 
	public ResponseJSend<Quotation> updateQuotation(
			@RequestBody JsonNode node, @PathVariable Long id, @Activeuser SecurityUser user) {
		
		return this.entityService.saveQuotation(node, user);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.GET}) 
	public Quotation findQuotation(@PathVariable Long id) {
		
		return this.entityService.findQuotation(id);
	}
	
	@RequestMapping(value="/findByField/page/{pageNumber}", method=RequestMethod.POST) 
	public ResponseJSend<Page<Quotation>> findByField(
			@PathVariable Integer pageNumber,
			@RequestBody JsonNode node) throws JsonMappingException {
		
		return this.entityService.findQuotationByField(node, pageNumber);
		
	}
}
