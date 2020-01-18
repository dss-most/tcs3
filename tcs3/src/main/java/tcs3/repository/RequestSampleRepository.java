package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import tcs3.model.lab.RequestSample;

public interface RequestSampleRepository extends JpaRepository<RequestSample, Long>, QueryDslPredicateExecutor<RequestSample> {

}
