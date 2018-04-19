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

	@RequestMapping(value = "/project/{name}/apps/new", method = RequestMethod.GET)
	public String deployApplicationForm(@PathVariable String name, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Application app = new Application();
		model.addAttribute("app", app);

		return "application/deploy_app";
	}

	@RequestMapping(value = "/project/{name}/apps", method = RequestMethod.POST)
	public String deployApplication(@ModelAttribute("app") Application app, @PathVariable String name,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
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
	public String updateApplicationForm(@PathVariable String name, @PathVariable int id, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Application app = appService.getApplicationById(id, name);
		model.addAttribute("app", app);
		return "application/update_app";
	}

	@RequestMapping(value = "/project/{name}/apps/{id}", method = RequestMethod.POST)
	public String updateApplication(@PathVariable String name, @PathVariable int id, Application app, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		appService.update(app, name);
		redirectAttributes.addFlashAttribute("info", "Application updated successfully");
		return "redirect:/project/" + name + "/overview";
	}
	
	@RequestMapping(value = "/project/{name}/apps/add-storage/{id}", method = RequestMethod.GET)
	public String addStorageForm(@PathVariable String name, @PathVariable int id, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		Application app = appService.getApplicationById(id, name);
		model.addAttribute("app", app);
		return "application/update_app";
	}

	@RequestMapping(value = "/project/{name}/apps/add-storage/{id}", method = RequestMethod.POST)
	public String addStorage(@PathVariable String name, @PathVariable int id, Application app, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		//appService.update(app, name);
		redirectAttributes.addFlashAttribute("info", "Application updated successfully");
		return "redirect:/project/" + name + "/overview";
	}

	@RequestMapping(value = "/project/{name}/apps/delete/{id}", method = RequestMethod.GET)
	public String deleteApplication(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		
		if (appService.getApplicationById(id, name) == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to delete it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		appService.delete(id, name);
		redirectAttributes.addFlashAttribute("info", "Application deleted successfully");
		return "redirect:/project/" + name + "/overview";
	}
	
	@RequestMapping(value = "/project/{name}/apps/scaleup/{id}", method = RequestMethod.GET)
	public String scaleUp(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		if (appService.getApplicationById(id, name) == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to scale it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		appService.scaleUp(id, name);
		redirectAttributes.addFlashAttribute("info", "Application scale up successfully");
		return "redirect:/project/" + name + "/overview";
	}
	
	@RequestMapping(value = "/project/{name}/apps/scaledown/{id}", method = RequestMethod.GET)
	public String scaleDown(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		if (appService.getApplicationById(id, name) == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to scale it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		appService.scaleDown(id, name);
		redirectAttributes.addFlashAttribute("info", "Application scale down successfully");
		return "redirect:/project/" + name + "/overview";
	}
	
	@RequestMapping(value = "/project/{name}/apps/pause/{id}", method = RequestMethod.GET)
	public String pause(@PathVariable String name, @PathVariable int id, Model model,
			RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			model.addAttribute("error", "The Project \"" + name +"\" does not exist or you are not authorized to use it.");
			return "403";
		}
		if (appService.getApplicationById(id, name) == null) {
			model.addAttribute("error", "The Application does not exist or you are not authorized to scale it.");
			return "403";
		}
		model.addAttribute("projectName", name);
		appService.pause(id, name);
		redirectAttributes.addFlashAttribute("info", "Application pause successfully");
		return "redirect:/project/" + name + "/overview";
	}
}
