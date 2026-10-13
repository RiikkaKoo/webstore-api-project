package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Customer;
import fi.metropolia.riikaka.webstore.entity.CustomerAddress;
import fi.metropolia.riikaka.webstore.entity.Order;
import fi.metropolia.riikaka.webstore.entity.SupplierAddress;
import fi.metropolia.riikaka.webstore.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(){
        List<Customer> customers = customerService.findAllCustomers();
        if (customers == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getByCustomerId(@PathVariable Integer id){
        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customer);
        }
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<CustomerAddress> getCustomerAddress(@PathVariable Integer id){
        CustomerAddress addresses = customerService.findCustomerAddress(id);
        if (addresses != null) {
            return ResponseEntity.ok(addresses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getCustomerOrders(@PathVariable Integer id){
        List<Order> orders = customerService.findCustomerOrders(id);
        if (orders != null) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getByCustomerEmail(@PathVariable String email){
        Customer customer = customerService.findCustomerByEmail(email);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Customer>postNewCustomer(@RequestBody Customer newCustomer){
        Customer addedCustomer = customerService.addNewCustomer(newCustomer);
        if(addedCustomer == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(addedCustomer);
        }
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<CustomerAddress> postCustomerAddress(@PathVariable Integer id, @RequestBody CustomerAddress newAddress){
        CustomerAddress addedAddress = customerService.addCustomerAddress(id, newAddress);
        if (addedAddress == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(addedAddress);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> putCustomer(@PathVariable Integer id, @RequestBody Customer updatedCustomer){
        Customer customer = customerService.updateCustomer(id, updatedCustomer);
        if (customer == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(customer);
        }
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<CustomerAddress> putCustomerAddress(@PathVariable Integer id, @RequestBody CustomerAddress customerAddress){
        CustomerAddress address = customerService.updateCustomerAddress(id, customerAddress);
        if (address != null) {
            return ResponseEntity.ok(address);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id){
        String message = customerService.removeCustomer(id);
        if (message != null) {
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Customer with ID " + id + " was not found");
            } else {
                return ResponseEntity.ok(message);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete customer");
        }
    }

    @DeleteMapping("/{id}/address")
    public ResponseEntity<String> deleteCustomerAddress(@PathVariable Integer id){
        String message = customerService.removeCustomerAddress(id);
        if (message != null) {
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Customer Address for Customer with ID " + id + " was not found");
            } else {
                return ResponseEntity.ok(message);
            }
        } else  {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete customer address");
        }
    }
}
