package kubernetes.client.service;

import java.util.List;

import kubernetes.client.model.Application;
import kubernetes.client.model.Project;
import kubernetes.client.model.ResourcesRequest;

public interface ApplicationService {
	void deploy(Application app, Project project);

	void delete(int id, String projectName);

	void update(Application app, String projectName);

	void scaleUp(int id, String projectName);

	void scaleDown(int id, String projectName);
	
	void pause(int id, String projectName);
	
	void autoScaling(Application app);
	
	void proactiveAutoscaling(Application app, int predictCPU, int actualCPU);
	
	void addStorage(Application app);
	
	void editResources(Application app, ResourcesRequest resources);
	
	void deleteStorage(Application app);

	List<Application> getAll(Project project);

	Application getApplicationByName(String name, String projectName);

	Application getApplicationById(int id, String projectName);

	boolean appExists(String name, String projectName);
}
