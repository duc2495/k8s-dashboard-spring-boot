package kubernetes.client.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kubernetes.client.model.Application;
import kubernetes.client.model.Template;

@Repository
public class DeploymentAPI extends ConnectK8SConfiguration {
	
	public void create(Application app, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a deployment
			Deployment deployment = new DeploymentBuilder().withNewMetadata().withName(app.getName())
					.addToLabels("app", app.getName()).endMetadata().withNewSpec().withReplicas(app.getPods())
					.withNewSelector().addToMatchLabels("app", app.getName()).endSelector().withNewTemplate()
					.withNewMetadata().addToLabels("app", app.getName()).endMetadata().withNewSpec().addNewContainer()
					.withName(app.getName()).withImage(app.getImage()).addNewPort().withContainerPort(app.getPort())
					.endPort().withNewResources().addToRequests("cpu", new Quantity("500m"))
					.addToRequests("memory", new Quantity("1Gi")).endResources().endContainer().endSpec()
					.endTemplate().endSpec().build();
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
	
	public void createAutoscaler(Application app) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a deployment
			String namespace = "default";
			String name = app.getName() + "-" + app.getDeployment().getMetadata().getNamespace() + "-tf-client";
			String image = "duc2495/tf-serving-client:latest";

			List<EnvVar> envs = new ArrayList<EnvVar>();
			envs.add(new EnvVar("TF_SERVER", "192.168.5.10:30900", null));
			envs.add(new EnvVar("INFLUX_SERVER","192.168.5.10:30086", null));
			envs.add(new EnvVar("WEB_SERVER", "192.168.5.10:8080", null));
			envs.add(new EnvVar("NAMESPACE", app.getDeployment().getMetadata().getNamespace(), null));
			envs.add(new EnvVar("NAME",app.getName(), null));
			envs.add(new EnvVar("MODEL_NAME", "cpu", null));
			String command = "bin/sh";
			List<String> args = new ArrayList<String>();
			args.add("-c");
			args.add("while true ; do ./run_client.sh & sleep 60; done");
			Deployment deployment = new DeploymentBuilder().withNewMetadata().withName(name)
					.addToLabels("app", name).endMetadata().withNewSpec()
					.withNewTemplate()
					.withNewMetadata().addToLabels("app", name).endMetadata().withNewSpec().addNewContainer()
					.withName(name).withImage(image).withEnv(envs).withCommand(command).withArgs(args).endContainer().endSpec()
					.endTemplate().endSpec().build();
			logger.info("{}: {}", "Created autoscaler job",
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
							.editTemplate().editOrNewSpec().editFirstContainer().withName(app.getName())
							.withImage(app.getImage()).editFirstPort().withContainerPort(app.getPort()).endPort()
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
	
	public void addStorage(Deployment deploy) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Add a Storage
			logger.info("{}: {}", "Add a Storage",
					client.extensions().deployments().inNamespace(deploy.getMetadata().getNamespace())
							.withName(deploy.getMetadata().getName()).edit().editSpec().editTemplate().editOrNewSpec()
							.editFirstContainer().addNewVolumeMount().withName("").withMountPath("").withReadOnly(false)
							.endVolumeMount().endContainer().addNewVolume().withName("").withNewPersistentVolumeClaim()
							.withClaimName("").endPersistentVolumeClaim().and().endSpec().endTemplate().endSpec()
							.done());
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

	public void scale(Deployment deploy) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Scale a deployment
			logger.info("{}: {}", "Scale deployment",
					client.extensions().deployments().inNamespace(deploy.getMetadata().getNamespace())
							.withName(deploy.getMetadata().getName()).scale(deploy.getSpec().getReplicas(), true));
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

	public void rollBack(Deployment deployment, Long revision) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Update a deployment
			logger.info("{}: {}", "Roll back deployment",
					client.extensions().deployments().inNamespace(deployment.getMetadata().getNamespace())
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

	public void pause(Deployment deployment) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Pause a deployment
			logger.info("{}: {}", "Pause deployment",
					client.extensions().deployments().inNamespace(deployment.getMetadata().getNamespace())
							.withName(deployment.getMetadata().getName()).edit().editSpec().withPaused(true).and()
							.done());
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
