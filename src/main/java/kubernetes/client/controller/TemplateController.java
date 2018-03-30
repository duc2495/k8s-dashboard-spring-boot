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

import kubernetes.client.model.MysqlTemplate;
import kubernetes.client.model.PostgresTemplate;
import kubernetes.client.model.Project;
import kubernetes.client.model.Template;
import kubernetes.client.service.ProjectService;
import kubernetes.client.service.TemplateService;

@Controller
public class TemplateController {
	@Autowired
	private TemplateService templateService;
	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = "/project/{name}/templates", method = RequestMethod.GET)
	public String listTemplate(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name);
		model.addAttribute("project",project);
		if(project == null) {
			return "403";
		}
		return "template/template";
	}
	
	@RequestMapping(value = "/project/{name}/template/postgres/new", method = RequestMethod.GET)
	public String deployPostgresTemplateForm(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name);
		model.addAttribute("project",project);
		Template template = new PostgresTemplate();
		model.addAttribute("template", template);

		return "template/deploy_postgres";
	}
	
	@RequestMapping(value = "/project/{name}/template/postgres", method = RequestMethod.POST)
	public String deployPostgresTemplate(@ModelAttribute("template") PostgresTemplate template, @PathVariable String name, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		Project project = projectService.getProjectByName(name);
		model.addAttribute("project",project);
		templateService.deploy(template, project);
		redirectAttributes.addFlashAttribute("info", "Application deploy successfully");
		return "redirect:/project/" + name + "/overview";
	}
	
	@RequestMapping(value = "/project/{name}/template/mysql/new", method = RequestMethod.GET)
	public String deployMySqlTemplateForm(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name);
		model.addAttribute("project",project);
		Template template = new MysqlTemplate();
		model.addAttribute("template", template);

		return "template/deploy_postgres";
	}
	
	@RequestMapping(value = "/project/{name}/template/mysql", method = RequestMethod.POST)
	public String deployMySqlTemplate(@ModelAttribute("template") MysqlTemplate template, @PathVariable String name, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		Project project = projectService.getProjectByName(name);
		model.addAttribute("project",project);
		templateService.deploy(template, project);
		redirectAttributes.addFlashAttribute("info", "Application deploy successfully");
		return "redirect:/project/" + name + "/overview";
	}
}
