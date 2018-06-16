package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import kubernetes.client.api.DeploymentAPI;
import kubernetes.client.model.Application;
import kubernetes.client.model.ProactiveAutoscaler;
import kubernetes.client.model.ResourcesRequest;
import kubernetes.client.model.Template;
import kubernetes.client.model.Volume;
import kubernetes.client.service.DeploymentService;

@Transactional
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
	public void createAutoscaler(ProactiveAutoscaler pa, String appName, String projectName) {
		deploymentAPI.createAutoscaler(pa, appName, projectName);
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

	@Override
	public void update(Application app, String projectName) {
		deploymentAPI.update(app, projectName);
	}

	@Override
	public void pause(Deployment deployment) {
		deploymentAPI.pause(deployment);
	}

	@Override
	public void scale(Deployment deployment) {
		deploymentAPI.scale(deployment);
	}

	@Override
	public void rollBack(Deployment deployment, long revision) {
		deploymentAPI.rollBack(deployment, revision);
	}

	@Override
	public void addStorage(Deployment deployment, Volume volume) {
		deploymentAPI.addStorage(deployment, volume);
	}

	@Override
	public void deleteStorage(Deployment deployment) {
		deploymentAPI.deleteStorage(deployment);
	}

	@Override
	public void editResourcesRequest(Deployment deployment, ResourcesRequest resources) {
		deploymentAPI.editResources(deployment, resources);
	}
}
