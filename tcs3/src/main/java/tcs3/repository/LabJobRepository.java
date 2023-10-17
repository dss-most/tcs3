package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.model.lab.LabJob;

public interface LabJobRepository extends JpaRepository<LabJob, Long>, QuerydslPredicateExecutor<LabJob> {

}
