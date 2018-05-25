package kubernetes.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kubernetes.client.mapper.ProactiveAutoscalerMapper;
import kubernetes.client.model.ProactiveAutoscaler;
import kubernetes.client.service.DeploymentService;
import kubernetes.client.service.ProactiveAutoscalerService;

@Service
public class ProactiveAutoscalerServiceImpl implements ProactiveAutoscalerService {

	@Autowired
	private DeploymentService deploymentService;

	@Autowired
	private ProactiveAutoscalerMapper paMapper;

	@Override
	public void create(ProactiveAutoscaler pa, int appId, String appName, String projectName) {
		pa.setName(appName + "-" + projectName + "-tf-client");
		if (deploymentService.getDeploymentByName(pa.getName(), "default") != null) {
			deploymentService.delete(pa.getName(), "default");
		}
		deploymentService.createAutoscaler(pa, appName, projectName);
		paMapper.insert(pa, appId);
	}

	@Override
	public void update(ProactiveAutoscaler pa, int appId) {
		paMapper.update(pa, appId);
	}

	@Override
	public ProactiveAutoscaler getPAByAppId(int appId) {
		return paMapper.getPAByAppId(appId);
	}

	@Override
	public void delete(String name, int appId) {
		deploymentService.delete(name, "default");
		paMapper.delete(appId);
	}

}
