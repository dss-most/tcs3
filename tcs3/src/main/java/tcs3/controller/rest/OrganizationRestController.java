package tcs3.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.hrx.Organization;
import tcs3.service.EntityService;

@RestController
@RequestMapping("/REST/Organization")
public class OrganizationRestController {

	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Organization findById(@PathVariable Long id) {
		Organization org = entityService.findOrgannizationById(id);
		return org;
		
	}
	
	@RequestMapping(value = "/{id}/children", method = RequestMethod.GET)
	public List<Organization> findChildrenOfId(@PathVariable Long id) {
		List<Organization> orgList = entityService.findOrgannizationChildrenOfId(id);
		return orgList;
		
	}
	
	@RequestMapping(value= "/", method= RequestMethod.GET)
	public List<Organization> findTopOrg() {
		List<Organization> orgList = entityService.findTopOrgannization();
		return orgList;
	}
	
	
}
