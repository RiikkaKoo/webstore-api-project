package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.Customer;
import fi.metropolia.riikaka.webstore.entity.CustomerAddress;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer> {
    CustomerAddress findByCustomer(Customer referenceById);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteByCustomer(Customer customer);
}
