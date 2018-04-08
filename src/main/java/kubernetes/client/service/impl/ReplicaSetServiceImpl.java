package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.ReplicaSet;
import kubernetes.client.api.ReplicaSetAPI;
import kubernetes.client.service.ReplicaSetService;

@Service
public class ReplicaSetServiceImpl implements ReplicaSetService{
	
	@Autowired
	private ReplicaSetAPI replicaSetAPI;
	
	@Override
	public List<ReplicaSet> getAll(Deployment deployment, String projectName) {
		return replicaSetAPI.getAll(deployment, projectName);
	}

	@Override
	public boolean replicaSetExists(String rsName, String projectName) {
		return replicaSetAPI.exists(rsName, projectName);
	}

	@Override
	public void delete(String rsName, String projectName) {
		replicaSetAPI.delete(rsName, projectName);
	}

	@Override
	public ReplicaSet getByName(String rsName, String projectName) {
		
		return replicaSetAPI.get(rsName, projectName);
	}

	@Override
	public long getRevision(String rsName, String projectName) {
		return replicaSetAPI.getRevision(rsName, projectName);
	}

}
