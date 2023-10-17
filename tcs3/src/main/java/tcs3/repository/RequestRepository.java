package tcs3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.model.lab.Request;

public interface RequestRepository extends JpaRepository<Request, Long>,  QuerydslPredicateExecutor<Request> {
	
	@Query(value = ""
				+ "SELECT request "
				+ "FROM Request request"
				+ " JOIN FETCH request.samples "
				+ "WHERE lower(request.reqNo) like lower(concat('%', ?1,'%')) "
				+ "	AND (lower(request.company.nameTh) like lower(concat('%', ?2,'%')) "
				+ "			OR lower(request.company.nameEn) like lower(concat('%', ?2,'%'))) ",
			countQuery = ""
				+ "SELECT count(request) "
				+ "FROM Request request " 
				+ "WHERE lower(request.reqNo) like lower(concat('%', ?1,'%')) "
				+ "	AND (lower(request.company.nameTh) like lower(concat('%', ?2,'%')) "
				+ "			OR lower(request.company.nameEn) like lower(concat('%', ?2,'%')))  ")
	Page<Request> findByReqNoOrCompanyName(String ReqNo, String Companyname, Pageable pageable);
}
