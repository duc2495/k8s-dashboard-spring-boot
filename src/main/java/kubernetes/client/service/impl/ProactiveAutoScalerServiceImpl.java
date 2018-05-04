package kubernetes.client.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.kubernetes.client.models.V2alpha1CronJob;
import kubernetes.client.api.CronJobAPI;
import kubernetes.client.model.Application;
import kubernetes.client.service.ProactiveAutoScalerService;

@Transactional
@Service
public class ProactiveAutoScalerServiceImpl implements ProactiveAutoScalerService {

	@Autowired
	private CronJobAPI cronJobAPI;

	@Override
	public void create(Application app) {
		try {
			cronJobAPI.create(app);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public V2alpha1CronJob get(String name, String namespace) {
		try {
			return cronJobAPI.get(name, namespace);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void delete(String name, String namespace) {
		try {
			cronJobAPI.delete(name, namespace);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Application app) {
		V2alpha1CronJob cronJob;
		Map<String, String> podLabels = app.getListPod().get(0).getMetadata().getLabels();
		String labels = "app:" + podLabels.get("app") + ",pod-template-hash:" + podLabels.get("pod-template-hash");
		try {
			cronJob = cronJobAPI.get(app.getDeployment().getMetadata().getName(), app.getDeployment().getMetadata().getNamespace());
			System.out.println(cronJob.getSpec().getJobTemplate().getSpec().getTemplate().getSpec().getContainers().get(0).getEnv().get(3).getValue()+ " Update: " + labels);
			cronJob.getSpec().getJobTemplate().getSpec().getTemplate().getSpec().getContainers().get(0).getEnv().get(3).setValue(labels);
			cronJobAPI.update(cronJob);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
