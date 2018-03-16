package kubernetes.client.model;

import java.util.Date;
import java.util.List;

public class Project {
	private int projectId;
	private String projectName;
	private String description;
	private Date createdAt;

	private List<Application> apps;

	public Project() {

	}

	public Project(int projectId, String projectName, String description, Date createdAt) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.description = description;
		this.createdAt = createdAt;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<Application> getApps() {
		return apps;
	}

	public void setApps(List<Application> apps) {
		this.apps = apps;
	}

}
