package kubernetes.client.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import kubernetes.client.model.Application;
import kubernetes.client.model.Project;

@Component
public class ApplicationValidator implements Validator {

	@Autowired
	protected MessageSource messageSource;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Application app = (Application) target;
		String name = app.getName();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "image", "error.required", new Object[] { app.getImage() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pods", "error.required", new Object[] { app.getPods() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "port", "error.required", new Object[] { app.getPort() });

		String textRegex = "^[a-z0-9]([-a-z0-9]*[a-z0-9])?$";
		
		if (!Pattern.matches(textRegex, name)) {
			errors.rejectValue("name", "error.invalid", new Object[] { name }, "Application " + name + "is invalid");
		}
		
		if (app.getPort()<1 || app.getPort()> 65535) {
			errors.rejectValue("port", "error.invalid", new Object[] { app.getPort() }, "Application " + app.getPort() + "is invalid");
		}
		
		if (app.getPods()<1 || app.getPods()> 10) {
			errors.rejectValue("pods", "error.invalid", new Object[] { app.getPods() }, "Application " + app.getPods() + "is invalid");
		}
	}

}