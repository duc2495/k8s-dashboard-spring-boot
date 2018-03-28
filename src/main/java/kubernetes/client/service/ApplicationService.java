package kubernetes.client.service;

import java.util.List;

import kubernetes.client.model.Application;

public interface ApplicationService {
	void deploy(Application app, String projectName);

	void delete(String name, String projectName);

	List<Application> getAll(String projectName);
	
	Application getByName(String name, String projectName);

	boolean appExists(String name, String projectName);
}
