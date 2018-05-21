package kubernetes.client.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import kubernetes.client.model.Storage;

@Component
public class StorageValidator implements Validator {
	@Autowired
	protected MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		return Storage.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Storage storage = (Storage) target;
		String name = storage.getName();
		String size = storage.getSize();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.required", new Object[] { name });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "size", "error.required", new Object[] { size });
		String textRegex = "^[a-z0-9]([-a-z0-9]*[a-z0-9])?$";
		String pvcRegex = "^([+-]?[0-9.]+)([eEinumkKMGTP]*[-+]?[0-9]*)$";
		
		if (!Pattern.matches(textRegex, name)) {
			errors.rejectValue("name", "error.invalid", new Object[] { name }, "Storage Name" + name + "is invalid");
		}
		
		if (!Pattern.matches(pvcRegex, size)) {
			errors.rejectValue("size", "error.invalid", new Object[] { size }, "Volume Capacity " + size + "is invalid");
		}
	}
}
