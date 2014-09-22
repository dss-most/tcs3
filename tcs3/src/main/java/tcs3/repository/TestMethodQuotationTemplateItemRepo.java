package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tcs3.model.lab.TestMethodQuotationTemplateItem;

public interface TestMethodQuotationTemplateItemRepo extends
		JpaRepository<TestMethodQuotationTemplateItem, Long> {

}
