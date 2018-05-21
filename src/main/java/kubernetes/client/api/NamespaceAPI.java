package kubernetes.client.api;

import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@Repository
public class NamespaceAPI extends ConnectK8SConfiguration {

	public void create(String name) {

		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {

			// Create a namespace
			Namespace ns = new NamespaceBuilder().withNewMetadata().withName(name).endMetadata().build();
			logger.info("Create namespace", client.namespaces().create(ns));

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

	public void delete(String name) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {

			// Delete a namespace
			Namespace ns = new NamespaceBuilder().withNewMetadata().withName(name).endMetadata().build();
			logger.info("Delete namespace", client.namespaces().delete(ns));
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

	public boolean exists(String name) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {

			// Exists namespace
			if (client.namespaces().withName(name).get() != null) {
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