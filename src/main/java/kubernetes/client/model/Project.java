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

	public Project(String name) {
		this.projectName = name;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Project other = (Project) obj;
		if (this.projectId != other.projectId) {
			return false;
		}
		if (this.projectName != null && other.projectName != null && !this.projectName.equals(other.projectName)) {
			return false;
		}
		if (this.description != null && other.description != null && !this.description.equals(other.description)) {
			return false;
		}
		if (this.createdAt != null && other.createdAt != null && !this.createdAt.equals(other.createdAt)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		return hash;
	}

}
