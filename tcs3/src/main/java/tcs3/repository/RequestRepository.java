package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import tcs3.model.lab.Request;

public interface RequestRepository extends JpaRepository<Request, Long>, QueryDslPredicateExecutor<Request> {

}
