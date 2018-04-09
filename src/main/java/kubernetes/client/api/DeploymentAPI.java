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
					.endPort().withNewResources().addToRequests("cpu", new Quantity("300m"))
					.addToRequests("memory", new Quantity("700Mi")).endResources().endContainer().endSpec().endTemplate()
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
			// Update a deployment
			logger.info("{}: {}", "Update deployment",
					client.extensions().deployments().inNamespace(namespace).withName(app.getName()).edit().editSpec()
							.editTemplate().withNewSpec().addNewContainer().withName(app.getName())
							.withImage(app.getImage()).addNewPort().withContainerPort(app.getPort()).endPort()
							.endContainer().endSpec().endTemplate().endSpec().done());
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

	public void scale(Application app, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Scale a deployment
			logger.info("{}: {}", "Scale deployment", client.extensions().deployments().inNamespace(namespace)
					.withName(app.getName()).scale(app.getPods(), true));
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

	public void rollBack(Deployment deployment, Long revision, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Update a deployment
			logger.info("{}: {}", "Roll back deployment",
					client.extensions().deployments().inNamespace(namespace)
							.withName(deployment.getMetadata().getName()).edit().editSpec().editOrNewRollbackTo()
							.withRevision(revision).endRollbackTo().endSpec().done());
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

	public void pause(Deployment deployment, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Update a deployment
			logger.info("{}: {}", "Pause deployment", client.extensions().deployments().inNamespace(namespace)
					.withName(deployment.getMetadata().getName()).edit().editSpec().withPaused(true).and().done());
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
