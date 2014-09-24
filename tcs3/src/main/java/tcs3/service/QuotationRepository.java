package tcs3.service;

import org.springframework.data.jpa.repository.JpaRepository;

import tcs3.model.lab.Quotation;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

}
