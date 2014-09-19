package tcs3.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import tcs3.auth.model.Activeuser;
import tcs3.auth.model.SecurityUser;
import tcs3.model.hrx.Officer;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.TestMethod;
import tcs3.service.EntityService;

@Controller
public class HomeController {
	
	public static Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping("/")
	public String home(Model model, @Activeuser SecurityUser user) {
		return "home";
	}
	
	@RequestMapping("/page/m02f01")
	public String m02f01(Model model, @Activeuser SecurityUser user) {
		//Officer officer = entityService.findOfficerByUserName(user.getUsername());
		
		Officer officer = user.getDssUser().getOfficer();
		
		logger.debug("found Officer: " + officer.getFirstName() + "  " + officer.getLastName());
		logger.debug("       WorkAt: " + officer.getWorkAt().getName());
				
		//QuotationTemplate qt = new QuotationTemplate();
		//qt.setName("ทดสอบ");
		
		//List<TestMethod> methods = entityService.findTestMethodByNameThLike("Phenol");
		
		//qt.setTestMethods(methods);
		
		//entityService.saveQuotationTemplate(qt);
		
		return "page/m02f01";
	}

}
