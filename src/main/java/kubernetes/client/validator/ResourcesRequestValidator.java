package kubernetes.client.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kubernetes.client.model.ResourcesRequest;

@Component
public class ResourcesRequestValidator implements Validator {
	@Autowired
	protected MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		return ResourcesRequest.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ResourcesRequest storage = (ResourcesRequest) target;
		String cpu = storage.getCpu();
		String memory = storage.getMemory();

		String quantityRegex = "^([+-]?[0-9.]+)([eEinumkKMGTP]*[-+]?[0-9]*)$";
		
		if (!Pattern.matches(quantityRegex, cpu)) {
			errors.rejectValue("cpu", "error.invalid", new Object[] { cpu }, "CPU " + cpu + " is invalid");
		}
		
		if (!Pattern.matches(quantityRegex, memory)) {
			errors.rejectValue("memory", "error.invalid", new Object[] { memory }, "Memory " + memory + " is invalid");
		}
	}

}
