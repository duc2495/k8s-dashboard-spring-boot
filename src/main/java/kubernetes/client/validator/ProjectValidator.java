package kubernetes.client.validator;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import kubernetes.client.model.Project;
import kubernetes.client.service.ProjectService;

@Component
public class ProjectValidator implements Validator {

	@Autowired
	protected MessageSource messageSource;
	@Autowired
	private ProjectService projectService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Project project = (Project) target;
		String name = project.getProjectName();
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "projectName", "error.required", new Object[] { name });
		String textRegex = "^[a-z0-9]([-a-z0-9]*[a-z0-9])?$";

		if (!Pattern.matches(textRegex, name)) {
			errors.rejectValue("projectName", "error.invalid", new Object[] { name }, "Project " + name + "is invalid");
		}

		if (projectService.projectExists(name)) {
			errors.rejectValue("projectName", "error.exists", new Object[] { name },
					"Project " + name + " already exists");
		}
	}

}
