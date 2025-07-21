package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dboModel.Customer;
import com.example.fastapi.service.CustomerService;
import com.example.fastapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private ResponseEntity<Object> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    private ResponseEntity<Object> invalidTokenResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    @PostMapping("/")
    public ResponseEntity<Object> addCustomer(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Customer customer) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
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
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAllCustomers(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<Customer> customers = customerService.getAllCustomers();

            if (customers == null || customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Hiçbir müşteri bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(customers);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
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
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchCustomerByName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String name) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<Customer> matchedCustomers = customerService.searchCustomersByName(name);

            if (matchedCustomers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Girilen isimle eşleşen müşteri bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(matchedCustomers);
        } finally {
            ContextHolder.clear();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCustomer(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody Customer customer) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<Customer> updatedCustomer = customerService.updateCustomer(id, customer);

            if (updatedCustomer.isPresent()) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(updatedCustomer.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Güncellenecek müşteri bulunamadı.");
            }
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCustomer(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            boolean deleted = customerService.deleteCustomerById(id);
            if (deleted) {
                return ResponseEntity.ok()
                        .body("Müşteri başarıyla silindi.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Silinecek müşteri bulunamadı.");
            }
        } finally {
            ContextHolder.clear();
        }
    }
}
