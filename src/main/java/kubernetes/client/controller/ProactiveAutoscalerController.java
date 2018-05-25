package kubernetes.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kubernetes.client.model.Application;
import kubernetes.client.model.ProactiveAutoscaler;
import kubernetes.client.service.ApplicationService;
import kubernetes.client.service.ProactiveAutoscalerService;
import kubernetes.client.service.ProjectService;
import kubernetes.client.validator.ProactiveAutoscalerValidator;

@Controller
public class ProactiveAutoscalerController extends BaseController {
	@Autowired
	private ProjectService projectService;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private ProactiveAutoscalerService paService;

	@Autowired
	private ProactiveAutoscalerValidator paValidator;

	@RequestMapping(value = "/admin/project/{namespace}/app/{name}/current/{actualCPU}/future/{predictCPU}", method = RequestMethod.GET)
	public String autoScaling(@ModelAttribute("namespace") @PathVariable String namespace,
			@ModelAttribute("name") @PathVariable String name, @ModelAttribute("actualCPU") @PathVariable int actualCPU,
			@ModelAttribute("predictCPU") @PathVariable int predictCPU, Model model) {
		Application app = appService.getApplicationByName(name, namespace);
		if (app == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to scale it.");
			return "403";
		}
		appService.proactiveAutoscaling(app, predictCPU, actualCPU);
		return "admin/autoscaler";
	}

	@RequestMapping(value = "/project/{name}/app/{appId}/pa/new", method = RequestMethod.GET)
	public String createAutoscalerForm(@PathVariable String name, @PathVariable int appId, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		if (appService.getApplicationById(appId, name) == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to access.");
			return "403";
		}
		model.addAttribute("appId", appId);
		ProactiveAutoscaler pa = new ProactiveAutoscaler();
		model.addAttribute("pa", pa);
		return "proactive/create_autoscaler";
	}

	@RequestMapping(value = "/project/{name}/app/{appId}/pa", method = RequestMethod.POST)
	public String createAutoscaler(@ModelAttribute("pa") ProactiveAutoscaler pa, @PathVariable String name,
			@PathVariable int appId, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		paValidator.validate(pa, result);
		model.addAttribute("projectName", name);
		if (result.hasErrors()) {
			return "proactive/create_autoscaler";
		}
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		Application app = appService.getApplicationById(appId, name);
		if (app == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to access.");
			return "403";
		}
		paService.create(pa, appId, app.getName(), name);
		redirectAttributes.addFlashAttribute("info", "Proactive autoscaler successfully created.");
		return "redirect:/project/" + name + "/overview";
	}

	@RequestMapping(value = "/project/{name}/app/{appId}/pa", method = RequestMethod.GET)
	public String editAutoscalerForm(@PathVariable String name, @PathVariable int appId, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		if (appService.getApplicationById(appId, name) == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to access.");
			return "403";
		}
		model.addAttribute("appId", appId);
		ProactiveAutoscaler pa = paService.getPAByAppId(appId);
		model.addAttribute("pa", pa);
		return "proactive/edit_autoscaler";
	}

	@RequestMapping(value = "/project/{name}/app/{appId}/pa", method = RequestMethod.PUT)
	public String editAutoscaler(@ModelAttribute("pa") ProactiveAutoscaler pa, @PathVariable String name,
			@PathVariable int appId, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		paValidator.validate(pa, result);
		model.addAttribute("projectName", name);
		if (result.hasErrors()) {
			return "proactive/edit_autoscaler";
		}
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		Application app = appService.getApplicationById(appId, name);
		if (app == null) {
			model.addAttribute("error", "The Application does not exist or  you are not authorized to access.");
			return "403";
		}
		paService.update(pa, appId);
		redirectAttributes.addFlashAttribute("info", "Proactive autoscaler successfully edited.");
		return "redirect:/project/" + name + "/overview";
	}

	@RequestMapping(value = "/project/{name}/app/{appId}/pa/delete", method = RequestMethod.GET)
	public String deleteAutoscaler(@PathVariable String name, @PathVariable int appId, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error",
					"The Project \"" + name + "\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Application app = appService.getApplicationById(appId, name);
		if (app == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to access.");
			return "403";
		}
		if (app.getPa() == null) {
			model.addAttribute("error", "Proactive autoscaler is not enabled or you are not authorized to access.");
			return "403";
		}
		paService.delete(app.getPa().getName(), appId);
		redirectAttributes.addFlashAttribute("info", "Proactive autoscaler successfully deleted.");
		return "redirect:/project/" + name + "/overview";
	}

}
