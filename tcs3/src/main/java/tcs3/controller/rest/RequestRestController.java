package tcs3.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.auth.model.Activeuser;
import tcs3.auth.model.SecurityUser;
import tcs3.model.lab.Request;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/REST/Request")
public class RequestRestController {
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "", method = {RequestMethod.POST}) 
	public ResponseJSend<Request> saveRequest(@RequestBody JsonNode node, @Activeuser SecurityUser user) {
		
		return this.entityService.saveRequest(node, user);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT}) 
	public ResponseJSend<Request> updateRequest(
			@RequestBody JsonNode node, @PathVariable Long id, @Activeuser SecurityUser user) {
		
		return this.entityService.saveRequest(node, user);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.GET}) 
	public Request findRequest(@PathVariable Long id) {
		
		return this.entityService.findRequest(id);
	}
	
	@RequestMapping(value="/findByField/page/{pageNumber}", method=RequestMethod.POST) 
	public ResponseJSend<Page<Request>> findByField(
			@PathVariable Integer pageNumber,
			@RequestBody JsonNode node) throws JsonMappingException {
		
		return this.entityService.findRequestByField(node, pageNumber);
		
	}
}
