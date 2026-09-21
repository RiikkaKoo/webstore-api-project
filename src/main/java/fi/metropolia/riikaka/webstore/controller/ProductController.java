package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Product;
import fi.metropolia.riikaka.webstore.entity.RemovedProduct;
import fi.metropolia.riikaka.webstore.repository.ProductRepository;
import fi.metropolia.riikaka.webstore.repository.RemovedProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final RemovedProductRepository removedProductRepository;

    public ProductController(ProductRepository productRepository, RemovedProductRepository removedProductRepository) {
        this.productRepository = productRepository;
        this.removedProductRepository = removedProductRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){

        List<Product> products = productRepository.findAll();
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

    @GetMapping("/removed")
    public ResponseEntity<List<RemovedProduct>> getRemovedProducts(){

        List<RemovedProduct> products = removedProductRepository.findAll();
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/removed/{id}")
    public ResponseEntity<RemovedProduct> getRemovedProductById(@PathVariable Integer id){
        return removedProductRepository.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product>postNewProduct(@RequestBody Product newProduct){
        try {
            Product addedProduct = productRepository.save(newProduct);
            return ResponseEntity.ok(addedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> putProduct(@PathVariable Integer id, @RequestBody Product updatedProduct){
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock_quantity(updatedProduct.getStock_quantity());
                    product.setCategory(updatedProduct.getCategory());
                    product.setSupplier(updatedProduct.getSupplier());
                    return ResponseEntity.ok(productRepository.save(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
                return ResponseEntity.ok("Product with ID " + id +" was successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + id + " was not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete product");
        }
    }
}
