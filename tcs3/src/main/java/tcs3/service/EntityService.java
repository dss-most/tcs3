package tcs3.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.TestMethod;

public interface EntityService {

	public Officer findOfficerByUserName(String userName);
	
	
	
	public List<TestMethod> findTestMethodByNameThLike(String nameTh);


	public Organization findOrgannizationById(Long id);
	public List<Organization> findTopOrgannization();
	public List<Organization> findOrgannizationChildrenOfId(Long id);


	public Long saveQuotationTemplate(JsonNode node);
	
}
