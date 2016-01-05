package tcs3.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.lab.Promotion;
import tcs3.model.lab.PromotionDiscount;

public interface PromotionDiscountRepository extends
		PagingAndSortingRepository<PromotionDiscount, Long>, QueryDslPredicateExecutor<PromotionDiscount> {

	

}
