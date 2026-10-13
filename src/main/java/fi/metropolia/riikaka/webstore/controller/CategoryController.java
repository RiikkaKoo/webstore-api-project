package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Category;
import fi.metropolia.riikaka.webstore.entity.Product;
import fi.metropolia.riikaka.webstore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = categoryService.findAllCategories();
        if (categories == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(categories);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getByCategoryId(@PathVariable Integer id){
        Category category = categoryService.findsCategoryById(id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(category);
        }
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<Product>> getProductsInCategory(@PathVariable Integer id){
        List<Product> products = categoryService.findProductsInCategory(id);
        if (products == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(products);
        }
    }

    @PostMapping
    public ResponseEntity<Category>postNewCategory(@RequestBody Category newCategory){
        Category addedCategory = categoryService.addNewCategory(newCategory);
        if (addedCategory == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(addedCategory);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> putCategory(@PathVariable Integer id, @RequestBody Category updatedCategory){
        Category category = categoryService.updateCategory(id, updatedCategory);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(category);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id){
        String message = categoryService.removeCategory(id);
        if (message != null) {
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Category with ID " + id + " was not found");
            } else {
                return ResponseEntity.ok(message);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete category");
        }
    }
}
