package fi.metropolia.riikaka.webstore.controller;

import fi.metropolia.riikaka.webstore.entity.Supplier;
import fi.metropolia.riikaka.webstore.entity.SupplierAddress;
import fi.metropolia.riikaka.webstore.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getSuppliers(){
        List<Supplier> suppliers = supplierService.findAllSuppliers();
        if (suppliers == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(suppliers);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getBySupplierId(@PathVariable Integer id){
        Supplier supplier = supplierService.findSupplierById(id);
        if (supplier == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(supplier);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Supplier> getBySupplierEmail(@PathVariable String email){
        Supplier supplier = supplierService.findSupplierByEmail(email);
        if (supplier == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(supplier);
        }
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<SupplierAddress>> getSupplierAddresses(){
        List<SupplierAddress> addresses = supplierService.findSupplierAddresses();
        if (addresses == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(addresses);
        }
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<SupplierAddress> getSupplierAddress(@PathVariable Integer id){
        SupplierAddress address = supplierService.findSupplierAddress(id);
        if (address != null) {
            return ResponseEntity.ok(address);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Supplier> postNewSupplier(@RequestBody Supplier newSupplier){
        Supplier supplier = supplierService.addNewSupplier(newSupplier);
        if (supplier == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(supplier);
        }
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<SupplierAddress> postNewSupplierAddress(@PathVariable Integer id, @RequestBody SupplierAddress newSupplierAddress){
        SupplierAddress address = supplierService.addNewSupplierAddress(id, newSupplierAddress);
        if (address == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(address);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> putSupplier(@PathVariable Integer id, @RequestBody Supplier updatedSupplier){
        Supplier supplier = supplierService.updateSupplier(id, updatedSupplier);
        if (supplier == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(supplier);
        }
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<SupplierAddress> putSupplierAddress(@PathVariable Integer id, @RequestBody SupplierAddress supplierAddress){
        SupplierAddress address = supplierService.updateSupplierAddress(id, supplierAddress);
        if (address != null) {
            return ResponseEntity.ok(address);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Integer id){
        String message = supplierService.removeSupplier(id);
        if (message != null) {
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Supplier with ID " + id + " was not found");
            } else {
                return ResponseEntity.ok(message);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete supplier");
        }
    }

    @DeleteMapping("/{id}/address")
    public ResponseEntity<String> deleteSupplierAddress(@PathVariable Integer id){
        String message = supplierService.removeSupplierAddress(id);
        if (message != null) {
            if (message.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Supplier Address for Supplier with ID " + id + " was not found");
            } else {
                return ResponseEntity.ok(message);
            }
        } else  {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not delete supplier address");
        }
    }
}
