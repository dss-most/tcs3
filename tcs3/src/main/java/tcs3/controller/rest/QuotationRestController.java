package tcs3.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import tcs3.auth.model.Activeuser;
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
	
	public static Logger logger = LoggerFactory.getLogger(QuotationRestController.class);
	
	@PostMapping("") 
	public ResponseJSend<Quotation> saveQuotation(@RequestBody JsonNode node, @Activeuser SecurityUser user) {
		
		return this.entityService.saveQuotation(node, user);
	}
	
	@PutMapping("/{id}") 
	public ResponseJSend<Quotation> updateQuotation(
			@RequestBody JsonNode node, @PathVariable Long id, @Activeuser SecurityUser user) {
		
		return this.entityService.saveQuotation(node, user);
	}
	
	@GetMapping("/{id}") 
	public Quotation findQuotation(@PathVariable Long id) {
		
		return this.entityService.findQuotation(id);
	}
	
	@PostMapping("/findByQuotationNo") 
	public Quotation findQuotation(@RequestParam String quotationNo) {
		
		logger.debug("quoationNo: " + quotationNo);
		
		return this.entityService.findQuotationByQuotationNo(quotationNo);
	}
	
	@PostMapping("/findByField/page/{pageNumber}") 
	public ResponseJSend<Page<Quotation>> findByField(
			@PathVariable Integer pageNumber,
			@RequestBody JsonNode node) throws JsonMappingException {
		
		return this.entityService.findQuotationByField(node, pageNumber);
		
	}
}
