package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Customer;
import fi.metropolia.riikaka.webstore.entity.CustomerAddress;
import fi.metropolia.riikaka.webstore.entity.SupplierAddress;
import fi.metropolia.riikaka.webstore.repository.CustomerAddressRepository;
import fi.metropolia.riikaka.webstore.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;

    public CustomerController(CustomerRepository customerRepository, CustomerAddressRepository customerAddressRepository) {
        this.customerRepository = customerRepository;
        this.customerAddressRepository = customerAddressRepository;
    }

    // Get all Customers
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(){

        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getByCustomerId(@PathVariable Integer id){
        return customerRepository.findById(id)
                .map(customer -> ResponseEntity.ok(customer))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<List<CustomerAddress>> getSupplierAddress(@PathVariable Integer id){
        List<CustomerAddress> addresses = customerRepository.getReferenceById(id).getCustomer_address();
        if (addresses != null) {
            return ResponseEntity.ok(addresses);
        } else {
            return ResponseEntity.notFound().build();
        }
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
            Customer addedCustomer = customerRepository.save(newCustomer);
            return ResponseEntity.ok(addedCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> putCustomer(@PathVariable Integer id, @RequestBody Customer updatedCustomer){
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirst_name(updatedCustomer.getFirst_name());
                    customer.setLast_name(updatedCustomer.getLast_name());
                    customer.setEmail(updatedCustomer.getEmail());
                    customer.setPhone(updatedCustomer.getPhone());
                    return ResponseEntity.ok(customerRepository.save(customer));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id){
        try {
            if (customerRepository.existsById(id)) {
                customerRepository.deleteById(id);
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
