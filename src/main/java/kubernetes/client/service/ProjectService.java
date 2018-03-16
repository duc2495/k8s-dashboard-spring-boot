package kubernetes.client.service;

import java.util.List;

import kubernetes.client.model.Project;

public interface ProjectService {

	void insert(Project project, int customerId);

	void update(Project project);

	void delete(int projectId);

	Project getProjectByName(String projectName);
	Project getProjectByName(String projectName, int customerId);

	Project getProjectById(int projectId);
	Project getProjectById(int projectId, int customerId);

	List<Project> getProjectsByUserId(int customerId);
	
	boolean projectExists(String projectName);
}
