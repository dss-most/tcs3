package tcs3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.lab.SampleType;
import tcs3.service.EntityService;

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
