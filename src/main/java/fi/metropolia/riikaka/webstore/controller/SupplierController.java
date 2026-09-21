package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Supplier;
import fi.metropolia.riikaka.webstore.entity.SupplierAddress;
import fi.metropolia.riikaka.webstore.repository.SupplierAddressRepository;
import fi.metropolia.riikaka.webstore.repository.SupplierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierRepository supplierRepository;
    private final SupplierAddressRepository supplierAddressRepository;

    public SupplierController(SupplierRepository supplierRepository, SupplierAddressRepository supplierAddressRepository) {
        this.supplierRepository = supplierRepository;
        this.supplierAddressRepository = supplierAddressRepository;
    }

    // Get all Suppliers
    @GetMapping
    public ResponseEntity<List<Supplier>> getSuppliers(){

        List<Supplier> suppliers = supplierRepository.findAll();
        if (suppliers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getBySupplierId(@PathVariable Integer id){
        return supplierRepository.findById(id)
                .map(supplier -> ResponseEntity.ok(supplier))
                .orElse(ResponseEntity.notFound().build());
    }

    /*
    @GetMapping("/email/{email}")
    public ResponseEntity<Supplier> getBySupplierEmail(@PathVariable String email){
        return supplierRepository.findByEmail(email)
                .map(supplier -> ResponseEntity.ok(supplier))
                .orElse(ResponseEntity.notFound().build());
    }
    */

    @GetMapping("/addresses")
    public ResponseEntity<List<SupplierAddress>> getSupplierAddresses(){

        List<SupplierAddress> addresses = supplierAddressRepository.findAll();
        if (addresses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(addresses);
    }

    /* Voi palauttaa monta osoitetta halutaanko tätä? */
    @GetMapping("/{id}/address")
    public ResponseEntity<SupplierAddress> getSupplierAddress(@PathVariable Integer id){
        SupplierAddress address = supplierAddressRepository.findBySupplier(supplierRepository.getReferenceById(id));
        if (address != null) {
            return ResponseEntity.ok(address);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Supplier> postNewSupplier(@RequestBody Supplier newSupplier){
        try {
            Supplier addedSupplier = supplierRepository.save(newSupplier);
            return ResponseEntity.ok(addedSupplier);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<SupplierAddress> postNewSupplierAddress(@PathVariable Integer id, @RequestBody SupplierAddress newSupplierAddress){
        try {
            Supplier supplier = supplierRepository.getReferenceById(id);
            SupplierAddress supplierAddress = new SupplierAddress(supplier, newSupplierAddress.getStreet_address(),
                    newSupplierAddress.getPostal_code(), newSupplierAddress.getCity(), newSupplierAddress.getCountry());
            SupplierAddress addedAddress = supplierAddressRepository.save(supplierAddress);
            return ResponseEntity.ok(addedAddress);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> putSupplier(@PathVariable Integer id, @RequestBody Supplier updatedSupplier){
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplier.setName(updatedSupplier.getName());
                    supplier.setContact_name(updatedSupplier.getContact_name());
                    supplier.setEmail(updatedSupplier.getEmail());
                    supplier.setPhone(updatedSupplier.getPhone());
                    return ResponseEntity.ok(supplierRepository.save(supplier));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<SupplierAddress> putSupplierAddress(@PathVariable Integer id, @RequestBody SupplierAddress supplierAddress){
        SupplierAddress oldAddress = supplierAddressRepository.findBySupplier(supplierRepository.getReferenceById(id));
        if (oldAddress != null) {
            oldAddress.setStreet_address(supplierAddress.getStreet_address());
            oldAddress.setCity(supplierAddress.getCity());
            oldAddress.setPostal_code(supplierAddress.getPostal_code());
            oldAddress.setCountry(supplierAddress.getCountry());
            return ResponseEntity.ok(supplierAddressRepository.save(oldAddress));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Integer id){
        try {
            if (supplierRepository.existsById(id)) {
                supplierRepository.deleteById(id);
                return ResponseEntity.ok("Supplier with ID " + id +" was successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Supplier with ID " + id + " was not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete supplier");
        }
    }

    @DeleteMapping("/{id}/address")
    public ResponseEntity<String> deleteSupplierAddress(@PathVariable Integer id){
        try {
            SupplierAddress supplierAddress = supplierAddressRepository.findBySupplier(supplierRepository.getReferenceById(id));
            if (supplierAddress != null) {
                supplierAddressRepository.deleteById(supplierAddress.getId());
                return ResponseEntity.ok("Supplier Address for Supplier with ID " + id +" was successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Supplier Address for Supplier with ID " + id + " was not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete supplier address");
        }
    }
}
