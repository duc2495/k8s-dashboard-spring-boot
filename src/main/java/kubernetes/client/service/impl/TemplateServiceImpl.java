package kubernetes.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kubernetes.client.api.DeploymentAPI;
import kubernetes.client.api.PVClaimsAPI;
import kubernetes.client.api.ServiceAPI;
import kubernetes.client.mapper.ApplicationMapper;
import kubernetes.client.model.Application;
import kubernetes.client.model.Project;
import kubernetes.client.model.Storage;
import kubernetes.client.model.Template;
import kubernetes.client.service.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService{

	@Autowired
	private ServiceAPI serviceAPI;
	@Autowired
	private DeploymentAPI deploymentAPI;
	@Autowired
	private PVClaimsAPI pVClaimsAPI;
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
		app.setImage(template.getImage()+":"+template.getTag());
		app.setPort(template.getPort());
		appMapper.insert(app, project.getProjectId());
		pVClaimsAPI.create(storage, project.getProjectName());
		serviceAPI.create(template, project.getProjectName());
		deploymentAPI.create(template, project.getProjectName());
	}
}
