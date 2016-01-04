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
import tcs3.model.lab.Promotion;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/REST/Promotion")
public class PromotionRestController {
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "", method = {RequestMethod.POST}) 
	public ResponseJSend<Long> savePromotion(@RequestBody JsonNode node, @Activeuser SecurityUser user) {
		
		return this.entityService.savePromotion(node, user);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.PUT}) 
	public ResponseJSend<Long> updatePromotion(
			@RequestBody JsonNode node, @PathVariable Long id, @Activeuser SecurityUser user) {
		
		return this.entityService.savePromotion(node, user);
	}
	
	@RequestMapping(value = "/{id}", method = {RequestMethod.GET}) 
	public Promotion findPromotion(@PathVariable Long id) {
		
		return this.entityService.findPromotion(id);
	}
	
	@RequestMapping(value = "/current", method = {RequestMethod.GET}) 
	public Iterable<Promotion> findPromotionCurrent() {
		
		return this.entityService.findPromotionCurrent();
	}
	
	
	@RequestMapping(value="/findByExample/page/{pageNumber}", method=RequestMethod.POST) 
	public ResponseJSend<Page<Promotion>> findByField(
			@PathVariable Integer pageNumber,
			@RequestBody JsonNode node) throws JsonMappingException {
		
		return this.entityService.findPromotionByField(node, pageNumber);
		
	}
}
