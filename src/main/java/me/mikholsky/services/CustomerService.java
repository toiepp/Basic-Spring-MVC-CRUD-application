package me.mikholsky.services;

import me.mikholsky.models.Customer;
import me.mikholsky.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {
	private CustomerRepository customerRepository;

	@Autowired
	public void setCustomerRepository(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public void save(Customer customer) {
		customerRepository.save(customer);
	}

	public Customer findById(Integer id) {
		return customerRepository.findById(id);
	}

	public void delete(Integer id) {
		customerRepository.delete(id);
	}

	public void delete(Customer customer) {
		customerRepository.delete(customer);
	}

	public Customer update(Customer customer) {
		return customerRepository.update(customer);
	}

	public List<Customer> getAll() {
		return customerRepository.getAll();
	}

}
