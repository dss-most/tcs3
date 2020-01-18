package tcs3.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.lab.TestProduct;
import tcs3.service.EntityService;

@RestController
@RequestMapping("/REST/TestProduct")
public class TestProductRestController {
	public static Logger logger = LoggerFactory.getLogger(TestProductRestController.class);
	
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "/", method = {RequestMethod.GET}) 
	public Page<TestProduct> findAllTestProduct(){
		
		return this.entityService.findAllTestProduct();
	}
	
	
	@RequestMapping(value = "/q", method = {RequestMethod.GET}) 
	public Page<TestProduct> findAllProduct(
			@RequestParam String search, @RequestParam Integer pageIndex,
			@RequestParam Integer pageSize, @RequestParam String categoryCode
			){
		
		if(categoryCode == null || categoryCode.equals("ALL")) {
			categoryCode = "";
		}
		
		return this.entityService.findTestProduct(search, pageIndex, pageSize, categoryCode);
	}
}
