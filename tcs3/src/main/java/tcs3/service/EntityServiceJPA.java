package tcs3.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import tcs3.model.customer.Address;
import tcs3.model.customer.Company;
import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.TestMethod;
import tcs3.model.lab.TestMethodQuotationTemplateItem;
import tcs3.repository.CompanyRepository;
import tcs3.repository.OfficerRepository;
import tcs3.repository.OrganizationRepository;
import tcs3.repository.QuotationTemplateRepository;
import tcs3.repository.TestMethodQuotationTemplateItemRepo;
import tcs3.repository.TestMethodRepository;
import tcs3.webUI.DefaultProperty;
import tcs3.webUI.ResponseJSend;
import tcs3.webUI.ResponseStatus;

@Service
public class EntityServiceJPA implements EntityService {
	public static Logger logger = LoggerFactory.getLogger(EntityServiceJPA.class);
	
	@Autowired
	private OfficerRepository officerRepo;
	
	@Autowired
	private OrganizationRepository organizationRepo;
	
	@Autowired
	private QuotationTemplateRepository quotationTemplateRepo;
	
	@Autowired
	private TestMethodRepository testMethodRepo;

	@Autowired
	private TestMethodQuotationTemplateItemRepo testMethodQuotationTemplateItemRepo;
	
	@Autowired
	private CompanyRepository companyRepo;
	
	@Override
	public Officer findOfficerByUserName(String userName) {
		Officer officer = officerRepo.findByDssUser_UserName(userName);
		
		return officer;
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
		
		qt.setCode(node.get("code") == null ? "" : node.get("code").asText());
		qt.setName(node.get("name") == null ? "" : node.get("name").asText());
		
		if(node.get("groupOrg") != null) {
			if(node.get("groupOrg").get("id") != null) {
				Organization groupOrg = organizationRepo.findOne(node.get("groupOrg").get("id").asLong());
				qt.setGroupOrg(groupOrg);
				qt.setMainOrg(groupOrg.getParent());
			}
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
		testMethodQuotationTemplateItemRepo.delete(oldItemList);

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
		Long[] ids = {4L,8L,9L,10L};
		
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
	public ResponseJSend<Page<QuotationTemplate>> findQuotationTemplateByField(
			String nameQuery, String codeQuery, Long mainOrgId,
			Long groupOrgId, Integer pageNumber) {
		
		
		nameQuery = "%"+nameQuery+"%";
		codeQuery = "%"+codeQuery+"%";
		
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, DefaultProperty.NUMBER_OF_ELEMENT_PER_PAGE, Sort.Direction.ASC, "code");
		
		Organization mainOrg = organizationRepo.findOne(mainOrgId);
		List<Organization> groupOrgList;
		if(groupOrgId == null || groupOrgId == 0) {
			groupOrgList = organizationRepo.findAllByParent_Id(mainOrg.getId());
		} else {
			Organization groupOrg = organizationRepo.findOne(groupOrgId);
			groupOrgList = new ArrayList<Organization>();
			groupOrgList.add(groupOrg);
		}
		
		Page<QuotationTemplate> templates = quotationTemplateRepo.findByField(nameQuery, codeQuery, mainOrg, groupOrgList, pageRequest);
		
		ResponseJSend<Page<QuotationTemplate>> response = new ResponseJSend<Page<QuotationTemplate>>();
		response.data=templates;
		response.status=ResponseStatus.SUCCESS;
		return response;
	}

	@Override
	public Company findCompanyById(Long id) {
		return companyRepo.findOne(id);
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
	
	
  
	
	
}
