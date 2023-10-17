package tcs3.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.lab.RequestAddress;

public interface RequestAddressRepository extends QuerydslPredicateExecutor<RequestAddress>, PagingAndSortingRepository<RequestAddress, Long> {

}
