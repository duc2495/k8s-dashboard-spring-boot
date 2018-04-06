package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kubernetes.client.mapper.ApplicationMapper;
import kubernetes.client.model.Application;
import kubernetes.client.model.Project;
import kubernetes.client.service.ApplicationService;
import kubernetes.client.service.DeploymentService;
import kubernetes.client.service.K8sServiceService;

@Transactional
@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private K8sServiceService serviceService;
	@Autowired
	private DeploymentService deploymentService;
	@Autowired
	private ApplicationMapper applicationMapper;

	@Override
	public void deploy(Application app, Project project) {
		applicationMapper.insert(app, project.getProjectId());
		serviceService.create(app, project.getProjectName());
		deploymentService.create(app, project.getProjectName());
	}

	@Override
	public void delete(String name, Project project) {
		applicationMapper.delete(name);
		serviceService.delete(name, project.getProjectName());
		deploymentService.delete(name, project.getProjectName());
	}

	@Override
	public List<Application> getAll(Project project) {
		List<Application> apps = applicationMapper.getApplicationsByProjectId(project.getProjectId());
		if (apps.isEmpty()) {
			return null;
		}
		for (Application application : apps) {
			application.setDeployment(deploymentService.getDeploymentByName(application.getName(), project.getProjectName()));
			application.setService(serviceService.getServiceByName(application.getName(), project.getProjectName()));
		}
		return apps;
	}

	@Override
	public boolean appExists(String name, String projectName) {
		return deploymentService.deploymentExists(name, projectName);
	}

	@Override
	public Application getByName(String name, String projectName) {
		Application app = new Application();
		app.setName(name);
		app.setDeployment(deploymentService.getDeploymentByName(name, projectName));
		app.setService(serviceService.getServiceByName(name, projectName));
		return app;
	}

}
