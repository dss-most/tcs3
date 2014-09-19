package tcs3.auth.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tcs3.auth.model.DssUser;
import tcs3.model.hrx.Officer;

public interface DssUserRepository extends CrudRepository<DssUser, Long> {

	public DssUser findByUserNameAndPassword(String userName, String password);

	@Query(""
			+ "SELECT officer "
			+ "FROM Officer officer "
			+ "		INNER JOIN FETCH officer.dssUser dssUser "
			+ "		INNER JOIN FETCH officer.workAt "
			+ "WHERE dssUser.userName like ?1 ")
	public Officer findOfficerByUserName(String userName);
	
}
