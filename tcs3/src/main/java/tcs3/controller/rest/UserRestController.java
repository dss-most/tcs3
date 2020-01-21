package tcs3.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import tcs3.auth.model.Activeuser;
import tcs3.auth.model.DssRole;
import tcs3.auth.model.SecurityUser;
import tcs3.model.hrx.Officer;
import tcs3.model.lab.Request;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/User")
public class UserRestController {
	
	public static Logger logger = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "/allRoles", method = {RequestMethod.GET}) 
	public Iterable<DssRole> findAllUser(){
		
		return this.entityService.findAllDssRoles();
	}
	
	@RequestMapping(value = "/findOfficer/page/{pageNumber}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<Officer>> findOfficer(
			@PathVariable Integer pageNumber, 
			@RequestParam(required = false) String queryTxt,
			@RequestParam(required = false) Long workAtId
			) throws JsonMappingException {	
		
		
		logger.debug("workAtId: " +  workAtId + ", query: " + queryTxt );
		
		if(workAtId == null) {
			workAtId = 0L;
		}
		
		return this.entityService.findOfficer(queryTxt, workAtId, pageNumber-1);
	}
	

	@RequestMapping(value = "/Officer/{id}", method = {RequestMethod.GET}) 
	public Officer findOfficer(
			@PathVariable Long id)	{
		
		
		
		
		return this.entityService.findOfficer(id);
	}
	
	@RequestMapping(value = "/Officer", method = {RequestMethod.POST}) 
	public ResponseJSend<Officer> saveOfficer(@RequestBody JsonNode node, @Activeuser SecurityUser user) {
		
		return this.entityService.saveOfficer(node, user);
	}
	
	@RequestMapping(value = "/Officer/{id}", method = {RequestMethod.PUT}) 
	public ResponseJSend<Officer> updateOfficer(
			@RequestBody JsonNode node, @PathVariable Long id, @Activeuser SecurityUser user) {
		
		return this.entityService.saveOfficer(node, user);
	}
	
}
