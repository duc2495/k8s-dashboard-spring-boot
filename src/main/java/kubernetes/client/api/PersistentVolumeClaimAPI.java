package kubernetes.client.api;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import kubernetes.client.model.Storage;

@Repository
public class PersistentVolumeClaimAPI extends ConnectK8SConfiguration {

	public void create(Storage storage, String namespace) {
		try {
			// Create a pvc
			Quantity size = new Quantity(storage.getSize());
			PersistentVolumeClaim pvc = new PersistentVolumeClaimBuilder().withNewMetadata().withName(storage.getName())
					.endMetadata().withNewSpec().withAccessModes().addToAccessModes("ReadWriteMany")
					.withStorageClassName("").withNewResources().addToRequests("storage", size).endResources().endSpec()
					.build();
			logger.info("Create PVC", client.persistentVolumeClaims().inNamespace(namespace).create(pvc));
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

	public void delete(String name, String namespace) {
		try {
			// Delete a pvc
			logger.info("Delete PVC", client.persistentVolumeClaims().inNamespace(namespace).withName(name).delete());
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

	public Storage getPVClaimByName(String name, String namespace) {
		try {
			// Get a pvc
			PersistentVolumeClaim pvc = client.persistentVolumeClaims().inNamespace(namespace).withName(name).get();
			if (pvc != null) {
				Storage storage = new Storage();
				storage.setName(name);
				storage.setSize(pvc.getSpec().getResources().getRequests().get("storage").getAmount());
				storage.setStatus(pvc.getStatus().getPhase());
				storage.setCreatedAt(pvc.getMetadata().getCreationTimestamp());
				return storage;
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

	public List<Storage> getPVClaims(String namespace) {
		try {
			// Get a pvc
			List<PersistentVolumeClaim> pvcList = client.persistentVolumeClaims().inNamespace(namespace).list()
					.getItems();
			if (!pvcList.isEmpty()) {
				List<Storage> list = new ArrayList<Storage>();
				for (PersistentVolumeClaim pvc : pvcList) {
					Storage storage = new Storage();
					storage.setName(pvc.getMetadata().getName());
					storage.setSize(pvc.getSpec().getResources().getRequests().get("storage").getAmount());
					storage.setStatus(pvc.getStatus().getPhase());
					storage.setCreatedAt(pvc.getMetadata().getCreationTimestamp());
					list.add(storage);
				}
				return list;
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

	public boolean exists(String name, String namespace) {
		try {
			// Exists pvc
			PersistentVolumeClaim pvc = client.persistentVolumeClaims().inNamespace(namespace).withName(name).get();
			if (pvc != null) {
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
