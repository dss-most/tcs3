package tcs3.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import tcs3.model.lab.QuotationTemplate;

public interface QuotationTemplateRepository extends
		PagingAndSortingRepository<QuotationTemplate, Long> {

}
