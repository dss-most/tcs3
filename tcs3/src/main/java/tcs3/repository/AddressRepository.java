package tcs3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tcs3.model.customer.Address;
import tcs3.model.global.District;
import tcs3.model.global.Province;

public interface AddressRepository extends JpaRepository<Address, Long> {

	@Query(""
			+ "SELECT province "
			+ "FROM Province province "
			+ "ORDER BY province.name ASC")
	List<Province> findProvinces();

	@Query(""
			+ "SELECT  district "
			+ "FROM District district "
			+ " 	WHERE district.province.id = ?1 "
			+ "ORDER BY district.name ASC ")
	List<District> findDistrictsOfProvince(Long id);

	@Query(""
			+ "SELECT province "
			+ "FROM Province province "
			+ "WHERE province.id = ?1")
	Province findProvinceById(Long provinceId);

	@Query(""
			+ "SELECT district "
			+ "FROM District district "
			+ "WHERE district.id = ?1")
	District findDistrictById(Long districtId);
	
	
}
