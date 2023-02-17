package me.mikholsky.controllers;

import me.mikholsky.models.Customer;
import me.mikholsky.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {
	private CustomerService customerService;

	@Autowired
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	@InitBinder
	public void init(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@GetMapping("/all-page")
	public String showListOfCustomersPage(Model model) {
		model.addAttribute("listOfCustomers", customerService.getAll());

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

	@PostMapping("/save")
	public String saveNewCustomer(@ModelAttribute Customer newCustomer) {
		customerService.save(newCustomer);
		return "redirect:/customers/all-page";
	}

	@PostMapping("/delete")
	public String deleteCustomer(@ModelAttribute("c") Customer customer) {
		customerService.delete(customer);
		return "redirect:/customers/all-page";
	}

	@PostMapping("/update")
	public String updateCustomer(@ModelAttribute("customerToUpdate") Customer customer) {
		customerService.update(customer);
		return "redirect:/customers/all-page";
	}
}
