package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tcs3.model.lab.TestProductCategory;

public interface TestProductCategoryRepository extends JpaRepository<TestProductCategory, Long> {
		
}
