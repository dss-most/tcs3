package tcs3.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

import tcs3.auth.model.DssUser;
import tcs3.auth.model.SecurityUser;
import tcs3.auth.service.DssUserRepository;
import tcs3.model.customer.Address;
import tcs3.model.customer.Company;
import tcs3.model.customer.Customer;
import tcs3.model.fin.InvoiceAddress;
import tcs3.model.fin.InvoiceDetail;
import tcs3.model.fin.InvoiceLanguage;
import tcs3.model.global.District;
import tcs3.model.global.Province;
import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.Invoice;
import tcs3.model.lab.JobPriority;
import tcs3.model.lab.LabJob;
import tcs3.model.lab.LabJobStatus;
import tcs3.model.lab.LabNoSequence;
import tcs3.model.lab.Promotion;
import tcs3.model.lab.PromotionDiscount;
import tcs3.model.lab.QLabNoSequence;
import tcs3.model.lab.QPromotion;
import tcs3.model.lab.QQuotation;
import tcs3.model.lab.QQuotationNumber;
import tcs3.model.lab.QQuotationTemplate;
import tcs3.model.lab.QReport;
import tcs3.model.lab.QRequest;
import tcs3.model.lab.QRequestSample;
import tcs3.model.lab.QSampleType;
import tcs3.model.lab.QTestMethod;
import tcs3.model.lab.QTestProduct;
import tcs3.model.lab.Quotation;
import tcs3.model.lab.QuotationNumber;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.Report;
import tcs3.model.lab.ReportDeliveryMethod;
import tcs3.model.lab.ReportLanguage;
import tcs3.model.lab.ReportStatus;
import tcs3.model.lab.Request;
import tcs3.model.lab.RequestAddress;
import tcs3.model.lab.RequestHistory;
import tcs3.model.lab.RequestSample;
import tcs3.model.lab.RequestSample12;
import tcs3.model.lab.RequestStatus;
import tcs3.model.lab.RequestTracker;
import tcs3.model.lab.SampleType;
import tcs3.model.lab.TestMethod;
import tcs3.model.lab.TestMethodQuotationItem;
import tcs3.model.lab.TestMethodQuotationTemplateItem;
import tcs3.model.lab.TestProduct;
import tcs3.model.lab.TestProductCategory;
import tcs3.repository.AddressRepository;
import tcs3.repository.CompanyRepository;
import tcs3.repository.CustomerRepository;
import tcs3.repository.FinInvoiceRepository;
import tcs3.repository.InvoiceRepository;
import tcs3.repository.LabJobRepository;
import tcs3.repository.LabNoSequenceRepository;
import tcs3.repository.OfficerRepository;
import tcs3.repository.OrganizationRepository;
import tcs3.repository.PromotionDiscountRepository;
import tcs3.repository.PromotionRepository;
import tcs3.repository.QuotationNumberRepository;
import tcs3.repository.QuotationRepository;
import tcs3.repository.QuotationTemplateRepository;
import tcs3.repository.RequestAddressRepository;
import tcs3.repository.RequestPromotionDiscountRepository;
import tcs3.repository.RequestRepository;
import tcs3.repository.RequestSampleRepository;
import tcs3.repository.SampleTypeRepo;
import tcs3.repository.TestMethodQuotationItemRepo;
import tcs3.repository.TestMethodQuotationTemplateItemRepo;
import tcs3.repository.TestMethodRepository;
import tcs3.repository.TestProductCategoryRepository;
import tcs3.repository.TestProductRepository;
import tcs3.webUI.DefaultProperty;
import tcs3.webUI.ResponseJSend;
import tcs3.webUI.ResponseStatus;

@Service
@Transactional
public class EntityServiceJPA implements EntityService {
	public static Logger logger = LoggerFactory.getLogger(EntityServiceJPA.class);
	
