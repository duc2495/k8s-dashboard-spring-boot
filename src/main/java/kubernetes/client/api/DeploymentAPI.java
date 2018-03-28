package kubernetes.client.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kubernetes.client.model.Application;

@Repository
public class DeploymentAPI {

	private static final Logger logger = LoggerFactory.getLogger(ServiceAPI.class);

	String master = "https://k8s-master:6443/";

	Config config = new ConfigBuilder().withMasterUrl(master).build();

	public void deploy(Application app, String projectName) {

		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {

			io.fabric8.kubernetes.api.model.Service service = new ServiceBuilder().withNewMetadata()
					.withName(app.getName()).endMetadata().withNewSpec().addNewPort().withPort(null)
					.withNewTargetPort(8080).endPort().addToSelector("app", app.getName()).withType("NodePort")
					.endSpec().build();
			service = client.services().inNamespace(projectName).create(service);
			logger.info("Created service", service);

			Deployment deployment = new DeploymentBuilder().withNewMetadata().withName(app.getName())
					.addToLabels("app", app.getName()).endMetadata().withNewSpec().withReplicas(1).withNewSelector()
					.addToMatchLabels("app", app.getName()).endSelector().withNewTemplate().withNewMetadata()
					.addToLabels("app", app.getName()).endMetadata().withNewSpec().addNewContainer()
					.withName(app.getName()).withImage("").addNewPort().withContainerPort(8080).endPort()
					.endContainer().endSpec().endTemplate().endSpec().build();
			deployment = client.extensions().deployments().inNamespace(projectName).create(deployment);
			logger.info("Created deployment", deployment);

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

}
