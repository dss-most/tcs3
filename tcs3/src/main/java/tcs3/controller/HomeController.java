package tcs3.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
	
	@RequestMapping("/page/m09f02")
	public String m09f02(Model model, @Activeuser SecurityUser user) {
		return "page/m09f02";
	}
	
	
	@GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> getPdf() {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File file;
		try{
			Map<String, Object> params = new HashMap<>();
	    	params.put("param1", "param1 value");

			file =ResourceUtils.getFile("classpath:reports/Blank_A4.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params);

			JasperExportManager.exportReportToPdfStream(jasperPrint, out);

		} catch(IOException | JRException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(out.toByteArray());
	}
}
