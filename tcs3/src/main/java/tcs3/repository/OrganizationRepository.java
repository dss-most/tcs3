package tcs3.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.hrx.Organization;

public interface OrganizationRepository extends
		PagingAndSortingRepository<Organization, Long> {

}
