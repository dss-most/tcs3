package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import tcs3.model.fin.Invoice;

public interface FinInvoiceRepository extends JpaRepository<Invoice, Long>, QueryDslPredicateExecutor<Invoice> {

}
