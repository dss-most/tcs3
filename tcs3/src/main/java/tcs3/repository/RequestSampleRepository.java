package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import tcs3.model.lab.Request;
import tcs3.model.lab.RequestSample;
import tcs3.model.lab.RequestSample12;

public interface RequestSampleRepository extends JpaRepository<RequestSample, Long>, QueryDslPredicateExecutor<RequestSample> {

}
