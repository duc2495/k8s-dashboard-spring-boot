package kubernetes.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kubernetes.client.model.Application;
import kubernetes.client.model.Project;
import kubernetes.client.service.ApplicationService;
import kubernetes.client.service.ProjectService;

@Controller
public class OverviewController extends BaseController {
	@Autowired
	private ProjectService projectService;

	@Autowired
	private ApplicationService appService;

	@RequestMapping(value = "/project/{name}/overview", method = RequestMethod.GET)
	public String listApplications(@PathVariable String name, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Project project = projectService.getProjectByName(name);
		List<Application> apps = appService.getAll(project);
		int sizeApps = 0;
		if (apps != null) {
			sizeApps = apps.size();
		}
		model.addAttribute("apps", apps);
		model.addAttribute("sizeApps", sizeApps);
		return "overview/overview";
	}
}
