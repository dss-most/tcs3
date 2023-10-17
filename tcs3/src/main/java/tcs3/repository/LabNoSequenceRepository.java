package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.model.lab.LabNoSequence;

public interface LabNoSequenceRepository extends JpaRepository<LabNoSequence, Long>, QuerydslPredicateExecutor<LabNoSequence> {

}
