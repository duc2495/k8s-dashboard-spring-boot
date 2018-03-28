package kubernetes.client.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kubernetes.client.model.K8sService;

@Repository
public class ServiceAPI {

	private static final Logger logger = LoggerFactory.getLogger(ServiceAPI.class);

	String master = "https://k8s-master:6443/";

	Config config = new ConfigBuilder().withMasterUrl(master).build();

	public void create(K8sService k8sService, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a service
			io.fabric8.kubernetes.api.model.Service service = new ServiceBuilder().withNewMetadata()
					.withName(k8sService.getName()).endMetadata().withNewSpec().addNewPort()
					.withPort(k8sService.getPort()).endPort()
					.addToSelector(k8sService.getSelectors()).withType("NodePort").endSpec().build();
			logger.info("Created service", client.services().inNamespace(namespace).create(service));
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

	public void delete(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Delete a service
			logger.info("Delete Service", client.services().inNamespace(namespace).withName(name).delete());
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
	
	public boolean exists(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Exists Service
			io.fabric8.kubernetes.api.model.Service service = client.services().inNamespace(namespace).withName(name).get();
			if (service != null) {
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
