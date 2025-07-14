package com.example.fastapi.controller;

import com.example.fastapi.dboModel.Customer;
import com.example.fastapi.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Yeni müşteri ekle
    @PostMapping("/")
    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
        // Check for duplicate (örnek: fullName + phone eşleşmesiyle)
        List<Customer> existingCustomers = customerService.getAllCustomers();
        boolean exists = existingCustomers.stream()
                .anyMatch(c -> c.getFullName().equals(customer.getFullName()) &&
                        c.getPhone().equals(customer.getPhone()));
        if (exists) {
            return ResponseEntity.badRequest()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Aynı isim ve telefon numarasıyla müşteri zaten mevcut.");
        }

        Customer savedCustomer = customerService.addCustomer(customer);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(savedCustomer);
    }

    // Tüm müşterileri getir
    @GetMapping("/")
    public ResponseEntity<Object> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();

        if (customers == null || customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Hiçbir müşteri bulunamadı.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(customers);
    }

    // Belirli müşteri ID ile getir
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable String id) {
        Optional<Customer> customerOpt = customerService.getCustomerById(id);

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(customer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Müşteri bulunamadı.");
        }
    }


    // Müşteri güncelle
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        Optional<Customer> updatedCustomer = customerService.updateCustomer(id, customer);

        if (updatedCustomer.isPresent()) {
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(updatedCustomer.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Güncellenecek müşteri bulunamadı.");
        }
    }

    // Müşteri sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable String id) {
        boolean deleted = customerService.deleteCustomerById(id);
        if (deleted) {
            return ResponseEntity.ok()
                    .body("Müşteri başarıyla silindi.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Silinecek müşteri bulunamadı.");
        }
    }
}
