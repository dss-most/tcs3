package tcs3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/REST/QuotationTemplate")
public class QuotationTemplateRestController {
	
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.PUT}) 
	public ResponseJSend<Long> saveQuotationTemplate(@RequestBody JsonNode node) {
		
		return this.entityService.saveQuotationTemplate(node);
	}
}
