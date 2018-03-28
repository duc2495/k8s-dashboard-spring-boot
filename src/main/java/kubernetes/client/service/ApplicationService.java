package kubernetes.client.service;

import java.util.List;

import kubernetes.client.model.Application;

public interface ApplicationService {
	void deploy(Application app, String projectName);

	void delete(String name);

	List<Application> getAllApp(String projectName);

	boolean appExists(String name, String projectName);
}
