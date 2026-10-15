package fi.metropolia.riikaka.webstore.service;

import fi.metropolia.riikaka.webstore.entity.Category;
import fi.metropolia.riikaka.webstore.entity.Product;
import fi.metropolia.riikaka.webstore.entity.Supplier;
import fi.metropolia.riikaka.webstore.repository.CategoryRepository;
import fi.metropolia.riikaka.webstore.repository.ProductRepository;
import fi.metropolia.riikaka.webstore.repository.SupplierRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }


    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return null;
        } else {
            return products;
        }
    }

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

    public Product findProductById(int id){
        try {
            return productRepository.getReferenceById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Product> findInactiveProducts(){
        List<Product> products = productRepository.findAllByActive(false);
        if (products.isEmpty()) {
            return null;
        } else {
            return products;
        }
    }

    public List<Product> findProductsLowOnStock(int max){
        List<Product> products = productRepository.findProductsLowOnStock(max);
        if (products.isEmpty()) {
            return null;
        } else {
            return products;
        }
    }

    public Product addNewProduct(Product newProduct){
        Category category = categoryRepository.getReferenceById(newProduct.getCategory().getId());
        Supplier supplier = supplierRepository.getReferenceById(newProduct.getSupplier().getId());
        newProduct.setSupplier(supplier);
        newProduct.setCategory(category);
        try {
            return productRepository.save(newProduct);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Product updateProduct(int id, Product updatedProduct) {
        try {
            Product product = productRepository.findById(id);

            Category category = categoryRepository.getReferenceById(updatedProduct.getCategory() != null
                ? updatedProduct.getCategory().getId() : product.getCategory().getId());
            Supplier supplier = supplierRepository.getReferenceById(updatedProduct.getSupplier() != null
                ? updatedProduct.getSupplier().getId() : product.getSupplier().getId());

            product.setName(updatedProduct.getName() != null ? updatedProduct.getName() : product.getName());
            product.setDescription(updatedProduct.getDescription() != null ? updatedProduct.getDescription() : product.getDescription());
            product.setPrice(updatedProduct.getPrice() != 0 ? updatedProduct.getPrice() : product.getPrice());
            product.setStock_quantity(updatedProduct.getStock_quantity());
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setActive(updatedProduct.isActive());
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Transactional
    public String raiseProductPrices(float percent){
        try {
            if (percent <= 0) {
                throw new IllegalArgumentException("Invalid increase percentage");
            }
            float increase = 1 + (percent/100);
            int changed = productRepository.increasePrice(increase);
            return "Price increased by " + percent + "% for " + changed + " products.";
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

    @Transactional
    public String softDeleteProduct(int id){
        try {
            if (productRepository.existsById(id)) {
                Product product = productRepository.findById(id);
                product.setActive(false);
                productRepository.save(product);
                return "SOFT DELETE: Product with ID " + id +" was successfully set inactive";
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw (e);
        }
    }

}
