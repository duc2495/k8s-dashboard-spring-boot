package kubernetes.client.model;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;

public class Application {
	private String name;
	private Deployment deployment;
	private Service service;
	private Pod pod;

	public Application() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Deployment getDeployment() {
		return deployment;
	}

	public void setDeployment(Deployment deployment) {
		this.deployment = deployment;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Pod getPod() {
		return pod;
	}

	public void setPod(Pod pod) {
		this.pod = pod;
	}

}
