package tcs3.auth.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import tcs3.auth.model.DssUser;
import tcs3.model.hrx.Officer;

public interface DssUserRepository extends JpaRepository<DssUser, Long>, QueryDslPredicateExecutor<DssUser> {

	@Query(""
			+ "SELECT dssUser "
			+ "FROM DssUser dssUser "
			+ "		INNER JOIN FETCH dssUser.officer officer "
			+ "		INNER JOIN FETCH officer.workAt workAt "
			+ "		INNER JOIN FETCH workAt.parent "
			+ "WHERE dssUser.userName like ?1 AND dssUser.password like ?2")
	public DssUser findByUserNameAndPasswordForDssUser(String userName, String password);
	
	@Query(""
			+ "SELECT dssUser "
			+ "FROM DssUser dssUser "
			+ "		LEFT JOIN FETCH dssUser.officer officer "
			+ "		LEFT JOIN FETCH officer.workAt workAt "
			+ "		LEFT JOIN FETCH workAt.parent "
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
