package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kubernetes.client.mapper.ApplicationMapper;
import kubernetes.client.model.Application;
import kubernetes.client.model.Project;
import kubernetes.client.model.ResourcesRequest;
import kubernetes.client.service.ApplicationService;
import kubernetes.client.service.AutoscalerService;
import kubernetes.client.service.DeploymentService;
import kubernetes.client.service.K8sServiceService;
import kubernetes.client.service.PodService;
import kubernetes.client.service.ProactiveAutoscalerService;
import kubernetes.client.service.ResourcesService;

@Transactional
@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private K8sServiceService serviceService;
	@Autowired
	private DeploymentService deploymentService;
	@Autowired
	private AutoscalerService autoscalerService;
	@Autowired
	private ProactiveAutoscalerService paService;
	@Autowired
	private ApplicationMapper applicationMapper;
	@Autowired
	private PodService podService;
	@Autowired
	private ResourcesService resourcesService;

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
		for (Application app : apps) {
			app.setDeployment(deploymentService.getDeploymentByName(app.getName(), project.getProjectName()));
			app.setService(serviceService.getServiceByName(app.getName(), project.getProjectName()));
			app.setListPod(podService.getAll(app.getDeployment()));
			app.setResources(resourcesService.get(app));
			app.setHpa(autoscalerService.getHpaByName(app.getName(), project.getProjectName()));
			app.setPa(paService.getPAByAppId(app.getId()));
			app.setImage(app.getDeployment().getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
			app.setPort(app.getDeployment().getSpec().getTemplate().getSpec().getContainers().get(0).getPorts().get(0)
					.getContainerPort());
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
		app.setListPod(podService.getAll(app.getDeployment()));
		app.setHpa(autoscalerService.getHpaByName(name, projectName));
		app.setPa(paService.getPAByAppId(app.getId()));
		app.setImage(app.getDeployment().getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
		app.setPort(app.getDeployment().getSpec().getTemplate().getSpec().getContainers().get(0).getPorts().get(0)
				.getContainerPort());
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
		app.setListPod(podService.getAll(app.getDeployment()));
		app.setHpa(autoscalerService.getHpaByName(app.getName(), projectName));
		app.setPa(paService.getPAByAppId(app.getId()));
		app.setImage(app.getDeployment().getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
		app.setPort(app.getDeployment().getSpec().getTemplate().getSpec().getContainers().get(0).getPorts().get(0)
				.getContainerPort());
		return app;
	}

	@Override
	public void update(Application app, String projectName) {

		deploymentService.update(app, projectName);
		app.setService(serviceService.getServiceByName(app.getName(), projectName));
		serviceService.update(app.getService(), app.getPort());
		applicationMapper.update(app);
	}

	@Override
	public void pause(int id, String projectName) {
		Application app = getApplicationById(id, projectName);
		deploymentService.pause(app.getDeployment());
	}

	@Override
	public void scaleUp(int id, String projectName) {
		Application app = getApplicationById(id, projectName);
		app.getDeployment().getSpec().setReplicas(app.getDeployment().getSpec().getReplicas() + 1);
		deploymentService.scale(app.getDeployment());
	}

	@Override
	public void scaleDown(int id, String projectName) {
		Application app = getApplicationById(id, projectName);
		if (app.getDeployment().getSpec().getReplicas() > 0) {
			app.getDeployment().getSpec().setReplicas(app.getDeployment().getSpec().getReplicas() - 1);
			deploymentService.scale(app.getDeployment());
		}
	}

	@Override
	public void autoScaling(Application app) {
		autoscalerService.create(app.getHpa(), app.getDeployment());
	}

	@Override
	public void proactiveAutoscaling(Application app, int predictCPU, int actualCPU) {
		if (app.getPa() == null) {
			return;
		}
		int pods = app.getDeployment().getSpec().getReplicas();
		if (predictCPU > app.getPa().getMaxCPU() && pods < app.getPa().getMaxReplicas()) {
			app.getDeployment().getSpec().setReplicas(app.getDeployment().getSpec().getReplicas() + 1);
			deploymentService.scale(app.getDeployment());
			System.out.println("Auto Scaling: scale up with \'" + predictCPU
					+ "%\' predict cpu usage. Number of pods after scaled: " + (pods + 1));
		} else if (actualCPU < 20 && pods > app.getPa().getMinReplicas()) {
			app.getDeployment().getSpec().setReplicas(app.getDeployment().getSpec().getReplicas() - 1);
			deploymentService.scale(app.getDeployment());
			System.out.println("Auto Scaling: scale down with \'" + predictCPU
					+ "%\' predict cpu usage. Number of pods after scaled: " + (pods - 1));
		} else {
			System.out.println(
					"Auto Scaling: no action with \'" + predictCPU + "%\' predict cpu usage. Current pods: " + pods);
		}
	}

	@Override
	public void addStorage(Application app) {
		deploymentService.addStorage(app.getDeployment(), app.getVolume());
	}

	@Override
	public void deleteStorage(Application app) {
		deploymentService.deleteStorage(app.getDeployment());
	}

	@Override
	public void editResources(Application app, ResourcesRequest resources) {
		deploymentService.editResourcesRequest(app.getDeployment(), resources);
	}
}
