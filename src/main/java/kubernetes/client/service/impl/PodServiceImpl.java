package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import kubernetes.client.api.PodAPI;
import kubernetes.client.service.PodService;

@Service
public class PodServiceImpl implements PodService {
	@Autowired
	private PodAPI podAPI;

	@Override
	public List<Pod> getAll(Deployment deployment) {
		return podAPI.getAll(deployment);
	}
}
