package tcs3.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import tcs3.model.customer.Address;
import tcs3.model.customer.Company;
import tcs3.model.global.District;
import tcs3.model.global.Province;
import tcs3.model.hrx.Officer;
import tcs3.model.hrx.Organization;
import tcs3.model.lab.Quotation;
import tcs3.model.lab.QuotationTemplate;
import tcs3.model.lab.SampleType;
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
			JsonNode node,
			Integer pageNumber) throws JsonMappingException;



	public Company findCompanyById(Long id);

	public List<Address> findAddressOfId(Long id);

	public ResponseJSend<Page<Company>> searchCompanyByName(
			String nameQuery, Integer pageNumber);



	public List<Province> findProvinces();
	public List<District> findDistrictsOfProvince(Long id);



	public ResponseJSend<Long> saveCompany(JsonNode node);



	public ResponseJSend<Quotation> saveQuotation(JsonNode node);



	public ResponseJSend<Page<Quotation>> findQuotationByField(
			JsonNode node,
			Integer pageNumber) throws JsonMappingException;



	public Quotation findQuotation(Long id);



	public QuotationTemplate findQuotationTemplate(Long id);



	public SampleType findSampleType(Long id);



	public Iterable<SampleType> findAllSampleType();
	
}
