package kubernetes.client.service;

import java.util.List;

import kubernetes.client.model.K8sService;

public interface K8sServiceService {
	void create(K8sService service, String projectName);

	void delete(String serviceName, String projectName);

	K8sService getServiceByName(String serviceName, String projectName);

	List<K8sService> getServiceByProjectName(String projectName);

	boolean serviceExists(String serviceName, String projectName);
}
