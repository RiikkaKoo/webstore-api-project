package fi.metropolia.riikaka.webstore.service;

import fi.metropolia.riikaka.webstore.entity.Customer;
import fi.metropolia.riikaka.webstore.entity.CustomerAddress;
import fi.metropolia.riikaka.webstore.entity.Order;
import fi.metropolia.riikaka.webstore.repository.CustomerAddressRepository;
import fi.metropolia.riikaka.webstore.repository.CustomerRepository;
import fi.metropolia.riikaka.webstore.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerAddressRepository customerAddressRepository,
                              OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.orderRepository = orderRepository;
    }

    public List<Customer> findAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            return null;
        } else {
            return customers;
        }
    }

    public Customer findCustomerById(int id){
        if (customerRepository.findById(id).isEmpty()) {
            return null;
        } else {
            return customerRepository.findById(id).get();
        }
    }

    public CustomerAddress findCustomerAddress(int id){
        CustomerAddress addresses = customerAddressRepository.findByCustomer(customerRepository.getReferenceById(id));
        if (addresses != null) {
            return addresses;
        } else {
            return null;
        }
    }

    public List<Order> findCustomerOrders(int id){
        List<Order> orders = orderRepository.findByCustomer(customerRepository.getReferenceById(id));
        if (orders != null) {
            return orders;
        } else {
            return null;
        }
    }

    public Customer findCustomerByEmail(String email){
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null) {
            return customer;
        } else {
            return null;
        }
    }

    @Transactional
    public Customer addNewCustomer(Customer newCustomer){
        try {
            return customerRepository.save(newCustomer);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public CustomerAddress addCustomerAddress(int id, CustomerAddress newAddress){
        try {
            newAddress.setCustomer(customerRepository.getReferenceById(id));
            return customerAddressRepository.save(newAddress);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Customer updateCustomer (int id, Customer updatedCustomer){
        Customer customer = customerRepository.getReferenceById(id);
        customer.setFirst_name(updatedCustomer.getFirst_name() != null ? updatedCustomer.getFirst_name() : customer.getFirst_name());
        customer.setLast_name(updatedCustomer.getLast_name() != null ? updatedCustomer.getLast_name() : customer.getLast_name());
        customer.setEmail(updatedCustomer.getEmail() != null ? updatedCustomer.getEmail() : customer.getEmail());
        customer.setPhone(updatedCustomer.getPhone() != null ? updatedCustomer.getPhone() : customer.getPhone());
        try {
            return customerRepository.save(customer);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public CustomerAddress updateCustomerAddress(int id, CustomerAddress customerAddress){
        try {
            CustomerAddress oldAddress = customerAddressRepository.findByCustomer(customerRepository.getReferenceById(id));
            oldAddress.setStreet_address(customerAddress.getStreet_address() != null ? customerAddress.getStreet_address() : oldAddress.getStreet_address());
            oldAddress.setCity(customerAddress.getCity() != null ? customerAddress.getCity() : oldAddress.getCity());
            oldAddress.setPostal_code(customerAddress.getPostal_code() != null ? customerAddress.getPostal_code() : oldAddress.getPostal_code());
            oldAddress.setCountry(customerAddress.getCountry() != null ? customerAddress.getCountry() : oldAddress.getCountry());
            return customerAddressRepository.save(oldAddress);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public String removeCustomer(int id){
        try {
            if (customerRepository.existsById(id)) {

                Customer customer = customerRepository.getReferenceById(id);
                customerAddressRepository.deleteByCustomer(customer);

                customer.setFirst_name("-");
                customer.setLast_name("-");
                customer.setEmail("-");
                customer.setPhone("-");
                customerRepository.save(customer);
                return "SOFT DELETE: Customer with ID " + id +" was successfully set anonymous";
            } else {
                return "";
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public String removeCustomerAddress(int id){
        try {
            CustomerAddress customerAddress = customerAddressRepository.findByCustomer(customerRepository.getReferenceById(id));
            if (customerAddress != null) {
                customerAddressRepository.deleteById(customerAddress.getId());
                return "Customer Address for Customer with ID " + id +" was successfully deleted";
            } else {
                return "";
            }
        } catch (Exception e) {
            return null;
        }
    }
}
