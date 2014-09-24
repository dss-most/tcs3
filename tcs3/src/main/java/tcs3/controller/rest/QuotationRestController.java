package tcs3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/REST/Quotation")
public class QuotationRestController {
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "", method = {RequestMethod.POST}) 
	public ResponseJSend<Long> saveQuotation(@RequestBody JsonNode node) {
		
		return this.entityService.saveQuotation(node);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT}) 
	public ResponseJSend<Long> updateQuotation(
			@RequestBody JsonNode node, @PathVariable Long id) {
		
		return this.entityService.saveQuotation(node);
	}
}
