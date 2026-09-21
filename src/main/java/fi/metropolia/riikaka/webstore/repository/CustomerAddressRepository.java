package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
}
