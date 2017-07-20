package tcs3.controller.reports;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import tcs3.model.lab.LabJob;
import tcs3.model.lab.PromotionDiscount;
import tcs3.model.lab.Quotation;
import tcs3.model.lab.Request;
import tcs3.model.lab.RequestSample;
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
	
	private final SimpleDateFormat thAbbrDate = new SimpleDateFormat("d MMM yyyy", new Locale("th","TH"));
	private final SimpleDateFormat thAbbrTime = new SimpleDateFormat("H.mm");

	@RequestMapping(value="/report/requestBarcode/{requestId}", method = RequestMethod.POST, produces = "application/pdf")
	public ModelAndView getRequestBarcodeReportPdf(@PathVariable Long requestId,
			@RequestParam(required=false) Integer reqNoAmount,
			@RequestParam(required=false) Integer[] labNoAmount) {
		final JasperReportsPdfView view = new ThJasperReportsPdfView();
		view.setReportDataKey("beans");
		view.setUrl("classpath:reports/barcode.jrxml");
		view.setApplicationContext(appContext);
		
		final Map<String, Object> params = new HashMap<>();
		Request req = entityService.findRequest(requestId);
		
		if(reqNoAmount == null) {
			reqNoAmount = 1;
		}
		
		
		List<BarcodeBean> list = new ArrayList<BarcodeBean>();
		for(int i=0;i<reqNoAmount;i++){
			BarcodeBean b1 = new BarcodeBean();
			b1.setCode1(req.getReqNo());
			b1.setIsCode1Barcode(true);
			String labNo = req.getReqNo() + ".1";
			if(req.getSamples().size() > 1) {
				labNo += "-" + req.getReqNo() + "." + req.getSamples().size();
			}
			b1.setCode1line2(labNo);
			b1.setDatePrint(thAbbrDate.format(req.getCreatedTime()) + " เวลา " + thAbbrTime.format(req.getCreatedTime()) + "น.");
			list.add(b1);
		}
		for(int m=0;m<labNoAmount.length;m++){
			RequestSample ex = req.getSamples().get(m);
			for(int n=0;n<labNoAmount[m];n++ ){
				BarcodeBean b1 = new BarcodeBean();
				b1.setCode1(ex.getLabNo());
				b1.setCode1line2(req.getMainOrg().getAbbr());
				b1.setIsCode1Barcode(false);
				list.add(b1);
			}
			/**
			for(int p=0;p<ex.getItem();p++ ){
				BarcodeBean b1 = new BarcodeBean();
				b1.setCode1(ex.getLabNo()+ "." + p);
				b1.setCode1line2(req.getMainOrg().getAbbreviation());
				b1.setIsCode1Barcode(false);
				list.add(b1);
			}
			**/
		}
		List<BarcodeBean> beans =  new ArrayList<BarcodeBean>();
		
		
		for(int b=0;b<list.size();b+=2){
			BarcodeBean bean = new BarcodeBean();
			bean.setCode1(list.get(b).getCode1());
			bean.setCode1line2(list.get(b).getCode1line2());
			bean.setIsCode1Barcode(list.get(b).getIsCode1Barcode());
			bean.setDatePrint(list.get(b).getDatePrint());
			if((b+1) < list.size()){
				bean.setCode2(list.get(b+1).getCode1());
				bean.setCode2line2(list.get(b+1).getCode1line2());
				bean.setIsCode2Barcode(list.get(b+1).getIsCode1Barcode());
			}
			beans.add(bean);
			
		}
		
		params.put("beans", beans);
		
		
		return new ModelAndView(view, params);
	}
	
	
	@RequestMapping(value="/report/request/{requestId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView getRequestReportPdf(@PathVariable Long requestId) {
		final JasperReportsPdfView view = new ThJasperReportsPdfView();
		view.setReportDataKey("requestData");
		view.setUrl("classpath:reports/invoice.jrxml");
		view.setApplicationContext(appContext);
		
		final Map<String, Object> params = new HashMap<>();
		Request request = entityService.findRequest(requestId);
		
		List<LabJob> jobs = new ArrayList<LabJob>();
		if(request != null) {
			for(RequestSample sample: request.getSamples()) {
				for(LabJob job : sample.getJobs()) {
					jobs.add(job);
				}
			}
			
			params.put("requestData", jobs);
			
			
			
			String url="http://labtracking.dss.go.th/track?trackingCode="+ request.getTrackingCode() + "&reqNo=" + URLEncoder.encode(request.getReqNo());
			ByteArrayOutputStream out = QRCode.from(url).to(ImageType.PNG).withSize(2500, 2500).stream();
			
			InputStream QRCodeInputStream = new ByteArrayInputStream(out.toByteArray());
			
			params.put("QRCodeInputStream", QRCodeInputStream);
			
			params.put("Request",request);
		}
		
		 return new ModelAndView(view, params);
	}
	
	@RequestMapping(value = "/report/quotation/{quotationId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView getQuotationReportPdf(
			@PathVariable Long quotationId) {
	    
	    final JasperReportsPdfView view = new ThJasperReportsPdfView();
	    
	    Quotation quotation = entityService.findQuotation(quotationId);
	    
	    view.setReportDataKey("testMethodItems");
	    view.setSubReportDataKeys("promotions");
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
	    
	    JRBeanCollectionDataSource promotions = new JRBeanCollectionDataSource(quotation.getPromotions());
	    params.put("promotions", promotions);
	    
	    Integer totalDiscount = 0;
	    for(PromotionDiscount pd : quotation.getPromotions()) {
	    	totalDiscount += pd.getDiscount();
	    }
	    params.put("totalDiscount", totalDiscount);
	    
	    return new ModelAndView(view, params);
	}
	
}
