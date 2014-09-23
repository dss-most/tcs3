package tcs3.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import tcs3.model.customer.Customer;
import tcs3.model.global.District;
import tcs3.model.global.Province;
import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.TestMethod;
import tcs3.model.lab.TestMethodQuotationTemplateItem;
import tcs3.repository.AddressRepository;
import tcs3.repository.CompanyRepository;
import tcs3.repository.CustomerRepository;
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
	
	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
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
	
	
  
	
	
}
