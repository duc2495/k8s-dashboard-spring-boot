package kubernetes.client.model;

public class Storage {
	private String name;
	private String status;
	private String size;
	private String createdAt;
	
	public Storage() {
		
	}
	
	public Storage(String name, String status, String size, String createdAt) {
		this.name = name;
		this.status = status;
		this.size = size;
		this.createdAt = createdAt;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
}
