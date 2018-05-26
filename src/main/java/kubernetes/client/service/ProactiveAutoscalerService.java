package kubernetes.client.service;

import kubernetes.client.model.ProactiveAutoscaler;

public interface ProactiveAutoscalerService {
	void create(ProactiveAutoscaler pa, int appId, String appName, String projectName);

	void update(ProactiveAutoscaler pa, int appId);
	
	ProactiveAutoscaler getPAByAppId(int appId);

	void delete(String name, int appId);
}
