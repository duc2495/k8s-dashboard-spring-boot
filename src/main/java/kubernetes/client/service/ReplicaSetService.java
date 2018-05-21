package kubernetes.client.service;

import java.util.List;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.ReplicaSet;

public interface ReplicaSetService {
	List<ReplicaSet> getAll(Deployment deployment);

	void delete(String rsName, String projectName);

	ReplicaSet getByName(String rsName, String projectName);

	long getRevision(String rsName, String projectName);

	boolean replicaSetExists(String rsName, String projectName);
}
