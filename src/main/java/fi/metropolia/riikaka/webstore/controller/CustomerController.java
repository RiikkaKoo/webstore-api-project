package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Customer;
import fi.metropolia.riikaka.webstore.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    // Get all Customers
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(){

        List<Customer> customers = repository.findAll();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getByCustomerId(@PathVariable Integer id){
        return repository.findById(id)
                .map(customer -> ResponseEntity.ok(customer))
                .orElse(ResponseEntity.notFound().build());
    }

    /*
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getByCustomerEmail(@PathVariable String email){
        return repository.findByEmail(email)
                .map(customer -> ResponseEntity.ok(customer))
                .orElse(ResponseEntity.notFound().build());
    }

*/
    @PostMapping
    public ResponseEntity<Customer>postNewCustomer(@RequestBody Customer newCustomer){
        try {
            Customer addedCustomer = repository.save(newCustomer);
            return ResponseEntity.ok(addedCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> putCustomer(@PathVariable Integer id, @RequestBody Customer updatedCustomer){
        return repository.findById(id)
                .map(customer -> {
                    customer.setFirst_name(updatedCustomer.getFirst_name());
                    customer.setLast_name(updatedCustomer.getLast_name());
                    customer.setEmail(updatedCustomer.getEmail());
                    customer.setPhone(updatedCustomer.getPhone());
                    return ResponseEntity.ok(repository.save(customer));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id){
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return ResponseEntity.ok("Customer with ID " + id +" was successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Customer with ID " + id + " was not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete customer");
        }
    }
}
