package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kubernetes.client.api.ServiceAPI;
import kubernetes.client.model.Application;
import kubernetes.client.service.K8sServiceService;

@Service
public class K8sServiceServiceImpl implements K8sServiceService{
	
	@Autowired
	private ServiceAPI serviceAPI;
	
	@Override
	public void create(Application app, String projectName) {
		serviceAPI.create(app, projectName);
		
	}

	@Override
	public void delete(String serviceName, String projectName) {
		serviceAPI.delete(serviceName, projectName);
	}

	@Override
	public io.fabric8.kubernetes.api.model.Service getServiceByName(String serviceName, String projectName) {
		return null;
	}

	@Override
	public List<io.fabric8.kubernetes.api.model.Service> getServiceByProjectName(String projectName) {
		
		return null;
	}

	@Override
	public boolean serviceExists(String serviceName, String projectName) {
		
		return serviceAPI.exists(serviceName, projectName);
	}

}