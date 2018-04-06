package kubernetes.client.controller;

import java.util.List;

import javax.validation.Valid;

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
import kubernetes.client.model.Project;
import kubernetes.client.service.ApplicationService;
import kubernetes.client.service.ProjectService;
import kubernetes.client.validator.ApplicationValidator;

@Controller
public class ApplicationController extends BaseController {
	@Autowired
	private ProjectService projectService;

	@Autowired
	private ApplicationService appService;

	@Autowired
	private ApplicationValidator appValidator;

	@RequestMapping(value = "/project/{name}/overview", method = RequestMethod.GET)
	public String listApplications(@PathVariable String name, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		Project project = projectService.getProjectByName(name);
		List<Application> apps = appService.getAll(project);
		model.addAttribute("apps", apps);
		return "application/apps";
	}

	@RequestMapping(value = "/project/{name}/apps/{appName}", method = RequestMethod.GET)
	public String viewApplication(@PathVariable String name, @PathVariable String appName, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		Application app = appService.getByName(appName, name);
		model.addAttribute("app", app);
		return "application/app";
	}

	@RequestMapping(value = "/project/{name}/apps/new", method = RequestMethod.GET)
	public String deployApplicationForm(@PathVariable String name, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		Application app = new Application();
		model.addAttribute("app", app);

		return "application/deploy_app";
	}

	@RequestMapping(value = "/project/{name}/apps", method = RequestMethod.POST)
	public String deployApplication(@Valid @ModelAttribute("app") Application app, @PathVariable String name,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		appValidator.validate(app, result);
		if (appService.appExists(app.getName(), name)) {
			result.rejectValue("name", "error.exists", new Object[] { name },
					"Application " + name + " already exists");
		}
		if (result.hasErrors()) {
			return "application/deploy_app";
		}

		Project project = projectService.getProjectByName(name);
		appService.deploy(app, project);
		redirectAttributes.addFlashAttribute("info", "Application deploy successfully");
		return "redirect:/project/" + name + "/overview";
	}
	
	@RequestMapping(value = "/project/{name}/apps/{id}", method = RequestMethod.GET)
	public String editApplication(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);

		return "application/edit_app";
	}

	@RequestMapping(value = "/project/{name}/apps/{id}", method = RequestMethod.POST)
	public String updateApplication(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);

		redirectAttributes.addFlashAttribute("info", "Application updated successfully");
		return "redirect:/project/" + name + "/overview";
	}
	
	@RequestMapping(value = "/project/{name}/apps/{id}", method = RequestMethod.DELETE)
	public String deleteApplication(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		Project project = projectService.getProjectByName(name);
		appService.delete(name, project);
		redirectAttributes.addFlashAttribute("info", "Application deleted successfully");
		return "redirect:/project/" + name + "/overview";
	}
}
