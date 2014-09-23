package tcs3.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonNode;

import tcs3.model.customer.Address;
import tcs3.model.customer.Company;
import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.TestMethod;
import tcs3.webUI.ResponseJSend;

public interface EntityService {

	public Officer findOfficerByUserName(String userName);
	
	
	
	public List<TestMethod> findTestMethodByNameThLike(String nameTh);


	public Organization findOrgannizationById(Long id);
	public List<Organization> findTopOrgannization();
	public List<Organization> findOrgannizationChildrenOfId(Long id);


	public ResponseJSend<Long> saveQuotationTemplate(JsonNode node);

	public ResponseJSend<Page<TestMethod>> findTestMethodByNameOrCode(
			String query, Integer pageNumber);

	public ResponseJSend<Page<QuotationTemplate>> findQuotationTemplateByField(
			String nameQuery, String codeQuery, Long mainOrgId,
			Long groupOrgId, Integer pageNumber);



	public Company findCompanyById(Long id);

	public List<Address> findAddressOfId(Long id);

	public ResponseJSend<Page<Company>> searchCompanyByName(
			String nameQuery, Integer pageNumber);
	
}
