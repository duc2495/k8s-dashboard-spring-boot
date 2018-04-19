package kubernetes.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.fabric8.kubernetes.api.model.HorizontalPodAutoscaler;
import kubernetes.client.model.Application;
import kubernetes.client.service.ApplicationService;
import kubernetes.client.service.ProjectService;

@Controller
public class AutoscalerController extends BaseController {
	@Autowired
	private ProjectService projectService;

	@Autowired
	private ApplicationService appService;

	@RequestMapping(value = "/project/{name}/app/{appName}/hpa/new", method = RequestMethod.GET)
	public String createAutoscalerForm(@PathVariable String name, @PathVariable String appName, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		if (appService.getApplicationByName(appName, name) == null) {
			model.addAttribute("error",
					"The Application  \"" + appName + "\"  does not exist or you are not authorized to scale it.");
			return "403";
		}
		model.addAttribute("appName", appName);
		HorizontalPodAutoscaler hpa = new HorizontalPodAutoscaler();
		model.addAttribute("hpa", hpa);
		return "autoscaler/create_autoscaler";
	}

	@RequestMapping(value = "/project/{name}/app/{appName}/hpa", method = RequestMethod.POST)
	public String createAutoscaler(@ModelAttribute("hpa") HorizontalPodAutoscaler hpa, @PathVariable String name,
			@PathVariable String appName, Model model, RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Application app = appService.getApplicationByName(appName, name);
		if (app == null) {
			model.addAttribute("error",
					"The Application  \"" + appName + "\"  does not exist or you are not authorized to scale it.");
			return "403";
		}
		app.setHpa(hpa);
		appService.autoScaling(app);
		redirectAttributes.addFlashAttribute("info", "Horizontal pod autoscaler successfully created.");
		return "redirect:/project/" + name + "/overview";
	}
}
