package kubernetes.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kubernetes.client.model.Application;
import kubernetes.client.service.ApplicationService;
import kubernetes.client.service.ProjectService;

@Controller
public class ProactiveAutoscalerController extends BaseController {
	@Autowired
	private ProjectService projectService;

	@Autowired
	private ApplicationService appService;

	@RequestMapping(value = "/project/{name}/app/{id}/autoscaler/create", method = RequestMethod.GET)
	public String createAutoscaler(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		Application app = appService.getApplicationById(id, name);
		if (app == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to scale it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		if (!app.isProAutoscaler()) {
			appService.proAutoScaling(app);
			redirectAttributes.addFlashAttribute("info", "Proactive autoscaler successfully created.");
		} else {
			redirectAttributes.addFlashAttribute("info", "Can't create action. Proactive autoscaler does exist.");
		}
		return "redirect:/project/" + name + "/overview";
	}

	@RequestMapping(value = "/project/{name}/app/{id}/autoscaler/delete", method = RequestMethod.GET)
	public String deleteAutoscaler(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		Application app = appService.getApplicationById(id, name);
		if (app == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to scale it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		if (app.isProAutoscaler()) {
			appService.deleteProAutoscaler(app);
			redirectAttributes.addFlashAttribute("info", "Proactive autoscaler successfully deleted.");
		} else {
			redirectAttributes.addFlashAttribute("info", "Can't delete action. Proactive autoscaler does not exist.");
		}
		return "redirect:/project/" + name + "/overview";
	}

	@RequestMapping(value = "/admin/project/{name}/app/{id}/current/{currentCPU}/future/{futureCPU}", method = RequestMethod.GET)
	public String autoScaling(@ModelAttribute("name") @PathVariable String name,
			@ModelAttribute("id") @PathVariable int id, @ModelAttribute("currentCPU") @PathVariable int currentCPU,
			@ModelAttribute("futureCPU") @PathVariable int futureCPU, Model model) {
		Application app = appService.getApplicationById(id, name);
		if (app == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to scale it.");
			return "403";
		}
		int pods = app.getDeployment().getSpec().getReplicas();
		if (currentCPU > 80 || futureCPU > 80) {
			appService.scaleUp(id, name);
			System.out.println(
					"Auto Scaling: scale up with \'" + futureCPU + "%\' future cpu usage(" + currentCPU + "% current). Number of pods after scaled: " + (pods + 1));
			model.addAttribute("info", "Auto Scaling: scale up with " + futureCPU + "% future cpu usage.");
		} else if (((currentCPU < 30 && futureCPU < 50 ) || ( currentCPU < 50 && futureCPU < 30)) && pods > 1) {
			appService.scaleDown(id, name);
			System.out.println("Auto Scaling: scale down with \'" + futureCPU + "%\' future cpu usage (" + currentCPU
					+ "% current). Number of pods after scaled: " + (pods - 1));
			model.addAttribute("info", "Auto Scaling: scale down with " + futureCPU + "% future cpu usage.");
		} else {
			System.out.println(
					"Auto Scaling: no action with \'" + futureCPU + "%\' future cpu usage (" + currentCPU
					+ "% current). Curent pods: " + pods);
			model.addAttribute("info", "Auto Scaling: no action with " + futureCPU + "% future cpu usage.");
		}
		return "admin/autoscaler";
	}
}
