package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kubernetes.client.api.ServiceAPI;
import kubernetes.client.model.K8sService;
import kubernetes.client.service.K8sServiceService;

@Service
public class K8sServiceServiceImpl implements K8sServiceService{
	
	@Autowired
	private ServiceAPI serviceAPI;
	
	@Override
	public void create(K8sService service, String projectName) {
		serviceAPI.create(service, projectName);
		
	}

	@Override
	public void delete(String serviceName, String projectName) {
		serviceAPI.delete(serviceName, projectName);
	}

	@Override
	public K8sService getServiceByName(String serviceName, String projectName) {
		
		return null;
	}

	@Override
	public List<K8sService> getServiceByProjectName(String projectName) {
		
		return null;
	}

	@Override
	public boolean serviceExists(String serviceName, String projectName) {
		
		return false;
	}

}
