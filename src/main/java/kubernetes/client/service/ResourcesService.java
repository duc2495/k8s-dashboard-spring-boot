package kubernetes.client.service;

import kubernetes.client.model.Application;
import kubernetes.client.model.Resources;

public interface ResourcesService {
	Resources get(Application app);
}
