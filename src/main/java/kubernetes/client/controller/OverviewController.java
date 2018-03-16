package kubernetes.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kubernetes.client.model.Project;
import kubernetes.client.service.ProjectService;

@Controller
public class OverviewController {
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(value="/project/{name}/overview", method=RequestMethod.GET)
	public String overviewForm(@PathVariable String name, Model model) {
		Project project = projectService.getProjectByName(name);
		if(project == null) {
			return "404";
		}
		model.addAttribute("project", project);
		return "overview/overview";
	}
}
