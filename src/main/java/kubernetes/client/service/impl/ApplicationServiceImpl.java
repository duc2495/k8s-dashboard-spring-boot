package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kubernetes.client.api.DeploymentAPI;
import kubernetes.client.api.ServiceAPI;
import kubernetes.client.mapper.ApplicationMapper;
import kubernetes.client.model.Application;
import kubernetes.client.model.Project;
import kubernetes.client.service.ApplicationService;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private ServiceAPI serviceAPI;
	@Autowired
	private DeploymentAPI deploymentAPI;
	@Autowired
	private ApplicationMapper applicationMapper;

	@Override
	public void deploy(Application app, Project project) {
		applicationMapper.insert(app, project.getProjectId());
		serviceAPI.create(app, project.getProjectName());
		deploymentAPI.create(app, project.getProjectName());
	}

	@Override
	public void delete(String name, Project project) {
		applicationMapper.delete(name);
		serviceAPI.delete(name, project.getProjectName());
		deploymentAPI.delete(name, project.getProjectName());
	}

	@Override
	public List<Application> getAll(Project project) {
		List<Application> apps = applicationMapper.getApplicationsByProjectId(project.getProjectId());
		for (Application application : apps) {
			application.setDeployment(deploymentAPI.get(application.getName(), project.getProjectName()));
			application.setService(serviceAPI.get(application.getName(), project.getProjectName()));
		}
		return apps;
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
