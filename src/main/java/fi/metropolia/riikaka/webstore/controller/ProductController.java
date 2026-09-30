package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Category;
import fi.metropolia.riikaka.webstore.entity.Product;
import fi.metropolia.riikaka.webstore.entity.Supplier;
import fi.metropolia.riikaka.webstore.repository.CategoryRepository;
import fi.metropolia.riikaka.webstore.repository.ProductRepository;
import fi.metropolia.riikaka.webstore.repository.SupplierRepository;
import fi.metropolia.riikaka.webstore.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository,
                             SupplierRepository supplierRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/{key}")
    public ResponseEntity<List<Product>> getProductsByCriteria(@PathVariable String key){
        List<Product> products = productService.findByCriteria(key);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getByProductId(@PathVariable Integer id){
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/archived")
    public ResponseEntity<List<Product>> getRemovedProducts(){
        List<Product> products = productRepository.findAllByArchived(true);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/lowStock/{max}")
    public ResponseEntity<List<Product>> getProductsLowOnStock(@PathVariable int max){
        List<Product> products = productRepository.findProductsLowOnStock(max);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product>postNewProduct(@RequestBody Product newProduct){
        Category category = categoryRepository.getReferenceById(newProduct.getCategory().getId());
        Supplier supplier = supplierRepository.getReferenceById(newProduct.getSupplier().getId());
        newProduct.setSupplier(supplier);
        newProduct.setCategory(category);
        try {
            Product addedProduct = productRepository.save(newProduct);
            return ResponseEntity.ok(addedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> putProduct(@PathVariable Integer id, @RequestBody Product updatedProduct){
        Category category = categoryRepository.getReferenceById(updatedProduct.getCategory().getId());
        Supplier supplier = supplierRepository.getReferenceById(updatedProduct.getCategory().getId());
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock_quantity(updatedProduct.getStock_quantity());
                    product.setCategory(category);
                    product.setSupplier(supplier);
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/price/increase/{percent}")
    public ResponseEntity<String> increaseProductPrices(@PathVariable float percent){
        float increase = 1 + (percent/100);
        int changed = productRepository.increasePrice(increase);
        return ResponseEntity.ok("Price increased by " + percent + "% for " + changed + " products.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        try {
            if (productRepository.existsById(id)) {
                Product product = productRepository.getReferenceById(id);
                product.setArchived(true);
                productRepository.save(product);
                return ResponseEntity.ok("SOFT DELETE: Product with ID " + id +" was successfully archived");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + id + " was not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not archive product");
        }
    }
}
