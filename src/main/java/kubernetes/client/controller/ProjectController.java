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

import kubernetes.client.model.Project;
import kubernetes.client.service.ProjectService;
import kubernetes.client.validator.ProjectValidator;

@Controller
public class ProjectController extends BaseController{

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectValidator projectValidator;

	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public String listProjects(Model model) {
		List<Project> projects = projectService.getProjectsByUserId(getCurrentUser().getCustomer().getId());
		model.addAttribute("projects", projects);
		return "projects/projects";
	}

	@RequestMapping(value = "/projects/new", method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		Project project = new Project();
		model.addAttribute("project", project);

		return "projects/create_project";
	}

	@RequestMapping(value = "/projects", method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("project") Project project, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {

		projectValidator.validate(project, result);
		if (result.hasErrors()) {
			return "projects/create_project";
		}
		
		projectService.insert(project, getCurrentUser().getCustomer().getId());
		redirectAttributes.addFlashAttribute("info", "Project created successfully");
		return "redirect:/projects";
	}

	@RequestMapping(value = "/projects/{id}", method = RequestMethod.GET)
	public String editProjectForm(@PathVariable int id, Model model) {
		Project project = projectService.getProjectById(id, getCurrentUser().getCustomer().getId());
		if(project == null) {
			return "403";
		}
		model.addAttribute("project", project);
		return "projects/edit_project";
	}

	@RequestMapping(value = "/projects/{id}", method = RequestMethod.POST)
	public String updateProject(Project project, Model model, RedirectAttributes redirectAttributes) {
		projectService.update(project);
		redirectAttributes.addFlashAttribute("info", "Project updated successfully");
		return "redirect:/projects";
	}

	@RequestMapping(value = "/projects/{id}", method = RequestMethod.DELETE)
	public String deleteProject(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
		projectService.delete(id);
		redirectAttributes.addFlashAttribute("info", "Project deleted successfully");
		return "redirect:/projects";
	}
	
}
