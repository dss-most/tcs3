package tcs3.auth.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import tcs3.auth.model.DssRole;
import tcs3.model.lab.Rfq;

public interface DssRoleRepository extends JpaRepository<DssRole, Long>, QueryDslPredicateExecutor<DssRole> {

}
