package tcs3.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.lab.Promotion;

public interface PromotionRepository extends
		PagingAndSortingRepository<Promotion, Long>, QueryDslPredicateExecutor<Promotion> {

	

}
