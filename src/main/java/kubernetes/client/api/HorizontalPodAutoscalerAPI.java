package kubernetes.client.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.HorizontalPodAutoscalerBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@Repository
public class HorizontalPodAutoscalerAPI {
	private static final Logger logger = LoggerFactory.getLogger(ServiceAPI.class);

	String master = "https://k8s-master:6443/";

	Config config = new ConfigBuilder().withMasterUrl(master).build();

	public void create(HorizontalPodAutoscaler hpa1, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a HPA
			HorizontalPodAutoscaler hpa = new HorizontalPodAutoscalerBuilder().withNewMetadata()
					.withName("survey-app").withNamespace("test").endMetadata().withNewSpec()
					.withNewScaleTargetRef().withApiVersion("extensions/v1beta1").withKind("Deployment").withName("survey-app")
					.endScaleTargetRef().withMinReplicas(1).withMaxReplicas(3).withTargetCPUUtilizationPercentage(50)
					.endSpec().build();
			logger.info("{}: {}", "Create HPA",
					client.autoscaling().horizontalPodAutoscalers().inNamespace("test").create(hpa));
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

	public HorizontalPodAutoscaler get(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get a HPA
			HorizontalPodAutoscaler hpa = client.autoscaling().horizontalPodAutoscalers().inNamespace(namespace).withName(name).get();
			logger.info("{}: {}", "Get HPA", hpa);
			return hpa;
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
	
	public void delete(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Delete a HPA
			logger.info("{}: {}", "Delete HPA",
					client.autoscaling().horizontalPodAutoscalers().inNamespace(namespace).withName(name).delete());
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
}
