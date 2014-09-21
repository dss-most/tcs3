package tcs3.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.TestMethod;
import tcs3.repository.OfficerRepository;
import tcs3.repository.OrganizationRepository;
import tcs3.repository.QuotationTemplateRepository;
import tcs3.repository.TestMethodRepository;
import tcs3.webUI.ResponseJSend;
import tcs3.webUI.ResponseStatus;

@Service
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
	public ResponseJSend<Long> saveQuotationTemplate(
			JsonNode node) {
		ResponseJSend<Long> response = new ResponseJSend<Long>();
		
		QuotationTemplate qt;
		if(node.get("id") == null) {
			qt = new QuotationTemplate();
		} else {
			qt = quotationTemplateRepo.findOne(node.get("id").asLong());
		}
		
		qt.setCode(node.get("code") == null ? "" : node.get("code").asText());
		qt.setName(node.get("name") == null ? "" : node.get("name").asText());
		
		// we have to check if we have duplicate code?
		QuotationTemplate qt1 = quotationTemplateRepo.findByCode(qt.getCode());
		if(qt1 != null) {
			response.status = ResponseStatus.FAIL;
			response.message = "รหัส " + qt1.getCode() + " มีอยู่ในฐานข้อมูลแล้ว";
			return response;
		}
		
		
		
		qt.setSampleNote(node.get("sampleNote") == null ? "" : node.get("sampleNote").asText());
		qt.setSamplePrep(node.get("samplePrep") == null ? "" : node.get("samplePrep").asText());
		qt.setRemark(node.get("remark") == null ? "" : node.get("remark").asText());
		
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
  
	
	
}
