package fi.metropolia.riikaka.webstore.service;

import fi.metropolia.riikaka.webstore.entity.Category;
import fi.metropolia.riikaka.webstore.entity.Product;
import fi.metropolia.riikaka.webstore.repository.CategoryRepository;
import fi.metropolia.riikaka.webstore.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> findAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return null;
        } else {
            return categories;
        }
    }

    public Category findsCategoryById(int id){
        if (categoryRepository.findById(id).isEmpty()) {
            return null;
        } else {
            return categoryRepository.findById(id).get();
        }
    }

    public List<Product> findProductsInCategory(int id){
        Category category = categoryRepository.getReferenceById(id);
        List<Product> products = productRepository.findAllByCategory(category);
        if (products.isEmpty()) {
            return null;
        } else {
            return products;
        }
    }

    public Category addNewCategory(Category newCategory){
        try {
            return categoryRepository.save(newCategory);
        } catch (Exception e) {
            return null;
        }
    }

    public Category updateCategory(int id, Category updatedCategory){
        Category category = categoryRepository.getReferenceById(id);
        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());

        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            return null;
        }
    }

    public String removeCategory(int id){
        try {
            if (categoryRepository.existsById(id)) {
                Category category = categoryRepository.getReferenceById(id);
                List<Product> products = productRepository.findAllByCategory(category);
                for (Product p : products) {
                    p.setCategory(null);
                    productRepository.save(p);
                }
                categoryRepository.deleteById(id);
                return "Category with ID " + id +" was successfully deleted";
            } else {
                return "";
            }
        } catch (Exception e) {
            return null;
        }
    }
}
