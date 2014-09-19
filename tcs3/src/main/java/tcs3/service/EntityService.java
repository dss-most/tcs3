package tcs3.service;

import java.util.List;

import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.TestMethod;

public interface EntityService {

	public Officer findOfficerByUserName(String userName);
	
	
	public QuotationTemplate saveQuotationTemplate(QuotationTemplate quotationTemplate);
	
	
	public List<TestMethod> findTestMethodByNameThLike(String nameTh);


	public Organization findOrgannizationById(Long id);
	
}
