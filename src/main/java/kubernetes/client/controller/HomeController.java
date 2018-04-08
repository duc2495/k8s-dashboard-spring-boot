package kubernetes.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kubernetes.client.service.ProjectService;

@Controller
public class HomeController {
	
	@Autowired
	ProjectService projectService;
	
    @RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
    public String welcomePage(Model model) {
        return "home";
    }
    
    @RequestMapping(value = { "/403", }, method = RequestMethod.GET)
    public String error403(Model model) {
    	model.addAttribute("error", "You are not authorized to access this page.");
        return "403";
    }
}
