package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
