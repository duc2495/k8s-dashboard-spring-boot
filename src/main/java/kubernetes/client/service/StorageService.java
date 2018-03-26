package kubernetes.client.service;

import java.util.List;
import kubernetes.client.model.Storage;

public interface StorageService {
	void insert(Storage storage, String projectName);
	void delete(String storageName, String projectName);
	Storage getStorageByName(String storageName, String projectName);
	List<Storage> getStorageByProjectName(String projectName);
	boolean storageExists(String storageName, String projectName);
}
