package tcs3.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tcs3.model.customer.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
