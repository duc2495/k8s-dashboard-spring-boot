package kubernetes.client.model;

import java.util.Hashtable;

public class K8sService {
	private String name;
	private Hashtable<String, String> labels;
	private Hashtable<String, String> selectors;
	private String createdAt;
	private int port;
	private int nodePort;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Hashtable<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Hashtable<String, String> label) {
		this.labels = label;
	}

	public Hashtable<String, String> getSelectors() {
		return selectors;
	}

	public void setSelectors(Hashtable<String, String> selector) {
		this.selectors = selector;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getNodePort() {
		return nodePort;
	}

	public void setNodePort(int nodePort) {
		this.nodePort = nodePort;
	}
}
