package kubernetes.client.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kubernetes.client.api.PVClaimsAPI;
import kubernetes.client.model.Storage;
import kubernetes.client.service.StorageService;

@Transactional
@Service
public class StorageServiceImpl implements StorageService{

	@Autowired
	private PVClaimsAPI pvcAPI;
	
	@Override
	public void create(Storage storage, String projectName) {
		pvcAPI.create(storage, projectName);
	}

	@Override
	public void delete(String storageName, String projectName) {
		pvcAPI.delete(storageName, projectName);
	}

	@Override
	public Storage getStorageByName(String storageName, String projectName) {
		
		return pvcAPI.getPVClaimByName(storageName, projectName);
	}

	@Override
	public List<Storage> getStorageByProjectName(String projectName){
		return pvcAPI.getPVClaims(projectName);
	}
	@Override
	public boolean storageExists(String storageName, String projectName) {
		return pvcAPI.exists(storageName, projectName);
	}

}
