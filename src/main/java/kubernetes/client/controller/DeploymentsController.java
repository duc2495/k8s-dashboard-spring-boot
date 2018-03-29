package kubernetes.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kubernetes.client.model.Project;
import kubernetes.client.service.ProjectService;

@Controller
public class DeploymentsController extends BaseController{
	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = "/project/{name}/deployments", method = RequestMethod.GET)
	public String getDeploymet(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name, getCurrentUser().getCustomer().getId());
		if (project == null) {
			return "403";
		}
		model.addAttribute("project", project);
		return "deployments/deployments";
	}
}
