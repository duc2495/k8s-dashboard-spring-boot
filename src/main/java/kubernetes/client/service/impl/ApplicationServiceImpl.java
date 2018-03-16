package kubernetes.client.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kubernetes.client.model.Application;
import kubernetes.client.service.ApplicationService;

@Service
public class ApplicationServiceImpl implements ApplicationService {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

	String master = "https://k8s-master:6443/";

	Config config = new ConfigBuilder().withMasterUrl(master).build();

	@Override
	public void deploy(Application app, String projectName) {

		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {

			io.fabric8.kubernetes.api.model.Service service = new ServiceBuilder().withNewMetadata()
					.withName(app.getName()).endMetadata().withNewSpec().addNewPort().withPort(8080)
					.withNewTargetPort(8080).endPort().addToSelector("app", app.getName()).withType("NodePort")
					.endSpec().build();
			service = client.services().inNamespace(projectName).create(service);
			log("Created service", service);

			Deployment deployment = new DeploymentBuilder().withNewMetadata().withName(app.getName())
					.addToLabels("app", app.getName()).endMetadata().withNewSpec().withReplicas(1).withNewSelector()
					.addToMatchLabels("app", app.getName()).endSelector().withNewTemplate().withNewMetadata()
					.addToLabels("app", app.getName()).endMetadata().withNewSpec().addNewContainer()
					.withName(app.getName()).withImage(app.getImage()).addNewPort().withContainerPort(8080).endPort()
					.endContainer().endSpec().endTemplate().endSpec().build();
			deployment = client.extensions().deployments().inNamespace(projectName).create(deployment);
			log("Created deployment", deployment);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			Throwable[] suppressed = e.getSuppressed();
			if (suppressed != null) {
				for (Throwable t : suppressed) {
					logger.error(t.getMessage(), t);
				}
			}
		}

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
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			if (client.extensions().deployments().inNamespace(projectName).withName(name).get() != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			Throwable[] suppressed = e.getSuppressed();
			if (suppressed != null) {
				for (Throwable t : suppressed) {
					logger.error(t.getMessage(), t);
				}
			}
		}
		return false;
	}

	private static void log(String action, Object obj) {
		logger.info("{}: {}", action, obj);
	}

	private static void log(String action) {
		logger.info(action);
	}
}
