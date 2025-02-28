package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import tcs3.model.lab.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>, QuerydslPredicateExecutor<Invoice> {

}
