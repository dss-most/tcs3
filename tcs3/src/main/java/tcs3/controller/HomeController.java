package tcs3.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import tcs3.auth.model.Activeuser;
import tcs3.auth.model.SecurityUser;
import tcs3.model.hrx.Officer;
import tcs3.service.EntityService;

@Controller
public class HomeController {
	
	public static Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private EntityService entityService;
	
	@Autowired 
	private ApplicationContext appContext;
	
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
	
	@RequestMapping("/page/m04f01")
	public String m04f01(Model model, @Activeuser SecurityUser user) {
		Officer officer = user.getDssUser().getOfficer();
	
		Long mainOrgId;
		if(officer.getWorkAt().getParent().getId() == 0L) {
			mainOrgId = officer.getWorkAt().getId();
		} else {
			mainOrgId = officer.getWorkAt().getParent().getId();
		}
		
		model.addAttribute("mainOrgId", mainOrgId);
		
		return "page/m04f01";
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

	@RequestMapping("/page/m02f03")
	public String m02f03(Model model, @Activeuser SecurityUser user) {
		Officer officer = user.getDssUser().getOfficer();
	
		Long mainOrgId;
		if(officer.getWorkAt().getParent().getId() == 0L) {
			mainOrgId = officer.getWorkAt().getId();
		} else {
			mainOrgId = officer.getWorkAt().getParent().getId();
		}
		
		model.addAttribute("mainOrgId", mainOrgId);
		
		return "page/m02f03";
	}
	
	@RequestMapping("/page/m03f01")
	public String m03f01(Model model, @Activeuser SecurityUser user) {
		Officer officer = user.getDssUser().getOfficer();
	
		Long mainOrgId;
		if(officer.getWorkAt().getParent().getId() == 0L) {
			mainOrgId = officer.getWorkAt().getId();
		} else {
			mainOrgId = officer.getWorkAt().getParent().getId();
		}
		
		model.addAttribute("mainOrgId", mainOrgId);
		
		return "page/m03f01";
	}
	
	@RequestMapping("/page/m09f01")
	public String m09f01(Model model, @Activeuser SecurityUser user) {
		Officer officer = user.getDssUser().getOfficer();
	
		Long mainOrgId;
		if(officer.getWorkAt().getParent().getId() == 0L) {
			mainOrgId = officer.getWorkAt().getId();
		} else {
			mainOrgId = officer.getWorkAt().getParent().getId();
		}
		
		model.addAttribute("mainOrgId", mainOrgId);
		
		return "page/m09f01";
	}
	
	
	@RequestMapping(value = "/pdf", method = RequestMethod.GET)
	public ModelAndView getPdf() {
	    JasperReportsPdfView view = new JasperReportsPdfView();
	    view.setUrl("classpath:report/Blank_A4.jrxml");
	    
	    view.setApplicationContext(appContext);
	    Map<String, Object> params = new HashMap<>();
	    
	    params.put("param1", "param1 value");
	    return new ModelAndView(view, params);
	}
}
