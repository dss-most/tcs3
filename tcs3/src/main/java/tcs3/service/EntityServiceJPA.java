package tcs3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.TestMethod;
import tcs3.repository.OfficerRepository;
import tcs3.repository.OrganizationRepository;
import tcs3.repository.QuotationTemplateRepository;
import tcs3.repository.TestMethodRepository;

public class EntityServiceJPA implements EntityService {

	@Autowired
	private OfficerRepository officerRepo;
	
	@Autowired
	private OrganizationRepository organizationRepo;
	
	@Autowired
	private QuotationTemplateRepository quotationTemplateRepo;
	
	@Autowired
	private TestMethodRepository testMethodRepo;
	
	@Override
	public Officer findOfficerByUserName(String userName) {
		Officer officer = officerRepo.findByDssUser_UserName(userName);
		
		return officer;
	}

	@Override
	public QuotationTemplate saveQuotationTemplate(
			QuotationTemplate quotationTemplate) {
		
		quotationTemplateRepo.save(quotationTemplate);
		
		return quotationTemplate;
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

	
}
