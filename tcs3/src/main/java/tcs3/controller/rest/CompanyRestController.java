package tcs3.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.customer.Address;
import tcs3.model.customer.Company;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/Company")
public class CompanyRestController {

	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Company findById(@PathVariable Long id) {
		Company company = entityService.findCompanyById(id);
		return company;
		
	}
	
	@RequestMapping(value = "/{id}/Addresses", method = RequestMethod.GET)
	public List<Address> findAddressOfId(@PathVariable Long id) {
		List<Address> addressList = entityService.findAddressOfId(id);
		return addressList;
		
	}
	
	@RequestMapping(value= "/findByName/page/{pageNumber}", method= RequestMethod.POST)
	public ResponseJSend<Page<Company>> searchCompanyByName(
			@PathVariable Integer pageNumber,
			@RequestParam(required=false) String nameQuery) {
		return entityService.searchCompanyByName(nameQuery, pageNumber);
		
	}
}