	public static SimpleDateFormat jsonDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "US"));
	public static SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy", new Locale("th", "TH"));
	
	@Autowired
	private OfficerRepository officerRepo;
	
	@Autowired
	private OrganizationRepository organizationRepo;
	
	@Autowired
	private QuotationTemplateRepository quotationTemplateRepo;

	@Autowired
	private QuotationRepository quotationRepo;
	
	@Autowired
	private TestMethodRepository testMethodRepo;

	@Autowired
	private TestMethodQuotationTemplateItemRepo testMethodQuotationTemplateItemRepo;
	
	@Autowired
	private TestMethodQuotationItemRepo testMethodQuotationItemRepo;
	
	@Autowired
	private CompanyRepository companyRepo;
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private RequestAddressRepository requestAddressRepo;
	
	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private RequestRepository requestRepo;

	@Autowired
	private RequestSampleRepository requestSampleRepo;

	@Autowired
	private LabJobRepository labJobRepo;
	
	@Autowired
	private QuotationNumberRepository quotationNumberRepo;
	
	@Autowired
	private SampleTypeRepo sampleTypeRepo;
	
	@Autowired
	private PromotionRepository promotionRepo;

	@Autowired
	private InvoiceRepository invoiceRepo;
	
	@Autowired
	private FinInvoiceRepository finInvoiceRepo;
	
	@Autowired
	private PromotionDiscountRepository promotionDiscountRepo;

	@Autowired
	private RequestPromotionDiscountRepository requestPromotionDiscountRepo;
	
	@Autowired
	private LabNoSequenceRepository labNoSequenceRepository;
	
	@Autowired
	private DssUserRepository dssUserRepo;
	
	@Autowired
	private TestProductRepository testProductRepo;
	
	@Autowired
	private TestProductCategoryRepository testProductCategoryRepo;
	
	@PersistenceContext 
	private EntityManager entityManager;
	
	private ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		mapper.setDateFormat(sdf);
		return mapper;
	}
	
	@Override
	public Officer findOfficerByUserName(String userName) {
		Officer officer = officerRepo.findByDssUser_UserName(userName);
		
		return officer;
	}
	
	@Override
	@Transactional
	public ResponseJSend<Quotation> saveQuotation(JsonNode node, SecurityUser user) {
		ResponseJSend<Quotation> response = new ResponseJSend<Quotation>();
		
		Calendar now = Calendar.getInstance(new Locale("th", "TH"));
		int year = now.get(Calendar.YEAR);
		
		DssUser dbUser = null;
		if(user != null) {
			dbUser = dssUserRepo.findOne(user.getDssUser().getId());
		}
		
		Quotation quotation;
		if(node.get("id") == null) {
			quotation = new Quotation();
			quotationRepo.save(quotation);
			logger.debug("ID is null");		
			
		
		} else {
			
			quotation = quotationRepo.findOne(node.get("id").asLong());
			logger.debug("ID: "  + quotation.getId());		
		}
		
		quotation.setCreatedBy(dbUser.getOfficer());
		quotation.setCode(node.get("code") == null || node.get("code").asText().equals("null") ? "" : node.get("code").asText());
		quotation.setName(node.get("name") == null || node.get("name").asText().equals("null") ? "" : node.get("name").asText());
		
		if(node.path("sampleType").path("id").asLong() > 0) {
			SampleType sampleType = sampleTypeRepo.findOne(node.path("sampleType").path("id").asLong());
			quotation.setSampleType(sampleType);
		}
		
		if(node.get("groupOrg") != null) {
			if(node.get("groupOrg").get("id") != null) {
				Organization groupOrg = organizationRepo.findOne(node.get("groupOrg").get("id").asLong());
				quotation.setGroupOrg(groupOrg);
				quotation.setMainOrg(groupOrg.getParent());
			}
		} 
		
		if(node.get("id") == null) {
			String quotationNo = getQuotationNumber(year, quotation);
			quotation.setQuotationNo(quotationNo);
		}
			
		
		quotation.setSampleNote(node.get("sampleNote") == null || node.get("sampleNote").asText().equals("null") ? "" : node.get("sampleNote").asText());
		quotation.setSamplePrep(node.get("samplePrep") == null || node.get("samplePrep").asText().equals("null") ? "" : node.get("samplePrep").asText());
		quotation.setRemark(node.get("remark") == null  || node.get("remark").asText().equals("null")  ? "" : node.get("remark").asText());

		quotation.setSampleNum(node.get("sampleNum") == null ? 0 : node.get("sampleNum").asInt());
		quotation.setCopyNum(node.get("copyNum") == null ? 0 : node.get("copyNum").asInt());
		quotation.setCopyFee(node.get("copyFee") == null ? 0 : node.get("copyFee").asInt());
		quotation.setCoaNum(node.get("coaNum") == null ? 0 : node.get("coaNum").asInt());
		quotation.setCoaFee(node.get("coaFee") == null ? 0 : node.get("coaFee").asInt());
		quotation.setTranslateNum(node.get("translateNum") == null ? 0 : node.get("translateNum").asInt());
		quotation.setTranslateFee(node.get("translateFee") == null ? 0 : node.get("translateFee").asInt());
		quotation.setEtcFee(node.get("etcFee") == null ? 0 : node.get("etcFee").asInt());
		quotation.setEtc(node.get("etc") == null || node.get("etc").asText().equals("null") ? "" : node.get("etcFee").asText());
		
		quotation.setServiceNo(node.path("serviceNo").asText());
		
		// new item
		if(node.get("company") != null) {
			if(node.get("company").get("id") != null) {
				Company company = companyRepo.findOne(node.get("company").get("id").asLong());
				quotation.setCompany(company);
			}
		}
		if(node.get("address") != null) {
			if(node.get("address").get("id") != null) {
				Address address = addressRepo.findOne(node.get("address").get("id").asLong());
				quotation.setAddress(address);
			}
		}
		if(node.get("contact") != null) {
			if(node.get("contact").get("id") != null) {
				Customer contact = customerRepo.findOne(node.get("contact").get("id").asLong());
				quotation.setContact(contact);
			}
		}
		
		quotation.setEstimatedDay(node.get("estimatedDay") == null ? 0 : node.get("estimatedDay").asInt());
		quotation.setQuotationDate(new Date());
		
		
		
		List<TestMethodQuotationItem> oldItemList = quotation.getTestMethodItems(); 
		List<TestMethodQuotationItem> itemList = new ArrayList<TestMethodQuotationItem>();
		
		for(JsonNode itemNode : node.get("testMethodItems")){
			TestMethodQuotationItem item;
			
			item = new TestMethodQuotationItem();	
			
			item.setQuotation(quotation);
			if(itemNode.get("fee") != null) {
				item.setFee(itemNode.get("fee").asDouble());
			}
			
			if(itemNode.get("quantity") !=null) {
				item.setQuantity(itemNode.get("quantity").asInt());
			}
			
			if(itemNode.get("name") != null) {
				item.setName(itemNode.get("name").asText());
			}
			if(itemNode.get("testMethod") != null && itemNode.get("testMethod").get("id") != null) {
				Long testMethodId=itemNode.get("testMethod").get("id").asLong();
				TestMethod testMethod = testMethodRepo.findOne(testMethodId);
				item.setTestMethod(testMethod);
			}
			
			
			itemList.add(item);
		}
		
		
		quotation.setTestMethodItems(itemList);
		if(oldItemList != null) {
			testMethodQuotationItemRepo.delete(oldItemList);
		}

		quotation.reCalculateTestMethodItemsRowNo();
		quotation = quotationRepo.save(quotation);
		
		// now check if there is any promotion
		if(node.path("promotions").isArray()) {
			quotation.setPromotions(new ArrayList<PromotionDiscount>() );
			for(JsonNode promotion : node.path("promotions") ) {
				Promotion p = promotionRepo.findOne(promotion.path("promotion").path("id").asLong());
				PromotionDiscount pd = new PromotionDiscount();
				pd.setQuotation(quotation);
				pd.setPromotion(p);
				pd.setDiscount(promotion.path("discount").asInt());
				
				quotation.getPromotions().add(pd);
				
			}
			
			promotionDiscountRepo.save(quotation.getPromotions());
		}
		
		
		response.status = ResponseStatus.SUCCESS;
		response.data = quotation;
		
		return response;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	private String getQuotationNumber(int year, Quotation quotation) {
		QQuotationNumber qQuotationNumber = QQuotationNumber.quotationNumber;
		
		
		BooleanExpression currentNumber = qQuotationNumber.year.eq(year)
				.and(qQuotationNumber.organization.eq(quotation.getMainOrg()));
		
		QuotationNumber number = quotationNumberRepo.findOne(currentNumber);
		
		if(number == null) {
			number = new QuotationNumber();
			number.setYear(year);
			number.setOrganization(quotation.getMainOrg());
			number.setCurrentNumber(0);
		}
		
		logger.debug("currentNumber: " + number.getCurrentNumber());
		
		quotationNumberRepo.save(number);
		
		// now come the hard part quotationNo
		return number.generateQuotationNumber();
		
		
	}

	@Override
	public ResponseJSend<Long> saveQuotationTemplate(
			JsonNode node) {
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		
		QuotationTemplate qt;
		if(node.get("id") == null) {
			qt = new QuotationTemplate();
			logger.debug("ID is null");		
		
		} else {
			
			qt = quotationTemplateRepo.findOne(node.get("id").asLong());
			logger.debug("ID: "  + qt.getId());		
		}
		
		qt.setIsActive(node.get("isActive") == null ? true : node.get("isActive").asBoolean());
		
		qt.setCode(node.get("code") == null ? "" : node.get("code").asText());
		qt.setName(node.get("name") == null ? "" : node.get("name").asText());
		
		if(node.get("groupOrg") != null) {
			if(node.get("groupOrg").get("id") != null) {
				Organization groupOrg = organizationRepo.findOne(node.get("groupOrg").get("id").asLong());
				qt.setGroupOrg(groupOrg);
				qt.setMainOrg(groupOrg.getParent());
			}
		}
		
		if(node.path("sampleType").path("id").asLong() > 0) {
			SampleType sampleType = sampleTypeRepo.findOne(node.path("sampleType").path("id").asLong());
			qt.setSampleType(sampleType);
		}
		
		// we have to check if we have duplicate code?
		QuotationTemplate qt1 = quotationTemplateRepo.findByCode(qt.getCode());
		if(qt1 != null && qt1.getId() != qt.getId()) {
			response.status = ResponseStatus.FAIL;
			response.message = "รหัส " + qt1.getCode() + " มีอยู่ในฐานข้อมูลแล้ว";
			return response;
		}
		
		
		
		qt.setSampleNote(node.get("sampleNote") == null ? "" : node.get("sampleNote").asText());
		qt.setSamplePrep(node.get("samplePrep") == null ? "" : node.get("samplePrep").asText());
		qt.setRemark(node.get("remark") == null ? "" : node.get("remark").asText());
		
		
		List<TestMethodQuotationTemplateItem> oldItemList = qt.getTestMethodItems();
		List<TestMethodQuotationTemplateItem> itemList = new ArrayList<TestMethodQuotationTemplateItem>();
		
		for(JsonNode itemNode : node.get("testMethodItems")){
			TestMethodQuotationTemplateItem item;
			
			item = new TestMethodQuotationTemplateItem();	
			
			item.setQuotationTemplate(qt);
			if(itemNode.get("fee") != null) {
				item.setFee(itemNode.get("fee").asDouble());
			}
			
			if(itemNode.get("quantity") !=null) {
				item.setQuantity(itemNode.get("quantity").asInt());
			}
			
			if(itemNode.get("name") != null) {
				item.setName(itemNode.get("name").asText());
			}
			if(itemNode.get("testMethod") != null && itemNode.get("testMethod").get("id") != null) {
				Long testMethodId=itemNode.get("testMethod").get("id").asLong();
				TestMethod testMethod = testMethodRepo.findOne(testMethodId);
				item.setTestMethod(testMethod);
			}
			
			
			itemList.add(item);
		}
		
		
		qt.setTestMethodItems(itemList);
		if(oldItemList != null) {
			testMethodQuotationTemplateItemRepo.delete(oldItemList);
		}

		qt = quotationTemplateRepo.save(qt);
		
		response.status = ResponseStatus.SUCCESS;
		response.data = qt.getId();
		
		return response;
	}

	@Override
	public List<TestMethod> findTestMethodByNameThLike(String nameTh) {
		
		String search = "%" + nameTh + "%";
		
		List<TestMethod> testMethods = testMethodRepo.findTestMethodByNameThLike(search);
		
		return testMethods;
	}

	@Override
	public Organization findOrgannizationById(Long id) {
		return organizationRepo.findOne(id);
	}

	@Override
	public List<Organization> findTopOrgannization() {
		Long[] ids = {4L,827L,835L,848L,856L};
		
		return organizationRepo.findAllByIds(Arrays.asList(ids));
	}

	@Override
	public List<Organization> findOrgannizationChildrenOfId(Long id) {
		
		return organizationRepo.findAllByParent_Id(id);
	}

	@Override
	public ResponseJSend<Page<TestMethod>> findTestMethodByNameOrCode(
			String query, Integer pageNumber) {
		ResponseJSend<Page<TestMethod>> response = new ResponseJSend<Page<TestMethod>>();
		
		query = "%"+query+"%";
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "nameTh");
		
		Page<TestMethod> testMethods = testMethodRepo.findByNameThLikeOrNameEnLikeOrCodeLike(query, pageRequest);
		
		response.data=testMethods;
		response.status=ResponseStatus.SUCCESS;
		return response;
	}

	
	
	@Override
	public ResponseJSend<Page<Quotation>> findQuotationByField(
			JsonNode node,
			Integer pageNumber) throws JsonMappingException {
		QQuotation q = QQuotation.quotation;
		BooleanBuilder p = new BooleanBuilder();
		
		ObjectMapper mapper = getObjectMapper();
		
		Quotation webModel;
		try {
			webModel = mapper.treeToValue(node, Quotation.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		logger.debug(node.toString());
		
		//logger.debug("findQuotationByExample: " + webModel.getContact().getFirstName());
		
		if(webModel.getQuotationNo()!=null && webModel.getQuotationNo().length()>0) {
			p=p.and(q.quotationNo.contains(webModel.getQuotationNo().trim()));
		}
		
		if(webModel.getCompany()!=null && webModel.getCompany().getNameTh()!=null && webModel.getCompany().getNameTh().length()>0) {
			p=p.andAnyOf(q.company.nameTh.contains(webModel.getCompany().getNameTh().trim()), 
					q.company.nameEn.contains(webModel.getCompany().getNameTh().trim()));
		}
		
		if(webModel.getContact() != null && webModel.getContact().getFirstName()!=null && webModel.getContact().getFirstName().length()>0) {
			p=p.andAnyOf(q.contact.firstName.contains(webModel.getContact().getFirstName().trim()), 
					q.contact.lastName.contains(webModel.getContact().getFirstName().trim()));
		}
		
		if(webModel.getGroupOrg() != null && webModel.getGroupOrg().getId() > 0) {
			p = p.and(q.groupOrg.id.eq(webModel.getGroupOrg().getId()));
		}
		
		if(webModel.getMainOrg() != null) {
			p = p.and(q.mainOrg.id.eq(webModel.getMainOrg().getId()));
		}
		
		
		
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.DESC, "id");
		


		Page<Quotation> quotations = quotationRepo.findAll(p, pageRequest);
		
		ResponseJSend<Page<Quotation>> response = new ResponseJSend<Page<Quotation>>();
		response.data=quotations;
		response.status=ResponseStatus.SUCCESS;
		return response;
	}

	
	
	
	@Override
	public ResponseJSend<RequestTracker> findReqeustTracker(Long reqId) {
		ResponseJSend<RequestTracker> response = new ResponseJSend<RequestTracker>();
		Request req = this.requestRepo.findOne(reqId);
		
		RequestTracker track = new RequestTracker(req);
		
		Hibernate.initialize(req.getHistories());
		for(Report report : req.getReports()) {
			Hibernate.initialize(report.getHistories());
		}
		
		track.setRequestHisotries(req.getHistories());
		
		response.data = track;
		response.status = ResponseStatus.SUCCESS;
		response.message = "found Request id " + reqId;
		
		return response;
	}

	@Override
	public ResponseJSend<RequestTracker> findReqeustTracker(Long reqId, String trackingCode) {
		ResponseJSend<RequestTracker> response = new ResponseJSend<RequestTracker>();
		Request req = this.requestRepo.findOne(reqId);
		if(!req.getTrackingCode().equals(trackingCode)) {
			response.data = null;
			response.status = ResponseStatus.FAIL;
			response.message = "Request id " + reqId + " and traking code " + trackingCode + " not found!";
			
			
		} else {
			RequestTracker track = new RequestTracker(req);
			response.data = track;
			response.status = ResponseStatus.SUCCESS;
			response.message = "found Request id " + reqId + " and traking code " + trackingCode;
		}
		
		
		return response;
	}
	
	

	@Override
	public ResponseJSend<Page<QuotationTemplate>> findQuotationTemplateActiveByField(JsonNode node,
			Integer pageNumber) throws JsonMappingException {
		
		((ObjectNode) node).put("isActive", true);
		
		logger.debug("node.get(\"isActive\"): " + node.get("isActive").asBoolean()); 
		
		return findQuotationTemplateByField(node, pageNumber);
	}

	@Override
	public ResponseJSend<Page<QuotationTemplate>> findQuotationTemplateByField(
			JsonNode node, Integer pageNumber) throws JsonMappingException {
		
		
		QQuotationTemplate q = QQuotationTemplate.quotationTemplate;
		BooleanBuilder p = new BooleanBuilder();
		
		ObjectMapper mapper = getObjectMapper();
		
		QuotationTemplate webModel;
		try {
			webModel = mapper.treeToValue(node, QuotationTemplate.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new JsonMappingException(e.getMessage() + "\n  JSON: " + node.toString());
		}
		
		
		logger.debug("findQuotationTemplateByExample");
		
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "code");
		
		if(webModel.getCode() != null && webModel.getCode().length() > 0) {
			p = p.and(q.code.containsIgnoreCase(webModel.getCode().trim()));
			logger.debug("%" + webModel.getCode().trim() + "%");
		}
		
		if(webModel.getName() != null && webModel.getName().length() > 0) {
			p = p.and(q.name.containsIgnoreCase(webModel.getName().trim()));
			logger.debug("%" + webModel.getName().trim() + "%");
		}
		
		if(webModel.getMainOrg() != null) {
			p = p.and(q.mainOrg.id.eq(webModel.getMainOrg().getId()));
		}
		
		if(webModel.getSampleType() != null && webModel.getSampleType().getId() > 0) {
			p = p.and(q.sampleType.id.eq(webModel.getSampleType().getId()));
		}
		
		if(webModel.getGroupOrg() != null && webModel.getGroupOrg().getId() > 0) {
			p = p.and(q.groupOrg.id.eq(webModel.getGroupOrg().getId()));
		}
		
		if(webModel.getIsActive()==null) {
			webModel.setIsActive(true);
		}
		
		p = p.and(q.isActive.eq(webModel.getIsActive()));
		
		Page<QuotationTemplate> templates = quotationTemplateRepo.findAll(p, pageRequest);
		
		ResponseJSend<Page<QuotationTemplate>> response = new ResponseJSend<Page<QuotationTemplate>>();
		response.data=templates;
		response.status=ResponseStatus.SUCCESS;
		return response;
	}

	@Override
	public Company findCompanyById(Long id) {
		Company c =  companyRepo.findOne(id);
		
		c.getPeople().size();
		c.getAddresses().size();
		
		return c;
	}

	@Override
	public List<Address> findAddressOfId(Long id) {
		return companyRepo.findAddressesOfId(id);
	}

	@Override
	public ResponseJSend<Page<Company>> searchCompanyByName(
			String nameQuery, Integer pageNumber) {
		nameQuery = "%"+nameQuery+"%";
		
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "nameTh");
		
		Page<Company> companies = companyRepo.findAllByNameLike(nameQuery, pageRequest);
		
		ResponseJSend<Page<Company>> response = new ResponseJSend<Page<Company>>();
		response.data=companies;
		response.status=ResponseStatus.SUCCESS;
		return response;
	}

	@Override
	public List<Province> findProvinces() {
		return addressRepo.findProvinces();
	}

	@Override
	public List<District> findDistrictsOfProvince(Long id) {
		return addressRepo.findDistrictsOfProvince(id);
	}

	@Override
	public ResponseJSend<Long> saveCompany(JsonNode node) {
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		Company company;
		if(node.get("id")==null) {
			company = new Company();
			companyRepo.save(company);
		} else {
			company = companyRepo.findOne(node.get("id").asLong());
		}
		
		if(node.get("nameTh") != null) {
			company.setNameTh(node.get("nameTh").asText());
		}
		if(node.get("nameEn") != null) {
			company.setNameEn(node.get("nameEn").asText());
		}
		
		if(node.get("addresses") != null) {
			Set<Address> addressSet = new HashSet<Address>();
			for(JsonNode addressNode : node.get("addresses")) {
				Address address = null;
				if(addressNode.get("id") != null) {
					address = addressRepo.findOne(addressNode.get("id").asLong());
				}
				if(address == null) { 
					address = new Address();
				}
				
				if(addressNode.get("title") !=null) address.setTitle(addressNode.get("title").asText());
				if(addressNode.get("line1") !=null) address.setLine1(addressNode.get("line1").asText());
				if(addressNode.get("line2") !=null) address.setLine2(addressNode.get("line2").asText());
				if(addressNode.get("phone") !=null) address.setPhone(addressNode.get("phone").asText());
				if(addressNode.get("mobilePhone") !=null) address.setMobilePhone(addressNode.get("mobilePhone").asText());
				if(addressNode.get("zipCode") !=null) address.setZipCode(addressNode.get("zipCode").asText());
				if(addressNode.get("fax") !=null) address.setFax(addressNode.get("fax").asText());

				// now province and district
				if(addressNode.get("province") != null) {
					Province province = addressRepo.findProvinceById(addressNode.get("province").get("id").asLong());
					address.setProvince(province);
				}
				if(addressNode.get("district") != null) {
					District district = addressRepo.findDistrictById(addressNode.get("district").get("id").asLong());
					address.setDistrict(district);
				}
				address.setAddressType('G');
				
				addressRepo.save(address);
				addressSet.add(address);
				
			}
			if(company.getPeople() == null) {
				company.setAddresses(addressSet);
			} else {
				Set<Address> oldSet = company.getAddresses();
				company.setAddresses(addressSet);

				// now we find the different between two set?
				oldSet.removeAll(addressSet);
				
				addressRepo.delete(oldSet);
			}
		}
		
		if(node.get("people") != null) {
			Set<Customer> customerSet = new HashSet<Customer>();
			for(JsonNode personNode : node.get("people")) {
				Customer customer = null;
				if(personNode.get("id") != null) {
					customer = customerRepo.findOne(personNode.get("id").asLong());
				}
				if(customer == null) { 
					customer = new Customer();
				}
				
				if(personNode.get("firstName") !=null) customer.setFirstName(personNode.get("firstName").asText());
				if(personNode.get("lastName") !=null) customer.setLastName(personNode.get("lastName").asText());
				if(personNode.get("email") !=null) customer.setEmail(personNode.get("email").asText());
				if(personNode.get("mobilePhone") !=null) customer.setMobilePhone(personNode.get("mobilePhone").asText());
				if(personNode.get("officePhone") !=null) customer.setOfficePhone(personNode.get("officePhone").asText());
				if(personNode.get("fax") !=null) customer.setFax(personNode.get("fax").asText());
				
				customer.setCompany(company);
				customerRepo.save(customer);
				customerSet.add(customer);
				
			}
			if(company.getPeople() == null) {
				company.setPeople(customerSet);
			} else {
				Set<Customer> oldSet = company.getPeople();
				company.setPeople(customerSet);

				// now we find the different between two set?
				oldSet.removeAll(customerSet);
				
				for(Customer c : oldSet) {
					c.setCompany(null);
				}
				customerRepo.delete(oldSet);
			}
		}
		
		company= companyRepo.save(company);
		response.status = ResponseStatus.SUCCESS;
		response.data = company.getId(); 
		return response;
	}

	
	
	@Override
	public Quotation findQuotationByQuotationNo(String quotationNo) {
		QQuotation quoation  = QQuotation.quotation;
		
				
		Quotation q =  quotationRepo.findOne(quoation.quotationNo.eq(quotationNo));
		
		if(q != null) {
			logger.debug("q.getPromotions().size(): " + q.getPromotions().size());
			for(PromotionDiscount pd: q.getPromotions()) {
				pd.getPromotion().getDescription();
				logger.debug(pd.getPromotion().getDescription() + " : " + pd.getDiscount());
			}
		}
		
		return q;
	}

	@Override
	public Quotation findQuotation(Long id) {
		
		Quotation q =  quotationRepo.findOne(id);
		logger.debug("q.getPromotions().size(): " + q.getPromotions().size());
		for(PromotionDiscount pd: q.getPromotions()) {
			pd.getPromotion().getDescription();
			logger.debug(pd.getPromotion().getDescription() + " : " + pd.getDiscount());
		}
		
		return q;
	}

	@Override
	public QuotationTemplate findQuotationTemplate(Long id) {
		return quotationTemplateRepo.findOne(id);
	}

	@Override
	public SampleType findSampleType(Long id) {
		return sampleTypeRepo.findOne(id);
	}

	@Override
	public Iterable<SampleType> findAllSampleType() {
		QSampleType q = QSampleType.sampleType;
		
		return sampleTypeRepo.findAll(q.feeTypeId.eq(5000));
	}

	@Override
	public ResponseJSend<Long> savePromotion(JsonNode node, SecurityUser user) {
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		
		Promotion promotion;
		if(node.path("id").asLong() == 0) {
			promotion = new Promotion();
		} else {
			promotion = findPromotion(node.path("id").asLong());
			
		}
		
		
		promotionRepo.save(promotion);
		response.status = ResponseStatus.SUCCESS;
		response.data = promotion.getId(); 
		return response;
	}

	@Override
	public Promotion findPromotion(Long id) {
		return promotionRepo.findOne(id);
	}

	@Override
	public ResponseJSend<Page<Promotion>> findPromotionByField(JsonNode node, Integer pageNumber) {

	ResponseJSend<Page<Promotion>> response = new ResponseJSend<Page<Promotion>>();
		
		
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "startDate");
		
		QPromotion promotion = QPromotion.promotion;
		
		BooleanBuilder p = new BooleanBuilder();
		
		if(node.path("description").asText()!=null || node.path("").asText().length() > 0) {
			p = p.and(promotion.description.containsIgnoreCase(node.path("description").asText()));
		}
		
		Page<Promotion> promotions = promotionRepo.findAll(p, pageRequest);
		
		response.data=promotions;
		response.status=ResponseStatus.SUCCESS;
		return response;
	}

	@Override
	public Iterable<Promotion> findPromotionCurrent() {
		QPromotion promotion = QPromotion.promotion;
		BooleanBuilder p = new BooleanBuilder();
		Date today = new Date();
		
		p = p.and(promotion.startDate.before(today).and(promotion.endDate.after(today)));
		Iterable<Promotion> promotions = promotionRepo.findAll(p);
		return promotions;
	}

	
	
	@Override
	public Request saveRequest(Request request) {
		requestRepo.save(request);
		return request;
	}

	@Override
	public ResponseJSend<Request> saveRequest(JsonNode node, SecurityUser user) {
		ResponseJSend<Request> response = new ResponseJSend<Request>();
		
		Request request;
		if(node.get("id") == null ) {
			logger.debug("new Reqeust");
			request = new Request();
			request.setStatus(RequestStatus.NEW_REQ);
			request.setType(0);
			request.setCreatedBy(user.getDssUser().getOfficer());
			request.setCreatedTime(new Date());
			request.setLastUpdatedBy(request.getCreatedBy());
			request.setCreatedByOrg(user.getDssUser().getOfficer().getWorkAt());
			request.setLastUpdatedTime(request.getCreatedTime());
			
			request.setReceivedDate(request.getCreatedTime());
			
			// now Generate ReqNo
			request.setReqNo(this.getLabNo());
			
			//now generate tracking code
			char[] alphNum = "0123456789".toCharArray();

			Random rnd = new Random(System.currentTimeMillis());

			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < 8; j++)
			    sb.append(alphNum[rnd.nextInt(alphNum.length)]);

			String trackingCode = sb.toString();
			
			request.setTrackingCode(trackingCode);
			
			
		} else {
			request = requestRepo.findOne(node.get("id").asLong());
			
			request.setLastUpdatedBy(user.getDssUser().getOfficer());
			request.setLastUpdatedTime(new Date());
			
		}
		
		if(node.get("company") == null ||
				node.path("company").get("id") == null) {
			response.status = ResponseStatus.ERROR;
			response.message = "COMPANY_IS_NULL";
			response.data = null;
			return response;
		}
		
		Company company = companyRepo.findOne(node.get("company").get("id").asLong());
		request.setCompany(company);
		request.setCompanyName(company.getNameTh());
		
		Customer customer = customerRepo.findOne(node.path("contact").path("id").asLong());
		if(customer != null) {
			request.setCustomer(customer);
			request.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
		}
		
		
		
		if(node.path("address").isNull()) {
			// new Request
			Address address = addressRepo.findOne(node.path("addressCompanyAddress").path("id").asLong());
			request.setAddress(RequestAddress.parseAddress(address));
			if(node.path("addressTitle").isNull()) {
				request.setAddressTitle(node.path("addressTitle").asText());
			} else {
				request.setAddressTitle(node.path("company").path("nameTh").asText());
			}
		} else {
			RequestAddress labAddress =  requestAddressRepo.findOne(node.path("address").path("id").asLong());
			if(labAddress == null) {
				labAddress = new RequestAddress();
			}
			labAddress.importFromJson(node.path("address"));
			request.setAddressTitle(node.path("addressTitle").asText());
		} 
		
		if(node.path("invoiceAddress").isNull()) {
			RequestAddress invoiceAddress = new RequestAddress();
			if(node.path("invoiceAddressCompanyAddress").isNull()) {
				invoiceAddress.importFromAddressJson(node.path("addressCompanyAddress"));
			} else {
				invoiceAddress.importFromAddressJson(node.path("invoiceAddressCompanyAddress")); 
			}
			
			request.setInvoiceAddress(invoiceAddress);
		
			String title = node.path("invoiceAddressCompanyAddress").path("title").asText();
			
			if(title != null &&  (title.equals("null") == false)) {
				request.setInvoiceTitle(title);
			} else {
				request.setInvoiceTitle(node.path("company").path("nameTh").asText());
			}
			
		} else {
			RequestAddress invoiceAddress = requestAddressRepo.findOne(node.path("invoiceAddress").path("id").asLong());
			invoiceAddress.importFromJson(node.path("invoiceAddress"));
			request.setInvoiceTitle(node.path("invoiceTitle").asText());
		}
		
		if(node.path("reportAddress").isNull()) {
			RequestAddress reportAddress = new RequestAddress();
			if(node.path("reportAddressCompanyAddress").isNull()) {
				reportAddress.importFromAddressJson(node.path("addressCompanyAddress"));
			} else {
				reportAddress.importFromAddressJson(node.path("reportAddressCompanyAddress")); 
			}
			
			request.setReportAddress(reportAddress);
			
			String title = node.path("reportAddressCompanyAddress").path("title").asText();
			
			if(title != null &&  (title.equals("null") == false)) {
				request.setReportTitle(title);
			} else {
				request.setReportTitle(node.path("company").path("nameTh").asText());
			}
			
		} else {
			RequestAddress reportAddress = requestAddressRepo.findOne(node.path("reportAddress").path("id").asLong());
			reportAddress.importFromJson(node.path("reportAddress"));
			request.setReportTitle(node.path("reportTitle").asText());
		}
		
		
		if(node.path("estimatedWorkingDay").asInt() <= 0) {
			request.setEstimatedWorkingDay(null);
		} else {
			request.setEstimatedWorkingDay(node.path("estimatedWorkingDay").asInt());	
		}
		
		if(node.path("quotation").get("id") != null) {
			Quotation quotation = quotationRepo.findOne(node.path("quotation").get("id").asLong());
			request.setQuotation(quotation);	
		}
		
		
		
		request.setDeliveryMethod(
				ReportDeliveryMethod.valueOf(
						node.path("deliveryMethod").asText()));
		
		request.setReportLanguage(
				ReportLanguage.valueOf(
						node.path("reportLanguage").asText()));
		
		request.setSpeed(
				JobPriority.valueOf(
						node.path("speed").asText()));
		
		if(node.path("sampleType").get("id") != null) {
			SampleType sampleType = sampleTypeRepo.findOne(node.path("sampleType").get("id").asLong());
			request.setSampleType(sampleType);
		}
		
		request.setTranslatedReport(node.path("translatedReport").asBoolean());
		request.setSeparatedReportForSample(node.path("separatedReportForSample").asBoolean());
		
		
		if(node.path("sampleReceiverOrg").get("id") != null) {
			Organization sampleReceiverOrg = organizationRepo.findOne(
					node.path("sampleReceiverOrg").get("id").asLong());
			request.setSampleReceiverOrg(sampleReceiverOrg);
		}
		
		
		if(node.path("mainOrg").path("id") != null) {
			Organization mainOrg = organizationRepo.findOne(node.path("mainOrg").path("id").asLong());
			request.setMainOrg(mainOrg);
		}
		
		request.setStatus(RequestStatus.GERNERATE_REQNO);
		try {
			request.setReqDate(jsonDateFormat.parse(node.path("reqDate").asText()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setReqDate(new Date());
		}
		
		
		if(request.getHistories() == null) {
			request.setHistories(new ArrayList<RequestHistory>());
		}
		
		RequestHistory history = new RequestHistory();
		history.setCreatedBy(user.getDssUser().getOfficer());
		history.setTimestamp(request.getLastUpdatedTime());
		if(request.getId() == null) {
			history.setHistory(RequestStatus.GERNERATE_REQNO.getHistoryString());
		} else {
			history.setHistory("แก้ไขใบคำร้อง");
		}
		history.setRequest(request);
		
		List<Invoice> invoices = request.getInvoices();
		if(invoices == null) {
			invoices = new ArrayList<Invoice>();
			request.setInvoices(invoices);
		}
		
		logger.debug("invoices: " + node.get("invoices").toString());
		
		for(JsonNode invoiceNode : node.get("invoices")){
			Boolean newInvoice = false;
			Invoice invoice;
			if(invoiceNode.get("id") == null) {
				invoice = new Invoice();
				newInvoice = true;
			} else {
				invoice = invoiceRepo.findOne(invoiceNode.get("id").asLong());
			}
			
			Invoice jsonInvoice;
			try {
				jsonInvoice = getObjectMapper().treeToValue(invoiceNode, Invoice.class);
				BeanUtils.copyProperties(jsonInvoice, invoice);
				
				
				
				
				invoice.setRequest(request);
				
				logger.debug("saving invoice...");
				
				invoiceRepo.save(invoice);
				
				if(newInvoice) {
					invoices.add(invoice);
				}
				
				//invoices.add(invoice);
			} catch (JsonProcessingException e) {
				logger.debug("invoice did not save! ");
				e.printStackTrace();
			}
			
		} 
		
		
		
		// there will only be one invoice
		
		
		
//		logger.debug("saving invoice...");
//		
//		List<Invoice> invoices = new ArrayList<Invoice>();
//		for(JsonNode invoiceNode : node.get("invoices")){
//			Invoice invoice;
//			if(invoiceNode.get("id") == null) {
//				invoice = new Invoice();
//			} else {
//				invoice = invoiceRepo.findOne(invoiceNode.get("id").asLong());
//			}
//			
//			Invoice jsonInvoice;
//			try {
//				jsonInvoice = getObjectMapper().treeToValue(invoiceNode, Invoice.class);
//				BeanUtils.copyProperties(invoice, jsonInvoice);
//				
//				invoice.setRequest(request);
//				invoiceRepo.save(invoice);
//				
//				invoices.add(invoice);
//			} catch (JsonProcessingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		} 
//		request.setInvoices(invoices);
//		logger.debug("saving promotion...");
//		List<RequestPromotionDiscount> promotions = new ArrayList<RequestPromotionDiscount>();
//		for(JsonNode promotionNode : node.get("promotions")) {
//			RequestPromotionDiscount pd;
//			if(promotionNode.get("id") == null) {
//				pd = new RequestPromotionDiscount();
//			} else {
//				pd = requestPromotionDiscountRepo.findOne(
//						promotionNode.get("id").asLong());
//			}
//			
//			Promotion promotion = promotionRepo.findOne(
//					promotionNode.get("promotion").get("id").asLong()); 
//			
//			pd.setDiscount(promotionNode.path("discount").asInt());
//			pd.setRequest(request);
//			pd.setPromotion(promotion);
//				
//			
//			
//			promotions.add(pd);
//		}
//		requestPromotionDiscountRepo.save(promotions);
//		request.setPromotions(promotions);
		
		logger.debug("saving samples...");
		List<RequestSample> newSamples = new ArrayList<RequestSample>();
		int sampleNum = 0;
		for(JsonNode sampleNode : node.get("samples")){
			logger.debug(sampleNode.textValue());
			sampleNum = sampleNum+1;
			RequestSample sample;
			if(sampleNode.get("id") == null) {
				sample = new RequestSample12();
			} else {
				sample = requestSampleRepo.findOne(sampleNode.get("id").asLong());
			}
			
			sample.setBrand(sampleNode.path("brand").asText());
			sample.setItem(sampleNode.path("item").asInt());
			sample.setName(sampleNode.path("name").asText());
			sample.setLabNo(request.getReqNo()+"."+sampleNum);
			sample.setRequest(request);
			
			// probably 0=not sending to lab
			sample.setSendStatus(0);
			
			List<LabJob> newJobs = new ArrayList<LabJob>();
			
			for(JsonNode jobNode : sampleNode.get("jobs")) {
				LabJob job;
				if(jobNode.get("id") == null) {
					job = new LabJob();
				} else {
					job = labJobRepo.findOne(jobNode.get("id").asLong());
				}
				
				job.setActive(true);
				job.setStatus(LabJobStatus.NEW_JOB);
				if(jobNode.get("testMethod") != null) {
					TestMethod method = testMethodRepo.findOne(
							jobNode.get("testMethod").get("id").asLong());
					
					job.setTestMethod(method);
					job.setQuantity(jobNode.path("quantity").asInt());
					job.setFee(method.getFee().intValue() * job.getQuantity());
				}
				
				job.setSample(sample);
				job.setOrg(request.getMainOrg());
				newJobs.add(job);
				
			}
			labJobRepo.save(newJobs);
			if(sample.getJobs() == null) {
				sample.setJobs(newJobs);
			} else {
				sample.getJobs().clear();
				sample.getJobs().addAll(newJobs);
			}
			
			
			newSamples.add(sample);
		}
		requestSampleRepo.save(newSamples);
		
		if(request.getSamples() == null) {
			request.setSamples(newSamples);
		} else {
			request.getSamples().clear();
			request.getSamples().addAll(newSamples);
			
		}
		
		
//		logger.debug("request.getAddress().getId(): " + request.getAddress().getId());
//		logger.debug("request.getReportAddress().getId(): " + request.getReportAddress().getId());
//		logger.debug("request.getInvoiceAddress().getId(): " + request.getInvoiceAddress().getId());
		
		request.getHistories().add(history);
		logger.debug("request.getID(): " + request.getId()); 
		
		logger.debug("About to save Request....");
		for(RequestHistory his : request.getHistories()) {
			logger.debug(his.getHistory() + " / his.req.ID: " + his.getRequest().getId() +
					" / his.getCreatedBy(): " + his.getCreatedBy().getFirstName() + " / timestamp: " + his.getTimestamp());
		}
		
		requestRepo.save(request);
		
		// now save to FIN_INVOICE
		generateFinInvoice(request);

		response.status = ResponseStatus.SUCCESS;
		response.data = request;
		return response;
	}

	private void generateFinInvoice(Request req) {
		// now see if we need to cancel the old one
		
		
		// then we create a new One
		
		Date createdDate = new Date();
		String createdBy = req.getCreatedBy().getDssUser().getUsername();
		
		InvoiceAddress invoiceAddress = new InvoiceAddress();
		invoiceAddress.setCustomerName(req.getInvoiceTitle());
		if(req.getInvoiceAddress() != null) {
			invoiceAddress.setAddress1(req.getInvoiceAddress().getAddress());
		} else {
			invoiceAddress.setAddress1(null);
		}
//		
		invoiceAddress.setCustomerCode(req.getCompany().getId().toString());
		invoiceAddress.setCreatedBy(req.getCreatedBy().getDssUser().getId());
		invoiceAddress.setCreatedDate(createdDate);
		invoiceAddress.setLastUpdatedDate(createdDate);
		invoiceAddress.setUpdatedBy(req.getCreatedBy().getDssUser().getId());
		invoiceAddress.setLanguage(InvoiceLanguage.TH);
		
		
		Province invoiceProvince = addressRepo.findProvinceByName(req.getInvoiceAddress().getProvince());
		
		District invoiceDistrict = addressRepo.findDistrictByName(req.getInvoiceAddress().getAmphur(), invoiceProvince);
		
		invoiceAddress.setDistrict(invoiceDistrict);
		invoiceAddress.setProvince(invoiceProvince);
		invoiceAddress.setCustomerType(1);
		invoiceAddress.setZipCode(req.getInvoiceAddress().getZipCode());
		invoiceAddress.setPhone(req.getInvoiceAddress().getPhone());
		invoiceAddress.setFax(req.getInvoiceAddress().getFax());
		
		
		
		tcs3.model.fin.Invoice finInvoice = new tcs3.model.fin.Invoice();
		finInvoice.setInvoiceAddress(invoiceAddress);
		finInvoice.setHasDetail(true);
		finInvoice.setCreatedDate(createdDate);
		finInvoice.setCreatedBy(createdBy);
		finInvoice.setInvoiceNo(
				req.getReqNo().substring(1, 3) + 
				"001" +
				req.getReqNo().substring(4, 9));
		finInvoice.setRemark("ค่าธรรมเนียมบริการทดสอบ สอบเทียบตามใบคำร้องที่ " + req.getReqNo());
		finInvoice.setTotal(req.getTotalFee());
		finInvoice.setQuantity(req.getTotalNumberOfReqExample());
		finInvoice.setRefCode(req.getReqNo());
		finInvoice.setDetails(new ArrayList<InvoiceDetail>());
		
		InvoiceDetail detail01 = new InvoiceDetail();
		detail01.setInvoice(finInvoice);
		detail01.setCreatedBy(createdBy);
		detail01.setCreatedDate(createdDate);
		detail01.setTypeCode("670");
		detail01.setDetailCode("67012");
		detail01.setDetailDescription("ค่าธรรมเนียมบริการทดสอบ สอบเทียบตามใบคำร้องที่ " + req.getReqNo());
		detail01.setQuantity(req.getTotalNumberOfReqExample());
		detail01.setAmount(req.getTotalReqExampleFee());
		
		finInvoice.getDetails().add(detail01);
		if(req.getTranslatedNumber() > 0) {
			InvoiceDetail detail02 = new InvoiceDetail();
			detail02.setInvoice(finInvoice);
			detail02.setCreatedBy(createdBy);
			detail02.setCreatedDate(createdDate);
			detail02.setTypeCode("830");
			detail02.setDetailCode("8301");
			detail02.setDetailDescription("ค่าธรรมเนียมการแปล");
			detail02.setQuantity(req.getTranslatedNumber());
			detail02.setAmount(req.getTranlatedFee());
			
			finInvoice.getDetails().add(detail02);
		}
		
		if(req.getCopyNumber() > 0) {
			InvoiceDetail detail03 = new InvoiceDetail();
			detail03.setInvoice(finInvoice);
			detail03.setCreatedBy(createdBy);
			detail03.setCreatedDate(createdDate);
			detail03.setTypeCode("830");
			detail03.setDetailCode("8301");
			detail03.setDetailDescription("ค่าธรรมเนียมการทำสำเนา");
			detail03.setQuantity(req.getCopyNumber());
			detail03.setAmount(req.getCopyFee());
			
			finInvoice.getDetails().add(detail03);
		}
		
		if(req.getCoaNumber() > 0 )  {
			InvoiceDetail detail04 = new InvoiceDetail();
			detail04.setInvoice(finInvoice);
			detail04.setCreatedBy(createdBy);
			detail04.setCreatedDate(createdDate);
			detail04.setTypeCode("830");
			detail04.setDetailCode("8301");
			detail04.setDetailDescription("ค่าธรรมเนียมการรออกใบรับรอง COA");
			detail04.setQuantity(req.getCoaNumber());
			detail04.setAmount(req.getCoaFee());
			
			finInvoice.getDetails().add(detail04);
		}
		
		if(req.getEtcFee() > 0.0 ) {
			InvoiceDetail detail05 = new InvoiceDetail();
			detail05.setInvoice(finInvoice);
			detail05.setCreatedBy(createdBy);
			detail05.setCreatedDate(createdDate);
			detail05.setTypeCode("670");
			detail05.setDetailCode("67012");
			detail05.setDetailDescription("ค่าธรรมเนียมอื่นๆ: " + req.getEtcFeeString());
			detail05.setQuantity(0);
			detail05.setAmount(req.getEtcFee());
			
			finInvoice.getDetails().add(detail05);
		}
		
		// now save
		finInvoiceRepo.save(finInvoice);
			
	}
	
	
	@Override
	public Request findRequest(Long id) {
		Request req =requestRepo.findOne(id);
		
		logger.debug("finding all samples");
		
		if(req != null) {
			logger.debug("req.getReqDate(): " + req.getReqDate());
			logger.debug("req.getReceivedDate(): " + req.getReceivedDate());
		}
			
			
//		QRequestSample requestSample = QRequestSample.requestSample;
//	 	Iterable<RequestSample> reqSamples = requestSampleRepo.findAll(requestSample.request.id.eq(req.getId()), requestSample.id.asc() );
//		
//	 	List<RequestSample> samples = new ArrayList<RequestSample>();
//	 	
//		for(RequestSample sample : reqSamples ) {
//			samples.add(sample);
//			logger.debug("sample id: " + sample.getId());
//			sample.getJobs().size();
//		}
//		req.setSamples(samples);
		
		for(RequestSample sample : req.getSamples() ) {
			logger.debug("sample.id(): "  +sample.getId() );
			logger.debug("  -- has job: "  +sample.getJobs().size() );
			
			for(LabJob job: sample.getJobs()) {
				logger.debug("job.id(): " + job.getId()); 
				Hibernate.initialize(job.getTestMethod());
			}
		}
		
		// load invoice
		req.getInvoices().size();
		
		// load receiver Org
		req.getSampleReceiverOrg().getId();
	
		return req;
	}

	
	
	
	
	@Override
	public ResponseJSend<Page<Request>> findRequestOverdue(JsonNode node, Integer pageNumber) {
		ResponseJSend<Page<Request>> response = new ResponseJSend<Page<Request>>();
		
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "estimatedReportDate");
		
		QRequest request = QRequest.request;
		QReport report = QReport.report;
		
		BooleanBuilder p = new BooleanBuilder();
		
		Date today = new Date();
		
		p = p.and(request.estimatedReportDate.before(today))
				.andNot(
						//request.status.ne(RequestStatus.FINISH)
						//.or (
								(		request.reports.any().status.eq(ReportStatus.HEAD_ARCHIVE_SIGN)
										.or(request.reports.any().status.eq(ReportStatus.SENTED))
										.or(request.reports.any().status.eq(ReportStatus.ADD_REPORT_NO)) )
						
						
						//in(
						//		JPAExpressions.selectFrom(report).where(report.parentReport.isNotNull().and(report.status.ne(ReportStatus.HEAD_ARCHIVE_SIGN))).select(report.request))
								
						//)
						//( request.status.eq(RequestStatus.REPORT).and(request.reports.any().status.ne(ReportStatus.HEAD_ARCHIVE_SIGN)) )
					)
				.and(request.reqNo.goe("L62"));
		
		if(node.path("mainOrg").has("id") && node.path("mainOrg").path("id").asLong() > 0) {
			logger.debug("node.path('mainOrg').path('id').asLong():" + node.path("mainOrg").path("id").asLong());
			p = p.and(request.mainOrg.id.eq(node.path("mainOrg").path("id").asLong()));
		}
		
		if(node.path("groupOrg").has("id") && node.path("groupOrg").path("id").asLong() > 0 ) {
			logger.debug("node.path('groupOrg').path('id').asLong(): " + node.path("groupOrg").path("id").asLong());
			p = p.and(request.groupOrg.id.eq(node.path("groupOrg").path("id").asLong()));
		}
		
		
		Page<Request> requests = requestRepo.findAll(p, pageRequest);
		
		for(Request req: requests.getContent()) {
			Hibernate.initialize(req.getSamples());
		}
		response.data=requests;
		response.status=ResponseStatus.SUCCESS;
		
		return response;
	}

	@Override
	public ResponseJSend<Page<Request>> findRequestByField(JsonNode node, Integer pageNumber) {

		ResponseJSend<Page<Request>> response = new ResponseJSend<Page<Request>>();
		
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.DESC, "id");
		
		QRequest request = QRequest.request;
		
		String companyName = "";
		String reqNo = "";
		
		BooleanBuilder p = new BooleanBuilder();
		
		if(node.path("companyName").asText()!=null && node.path("companyName").asText().length() > 0) {
			companyName =  node.path("companyName").asText();
			logger.debug("node.path('companyName').asText(): " + node.path("companyName").asText());
			p = p.and(request.companyName.containsIgnoreCase(node.path("companyName").asText()));
		}
		
		if(node.path("reqNo").asText()!=null && node.path("reqNo").asText().length() > 0) {
			reqNo = node.path("reqNo").asText();
			logger.debug("node.path('reqNo').asText(): " + node.path("reqNo").asText());
			p = p.and(request.reqNo.containsIgnoreCase(node.path("reqNo").asText()));
		}
		
		if(node.path("sampleType").has("id") && node.path("sampleType").path("id").asLong() > 0) {
			logger.debug("node.path('sampleType').path('id').asLong():" + node.path("sampleType").path("id").asLong());
			p = p.and(request.sampleType.id.eq(node.path("sampleType").path("id").asLong()));
		}
		
		if(node.path("mainOrg").has("id") && node.path("mainOrg").path("id").asLong() > 0) {
			logger.debug("node.path('mainOrg').path('id').asLong():" + node.path("mainOrg").path("id").asLong());
			p = p.and(request.mainOrg.id.eq(node.path("mainOrg").path("id").asLong()));
		}
		
		if(node.path("groupOrg").has("id") && node.path("groupOrg").path("id").asLong() > 0 ) {
			logger.debug("node.path('groupOrg').path('id').asLong(): " + node.path("groupOrg").path("id").asLong());
			p = p.and(request.groupOrg.id.eq(node.path("groupOrg").path("id").asLong()));
		}
		
		logger.debug("about to search for Request...");
		
		Page<Request> requests = requestRepo.findAll(p, pageRequest);
		
		//Page<Request> requests = requestRepo.findByReqNoOrCompanyName(reqNo, companyName, pageRequest);
		
