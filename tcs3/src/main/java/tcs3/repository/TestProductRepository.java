package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.model.lab.TestProduct;

public interface TestProductRepository extends 
	JpaRepository<TestProduct, Long>,
	QuerydslPredicateExecutor<TestProduct> {
		
}
