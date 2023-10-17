package tcs3.controller.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import tcs3.model.hrx.Organization;
import tcs3.service.EntityService;

@RestController
@RequestMapping("/REST/Organization")
public class OrganizationRestController {

	public static Logger logger = LoggerFactory.getLogger(OrganizationRestController.class);

	@Autowired
	EntityService entityService;

	@Autowired
	ObjectMapper mapper;
	
	@GetMapping("/{id}")
	public Organization findById(@PathVariable Long id) {
		Organization org = entityService.findOrgannizationById(id);
		return org;
		
	}
	
	@GetMapping("/{id}/children")
	public List<Organization> findChildrenOfId(@PathVariable Long id) {
		List<Organization> orgList = entityService.findOrgannizationChildrenOfId(id);
		return orgList;
		
	}
	
	@GetMapping("/")
	public List<Organization> findTopOrg() {
		List<Organization> orgList = entityService.findTopOrgannization();

		// try {
		// 	logger.debug(">>>>>> "  + mapper.writeValueAsString(orgList));
		// } catch (JsonProcessingException e) {
		// 	// TODO Auto-generated catch block
		// 	e.printStackTrace();
		// }
		return orgList;
	}
	
	@GetMapping("/DSS")
	public Organization findDSSWithChildren() {
		Organization dss = entityService.findDsswithAllOrganization();
		return dss;
	}
	
	@GetMapping("/allOrgs")
	public Iterable<Organization> findallOrgs() {
		
		Iterable<Organization> orgList = entityService.findAllOrganization();
		return orgList;
	}
	
	
}
