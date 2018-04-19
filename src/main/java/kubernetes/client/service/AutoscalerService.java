package kubernetes.client.service;

import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.extensions.Deployment;

public interface AutoscalerService {
	void create(HorizontalPodAutoscaler hpa, Deployment deployment);

	void delete(String name, String projectName);

	HorizontalPodAutoscaler getHpaByName(String name, String projectName);

}
