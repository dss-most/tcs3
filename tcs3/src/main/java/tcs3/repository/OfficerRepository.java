package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.model.hrx.Officer;

public interface OfficerRepository extends JpaRepository<Officer, Long>, QuerydslPredicateExecutor<Officer> {
	
	public Officer findByDssUser_UserName(String userName);
	
}
