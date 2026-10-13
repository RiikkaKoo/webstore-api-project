package fi.metropolia.riikaka.webstore.service;

import fi.metropolia.riikaka.webstore.entity.Supplier;
import fi.metropolia.riikaka.webstore.entity.SupplierAddress;
import fi.metropolia.riikaka.webstore.repository.SupplierAddressRepository;
import fi.metropolia.riikaka.webstore.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierAddressRepository supplierAddressRepository;

    public SupplierService(SupplierRepository supplierRepository, SupplierAddressRepository supplierAddressRepository) {
        this.supplierRepository = supplierRepository;
        this.supplierAddressRepository = supplierAddressRepository;
    }

    public List<Supplier> findAllSuppliers(){
        List<Supplier> suppliers = supplierRepository.findAll();
        if (suppliers.isEmpty()) {
            return null;
        } else {
            return suppliers;
        }
    }

    public Supplier findSupplierById(int id){
        if (supplierRepository.findById(id).isEmpty()) {
            return null;
        } else {
            return supplierRepository.findById(id).get();
        }
    }

    public Supplier findSupplierByEmail(String email){
        Supplier supplier = supplierRepository.findByEmail(email);
        if (supplier != null) {
            return supplier;
        } else {
            return null;
        }
    }

    public List<SupplierAddress> findSupplierAddresses(){
        List<SupplierAddress> addresses = supplierAddressRepository.findAll();
        if (addresses.isEmpty()) {
            return null;
        } else {
            return addresses;
        }
    }

    public SupplierAddress findSupplierAddress(int id){
        SupplierAddress address = supplierAddressRepository.findBySupplier(supplierRepository.getReferenceById(id));
        if (address != null) {
            return address;
        } else {
            return null;
        }
    }

    @Transactional
    public Supplier addNewSupplier(Supplier newSupplier){
        try {
            return supplierRepository.save(newSupplier);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public SupplierAddress addNewSupplierAddress(int id, SupplierAddress newSupplierAddress){
        try {
            newSupplierAddress.setSupplier(supplierRepository.getReferenceById(id));
            return supplierAddressRepository.save(newSupplierAddress);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public Supplier updateSupplier(int id, Supplier updatedSupplier){
        try {
            Supplier supplier = supplierRepository.getReferenceById(id);
            supplier.setName(updatedSupplier.getName() != null ? updatedSupplier.getName() : supplier.getName());
            supplier.setContact_name(updatedSupplier.getContact_name() != null ? updatedSupplier.getContact_name() : supplier.getContact_name());
            supplier.setEmail(updatedSupplier.getEmail() != null ? updatedSupplier.getEmail() : supplier.getEmail());
            supplier.setPhone(updatedSupplier.getPhone() != null ? updatedSupplier.getPhone() : supplier.getPhone());
            return supplierRepository.save(supplier);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public SupplierAddress updateSupplierAddress(int id, SupplierAddress supplierAddress){
        try {
            SupplierAddress oldAddress = supplierAddressRepository.findBySupplier(supplierRepository.getReferenceById(id));
            oldAddress.setStreet_address(supplierAddress.getStreet_address() != null ? supplierAddress.getStreet_address() : oldAddress.getStreet_address());
            oldAddress.setCity(supplierAddress.getCity() != null ? supplierAddress.getCity() : oldAddress.getCity());
            oldAddress.setPostal_code(supplierAddress.getPostal_code() != null ? supplierAddress.getPostal_code() : oldAddress.getPostal_code());
            oldAddress.setCountry(supplierAddress.getCountry() != null ? supplierAddress.getCountry() : oldAddress.getCountry());
            return supplierAddressRepository.save(oldAddress);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public String removeSupplier(int id){
        try {
            if (supplierRepository.existsById(id)) {
                supplierRepository.deleteById(id);
                return "Supplier with ID " + id +" was successfully deleted";
            } else {
                return "";
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public String removeSupplierAddress(int id){
        try {
            SupplierAddress supplierAddress = supplierAddressRepository.findBySupplier(supplierRepository.getReferenceById(id));
            System.out.println(supplierAddress.getId());
            if (supplierAddress != null) {
                supplierAddressRepository.deleteById(supplierAddress.getId());
                return "Supplier Address for Supplier with ID " + id +" was successfully deleted";
            } else {
                return "";
            }
        } catch (Exception e) {
            return null;
        }
    }
}
