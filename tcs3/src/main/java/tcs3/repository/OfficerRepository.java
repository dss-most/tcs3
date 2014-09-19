package tcs3.repository;

import org.springframework.data.repository.CrudRepository;

import tcs3.model.hrx.Officer;

public interface OfficerRepository extends CrudRepository<Officer, Long> {
	
	public Officer findByDssUser_UserName(String userName);
	
}
