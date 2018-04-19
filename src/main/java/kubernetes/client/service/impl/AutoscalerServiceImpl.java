package kubernetes.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import kubernetes.client.api.HorizontalPodAutoscalerAPI;
import kubernetes.client.service.AutoscalerService;

@Service
public class AutoscalerServiceImpl implements AutoscalerService {

	@Autowired
	private HorizontalPodAutoscalerAPI hpaAPI;

	@Override
	public void create(HorizontalPodAutoscaler hpa , Deployment deployment) {
		hpaAPI.create(hpa, deployment);
	}

	@Override
	public void delete(String name, String projectName) {
		hpaAPI.delete(name, projectName);
	}

	@Override
	public HorizontalPodAutoscaler getHpaByName(String name, String projectName) {
		return hpaAPI.get(name, projectName);
	}

}
