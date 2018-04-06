package kubernetes.client.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kubernetes.client.model.Application;
import kubernetes.client.model.Template;

@Repository
public class DeploymentAPI {

	private static final Logger logger = LoggerFactory.getLogger(ServiceAPI.class);

	String master = "https://k8s-master:6443/";

	Config config = new ConfigBuilder().withMasterUrl(master).build();

	public void create(Application app, String namespace) {

		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a deployment
			Deployment deployment = new DeploymentBuilder().withNewMetadata().withName(app.getName())
					.addToLabels("app", app.getName()).endMetadata().withNewSpec().withReplicas(app.getPods())
					.withNewSelector().addToMatchLabels("app", app.getName()).endSelector().withNewTemplate()
					.withNewMetadata().addToLabels("app", app.getName()).endMetadata().withNewSpec().addNewContainer()
					.withName(app.getName()).withImage(app.getImage()).addNewPort().withContainerPort(app.getPort())
					.endPort().withNewResources().addToLimits("cpu", new Quantity("500m"))
					.addToLimits("memory", new Quantity("1Gi")).addToRequests("cpu", new Quantity("500m"))
					.addToRequests("memory", new Quantity("1Gi")).endResources().endContainer().endSpec().endTemplate()
					.endSpec().build();
			logger.info("{}: {}", "Created deployment",
					client.extensions().deployments().inNamespace(namespace).create(deployment));

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

	public void create(Template template, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a deployment
			Deployment deployment = new DeploymentBuilder().withNewMetadata().withName(template.getName())
					.addToLabels("app", template.getName()).endMetadata().withNewSpec().withReplicas(1)
					.withNewSelector().addToMatchLabels("app", template.getName()).endSelector().withNewTemplate()
					.withNewMetadata().addToLabels("app", template.getName()).endMetadata().withNewSpec()
					.addNewContainer().withName(template.getName())
					.withImage(template.getImage() + ":" + template.getTag()).withEnv(template.getEnvs()).addNewPort()
					.withContainerPort(template.getPort()).endPort().withNewResources()
					.addToLimits("cpu", new Quantity("100m")).addToLimits("memory", new Quantity("300Mi"))
					.addToRequests("cpu", new Quantity("100m")).addToRequests("memory", new Quantity("300Mi"))
					.endResources().addNewVolumeMount().withName(template.getName())
					.withMountPath(template.getMountPath()).withReadOnly(false).endVolumeMount().endContainer()
					.addNewVolume().withName(template.getName()).withNewPersistentVolumeClaim()
					.withClaimName(template.getName()).endPersistentVolumeClaim().and().endSpec().endTemplate()
					.endSpec().build();
			logger.info("{}: {}", "Created deployment",
					client.extensions().deployments().inNamespace(namespace).create(deployment));

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

	public Deployment get(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get a deployment
			Deployment deployment = client.extensions().deployments().inNamespace(namespace).withName(name).get();
			logger.info("{}: {}", "Get deployment", deployment);
			return deployment;
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

	public List<Deployment> getAll(String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get all deployment
			List<Deployment> deployments = client.extensions().deployments().inNamespace(namespace).list().getItems();
			return deployments;
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
			// Delete a deployment
			logger.info("{}: {}", "Delete deployment",
					client.extensions().deployments().inNamespace(namespace).withName(name).delete());
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
	
	public void update(Application app, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Delete a deployment
			logger.info("{}: {}", "Update deployment",
					client.extensions().deployments().inNamespace(namespace).withName(app.getName()));
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
	
	public void scale(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Delete a deployment
			logger.info("{}: {}", "Delete deployment",
					client.extensions().deployments().inNamespace(namespace).withName(name).delete());
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
			if (client.extensions().deployments().inNamespace(namespace).withName(name).get() != null) {
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
