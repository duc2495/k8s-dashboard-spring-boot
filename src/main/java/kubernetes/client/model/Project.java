package kubernetes.client.model;

import java.util.List;

public class Project {
	private int projectId;
	private String projectName;
	private String description;

	private List<Application> apps;

	public Project() {

	}

	public Project(String name) {
		this.projectName = name;
	}

	public Project(int projectId, String projectName, String description) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.description = description;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Application> getApps() {
		return apps;
	}

	public void setApps(List<Application> apps) {
		this.apps = apps;
	}

}
