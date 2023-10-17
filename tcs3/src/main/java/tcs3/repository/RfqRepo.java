package tcs3.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.model.lab.Rfq;


public interface RfqRepo extends JpaRepository<Rfq, Long>, QuerydslPredicateExecutor<Rfq> {

}
