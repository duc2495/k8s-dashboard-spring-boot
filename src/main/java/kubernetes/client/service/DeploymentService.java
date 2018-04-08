package kubernetes.client.service;

import java.util.List;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import kubernetes.client.model.Application;
import kubernetes.client.model.Template;

public interface DeploymentService {
	void create(Application app, String projectName);

	void create(Template template, String projectName);

	void delete(String name, String projectName);
	
	void update(Application app, String projectName);
	
	void scale(Application app, String projectName);
	
	void rollBack(Deployment deployment, long revision, String projectName);

	Deployment getDeploymentByName(String name, String projectName);

	List<Deployment> getDeploymentByProjectName(String projectName);

	boolean deploymentExists(String name, String projectName);
}
