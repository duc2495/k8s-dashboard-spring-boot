package kubernetes.client.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kubernetes.client.api.ServiceAPI;
import kubernetes.client.model.Application;
import kubernetes.client.service.ApplicationService;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Autowired
	private ServiceAPI serviceAPI;
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

	String master = "https://k8s-master:6443/";

	Config config = new ConfigBuilder().withMasterUrl(master).build();

	@Override
	public void deploy(Application app, String projectName) {

	}

	@Override
	public void delete(String name) {

	}

	@Override
	public List<Application> getAllApp(String projectName) {

		return null;
	}

	@Override
	public boolean appExists(String name, String projectName) {

		return false;
	}

}
