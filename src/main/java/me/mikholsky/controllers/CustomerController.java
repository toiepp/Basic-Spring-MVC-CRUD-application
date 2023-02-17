package me.mikholsky.controllers;

import jakarta.validation.Valid;
import me.mikholsky.models.Customer;
import me.mikholsky.services.CustomerService;
import net.bytebuddy.matcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

	/* Не знаю почему, но когда я не указываю в скобках в @ModelAttribute
	* название сущности на прямую, то ничего не работает */
	@PostMapping("/save")
	public String saveNewCustomer(@ModelAttribute("newCustomer") @Valid Customer newCustomer,
								  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors()
						 .forEach(System.out::println);
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
			bindingResult.getAllErrors().forEach(System.out::println);
			return "update-customer";
		}

		customerService.update(customer);
		return "redirect:/customers/all-page";
	}
}
