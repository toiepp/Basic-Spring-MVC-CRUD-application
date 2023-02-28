package me.mikholsky.controllers;

import jakarta.validation.Valid;
import me.mikholsky.models.Customer;
import me.mikholsky.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {
	private CustomerService customerService;
	private final Map<String, String> fieldToOrderBy;
	private final Map<String, String> orderTypes;

	{
		fieldToOrderBy = new LinkedHashMap<>();

		fieldToOrderBy.put("First name", "firstName");
		fieldToOrderBy.put("Last name", "lastName");
		fieldToOrderBy.put("Email", "email");

		orderTypes = new LinkedHashMap<>();

		orderTypes.put("Ascending", "asc");
		orderTypes.put("Descending", "desc");
	}

	@Autowired
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	@InitBinder
	public void init(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}

	/* sorting by: name, last name, email */
	@GetMapping("/all-page")
	public String showListOfCustomersPage(
		@RequestParam(value = "orderBy", required = false, defaultValue = "") String orderBy,
		@RequestParam(value = "orderType", required = false, defaultValue = "") String orderType,
		Model model) {

		List<Customer> queryResult = customerService.getAll(orderBy, orderType);

		model.addAttribute("fieldsToOrderBy", fieldToOrderBy);
		model.addAttribute("orderTypes", orderTypes);

		model.addAttribute("listOfCustomers", queryResult);

		return "list-of-customers";
	}

	@GetMapping("/new-page")
	public String showRegisterNewCustomerFormPage(Model model) {
		model.addAttribute("newCustomer", new Customer());
		return "register-new-customer-form";
	}

	@GetMapping("/update-page")
	public String showUpdatePage(@RequestParam Integer id, Model model) {
		Customer toUpdate = customerService.findById(id);
		model.addAttribute("customerToUpdate", toUpdate);
		return "update-customer";
	}

	@GetMapping("/find-page")
	public String showFindCustomerPage() {
		return "find-customer-page";
	}

	/* Не знаю почему, но когда я не указываю в скобках в @ModelAttribute
	 * название сущности на прямую, то ничего не работает */
	@PostMapping("/save")
	public String saveNewCustomer(@ModelAttribute("newCustomer") @Valid Customer newCustomer,
								  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "register-new-customer-form";
		}

		customerService.save(newCustomer);

		return "redirect:/customers/all-page";
	}

	@PostMapping("/delete")
	public String deleteCustomer(@ModelAttribute("c") Customer customer) {
		customerService.delete(customer);
		return "redirect:/customers/all-page";
	}

	@PostMapping("/update")
	public String updateCustomer(@ModelAttribute("customerToUpdate") @Valid Customer customer,
								 BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "update-customer";
		}

		customerService.update(customer);
		return "redirect:/customers/all-page";
	}

	@PostMapping("/find")
	public String findCustomers(@RequestParam(value = "firstName", required = false) String firstName,
								@RequestParam(value = "lastName", required = false) String lastName,
								@RequestParam(value = "email", required = false) String email,
								Model model) {
		List<Customer> matchedCustomers = customerService.getAll().stream()
														 .filter((customer) -> {
															 if (firstName == null) return true;
															 return customer.getFirstName()
																			.toLowerCase()
																			.contains(firstName.toLowerCase());
														 })
														 .filter((customer) -> {
															 if (lastName == null) return true;
															 return customer.getLastName()
																			.toLowerCase()
																			.contains(lastName.toLowerCase());
														 })
														 .filter(customer -> {
															 if (email == null) return true;
															 return customer.getEmail()
																			.toLowerCase()
																			.contains(email.toLowerCase());
														 }).toList();

		model.addAttribute("foundCustomers", matchedCustomers);
		return "found-customers";
	}
}
