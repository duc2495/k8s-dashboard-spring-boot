package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kubernetes.client.api.NamespacesAPI;
import kubernetes.client.mapper.ProjectMapper;
import kubernetes.client.model.Project;
import kubernetes.client.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private NamespacesAPI namespaces;

	@Override
	public synchronized void insert(Project project, int customerId) {
		namespaces.create(project.getProjectName());
		projectMapper.insert(project, customerId);

	}

	@Override
	public void update(Project project) {
		projectMapper.update(project);
	}

	@Override
	public void delete(int projectId) {
		Project project = projectMapper.getProjectById(projectId);
		namespaces.delete(project.getProjectName());
		projectMapper.delete(projectId);
	}

	@Override
	public Project getProjectByName(String projectName) {
		Project obj = projectMapper.getProjectByName(projectName);
		return obj;
	}

	@Override
	public Project getProjectByName(String projectName, int customerId) {
		Project obj = projectMapper.getProjectByNameAndUserId(projectName, customerId);
		return obj;
	}

	@Override
	public Project getProjectById(int projectId) {
		Project obj = projectMapper.getProjectById(projectId);
		return obj;
	}

	@Override
	public Project getProjectById(int projectId, int customerId) {
		Project obj = projectMapper.getProjectByIdAndUserId(projectId, customerId);
		return obj;
	}

	@Override
	public List<Project> getProjectsByUserId(int customerId) {
		List<Project> projectList = projectMapper.getProjectsByUserId(customerId);
		return projectList;
	}

	@Override
	public boolean projectExists(String projectName) {
		return namespaces.exists(projectName);
	}

}
