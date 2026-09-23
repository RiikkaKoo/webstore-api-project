package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Category;
import fi.metropolia.riikaka.webstore.entity.Product;
import fi.metropolia.riikaka.webstore.repository.CategoryRepository;
import fi.metropolia.riikaka.webstore.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(){

        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getByCategoryId(@PathVariable Integer id){
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(category))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getProductsInCategory(@PathVariable Integer id){
        Category category = categoryRepository.getReferenceById(id);
        List<Product> products = productRepository.findAllByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Category>postNewCategory(@RequestBody Category newCategory){
        try {
            Category addedCategory = categoryRepository.save(newCategory);
            return ResponseEntity.ok(addedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> putCategory(@PathVariable Integer id, @RequestBody Category updatedCategory){
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    category.setDescription(updatedCategory.getDescription());
                    return ResponseEntity.ok(categoryRepository.save(category));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id){
        try {
            if (categoryRepository.existsById(id)) {
                Category category = categoryRepository.getReferenceById(id);
                List<Product> products = productRepository.findAllByCategory(category);
                for (Product p : products) {
                    p.setCategory(null);
                    productRepository.save(p);
                }
                categoryRepository.deleteById(id);
                return ResponseEntity.ok("Category with ID " + id +" was successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Category with ID " + id + " was not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete category");
        }
    }
}
