package kubernetes.client.service;

import java.util.List;

import io.fabric8.kubernetes.api.model.Service;
import kubernetes.client.model.Application;

public interface K8sServiceService {
	void create(Application app, String projectName);

	void delete(String serviceName, String projectName);

	Service getServiceByName(String serviceName, String projectName);

	List<Service> getServiceByProjectName(String projectName);

	boolean serviceExists(String serviceName, String projectName);
}
