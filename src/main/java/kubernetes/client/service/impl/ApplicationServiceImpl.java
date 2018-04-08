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
		deploymentService.create(app, project.getProjectName());
		serviceService.create(app, project.getProjectName());
		applicationMapper.insert(app, project.getProjectId());
	}

	@Override
	public void delete(int id, String projectName) {
		Application app = applicationMapper.getApplicationById(id);
		if (app != null) {
			deploymentService.delete(app.getName(), projectName);
			serviceService.delete(app.getName(), projectName);
			applicationMapper.delete(id);
		}
	}

	@Override
	public List<Application> getAll(Project project) {
		List<Application> apps = applicationMapper.getApplicationsByProjectId(project.getProjectId());
		if (apps.isEmpty()) {
			return null;
		}
		for (Application application : apps) {
			application.setDeployment(
					deploymentService.getDeploymentByName(application.getName(), project.getProjectName()));
			application.setService(serviceService.getServiceByName(application.getName(), project.getProjectName()));
		}
		return apps;
	}

	@Override
	public boolean appExists(String name, String projectName) {
		return deploymentService.deploymentExists(name, projectName);
	}

	@Override
	public Application getApplicationByName(String name, String projectName) {
		Application app = applicationMapper.getApplicationByName(name);
		if (app == null) {
			return null;
		}
		app.setDeployment(deploymentService.getDeploymentByName(name, projectName));
		app.setService(serviceService.getServiceByName(name, projectName));
		return app;
	}

	@Override
	public Application getApplicationById(int id, String projectName) {
		Application app = applicationMapper.getApplicationById(id);
		if (app == null) {
			return null;
		}
		app.setDeployment(deploymentService.getDeploymentByName(app.getName(), projectName));
		app.setService(serviceService.getServiceByName(app.getName(), projectName));
		return app;
	}

	@Override
	public void update(Application app, String projectName) {
		deploymentService.update(app, projectName);
		serviceService.update(app, projectName);
		applicationMapper.update(app);
	}

	@Override
	public void scaleUp(int id, String projectName) {
		Application app = getApplicationById(id, projectName);
		app.setPods(app.getDeployment().getSpec().getReplicas() + 1);
		deploymentService.scale(app, projectName);
		applicationMapper.updatePods(app);
	}

	@Override
	public void scaleDown(int id, String projectName) {
		Application app = getApplicationById(id, projectName);
		if (app.getDeployment().getSpec().getReplicas() > 0) {
			app.setPods(app.getDeployment().getSpec().getReplicas() - 1);
			deploymentService.scale(app, projectName);
			applicationMapper.updatePods(app);
		}
	}
}
