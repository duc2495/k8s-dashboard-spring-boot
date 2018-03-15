package kubernetes.client.model;

import java.util.List;

public class User {
	
	private Long userId;
    private String userName;
    private String encrytedPassword;
    private String email;
    private List<Project> projects;
    
    public User(){
    	
    }
    
    public User(String userName, String encrytedPassword, String email, List<Project> projects) {
        this.userName = userName;
        this.encrytedPassword = encrytedPassword;
        this.email = email;
        this.projects = projects;
    }
    

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEncrytedPassword() {
		return encrytedPassword;
	}
	public void setEncrytedPassword(String encrytedPassword) {
		this.encrytedPassword = encrytedPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
    
}
