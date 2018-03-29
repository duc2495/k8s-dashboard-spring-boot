package kubernetes.client.service;

import java.util.List;

import kubernetes.client.model.Application;
import kubernetes.client.model.Project;

public interface ApplicationService {
	void deploy(Application app, Project project);

	void delete(String name, Project project);

	List<Application> getAll(Project project);
	
	Application getByName(String name, String projectName);

	boolean appExists(String name, String projectName);
}
