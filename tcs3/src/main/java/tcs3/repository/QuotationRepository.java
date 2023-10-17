package tcs3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.model.hrx.Organization;
import tcs3.model.lab.Quotation;

public interface QuotationRepository extends JpaRepository<Quotation, Long>, QuerydslPredicateExecutor<Quotation> {

	
	@Query(""
			+ "SELECT quotation "
			+ "FROM Quotation quotation "
			+ "WHERE UPPER(quotation.name) like UPPER(?1) "
			+ "		AND UPPER(quotation.code) like UPPER(?2) "
			+ "		AND ( ?3 = ?3) "
			+ "		AND ( ?4 = ?4 ) "
			+ "		AND quotation.mainOrg = ?5 "
			+ "		AND quotation.groupOrg in (?6) ")
	Page<Quotation> findByField(
			String nameQuery, 		// ?1
			String codeQuery,		// ?2
			String companyQuery,	// ?3
			String quotationNo, 	// ?4
			Organization mainOrg,	// ?5
			List<Organization> groupOrgList, // $6
			Pageable pageRequest);

}
