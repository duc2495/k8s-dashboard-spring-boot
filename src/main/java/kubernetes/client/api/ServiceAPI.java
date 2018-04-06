package kubernetes.client.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kubernetes.client.model.Application;
import kubernetes.client.model.Template;

@Repository
public class ServiceAPI {

	private static final Logger logger = LoggerFactory.getLogger(ServiceAPI.class);

	String master = "https://k8s-master:6443/";

	Config config = new ConfigBuilder().withMasterUrl(master).build();

	public void create(Application app, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a service
			Service service = new ServiceBuilder().withNewMetadata()
					.withName(app.getName()).endMetadata().withNewSpec().addNewPort().withPort(app.getPort()).endPort()
					.addToSelector("app", app.getName()).withType("NodePort").endSpec().build();
			logger.info("{}: {}", "Created service", client.services().inNamespace(namespace).create(service));

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
			// Create a service
			Service service = new ServiceBuilder().withNewMetadata()
					.withName(template.getName()).endMetadata().withNewSpec().addNewPort().withPort(template.getPort())
					.endPort().addToSelector("app", template.getName()).endSpec().build();
			logger.info("{}: {}", "Created service", client.services().inNamespace(namespace).create(service));

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

	public Service get(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get a service
			Service service = client.services().inNamespace(namespace).withName(name).get();
			logger.info("{}: {}", "Get service", service);
			if (service != null) {
				return service;
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
		return null;
	}

	public List<Service> getAll(String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get all service
			List<Service> services = client.services().inNamespace(namespace).list().getItems();
			if (services != null) {
				return services;
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
		return null;
	}

	public void delete(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Delete a service
			logger.info("{}: {}", "Delete Service", client.services().inNamespace(namespace).withName(name).delete());
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
			io.fabric8.kubernetes.api.model.Service service = client.services().inNamespace(namespace).withName(name)
					.get();
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
