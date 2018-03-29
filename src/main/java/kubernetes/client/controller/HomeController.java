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
}
