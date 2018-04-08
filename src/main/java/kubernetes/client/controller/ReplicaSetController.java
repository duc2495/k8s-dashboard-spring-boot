package kubernetes.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.ReplicaSet;
import kubernetes.client.service.DeploymentService;
import kubernetes.client.service.ProjectService;
import kubernetes.client.service.ReplicaSetService;

@Controller
public class ReplicaSetController extends BaseController {
	@Autowired
	private DeploymentService deployService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ReplicaSetService replicaSetService;

	@RequestMapping(value = "/project/{name}/deployment/{deployName}", method = RequestMethod.GET)
	public String getDeployment(@PathVariable("name") String name, @PathVariable("deployName") String deployName,
			Model model) {

		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Deployment deployment = deployService.getDeploymentByName(deployName, name);
		if (deployment == null) {
			model.addAttribute("error", "The Deployment \"" + deployName + "\" does not exist");
		}
		List<ReplicaSet> replicaSets = replicaSetService.getAll(deployment, name);
		model.addAttribute("deployment", deployment);
		model.addAttribute("replicaSets", replicaSets);
		return "deployments/deployment";
	}

	@RequestMapping(value = "/project/{name}/deployment/{deployName}/rs/delete/{rsName}", method = RequestMethod.GET)
	public String deleteReplicaSet(@PathVariable("name") String name, @PathVariable("deployName") String deployName,
			@PathVariable("rsName") String rsName, Model model) {

		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Deployment deployment = deployService.getDeploymentByName(deployName, name);
		if (deployment == null) {
			model.addAttribute("error", "The Deployment \"" + deployName + "\" does not exist");
			return "deployment/deployment";
		}

		if (!replicaSetService.replicaSetExists(rsName, name)) {
			model.addAttribute("error", "The version \"" + rsName + "\" does not exist");
			return "deployment/deployment";
		}
		replicaSetService.delete(rsName, name);

		return "redirect:/project/" + name + "/deployment/" + "deployName";
	}
	
	@RequestMapping(value = "/project/{name}/deployment/{deployName}/rs/rollback/{rsName}", method = RequestMethod.GET)
	public String rollBackVersion(@PathVariable("name") String name, @PathVariable("deployName") String deployName,
			@PathVariable("rsName") String rsName, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("projectName", name);
		model.addAttribute("deployName", deployName);
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		Deployment deployment = deployService.getDeploymentByName(deployName, name);
		if (deployment == null) {
			model.addAttribute("error", "The Deployment \"" + deployName + "\" does not exist");
			return "deployment/deployment";
		}
		if (!replicaSetService.replicaSetExists(rsName, name)) {
			model.addAttribute("error", "The version \"" + rsName + "\" does not exist");
			return "deployment/deployment";
		}
		long revision = replicaSetService.getRevision(rsName, name);
		deployService.rollBack(deployment, revision, name);

		redirectAttributes.addFlashAttribute("name", name);
		redirectAttributes.addFlashAttribute("deployName", deployName);
		return "redirect:/project/" + name + "/deployment/" + "deployName";
	}
}
