package tcs3.auth.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.auth.model.DssRole;

public interface DssRoleRepository extends JpaRepository<DssRole, Long>, QuerydslPredicateExecutor<DssRole> {

}
