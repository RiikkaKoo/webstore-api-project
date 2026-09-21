package fi.metropolia.riikaka.webstore.repository;

import aj.org.objectweb.asm.commons.Remapper;
import fi.metropolia.riikaka.webstore.entity.Supplier;
import fi.metropolia.riikaka.webstore.entity.SupplierAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierAddressRepository extends JpaRepository<SupplierAddress, Integer> {
    SupplierAddress findBySupplier(Supplier supplier);
}
