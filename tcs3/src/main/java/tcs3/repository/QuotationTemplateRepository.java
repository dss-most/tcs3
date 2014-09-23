package tcs3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.hrx.Organization;
import tcs3.model.lab.QuotationTemplate;

public interface QuotationTemplateRepository extends
		PagingAndSortingRepository<QuotationTemplate, Long> {

	QuotationTemplate findByCode(String code);

	@Query(""
			+ "SELECT template "
			+ "FROM QuotationTemplate template "
			+ "WHERE UPPER(template.name) like ?1 "
			+ "		AND UPPER(template.code) like ?2 "
			+ "		AND template.mainOrg = ?3 "
			+ "		AND template.groupOrg in (?4) ")
	Page<QuotationTemplate> findByField(String nameQuery, String codeQuery,
			Organization mainOrg, List<Organization> groupOrgList,
			Pageable pageRequest);

}
