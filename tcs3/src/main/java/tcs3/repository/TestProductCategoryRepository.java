package tcs3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tcs3.model.lab.TestMethod;
import tcs3.model.lab.TestProductCategory;

public interface TestProductCategoryRepository extends JpaRepository<TestProductCategory, Long> {
		
}
