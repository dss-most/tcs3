package tcs3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.lab.Quotation;
import tcs3.model.lab.QuotationTemplate;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/REST/Quotation")
public class QuotationRestController {
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "", method = {RequestMethod.POST}) 
	public ResponseJSend<Quotation> saveQuotation(@RequestBody JsonNode node) {
		
		return this.entityService.saveQuotation(node);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT}) 
	public ResponseJSend<Quotation> updateQuotation(
			@RequestBody JsonNode node, @PathVariable Long id) {
		
		return this.entityService.saveQuotation(node);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.GET}) 
	public Quotation findQuotation(@PathVariable Long id) {
		
		return this.entityService.findQuotation(id);
	}
	
	@RequestMapping(value="/findByField/page/{pageNumber}", method=RequestMethod.POST) 
	public ResponseJSend<Page<Quotation>> findByField(
			@PathVariable Integer pageNumber,
			@RequestParam(required=false) String nameQuery,
			@RequestParam(required=false) String codeQuery,
			@RequestParam(required=false) String companyQuery,
			@RequestParam(required=false) String quotationNo,
			@RequestParam(required=false) Long mainOrgId,
			@RequestParam(required=false) Long groupOrgId) {
		
		return this.entityService.findQuotationByField(nameQuery, codeQuery, companyQuery, quotationNo, mainOrgId, groupOrgId, pageNumber);
		
	}
}
