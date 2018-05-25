package kubernetes.client.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kubernetes.client.model.ProactiveAutoscaler;
import kubernetes.client.model.Project;

@Component
public class ProactiveAutoscalerValidator implements Validator {
	@Autowired
	protected MessageSource messageSource;

	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ProactiveAutoscaler pa = (ProactiveAutoscaler) target;

		if (pa.getMinReplicas() < 1 || pa.getMinReplicas() > 5) {
			errors.rejectValue("minReplicas", "error.invalid", new Object[] { pa.getMinReplicas() },
					"The value " + pa.getMinReplicas() + "is invalid");
		}

		if ((pa.getMaxReplicas() < 1) || (pa.getMaxReplicas() > 10) || (pa.getMaxReplicas() <= pa.getMinReplicas())) {
			errors.rejectValue("maxReplicas", "error.invalid", new Object[] { pa.getMaxReplicas() },
					"The value " + pa.getMaxReplicas() + "is invalid");
		}

		if (pa.getMaxCPU() < 50 || pa.getMaxCPU() > 100) {
			errors.rejectValue("maxCPU", "error.invalid", new Object[] { pa.getMaxCPU() },
					"The value " + pa.getMaxCPU() + "is invalid");
		}
	}
}
