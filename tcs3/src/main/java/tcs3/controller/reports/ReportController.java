package tcs3.controller.reports;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;
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
	
	public static DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM yyyy").withLocale(new Locale("TH", "th", "TH"));

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
	    	
	    	params.put("name", quotation.getName());
	    }
	    
	    
	    if(quotation.getContact() != null) {
	    	params.put("contactPerson", quotation.getContact().getFirstName() + quotation.getContact().getLastName());
	    }
	    params.put("companyNameTh", quotation.getCompany().getNameTh());
	    params.put("quotationNo", quotation.getQuotationNo());
	    
	    logger.debug("quotationDate: "+  quotation.getQuotationDate());
	    
	    params.put("quotationDateStr", fmt.print(new DateTime(quotation.getQuotationDate())));
	    params.put("estimatedDay", quotation.getEstimatedDay());
	    
	    
	    
	    params.put("testMethodItems", quotation.getTestMethodItems());

	    return new ModelAndView(view, params);
	}
	
}
