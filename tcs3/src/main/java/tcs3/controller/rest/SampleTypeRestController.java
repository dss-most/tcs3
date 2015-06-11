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
import tcs3.model.lab.SampleType;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/REST/SampleType")
public class SampleTypeRestController {
	
	@Autowired
	EntityService entityService;
	
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.GET}) 
	public SampleType find(@PathVariable Long id) {
		
		return this.entityService.findSampleType(id);
	}
	
	@RequestMapping(value = "/", method = {RequestMethod.GET}) 
	public Iterable<SampleType> findAll() {
		
		return this.entityService.findAllSampleType();
	}
	
}
