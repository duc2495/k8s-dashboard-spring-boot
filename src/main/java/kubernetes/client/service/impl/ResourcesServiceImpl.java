package kubernetes.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kubernetes.client.mapper.ResourcesMapper;
import kubernetes.client.model.Application;
import kubernetes.client.model.Resources;
import kubernetes.client.service.ResourcesService;

@Service
public class ResourcesServiceImpl implements ResourcesService {
	
	@Autowired
	private ResourcesMapper resourceMapper;
	@Override
	public Resources get(Application app) {
		Resources resources = resourceMapper.get(app);
		return resources;
	}

}
