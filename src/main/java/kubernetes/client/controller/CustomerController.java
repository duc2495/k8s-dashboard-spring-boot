package kubernetes.client.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kubernetes.client.model.Customer;
import kubernetes.client.service.CustomerService;
import kubernetes.client.validator.CustomerValidator;

@Controller
public class CustomerController extends BaseController{

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerValidator customerValidator;
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "login";
    }

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	protected String registerForm(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	protected String register(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		customerValidator.validate(customer, result);
		if (result.hasErrors()) {
			return "register";
		}
		String password = customer.getPassword();
		String encodedPwd = passwordEncoder.encode(password);
		customer.setPassword(encodedPwd);

		customerService.createCustomer(customer);
		redirectAttributes.addFlashAttribute("info", "Customer created successfully");
		return "redirect:/login";
	}

	@RequestMapping(value = "/myAccount", method = RequestMethod.GET)
	protected String myAccount(Model model) {
		String email = getCurrentUser().getCustomer().getEmail();
		Customer customer = customerService.getCustomerByEmail(email);
		model.addAttribute("customer", customer);
		return "customer/myAccount";
	}
}
