package tcs3.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import tcs3.auth.model.Activeuser;
import tcs3.auth.model.SecurityUser;
import tcs3.model.hrx.Officer;
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

	@RequestMapping("/page/m01f01")
	public String m01f01(Model model, @Activeuser SecurityUser user) {
		
		return "page/m01f01";
	}
	
	@RequestMapping("/page/m02f01")
	public String m02f01(Model model, @Activeuser SecurityUser user) {
		Officer officer = user.getDssUser().getOfficer();
	
		Long mainOrgId;
		if(officer.getWorkAt().getParent().getId() == 0L) {
			mainOrgId = officer.getWorkAt().getId();
		} else {
			mainOrgId = officer.getWorkAt().getParent().getId();
		}
		
		model.addAttribute("mainOrgId", mainOrgId);
		
		return "page/m02f01";
	}
	
	@RequestMapping("/page/m02f02")
	public String m02f02(Model model, @Activeuser SecurityUser user) {
		Officer officer = user.getDssUser().getOfficer();
	
		Long mainOrgId;
		if(officer.getWorkAt().getParent().getId() == 0L) {
			mainOrgId = officer.getWorkAt().getId();
		} else {
			mainOrgId = officer.getWorkAt().getParent().getId();
		}
		
		model.addAttribute("mainOrgId", mainOrgId);
		
		return "page/m02f02";
	}

}
