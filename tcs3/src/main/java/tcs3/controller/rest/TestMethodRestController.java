package tcs3.controller.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tcs3.model.lab.TestMethod;
import tcs3.service.EntityService;
import tcs3.webUI.ResponseJSend;

@RestController
@RequestMapping("/REST/TestMethod")
public class TestMethodRestController {
	public static Logger logger = LoggerFactory.getLogger(TestMethodRestController.class);
	
	@Autowired
	EntityService entityService;
	
	@RequestMapping(value = "/findByNameOrCode/page/{pageNumber}", method = {RequestMethod.POST}) 
	public ResponseJSend<Page<TestMethod>> saveQuotationTemplate(
			@RequestParam String query, @PathVariable Integer pageNumber){
		
		return this.entityService.findTestMethodByNameOrCode(query, pageNumber);
	}
}
