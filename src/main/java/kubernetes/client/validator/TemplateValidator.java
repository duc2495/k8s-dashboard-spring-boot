package kubernetes.client.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import kubernetes.client.model.Project;
import kubernetes.client.model.Template;

@Component
public class TemplateValidator implements Validator{
	@Autowired
	protected MessageSource messageSource;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Template template = (Template) target;
		String name = template.getName();
		String volumeSize = template.getVolumeSize();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.required", new Object[] { name });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tag", "error.required", new Object[] { template.getTag() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "error.required", new Object[] { template.getUsername() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.required", new Object[] { template.getPassword() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dbname", "error.required", new Object[] { template.getDbname() });
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "volumeSize", "error.required", new Object[] { volumeSize });

		String textRegex = "^[a-z0-9]([-a-z0-9]*[a-z0-9])?$";
		String pvcRegex = "^([+-]?[0-9.]+)([eEinumkKMGTP]*[-+]?[0-9]*)$";

		if (!Pattern.matches(textRegex, name)) {
			errors.rejectValue("name", "error.invalid", new Object[] { name }, "Application " + name + "is invalid");
		}
		
		if (!Pattern.matches(pvcRegex, volumeSize)) {
			errors.rejectValue("volumeSize", "error.invalid", new Object[] { volumeSize }, "Application " + volumeSize + "is invalid");
		}

	}

}
