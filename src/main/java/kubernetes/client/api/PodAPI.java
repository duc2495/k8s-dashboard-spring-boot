package kubernetes.client.api;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@Repository
public class PodAPI extends ConnectK8SConfiguration {

	public List<Pod> getAll(Deployment deployment) {

		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			List<Pod> pods = client.pods().inNamespace(deployment.getMetadata().getNamespace())
					.withLabel("app", deployment.getMetadata().getLabels().get("app")).list().getItems();
			logger.info("{}: {}", "Get Pods", pods);
			return pods;
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
		return null;
	}
}
