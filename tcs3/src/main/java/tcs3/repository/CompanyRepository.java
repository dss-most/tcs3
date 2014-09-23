package tcs3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tcs3.model.customer.Address;
import tcs3.model.customer.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query(""
			+ "SELECT company.addresses "
			+ "FROM Company company "
			+ "WHERE company.id = ?1 ")
	List<Address> findAddressesOfId(Long id);

	@Query(""
			+ "SELECT company "
			+ "FROM Company company "
			+ "WHERE UPPER(company.nameTh) like UPPER(?1) "
			+ "		OR  UPPER(company.nameEn) like UPPER(?1) ")
	Page<Company> findAllByNameLike(String nameQuery, Pageable pageRequest);

}
