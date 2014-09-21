package tcs3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.hrx.Organization;

public interface OrganizationRepository extends
		PagingAndSortingRepository<Organization, Long> {

	@Query(""
			+ "SELECT org "
			+ "FROM Organization org "
			+ "WHERE org.id in (?1) "
			+ "ORDER BY org.id ASC ")
	List<Organization> findAllByIds(List<Long> idList);

	List<Organization> findAllByParent_Id(Long id);

}
