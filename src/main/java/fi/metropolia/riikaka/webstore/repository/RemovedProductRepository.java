package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.RemovedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemovedProductRepository extends JpaRepository<RemovedProduct, Integer> {
}
