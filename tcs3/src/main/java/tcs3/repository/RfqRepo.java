package tcs3.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import tcs3.model.lab.Rfq;


public interface RfqRepo extends JpaRepository<Rfq, Long>, QueryDslPredicateExecutor<Rfq> {

}
