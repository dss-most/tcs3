package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import tcs3.model.lab.QuotationNumber;



public interface QuotationNumberRepository extends JpaRepository<QuotationNumber, Long>,
	QueryDslPredicateExecutor<QuotationNumber> {

}
