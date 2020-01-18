package tcs3.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.lab.TestProductCategory;
import tcs3.service.EntityService;

@RestController
@RequestMapping("/REST/TestProductCategory")
public class TestProductCategoryRestController {
	public static Logger logger = LoggerFactory.getLogger(TestProductCategoryRestController.class);
	
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "/", method = {RequestMethod.GET}) 
	public Page<TestProductCategory> findAllTestProductCategory(){
		
		return this.entityService.findAllTestProductCategory();
	}
}
