package tcs3.controller.reports;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import tcs3.model.lab.Quotation;
import tcs3.service.EntityService;


@Controller
public class ReportController {
	
	@Autowired
	private EntityService entityService;
	
	@Autowired 
	private ApplicationContext appContext;
	
	public static Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	public static DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMM yyyy")
			.withChronology(BuddhistChronology.getInstance())
			.withLocale(new Locale("th", "TH"));

	@RequestMapping(value = "/report/quotation/{quotationId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView getQuotationReportPdf(
			@PathVariable Long quotationId) {
	    
	    final JasperReportsPdfView view = new ThJasperReportsPdfView();
	    
	    Quotation quotation = entityService.findQuotation(quotationId);
	    
	    view.setReportDataKey("testMethodItems");
	    view.setUrl("classpath:reports/quotationReport.jrxml");
	    view.setApplicationContext(appContext);

	    final Map<String, Object> params = new HashMap<>();
	    
	    if(quotation.getAddress() != null) {
	    
	    	
	    	if(quotation.getAddress().getLine1() != null ) {
	    		params.put("addressLine1", quotation.getAddress().getLine1());
	    	} else {
	    		params.put("addressLine1", quotation.getAddress().getLine1FromOldAddress());	
	    	}
	    	
	    	if(quotation.getAddress().getLine2() != null ) {
	    		params.put("addressLine2", quotation.getAddress().getLine2());
	    	} else {
	    		params.put("addressLine2", quotation.getAddress().getLine2FromOldAddress());	
	    	}
	    	
	    	params.put("districtName", quotation.getAddress().getDistrict().getName());
	    	params.put("provinceName", quotation.getAddress().getProvince().getName());
	    	params.put("zipCode", quotation.getAddress().getZipCode());
	    	

	    } else {
	    	params.put("addressLine1", "-");
	    	params.put("addressLine2", "");
	    	params.put("districtName", "");
	    	params.put("provinceName", "");
	    	params.put("zipCode", "");
	    }
	    
    	
    	
	    
	    if(quotation.getContact() != null) {
	    	params.put("contactPerson", quotation.getContact().getFirstName() + "  " +quotation.getContact().getLastName());
	    	params.put("email", quotation.getContact().getEmail());
	    	params.put("fax", quotation.getContact().getFax());
	    	params.put("mobilePhone", quotation.getContact().getMobilePhone());
	    	params.put("officePhone", quotation.getContact().getOfficePhone());
	    } else {
	    	params.put("contactPerson", "");
	    	params.put("email", "");
	    	params.put("fax", "");
	    	params.put("mobilePhone", "");
	    	params.put("officePhone", "");
	    }
	    params.put("companyNameTh", quotation.getCompany().getNameTh());
	    params.put("quotationNo", quotation.getQuotationNo());
	    params.put("quotation", quotation);
	    params.put("name", quotation.getName());
	    
	    params.put("quotationDateStr", fmt.print(quotation.getQuotationDate().getTime()));
	    params.put("estimatedDay", quotation.getEstimatedDay());
	    
	    params.put("testMethodItems", quotation.getTestMethodItems());
	    
	    return new ModelAndView(view, params);
	}
	
}
