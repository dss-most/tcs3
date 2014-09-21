package tcs3.auth.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tcs3.auth.model.DssUser;
import tcs3.model.hrx.Officer;

public interface DssUserRepository extends CrudRepository<DssUser, Long> {

	@Query(""
			+ "SELECT dssUser "
			+ "FROM DssUser dssUser "
			+ "		INNER JOIN FETCH dssUser.officer officer "
			+ "		INNER JOIN FETCH officer.workAt workAt "
			+ "		INNER JOIN FETCH workAt.parent "
			+ "WHERE dssUser.userName like ?1 AND dssUser.password like ?2")
	public DssUser findByUserNameAndPassword(String userName, String password);

	@Query(""
			+ "SELECT officer "
			+ "FROM Officer officer "
			+ "		INNER JOIN FETCH officer.dssUser dssUser "
			+ "		INNER JOIN FETCH officer.workAt workAt "
			+ "		INNER JOIN FETCH workAt.parent "
			+ "WHERE dssUser.userName like ?1 ")
	public Officer findOfficerByUserName(String userName);
	
}
