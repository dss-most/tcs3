package tcs3.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.customer.Company;
import tcs3.model.global.District;
import tcs3.model.global.Province;
import tcs3.service.EntityService;

@RestController
@RequestMapping("/REST/Global")
public class GlobalRestController {
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "/provinces", method = RequestMethod.GET)
	public List<Province> findAllProvince() {
		return entityService.findProvinces();
	}
	
	@RequestMapping(value = "/province/{id}/districts", method = RequestMethod.GET)
	public List<District> findAllDistrictOfProvince(@PathVariable Long id) {
		return entityService.findDistrictsOfProvince(id);
	}
}
