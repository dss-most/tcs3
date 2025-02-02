package tcs3.controller.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.examples.signature.CreateSignatureBase;
import org.apache.pdfbox.examples.signature.CreateVisibleSignature2;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.googlecode.jthaipdf.jasperreports.engine.ThaiExporterManager;
import com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import net.sf.jasperreports.data.DataSourceCollection;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import oracle.security.o3logon.O3LoginClientHelper;
import tcs3.model.lab.LabJob;
import tcs3.model.lab.PromotionDiscount;
import tcs3.model.lab.Quotation;
import tcs3.model.lab.Request;
import tcs3.model.lab.RequestSample;
import tcs3.service.EntityService;
import tcs3.utils.CreateDSSVisualSignature;
import tcs3.webUI.NumToBaht;

class CreateSignature extends CreateSignatureBase {
	public CreateSignature(KeyStore keystore, char[] pin)
			throws KeyStoreException,
			UnrecoverableKeyException,
			NoSuchAlgorithmException,
			IOException,
			CertificateException {
		super(keystore, pin);
	}
}

@Controller
public class ReportController {

	@Autowired
	private EntityService entityService;

	@Autowired
	private ApplicationContext appContext;

	public static Logger logger = LoggerFactory.getLogger(ReportController.class);

	public static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMM yyyy")
			.withChronology(ThaiBuddhistChronology.INSTANCE)
			.withLocale(new Locale("th", "TH"));

