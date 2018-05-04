package kubernetes.client.service;

import io.kubernetes.client.models.V2alpha1CronJob;
import kubernetes.client.model.Application;

public interface ProactiveAutoScalerService {
	void create(Application app);
	
	void update(Application app);

	void delete(String name, String namespace);

	V2alpha1CronJob get(String name, String namespace);
}
