package kubernetes.client.model;

import io.fabric8.kubernetes.api.model.extensions.Deployment;

public class ProactiveAutoscaler {
	private int id;
	private String name;
	private int minReplicas;
	private int maxReplicas;
	private int maxCPU;
	private Deployment deployment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinReplicas() {
		return minReplicas;
	}

	public void setMinReplicas(int minReplicas) {
		this.minReplicas = minReplicas;
	}

	public int getMaxReplicas() {
		return maxReplicas;
	}

	public void setMaxReplicas(int maxReplicas) {
		this.maxReplicas = maxReplicas;
	}

	public int getMaxCPU() {
		return maxCPU;
	}

	public void setMaxCPU(int maxCPU) {
		this.maxCPU = maxCPU;
	}

	public Deployment getDeployment() {
		return deployment;
	}

	public void setDeployment(Deployment deployment) {
		this.deployment = deployment;
	}

}
