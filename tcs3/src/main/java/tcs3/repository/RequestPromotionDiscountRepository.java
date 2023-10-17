package tcs3.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.lab.RequestPromotionDiscount;

public interface RequestPromotionDiscountRepository extends
		PagingAndSortingRepository<RequestPromotionDiscount, Long>, QuerydslPredicateExecutor<RequestPromotionDiscount> {

	

}
