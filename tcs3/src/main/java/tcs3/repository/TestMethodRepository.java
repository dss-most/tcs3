package tcs3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.lab.TestMethod;

public interface TestMethodRepository extends PagingAndSortingRepository<TestMethod, Long> {
	
	@Query(""
			+ "SELECT testMethod "
			+ "FROM TestMethod testMethod "
			+ "WHERE testMethod.nameTh like ?1 "
			+ "	AND testMethod.code is not null ")
	public List<TestMethod> findTestMethodByNameThLike(String nameTh);
	
}
