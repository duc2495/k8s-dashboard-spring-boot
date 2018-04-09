package kubernetes.client.service;

import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;

public interface AutoscalerService {
	void create(HorizontalPodAutoscaler hpa, String projectName);

	void delete(String name, String projectName);

	HorizontalPodAutoscaler getHpaByName(String name, String projectName);

}
