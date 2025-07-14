package com.example.fastapi.service;

import com.example.fastapi.dboModel.Customer;
import com.example.fastapi.repository.CustomerRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // اضافه کردن مشتری جدید
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // دریافت همه مشتری‌ها
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // دریافت مشتری بر اساس Id
    public Optional<Customer> getCustomerById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return customerRepository.findById(new ObjectId(id));
    }

    // ویرایش مشتری (ابتدا چک کن مشتری وجود دارد)
    public Optional<Customer> updateCustomer(String id, Customer updatedCustomer) {
        return getCustomerById(id).map(existingCustomer -> {
            existingCustomer.setFullName(updatedCustomer.getFullName());
            existingCustomer.setPhone(updatedCustomer.getPhone());
            existingCustomer.setNationalId(updatedCustomer.getNationalId());
            existingCustomer.setAddress(updatedCustomer.getAddress());
            return customerRepository.save(existingCustomer);
        });
    }

    // حذف مشتری بر اساس Id
    public boolean deleteCustomerById(String id) {
        if (id == null || id.isBlank()) return false;
        if (customerRepository.existsById(new ObjectId(id))) {
            customerRepository.deleteById(new ObjectId(id));
            return true;
        }
        return false;
    }
}
