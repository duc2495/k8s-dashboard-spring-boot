package kubernetes.client.model;

import io.fabric8.kubernetes.api.model.extensions.Deployment;

import java.util.List;

import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;

public class Application {
	private int id;
	private String name;
	private String description;
	private String image;
	private int pods;
	private int port;
	private Deployment deployment;
	private Service service;
	private List<Pod> listPod;
	private HorizontalPodAutoscaler hpa;

	public Application() {
		this.pods = 1;
		this.port = 80;
	}
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPods() {
		return pods;
	}

	public void setPods(int pods) {
		this.pods = pods;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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
	public List<Pod> getListPod() {
		return listPod;
	}
	public void setListPod(List<Pod> listPod) {
		this.listPod = listPod;
	}
	public HorizontalPodAutoscaler getHpa() {
		return hpa;
	}
	public void setHpa(HorizontalPodAutoscaler hpa) {
		this.hpa = hpa;
	}

}
