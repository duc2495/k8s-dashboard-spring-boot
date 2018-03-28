package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kubernetes.client.api.DeploymentAPI;
import kubernetes.client.api.ServiceAPI;
import kubernetes.client.model.Application;
import kubernetes.client.service.ApplicationService;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private ServiceAPI serviceAPI;
	@Autowired
	private DeploymentAPI deploymentAPI;

	@Override
	public void deploy(Application app, String projectName) {
		serviceAPI.create(app, projectName);
		deploymentAPI.create(app, projectName);
	}

	@Override
	public void delete(String name, String projectName) {
		serviceAPI.delete(name, projectName);
		deploymentAPI.delete(name, projectName);
	}

	@Override
	public List<Application> getAll(String projectName) {

		return null;
	}

	@Override
	public boolean appExists(String name, String projectName) {
		return deploymentAPI.exists(name, projectName);
	}

	@Override
	public Application getByName(String name, String projectName) {
		Application app = new Application();
		app.setName(name);
		app.setDeployment(deploymentAPI.get(name, projectName));
		app.setService(serviceAPI.get(name, projectName));
		return app;
	}

}
