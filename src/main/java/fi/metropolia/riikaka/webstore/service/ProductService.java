package fi.metropolia.riikaka.webstore.service;

import fi.metropolia.riikaka.webstore.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Product> findByCriteria(String key) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Product.class);
        Root<Product> productRoot = cq.from(Product.class);
        cq.select(productRoot).where(cb.or(
                cb.like(cb.lower(productRoot.get("name")),"%"+key+"%"),
                cb.like(cb.lower(productRoot.get("description")),"%"+key+"%"))
                );
        return entityManager.createQuery(cq).getResultList();
    }


}
