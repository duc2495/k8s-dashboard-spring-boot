package kubernetes.client.service.impl;

import java.io.IOException;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public V2alpha1CronJob get(String name, String namespace) {
		return cronJobAPI.get(name, namespace);
	}

	@Override
	public void delete(String name, String namespace) {
		try {
			cronJobAPI.delete(name, namespace);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
