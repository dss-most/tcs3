package tcs3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tcs3.model.lab.TestMethod;

public interface TestMethodRepository extends JpaRepository<TestMethod, Long> {
	
	@Query(""
			+ "SELECT testMethod "
			+ "FROM TestMethod testMethod "
			+ "WHERE testMethod.nameTh like ?1 "
			+ "	AND testMethod.code is not null ")
	public List<TestMethod> findTestMethodByNameThLike(String nameTh);

	@Query(""
			+ "SELECT testMethod "
			+ "FROM TestMethod testMethod "
			+ "WHERE ( UPPER(testMethod.nameTh) like UPPER(?1) "
			+ "		OR UPPER(testMethod.nameEn) like UPPER(?1) "
			+ "		OR UPPER(testMethod.code) like UPPER(?1) ) "
			+ "	AND testMethod.code is not null ")
	public Page<TestMethod> findByNameThLikeOrNameEnLikeOrCodeLike(String query, Pageable pageRequest);
	
}
