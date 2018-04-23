package kubernetes.client.api;

import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;
import io.fabric8.kubernetes.api.model.HorizontalPodAutoscalerBuilder;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@Repository
public class HorizontalPodAutoscalerAPI extends ConnectK8SConfig {

	public void create(HorizontalPodAutoscaler hpa, Deployment deployment) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a HPA
			HorizontalPodAutoscaler temp = new HorizontalPodAutoscalerBuilder().withNewMetadata()
					.withName(deployment.getMetadata().getName()).withNamespace(deployment.getMetadata().getNamespace())
					.endMetadata().withNewSpec().withNewScaleTargetRef().withApiVersion("extensions/v1beta1")
					.withKind("Deployment").withName(deployment.getMetadata().getName()).endScaleTargetRef()
					.withMinReplicas(hpa.getSpec().getMinReplicas()).withMaxReplicas(hpa.getSpec().getMaxReplicas())
					.withTargetCPUUtilizationPercentage(hpa.getSpec().getTargetCPUUtilizationPercentage()).endSpec()
					.build();
			logger.info("{}: {}", "Create HPA", client.autoscaling().horizontalPodAutoscalers()
					.inNamespace(deployment.getMetadata().getNamespace()).create(temp));
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

	public void edit(HorizontalPodAutoscaler hpa) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Update a HPA
			logger.info("{}: {}", "Edit HPA",
					client.autoscaling().horizontalPodAutoscalers().inNamespace(hpa.getMetadata().getNamespace())
							.withName(hpa.getMetadata().getName()).edit().editSpec()
							.withMinReplicas(hpa.getSpec().getMinReplicas())
							.withMaxReplicas(hpa.getSpec().getMaxReplicas())
							.withTargetCPUUtilizationPercentage(hpa.getSpec().getTargetCPUUtilizationPercentage())
							.endSpec().done());
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
			HorizontalPodAutoscaler hpa = client.autoscaling().horizontalPodAutoscalers().inNamespace(namespace)
					.withName(name).get();
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
