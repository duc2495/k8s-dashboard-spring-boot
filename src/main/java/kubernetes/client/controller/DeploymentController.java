package kubernetes.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import kubernetes.client.service.DeploymentService;
import kubernetes.client.service.ProjectService;

@Controller
public class DeploymentController extends BaseController {
	@Autowired
	private DeploymentService deployService;

	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(value = "/project/{name}/deployments", method = RequestMethod.GET)
	public String listDeployments(@PathVariable String name, Model model) {

		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);

		List<Deployment> deployments = deployService.getDeploymentByProjectName(name);
		model.addAttribute("deployments", deployments);
		return "deployments/deployments";
	}
}
