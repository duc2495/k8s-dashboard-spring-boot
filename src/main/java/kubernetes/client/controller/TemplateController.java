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
import kubernetes.client.validator.TemplateValidator;

@Controller
public class TemplateController extends BaseController {
	@Autowired
	private TemplateService templateService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private TemplateValidator templateValitator;

	@RequestMapping(value = "/project/{name}/templates", method = RequestMethod.GET)
	public String listTemplate(@PathVariable String name, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		return "template/template";
	}

	@RequestMapping(value = "/project/{name}/template/postgres/new", method = RequestMethod.GET)
	public String deployPostgresTemplateForm(@PathVariable String name, Model model) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		Template template = new PostgresTemplate();
		model.addAttribute("template", template);

		return "template/deploy_postgres";
	}

	@RequestMapping(value = "/project/{name}/template/postgres", method = RequestMethod.POST)
	public String deployPostgresTemplate(@ModelAttribute("template") PostgresTemplate template,
			@PathVariable String name, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		templateValitator.validate(template, result);
		if (result.hasErrors()) {
			return "template/deploy_postgres";
		}
		if (templateService.exists(template.getName(), name)) {
			result.rejectValue("name", "error.exists", new Object[] { template.getName() },
					"Application " + template.getName() + " already exists");
			return "template/deploy_postgres";
		}
		Project project = projectService.getProjectByName(name);
		templateService.deploy(template, project);
		redirectAttributes.addFlashAttribute("info", "Application deploy successfully");
		return "redirect:/project/" + name + "/overview";
	}

	@RequestMapping(value = "/project/{name}/template/mysql/new", method = RequestMethod.GET)
	public String deployMySqlTemplateForm(@PathVariable String name, Model model) {
		if (!getCurrentUser().getCustomer().getProjects().contains(new Project(name))) {
			return "403";
		}
		model.addAttribute("projectName", name);;
		Template template = new MysqlTemplate();
		model.addAttribute("template", template);

		return "template/deploy_mysql";
	}

	@RequestMapping(value = "/project/{name}/template/mysql", method = RequestMethod.POST)
	public String deployMySqlTemplate(@ModelAttribute("template") MysqlTemplate template, @PathVariable String name,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		if (projectService.getProjectByName(name, getCurrentUser().getCustomer().getId()) == null) {
			return "403";
		}
		model.addAttribute("projectName", name);
		templateValitator.validate(template, result);
		if (result.hasErrors()) {
			return "template/deploy_mysql";
		}
		if (templateService.exists(template.getName(), name)) {
			result.rejectValue("name", "error.exists", new Object[] { name },
					"Application " + name + " already exists");
			return "template/deploy_mysql";
		}
		Project project = projectService.getProjectByName(name);
		templateService.deploy(template, project);
		redirectAttributes.addFlashAttribute("info", "Application deploy successfully");
		return "redirect:/project/" + name + "/overview";
	}
}
