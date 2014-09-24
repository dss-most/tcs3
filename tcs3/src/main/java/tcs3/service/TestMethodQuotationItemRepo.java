package tcs3.service;

import org.springframework.data.jpa.repository.JpaRepository;

import tcs3.model.lab.TestMethodQuotationItem;

public interface TestMethodQuotationItemRepo extends JpaRepository<TestMethodQuotationItem, Long> {

}
