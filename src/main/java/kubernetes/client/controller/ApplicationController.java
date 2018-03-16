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
public class ApplicationController {
	@Autowired
	private ProjectService projectService;

	@Autowired
	private ApplicationService appService;
	
	@Autowired
	private ApplicationValidator appValidator;
	
	Project project;
	
	@RequestMapping(value = "/project/{name}/apps", method = RequestMethod.GET)
	public String listApplications(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name);
		if(project == null) {
			return "403";
		}
		model.addAttribute("project",project);
		List<Application> apps = appService.getAllApp(name);
		model.addAttribute("apps", apps);
		return "application/apps";
	}

	@RequestMapping(value = "/project/{name}/apps/new", method = RequestMethod.GET)
	public String deployApplicationForm(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name);
		model.addAttribute("project",project);
		Application app = new Application();
		model.addAttribute("app", app);

		return "application/deploy_app";
	}

	@RequestMapping(value = "/project/{name}/apps", method = RequestMethod.POST)
	public String deployApplication(@Valid @ModelAttribute("app") Application app, @PathVariable String name, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		Project project = projectService.getProjectByName(name);
		model.addAttribute("project",project);
		appValidator.validate(app, result);
		if (result.hasErrors()) {
			return "application/deploy_app";
		}
		
		if (appService.appExists(app.getName(),name)) {
			result.rejectValue("name", "error.exists", new Object[] { name },
					"Application " + name + " already exists");
			return "application/deploy_app";
		}
		appService.deploy(app, name);
		redirectAttributes.addFlashAttribute("info", "Application deploy successfully");
		return "redirect:/project/" + name + "/apps";
	}
}
