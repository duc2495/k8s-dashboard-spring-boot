package kubernetes.client.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class ConnectK8SConfiguration {

	static final Logger logger = LoggerFactory.getLogger(ConnectK8SConfiguration.class);

	KubernetesClient client = new DefaultKubernetesClient();
}