//		Set<Long> reqIds = new HashSet<Long>();
		for(Request req: requests.getContent()) {
			Hibernate.initialize(req.getSamples());
		}
//		
//		QRequestSample reqSample = QRequestSample.requestSample;
//		requestSampleRepo.findAll(reqSample.request.id.in(reqIds));
//		
		logger.debug("result return: " + requests.getNumberOfElements());
		
		response.data=requests;
		response.status=ResponseStatus.SUCCESS;
		
		
		return response;
	}
	
	private String getLabNo() {
		QLabNoSequence labNoSequence = QLabNoSequence.labNoSequence;
		LabNoSequence seq = labNoSequenceRepository.findOne(labNoSequence.name.eq("seq_req_no"));
		Integer maxNumber = 1;
		
		String currentYear = yearDateFormat.format(new Date());
		Integer currentYearInt = Integer.parseInt(currentYear);
		
		logger.debug("currentYear: " + currentYear);
		logger.debug("seq.getYear: " + seq.getYear());
		
		if(!currentYearInt.equals(seq.getYear())) {
			seq.setYear(currentYearInt);
			
		} else {
			maxNumber = seq.getMaxNumber() +1; 
			logger.debug("maxNumber: " + maxNumber);
		}
		
		seq.setMaxNumber(maxNumber);
		
		labNoSequenceRepository.save(seq);
		
		return String.format("L%s/%05d", currentYear.substring(2), maxNumber);
		
	}

	@Override
	public ResponseJSend<RequestAddress> updateRequestAddressOfRequest(Long id, Long requestAddressId, JsonNode node) {
		ResponseJSend<RequestAddress> response = new ResponseJSend<RequestAddress>();
		
		
		Request req = requestRepo.findOne(id);
		RequestAddress address = null;
		if(req == null)  {
			response.status = ResponseStatus.ERROR;
			response.message = "Request with id: "+ id + " cannot be found!";
		} else {
		
		
			if(req.getAddress().getId().equals(requestAddressId) || 
					req.getReportAddress().getId().equals(requestAddressId) ||
					req.getInvoiceAddress().getId().equals(requestAddressId) ) {
				
				response.status = ResponseStatus.SUCCESS;
				
				address = requestAddressRepo.findOne(requestAddressId);
				address.setAddress(node.path("address").asText());
				address.setAmphur(node.path("amphur").asText());
				address.setProvince(node.path("province").asText());
				address.setCountry(node.path("country").asText());
				address.setFax(node.path("fax").asText());
				address.setZipCode(node.path("zipCode").asText());
				address.setPhone(node.path("phone").asText());
				
				if(req.getAddress().getId().equals(requestAddressId)) {
					req.setAddressTitle(node.path("title").asText());
					
					logger.debug(node.path("title").asText());
					
				} else if(req.getReportAddress().getId().equals(requestAddressId) ) {
					req.setReportTitle(node.path("title").asText());
					
				} else if(req.getInvoiceAddress().getId().equals(requestAddressId) ) {
					req.setInvoiceTitle(node.path("title").asText());
					
				}
				
				// now save req;
				requestRepo.save(req);
				requestAddressRepo.save(address);
				
				
			} else {
				// requestAddress Id can't be found with this reqId
				response.status = ResponseStatus.ERROR;
				response.message = "RequestAddress with id: "+ requestAddressId + " cannot be found within Request Id: "+ id+ " !";
			}
			
		}
		
		response.data = address;
		
		return response;
	}

	@Override
	public Page<TestProductCategory> findAllTestProductCategory() {
		PageRequest pageRequest =
	            new PageRequest(0, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "id");
		
		
		return this.testProductCategoryRepo.findAll(pageRequest);
	}

	@Override
	public Page<TestProduct> findAllTestProduct() {
		PageRequest pageRequest =
	            new PageRequest(0, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "id");
		
		Page<TestProduct> products =  this.testProductRepo.findAll(pageRequest);
		for(TestProduct product : products.getContent()) {
			product.getMethods().size();
		}
		
		return products;
	}

	@Override
	public Page<TestProduct> findTestProduct(String query, Integer pageIndex, Integer pageSize, String categoryCode) {
		PageRequest pageRequest = 
				new PageRequest(pageIndex-1, pageSize, Sort.Direction.ASC, "id");
		
		QTestProduct testProduct = QTestProduct.testProduct;
		QTestMethod testMethod = QTestMethod.testMethod;
		BooleanBuilder p1 = new BooleanBuilder();
		
		List<String> queryList  = Collections.list(new StringTokenizer(query, " ")).stream()
	      .map(token -> (String) token)
	      .collect(Collectors.toList());

		categoryCode = categoryCode == null?categoryCode="":categoryCode;
		
		p1=p1.and(testMethod.isActive.eq(true)
					.and(testMethod.code.like(categoryCode+"%")));
		
		for(String q : queryList) {
			p1 = p1.andAnyOf(
					testMethod.nameEn.containsIgnoreCase(q),
					testMethod.code.containsIgnoreCase(q),
					testMethod.nameTh.containsIgnoreCase(q));
		}
		
		SubQueryExpression<TestMethod> subQuery = JPAExpressions.selectFrom(testMethod).where(p1);
		
		BooleanBuilder p2 = new BooleanBuilder();
		p2 = p2.and(testProduct.isActive.eq(true));
		
		for(String q : queryList) {
			p2 = p2.and(testProduct.keyword.containsIgnoreCase(q));
		}
		
		
		p2 = p2.or(testProduct.methods.any().in(subQuery) );

		logger.debug("about doing searching....");
		
		Page<TestProduct> products =  this.testProductRepo.findAll(p2, pageRequest);
		
		logger.debug("done doing searching....");
		
		for(TestProduct product : products.getContent()) {
			product.getMethods().size();
		}
		
		return products;
	}
	
	
	
	
	
	

	
	
  
	
	
}