	public static SimpleDateFormat jsonDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "US"));

	private final SimpleDateFormat thAbbrDate = new SimpleDateFormat("d MMM yyyy", new Locale("th", "TH"));
	private final SimpleDateFormat thAbbrTime = new SimpleDateFormat("H.mm");

	public ResponseEntity<byte[]> getPdfReport(String reportName, Map<String, Object> params,
			JRDataSource datatsource, boolean shouldSigned, String reason) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		File file;

		try {
			params.put("param1", "param1 value");

			file = ResourceUtils.getFile("classpath:" + reportName);
			JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datatsource);

			JasperExportManager.exportReportToPdfStream(jasperPrint, out);
			// ThaiExporterManager.exportReportToPdfStream(jasperPrint, out);

			if (shouldSigned) {
				out = signPdf(out, reason, "ระบบเทคโนโลยีสารสนเทศกรมวิทยาศาสตร์บริการ", "saraban@dss.go.th");
			}

			// PDDocument doc = Loader.loadPDF(out.toByteArray());
			// int pageNum = doc.getNumberOfPages();
			// doc.close();

			// Resource dssFadedResource = new
			// ClassPathResource("static/graphics/document-signed-svgrepo-com.png");
			// Resource dssCertificate = new ClassPathResource("dss.p12");
			// char[] password = "dssits".toCharArray();

			// KeyStore ks = KeyStore.getInstance("PKCS12");
			// ks.load(dssCertificate.getInputStream(), password);
			// CreateDSSVisualSignature signing = new CreateDSSVisualSignature(ks,
			// password.clone());
			// signing.setImageFile(dssFadedResource.getFile());
			// signing.setReason("จัดทำใบเสนอราคา");
			// signing.setLocation("ระบบเทคโนโลยีสารสนเทศกรมวิทยาศาสตร์บริการ");
			// signing.setContactInfo("saraban@dss.go.th");
			// Rectangle2D humanRect = new Rectangle2D.Float(5, 780, 500, 50);

			// // pageNumber

			// for (int i = 0; i < pageNum; i++) {
			// out = signing.signPDF(out, i, humanRect, null);
			// }

		} catch (IOException | JRException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| UnrecoverableKeyException e) {
			logger.info("xxx");

			e.printStackTrace();
		}

		return ResponseEntity.ok(out.toByteArray());
	}

	private ByteArrayOutputStream signPdf(ByteArrayOutputStream out, String reason, String location, String contactInfo)
			throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException {
		PDDocument doc = Loader.loadPDF(out.toByteArray());
		int pageNum = doc.getNumberOfPages();
		doc.close();

		Resource dssFadedResource = new ClassPathResource("static/graphics/document-signed-svgrepo-com.png");
		Resource dssCertificate = new ClassPathResource("dss-ereport-v2.p12");
		char[] password = "dssereport".toCharArray();

		KeyStore ks = KeyStore.getInstance("PKCS12");
		ks.load(dssCertificate.getInputStream(), password);
		CreateDSSVisualSignature signing = new CreateDSSVisualSignature(ks, password.clone());
		signing.setImageFile(dssFadedResource.getFile());
		// signing.setReason("จัดทำใบเสนอราคา");
		// signing.setLocation("ระบบเทคโนโลยีสารสนเทศกรมวิทยาศาสตร์บริการ");
		// signing.setContactInfo("saraban@dss.go.th");

		signing.setReason(reason);
		signing.setLocation(location);
		signing.setContactInfo(contactInfo);

		Rectangle2D humanRect = new Rectangle2D.Float(5, 810, 500, 25);

		// pageNumber

		for (int i = 0; i < pageNum; i++) {
			out = signing.signPDF(out, i, humanRect, null);
		}
		return out;
	}

	@RequestMapping(value = "/report/requestBarcode/{requestId}", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/pdf")
	public ResponseEntity<byte[]> getRequestBarcodeReportPdf(@PathVariable Long requestId,
			@RequestParam(required = false) Integer reqNoAmount,
			@RequestParam(required = false) Integer[] labNoAmount) {

		// view.setReportDataKey("beans");
		// view.setUrl("classpath:reports/barcode.jrxml");
		// view.setApplicationContext(appContext);

		final Map<String, Object> params = new HashMap<>();
		Request req = entityService.findRequest(requestId);

		if (reqNoAmount == null) {
			reqNoAmount = 1;
		}

		List<BarcodeBean> list = new ArrayList<BarcodeBean>();
		for (int i = 0; i < reqNoAmount; i++) {
			BarcodeBean b1 = new BarcodeBean();
			b1.setLabNo1(req.getReqNo());
			b1.setCode1(req.getInvoiceNoBillPayment());
			b1.setIsCode1Barcode(true);
			String labNo = req.getReqNo() + ".1";
			if (req.getSamples().size() > 1) {
				labNo += "-" + req.getReqNo() + "." + req.getSamples().size();
			}
			b1.setCode1line2(labNo);
			b1.setDatePrint(thAbbrDate.format(req.getCreatedTime()) + " เวลา " + thAbbrTime.format(req.getCreatedTime())
					+ "น.");
			list.add(b1);
		}
		for (int m = 0; m < labNoAmount.length; m++) {
			RequestSample ex = req.getSamples().get(m);
			for (int n = 0; n < labNoAmount[m]; n++) {
				BarcodeBean b1 = new BarcodeBean();
				b1.setCode1(ex.getLabNo());
				b1.setCode1line2(req.getMainOrg().getAbbr());
				b1.setIsCode1Barcode(false);
				list.add(b1);
			}
			/**
			 * for(int p=0;p<ex.getItem();p++ ){
			 * BarcodeBean b1 = new BarcodeBean();
			 * b1.setCode1(ex.getLabNo()+ "." + p);
			 * b1.setCode1line2(req.getMainOrg().getAbbreviation());
			 * b1.setIsCode1Barcode(false);
			 * list.add(b1);
			 * }
			 **/
		}
		List<BarcodeBean> beans = new ArrayList<BarcodeBean>();

		for (int b = 0; b < list.size(); b += 2) {
			BarcodeBean bean = new BarcodeBean();
			bean.setCode1(list.get(b).getCode1());
			bean.setLabNo1(list.get(b).getLabNo1());
			bean.setCode1line2(list.get(b).getCode1line2());
			bean.setIsCode1Barcode(list.get(b).getIsCode1Barcode());
			bean.setDatePrint(list.get(b).getDatePrint());
			if ((b + 1) < list.size()) {
				bean.setCode2(list.get(b + 1).getCode1());
				bean.setLabNo2(list.get(b + 1).getLabNo1());
				bean.setCode2line2(list.get(b + 1).getCode1line2());
				bean.setIsCode2Barcode(list.get(b + 1).getIsCode1Barcode());
			}
			beans.add(bean);

		}

		params.put("beans", beans);

		return getPdfReport("reports/barcode.jrxml", params, new JRBeanCollectionDataSource(beans), false, null);

	}

	@RequestMapping(value = "/report/invoice/BillPayment/{requestId}", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/pdf")
	public ResponseEntity<byte[]> getRequestReportOnlineBankPdf(@PathVariable Long requestId,
			@RequestParam(required = false) String paymentDueDate) {

		// final JasperReportsPdfView view = new ThJasperReportsPdfView();
		// view.setReportDataKey("requestData");
		// view.setUrl("classpath:reports/invoiceBillPayment.jrxml");
		// view.setApplicationContext(appContext);

		logger.debug("paymentDueDate :" + paymentDueDate);

		final Map<String, Object> params = new HashMap<>();
		Request request = entityService.findRequest(requestId);

		if (paymentDueDate != null && paymentDueDate.length() > 0) {
			try {
				request.setPaymentDueDate(jsonDateFormat.parse(paymentDueDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				request.setPaymentDueDate(new Date());
			}
		}

		entityService.saveRequest(request);

		List<LabJob> jobs = new ArrayList<LabJob>();
		if (request != null) {
			for (RequestSample sample : request.getSamples()) {
				for (LabJob job : sample.getJobs()) {
					jobs.add(job);
				}
			}

			params.put("requestData", jobs);

			String url = "http://labtracking.dss.go.th/track?trackingCode=" + request.getTrackingCode() + "&reqNo="
					+ URLEncoder.encode(request.getReqNo());
			ByteArrayOutputStream out = QRCode.from(url).to(ImageType.PNG).withSize(2500, 2500).stream();

			InputStream QRCodeInputStream = new ByteArrayInputStream(out.toByteArray());

			params.put("QRCodeInputStream", QRCodeInputStream);

			String bankQRCode = NumToBaht.toBarcode(request.getInvoiceNoBillPayment(), request.getTotalFee());

			logger.debug(bankQRCode);

			ByteArrayOutputStream out2 = QRCode.from(bankQRCode).to(ImageType.PNG).withSize(2500, 2500).stream();

			InputStream bankQRCodeInputStream = new ByteArrayInputStream(out2.toByteArray());

			params.put("bankQRCodeInputStream", bankQRCodeInputStream);

			params.put("Request", request);
		}

		return getPdfReport("reports/invoiceBillPayment.jrxml", params, new JRBeanCollectionDataSource(jobs), true,
				"จัดทำใบแจ้งชำระค่าบริการ");

	}

	@RequestMapping(value = "/report/invoice/WalkIn/{requestId}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<byte[]> getRequestReportPdf(@PathVariable Long requestId) {
		// final JasperReportsPdfView view = new ThJasperReportsPdfView();
		// view.setReportDataKey("requestData");
		// view.setUrl("classpath:reports/invoiceWalkIn.jrxml");
		// view.setApplicationContext(appContext);

		final Map<String, Object> params = new HashMap<>();
		Request request = entityService.findRequest(requestId);

		List<LabJob> jobs = new ArrayList<LabJob>();
		if (request != null) {
			for (RequestSample sample : request.getSamples()) {
				for (LabJob job : sample.getJobs()) {
					jobs.add(job);
				}
			}

			params.put("requestData", jobs);

			String url = "http://labtracking.dss.go.th/track?trackingCode=" + request.getTrackingCode() + "&reqNo="
					+ URLEncoder.encode(request.getReqNo());
			ByteArrayOutputStream out = QRCode.from(url).to(ImageType.PNG).withSize(2500, 2500).stream();

			InputStream QRCodeInputStream = new ByteArrayInputStream(out.toByteArray());

			params.put("QRCodeInputStream", QRCodeInputStream);

			params.put("Request", request);
		}

		return getPdfReport("reports/invoiceWalkIn.jrxml", params, new JRBeanCollectionDataSource(jobs), false, null);

		// return new ModelAndView(view, params);
	}

	@RequestMapping(value = "/report/quotation/{quotationId}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity<byte[]> getQuotationReportPdf(
			@PathVariable Long quotationId) {

		// final JasperReportsPdfView view = new ThJasperReportsPdfView();

		Quotation quotation = entityService.findQuotation(quotationId);

		// view.setReportDataKey("testMethodItems");
		// view.setSubReportDataKeys("promotions");
		// view.setUrl("classpath:reports/quotationReport.jrxml");
		// view.setApplicationContext(appContext);

		final Map<String, Object> params = new HashMap<>();

		if (quotation.getAddress() != null) {

			if (quotation.getAddress().getLine1() != null) {
				params.put("addressLine1", quotation.getAddress().getLine1());
			} else {
				params.put("addressLine1", quotation.getAddress().getLine1FromOldAddress());
			}

			if (quotation.getAddress().getLine2() != null) {
				params.put("addressLine2", quotation.getAddress().getLine2());
			} else {
				params.put("addressLine2", quotation.getAddress().getLine2FromOldAddress());
			}

			// logger.debug("quotation ID: " + quotation.getId());
			// logger.debug("quotation.getAddress.getID: " +
			// quotation.getAddress().getId());
			// logger.debug("quotatio);

			if (quotation.getAddress().getProvince() != null) {
				if (quotation.getAddress().getProvince().getId().equals(21L)) {
					params.put("districtName", quotation.getAddress().getDistrict() == null ? ""
							: "เขต" + quotation.getAddress().getDistrict().getName());
					params.put("provinceName", quotation.getAddress().getProvince() == null ? ""
							: quotation.getAddress().getProvince().getName());

				} else {
					params.put("districtName", quotation.getAddress().getDistrict() == null ? ""
							: "อ." + quotation.getAddress().getDistrict().getName());
					params.put("provinceName", quotation.getAddress().getProvince() == null ? ""
							: "จ." + quotation.getAddress().getProvince().getName());
				}
				logger.debug("----------" + quotation.getAddress().getZipCode());
				params.put("zipCode", quotation.getAddress().getZipCode());
			} else {

				params.put("districtName", quotation.getAddress().getDistrict() == null ? ""
						: quotation.getAddress().getDistrict().getName());
				params.put("provinceName", quotation.getAddress().getProvince() == null ? ""
						: quotation.getAddress().getProvince().getName());
				params.put("zipCode", quotation.getAddress().getZipCode());
			}

		} else {
			params.put("addressLine1", "-");
			params.put("addressLine2", "");
			params.put("districtName", "");
			params.put("provinceName", "");
			params.put("zipCode", "");
		}

		if (quotation.getContact() != null) {
			params.put("contactPerson",
					quotation.getContact().getFirstName() + "  " + quotation.getContact().getLastName());
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

		params.put("quotationDateStr",
				fmt.format((new java.sql.Date(quotation.getQuotationDate().getTime())).toLocalDate()));
		params.put("estimatedDay", quotation.getEstimatedDay());

		params.put("testMethodItems", quotation.getTestMethodItems());

		JRBeanCollectionDataSource promotions = new JRBeanCollectionDataSource(quotation.getPromotions());
		params.put("promotions", promotions);

		Integer totalDiscount = 0;
		for (PromotionDiscount pd : quotation.getPromotions()) {
			totalDiscount += pd.getDiscount();
		}
		params.put("totalDiscount", totalDiscount);

		return getPdfReport("reports/quotationReport.jrxml", params,
				new JRBeanCollectionDataSource(quotation.getTestMethodItems()), true, "จัดทำใบเสนอราคา");

		// return new ModelAndView(view, params);
	}

}
