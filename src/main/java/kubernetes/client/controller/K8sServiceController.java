package kubernetes.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kubernetes.client.model.K8sService;
import kubernetes.client.model.Project;
import kubernetes.client.service.K8sServiceService;
import kubernetes.client.service.ProjectService;

@Controller
public class K8sServiceController extends BaseController {
	@Autowired
	private ProjectService projectService;

	@Autowired
	private K8sServiceService serviceService;

	@RequestMapping(value = "/project/{name}/services", method = RequestMethod.GET)
	public String listService(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name, getCurrentUser().getCustomer().getId());
		if (project == null) {
			return "403";
		}
		model.addAttribute("project", project);
		List<K8sService> serviceList = serviceService.getServiceByProjectName(project.getProjectName());
		model.addAttribute("serviceList", serviceList);
		return "services/services";
	}
	/*
	 * @RequestMapping(value = "/project/{name}/services/new", method =
	 * RequestMethod.GET) public String createServiceForm(@PathVariable String name,
	 * Model model) { Project project = projectService.getProjectByName(name,
	 * getCurrentUser().getCustomer().getId()); if (project == null) { return "403";
	 * } model.addAttribute("project", project); K8sService service = new
	 * K8sService(); model.addAttribute("service", service); return
	 * "services/create_service"; }
	 * 
	 * @RequestMapping(value = "/project/{name}/services", method =
	 * RequestMethod.POST) public String
	 * createService(@Valid @ModelAttribute("service") K8sService
	 * service, @PathVariable String name, BindingResult result, Model model,
	 * RedirectAttributes redirectAttributes) { Project project =
	 * projectService.getProjectByName(name,
	 * getCurrentUser().getCustomer().getId()); if (project == null) { return "403";
	 * } model.addAttribute("project", project); serviceValidator.validate(service,
	 * result); if (result.hasErrors()) { return "services/create_service"; }
	 * 
	 * if (serviceService.serviceExists(service.getName(), name)) {
	 * result.rejectValue("name", "error.exists", new Object[] { name }, "Service "
	 * + name + " already exists"); return "services/create_service"; }
	 * serviceService.create(service, name);
	 * redirectAttributes.addFlashAttribute("info", "Service created successfully");
	 * return "redirect:/project/" + name + "/services"; }
	 */

	@RequestMapping(value = "/project/{name}/services/{serviceName}", method = RequestMethod.GET)
	public String getService(@PathVariable("name") String name, @PathVariable("serviceName") String serviceName,
			Model model) {
		Project project = projectService.getProjectByName(name, getCurrentUser().getCustomer().getId());
		if (project == null) {
			return "403";
		}
		model.addAttribute("project", project);
		K8sService service = serviceService.getServiceByName(serviceName, name);
		model.addAttribute("service", service);
		return "services/service";
	}

	@RequestMapping(value = "/project/{name}/services/{serviceName}", method = RequestMethod.DELETE)
	public String deleteService(@PathVariable("name") String name, @PathVariable("serviceName") String serviceName,
			Model model, RedirectAttributes redirectAttributes) {
		Project project = projectService.getProjectByName(name, getCurrentUser().getCustomer().getId());
		if (project == null) {
			return "403";
		}
		serviceService.delete(serviceName, name);
		redirectAttributes.addFlashAttribute("info", "Service deleted successfully");
		return "redirect:/project/" + name + "/services";
	}
}
