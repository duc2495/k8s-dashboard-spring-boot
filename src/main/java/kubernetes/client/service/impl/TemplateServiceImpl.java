package kubernetes.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kubernetes.client.mapper.ApplicationMapper;
import kubernetes.client.model.Application;
import kubernetes.client.model.Project;
import kubernetes.client.model.Storage;
import kubernetes.client.model.Template;
import kubernetes.client.service.DeploymentService;
import kubernetes.client.service.K8sServiceService;
import kubernetes.client.service.StorageService;
import kubernetes.client.service.TemplateService;

@Transactional
@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private K8sServiceService serviceService;
	@Autowired
	private DeploymentService deploymentService;
	@Autowired
	private StorageService storageService;
	@Autowired
	private ApplicationMapper appMapper;

	@Override
	public void deploy(Template template, Project project) {
		Storage storage = new Storage();
		storage.setName(template.getName());
		storage.setSize(template.getVolumeSize());
		Application app = new Application();
		app.setName(template.getName());
		app.setDescription("Created with Template");
		appMapper.insert(app, project.getProjectId());
		storageService.create(storage, project.getProjectName());
		serviceService.create(template, project.getProjectName());
		deploymentService.create(template, project.getProjectName());
	}

	@Override
	public boolean exists(String name, String projectName) {
		if (serviceService.serviceExists(name, projectName) || deploymentService.deploymentExists(name, projectName)
				|| storageService.storageExists(name, projectName)) {
			return true;
		}
		return false;
	}
}
