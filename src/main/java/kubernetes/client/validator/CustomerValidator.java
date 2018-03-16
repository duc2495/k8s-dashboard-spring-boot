package kubernetes.client.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import kubernetes.client.model.Customer;
import kubernetes.client.service.CustomerService;

@Component
public class CustomerValidator implements Validator
{
	@Autowired private CustomerService customerService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Customer.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Customer customer = (Customer) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "error.required", new Object[] { customer.getFullName() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.required", new Object[] { customer.getEmail() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.required", new Object[] { customer.getPassword() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "error.required", new Object[] { customer.getPasswordConfirm() });
		Customer customerByEmail = customerService.getCustomerByEmail(customer.getEmail());
		if(customerByEmail != null){
			errors.rejectValue("email", "error.email_registered");
		}

        if (customer.getPassword().length() < 8 || customer.getPassword().length() > 32) {
            errors.rejectValue("password", "error.password_length");
        }

        if (!customer.getPasswordConfirm().equals(customer.getPassword())) {
            errors.rejectValue("passwordConfirm", "error.password_conf_password_mismatch");
        }
	}
	
}
