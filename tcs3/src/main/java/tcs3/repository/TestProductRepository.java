package tcs3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

import tcs3.model.lab.TestMethod;
import tcs3.model.lab.TestProduct;
import tcs3.model.lab.TestProductCategory;

public interface TestProductRepository extends 
	JpaRepository<TestProduct, Long>,
	QueryDslPredicateExecutor<TestProduct> {
		
}
