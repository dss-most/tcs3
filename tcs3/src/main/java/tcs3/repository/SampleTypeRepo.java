package tcs3.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import tcs3.model.lab.Rfq;
import tcs3.model.lab.SampleType;


public interface SampleTypeRepo extends JpaRepository<SampleType, Long>, QueryDslPredicateExecutor<SampleType> {

}
