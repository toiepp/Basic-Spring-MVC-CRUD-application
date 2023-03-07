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

	public Customer save(Customer customer) {
		customerRepository.save(customer);
		return customer;
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

	public List<Customer> getAll(String orderBy, String orderType) {
		List<Customer> queryResult = this.getAll();

		switch (orderBy) {
			case "firstName":
				queryResult = (orderType.equals("asc")) ?
					queryResult.stream()
							   .sorted((c1, c2) -> c1.getFirstName().compareToIgnoreCase(c2.getFirstName()))
							   .toList() :
					queryResult.stream()
							   .sorted((c1, c2) -> c2.getFirstName().compareToIgnoreCase(c1.getFirstName()))
							   .toList();
				break;
			case "lastName":
				queryResult = (orderType.equals("asc")) ?
					queryResult.stream()
							   .sorted((c1, c2) -> c1.getLastName().compareToIgnoreCase(c2.getLastName()))
							   .toList() :
					queryResult.stream()
							   .sorted((c1, c2) -> c2.getLastName().compareToIgnoreCase(c1.getLastName()))
							   .toList();
				break;
			case "email":
				queryResult = (orderType.equals("asc")) ?
					queryResult.stream().sorted((c1, c2) -> c1.getEmail().compareToIgnoreCase(c2.getEmail())).toList() :
					queryResult.stream().sorted((c1, c2) -> c2.getEmail().compareToIgnoreCase(c1.getEmail())).toList();
				break;
			default:
				return queryResult;
		}

		return queryResult;
	}
}
