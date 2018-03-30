package kubernetes.client.service;

import kubernetes.client.model.Project;
import kubernetes.client.model.Template;

public interface TemplateService {
	public void deploy(Template template, Project project);
}
