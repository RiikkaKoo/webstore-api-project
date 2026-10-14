package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Product;
import fi.metropolia.riikaka.webstore.repository.CategoryRepository;
import fi.metropolia.riikaka.webstore.repository.ProductRepository;
import fi.metropolia.riikaka.webstore.repository.SupplierRepository;
import fi.metropolia.riikaka.webstore.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository,
                             SupplierRepository supplierRepository, ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productService.findAll();
        if (products == null) {
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
        Product product = productService.findProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(product);
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Product>> getInactiveProducts(){
        List<Product> products = productService.findInactiveProducts();
        if (products == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(products);
        }
    }

    @GetMapping("/lowStock/{max}")
    public ResponseEntity<List<Product>> getProductsLowOnStock(@PathVariable int max){
        List<Product> products = productService.findProductsLowOnStock(max);
        if (products == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(products);
        }
    }

    @PostMapping
    public ResponseEntity<Product>postNewProduct(@RequestBody Product newProduct){
        Product product = productService.addNewProduct(newProduct);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(product);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> putProduct(@PathVariable Integer id, @RequestBody Product updatedProduct){
        Product product = productService.updateProduct(id, updatedProduct);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(product);
        }
    }

    @PutMapping("/price/increase/{percent}")
    public ResponseEntity<String> increaseProductPrices(@PathVariable float percent){
        return ResponseEntity.ok(productService.raiseProductPrices(percent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
        String message = productService.softDeleteProduct(id);
        if (message != null) {
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product with ID " + id + " was not found");
            } else {
                return ResponseEntity.ok(message);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not archive product");
        }
    }
}
