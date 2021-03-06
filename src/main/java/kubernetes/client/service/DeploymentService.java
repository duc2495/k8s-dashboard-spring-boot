package kubernetes.client.service;

import java.util.List;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import kubernetes.client.model.Application;
import kubernetes.client.model.ProactiveAutoscaler;
import kubernetes.client.model.ResourcesRequest;
import kubernetes.client.model.Template;
import kubernetes.client.model.Volume;

public interface DeploymentService {
	void create(Application app, String projectName);

	void create(Template template, String projectName);

	void createAutoscaler(ProactiveAutoscaler pa, String appName, String projectName);

	void delete(String name, String projectName);

	void update(Application app, String projectName);

	void scale(Deployment deployment);

	void pause(Deployment deployment);

	void rollBack(Deployment deployment, long revision);

	void addStorage(Deployment deployment, Volume volume);

	void deleteStorage(Deployment deployment);
	
	void editResourcesRequest(Deployment deployment, ResourcesRequest resources);

	Deployment getDeploymentByName(String name, String projectName);

	List<Deployment> getDeploymentByProjectName(String projectName);

	boolean deploymentExists(String name, String projectName);
}
