package kubernetes.client.service;

import java.util.List;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.extensions.Deployment;

public interface PodService {
	List<Pod> getAll(Deployment deployment);
}
