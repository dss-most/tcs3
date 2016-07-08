package tcs3.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.lab.RequestAddress;

public interface RequestAddressRepository extends QueryDslPredicateExecutor<RequestAddress>, PagingAndSortingRepository<RequestAddress, Long> {

}
