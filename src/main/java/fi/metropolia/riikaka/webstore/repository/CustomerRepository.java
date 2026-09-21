package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
