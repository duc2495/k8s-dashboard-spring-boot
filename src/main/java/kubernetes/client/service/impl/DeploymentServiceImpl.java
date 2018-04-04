package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import kubernetes.client.api.DeploymentAPI;
import kubernetes.client.model.Application;
import kubernetes.client.model.Template;
import kubernetes.client.service.DeploymentService;

@Service
public class DeploymentServiceImpl implements DeploymentService {

	@Autowired
	private DeploymentAPI deploymentAPI;

	@Override
	public void create(Application app, String projectName) {
		deploymentAPI.create(app, projectName);
	}

	@Override
	public void create(Template template, String projectName) {
		deploymentAPI.create(template, projectName);
	}

	@Override
	public void delete(String name, String projectName) {
		deploymentAPI.delete(name, projectName);
	}

	@Override
	public Deployment getDeploymentByName(String name, String projectName) {
		return deploymentAPI.get(name, projectName);
	}

	@Override
	public List<Deployment> getDeploymentByProjectName(String projectName) {
		return deploymentAPI.getAll(projectName);
	}

	@Override
	public boolean deploymentExists(String name, String projectName) {
		return deploymentAPI.exists(name, projectName);
	}

}
