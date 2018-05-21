package kubernetes.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.fabric8.kubernetes.api.model.Service;
import kubernetes.client.service.K8sServiceService;
import kubernetes.client.service.ProjectService;

@Controller
public class K8sServiceController extends BaseController {

	@Autowired
	private K8sServiceService serviceService;

	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = "/project/{name}/services", method = RequestMethod.GET)
	public String listService(@PathVariable String name, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		List<Service> services = serviceService.getServiceByProjectName(name);
		model.addAttribute("services", services);
		return "services/services";
	}

	@RequestMapping(value = "/project/{name}/services/{serviceName}", method = RequestMethod.GET)
	public String getService(@PathVariable("name") String name, @PathVariable("serviceName") String serviceName,
			Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Service service = serviceService.getServiceByName(serviceName, name);
		model.addAttribute("service", service);
		return "services/service";
	}
}
