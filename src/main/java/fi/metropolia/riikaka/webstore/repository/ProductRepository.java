package fi.metropolia.riikaka.webstore.repository;

import fi.metropolia.riikaka.webstore.entity.Category;
import fi.metropolia.riikaka.webstore.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByCategory(Category category);
    List<Product> findAllByArchived(boolean b);

    @Query("SELECT p FROM Product p WHERE p.stock_quantity <= :max")
    List<Product> findProductsLowOnStock(@Param("max") int max);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.price = p.price * :increase")
    int increasePrice(@Param("increase") double increase);
}
