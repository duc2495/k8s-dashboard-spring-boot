package kubernetes.client.model;

import java.util.Hashtable;

public class Application {
	private String name;
	private String image;
	private String tag;
	private String createdAt;
	private int pods;
	private String version;


	private Hashtable<String, String> envList;
	private Hashtable<String, String> labelList;
	
	public Application() {	
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Hashtable<String, String> getEnvList() {
		return envList;
	}

	public void setEnvList(Hashtable<String, String> envList) {
		this.envList = envList;
	}

	public Hashtable<String, String> getLabelList() {
		return labelList;
	}

	public void setLabelList(Hashtable<String, String> labelList) {
		this.labelList = labelList;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getPods() {
		return pods;
	}

	public void setPods(int pods) {
		this.pods = pods;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
